package com.example.demo.domain.user;

import com.example.demo.domain.group.Group;
import com.example.demo.domain.group.GroupService;
import com.example.demo.domain.user.dto.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = GroupService.class)
public abstract class AbstractUserMapper implements UserMapper {
    @Autowired
    GroupService service;
    @Named("idFromGroup")
    UUID idFromGroup(Group group) {
        try {
            return group.getId();
        } catch (NullPointerException _e) {
            return null;
        }
    }

    @Named("nameFromGroup")
    String nameFromGroup(Group group) {
        try {
            return group.getName();
        } catch (NullPointerException _e) {
            return "";
        }
    }

    @Named("groupFromId")
    Group groupFromId(UUID id) {
        if (id == null) {
            return null;
        } else {
            try {
                return service.findById(id);
            } catch (NoSuchElementException _e) {
                return null;
            }
        }
    }
}
