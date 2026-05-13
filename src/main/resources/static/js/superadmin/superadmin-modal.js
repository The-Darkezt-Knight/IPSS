document.addEventListener("DOMContentLoaded", () => {
  // ── Modal open/close ──────────────────────────────────────────────────
  const modal = document.getElementById("user-modal");
  const openBtn = document.getElementById("open-modal-btn");
  const closeBtn = document.getElementById("close-modal-btn");
  const cancelBtn = document.getElementById("cancel-modal-btn");
  const form = document.getElementById("create-user-form");
  const modalTitle = document.getElementById("modal-title");
  const modalSubtitle = modal ? modal.querySelector("#modal-title + p") : null;
  const tbody = document.getElementById("user-tbody");
  const govtEmailInput = document.getElementById("gov-email");
  const govtIdInput = document.getElementById("gov-id");
  const searchInput = document.getElementById("search-input");
  const roleFilterSelect = document.getElementById("filter-role");
  const statusFilterSelect = document.getElementById("filter-status");
  const userCache = new Map();
  let lastFocus = null;
  let editingUserId = null;
  let searchTerm = "";
  let roleFilter = roleFilterSelect ? roleFilterSelect.value : "All Roles";
  let statusFilter = statusFilterSelect ? statusFilterSelect.value : "All Status";

  setCreateMode();

  loadUsers();

  if (tbody) {
    tbody.addEventListener("click", handleRowAction);
  }

  if (searchInput) {
    searchInput.addEventListener("input", () => {
      searchTerm = searchInput.value.trim().toLowerCase();
      renderUsers();
    });
  }

  if (roleFilterSelect) {
    roleFilterSelect.addEventListener("change", () => {
      roleFilter = roleFilterSelect.value;
      renderUsers();
    });
  }

  if (statusFilterSelect) {
    statusFilterSelect.addEventListener("change", () => {
      statusFilter = statusFilterSelect.value;
      renderUsers();
    });
  }

  function openModal() {
    lastFocus = document.activeElement;
    modal.classList.remove("hidden");
    document.body.style.overflow = "hidden";
    // Move focus to first input
    setTimeout(() => {
      const first = modal.querySelector("input, select, button");
      if (first) first.focus();
    }, 50);
  }

  function closeModal() {
    modal.classList.add("hidden");
    document.body.style.overflow = "";
    form.reset();
    clearErrors();
    setCreateMode();
    if (lastFocus) lastFocus.focus();
  }

  openBtn.addEventListener("click", () => {
    setCreateMode();
    openModal();
  });
  closeBtn.addEventListener("click", closeModal);
  cancelBtn.addEventListener("click", closeModal);

  // Close on backdrop click
  modal.addEventListener("click", (e) => {
    if (e.target === modal) closeModal();
  });

  // Close on Escape
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape" && !modal.classList.contains("hidden")) closeModal();
  });

  // Trap focus inside modal
  modal.addEventListener("keydown", (e) => {
    if (e.key !== "Tab") return;
    const focusable = [
      ...modal.querySelectorAll(
        'button, input, select, textarea, a[href], [tabindex]:not([tabindex="-1"])',
      ),
    ].filter((el) => !el.disabled && el.offsetParent !== null);
    const first = focusable[0];
    const last = focusable[focusable.length - 1];
    if (e.shiftKey) {
      if (document.activeElement === first) {
        last.focus();
        e.preventDefault();
      }
    } else {
      if (document.activeElement === last) {
        first.focus();
        e.preventDefault();
      }
    }
  });

  // ── Validation ────────────────────────────────────────────────────────
  const requiredFields = [
    { id: "first-name", errId: "first-name-err", msg: "First name is required."},
    { id: "last-name", errId: "last-name-err", msg: "Last name is required." },
    { id: "birthdate", errId: "birthdate-err", msg: "Birthdate is required." },
    { id: "sex", errId: "sex-err", msg: "Please select a sex." },
    { id: "barangay", errId: "barangay-err", msg: "Barangay is required." },
    { id: "city", errId: "city-err", msg: "City/Municipality is required." },
    { id: "province", errId: "province-err", msg: "Province is required." },
    {
      id: "gov-email",
      errId: "gov-email-err",
      msg: "A valid government email is required.",
      type: "email",
    },
    {
      id: "gov-id",
      errId: "gov-id-err",
      msg: "ID must follow format GOV-YYYY-XXXX.",
      //pattern: /^GOV-\d{4}-\d{4}$/,
      //Govt ID must be changed accordingly
    },
  ];

  function clearErrors() {
    requiredFields.forEach(({ id, errId }) => {
      const el = document.getElementById(id);
      const err = document.getElementById(errId);
      if (el) el.classList.remove("field-error");
      if (err) err.classList.remove("show");
    });
    const roleErr = document.getElementById("role-err");
    if (roleErr) roleErr.classList.remove("show");
  }

  const roleLabels = {
    ROLE_DIRECTOR: "Director",
    ROLE_SUPERADMIN: "Superadmin",
    ROLE_ADMIN: "Admin",
    ROLE_SURVEYOR: "Surveyor",
  };

  const roleBadges = {
    ROLE_DIRECTOR: "bg-secondary-container text-on-secondary-container",
    ROLE_SUPERADMIN: "bg-primary-fixed text-on-primary-fixed-variant",
    ROLE_ADMIN: "bg-outline-variant text-on-surface-variant",
    ROLE_SURVEYOR: "bg-outline-variant text-on-surface-variant",
  };

  function normalizeRoleValue(role) {
    if (!role) return "";
    const raw = String(role).trim();
    if (roleLabels[raw]) return raw;
    const upper = raw.toUpperCase();
    if (roleLabels[upper]) return upper;
    const guess = `ROLE_${upper.replace(/[^A-Z0-9]/g, "")}`;
    return roleLabels[guess] ? guess : raw;
  }

  function getRoleLabel(role) {
    const roleValue = normalizeRoleValue(role);
    return roleLabels[roleValue] || String(role || "");
  }

  function normalizeUser(user) {
    if (!user) return null;
    return {
      ...user,
      role: normalizeRoleValue(user.role),
      govtEmail: user.govtEmail || "",
      govtId: user.govtId ?? "",
      isActive: user.isActive !== false,
    };
  }

  function upsertUser(user) {
    if (!user || user.id == null) return null;
    const normalized = normalizeUser(user);
    userCache.set(String(normalized.id), normalized);
    return normalized;
  }

  function setCreateMode() {
    editingUserId = null;
    form.dataset.mode = "create";
    if (modalTitle) modalTitle.textContent = "Create New User Profile";
    if (modalSubtitle) {
      modalSubtitle.textContent = "Fill in all required fields to register a new government user.";
    }
    if (govtEmailInput) govtEmailInput.readOnly = false;
    if (govtIdInput) govtIdInput.readOnly = false;
  }

  function setEditMode(user) {
    const normalized = normalizeUser(user);
    if (!normalized || normalized.id == null) return;
    editingUserId = String(normalized.id);
    form.dataset.mode = "edit";
    form.reset();
    clearErrors();
    if (modalTitle) modalTitle.textContent = "Edit User Profile";
    if (modalSubtitle) {
      modalSubtitle.textContent = "Update the user's information and save your changes.";
    }
    if (govtEmailInput) govtEmailInput.readOnly = true;
    if (govtIdInput) govtIdInput.readOnly = true;
    fillFormFromUser(normalized);
  }

  function fillFormFromUser(user) {
    const u = user || {};
    const roleValue = normalizeRoleValue(u.role);
    const roleInput = form.querySelector(`input[name="role"][value="${roleValue}"]`);

    document.getElementById("first-name").value = u.firstName || "";
    document.getElementById("middle-name").value = u.middleName || "";
    document.getElementById("last-name").value = u.lastName || "";
    document.getElementById("birthdate").value = u.birthDate || "";
    document.getElementById("sex").value = u.sex || "";
    document.getElementById("barangay").value = u.baranggay || "";
    document.getElementById("city").value = u.cityMunicipality || "";
    document.getElementById("province").value = u.province || "";
    if (govtEmailInput) govtEmailInput.value = u.govtEmail || "";
    if (govtIdInput) govtIdInput.value = u.govtId != null ? String(u.govtId) : "";
    if (roleInput) roleInput.checked = true;
  }

  function getFormPayload() {
    return {
      firstName: document.getElementById("first-name").value.trim(),
      middleName: document.getElementById("middle-name").value.trim(),
      lastName: document.getElementById("last-name").value.trim(),
      birthDate: document.getElementById("birthdate").value,
      sex: document.getElementById("sex").value,
      baranggay: document.getElementById("barangay").value.trim(),
      cityMunicipality: document.getElementById("city").value.trim(),
      province: document.getElementById("province").value.trim(),
      govtEmail: document.getElementById("gov-email").value.trim(),
      govtId: document.getElementById("gov-id").value.trim(),
      role: form.querySelector('input[name="role"]:checked')?.value || "",
    };
  }

  function matchesRole(user, filterValue) {
    const filter = String(filterValue || "").trim().toLowerCase();
    if (!filter || filter === "all roles") return true;
    const roleLabel = getRoleLabel(user.role).toLowerCase();
    return roleLabel === filter;
  }

  function matchesStatus(user, filterValue) {
    const filter = String(filterValue || "").trim().toLowerCase();
    if (!filter || filter === "all status") return true;
    const isActive = user.isActive !== false;
    return filter === "active" ? isActive : !isActive;
  }

  function matchesSearch(user, termValue) {
    const term = String(termValue || "").trim().toLowerCase();
    if (!term) return true;
    const u = user || {};
    const roleLabel = getRoleLabel(u.role);
    const haystack = [
      u.firstName,
      u.middleName,
      u.lastName,
      `${u.lastName || ""}, ${u.firstName || ""}`.trim(),
      u.govtEmail,
      u.govtId,
      u.id,
      roleLabel,
    ]
      .filter((value) => value != null && String(value).trim() !== "")
      .map((value) => String(value).toLowerCase());

    return haystack.some((value) => value.includes(term));
  }

  function renderUsers() {
    if (!tbody) return;
    tbody.innerHTML = "";
    Array.from(userCache.values())
      .filter(
        (user) =>
          matchesRole(user, roleFilter) &&
          matchesStatus(user, statusFilter) &&
          matchesSearch(user, searchTerm),
      )
      .forEach((user) => tbody.appendChild(buildUserRow(user)));
  }

  function handleRowAction(event) {
    const btn = event.target.closest("button[data-action]");
    if (!btn || !tbody || !tbody.contains(btn)) return;

    const row = btn.closest("tr");
    const userId = row ? row.dataset.userId : null;
    if (!userId) return;

    switch (btn.dataset.action) {
      case "edit":
        handleEdit(userId);
        break;
      case "deactivate":
        handleToggleStatus(userId);
        break;
      case "remove":
        handleRemove(userId);
        break;
      default:
        break;
    }
  }

  function handleEdit(userId) {
    const user = userCache.get(String(userId));
    if (!user) {
      showToast("User details are not available in cache.");
      return;
    }
    setEditMode(user);
    openModal();
  }

  function handleToggleStatus(userId) {
    const cached = userCache.get(String(userId));
    if (!cached) {
      showToast("User details are not available in cache.");
      return;
    }

    const idValue = Number(userId);
    const payload = { id: Number.isNaN(idValue) ? userId : idValue };

    fetch("/api/user/set/status", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...getAuthHeaders(),
      },
      body: JSON.stringify(payload),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Failed to update user status");
        }
        return res.text();
      })
      .then(() => {
        const updated = { ...cached, isActive: !cached.isActive };
        upsertUser(updated);
        renderUsers();
        showToast("User status updated successfully.");
      })
      .catch((err) => {
        console.error("Update status failed:", err);
      });
  }

  function handleRemove(userId) {
    const cached = userCache.get(String(userId));
    const name = cached
      ? `${cached.lastName}, ${cached.firstName}`
      : "this user";
    if (!window.confirm(`Remove ${name}? This cannot be undone.`)) return;

    const idValue = Number(userId);
    const payload = { id: Number.isNaN(idValue) ? userId : idValue };

    fetch("/api/user/remove", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...getAuthHeaders(),
      },
      body: JSON.stringify(payload),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Failed to remove user");
        }
        return res.text();
      })
      .then(() => {
        userCache.delete(String(userId));
        renderUsers();
        showToast(`User ${name} removed successfully.`);
      })
      .catch((err) => {
        console.error("Remove user failed:", err);
      });
  }

  function validateForm() {
    let valid = true;
    clearErrors();

    requiredFields.forEach(({ id, errId, msg, type, pattern }) => {
      const el = document.getElementById(id);
      const err = document.getElementById(errId);
      if (!el) return;

      const val = el.value.trim();
      let fieldOk = val !== "";
      if (fieldOk && type === "email") {
        fieldOk = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val);
      }
      if (fieldOk && pattern) {
        fieldOk = pattern.test(val);
      }
      if (!fieldOk) {
        el.classList.add("field-error");
        if (err) {
          err.textContent = msg;
          err.classList.add("show");
        }
        valid = false;
      }
    });

    // Check role radio
    const roleSelected = form.querySelector('input[name="role"]:checked');
    if (!roleSelected) {
      const roleErr = document.getElementById("role-err");
      if (roleErr) roleErr.classList.add("show");
      valid = false;
    }

    return valid;
  }

  // Inline validation on blur
  requiredFields.forEach(({ id, errId, msg, type, pattern }) => {
    const el = document.getElementById(id);

    if (!el) return;
    el.addEventListener("blur", () => {
      const val = el.value.trim();
      let ok = val !== "";
      if (ok && type === "email") ok = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val);
      if (ok && pattern) ok = pattern.test(val);
      const err = document.getElementById(errId);
      el.classList.toggle("field-error", !ok);
      if (err) err.classList.toggle("show", !ok);
    });

    el.addEventListener("input", () => {
      if (el.classList.contains("field-error")) {
        el.classList.remove("field-error");
        const err = document.getElementById(errId);
        if (err) err.classList.remove("show");
      }
    });
  });


  // ── Form submit ───────────────────────────────────────────────────────
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    if (!validateForm()) return;
    const payload = getFormPayload();
    const isEdit = form.dataset.mode === "edit" && editingUserId;
    const endpoint = isEdit ? "/api/user/update" : "/api/user/create";
    const body = isEdit ? payload : { ...payload, status: "Active" };

    fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...getAuthHeaders(),
      },
      body: JSON.stringify(body),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error(isEdit ? "Failed to update user" : "Failed to create user");
        }
        return isEdit ? res.text() : res.json().catch(() => null);
      })
      .then((created) => {
        if (isEdit) {
          const cached = userCache.get(String(editingUserId)) || {};
          const updated = {
            ...cached,
            ...payload,
            role: normalizeRoleValue(payload.role),
          };
          upsertUser(updated);
          renderUsers();
          showToast(`User ${payload.lastName}, ${payload.firstName} updated successfully.`);
          closeModal();
          return;
        }

        if (created && created.id != null) {
          appendUserRow(created);
        } else {
          loadUsers();
        }
        showToast(`User ${payload.lastName}, ${payload.firstName} added successfully.`);
        closeModal();
      })
      .catch((err) => {
        console.error(isEdit ? "Update user failed:" : "Create user failed:", err);
      });
  });

  // ── Add row to table ──────────────────────────────────────────────────
  function getAuthHeaders() {
    const token = localStorage.getItem("token");
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  function loadUsers() {
    if (!tbody) return;
    tbody.innerHTML = "";
    userCache.clear();

    fetch("/api/user/list/all", {
      method: "GET",
      headers: {
        ...getAuthHeaders(),
      },
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Failed to load users");
        }
        return res.json();
      })
      .then((data) => {
        if (!Array.isArray(data)) return;
        data.forEach((user) => upsertUser(user));
        renderUsers();
      })
      .catch((err) => {
        console.error("Load users failed:", err);
      });
  }

  function appendUserRow(user) {
    if (!tbody || !user || user.id == null) return;
    upsertUser(user);
    renderUsers();
  }

  function buildUserRow(user) {
    const u = user || {};
    const tr = document.createElement("tr");
    tr.className = "hover:bg-surface-container-low transition-colors";
    if (u.id != null) {
      tr.dataset.userId = String(u.id);
    }

    const roleValue = normalizeRoleValue(u.role);
    const roleLabel = getRoleLabel(roleValue);
    const roleBadge = roleBadges[roleValue] || "bg-outline-variant text-on-surface-variant";

    const isActive = u.isActive !== false;
    const statusDot = isActive ? "bg-emerald-500" : "bg-rose-500";
    const statusText = isActive ? "text-emerald-700" : "text-rose-700";
    const statusLabel = isActive ? "Active" : "Inactive";
    const statusActionLabel = isActive ? "Deactivate" : "Activate";
    const statusIcon = isActive ? "block" : "check_circle";
    const statusButtonClass = isActive
      ? "p-2 hover:bg-error-container rounded-lg text-error transition-colors"
      : "p-2 hover:bg-secondary-container rounded-lg text-emerald-700 transition-colors";

    tr.innerHTML = `
      <td class="px-lg py-md">
        <div class="flex flex-col">
          <span class="text-[14px] font-semibold text-on-surface">${escHtml(u.lastName)}, ${escHtml(u.firstName)}</span>
          ${u.middleName ? `<span class="text-[13px] text-outline">Middle: ${escHtml(u.middleName)}</span>` : ""}
        </div>
      </td>
      <td class="px-lg py-md text-[14px]">${escHtml(u.govtEmail)}</td>
      <td class="px-lg py-md text-[14px]">${escHtml(u.govtId)}</td>
      <td class="px-lg py-md">
        <span class="${roleBadge} px-sm py-xs rounded text-[10px] font-bold uppercase tracking-wider">${escHtml(roleLabel)}</span>
      </td>
      <td class="px-lg py-md">
        <div class="flex items-center gap-xs">
          <span class="w-2 h-2 rounded-full ${statusDot} inline-block" aria-hidden="true"></span>
          <span class="text-[12px] font-semibold ${statusText}">${statusLabel}</span>
        </div>
      </td>
      <td class="px-lg py-md text-right">
        <div class="flex justify-end gap-sm">
          <button data-action="edit" class="p-2 hover:bg-primary-fixed rounded-lg text-on-surface-variant transition-colors" aria-label="Edit ${escHtml(u.lastName)}, ${escHtml(u.firstName)}">
            <span class="material-symbols-outlined text-[20px]" aria-hidden="true">edit</span>
          </button>
          <button data-action="deactivate" class="${statusButtonClass}" aria-label="${statusActionLabel} ${escHtml(u.lastName)}, ${escHtml(u.firstName)}">
            <span class="material-symbols-outlined text-[20px]" aria-hidden="true">${statusIcon}</span>
          </button>
          <button data-action="remove" class="p-2 hover:bg-error-container rounded-lg text-error transition-colors" aria-label="Remove ${escHtml(u.lastName)}, ${escHtml(u.firstName)}">
            <span class="material-symbols-outlined text-[20px]" aria-hidden="true">delete</span>
          </button>
        </div>
      </td>
    `;

    return tr;
  }

  function escHtml(value) {
    const str = value == null ? "" : String(value);
    return str.replace(
      /[&<>"']/g,
      (m) =>
        ({
          "&": "&amp;",
          "<": "&lt;",
          ">": "&gt;",
          '"': "&quot;",
          "'": "&#39;",
        })[m],
    );
  }

  // ── Toast ─────────────────────────────────────────────────────────────
  function showToast(msg) {
    const t = document.getElementById("toast");
    t.textContent = msg;
    t.classList.add("show");
    setTimeout(() => t.classList.remove("show"), 3500);
  }
});
