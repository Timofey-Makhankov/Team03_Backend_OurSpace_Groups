package com.example.demo.domain.user.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.group.dto.GroupDTO;
import com.example.demo.domain.role.dto.RoleDTO;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UserDTO extends AbstractDTO {

    @NotBlank
    @Length(min = 1, max = 255)
    private String firstName;

    @NotBlank
    @Length(min = 1, max = 255)
    private String lastName;

    @Email
    @NotBlank
    @Length(min = 1, max = 255)
    private String email;

    @Valid
    private Set<RoleDTO> roles;

    private UUID group_id;

    private String group_name;
}
