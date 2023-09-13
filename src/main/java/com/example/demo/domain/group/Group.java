package com.example.demo.domain.group;

import com.example.demo.core.generic.AbstractEntity;
import com.example.demo.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups")
public class Group extends AbstractEntity {

    @Column(name = "name"/*, nullable = false*/)
    private String name;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "motto")
    private String motto;

    @Column(name = "logo")
    private String logo;

    @Column(name = "members")
    @OneToMany(fetch = FetchType.EAGER)
    private List<User> users;

    @PrePersist
    public void logNewGroupAttempt() {
        log.trace("Attempting to add new group");
    }

    @PostPersist
    public void logNewGroupAdded() {
        log.debug("Created Group with Id: " + super.getId());
    }

    @PreRemove
    public void logGroupRemovalAttempt() {
        log.trace("Tried to delete Group with Id: " + super.getId());
    }

    @PostRemove
    public void logGroupRemoval() {
        log.debug("Deleted Group with Id: " + super.getId());
    }

    @PreUpdate
    public void logGroupUpdateAttempt() {
        log.trace("Tried to update Group with Id: " + super.getId());
    }

    @PostUpdate
    public void logGroupUpdated() {
        log.debug("Group has been updated with Id: " + super.getId());
    }

}
