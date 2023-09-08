package com.example.demo.domain.authority;

import com.example.demo.core.generic.AbstractEntity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "authority")
public class Authority extends AbstractEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Authority(UUID id, String name) {
        super(id);
        this.name = name;
    }
}
