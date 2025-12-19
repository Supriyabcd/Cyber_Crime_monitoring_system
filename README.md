# Cyber Crime Management System

A **web-based platform** designed to streamline the reporting, investigation, and supervision of cybercrime cases.  
The system allows victims to file complaints online, officers to manage cases efficiently, and supervisors to monitor progress — all through dedicated dashboards.

---

## Features

### User (Victim)
- **Register & Login:** Secure authentication for complaint submission.  
- **File Complaint:** Report incidents under specific domains (e.g., hacking, bullying, trolling).  
- **View Case Status:** Track progress in real-time using unique Case IDs.  
- **Upload Evidence:** Attach screenshots supporting the complaint.  
- **Give Feedback:** Rate the officer’s professionalism, fairness, and responsiveness after resolution.  
- **Profile Management:** View or update personal details securely.  

---

### Officer
- **View Assigned Cases:** Access all cases assigned to them.  
- **Update Case Status:** Modify progress (Under Review, Taken, Closed, etc.).  
- **Add Criminal Records:** Log identified criminals with details like name, DOB, contact info, and social handles.  
- **View Notifications:** Receive alerts for new cases or case updates.  
- **Profile Management:** Maintain and update officer profile data.  

---

### Supervisor
- **Assign Cases:** Allocate unassigned or pending cases to available officers based on domain or workload.  
- **Monitor Progress:** Track overall case activity and ensure timely handling.  
- **View Feedback:** Review user feedback related to officers under their supervision.  
- **Performance Oversight:** Identify inactive officers and send reminders or reassign cases.  
- **Profile Management:** Manage supervisor details and access history logs.  

---

## System Modules (Database Entities)

- **User:** Stores victim details and credentials.  
- **Officer:** Contains officer information and active status.  
- **Supervisor:** Higher-level officer responsible for case distribution and monitoring.  
- **Cases:** Records complaint data, including status, domain, and timestamps.  
- **Criminal:** Holds details of identified suspects or convicted criminals.  
- **Domain:** Defines categories like hacking, threats, trolling, etc.  
- **Audit Log:** Tracks all critical operations for accountability.  
- **Feedback:** Stores user ratings and comments for officer performance.  

---

## Tech Stack

- **Frontend:** HTML, CSS, JavaScript, Thymeleaf  
- **Backend:** Java Spring Boot  
- **Database:** MySQL  
- **Build Tool:** Maven or Gradle  
- **Server:** Apache Tomcat (via Spring Boot)  

---

## How It Works

1. Users register and log in to file cybercrime complaints.  
2. Each complaint generates a unique Case ID and is categorized by domain.  
3. The system automatically notifies the concerned supervisor for assignment.
4. Supervisor assigns the case to an officer under his/her domain.
5. Officers investigate, update case progress, and record identified criminals.  
6. Supervisors monitor case flow, assign delayed cases, and check officer performance.  
7. Users can track their case status, view updates, and provide feedback.  

---

## Security and Logging

- Role-based access control (User, Officer, Supervisor).  
- Encrypted passwords and secure session handling.  
- Audit logs maintained for every case update, reassignment, or closure.  
- Restricted data modification and deletion permissions.  

---

## Contribution

Open to contributions — fork the repository, create a new branch, make your changes, and submit a pull request.
