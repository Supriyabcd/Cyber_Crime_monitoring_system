
async function loadAuditLogs() {
  try {
    const response = await fetch("http://localhost:8080/audit/all");
    if (!response.ok) throw new Error("Failed to fetch audit logs");

    const logs = await response.json();
    const tableBody = document.getElementById("auditTableBody");
    tableBody.innerHTML = "";

    if (logs.length === 0) {
      tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center;">No logs found</td></tr>`;
      return;
    }

    logs.forEach(log => {
      const row = `
        <tr>
          <td>${log.logId}</td>
          <td>${log.caseId}</td>
          <td>${log.previousStatus}</td>
          <td>${log.currentStatus}</td>
          <td>${new Date(log.timestamp).toLocaleString()}</td>
        </tr>
      `;
      tableBody.innerHTML += row;
    });
  } catch (error) {
    console.error("Error loading audit logs:", error);
  }
}

// Load logs when the page is opened
window.onload = loadAuditLogs;

