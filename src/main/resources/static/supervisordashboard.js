// ======================
    // Supervisor Dashboard (Dynamic)
    // ======================

    // Global state
    let currentSection = 'dashboard';
    let officers = [];
    let cases = [];
    let supervisorProfile = {};

    // ======================
    // Initialization
    // ======================
    document.addEventListener('DOMContentLoaded', async function() {
        await initializeApp();
        loadDashboardData();
    });

    async function initializeApp() {
        loadSupervisorProfile();
        await Promise.all([
            loadOfficersData(),
            loadSupervisorProfile(),
            loadSupervisorStats(localStorage.getItem("supervisorId") || localStorage.getItem("officerId")),
            loadCasesData(),
            // updateSupervisorStats(),
            populateAssignmentsDropdowns(), 
        ]);
    }


    // ======================
    // Profile Dropdown
    // ======================
    function toggleProfileMenu() {
        const menu = document.getElementById('profileMenu');
        if (menu) menu.classList.toggle('show');
    }

    document.addEventListener('click', function (e) {
        const menu = document.getElementById('profileMenu');
        const dropdown = document.querySelector('.profile-dropdown');
        if (menu && !dropdown.contains(e.target)) {
            menu.classList.remove('show');
        }
    });

    // ======================
    // Profile Management
    // ======================
    function toggleEdit() {
        editMode = !editMode;
        const inputs = document.querySelectorAll('#profileForm input');
        const editBtn = document.querySelector('[onclick="toggleEdit()"]');
        const saveBtn = document.getElementById('saveProfile');
        const cancelBtn = document.getElementById('cancelEdit');

        inputs.forEach(input => {
            if (input.id !== 'fullName' && input.id !== 'aadhar') {
                input.readOnly = !editMode;
            }
        });

        if (editMode) {
            editBtn.style.display = 'none';
            saveBtn.style.display = 'inline-flex';
            cancelBtn.style.display = 'inline-flex';
        } else {
            editBtn.style.display = 'inline-flex';
            saveBtn.style.display = 'none';
            cancelBtn.style.display = 'none';
        }
    }

    function saveProfile() {
        const profileData = {
            fullName: document.getElementById('fullName').value,
            email: document.getElementById('email').value,
            phone: document.getElementById('phone').value,
            address: document.getElementById('address').value,
            aadhar: document.getElementById('aadhar').value,
            occupation: document.getElementById('occupation').value
        };

        localStorage.setItem('userProfile', JSON.stringify(profileData));
        showNotification('Profile updated successfully!', 'success');
        toggleEdit();
    }

    function cancelEdit() {
        loadUserData();
        toggleEdit();
    }

    function loadUserData() {
        const savedProfile = localStorage.getItem('userProfile');
        if (savedProfile) {
            const profileData = JSON.parse(savedProfile);
            document.getElementById('fullName').value = profileData.fullName;
            document.getElementById('email').value = profileData.email;
            document.getElementById('phone').value = profileData.phone;
            document.getElementById('address').value = profileData.address;
            document.getElementById('aadhar').value = profileData.aadhar;
            document.getElementById('occupation').value = profileData.occupation;
            document.getElementById('userName').textContent = profileData.fullName;
        }
    }
    // ======================
    // Supervisor Profile
    // ======================
    // async function loadSupervisorProfile() {
    //     //const supervisorId = localStorage.getItem("supervisorId");
    //     const supervisorId = localStorage.getItem("supervisorId") || localStorage.getItem("officerId");

    //     const token = localStorage.getItem("jwtToken");

    //     if (!supervisorId || !token) {
    //         console.error("Supervisor not logged in");
    //         return;
    //     }

    //     try {
    //         const res = await fetch(`http://localhost:8080/supervisor/${supervisorId}/profile`, {
    //             headers: {
    //                 "Authorization": `Bearer ${token}`,
    //                 "Content-Type": "application/json"
    //             }
    //         });

    //         if (!res.ok) throw new Error("Failed to fetch supervisor profile");

    //         const data = await res.json();

    //         // ✅ Populate Profile Fields
    //         document.getElementById("officerid").value = data.officerId;
    //         document.getElementById("officername").value = `${data.fname} ${data.lname}`;
    //         document.getElementById("doj").value = data.joinDate || '';
    //         document.getElementById("mobile").value = data.mobNo || '';
    //         document.getElementById("Domain").value = data.domain || '';
        

    //     } catch (err) {
    //         console.error("Error loading supervisor profile:", err);
    //         alert("Failed to load profile details");
    //     }
    // }

    async function loadSupervisorProfile() {
  const supervisorId = localStorage.getItem("supervisorId") || localStorage.getItem("officerId");
  const token = localStorage.getItem("jwtToken");

  if (!supervisorId || !token) {
    console.error("Supervisor not logged in");
    return;
  }

  try {
    const res = await fetch(`http://localhost:8080/supervisor/${supervisorId}/profile`, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });

    if (!res.ok) throw new Error("Failed to fetch supervisor profile");

    const data = await res.json();
    supervisorProfile = data; // ✅ Now globally stored

    document.getElementById("officerid").value = data.officerId;
    // document.getElementById("officername").value = `${data.fname} ${data.lname}`;
    document.getElementById("officername").value = data.fullName || '';
    document.getElementById("doj").value = data.joinDate || '';
    document.getElementById("mobile").value = data.mobNo || '';
    document.getElementById("Domain").value = data.domain || '';
  } catch (err) {
    console.error("Error loading supervisor profile:", err);
    alert("Failed to load profile details");
  }
}


    // ======================
    // Dashboard Statistics
    // ======================
 async function loadSupervisorStats(supervisorId) {
    try {
        const response = await fetch(`http://localhost:8080/supervisor/${supervisorId}/stats`);
        if (!response.ok) {
            throw new Error("Failed to fetch stats");
        }

        const stats = await response.json();

        // ✅ Display stats in dashboard cards or elements
        document.getElementById("totalCases").textContent = stats.totalCases || 0;
        document.getElementById("pendingAssignment").textContent = stats.unassignedCases || 0;
        document.getElementById("activeOfficers").textContent = stats.activeOfficers || 0;
        document.getElementById("closureRate").textContent = `${stats.closureRate}%`;

    } catch (error) {
        console.error("Error loading supervisor stats:", error);
    }
}


    function loadDashboardData() {
        setTimeout(() => updateCharts(), 1000);
    }

    function updateCharts() {
        console.log('Charts updated with latest data');
    }

    // ======================
    // Officers
    // ======================
    async function loadOfficersData() {
        try {
            const res = await fetch('http://localhost:8080/api/supervisor/officers/performance');
            officers = await res.json();
            localStorage.setItem('officers', JSON.stringify(officers));

            const tableBody = document.querySelector('#officerPerformanceTable tbody');
            if (tableBody) {
                tableBody.innerHTML = officers.map(o => `
                    <tr>
                        <td>${o.officerId}</td>
                        <td>${o.name}</td>
                        <td>${o.casesSolved}</td>
                    </tr>
                `).join('');
            }
        } catch (err) {
            console.error('Error fetching officers:', err);
            showNotification('Failed to load officer data.', 'error');
        }
    }

    // ======================
    // Cases
    // ======================
    // ======================
    // Cases - Oversight Table
    // ======================
//   async function loadCasesData() {
//     try {
//         const supervisorId = localStorage.getItem("supervisorId") || localStorage.getItem("officerId");
//         const token = localStorage.getItem("jwtToken");
//         if (!supervisorId || !token) {
//             console.error("Missing supervisor ID or token");
//             return;
//         }

//         const res = await fetch(`http://localhost:8080/supervisor/${supervisorId}/cases`, {
//             headers: {
//                 "Authorization": `Bearer ${token}`,
//                 "Content-Type": "application/json"
//             }
//         });

//         if (!res.ok) throw new Error("Failed to fetch cases from backend");

//         const cases = await res.json();
//         localStorage.setItem("supervisorCases", JSON.stringify(cases));
//         console.log("Supervisor cases JSON:", cases);
//         const tableBody = document.getElementById("oversightCasesBody");
//         if (!tableBody) return;

//         // ✅ Fetch usernames for each case (if userId exists)
//         const casesWithUsernames = await Promise.all(cases.map(async c => {
//             if (c.userId) {
//                 try {
//                     const userRes = await fetch(`http://localhost:8080/api/user/${c.userId}`, {
//                         headers: { "Authorization": `Bearer ${token}` }
//                     });
//                     if (userRes.ok) {
//                         const userData = await userRes.json();
//                         c.reportedBy = userData.username || 'Unknown';
//                     } else {
//                         c.reportedBy = 'Unknown';
//                     }
//                 } catch {
//                     c.reportedBy = 'Unknown';
//                 }
//             } else {
//                 c.reportedBy = 'Unknown';
//             }
//             return c;
//         }));

//         // ✅ Render table rows
//         tableBody.innerHTML = casesWithUsernames.map(c => `
//             <tr>
//                 <td>${c.caseId}</td>
//                 <td>${c.caseName || 'N/A'}</td>
//                 <td>${c.assignedOfficer?.name || 'Unassigned'}</td>
//                 <td>${c.currentStatus || 'Unknown'}</td>
//                 <td>${c.reportedBy}</td>
//                 <td>
//                     <button class="btn btn-sm" onclick="assignOfficer('${c.caseId}')">Assign Officer</button>
//                 </td>
//             </tr>
//         `).join('');

//     } catch (err) {
//         console.error("Error fetching cases:", err);
//         showNotification("Failed to load cases data.", "error");
//     }
// }

async function loadCasesData() {
  try {
    const supervisorId = localStorage.getItem("supervisorId") || localStorage.getItem("officerId");
    const token = localStorage.getItem("jwtToken");

    if (!supervisorId || !token) {
      console.error("Missing supervisor ID or token");
      showNotification("Supervisor ID or token missing. Please log in again.", "error");
      return;
    }

    const res = await fetch(`http://localhost:8080/supervisor/${supervisorId}/cases`, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });

    if (!res.ok) throw new Error("Failed to fetch cases from backend");
    const cases = await res.json();
    const tableBody = document.getElementById("oversightCasesBody");
    if (!tableBody) return;

    tableBody.innerHTML = cases.map(c => {
      const isUnassigned = c.canAssign;

      const assignBtn = isUnassigned
        ? `<button class="btn btn-sm btn-primary" onclick="assignOfficer(${c.caseId})">Assign Officer</button>`
        : `<button class="btn btn-sm btn-secondary" disabled>Assigned</button>`;

      return `
        <tr>
          <td>${c.caseId}</td>
          <td>${c.caseName || "N/A"}</td>
          <td>${c.assignedOfficer || "Unassigned"}</td>
          <td>${c.currentStatus || "Unknown"}</td>
          <td>${c.reportedBy || "Unknown"}</td>
          <td>${assignBtn}</td>
        </tr>`;
    }).join("");
  } catch (err) {
    console.error("Error loading supervisor cases:", err);
    showNotification("Failed to load supervisor cases.", "error");
  }
}






    // ======================
    // Assign Officer Button
    // ======================
async function assignOfficer(caseId) {
  try {
    // ✅ Ensure supervisor profile is loaded
    if (!supervisorProfile || !supervisorProfile.domainId) {
      await loadSupervisorProfile(); // fetch it if not available
    }

    const domainId = supervisorProfile.domainId;
    if (!domainId) {
      console.error("Supervisor domainId still missing after load");
      showNotification("Unable to load your domain details.", "error");
      return;
    }

    // ✅ Fetch officers of that domain
    const token = localStorage.getItem("jwtToken");
    const res = await fetch(`http://localhost:8080/supervisor/officers/${domainId}`, {
      headers: { "Authorization": `Bearer ${token}` }
    });

    if (!res.ok) throw new Error("Failed to fetch officers of this domain");

    const officersInDomain = await res.json();

    if (!officersInDomain.length) {
      showNotification("No officers found in your domain.", "warning");
      return;
    }

    // ✅ Show modal with officers list
    showModal("Assign Officer", generateAssignOfficerHTML(caseId, officersInDomain));
  } catch (err) {
    console.error("Error loading domain officers:", err);
    showNotification("Failed to load domain officers.", "error");
  }
}

function generateAssignOfficerHTML(caseId, officersInDomain) {
  let options = officersInDomain
    .map(o => `<option value="${o.officerId}">${o.fname} ${o.lname}</option>`)
    .join('');
  return `
    <div>
      <label>Select Officer:</label>
      <select id="assignOfficerSelect">${options}</select>
      <button class="btn btn-primary" onclick="confirmAssignOfficer('${caseId}')">Assign</button>
    </div>
  `;
}


    async function confirmAssignOfficer(caseId) {
    const officerId = document.getElementById('assignOfficerSelect').value;
    if (!officerId) return alert('Please select an officer!');

    try {
        const res = await fetch('http://localhost:8080/supervisor/assign', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ caseId, officerId })
        });

        const msg = await res.text();
        showNotification(msg, 'success');
        closeModal();
        loadCasesData(); // refresh table
    } catch (err) {
        console.error(err);
        showNotification('Failed to assign officer', 'error');
    }
}


    function filterOversightCases(status) {
        const filterBtns = document.querySelectorAll('.filter-btn');
        filterBtns.forEach(btn => btn.classList.remove('active'));

        const activeBtn = document.querySelector(`[onclick="filterOversightCases('${status}')"]`);
        if (activeBtn) activeBtn.classList.add('active');

        showNotification(`Filtering cases by: ${status}`, 'info');
    }

    function reviewCase(caseId) {
        showModal('Case Review', generateCaseReviewHTML(caseId));
    }

    function generateCaseReviewHTML(caseId) {
        const caseData = cases.find(c => c.caseId === caseId);
        if (!caseData) return '<p>Case not found.</p>';

        return `
            <div class="case-review">
                <div class="review-section">
                    <h4>Case Overview</h4>
                    <p><strong>Case ID:</strong> ${caseData.caseId}</p>
                    <p><strong>Type:</strong> ${caseData.type}</p>
                    <p><strong>Assigned Officer:</strong> ${caseData.assignedOfficer}</p>
                    <p><strong>Status:</strong> ${caseData.status}</p>
                    <p><strong>Progress:</strong> ${caseData.progress}%</p>
                </div>
                <div class="review-section">
                    <h4>Recent Updates</h4>
                    <div class="update-item">Evidence updated recently</div>
                    <div class="update-item">Status checked last week</div>
                </div>
                <div class="review-actions">
                    <button class="btn btn-primary" onclick="approveProgress('${caseId}')">Approve Progress</button>
                    <button class="btn btn-outline" onclick="requestUpdate('${caseId}')">Request Update</button>
                </div>
            </div>
        `;
    }

    // ======================
    // Assignments
    // ======================
    async function populateAssignmentsDropdowns() {
        try {
            const [casesRes, officersRes] = await Promise.all([
                fetch('http://localhost:8080/api/supervisor/cases/unassigned'),
                fetch('http://localhost:8080/api/supervisor/officers/all')
            ]);

            const unassignedCasesRes = await casesRes.json();
            const allOfficersRes = await officersRes.json();

            const unassignedCases = Array.isArray(unassignedCasesRes)
            ? unassignedCasesRes
            : unassignedCasesRes.cases || [];

            const allOfficers = Array.isArray(allOfficersRes)
            ? allOfficersRes
            : allOfficersRes.officers || [];


            const caseSelect = document.getElementById('unassignedCase');
            const officerSelect = document.getElementById('assignOfficer');

            if (caseSelect && officerSelect) {
                caseSelect.innerHTML = unassignedCases.map(c => `<option value="${c.caseId}">${c.caseId} - ${c.type}</option>`).join('');
                officerSelect.innerHTML = allOfficers.map(o => `<option value="${o.officerId}">${o.name}</option>`).join('');
            }
        } catch (err) {
            console.error('Error loading assignment dropdowns:', err);
            showNotification('Failed to load dropdown options.', 'error');
        }
    }

    async function assignNewCase(event) {
        event.preventDefault();
        const data = {
            caseId: document.getElementById('unassignedCase').value,
            officerId: document.getElementById('assignOfficer').value,
            deadline: document.getElementById('deadline')?.value || '',
            notes: document.getElementById('assignmentNotes')?.value || '',
            assignedDate: new Date().toISOString().split('T')[0],
            assignedBy: 'Chief Inspector Patel'
        };

        try {
            const res = await fetch('http://localhost:8080/api/supervisor/cases/assign', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(data)
            });

            const msg = await res.text();
            showNotification(msg, 'success');
            event.target.reset();
        } catch (err) {
            console.error('Error assigning case:', err);
            showNotification('Failed to assign case.', 'error');
        }
    }

    // ======================
    // Analytics
    // ======================


    // ======================
    // Case Review Actions
    // ======================
    function approveProgress(caseId) {
        showNotification(`Progress approved for Case ${caseId}`, 'success');
        closeModal();
    }

    function requestUpdate(caseId) {
        const updateRequest = prompt(`Request update for Case ${caseId}:\n\nEnter your message to the assigned officer:`);
        if (updateRequest) {
            showNotification(`Update request sent for Case ${caseId}`, 'success');
            closeModal();
        }
    }

    // ======================
    // UI Utilities
    // ======================
    function showNotification(message, type = 'info') {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;
        notification.style.cssText = `
            position: fixed; top: 20px; right: 20px;
            padding: 16px 24px; border-radius: 8px;
            color: white; font-weight: 500; z-index: 10000;
            transform: translateX(400px);
            transition: transform 0.3s ease-in-out;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        `;
        const colors = { success: '#10b981', error: '#ef4444', warning: '#f59e0b', info: '#3b82f6' };
        notification.style.backgroundColor = colors[type] || colors.info;

        document.body.appendChild(notification);
        setTimeout(() => notification.style.transform = 'translateX(0)', 100);
        setTimeout(() => {
            notification.style.transform = 'translateX(400px)';
            setTimeout(() => notification.remove(), 300);
        }, 5000);
    }

    function showModal(title, content) {
        const modal = document.createElement('div');
        modal.className = 'modal-overlay';
        modal.style.cssText = `
            position: fixed; top: 0; left: 0; right: 0; bottom: 0;
            background: rgba(0,0,0,0.5);
            display: flex; align-items: center; justify-content: center;
            z-index: 10001; padding: 20px;
        `;

        const modalContent = document.createElement('div');
        modalContent.className = 'modal-content';
        modalContent.style.cssText = `
            background: white; border-radius: 12px;
            max-width: 600px; width: 100%; max-height: 80vh; overflow-y: auto;
            color: #1f2937;
        `;
        modalContent.innerHTML = `
            <div style="padding:24px; border-bottom:1px solid #e5e7eb; display:flex; justify-content:space-between; align-items:center;">
                <h3>${title}</h3>
                <button onclick="closeModal()" style="background:none; border:none; font-size:24px; cursor:pointer;">×</button>
            </div>
            <div style="padding:24px;">${content}</div>
        `;
        modal.appendChild(modalContent);
        document.body.appendChild(modal);
        modal.addEventListener('click', e => { if (e.target === modal) closeModal(); });
        window.currentModal = modal;
    }

    function closeModal() {
        if (window.currentModal) {
            window.currentModal.remove();
            window.currentModal = null;
        }
    }

    function toggleAdminMenu() {
        const menu = document.getElementById('adminDropdown');
        if (menu) menu.classList.toggle('show');
    }

    // ======================
    // Auto Refresh
    // ======================
    setInterval(() => {
        loadDashboardData();
        updateSupervisorStats();
    }, 120000);

    // ======================
    // Section Navigation (Dashboard, Oversight, Assignments, Analytics, Profile)
    // ======================
    function showSection(sectionId) {
    // Hide all sections
    const sections = document.querySelectorAll('.section');
    sections.forEach(section => section.classList.remove('active'));

    // Remove active state from nav links
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => link.classList.remove('active'));

    // Show the selected section
    const targetSection = document.getElementById(sectionId);
    if (targetSection) {
        targetSection.classList.add('active');
    }

    // Highlight the active nav link
    const activeLink = document.querySelector(`.nav-link[onclick="showSection('${sectionId}')"]`);
    if (activeLink) activeLink.classList.add('active');

    // Close profile dropdown if open
    const menu = document.getElementById('profileMenu');
    if (menu) menu.classList.remove('show');
    }

    // ======================
    // Profile Dropdown + Logout
    // ======================
    function toggleProfileMenu() {
    const menu = document.getElementById('profileMenu');
    if (menu) menu.classList.toggle('show');
    }

    document.addEventListener('click', function (e) {
    const menu = document.getElementById('profileMenu');
    const dropdown = document.querySelector('.profile-dropdown');
    if (menu && !dropdown.contains(e.target)) {
        menu.classList.remove('show');
    }
    });

    function logout() {
    localStorage.clear();
    window.location.href = "home.html";
    }

