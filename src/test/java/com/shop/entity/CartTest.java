package com.shop.entity;


import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest(){
        Member member = createMember(); //멤버객체를 위의 메소드로 만들고
        memberRepository.save(member); // 데베에 저장한 뒤

        Cart cart = new Cart();  //카트객체또한 만들고 위의 멤버를 토대로 member_id 속성 저장
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush();  //영속성 컨텍스트에 데이터 저장 후 트랜잭션이 끝날 때 flush()를 호출해서 데이터베이스에 강제 반영
        em.clear();  //영속성 컨텍스트에 엔티티 조회 후 없을경우엔 데이터베이스를 조회한다. 실제 DB에서 장바구니엔티티와 회원 엔티티를 같이 가져오는지 보기 위해 컨텍스트를 비워줌

        Cart savedCart = cartRepository.findById(cart.getId()) //장바구니 조회
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(savedCart.getMember().getId(), member.getId()); //처음 저장한 member id랑 saved에 매핑된 member id를 비교함.
    }
}
