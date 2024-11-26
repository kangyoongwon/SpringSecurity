package com.example.ss.service;

import com.example.ss.dto.JoinDTO;
import com.example.ss.entity.Member;
import com.example.ss.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean join(JoinDTO joinDTO) {
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        if (memberRepository.findByUsername(username) == null) {
            Member memberEntity =
            Member.builder()
                    .username(username)
                    .password(bCryptPasswordEncoder.encode(password))
                    .role("ROLE_ADMIN")
                    .build();
            memberRepository.save(memberEntity);
            return true;
        } else return false;
    }
}
