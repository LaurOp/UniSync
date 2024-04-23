package com.example.unisync.Mapper;

import com.example.unisync.DTO.UserDTO;
import com.example.unisync.Model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AppUser map (UserDTO userDTO){
        AppUser user = new AppUser();
        user.setUniversity(userDTO.isUniversity());
        user.setTeacher(userDTO.isTeacher());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        return user;
    }

    public UserDTO map (AppUser user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUniversity(user.isUniversity());
        userDTO.setTeacher(user.isTeacher());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }


}
