document.addEventListener("DOMContentLoaded", () => {
  // ── Modal open/close ──────────────────────────────────────────────────
  const modal = document.getElementById("user-modal");
  const openBtn = document.getElementById("open-modal-btn");
  const closeBtn = document.getElementById("close-modal-btn");
  const cancelBtn = document.getElementById("cancel-modal-btn");
  const form = document.getElementById("create-user-form");
  let lastFocus = null;

  loadUsers();

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
    if (lastFocus) lastFocus.focus();
  }

  openBtn.addEventListener("click", openModal);
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

    const data = {
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
      role: form.querySelector('input[name="role"]:checked').value,
      status: "Active",
    };

    fetch('/api/user/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
      body: JSON.stringify(data),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Failed to create user");
        }
        return res.json().catch(() => null);
      })
      .then((created) => {
        const userToAdd = created || { ...data, isActive: true };
        appendUserRow(userToAdd);
        showToast(`User ${data.lastName}, ${data.firstName} added successfully.`);
        closeModal();
      })
      .catch((err) => {
        console.error("Create user failed:", err);
      });
  });

  // ── Add row to table ──────────────────────────────────────────────────
  function getAuthHeaders() {
    const token = localStorage.getItem("token");
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  function loadUsers() {
    const tbody = document.getElementById("user-tbody");
    if (!tbody) return;
    tbody.innerHTML = "";

    fetch('/api/user/list/all', {
      method: 'GET',
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
        data.forEach((user) => appendUserRow(user));
      })
      .catch((err) => {
        console.error("Load users failed:", err);
      });
  }

  function appendUserRow(user) {
    const tbody = document.getElementById("user-tbody");
    if (!tbody) return;
    tbody.appendChild(buildUserRow(user));
  }

  function buildUserRow(user) {
    const u = user || {};
    const tr = document.createElement("tr");
    tr.className = "hover:bg-surface-container-low transition-colors";

    const roleBadge =
      {
        Director: "bg-secondary-container text-on-secondary-container",
        Superadmin: "bg-primary-fixed text-on-primary-fixed-variant",
        Admin: "bg-outline-variant text-on-surface-variant",
        Surveyor: "bg-outline-variant text-on-surface-variant",
      }[u.role] || "bg-outline-variant text-on-surface-variant";

    const isActive = u.isActive !== false;
    const statusDot = isActive ? "bg-emerald-500" : "bg-rose-500";
    const statusText = isActive ? "text-emerald-700" : "text-rose-700";
    const statusLabel = isActive ? "Active" : "Inactive";

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
        <span class="${roleBadge} px-sm py-xs rounded text-[10px] font-bold uppercase tracking-wider">${escHtml(u.role)}</span>
      </td>
      <td class="px-lg py-md">
        <div class="flex items-center gap-xs">
          <span class="w-2 h-2 rounded-full ${statusDot} inline-block" aria-hidden="true"></span>
          <span class="text-[12px] font-semibold ${statusText}">${statusLabel}</span>
        </div>
      </td>
      <td class="px-lg py-md text-right">
        <div class="flex justify-end gap-sm">
          <button class="p-2 hover:bg-primary-fixed rounded-lg text-on-surface-variant transition-colors" aria-label="Edit ${escHtml(u.lastName)}, ${escHtml(u.firstName)}">
            <span class="material-symbols-outlined text-[20px]" aria-hidden="true">edit</span>
          </button>
          <button class="p-2 hover:bg-error-container rounded-lg text-error transition-colors" aria-label="Deactivate ${escHtml(u.lastName)}, ${escHtml(u.firstName)}">
            <span class="material-symbols-outlined text-[20px]" aria-hidden="true">block</span>
          </button>
          <button class="p-2 hover:bg-error-container rounded-lg text-error transition-colors" aria-label="Remove ${escHtml(u.lastName)}, ${escHtml(u.firstName)}">
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
