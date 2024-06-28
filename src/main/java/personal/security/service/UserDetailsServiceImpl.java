package personal.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import personal.security.domain.Member;
import personal.security.domain.UserDetailsImpl;
import personal.security.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                //T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X
                .orElseThrow(() -> new IllegalArgumentException(email));
        return new UserDetailsImpl(member);
    }
}
