package com.example.demo.domain.user.dto;


import com.example.demo.core.generic.AbstractMapper;
import com.example.demo.domain.user.User;
import org.mapstruct.*;


public interface UserMapper extends AbstractMapper<User, UserDTO> {
    User fromUserRegisterDTO(UserRegisterDTO dto);

    @Override
    @Mapping(source = "group", target = "group_id", qualifiedByName = "idFromGroup")
    @Mapping(source = "group", target = "group_name", qualifiedByName = "nameFromGroup")
    UserDTO toDTO(User user);

    @Override
    @Mapping(source = "group_id", target = "group", qualifiedByName = "groupFromId")
    User fromDTO(UserDTO dto);
}
