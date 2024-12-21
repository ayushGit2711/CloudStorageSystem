package com.ayushspring.cloudstoragesystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private long storageLimit = 5000000000L; // Example: 5 GB limit
    private long currentStorage = 0L; // To track current used storage

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public long getCurrentStorage() {
        return currentStorage;
    }

    public long getStorageLimit() {
        return storageLimit;
    }

    public void setCurrentStorage(long l) {
        this.currentStorage = l;
    }
}

