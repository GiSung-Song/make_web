package make.web.cart.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import make.web.etc.entity.BaseEntity;
import make.web.member.entity.Member;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter
@NoArgsConstructor
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //장바구니 번호

    /**
     * member_id를 외래키로 지정
     * 1:1 단방향 매핑
     * Cart 엔티티가 Member 엔티티를 참조하고 있음
     * 즉, Cart 엔티티가 Member 엔티티를 참조하는 1:1 단방향 매핑
     */

    @OneToOne(fetch = FetchType.LAZY) //회원과 1:1매핑, LAZY : 지연로딩, EAGER : 즉시로딩
    @JoinColumn(name = "member_id")
    private Member member;

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.addMember(member);

        return cart;
    }

    public void addMember(Member member) {
        this.member = member;
    }

}
