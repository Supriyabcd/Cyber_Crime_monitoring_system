package com.cybercrime.dbmsproject.service;

import com.cybercrime.dbmsproject.dao.UserDetailDAO;
import com.cybercrime.dbmsproject.dao.UserMNameDAO;
import com.cybercrime.dbmsproject.model.UserDetail;
import com.cybercrime.dbmsproject.model.UserMName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserService {

    private final UserDetailDAO userDetailDAO;
    private final UserMNameDAO userMNameDAO;

    public UserService(UserDetailDAO userDetailDAO, UserMNameDAO userMNameDAO) {
        this.userDetailDAO = userDetailDAO;
        this.userMNameDAO = userMNameDAO;
    }

    // ✅ Register user with optional middle names
    @Transactional
    public int addUserWithMNames(UserDetail user, List<String> middleNames) {
        int userId = userDetailDAO.saveAndReturnId(user); // insert into UserDetail

        if (middleNames != null) {
            for (String m : middleNames) {
                if (m != null && !m.trim().isEmpty()) {
                    userMNameDAO.save(new UserMName(m.trim(), userId));
                }
            }
        }
        return userId;
    }

    // ✅ Login check: username + mobNo + password
    public UserDetail login(String username, String mobNo, String password) {
        return userDetailDAO.loginUser(username, mobNo, password);
    }
}