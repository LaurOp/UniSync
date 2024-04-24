package com.example.unisync.Mapper;

import com.example.unisync.DTO.UserDTO;
import com.example.unisync.Model.UserInfo;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserInfo map (UserDTO userDTO){
        UserInfo user = new UserInfo();
        user.setRoles(user.getRoles() + (userDTO.isTeacher() ? "TEACHER," : ""));
        user.setRoles(user.getRoles() + (userDTO.isUniversity() ? "UNIVERSITY,," : ""));
        user.setRoles(user.getRoles() + (!userDTO.isTeacher() && !userDTO.isUniversity() ? "USER," : ""));
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        return user;
    }

    public UserDTO map (UserInfo user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUniversity(user.getRoles().contains("UNIVERSITY"));
        userDTO.setTeacher(user.getRoles().contains("TEACHER"));
        userDTO.setUsername(user.getRoles().concat("USER"));
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }


}
