package com.example.demo.domain.user.dto;

import com.example.demo.core.generic.AbstractDTO;
import java.util.UUID;
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
public class UserRegisterDTO extends AbstractDTO {
  private String firstName;
  private String lastName;

  @Email
  private String email;

  private String password;

}
