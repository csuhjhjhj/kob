package com.kob.backend.controller.user;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.soap.SOAPBinding;
import java.util.List;
@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/user/all/")
    public List<User> getAll(){
        System.out.println(userMapper.selectList(null));
        return userMapper.selectList(null);
    }

    @GetMapping("/user/add/{userId}/{username}/{password}")
    public String addUser(
            @PathVariable int userId,
            @PathVariable String username,
            @PathVariable String password){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(password);
        User user = new User(userId,username,encodePassword);
        userMapper.insert(user);
        return "Add User Successfully";
    }
}
