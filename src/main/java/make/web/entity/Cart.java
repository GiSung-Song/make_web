package make.web.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter
@NoArgsConstructor
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //찜하기 번호

    /**
     * member_id를 외래키로 지정
     * 1:1 단방향 매핑
     * Cart 엔티티가 Member 엔티티를 참조하고 있음
     * 즉, Cart 엔티티가 Member 엔티티를 참조하는 1:1 단방향 매핑
     */

    @OneToOne(fetch = FetchType.LAZY) //회원과 1:1매핑, LAZY : 지연로딩, EAGER : 즉시로딩
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Cart(Member member) {
        this.member = member;
    }
}
