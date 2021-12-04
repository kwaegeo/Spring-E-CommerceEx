package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional  //로직을 처리하다가 에러가 발생 시 변경된 데이터를 로직을 수행하기 이전 상태로 콜백 시켜준다.
@RequiredArgsConstructor // 빈을 주입하는 방법으로 final이나 @NonNull이 붙은 필드에 생성자를 생성해준다.
public class MemberService implements UserDetailsService { // UserDetailService 인터페이스를 상속받아 안의 내용을 구현

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());  //새로 요청된 이메일을 저장소에서 탐색하여 findMember에 저장
        if(findMember != null){                                              //만약 null이 아니라면 즉 가입된 것이 있다면
            throw new IllegalStateException("이미 가입된 회원입니다.");  //예외 발생 시킴.
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { //로그인 할 유저의 email을 매개변수로 전달받음.
        Member member = memberRepository.findByEmail(email); //데베에서 이메일을 찾아 member객체에 정보를 넣은 뒤

        if(member == null){  //멤버(정보)가 없다면 예외처리를 하고
             throw new UsernameNotFoundException(email);
        }

        return User.builder()  //있다면 User객체를 반환하여 준다. 그런데 객체를 생성하기 위한 생성자로 회원의 이메일 비밀번호, role을 member에서 꺼내 준다.
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
