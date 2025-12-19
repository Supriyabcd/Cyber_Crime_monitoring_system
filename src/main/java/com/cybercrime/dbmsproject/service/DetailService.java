package com.cybercrime.dbmsproject.service;

import com.cybercrime.dbmsproject.dao.DetailDAO;
import com.cybercrime.dbmsproject.model.Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DetailService {

    @Autowired
    private DetailDAO detailDAO;

    public List<Detail> getDetailsByCaseId(int caseId) {
        return detailDAO.findByCaseId(caseId);
    }
}
