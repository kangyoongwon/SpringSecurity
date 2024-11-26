package com.example.ss.controller;

import com.example.ss.dto.JoinDTO;
import com.example.ss.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String join(JoinDTO joinDTO) {
        joinService.join(joinDTO);
        return "ok";
    }
}
