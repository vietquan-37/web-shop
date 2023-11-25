package com.vietquan.security.entity;

import com.vietquan.security.enumPackage.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(
        staticName = "build"
)
@Builder
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private boolean mfaEnable;
    private String phoneNumber;
    private String secret;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(
            mappedBy = "user"
    )
    private List<Token> tokens;
    @OneToMany(
            mappedBy = "users",cascade = CascadeType.ALL,orphanRemoval = true
    )
    private List<Review> reviews;
    @Lob
    @Column(columnDefinition = "longblob", length = 1048576)  // 1 MB in bytes
    private byte[] avatar;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
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
}
