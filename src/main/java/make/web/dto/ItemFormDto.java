package make.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import make.web.constant.SellStatus;
import make.web.entity.Item;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemFormDto {

    private Long id;

    @NotEmpty(message = "상품명을 입력해주세요.")
    private String itemNm;

    @NotNull(message = "가격을 입력해주세요.")
    @Pattern(regexp = "^[0-9]+$", message = "숫자만 입력가능합니다.")
    private String price; //상품 가격

    @NotEmpty(message = "상품 설명을 입력해주세요.")
    private String detail; //상품 설명

    private SellStatus sellStatus; //상품 판매 상태

    private String region; //거래 지역

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

//    public Item dtoToEntity() {
//        return modelMapper.map(this, Item.class);
//    }

    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }

    public Item toEntity() {
        Item item = Item.builder()
                .itemNm(this.itemNm)
                .detail(this.detail)
                .price(Integer.parseInt(this.price))
                .region(this.region)
                .sellStatus(this.sellStatus)
                .build();

        return item;
    }
}
