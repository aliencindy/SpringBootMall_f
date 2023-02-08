package com.chrissy.springbootmall.dao;

import com.chrissy.springbootmall.dto.UserRegisterRequest;
import com.chrissy.springbootmall.model.User;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);

    User getUserByEmail(String email);

    User getUserById(Integer userId);
}
