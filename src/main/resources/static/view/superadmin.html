<!DOCTYPE html>
<html class="light" lang="en">
<head>
<meta charset="utf-8"/>
<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
<title>User Management | City Governance Portal</title>
<script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
<link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet"/>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap" rel="stylesheet"/>
<script src="/js/superadmin/superadmin-modal.js"></script>
<style>
  body { font-family: 'Public Sans', sans-serif; }
  .material-symbols-outlined {
    font-variation-settings: 'FILL' 0, 'wght' 400, 'GRAD' 0, 'opsz' 24;
    vertical-align: middle;
  }

  /* Modal animation */
  #user-modal {
    transition: opacity 0.2s ease;
  }
  #user-modal.hidden { opacity: 0; pointer-events: none; }
  #modal-panel {
    transition: transform 0.25s cubic-bezier(0.16, 1, 0.3, 1), opacity 0.2s ease;
  }
  #user-modal.hidden #modal-panel { transform: translateY(16px) scale(0.98); opacity: 0; }
  #user-modal:not(.hidden) #modal-panel { transform: translateY(0) scale(1); opacity: 1; }

  /* Focus ring */
  input:focus, select:focus, textarea:focus {
    outline: none;
    border-color: #3a5f94;
    box-shadow: 0 0 0 3px rgba(58, 95, 148, 0.12);
  }

  /* Table row hover */
  tbody tr { transition: background 0.12s; }

  /* Custom scrollbar for modal */
  .modal-scroll::-webkit-scrollbar { width: 6px; }
  .modal-scroll::-webkit-scrollbar-track { background: transparent; }
  .modal-scroll::-webkit-scrollbar-thumb { background: #c3c6d1; border-radius: 3px; }

  /* Step indicator */
  .step-dot {
    width: 8px; height: 8px; border-radius: 50%;
    background: #c3c6d1; transition: background 0.2s, transform 0.2s;
  }
  .step-dot.active { background: #3a5f94; transform: scale(1.3); }

  /* Field error state */
  .field-error { border-color: #ba1a1a !important; }
  .error-msg { color: #ba1a1a; font-size: 11px; margin-top: 2px; display: none; }
  .field-error + .error-msg, .error-msg.show { display: block; }

  /* Toast */
  #toast {
    position: fixed; bottom: 32px; left: 50%; transform: translateX(-50%) translateY(20px);
    background: #1a1c1e; color: #fff; padding: 12px 24px; border-radius: 8px;
    font-size: 14px; font-weight: 500; z-index: 9999;
    opacity: 0; transition: opacity 0.3s, transform 0.3s; pointer-events: none;
    white-space: nowrap;
  }
  #toast.show { opacity: 1; transform: translateX(-50%) translateY(0); }
</style>
<script id="tailwind-config">
  tailwind.config = {
    darkMode: "class",
    theme: {
      extend: {
        colors: {
          "on-surface": "#1a1c1e",
          "on-primary-container": "#799dd6",
          "on-primary": "#ffffff",
          "outline": "#737780",
          "tertiary": "#191f22",
          "surface-container": "#eeeef0",
          "on-tertiary-fixed": "#161c1f",
          "inverse-surface": "#2f3133",
          "on-background": "#1a1c1e",
          "on-error": "#ffffff",
          "primary-fixed": "#d5e3ff",
          "secondary": "#4a6077",
          "secondary-fixed": "#cee5ff",
          "tertiary-fixed": "#dde3e7",
          "inverse-on-surface": "#f0f0f3",
          "surface-container-high": "#e8e8ea",
          "surface": "#f9f9fc",
          "on-tertiary-container": "#969ca0",
          "surface-variant": "#e2e2e5",
          "surface-container-lowest": "#ffffff",
          "outline-variant": "#c3c6d1",
          "on-tertiary-fixed-variant": "#41484b",
          "primary-fixed-dim": "#a7c8ff",
          "secondary-container": "#cbe2fc",
          "on-secondary-fixed-variant": "#33495e",
          "on-primary-fixed-variant": "#1f477b",
          "tertiary-fixed-dim": "#c1c7cb",
          "primary": "#001e40",
          "error-container": "#ffdad6",
          "primary-container": "#003366",
          "surface-container-highest": "#e2e2e5",
          "surface-tint": "#3a5f94",
          "inverse-primary": "#a7c8ff",
          "background": "#f9f9fc",
          "on-tertiary": "#ffffff",
          "on-surface-variant": "#43474f",
          "on-secondary-container": "#4f657b",
          "surface-bright": "#f9f9fc",
          "on-primary-fixed": "#001b3c",
          "secondary-fixed-dim": "#b2c9e2",
          "on-error-container": "#93000a",
          "error": "#ba1a1a",
          "surface-dim": "#dadadc",
          "surface-container-low": "#f3f3f6",
          "tertiary-container": "#2e3437",
          "on-secondary-fixed": "#041d30",
          "on-secondary": "#ffffff"
        },
        borderRadius: {
          DEFAULT: "0.125rem", lg: "0.25rem", xl: "0.5rem", full: "0.75rem"
        },
        spacing: {
          gutter: "24px", unit: "4px", xs: "4px", xxl: "48px",
          sm: "8px", lg: "24px", xl: "32px", "margin-mobile": "16px",
          md: "16px", "container-max": "1280px"
        },
        fontFamily: {
          "label-md": ["Public Sans"], "headline-sm": ["Public Sans"],
          "headline-lg": ["Public Sans"], "body-sm": ["Public Sans"],
          "display-lg": ["Public Sans"], "body-md": ["Public Sans"],
          "body-lg": ["Public Sans"], "headline-lg-mobile": ["Public Sans"],
          "label-lg": ["Public Sans"], "headline-md": ["Public Sans"]
        },
        fontSize: {
          "label-md": ["12px", { lineHeight: "16px", letterSpacing: "0.02em", fontWeight: "600" }],
          "headline-sm": ["20px", { lineHeight: "28px", fontWeight: "600" }],
          "headline-lg": ["32px", { lineHeight: "40px", fontWeight: "700" }],
          "body-sm": ["14px", { lineHeight: "20px", fontWeight: "400" }],
          "display-lg": ["48px", { lineHeight: "56px", letterSpacing: "-0.02em", fontWeight: "700" }],
          "body-md": ["16px", { lineHeight: "24px", fontWeight: "400" }],
          "body-lg": ["18px", { lineHeight: "28px", fontWeight: "400" }],
          "headline-lg-mobile": ["24px", { lineHeight: "32px", fontWeight: "700" }],
          "label-lg": ["14px", { lineHeight: "20px", letterSpacing: "0.01em", fontWeight: "600" }],
          "headline-md": ["24px", { lineHeight: "32px", fontWeight: "600" }]
        }
      }
    }
  }
</script>
</head>
<body class="bg-background text-on-surface flex flex-col min-h-screen">

<!-- ═══════════════════════════════ HEADER ═══════════════════════════════ -->
<header class="bg-surface border-b border-outline-variant/15 z-50 sticky top-0">
  <div class="flex justify-between items-center w-full px-gutter max-w-[1280px] mx-auto h-16">
    <div class="flex items-center gap-lg">
      <span class="font-bold text-[22px] leading-tight text-primary tracking-tight">City Governance Portal</span>
      <nav class="hidden md:flex items-center gap-md ml-xl" aria-label="Main navigation">
        <a class="text-[12px] font-semibold tracking-wide text-on-surface-variant hover:text-primary transition-colors" href="#">Dashboard</a>
        <a class="text-[12px] font-semibold tracking-wide text-primary border-b-2 border-primary pb-[1px]" href="#" aria-current="page">User Management</a>
        <a class="text-[12px] font-semibold tracking-wide text-on-surface-variant hover:text-primary transition-colors" href="#">Reports</a>
      </nav>
    </div>
    <div class="flex items-center gap-md">
      <button class="text-[12px] font-semibold text-primary hover:text-primary/80 transition-colors">Help Center</button>
      <div class="w-8 h-8 rounded-full bg-secondary-container flex items-center justify-center text-on-secondary-container font-bold text-xs" aria-label="Signed in as SA" title="System Administrator">SA</div>
    </div>
  </div>
</header>

<!-- ═══════════════════════════════ MAIN ═══════════════════════════════ -->
<main class="flex-grow w-full max-w-[1280px] mx-auto px-gutter py-xl">

  <!-- Page Header & CTA -->
  <div class="flex flex-col md:flex-row justify-between items-start md:items-center gap-md mb-xl">
    <div>
      <h1 class="text-[32px] font-bold leading-10 text-primary">User Management</h1>
      <p class="text-[16px] text-on-surface-variant mt-1">Administer administrative roles and city personnel access.</p>
    </div>
    <button
      id="open-modal-btn"
      class="bg-primary text-on-primary px-lg py-sm rounded-lg text-[14px] font-semibold flex items-center gap-xs hover:bg-primary/90 transition-all shadow-sm focus-visible:ring-2 focus-visible:ring-primary focus-visible:ring-offset-2"
      aria-haspopup="dialog"
      aria-controls="user-modal"
    >
      <span class="material-symbols-outlined text-[18px]">add_circle</span>
      Create New User
    </button>
  </div>

  <!-- Search & Filter Bar -->
  <div class="bg-surface-container-lowest border border-outline-variant/30 rounded-xl p-md mb-lg flex flex-col md:flex-row gap-md items-center" role="search">
    <div class="relative w-full md:w-96">
      <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-outline text-[20px]" aria-hidden="true">search</span>
      <input
        id='search-input'
        class="w-full pl-10 pr-md py-sm border border-outline rounded-lg bg-surface text-[14px] focus:ring-2 focus:ring-primary focus:border-primary outline-none"
        placeholder="Find a user by name, email, or ID…"
        type="search"
        aria-label="Search users"
      />
    </div>
    <div class="flex items-center gap-sm w-full md:w-auto">
      <label class="sr-only" for="filter-role">Filter by role</label>
      <select id="filter-role" class="px-md py-sm border border-outline rounded-lg bg-surface text-[12px] font-semibold focus:ring-2 focus:ring-primary outline-none text-on-surface-variant">
        <option>All Roles</option>
        <option>Director</option>
        <option>Superadmin</option>
        <option>Admin</option>
        <option>Surveyor</option>
      </select>
      <label class="sr-only" for="filter-status">Filter by status</label>
      <select id="filter-status" class="px-md py-sm border border-outline rounded-lg bg-surface text-[12px] font-semibold focus:ring-2 focus:ring-primary outline-none text-on-surface-variant">
        <option>All Status</option>
        <option>Active</option>
        <option>Inactive</option>
      </select>
      <button class="text-on-surface-variant hover:text-primary transition-colors flex items-center gap-xs ml-auto">
        <span class="material-symbols-outlined text-[20px]" aria-hidden="true">filter_list</span>
        <span class="text-[12px] font-semibold">Filters</span>
      </button>
    </div>
  </div>

  <!-- Users Table -->
  <div class="bg-surface-container-lowest border border-outline-variant/30 rounded-xl overflow-hidden">
    <div class="overflow-x-auto">
      <table class="w-full text-left border-collapse" aria-label="Government users list">
        <thead class="bg-surface-container-high border-b border-outline-variant/30">
          <tr>
            <th scope="col" class="px-lg py-md text-[14px] font-semibold text-primary whitespace-nowrap">Name</th>
            <th scope="col" class="px-lg py-md text-[14px] font-semibold text-primary whitespace-nowrap">Government Email</th>
            <th scope="col" class="px-lg py-md text-[14px] font-semibold text-primary whitespace-nowrap">Government ID</th>
            <th scope="col" class="px-lg py-md text-[14px] font-semibold text-primary whitespace-nowrap">Role</th>
            <th scope="col" class="px-lg py-md text-[14px] font-semibold text-primary whitespace-nowrap">Status</th>
            <th scope="col" class="px-lg py-md text-[14px] font-semibold text-primary text-right whitespace-nowrap">Actions</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-outline-variant/15" id="user-tbody">
            <!--Lists of user goes here-->
        </tbody>
      </table>
    </div>

    <!-- Pagination
    <div class="px-lg py-md border-t border-outline-variant/30 flex justify-between items-center bg-surface" role="navigation" aria-label="Table pagination">
      <p class="text-[14px] text-on-surface-variant" id="pagination-info">Showing 1 to 2 of 24 users</p>
      <div class="flex gap-xs">
        <button class="p-2 border border-outline-variant rounded hover:bg-surface-variant transition-all disabled:opacity-30" disabled aria-label="Previous page">
          <span class="material-symbols-outlined text-[20px]" aria-hidden="true">chevron_left</span>
        </button>
        <button class="w-10 h-10 bg-primary text-on-primary rounded text-[12px] font-bold" aria-current="page" aria-label="Page 1">1</button>
        <button class="w-10 h-10 border border-outline-variant rounded text-[12px] font-semibold hover:bg-surface-variant" aria-label="Page 2">2</button>
        <button class="w-10 h-10 border border-outline-variant rounded text-[12px] font-semibold hover:bg-surface-variant" aria-label="Page 3">3</button>
        <button class="p-2 border border-outline-variant rounded hover:bg-surface-variant transition-all" aria-label="Next page">
          <span class="material-symbols-outlined text-[20px]" aria-hidden="true">chevron_right</span>
        </button>
      </div>
    </div>
    -->

  </div>
</main>

<!-- ═══════════════════════════════ MODAL ═══════════════════════════════ -->
<div
  id="user-modal"
  class="hidden fixed inset-0 z-[60] bg-on-surface/40 backdrop-blur-sm flex items-start justify-center p-md pt-8 overflow-y-auto"
  role="dialog"
  aria-modal="true"
  aria-labelledby="modal-title"
>
  <div id="modal-panel" class="bg-surface-container-lowest w-full max-w-3xl rounded-xl shadow-2xl overflow-hidden mb-8">

    <!-- Modal Header -->
    <div class="bg-primary text-on-primary px-lg py-md flex justify-between items-center">
      <div>
        <h2 id="modal-title" class="text-[20px] font-semibold">Create New User Profile</h2>
        <p class="text-[13px] text-on-primary/70 mt-0.5">Fill in all required fields to register a new government user.</p>
      </div>
      <button
        id="close-modal-btn"
        class="hover:rotate-90 transition-transform p-1 rounded-lg hover:bg-on-primary/10 focus-visible:ring-2 focus-visible:ring-white"
        aria-label="Close modal"
      >
        <span class="material-symbols-outlined" aria-hidden="true">close</span>
      </button>
    </div>

    <!-- Form -->
    <div class="p-lg modal-scroll overflow-y-auto max-h-[calc(100vh-200px)]">
      <form id="create-user-form" novalidate>

        <!-- Section 1: Personal Information -->
        <fieldset class="mb-xl">
          <legend class="w-full">
            <div class="flex items-center gap-sm mb-md pb-xs border-b border-outline-variant">
              <span class="w-6 h-6 rounded-full bg-primary text-on-primary flex items-center justify-center text-[11px] font-bold flex-shrink-0">1</span>
              <span class="text-[14px] font-semibold text-primary tracking-wide uppercase">Personal Information</span>
            </div>
          </legend>

          <div class="grid grid-cols-1 md:grid-cols-3 gap-md">
            <!-- First Name -->
            <div class="flex flex-col gap-xs">
              <label class="text-[12px] font-semibold text-on-surface-variant" for="first-name">
                First Name <span class="text-error" aria-hidden="true">*</span>
              </label>
              <input
                id="first-name" name="firstName" type="text" required autocomplete="given-name"
                placeholder="e.g. Maria Clara"
                class="border border-outline rounded-lg px-md py-sm bg-surface text-[14px] outline-none transition-all"
                aria-required="true" aria-describedby="first-name-err"
              />
              <span id="first-name-err" class="error-msg" role="alert">First name is required.</span>
            </div>

            <!-- Middle Name -->
            <div class="flex flex-col gap-xs">
              <label class="text-[12px] font-semibold text-on-surface-variant" for="middle-name">
                Middle Name <span class="text-outline font-normal">(optional)</span>
              </label>
              <input
                id="middle-name" name="middleName" type="text" autocomplete="additional-name"
                placeholder="e.g. Dela Cruz"
                class="border border-outline rounded-lg px-md py-sm bg-surface text-[14px] outline-none transition-all"
              />
            </div>

            <!-- Last Name -->
            <div class="flex flex-col gap-xs">
              <label class="text-[12px] font-semibold text-on-surface-variant" for="last-name">
                Last Name <span class="text-error" aria-hidden="true">*</span>
              </label>
              <input
                id="last-name" name="lastName" type="text" required autocomplete="family-name"
                placeholder="e.g. Santos"
                class="border border-outline rounded-lg px-md py-sm bg-surface text-[14px] outline-none transition-all"
                aria-required="true" aria-describedby="last-name-err"
              />
              <span id="last-name-err" class="error-msg" role="alert">Last name is required.</span>
            </div>

            <!-- Birthdate -->
            <div class="flex flex-col gap-xs">
              <label class="text-[12px] font-semibold text-on-surface-variant" for="birthdate">
                Birthdate <span class="text-error" aria-hidden="true">*</span>
              </label>
              <input
                id="birthdate" name="birthdate" type="date" required autocomplete="bday"
                class="border border-outline rounded-lg px-md py-sm bg-surface text-[14px] outline-none transition-all"
                aria-required="true" aria-describedby="birthdate-err"
              />
              <span id="birthdate-err" class="error-msg" role="alert">Birthdate is required.</span>
            </div>

            <!-- Sex -->
            <div class="flex flex-col gap-xs">
              <label class="text-[12px] font-semibold text-on-surface-variant" for="sex">
                Sex <span class="text-error" aria-hidden="true">*</span>
              </label>
              <select
                id="sex" name="sex" required
                class="border border-outline rounded-lg px-md py-sm bg-surface text-[14px] outline-none transition-all"
                aria-required="true" aria-describedby="sex-err"
              >
                <option value="">Select…</option>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
              </select>
              <span id="sex-err" class="error-msg" role="alert">Please select a sex.</span>
            </div>
          </div>
        </fieldset>

        <!-- Section 2: Locality Details -->
        <fieldset class="mb-xl">
          <legend class="w-full">
            <div class="flex items-center gap-sm mb-md pb-xs border-b border-outline-variant">
              <span class="w-6 h-6 rounded-full bg-primary text-on-primary flex items-center justify-center text-[11px] font-bold flex-shrink-0">2</span>
              <span class="text-[14px] font-semibold text-primary tracking-wide uppercase">Locality Details</span>
            </div>
          </legend>

          <div class="grid grid-cols-1 md:grid-cols-3 gap-md">
            <!-- Barangay -->
            <div class="flex flex-col gap-xs">
              <label class="text-[12px] font-semibold text-on-surface-variant" for="barangay">
                Barangay <span class="text-error" aria-hidden="true">*</span>
              </label>
              <input
                id="barangay" name="barangay" type="text" required
                placeholder="e.g. Lahug"
                class="border border-outline rounded-lg px-md py-sm bg-surface text-[14px] outline-none transition-all"
                aria-required="true" aria-describedby="barangay-err"
              />
              <span id="barangay-err" class="error-msg" role="alert">Barangay is required.</span>
            </div>

            <!-- City / Municipality -->
            <div class="flex flex-col gap-xs">
              <label class="text-[12px] font-semibold text-on-surface-variant" for="city">
                City / Municipality <span class="text-error" aria-hidden="true">*</span>
              </label>
              <input
                id="city" name="city" type="text" required
                placeholder="e.g. Cebu City"
                class="border border-outline rounded-lg px-md py-sm bg-surface text-[14px] outline-none transition-all"
                aria-required="true" aria-describedby="city-err"
              />
              <span id="city-err" class="error-msg" role="alert">City/Municipality is required.</span>
            </div>

            <!-- Province -->
            <div class="flex flex-col gap-xs">
              <label class="text-[12px] font-semibold text-on-surface-variant" for="province">
                Province <span class="text-error" aria-hidden="true">*</span>
              </label>
              <input
                id="province" name="province" type="text" required
                placeholder="e.g. Cebu"
                class="border border-outline rounded-lg px-md py-sm bg-surface text-[14px] outline-none transition-all"
                aria-required="true" aria-describedby="province-err"
              />
              <span id="province-err" class="error-msg" role="alert">Province is required.</span>
            </div>
          </div>
        </fieldset>

        <!-- Section 3: Administrative Identity -->
        <fieldset class="mb-xs">
          <legend class="w-full">
            <div class="flex items-center gap-sm mb-md pb-xs border-b border-outline-variant">
              <span class="w-6 h-6 rounded-full bg-primary text-on-primary flex items-center justify-center text-[11px] font-bold flex-shrink-0">3</span>
              <span class="text-[14px] font-semibold text-primary tracking-wide uppercase">Administrative Identity</span>
            </div>
          </legend>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-md">
            <!-- Government Email -->
            <div class="flex flex-col gap-xs">
              <label class="text-[12px] font-semibold text-on-surface-variant" for="gov-email">
                Government Email <span class="text-error" aria-hidden="true">*</span>
              </label>
              <input
                id="gov-email" name="govEmail" type="email" required autocomplete="email"
                placeholder="name@governance.ph"
                class="border border-outline rounded-lg px-md py-sm bg-surface text-[14px] outline-none transition-all"
                aria-required="true" aria-describedby="gov-email-err"
              />
              <span id="gov-email-err" class="error-msg" role="alert">A valid government email is required.</span>
            </div>

            <!-- Government ID -->
            <div class="flex flex-col gap-xs">
              <label class="text-[12px] font-semibold text-on-surface-variant" for="gov-id">
                Government ID <span class="text-error" aria-hidden="true">*</span>
              </label>
              <input
                id="gov-id" name="govId" type="text" required
                placeholder="YYYY-XXXX-ZZZ"
                pattern="GOV-\d{4}-\d{4}"
                class="border border-outline rounded-lg px-md py-sm bg-surface text-[14px] outline-none transition-all"
                aria-required="true" aria-describedby="gov-id-err gov-id-hint"
              />
              <span id="gov-id-hint" class="text-[11px] text-outline">Format: (subject to change)</span>
              <span id="gov-id-err" class="error-msg" role="alert">ID must follow format GOV-YYYY-XXXX.</span>
            </div>

            <!-- Role -->
            <div class="flex flex-col gap-xs md:col-span-2">
              <label class="text-[12px] font-semibold text-on-surface-variant" for="role">
                Role <span class="text-error" aria-hidden="true">*</span>
              </label>
              <div class="flex gap-sm flex-wrap" role="group" aria-labelledby="role-label">
                <label class="role-option flex items-center gap-sm border border-outline rounded-lg px-md py-sm cursor-pointer hover:border-primary transition-all has-[:checked]:border-primary has-[:checked]:bg-primary-fixed/30">
                  <input type="radio" name="role" value="ROLE_DIRECTOR" class="accent-primary" required />
                  <span class="text-[13px] font-semibold text-on-surface">DIRECTOR</span>
                  <span class="text-[11px] text-outline hidden sm:block">— Full access</span>
                </label>
                <label class="role-option flex items-center gap-sm border border-outline rounded-lg px-md py-sm cursor-pointer hover:border-primary transition-all has-[:checked]:border-primary has-[:checked]:bg-primary-fixed/30">
                  <input type="radio" name="role" value="ROLE_SUPERADMIN" class="accent-primary" />
                  <span class="text-[13px] font-semibold text-on-surface">SUPER ADMIN</span>
                  <span class="text-[11px] text-outline hidden sm:block">— IT and back-office work</span>
                </label>
                <label class="role-option flex items-center gap-sm border border-outline rounded-lg px-md py-sm cursor-pointer hover:border-primary transition-all has-[:checked]:border-primary has-[:checked]:bg-primary-fixed/30">
                  <input type="radio" name="role" value="ROLE_ADMIN" class="accent-primary" />
                  <span class="text-[13px] font-semibold text-on-surface">ADMIN</span>
                  <span class="text-[11px] text-outline hidden sm:block">— Administrative work</span>
                </label>
                <label class="role-option flex items-center gap-sm border border-outline rounded-lg px-md py-sm cursor-pointer hover:border-primary transition-all has-[:checked]:border-primary has-[:checked]:bg-primary-fixed/30">
                  <input type="radio" name="role" value="ROLE_SURVEYOR" class="accent-primary" />
                  <span class="text-[13px] font-semibold text-on-surface">SURVEYOR</span>
                  <span class="text-[11px] text-outline hidden sm:block">— Consensus planning</span>
                </label>
              </div>
              <span id="role-err" class="error-msg" role="alert">Please select a role.</span>
            </div>
          </div>
        </fieldset>

        <!-- Required note -->
        <p class="text-[12px] text-outline mt-md">
          Fields marked <span class="text-error font-bold">*</span> are required.
        </p>

      </form>
    </div>

    <!-- Modal Footer -->
    <div class="px-lg py-md bg-surface-container border-t border-outline-variant/20 flex flex-col sm:flex-row justify-end gap-md items-center">
      <button
        id="cancel-modal-btn"
        type="button"
        class="px-xl py-sm text-[14px] font-semibold text-on-surface-variant hover:text-on-surface transition-colors focus-visible:ring-2 focus-visible:ring-primary rounded-lg"
      >
        Cancel
      </button>
      <button
        id="submit-form-btn"
        type="submit"
        form="create-user-form"
        class="bg-primary text-on-primary px-xl py-sm rounded-lg text-[14px] font-semibold shadow-sm hover:bg-primary/90 transition-all focus-visible:ring-2 focus-visible:ring-primary focus-visible:ring-offset-2 flex items-center gap-xs"
      >
        <span class="material-symbols-outlined text-[18px]" aria-hidden="true">person_add</span>
        Submit Entry
      </button>
    </div>
  </div>
</div>

<!-- Toast notification -->
<div id="toast" role="status" aria-live="polite"></div>

<!-- ═══════════════════════════════ FOOTER ═══════════════════════════════ -->
<footer class="bg-surface-container border-t border-outline-variant/15">
  <div class="w-full py-xl px-gutter max-w-[1280px] mx-auto flex flex-col md:flex-row justify-between items-center gap-md">
    <div class="flex flex-col items-center md:items-start gap-xs">
      <span class="text-[20px] font-bold text-primary">City Governance Portal</span>
      <p class="text-[14px] text-on-surface-variant">© 2024 Municipal Administration. All rights reserved.</p>
    </div>
    <nav class="flex flex-wrap justify-center gap-md" aria-label="Footer navigation">
      <a class="text-[12px] font-semibold text-on-surface-variant hover:text-primary transition-colors" href="#">Privacy Policy</a>
      <a class="text-[12px] font-semibold text-on-surface-variant hover:text-primary transition-colors" href="#">Terms of Service</a>
      <a class="text-[12px] font-semibold text-on-surface-variant hover:text-primary transition-colors" href="#">Accessibility Statement</a>
      <a class="text-[12px] font-semibold text-on-surface-variant hover:text-primary transition-colors" href="#">Contact Registry</a>
    </nav>
  </div>
</footer>
</body>
</html>