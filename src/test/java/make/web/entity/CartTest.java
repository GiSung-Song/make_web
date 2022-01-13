package make.web.entity;

import make.web.dto.MemberFormDto;
import make.web.repository.CartRepository;
import make.web.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext //영속성 컨텍스트
    EntityManager em;

    Member ex_dto() {
        MemberFormDto memberFormDto = MemberFormDto.builder()
                .email("test@test.test")
                .name("테스트")
                .password("비밀번호")
                .address("서울시 메시")
                .build();

        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    void 매핑_조회_테스트() {
        Member member = ex_dto();
        memberRepository.save(member);

        Cart cart = Cart.builder().member(member).build();
        cartRepository.save(cart);

        /**
         * em.flush()
         * 엔티티 매니저의 쿼리문 저장소에 있는 쿼리들을 DB에 반영
         * 영속성 컨텍스트와 DB를 동기화함.
         *
         * em.clear()
         * 영속성 컨텍스트(1차 캐시 저장소, 쿼리문 저장소) 초기화
         */

        em.flush();
        em.clear();

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);

        //Cart 엔티티의 member_id와 member.getId() 가 같은 값을 가짐으로 매핑이 된 것을 확인인
       assertEquals(savedCart.getMember().getId(), member.getId());
    }
}