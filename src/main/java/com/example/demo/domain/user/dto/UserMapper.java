package com.example.demo.domain.user.dto;


import com.example.demo.core.generic.AbstractMapper;
import com.example.demo.domain.group.Group;
import com.example.demo.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends AbstractMapper<User, UserDTO> {
    User fromUserRegisterDTO(UserRegisterDTO dto);

    @Override
    @Mapping(source = "group", target = "group_id", qualifiedByName = "idFromGroup")
    @Mapping(source = "group", target = "group_name", qualifiedByName = "nameFromGroup")
    UserDTO toDTO(User user);

    @Named("idFromGroup")
    default UUID idFromGroup(Group group) {
        return group.getId();
    }

    @Named("nameFromGroup")
    default String nameFromGroup(Group group) {
        return group.getName();
    }
}
