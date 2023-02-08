package com.chrissy.springbootmall.dao.impl;

import com.chrissy.springbootmall.dao.UserDao;
import com.chrissy.springbootmall.dto.UserRegisterRequest;
import com.chrissy.springbootmall.model.User;
import com.chrissy.springbootmall.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate npjt;


    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql = "INSERT INTO user (email,password,created_date,last_modified_date) " +
                "VALUES (:email,:password,:createdDate,:lastModifiedDate)";

        HashMap<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());
        map.put("createdDate", new Date());
        map.put("lastModifiedDate", new Date());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        npjt.update(sql, new MapSqlParameterSource(map), keyHolder);
        int userId = keyHolder.getKey().intValue();

        return userId;
    }

    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT user_id,email,password,created_date,last_modified_date " +
                "FROM user WHERE user_id=:userId";

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<User> userList = npjt.query(sql, map, new UserRowMapper());

        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }

    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id,email,password,created_date,last_modified_date " +
                "FROM user WHERE email=:email";

        HashMap<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<User> userList = npjt.query(sql, map, new UserRowMapper());

        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }
}
