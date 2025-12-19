package com.cybercrime.dbmsproject.service;

import com.cybercrime.dbmsproject.dao.DomainDAO;
import com.cybercrime.dbmsproject.model.Domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainService {

    @Autowired
    private DomainDAO domainDAO;

    public Domain getDomainById(int domainId) {
        return domainDAO.findById(domainId);
    }
}
