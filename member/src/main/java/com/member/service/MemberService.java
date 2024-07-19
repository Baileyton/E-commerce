package com.member.service;

import com.member.dto.SignupDto;
import com.member.entity.Member;
import com.member.entity.MemberRole;
import com.member.exception.ErrorCode;
import com.member.exception.MemberException;
import com.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional // 메서드 수준에서 트랜잭션 적용
    public SignupDto.Response signUp(SignupDto.Request request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new MemberException(ErrorCode.EXIST_USER);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(MemberRole.USER) // 기본 역할 설정
                .build();

        Member savedMember = memberRepository.save(member);

        return new SignupDto.Response(
                savedMember.getName(),
                savedMember.getEmail(),
                savedMember.getPhone(),
                savedMember.getAddress(),
                savedMember.getCreatedAt()
        );
    }
}
