package make.web.cart.entity;

import lombok.Getter;
import make.web.etc.entity.BaseEntity;
import make.web.item.entity.Item;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_item_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public static CartItem createCartItem(Cart cart, Item item) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);

        return cartItem;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
