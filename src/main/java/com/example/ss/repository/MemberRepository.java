package com.example.ss.repository;

import com.example.ss.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
//    boolean existByUsername(String username);

    Member findByUsername(String username);


}
