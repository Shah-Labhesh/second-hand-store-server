package com.labhesh.secondhandstore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.labhesh.secondhandstore.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Table(name = "users")
@Entity
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    @JsonIgnore
    private String password;
    private String email;
    @Builder.Default
    private boolean isVerified = false;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER; // e.g., "ROLE_USER" or "ROLE_ADMIN"
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private Files file;
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate;
    private LocalDateTime deletedDate;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>() {{
            add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return role.name();
                }
            });
        }};
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;

    }

    @Override
    public boolean isAccountNonLocked() {
        return true;

    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;

    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // getters and setters
}
