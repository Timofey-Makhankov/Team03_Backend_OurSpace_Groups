package com.example.demo.domain.group.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.user.dto.UserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @Size(max = 512)
    private String description = "";

    @Size(min = 1, max = 255)
    private String motto = "";

    @Size(min = 1, max = 255)
    private String logo = "";

    private List<UserDTO> users;
}
