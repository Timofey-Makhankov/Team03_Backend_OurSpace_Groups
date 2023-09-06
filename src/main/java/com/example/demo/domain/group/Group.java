package com.example.demo.domain.group;

import com.example.demo.core.generic.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "group")
public class Group extends AbstractEntity {

    @Column
    @NotNull
    private String name;
    private String description;
    private String motto;
    private String logo;
}
