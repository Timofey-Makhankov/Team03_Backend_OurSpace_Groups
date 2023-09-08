package com.example.demo.core.security.permissionevaluators;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.dto.UserDTO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@NoArgsConstructor
public class UserPermissionEvaluator {
  public boolean isSameUSer(User principal, UserDTO userDTO) {
    return principal.getEmail().equals(userDTO.getEmail());
  }

  public boolean isSameUser(User principal, UUID id) {
    return principal.getId().equals(id);
  }
}
