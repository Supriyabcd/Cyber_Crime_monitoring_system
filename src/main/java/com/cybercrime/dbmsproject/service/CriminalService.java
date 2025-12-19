package com.cybercrime.dbmsproject.service;

import com.cybercrime.dbmsproject.dao.CriminalDAO;
import com.cybercrime.dbmsproject.model.Criminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CriminalService {

    @Autowired
    private CriminalDAO criminalDAO;

    public int saveCriminal(Criminal c) {
        return criminalDAO.save(c);
    }

    public List<Criminal> getAllCriminals() {
        return criminalDAO.findAll();
    }
}
