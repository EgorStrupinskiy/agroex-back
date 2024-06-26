package com.vention.agroex.entity;

import com.vention.agroex.util.constant.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "avatar")
    private String avatar;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<LotEntity> lots;

    @Column(name = "zone_info")
    private ZoneId zoneinfo;

    @Column(name = "enabled")
    private Boolean enabled;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

}
