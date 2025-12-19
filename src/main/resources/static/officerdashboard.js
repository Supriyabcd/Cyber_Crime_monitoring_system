// ======================
// Global Variables
// ======================
let currentSection = 'dashboard';
let selectedCaseForEvidence = null;

// ======================
// Initialize App
// ======================
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    loadOfficerProfile();
    updateOfficerStats();
    loadAssignedCases();
    loadFeedbackData();
}

// ======================
// Navigation Functions
// ======================
function showSection(sectionName) {
    // Hide all sections
    const sections = document.querySelectorAll('.section');
    sections.forEach(section => section.classList.remove('active'));

    // Show selected section
    const targetSection = document.getElementById(sectionName);
    if (targetSection) targetSection.classList.add('active');

    // Update navigation links
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => link.classList.remove('active'));

    const activeLink = document.querySelector(`[onclick="showSection('${sectionName}')"]`);
    if (activeLink) activeLink.classList.add('active');

    currentSection = sectionName;
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

async function loadOfficerProfile() {
    const officerId = localStorage.getItem("officerId");
    const token = localStorage.getItem("jwtToken");

    if (!officerId || !token) return;

    try {
        const res = await fetch(`http://localhost:8080/officer/${officerId}/profile`, {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (!res.ok) throw new Error("Failed to fetch profile");

        const data = await res.json();  // ✅ changed variable name
        const officer = data.officer;
        const domainName = data.domain;

        document.getElementById("officerid").value = officer.officerId;
        document.getElementById("officername").value = officer.fname + " " + officer.lname;
        document.getElementById("doj").value = officer.joinDate || '';
        document.getElementById("mobile").value = officer.mobNo || '';
        document.getElementById("Domain").value = domainName || '';  // ✅ fix here
    } catch (err) {
        console.error(err);
        alert("Unable to load profile.");
    }
}



function updateOfficerStats() {
    // Simulate real-time stats updates
    document.getElementById('assignedCases').textContent = '12';
    document.getElementById('activeCases').textContent = '8';
    document.getElementById('pendingEvidence').textContent = '3';
    document.getElementById('closedCases').textContent = '4';
}

async function loadAssignedCases() {
    const officerId = localStorage.getItem("officerId");
    const token = localStorage.getItem("jwtToken");

    if (!officerId || !token) {
        showNotification("Please log in again.", "error");
        window.location.href = "/officer/officerlogin";
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/officer/${officerId}/cases`, {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) throw new Error("Failed to load cases");

        const cases = await response.json();
        renderCases(cases);

    } catch (err) {
        console.error(err);
        showNotification("Unable to fetch assigned cases. Please try later.", "error");
    }
}


function renderCases(cases) {
    const container = document.querySelector(".cases-list");
    container.innerHTML = ""; // clear old cases

    if (!cases || cases.length === 0) {
        container.innerHTML = `<p class="no-cases">No cases assigned yet.</p>`;
        return;
    }

    cases.forEach(c => {
        const caseCard = document.createElement("div");
        caseCard.className = "case-card";
        caseCard.dataset.status = c.currentStatus.toLowerCase();

        caseCard.innerHTML = `
            <div class="case-header">
                <h3>${c.caseName}</h3>
                <div class="case-status ${c.currentStatus === 'Completed' ? 'status-completed' : 'status-under-review'}">${c.currentStatus}</div>
            </div>
            <p><strong>Case ID:</strong> ${c.caseId}</p>
            <p><strong>Complainant ID:</strong> ${c.userId || 'N/A'}</p>
            <p><strong>Filed Date:</strong> ${c.createdOn || '—'}</p>
            <p><strong>Description:</strong> ${c.detailedDescription}</p>
            <div class="case-actions">
                <button onclick="viewCase('${c.caseId}')">View Details</button>
                <button onclick="updateCaseStatus('${c.caseId}')">Update Status</button>
                <button onclick="getcriminaldetails('${c.caseId}')">View Criminal</button>
            </div>
        `;
        container.appendChild(caseCard);
    });
}

// ======================
// Going back to Cases
// ======================

function goBackToCases() {
    showSection('cases'); // This already exists in your JS to handle section switching
}


// ======================
// Case Management Functions
// ======================


function filterOfficerCases(status) {
  // Highlight the active button
  const filterBtns = document.querySelectorAll('.filter-btn');
  filterBtns.forEach(btn => btn.classList.remove('active'));
  
  const activeBtn = document.querySelector(`[onclick="filterOfficerCases('${status}')"]`);
  if (activeBtn) activeBtn.classList.add('active');

  // Normalize the filter status for comparison
  const normalizedFilter = status.trim().toLowerCase().replace(/\s+/g, '-');

  // Loop through all case cards
  const cases = document.querySelectorAll('.case-card');
  cases.forEach(caseCard => {
    // Normalize the dataset status too
    const normalizedStatus = (caseCard.dataset.status || '')
      .trim()
      .toLowerCase()
      .replace(/\s+/g, '-');

    // Show/hide based on match
    if (normalizedFilter === 'all' || normalizedStatus === normalizedFilter) {
      caseCard.style.display = 'block';
    } else {
      caseCard.style.display = 'none';
    }
  });
}

// async function viewCase(caseId) {
//     const token = localStorage.getItem("jwtToken");
//     if (!token) return;

//     try {
//         const res = await fetch(`http://localhost:8080/officer/cases/${caseId}/details`, {
//             headers: {
//                 "Authorization": `Bearer ${token}`,
//                 "Content-Type": "application/json"
//             }
//         });

//         if (!res.ok) throw new Error("Failed to fetch case details");

//         const data = await res.json();

//         // Switch to Investigation Workspace
//         showSection('investigations');

//         // Populate Case Info
//         const caseInfo = data.case;
//         const user = data.user || {};
//         const domain = data.domain || '';
//         const details = data.details || [];

//         document.getElementById("investigationCaseName").innerText = caseInfo.caseName || '';
//         document.getElementById("investigationCaseId").innerText = caseInfo.caseId || '';
//         document.getElementById("investigationUserId").innerText = user.userId 
//             ? `${user.userId} - ${user.username}` 
//             : 'N/A';
//         document.getElementById("investigationDomain").innerText = domain;

//         document.getElementById("investigationDetails").innerText = details.map(d => 
//             `Platform: ${d.platformName}\nPersonal Details: ${d.personalDetails}\nSuspect Details: ${d.suspectDetails}\n`
//         ).join("\n---\n");

//     } catch (err) {
//         console.error(err);
//         alert("Unable to load case details.");
//     }
// }


// async function viewCase(caseId) {
//     const token = localStorage.getItem("jwtToken");
//     if (!token) return;

//     try {
//         const res = await fetch(`http://localhost:8080/officer/cases/${caseId}/details`, {
//             headers: {
//                 "Authorization": `Bearer ${token}`,
//                 "Content-Type": "application/json"
//             }
//         });

//         if (!res.ok) throw new Error("Failed to fetch case details");

//         const data = await res.json();

//         showSection('investigations');

//         const caseInfo = data.case;
//         const user = data.user || {};
//         const domain = data.domain || '';
//         const details = data.details || [];

//         document.getElementById("investigationCaseName").innerText = caseInfo.caseName || '';
//         document.getElementById("investigationCaseId").innerText = caseInfo.caseId || '';
//         document.getElementById("investigationUserId").innerText = user.userId 
//             ? `${user.userId} - ${user.username}` 
//             : 'N/A';
//         document.getElementById("investigationDomain").innerText = domain;

//         document.getElementById("investigationDetails").innerText = details.map(d => 
//             `Platform: ${d.platformName}\nPersonal Details: ${d.personalDetails}\nSuspect Details: ${d.suspectDetails}\n`
//         ).join("\n---\n");

//         // ✅ SHOW IMAGE (if present)
//         const imgContainer = document.getElementById("investigationImage");
//         imgContainer.innerHTML = ""; // clear old image

//         if (caseInfo.imagePaths) {
//             // handle single or multiple images
//             const paths = Array.isArray(caseInfo.imagePaths)
//                 ? caseInfo.imagePaths
//                 : caseInfo.imagePaths.split(",");

//             paths.forEach(path => {
//                 const img = document.createElement("img");
//                 // img.src = `http://localhost:8080/${path.trim()}`;
//                 img.src = decodeURI(`http://localhost:8080/${path.trim()}`);
//                 img.alt = "Case Image";
//                 img.style.maxWidth = "300px";
//                 img.style.margin = "10px";
//                 imgContainer.appendChild(img);
//             });
//         } else {
//             imgContainer.innerHTML = "<p>No image uploaded for this case.</p>";
//         }

//     } catch (err) {
//         console.error(err);
//         alert("Unable to load case details.");
//     }
// }


async function viewCase(caseId) {
    const token = localStorage.getItem("jwtToken");
    if (!token) {
        alert("No token found. Please log in again.");
        return;
    }

    let res; // declare outside to log in catch
    try {
        res = await fetch(`http://localhost:8080/officer/cases/${caseId}/details`, {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (!res.ok) {
            const errorText = await res.text();
            console.error("❌ Failed to fetch case details:", res.status, errorText);
            throw new Error(`HTTP ${res.status}: ${errorText}`);
        }

        const data = await res.json();
        console.log("✅ Case data fetched:", data);

        showSection('investigations');

        const caseInfo = data.case || {};
        const user = data.user || {};
        const domain = data.domain || '';
        const details = data.details || [];

        // 🟢 Fill text details
        document.getElementById("investigationCaseName").innerText = caseInfo.caseName || '';
        document.getElementById("investigationCaseId").innerText = caseInfo.caseId || '';
        document.getElementById("investigationUserId").innerText = user.userId
            ? `${user.userId} - ${user.username}`
            : 'N/A';
        document.getElementById("investigationDomain").innerText = domain;

        document.getElementById("investigationDetails").innerText = details.map(d =>
            `Platform: ${d.platformName}\nPersonal Details: ${d.personalDetails}\nSuspect Details: ${d.suspectDetails}\n`
        ).join("\n---\n");

        // 🟢 Show images (works like user side)
        const imgContainer = document.getElementById("investigationImage");
        imgContainer.innerHTML = ""; // clear previous images

        if (caseInfo.imagePaths) {
            const paths = Array.isArray(caseInfo.imagePaths)
                ? caseInfo.imagePaths
                : caseInfo.imagePaths.split(",");

            paths.forEach(rawPath => {
                const path = rawPath.trim();

                // Build valid URL just like user page
                const fullUrl = path.startsWith("/")
                    ? `http://localhost:8080${path}`
                    : `http://localhost:8080/${path}`;

                const img = document.createElement("img");
                img.src = fullUrl; // don't decodeURI — keep %20 for spaces
                img.alt = "Case Image";
                img.style.maxWidth = "300px";
                img.style.margin = "10px";
                img.style.borderRadius = "8px";
                imgContainer.appendChild(img);
            });
        } else {
            imgContainer.innerHTML = "<p>No image uploaded for this case.</p>";
        }

    } catch (err) {
        console.error("⚠️ Error loading case details:", err);
        alert("Unable to load case details. Check console for details.");
    }
}


// function updateCaseStatus(caseId) {
//     const statusOptions = ['Investigating', 'Evidence Collection', 'Analysis', 'Report Preparation', 'Completed'];
//     const currentStatus = prompt(`Update status for Case ${caseId}:\n\nSelect status:\n1. Investigating\n2. Evidence Collection\n3. Analysis\n4. Report Preparation\n5. Completed\n\nEnter number (1-5):`);
    
//     if (currentStatus && currentStatus >= 1 && currentStatus <= 5) {
//         const newStatus = statusOptions[currentStatus - 1];
//         showNotification(`Case ${caseId} status updated to: ${newStatus}`, 'success');
        
//         // Update case in storage
//         const assignedCases = JSON.parse(localStorage.getItem('assignedCases')) || [];
//         const caseIndex = assignedCases.findIndex(c => c.id === caseId);
//         if (caseIndex !== -1) {
//             assignedCases[caseIndex].status = newStatus.toLowerCase().replace(' ', '-');
//             localStorage.setItem('assignedCases', JSON.stringify(assignedCases));
//         }
//     }
// }

// function updateCaseStatus(caseId) {
//   const modal = document.getElementById('statusModal');
//   const caseIdLabel = document.getElementById('statusCaseId');
//   const closeModal = document.getElementById('closeModal');

//   modal.classList.add('show');
//   caseIdLabel.textContent = `Case ID: ${caseId}`;

//   const buttons = modal.querySelectorAll('.status-options button');

//   // Add click listeners for each status option
//         buttons.forEach(btn => {
//             btn.onclick = async () => {
//         const newStatus = btn.getAttribute('data-status');

//         try {
//             const response = await fetch(`/officer/cases/${caseId}/status?status=${encodeURIComponent(newStatus)}`, {
//             method: 'PUT'
//             });

//             if (response.ok) {
//             showNotification(`Case ${caseId} status updated to: ${newStatus}`, 'success');
//             } else {
//             showNotification(`Failed to update status for case ${caseId}`, 'error');
//             }
//         } catch (error) {
//             showNotification('Error connecting to server.', 'error');
//         }

//         modal.classList.remove('show'); // close popup
//         };
// });

//   // Cancel button closes the modal
//   closeModal.onclick = () => {
//     modal.classList.remove('show');
//   };

//   // Close when clicking outside
//   modal.onclick = (e) => {
//     if (e.target === modal) modal.classList.remove('show');
//   };
// }

async function updateCaseStatus(caseId) {
  const modal = document.getElementById('statusModal');
  const caseIdLabel = document.getElementById('statusCaseId');
  const closeModal = document.getElementById('closeModal');

  modal.classList.add('show');
  caseIdLabel.textContent = `Case ID: ${caseId}`;

  const buttons = modal.querySelectorAll('.status-options button');

  buttons.forEach(btn => {
    btn.onclick = async () => {
      const newStatus = btn.getAttribute('data-status');

      try {
        const response = await fetch(`/officer/cases/${caseId}/status?status=${encodeURIComponent(newStatus)}`, {
          method: 'PUT'
        });

        if (response.ok) {
          showNotification(`Case ${caseId} status updated to: ${newStatus}`, 'success');

          // 🟢 Automatically refresh the page after 1 second
          setTimeout(() => {
            window.location.reload();
          }, 1000);
        } else {
          showNotification(`Failed to update status for case ${caseId}`, 'error');
        }
      } catch (error) {
        showNotification('Error connecting to server.', 'error');
      }

      modal.classList.remove('show');
    };
  });

  closeModal.onclick = () => modal.classList.remove('show');
  modal.onclick = (e) => { if (e.target === modal) modal.classList.remove('show'); };
}


function requestInfo(caseId) {
    const infoType = prompt(`Request additional information for Case ${caseId}:\n\n1. Banking records\n2. Phone records\n3. Email headers\n4. Social media data\n5. Other\n\nEnter number (1-5):`);
    
    if (infoType) {
        const types = ['Banking records', 'Phone records', 'Email headers', 'Social media data', 'Other information'];
        showNotification(`Information request sent: ${types[infoType - 1]} for Case ${caseId}`, 'success');
    }
}


async function viewFeedback(caseId, btn) {
    btn.disabled = true;
    try {
        // Check if case is completed
        const caseStatus = btn.getAttribute('data-case-status');
        if(caseStatus !== 'completed') {
            alert("User has not given feedback yet.");
            return;
        }

        // Fetch feedback dynamically from backend
        const res = await fetch(`/officer/cases/${caseId}/feedback`);
        const feedback = await res.json();

        if(!feedback) {
            alert("No feedback available for this case.");
            return;
        }

        // Populate modal
        document.getElementById('feedbackDate').textContent = feedback.feedback_date;
        document.getElementById('feedbackResponsiveness').textContent = feedback.responsiveness + " / 5";
        document.getElementById('feedbackOutcome').textContent = feedback.outcome_satisfaction + " / 5";
        document.getElementById('feedbackOverall').textContent = feedback.overall_experience + " / 5";
        document.getElementById('feedbackPrivacy').textContent = feedback.privacy_respected ? "Yes" : "No";

        document.getElementById('feedbackModal').classList.remove('hidden');
    } catch(err) {
        console.error(err);
        alert("Failed to fetch feedback.");
    } finally {
        btn.disabled = false;
    }
}

function closeFeedbackModal() {
    document.getElementById('feedbackModal').classList.add('hidden');
}
 
// ======================
// Evidence Management
// ======================
function uploadEvidence(caseId) {
    selectedCaseForEvidence = caseId;
    showSection('evidence');
    document.getElementById('caseSelect').value = caseId;
}

function submitEvidence(event) {
    event.preventDefault();
    
    const formData = {
        caseId: document.getElementById('caseSelect').value,
        type: document.getElementById('evidenceType').value,
        description: document.getElementById('evidenceDescription').value,
        uploadDate: new Date().toISOString().split('T')[0]
    };
    
    let evidenceList = JSON.parse(localStorage.getItem('evidenceList')) || [];
    evidenceList.push(formData);
    localStorage.setItem('evidenceList', JSON.stringify(evidenceList));
    
    showNotification('Evidence uploaded successfully!', 'success');
    event.target.reset();
}

// ======================
// Report Generation
// ======================
function generateReport(event) {
    event.preventDefault();
    
    const reportData = {
        caseId: document.getElementById('reportCase').value,
        type: document.getElementById('reportType').value,
        findings: document.getElementById('reportFindings').value,
        recommendations: document.getElementById('reportRecommendations').value,
        generatedDate: new Date().toISOString().split('T')[0],
        officer: 'Inspector Kumar'
    };
    
    let reports = JSON.parse(localStorage.getItem('investigationReports')) || [];
    reports.push(reportData);
    localStorage.setItem('investigationReports', JSON.stringify(reports));
    
    showNotification(`${reportData.type} generated successfully for Case ${reportData.caseId}!`, 'success');
    event.target.reset();
    
    // Simulate report download
    setTimeout(() => {
        const reportName = `${reportData.type}_${reportData.caseId}_${reportData.generatedDate}.pdf`;
        showNotification(`Report downloaded: ${reportName}`, 'info');
    }, 1500);
}

// ======================
// Utility Functions
// ======================
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 16px 24px;
        border-radius: 8px;
        color: white;
        font-weight: 500;
        z-index: 10000;
        transform: translateX(400px);
        transition: transform 0.3s ease-in-out;
        max-width: 400px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    `;
    
    const colors = { 
        success: '#10b981', 
        error: '#ef4444', 
        warning: '#f59e0b', 
        info: '#3b82f6' 
    };
    notification.style.backgroundColor = colors[type] || colors.info;
    
    document.body.appendChild(notification);

    setTimeout(() => notification.style.transform = 'translateX(0)', 100);
    setTimeout(() => {
        notification.style.transform = 'translateX(400px)';
        setTimeout(() => notification.remove(), 300);
    }, 5000);
}

// ======================
// Investigation Tools
// ======================
function launchForensicTool() {
    showNotification('Launching Digital Forensics Suite...', 'info');
    setTimeout(() => {
        showNotification('Forensics tools ready. Remember to document all findings.', 'success');
    }, 2000);
}

function launchIPTracker() {
    showNotification('Initializing IP Tracking Module...', 'info');
    setTimeout(() => {
        showNotification('IP Tracker online. Enter target IP for analysis.', 'success');
    }, 1500);
}

function launchEmailAnalyzer() {
    showNotification('Starting Email Header Analysis Tool...', 'info');
    setTimeout(() => {
        showNotification('Email analyzer ready. Upload .eml files for processing.', 'success');
    }, 1800);
}

function launchFinancialTracker() {
    showNotification('Connecting to Financial Investigation Database...', 'info');
    setTimeout(() => {
        showNotification('Financial tracker connected. Secure connection established.', 'success');
    }, 2200);
}

// ======================
// Real-time Updates (Simulation)
// ======================
setInterval(() => {
    // Simulate real-time case updates
    const now = new Date();
    if (now.getSeconds() % 30 === 0) { // Every 30 seconds
        const updates = [
            'New evidence received for Case #CC2024001',
            'Case #CC2024002 deadline approaching',
            'Forensic analysis completed for Case #CC2024003',
            'Complainant response received for Case #CC2024001'
        ];
        
        const randomUpdate = updates[Math.floor(Math.random() * updates.length)];
        // Could show a subtle notification here
    }
}, 1000);

// update statuss dropdown
// ------------------ CASE STATUS DROPDOWN ------------------
// Toggle the status dropdown for a specific case
function toggleStatusDropdown(event, btn) {
  // prevent global click handler from immediately closing it
  event.stopPropagation();

  const dropdown = btn.closest('.status-dropdown');
  if (!dropdown) return;

  const menu = dropdown.querySelector('.status-dropdown-menu');
  if (!menu) return;

  // toggle visibility
  menu.classList.toggle('hidden');
  menu.setAttribute('aria-hidden', menu.classList.contains('hidden'));

  // close other status dropdowns
  document.querySelectorAll('.status-dropdown-menu').forEach(m => {
    if (m !== menu) {
      m.classList.add('hidden');
      m.setAttribute('aria-hidden', 'true');
    }
  });
}

// Change case status when selecting an option
function changeCaseStatus(event, el, newStatus) {
  event.stopPropagation();

  // close this dropdown
  const menu = el.closest('.status-dropdown-menu');
  if (menu) {
    menu.classList.add('hidden');
    menu.setAttribute('aria-hidden', 'true');
  }

  const card = el.closest('.case-card');
  if (!card) return;

  // update the visible badge text + classes
  const statusDiv = card.querySelector('.case-status');
  if (statusDiv) {
    const text = newStatus.charAt(0).toUpperCase() + newStatus.slice(1);
    statusDiv.textContent = text;

    // replace classes safely
    statusDiv.classList.remove('status-under-review', 'status-completed');
    if (newStatus === 'completed') {
      statusDiv.classList.add('status-completed');
    } else {
      statusDiv.classList.add('status-under-review');
    }
  }

  // update data-status attribute used by your filters
  card.setAttribute('data-status', newStatus);

  // OPTIONAL: send update to backend - uncomment and adapt the endpoint
  /*
  const caseId = card.querySelector('.case-id')?.textContent.replace('Case #', '') || '';
  fetch('/api/cases/update-status', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({ caseId, status: newStatus })
  }).then(res => res.json()).then(data => {
    console.log('server response', data);
  }).catch(err => console.error(err));
  */
}

// Close status dropdowns on clicking outside
document.addEventListener('click', function (e) {
  // if click isn't inside a status-dropdown, close all status dropdowns
  if (!e.target.closest('.status-dropdown')) {
    document.querySelectorAll('.status-dropdown-menu').forEach(m => {
      m.classList.add('hidden');
      m.setAttribute('aria-hidden', 'true');
    });
  }
});

// If you have a profile dropdown that uses document clicks too, ensure it uses different classes/IDs
// so these listeners don't conflict.



// Loading officer stats
// Fetch officer ID from localStorage
// Get officer ID from localStorage
const officerId = localStorage.getItem("officerId");
// Function to load officer stats dynamically
async function loadOfficerStats() {
    try {
        // ✅ Use the value already stored in localStorage
        const officerId = localStorage.getItem("officerId");
        if (!officerId) {
            console.error("Officer ID not found in localStorage!");
            return;
        }

        const response = await fetch(`/officer/${officerId}/stats`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const stats = await response.json();

        // Update stats cards
        document.getElementById("assignedCases").textContent = stats.assignedCases || 0;
        document.getElementById("activeCases").textContent = stats.activeCases || 0;
        document.getElementById("pendingEvidence").textContent = stats.pendingEvidence || 0;
        document.getElementById("closedCases").textContent = stats.closedCases || 0;

    } catch (error) {
        console.error("Error loading officer stats:", error);
    }
}

loadOfficerStats();




async function loadRecentActivity() {
    try {
        const response = await fetch(`/officer/${officerId}/recent`);
        const cases = await response.json();
        const activityList = document.querySelector(".activity-list");
        activityList.innerHTML = "";

        if (!cases || cases.length === 0) {
            activityList.innerHTML = `<div class="activity-item"><div class="activity-content">No recent activity.</div></div>`;
            return;
        }

        cases.forEach(c => {
            const item = document.createElement("div");
            item.className = "activity-item";
            item.innerHTML = `
                <div class="activity-icon">🕵️‍♂️</div>
                <div class="activity-content">
                    <div class="activity-title">Case: ${c.caseName}</div>
                    <div class="activity-time">${c.createdOn || "Unknown date"}</div>
                </div>
            `;
            activityList.appendChild(item);
        });
    } catch (err) {
        console.error("Error loading recent activity:", err);
    }
}

// Call both functions after dashboard loads

loadRecentActivity();


// async function getcriminaldetails(caseId) {
//     const token = localStorage.getItem("jwtToken");
//     if (!token) return;

//     try {
//         const res = await fetch(`http://localhost:8080/officer/cases/${caseId}/criminals`, {
//             headers: {
//                 "Authorization": `Bearer ${token}`,
//                 "Content-Type": "application/json"
//             }
//         });

//         if (!res.ok) throw new Error("Failed to fetch criminal details");

//         const data = await res.json();

//         if (!data || data.length === 0) {
//             alert("No criminal records found for this case.");
//             return;
//         }

//         // 🧾 Display details (for now, simple alert or console)
//         let details = data.map(c => `
// ID: ${c.criminalId}
// Name: ${c.fname} ${c.lname}
// DOB: ${c.dob || "—"}
// Address: ${c.hNo || ""}, ${c.street || ""}, ${c.city || ""}, ${c.state || ""}
// Mobile: ${c.mobNo || "—"}
// Social Handle: ${c.socialHandle || "—"}
//         `).join("\n-------------------------\n");

//         alert("Criminal Details:\n\n" + details);

//     } catch (err) {
//         console.error(err);
//         alert("Unable to load criminal details.");
//     }
// }




async function getcriminaldetails(caseId) {
  const token = localStorage.getItem("jwtToken");
  if (!token) return;

  try {
    const res = await fetch(`http://localhost:8080/officer/cases/${caseId}/criminals`, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });

    if (!res.ok) throw new Error("Failed to fetch criminal details");

    const data = await res.json();

    const modal = document.getElementById("criminalModal");
    const list = document.getElementById("criminalList");

    list.innerHTML = ""; // clear previous

    if (!data || data.length === 0) {
      list.innerHTML = `<p style="text-align:center; color:#94a3b8;">No criminal records found for this case.</p>`;
    } else {
      data.forEach(c => {
        const card = document.createElement("div");
        card.className = "criminal-card";

        // 🔹 Convert multiline description into bullet list
        const bulletPoints = c.description
            ? c.description
                .split("\n")
                .filter(line => line.trim() !== "")
                .map(line => `<li>${line.replace(/^•\s*/, "")}</li>`)
                .join("")
            : "<li>No description available</li>";

        card.innerHTML = `
            <p><strong>ID:</strong> ${c.criminalId}</p>
            <p><strong>Name:</strong> ${c.fname} ${c.lname}</p>
            <p><strong>DOB:</strong> ${c.dob || "—"}</p>
            <p><strong>Address:</strong> ${[c.hNo, c.street, c.city, c.state].filter(Boolean).join(", ")}</p>
            <p><strong>Mobile:</strong> ${c.mobNo || "—"}</p>
            <p><strong>Social Handle:</strong> ${c.socialHandle || "—"}</p>

            <p><strong>Description:</strong></p>
            <ul style="margin-left: 1.5rem; list-style-type: disc;">
            ${bulletPoints}
            </ul>
        `;

        list.appendChild(card);
        });
    }

    modal.style.display = "flex";
  } catch (err) {
    console.error(err);
    alert("Unable to load criminal details.");
  }
}

function closeCriminalModal() {
  document.getElementById("criminalModal").style.display = "none";
}

// Optional: close modal when clicking outside
window.onclick = function (e) {
  const modal = document.getElementById("criminalModal");
  if (e.target === modal) modal.style.display = "none";
};


async function loadFeedbackData() {
    const officerId = localStorage.getItem('officerId');
    if (!officerId) return;

    try {
        const res = await fetch(`http://localhost:8080/api/feedback/officer/${officerId}`);
        const feedbackList = await res.json();
        const tbody = document.getElementById('feedbackTableBody');
        tbody.innerHTML = '';

        if (!feedbackList || feedbackList.length === 0) {
            tbody.innerHTML = `<tr><td colspan="6">No feedback found</td></tr>`;
            return;
        }

        feedbackList.forEach(f => {
            const row = `
                <tr>
                    <td>${f.userId}</td>
                    <td>${f.outcomeSatisfaction}</td>
                    <td>${f.overallExperience}</td>
                    <td>${f.responsiveness}</td>
                    <td>${f.privacyRespected ? 'Yes' : 'No'}</td>
                    <td>${f.feedbackDate || '—'}</td>
                </tr>
            `;
            tbody.insertAdjacentHTML('beforeend', row);
        });
    } catch (err) {
        console.error('Error loading feedback:', err);
        document.getElementById('feedbackTableBody').innerHTML =
            `<tr><td colspan="6">Error loading feedback</td></tr>`;
    }
}

