package com.example.demo.core.security.permissionevaluators;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.dto.UserDTO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * This class is for evaluating user permissions in the @PreAuthorize Annotation
 *
 * @author Timofey
 * @since 1.0
 */
@Component
@NoArgsConstructor
public class UserPermissionEvaluator {
    /**
     * Check, if the requested user is the Same user as the user given
     *
     * @param principal get the principal from the request
     * @param userDTO   get the user from the request
     * @return true, if the user given is the same as requested, otherwise return false
     * @since 1.0
     */
    public boolean isSameUser(User principal, UserDTO userDTO) {
        return principal.getEmail().equals(userDTO.getEmail());
    }

    /**
     * Check, if the requested user is the Same user as the user given
     *
     * @param principal get the principal from the request
     * @param id        The User id from the request
     * @return true, if the user given is the same as requested, otherwise return false
     * @since 1.0
     */
    public boolean isSameUser(User principal, UUID id) {
        return principal.getId().equals(id);
    }

    /**
     * Check, if the given user is in the group that is requested
     *
     * @param principal get the principal from the request
     * @param groupId   get the Id from Group
     * @return true, if the user belongs to the same group, otherwise return false
     * @since 1.0
     */
    public boolean isInGroup(User principal, UUID groupId) {
        return principal.getGroup().getId().equals(groupId);
    }
}
