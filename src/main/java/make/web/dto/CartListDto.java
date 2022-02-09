package make.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartListDto {

    private Long cartItemId;

    private String region;

    private String itemNm;

    private int price;

    private String imgUrl;

    @QueryProjection
    public CartListDto(Long cartItemId, String itemNm, int price, String imgUrl, String region) {
        this.cartItemId = cartItemId;
        this.region = region;
        this.itemNm = itemNm;
        this.price = price;
        this.imgUrl = imgUrl;
    }
}
