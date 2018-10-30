package com.ebrightmoon.manager.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ebrightmoon.common.JacksonUtils;
import com.ebrightmoon.manager.dao.RedisDao;
import com.ebrightmoon.manager.mapper.UserMapper;
import com.ebrightmoon.manager.pojo.User;
import com.ebrightmoon.manager.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RedisDao redisDao;
	@Override
	public List<User> getUsers() {
		return userMapper.getUsers();
	}

	@Override
	public void insertUser(User user) {
		
		userMapper.insertUser(user);
		String userJson = redisDao.get("user_" + user.getId());
		if(StringUtils.isEmpty(userJson)){
			redisDao.set("user_" + user.getId(), JacksonUtils.objectToJson(user));
		}
		
	}

	@Override
	public User getUserById(String id) {
		String userJson = redisDao.get("user_" + id);
		User user = null;
		if(StringUtils.isEmpty(userJson)){
			user = userMapper.getUserById(id);
			//涓嶅瓨鍦�,璁剧疆
			if(user != null)
				redisDao.set("user_" + id, JacksonUtils.objectToJson(user));
		}else{
			user = JacksonUtils.jsonToPojo(userJson, User.class);
		}
		return user;
	}

}
