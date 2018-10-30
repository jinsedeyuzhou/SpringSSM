package com.ebrightmoon.manager.service;

import java.util.List;

import com.ebrightmoon.manager.pojo.User;

public interface UserService {
	public List<User> getUsers();

	public User getUserById(String id);

	public void insertUser(User user);
}
