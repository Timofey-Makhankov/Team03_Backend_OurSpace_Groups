package com.example.demo.domain.group.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class GroupDTO extends AbstractDTO {

    private String name;
    private String description;
    private String motto;
    private String logo;
    private List<User> users;

    public GroupDTO(UUID id, String name, String description, String motto, String logo, List<User> users) {
        super(id);
        this.name = name;
        this.description = description;
        this.motto = motto;
        this.logo = logo;
        this.users = users;
    }
}
