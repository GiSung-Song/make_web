package make.web.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ItemDto {

    private Long id; //상품 번호

    private String cofName; //상품 이름

    private Integer price; //상품 가격

    private String detail; //상품 설명

    private String sellStatus; //상품 판매 상태

    private LocalDateTime regTime; //상품 등록 시간

    private LocalDateTime updateTime; //상품 업데이트 시간간
}
