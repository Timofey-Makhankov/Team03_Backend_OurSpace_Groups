package com.example.demo.domain.group.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class GroupDTO extends AbstractDTO {
    private String name;
    private String description;
    private String motto;
    private String logo;
    private List<UserDTO> users;
}
