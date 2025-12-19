// ======================
// Global Variables
// ======================
// (Already declared above, do not redeclare)

// ======================
// Smooth Scroll Function
// ======================
function scrollToSection(sectionId) {
    const section = document.getElementById(sectionId);
    if (section) {
        window.scrollTo({
            top: section.offsetTop - 70, // Adjust for fixed navbar
            behavior: 'smooth'
        });
    }
}

// ======================
// Initialize App
// ======================
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();

    // Handle report buttons
    const reportButtons = document.querySelectorAll('.btn-primary');
    reportButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const row = btn.closest('tr');
            alert(`You clicked to view details for Report ID: ${row.children[0].textContent}`);
        });
    });
});

function initializeApp() {
    loadUserData();
    updateDashboardStats();
    initializeStarRating();
    setDefaultDate();
}

// ======================
// Navigation Functions
// ======================
// function showSection(sectionName) {
//     // Hide all sections
//     const sections = document.querySelectorAll('.section');
//     sections.forEach(section => section.classList.remove('active'));

//     // Show selected section
//     const targetSection = document.getElementById(sectionName);
//     if (targetSection) targetSection.classList.add('active');

//     // Update navigation links
//     const navLinks = document.querySelectorAll('.nav-link');
//     navLinks.forEach(link => link.classList.remove('active'));

//     const activeLink = document.querySelector(`[onclick="showSection('${sectionName}')"]`);
//     if (activeLink) activeLink.classList.add('active');

//     currentSection = sectionName;
// }


// dashboard.js

function showSection(sectionId) {
    // hide all sections
    const sections = document.querySelectorAll('.section');
    sections.forEach(sec => sec.classList.remove('active'));

    // show the clicked one
    document.getElementById(sectionId).classList.add('active');

    // update navbar active link
    const links = document.querySelectorAll('.nav-link');
    links.forEach(link => link.classList.remove('active'));
    const clickedLink = Array.from(links).find(link => 
        link.textContent.trim().toLowerCase().includes(sectionId)
    );
    if (clickedLink) clickedLink.classList.add('active');
}

function toggleProfileMenu() {
    document.getElementById('profileMenu').classList.toggle('show');
}






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
// Complaint Management
// ======================
// function submitComplaint(event) {
//     event.preventDefault();

//     const complaintData = {
//         id: 'CC2024' + String(Date.now()).slice(-3),
//         type: document.getElementById('incidentType').value,
//         date: document.getElementById('incidentDate').value,
//         description: document.getElementById('description').value,
//         urgency: document.getElementById('urgency').value,
//         status: 'pending',
//         filedDate: new Date().toISOString().split('T')[0],
//         officer: 'Not Assigned'
//     };

//     let complaints = JSON.parse(localStorage.getItem('complaints')) || [];
//     complaints.push(complaintData);
//     localStorage.setItem('complaints', JSON.stringify(complaints));

//     showNotification('Complaint filed successfully! Case ID: ' + complaintData.id, 'success');

//     event.target.reset();
//     updateDashboardStats();

//     setTimeout(() => {
//         showSection('status');
//         updateCasesList();
//     }, 2000);
// }

function setDefaultDate() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('incidentDate').max = today;
    document.getElementById('incidentDate').value = today;
}

// ======================
// Case Status Management
// ======================
function filterCases(status) {
    const filterBtns = document.querySelectorAll('.filter-btn');
    filterBtns.forEach(btn => btn.classList.remove('active'));

    const activeBtn = document.querySelector(`[onclick="filterCases('${status}')"]`);
    if (activeBtn) activeBtn.classList.add('active');

    const cases = document.querySelectorAll('.case-card');
    cases.forEach(caseCard => {
        if (status === 'all' || caseCard.dataset.status === status) {
            caseCard.style.display = 'block';
        } else {
            caseCard.style.display = 'none';
        }
    });
}

function viewCaseDetails(caseId) {
    const caseDetails = getCaseDetails(caseId);
    if (caseDetails) showModal('Case Details', generateCaseDetailsHTML(caseDetails));
}

function getCaseDetails(caseId) {
    const mockDetails = {
        'CC2024001': {
            id: 'CC2024001',
            title: 'Email Phishing Attack',
            description: 'Received suspicious emails claiming to be from bank asking for account details.',
            status: 'Under Investigation',
            officer: 'Inspector Kumar',
            filedDate: 'Jan 15, 2024',
            priority: 'High',
            updates: [
                { date: 'Jan 16, 2024', update: 'Case assigned to Inspector Kumar' },
                { date: 'Jan 18, 2024', update: 'Initial investigation started' },
                { date: 'Jan 22, 2024', update: 'Evidence collected and being analyzed' }
            ]
        },
        'CC2024002': {
            id: 'CC2024002',
            title: 'Social Media Fraud',
            description: 'Fake profile created on social media using my photos and personal information.',
            status: 'Pending Assignment',
            officer: 'Not Assigned',
            filedDate: 'Jan 20, 2024',
            priority: 'Medium',
            updates: [{ date: 'Jan 20, 2024', update: 'Case filed and under review' }]
        },
        'CC2023045': {
            id: 'CC2023045',
            title: 'Online Shopping Fraud',
            description: 'Paid for product online but never received the item. Seller disappeared.',
            status: 'Resolved',
            officer: 'Sub-Inspector Sharma',
            filedDate: 'Dec 10, 2023',
            priority: 'Medium',
            updates: [
                { date: 'Dec 11, 2023', update: 'Case assigned to Sub-Inspector Sharma' },
                { date: 'Dec 15, 2023', update: 'Merchant investigation initiated' },
                { date: 'Dec 22, 2023', update: 'Fraudulent seller identified' },
                { date: 'Jan 5, 2024', update: 'Case resolved - Refund processed' }
            ]
        }
    };

    return mockDetails[caseId];
}

function generateCaseDetailsHTML(caseDetails) {
    let updatesHTML = '';
    caseDetails.updates.forEach(update => {
    updatesHTML += `<div class="update-item"><strong>${update.date}:</strong> ${update.update}</div>`;
    });




    return `
        <div class="case-details-modal">
            <div class="detail-row"><strong>Case ID:</strong> ${caseDetails.id}</div>
            <div class="detail-row"><strong>Title:</strong> ${caseDetails.title}</div>
            <div class="detail-row"><strong>Status:</strong> <span class="status-${caseDetails.status.toLowerCase().replace(' ', '-')}">${caseDetails.status}</span></div>
            <div class="detail-row"><strong>Investigating Officer:</strong> ${caseDetails.officer}</div>
            <div class="detail-row"><strong>Filed Date:</strong> ${caseDetails.filedDate}</div>
            <div class="detail-row"><strong>Priority:</strong> ${caseDetails.priority}</div>
            <div class="detail-row"><strong>Description:</strong><p>${caseDetails.description}</p></div>
            <div class="detail-row"><strong>Case Updates:</strong><div class="updates-list">${updatesHTML}</div></div>
        </div>
        <style>
            .case-details-modal { padding: 20px; }
            .detail-row { margin-bottom: 16px; }
            .detail-row strong { color: var(--primary-color); }
            .updates-list { margin-top: 12px; background: var(--bg-secondary); padding: 16px; border-radius: 8px; }
            .update-item { margin-bottom: 8px; padding: 8px; background: white; border-radius: 4px; }
        </style>
    `;
}

function updateCasesList() {
    // Optional: populate dynamically from localStorage if needed
}

// ======================
// Feedback Management
// ======================
function initializeStarRating() {
    const stars = document.querySelectorAll('.star');
    const ratingText = document.getElementById('ratingText');

    stars.forEach((star, index) => {
        star.addEventListener('click', () => {
            selectedRating = index + 1;
            updateStarDisplay();
            ratingText.textContent = getRatingText(selectedRating);
        });

        star.addEventListener('mouseover', () => {
            highlightStars(index + 1);
        });
    });

    document.getElementById('ratingStars').addEventListener('mouseleave', () => updateStarDisplay());
}

function highlightStars(rating) {
    const stars = document.querySelectorAll('.star');
    stars.forEach((star, index) => {
        star.classList.toggle('active', index < rating);
    });
}

function updateStarDisplay() {
    highlightStars(selectedRating);
}

function getRatingText(rating) {
    const ratingTexts = { 1: 'Poor', 2: 'Fair', 3: 'Good', 4: 'Very Good', 5: 'Excellent' };
    return ratingTexts[rating] || 'Click to rate';
}

function submitFeedback(event) {
    event.preventDefault();

    const caseSelect = document.getElementById("caseSelect");
    const selectedOption = caseSelect.options[caseSelect.selectedIndex];

    if (!selectedOption.value) {
        alert("Please select a case.");
        return;
    }

    const officerId = selectedOption.dataset.officerId;

    const data = new URLSearchParams({
        caseId: caseSelect.value,
        officerId: officerId,
        responsiveness: document.getElementById("responsivenessStars").dataset.rating || 0,
        confidentiality: document.getElementById("confidentialityStars").dataset.rating || 0,
        outcomeSatisfaction: document.getElementById("outcomeStars").dataset.rating || 0,
        overallExperience: document.getElementById("overallStars").dataset.rating || 0
    });

    fetch("/submitFeedback", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: data
    })
    .then(res => res.text())
    .then(msg => {
    // Create green success popup
    const successMsg = document.createElement("div");
    successMsg.textContent = "Feedback recorded successfully!";
    successMsg.style.position = "fixed";
    successMsg.style.top = "20px";
    successMsg.style.left = "50%";
    successMsg.style.transform = "translateX(-50%)";
    successMsg.style.backgroundColor = "#4CAF50";
    successMsg.style.color = "white";
    successMsg.style.padding = "10px 20px";
    successMsg.style.borderRadius = "5px";
    successMsg.style.boxShadow = "0 4px 8px rgba(0,0,0,0.2)";
    successMsg.style.transition = "opacity 0.5s ease";
    successMsg.style.zIndex = "9999";
    document.body.appendChild(successMsg);

    // Keep popup visible for 1.5 seconds, then fade it out
    setTimeout(() => {
        successMsg.style.opacity = "0";
    }, 1000);

    // After fade completes, redirect to dashboard
    setTimeout(() => {
        successMsg.remove();
        window.location.href = "/userdashboard"; // update if needed
    }, 1000);
    })




    .catch(err => console.error("Feedback submission error:", err));
}



document.addEventListener("DOMContentLoaded", () => {
    if (document.getElementById("feedbackForm")) {
        loadUserCases();
        setupStarRatings();
    }
});

function loadUserCases() {
    fetch("/user/cases")
        .then(res => res.json())
        .then(data => {
            const caseSelect = document.getElementById("caseSelect");
            caseSelect.innerHTML = '<option value="">Choose a case</option>';
            data.forEach(c => {
                const opt = document.createElement("option");
                opt.value = c.case_id;
                opt.textContent = `${c.case_name} (Officer: ${c.officer_name})`;
                opt.dataset.officerId = c.officer_id;
                opt.dataset.officerName = c.officer_name;
                caseSelect.appendChild(opt);
            });

            caseSelect.addEventListener("change", e => {
                const selected = caseSelect.options[caseSelect.selectedIndex];
                document.getElementById("officerName").value = selected.dataset.officerName || "";
            });
        })
        .catch(err => console.error("Error loading cases:", err));
}

function setupStarRatings() {
    document.querySelectorAll(".rating-stars").forEach(container => {
        const stars = container.querySelectorAll(".star");
        stars.forEach((star, index) => {
            star.addEventListener("click", () => {
                stars.forEach((s, i) => s.classList.toggle("active", i <= index));
                container.dataset.rating = index + 1;
            });
        });
    });
}

// function submitFeedback(event) {
//     event.preventDefault();

//     const caseSelect = document.getElementById("caseSelect");
//     const selectedOption = caseSelect.options[caseSelect.selectedIndex];
//     const officerId = selectedOption.dataset.officerId;

//     const data = new URLSearchParams({
//         caseId: caseSelect.value,
//         officerId: officerId,
//         responsiveness: document.getElementById("responsivenessStars").dataset.rating || 0,
//         confidentiality: document.getElementById("confidentialityStars").dataset.rating || 0,
//         outcomeSatisfaction: document.getElementById("outcomeStars").dataset.rating || 0,
//         overallExperience: document.getElementById("overallStars").dataset.rating || 0,
//     });

//     fetch("/submitFeedback", {
//         method: "POST",
//         headers: { "Content-Type": "application/x-www-form-urlencoded" },
//         body: data
//     })
//     .then(res => res.text())
//     .then(msg => alert(msg))
//     .catch(err => console.error("Feedback submission error:", err));
// }


// ======================
// Dashboard Stats
// ======================
// function updateDashboardStats() {
//     const complaints = JSON.parse(localStorage.getItem('complaints')) || [];
//     const totalCases = complaints.length + 3; // Including static cases
//     const pendingCases = complaints.filter(c => c.status === 'pending').length + 2;
//     const resolvedCases = totalCases - pendingCases;

//     document.getElementById('totalCases').textContent = totalCases;
//     document.getElementById('pendingCases').textContent = pendingCases;
//     document.getElementById('resolvedCases').textContent = resolvedCases;
// }


function updateDashboardStats() {
    const complaints = JSON.parse(localStorage.getItem('complaints')) || [];
    const totalCases = complaints.length + 3;
    const pendingCases = complaints.filter(c => c.status === 'pending').length + 2;
    const resolvedCases = totalCases - pendingCases;

    const totalEl = document.getElementById('totalCases');
    const pendingEl = document.getElementById('pendingCases');
    const resolvedEl = document.getElementById('resolvedCases');

    if (totalEl) totalEl.textContent = totalCases;
    if (pendingEl) pendingEl.textContent = pendingCases;
    if (resolvedEl) resolvedEl.textContent = resolvedCases;
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
        background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center;
        z-index: 10001; padding: 20px;
    `;

    const modalContent = document.createElement('div');
    modalContent.className = 'modal-content';
    modalContent.style.cssText = `
        background: white; border-radius: 12px; max-width: 600px; width: 100%; max-height: 80vh; overflow-y: auto;
        box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1);
    `;

    modalContent.innerHTML = `
        <div style="padding:24px; border-bottom:1px solid #ccc; display:flex; justify-content:space-between; align-items:center;">
            <h3 style="margin:0; font-size:20px; font-weight:600;">${title}</h3>
            <button onclick="closeModal()" style="background:none; border:none; font-size:24px; cursor:pointer; padding:4px; line-height:1;">×</button>
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

function logout() {
    if (confirm('Are you sure you want to logout?')) {
        showNotification('Logged out successfully!', 'success');
        setTimeout(() => window.location.reload(), 1500);
    }
}

// ======================
// Global Variables
// ======================
let currentSection = 'dashboard';
let editMode = false;
let selectedRating = 0;

// ======================
// Smooth Scroll Function
// ======================
function scrollToSection(sectionId) {
    const section = document.getElementById(sectionId);
    if (section) {
        window.scrollTo({
            top: section.offsetTop - 70,
            behavior: 'smooth'
        });
    }
}

// ======================
// Initialize App
// ======================
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();

    // Handle report buttons
    const reportButtons = document.querySelectorAll('.btn-primary');
    reportButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const row = btn.closest('tr');
            alert(`You clicked to view details for Report ID: ${row.children[0].textContent}`);
        });
    });
});

function initializeApp() {
    loadUserData();
    updateDashboardStats();
    initializeStarRating();
    setDefaultDate();
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
// Complaint Management
// ======================
function submitComplaint(event) {
    event.preventDefault();

    const complaintData = {
        id: 'CC2024' + String(Date.now()).slice(-3),
        type: document.getElementById('incidentType').value,
        date: document.getElementById('incidentDate').value,
        description: document.getElementById('description').value,
        urgency: document.getElementById('urgency').value,
        status: 'pending',
        filedDate: new Date().toISOString().split('T')[0],
        officer: 'Not Assigned'
    };

    let complaints = JSON.parse(localStorage.getItem('complaints')) || [];
    complaints.push(complaintData);
    localStorage.setItem('complaints', JSON.stringify(complaints));

    showNotification('Complaint filed successfully! Case ID: ' + complaintData.id, 'success');
    event.target.reset();
    updateDashboardStats();

    setTimeout(() => {
        showSection('status');
        updateCasesList();
    }, 2000);
}

function setDefaultDate() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('incidentDate').max = today;
    document.getElementById('incidentDate').value = today;
}

// ======================
// Case Status Management
// ======================
function filterCases(status) {
    document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));

    const activeBtn = Array.from(document.querySelectorAll('.filter-btn'))
                            .find(btn => btn.getAttribute('onclick') === filterCases('${status}'));
    if (activeBtn) activeBtn.classList.add('active');

    document.querySelectorAll('.case-card').forEach(caseCard => {
        if (status === 'all' || caseCard.dataset.status === status) {
            caseCard.style.display = 'block';
        } else {
            caseCard.style.display = 'none';
        }
    });
}

function viewCaseDetails(caseId) {
    const caseDetails = getCaseDetails(caseId);
    if (caseDetails) showModal('Case Details', generateCaseDetailsHTML(caseDetails));
}

// function getCaseDetails(caseId) {
//     const mockDetails = {
//         'CC2024001': {
//             id: 'CC2024001',
//             title: 'Email Phishing Attack',
//             description: 'Received suspicious emails claiming to be from bank asking for account details.',
//             status: 'Under Investigation',
//             officer: 'Inspector Kumar',
//             filedDate: 'Jan 15, 2024',
//             priority: 'High',
//             updates: [
//                 { date: 'Jan 16, 2024', update: 'Case assigned to Inspector Kumar' },
//                 { date: 'Jan 18, 2024', update: 'Initial investigation started' },
//                 { date: 'Jan 22, 2024', update: 'Evidence collected and being analyzed' }
//             ]
//         },
//         'CC2024002': {
//             id: 'CC2024002',
//             title: 'Social Media Fraud',
//             description: 'Fake profile created on social media using my photos and personal information.',
//             status: 'Pending Assignment',
//             officer: 'Not Assigned',
//             filedDate: 'Jan 20, 2024',
//             priority: 'Medium',
//             updates: [{ date: 'Jan 20, 2024', update: 'Case filed and under review' }]
//         },
//         'CC2023045': {
//             id: 'CC2023045',
//             title: 'Online Shopping Fraud',
//             description: 'Paid for product online but never received the item. Seller disappeared.',
//             status: 'Resolved',
//             officer: 'Sub-Inspector Sharma',
//             filedDate: 'Dec 10, 2023',
//             priority: 'Medium',
//             updates: [
//                 { date: 'Dec 11, 2023', update: 'Case assigned to Sub-Inspector Sharma' },
//                 { date: 'Dec 15, 2023', update: 'Merchant investigation initiated' },
//                 { date: 'Dec 22, 2023', update: 'Fraudulent seller identified' },
//                 { date: 'Jan 5, 2024', update: 'Case resolved - Refund processed' }
//             ]
//         }
//     };
//     return mockDetails[caseId];
// }

function generateCaseDetailsHTML(caseDetails) {
    let updatesHTML = '';
    caseDetails.updates.forEach(update => {
        updatesHTML += `<div class="update-item"><strong>${update.date}:</strong> ${update.update}</div>`;
    });

    return `
        <div class="case-details-modal">
            <div class="detail-row"><strong>Case ID:</strong> ${caseDetails.id}</div>
            <div class="detail-row"><strong>Title:</strong> ${caseDetails.title}</div>
            <div class="detail-row"><strong>Status:</strong> <span class="status-${caseDetails.status.toLowerCase().replace(/ /g,'-')}">${caseDetails.status}</span></div>
            <div class="detail-row"><strong>Investigating Officer:</strong> ${caseDetails.officer}</div>
            <div class="detail-row"><strong>Filed Date:</strong> ${caseDetails.filedDate}</div>
            <div class="detail-row"><strong>Priority:</strong> ${caseDetails.priority}</div>
            <div class="detail-row"><strong>Description:</strong><p>${caseDetails.description}</p></div>
            <div class="detail-row"><strong>Case Updates:</strong><div class="updates-list">${updatesHTML}</div></div>
        </div>
        <style>
            .case-details-modal { padding: 20px; }
            .detail-row { margin-bottom: 16px; }
            .detail-row strong { color: var(--primary-color); }
            .updates-list { margin-top: 12px; background: var(--bg-secondary); padding: 16px; border-radius: 8px; }
            .update-item { margin-bottom: 8px; padding: 8px; background: white; border-radius: 4px; }
        </style>
    `;
}

function updateCasesList() {
    // Optional: dynamically populate case cards
}

// ======================
// Feedback Management
// ======================
// function initializeStarRating() {
//     const stars = document.querySelectorAll('.star');
//     const ratingText = document.getElementById('ratingText');

//     stars.forEach((star, index) => {
//         star.addEventListener('click', () => {
//             selectedRating = index + 1;
//             updateStarDisplay();
//             ratingText.textContent = getRatingText(selectedRating);
//         });

//         star.addEventListener('mouseover', () => {
//             highlightStars(index + 1);
//         });
//     });

//     document.getElementById('ratingStars').addEventListener('mouseleave', () => updateStarDisplay());
// }

function initializeStarRating() {
    const stars = document.querySelectorAll('.star');
    const ratingText = document.getElementById('ratingText');
    const ratingStarsContainer = document.getElementById('ratingStars');

    // If there's no rating section on this page, stop early
    if (!ratingStarsContainer || stars.length === 0) return;

    stars.forEach((star, index) => {
        star.addEventListener('click', () => {
            selectedRating = index + 1;
            updateStarDisplay();
            if (ratingText) ratingText.textContent = getRatingText(selectedRating);
        });

        star.addEventListener('mouseover', () => {
            highlightStars(index + 1);
        });
    });

    ratingStarsContainer.addEventListener('mouseleave', () => updateStarDisplay());
}


function highlightStars(rating) {
    document.querySelectorAll('.star').forEach((star, index) => {
        star.classList.toggle('active', index < rating);
    });
}

function updateStarDisplay() {
    highlightStars(selectedRating);
}

function getRatingText(rating) {
    const ratingTexts = { 1: 'Poor', 2: 'Fair', 3: 'Good', 4: 'Very Good', 5: 'Excellent' };
    return ratingTexts[rating] || 'Click to rate';
}


// ======================
// Dashboard Stats
// ======================
function updateDashboardStats() {
    const complaints = JSON.parse(localStorage.getItem('complaints')) || [];
    const totalCases = complaints.length + 3;
    const pendingCases = complaints.filter(c => c.status === 'pending').length + 2;
    const resolvedCases = totalCases - pendingCases;

    const totalEl = document.getElementById('totalCases');
    const pendingEl = document.getElementById('pendingCases');
    const resolvedEl = document.getElementById('resolvedCases');

    if (totalEl) totalEl.textContent = totalCases;
    if (pendingEl) pendingEl.textContent = pendingCases;
    if (resolvedEl) resolvedEl.textContent = resolvedCases;
}



// ======================
// Utility Functions
// ======================
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed; top: 20px; right: 20px;
        padding: 16px 24px; border-radius: 8px; color: white;
        font-weight: 500; z-index: 10000; transform: translateX(400px);
        transition: transform 0.3s ease-in-out; max-width: 400px;
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
        position: fixed; top:0; left:0; right:0; bottom:0;
        background: rgba(0,0,0,0.5); display: flex;
        align-items: center; justify-content: center;
        z-index: 10001; padding: 20px;
    `;

    const modalContent = document.createElement('div');
    modalContent.className = 'modal-content';
    modalContent.style.cssText = `
        background: white; border-radius: 12px; max-width: 600px;
        width: 100%; max-height: 80vh; overflow-y: auto;
        box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1);
    `;

    modalContent.innerHTML = `
        <div style="padding:24px; border-bottom:1px solid #ccc; display:flex; justify-content:space-between; align-items:center;">
            <h3 style="margin:0; font-size:20px; font-weight:600;">${title}</h3>
            <button onclick="closeModal()" style="background:none; border:none; font-size:24px; cursor:pointer; padding:4px; line-height:1;">×</button>
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

function logout() {
    if (confirm('Are you sure you want to logout?')) {
        showNotification('Logged out successfully!', 'success');
        setTimeout(() => window.location.reload(), 1500);
    }
}



// Feedback section

