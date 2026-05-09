package org.example.busticketpro.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    private Long id;   // Share primary key with User

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String address;
    private String identityCard; // CMND/CCCD
}