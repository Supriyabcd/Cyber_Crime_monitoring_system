package com.cybercrime.dbmsproject.service;

import com.cybercrime.dbmsproject.dao.CaseFileDAO;
import com.cybercrime.dbmsproject.dao.OfficerDAO;
import com.cybercrime.dbmsproject.dao.SupervisorDAO;
import com.cybercrime.dbmsproject.model.CaseFile;
import com.cybercrime.dbmsproject.model.Officer;
import com.cybercrime.dbmsproject.model.Supervisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SupervisorService {

    @Autowired
    private SupervisorDAO supervisorDAO;

    @Autowired
    private OfficerDAO officerDAO;

    @Autowired
    private CaseFileDAO caseFileDAO;

    /**
     * Get all cases visible to a supervisor:
     * - All cases handled by officers under this supervisor
     * - All cases in supervisor’s domain (even if unassigned)
     */
    public List<CaseFile> getCasesForSupervisor(int supervisorId) {
        // Get all officers under this supervisor
        List<Supervisor> supervised = supervisorDAO.getBySupervisorId(supervisorId);

        // Extract their officer_ids
        List<Integer> officerIds = new ArrayList<>();
        for (Supervisor s : supervised) {
            officerIds.add(s.getOfficerId());
        }

        // Find supervisor’s own domain
        Officer supervisorOfficer = officerDAO.findByOfficerId(supervisorId);
        if (supervisorOfficer == null || supervisorOfficer.getDomainId() == null)
            return Collections.emptyList();

        int domainId = supervisorOfficer.getDomainId();

        // Fetch cases from both sources
        Set<CaseFile> allCases = new HashSet<>();

        // 1️⃣ Cases assigned to officers
        for (Integer officerId : officerIds) {
            allCases.addAll(caseFileDAO.findByOfficerId(officerId));
        }

        // 2️⃣ Cases in same domain (including unassigned)
        allCases.addAll(caseFileDAO.findByDomainId(domainId));

        return new ArrayList<>(allCases);
    }
}