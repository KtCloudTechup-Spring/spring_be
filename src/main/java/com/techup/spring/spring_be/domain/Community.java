package com.techup.spring.spring_be.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "community")
public class Community extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    protected Community() {

    }

    public Community(String name) {
        this.name = name;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // --- 비즈니스 메서드 (필요하면) ---
    public void changeName(String name) {
        this.name = name;
    }
}
