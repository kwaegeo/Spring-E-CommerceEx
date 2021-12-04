package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Table(name="member")
@Getter
@Setter                               //엔티티 클래스로 설정하며 테이블의 이름은 member로한다.
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name ="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)   //id를 기본키로
    private Long id;

    private String name;

    @Column(unique = true)  //이메일은 unique 조건을 붙여 동일한 값이 들어올 수 없게 지정한다.
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    //dto객체와 passwordEncoder 인터페이스의 객체를 매개변수로 하여 회원을 추가하는 메서드
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member(); //멤버 객체를 만든 뒤
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword()); //스프링 시큐리티 설정 클래스에 등록한 Encored Bean을 파라미터로 넘겨서 비밀번호를 암호화한다.
        member.setPassword(password); //이후 password를 set
        member.setRole(Role.ADMIN);
        return member;  //완성된 객체를 리턴
    }
}
