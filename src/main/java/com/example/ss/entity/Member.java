package com.example.ss.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name = "MEMBER_ID_SEQ_GENERATOR",
        sequenceName = "MEMBER_ID_SEQ",
        allocationSize = 1
)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_ID_SEQ_GENERATOR")
    private long id;
    private String username;
    private String password;
    private String role;
    private String refreshToken;
}
