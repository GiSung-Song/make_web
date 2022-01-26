package make.web.dto;

import lombok.Getter;
import lombok.Setter;
import make.web.constant.SellStatus;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType; //상품 등록일 비교하여 검색

    private SellStatus searchSellStatus; //판매 상태로 검색

    private String searchBy; //상품명 or 상품 등록자명으로 검색

    private String searchQuery = "";
}
