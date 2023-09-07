package com.example.demo.domain.user.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.group.dto.GroupDTO;
import com.example.demo.domain.role.dto.RoleDTO;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UserDTO extends AbstractDTO {
  private String firstName;
  private String lastName;

  @Email
  private String email;

  @Valid
  private Set<RoleDTO> roles;

  private UUID group_id;

  private String group_name;
}
