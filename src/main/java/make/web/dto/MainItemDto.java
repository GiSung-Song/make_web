package make.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {

    private Long id;

    private String itemNm;

    private String detail;

    private String imgUrl;

    private Integer price;

    @QueryProjection
    public MainItemDto(Long id, String itemNm, String detail, String imgUrl, Integer price) {
        this.id = id;
        this.itemNm = itemNm;
        this.detail = detail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
