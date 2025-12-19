// package com.cybercrime.dbmsproject.dao;

// import com.cybercrime.dbmsproject.model.UserDetail;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.jdbc.support.GeneratedKeyHolder;
// import org.springframework.jdbc.support.KeyHolder;
// import org.springframework.stereotype.Repository;

// import java.sql.Statement;

// import java.sql.PreparedStatement;
// import java.util.List;

// @Repository
// public class UserDetailDAO {
//     private final JdbcTemplate jdbcTemplate;

//     public UserDetailDAO(JdbcTemplate jdbcTemplate) {
//         this.jdbcTemplate = jdbcTemplate;
//     }

//     // Fetch all users
//     public List<UserDetail> findAll() {
//         String sql = "SELECT * FROM UserDetail";
//         return jdbcTemplate.query(sql, (rs, rowNum) ->
//                 new UserDetail(
//                         rs.getInt("user_id"),
//                         rs.getString("fname"),
//                         rs.getString("lname"),
//                         rs.getString("mob_no"),
//                         rs.getString("dob"),
//                         rs.getString("h_no"),
//                         rs.getString("street_no"),
//                         rs.getString("city"),
//                         rs.getString("state"),
//                         rs.getString("username"),
//                         rs.getString("password")
//                 )
//         );
//     }

//     // Insert user
//     public int save(UserDetail user) {
//         String sql = "INSERT INTO UserDetail (fname, lname, mob_no, dob, h_no, street_no, city, state, username, password) " +
//                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//         return jdbcTemplate.update(sql,
//                 user.getFname(), user.getLname(), user.getMobNo(), user.getDob(),
//                 user.getHNo(), user.getStreetNo(), user.getCity(), user.getState(),
//                 user.getUsername(), user.getPassword());
//     }

//     // Save and return userID

//     public int saveAndReturnId(UserDetail user) {
//         String sql = "INSERT INTO UserDetail (fname, lname, mob_no, dob, h_no, street_no, city, state, username, password) " +
//                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

//         KeyHolder keyHolder = new GeneratedKeyHolder();

//         jdbcTemplate.update(connection -> {
//             PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
//             ps.setString(1, user.getFname());
//             ps.setString(2, user.getLname());
//             ps.setString(3, user.getMobNo());
//             ps.setString(4, user.getDob());
//             ps.setString(5, user.getHNo());
//             ps.setString(6, user.getStreetNo());
//             ps.setString(7, user.getCity());
//             ps.setString(8, user.getState());
//             ps.setString(9, user.getUsername());
//             ps.setString(10, user.getPassword());
//             return ps;
//         }, keyHolder);

//         return keyHolder.getKey().intValue();
//     }

//             // Fetch user by username (for authentication)
//         public UserDetail findByUsername(String username) {
//             String sql = "SELECT * FROM UserDetail WHERE username = ?";
//             return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) ->
//                     new UserDetail(
//                             rs.getInt("user_id"),
//                             rs.getString("fname"),
//                             rs.getString("lname"),
//                             rs.getString("mob_no"),
//                             rs.getString("dob"),
//                             rs.getString("h_no"),
//                             rs.getString("street_no"),
//                             rs.getString("city"),
//                             rs.getString("state"),
//                             rs.getString("username"),
//                             rs.getString("password")
//                     )
//             );
//         }



// }



package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.UserDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class UserDetailDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserDetailDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Fetch all users
    public List<UserDetail> findAll() {
        String sql = "SELECT * FROM UserDetail";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new UserDetail(
                        rs.getInt("user_id"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("mob_no"),
                        rs.getString("dob"),
                        rs.getString("h_no"),
                        rs.getString("street_no"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("username"),
                        rs.getString("password")
                )
        );
    }

    // Insert user
    public int save(UserDetail user) {
        String sql = "INSERT INTO UserDetail (fname, lname, mob_no, dob, h_no, street_no, city, state, username, password) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getFname(), user.getLname(), user.getMobNo(), user.getDob(),
                user.getHNo(), user.getStreetNo(), user.getCity(), user.getState(),
                user.getUsername(), user.getPassword());
    }

    // Save and return userID

    public int saveAndReturnId(UserDetail user) {
        String sql = "INSERT INTO UserDetail (fname, lname, mob_no, dob, h_no, street_no, city, state, username, password) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFname());
            ps.setString(2, user.getLname());
            ps.setString(3, user.getMobNo());
            ps.setString(4, user.getDob());
            ps.setString(5, user.getHNo());
            ps.setString(6, user.getStreetNo());
            ps.setString(7, user.getCity());
            ps.setString(8, user.getState());
            ps.setString(9, user.getUsername());
            ps.setString(10, user.getPassword());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

            // Fetch user by username (for authentication)
        public UserDetail findByUsername(String username) {
            String sql = "SELECT * FROM UserDetail WHERE username = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) ->
                    new UserDetail(
                            rs.getInt("user_id"),
                            rs.getString("fname"),
                            rs.getString("lname"),
                            rs.getString("mob_no"),
                            rs.getString("dob"),
                            rs.getString("h_no"),
                            rs.getString("street_no"),
                            rs.getString("city"),
                            rs.getString("state"),
                            rs.getString("username"),
                            rs.getString("password")
                    )
            );
        }

        // ✅ Fetch user by username, mobile number, and password
public UserDetail loginUser(String username, String mobNo, String password) {
    String sql = "SELECT * FROM UserDetail WHERE username = ? AND mob_no = ? AND password = ?";
    List<UserDetail> users = jdbcTemplate.query(sql, new Object[]{username, mobNo, password}, (rs, rowNum) ->
            new UserDetail(
                    rs.getInt("user_id"),
                    rs.getString("fname"),
                    rs.getString("lname"),
                    rs.getString("mob_no"),
                    rs.getString("dob"),
                    rs.getString("h_no"),
                    rs.getString("street_no"),
                    rs.getString("city"),
                    rs.getString("state"),
                    rs.getString("username"),
                    rs.getString("password")
            )
    );

    return users.isEmpty() ? null : users.get(0);
}

 public UserDetail findById(int userId) {
    String sql = "SELECT * FROM UserDetail WHERE user_id = ?";
    try {
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, (rs, rowNum) ->
                new UserDetail(
                        rs.getInt("user_id"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("mob_no"),
                        rs.getString("dob"),
                        rs.getString("h_no"),
                        rs.getString("street_no"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("username"),
                        rs.getString("password")
                )
        );
    } catch (Exception e) {
        return null; // return null if no user found
    }
}

// Update session token in DB
public void updateSessionToken(int userId, String token) {
    String sql = "UPDATE UserDetail SET session_token = ? WHERE user_id = ?";
    jdbcTemplate.update(sql, token, userId);
}

// Fetch session token by userId
public String getSessionToken(int userId) {
    String sql = "SELECT session_token FROM UserDetail WHERE user_id = ?";
    return jdbcTemplate.queryForObject(sql, new Object[]{userId}, String.class);
}




}
