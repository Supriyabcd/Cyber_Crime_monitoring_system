package com.cybercrime.dbmsproject.service;

import com.cybercrime.dbmsproject.dao.CaseFileDAO;
import com.cybercrime.dbmsproject.dao.OfficerDAO;
import com.cybercrime.dbmsproject.dao.SupervisorDAO;
import com.cybercrime.dbmsproject.model.Officer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfficerService {

    @Autowired
    private OfficerDAO officerDAO;

    @Autowired
    private SupervisorDAO supervisorDAO;

    @Autowired
    private CaseFileDAO caseFileDAO;



    public Officer getOfficerById(int officerId) {
        return officerDAO.findByOfficerId(officerId);
    }

    // New method to check if officer is a supervisor
    // public boolean isSupervisor(int officerId) {
    //     return supervisorDAO.isSupervisor(officerId);
    // }
        public List<Officer> getAllOfficers() {
        return officerDAO.findAll();
    }


    public List<Officer> getOfficersByDomainId(int domainId) {
    return officerDAO.findByDomainId(domainId);
}


public List<Officer> getOfficersUnderSupervisor(int supervisorId) {
    return supervisorDAO.getBySupervisorDomain(supervisorId);
    }

    public int getCaseCountByDomain(int domainID) {
    return caseFileDAO.getCaseCountByDomain(domainID);
    }
    // New method to check if officer is a supervisor
    public boolean isSupervisor(int officerId) {
        return supervisorDAO.isSupervisor(officerId);
    }

    public int getUnassignedCaseCount(int domainId) {
    return caseFileDAO.getUnassignedCaseCount(domainId);

    }

    public double getCaseClosureRateByDomain(int domainId) {
        return caseFileDAO.getCaseClosureRate(domainId);
    }
    
    public int getActiveOfficerCount(int supervisorId) {
    return supervisorDAO.getActiveOfficerCount(supervisorId);
   }  


}