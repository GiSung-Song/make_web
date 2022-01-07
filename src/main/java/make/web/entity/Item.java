package make.web.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import make.web.constant.SellStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Item 테이블 생성
 * @Lob (CLOB, BLOB)
 * CLOB : 문자형 대용량 파일을 저장하는데 사용하는 데이터 타입
 * BLOB : Binary 데이터를 DB 외부에 저장하기 위한 타입(이미지, 사운드, 비디오 같은 멀티미디어 데이터를 다룰 때 사용)
 * @Enumerated(EnumType.STRING) ORDINARY는 1,2로 표현, STRING은 문자 그대로 표현
 */

@Getter
@Entity
@Table(name = "item")
@NoArgsConstructor
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //상품 코드

    @Column(nullable = false, length = 30)
    private String itemNm; //상품 이름

    @Column(nullable = false)
    private int price; //상품 가격

    @Column(nullable = false)
    private int stock; //재고량

    @Lob
    @Column(nullable = false)
    private String detail; //상품 설명

    @Enumerated(EnumType.STRING)
    private SellStatus sellStatus; //판매 상태

    private LocalDateTime regTime; //상품 등록 시간

    private LocalDateTime updateTime; //수정 시간

    @Builder
    public Item(String itemNm, int price, int stock, String detail, SellStatus sellStatus, LocalDateTime regTime) {
        this.itemNm = itemNm;
        this.price = price;
        this.stock = stock;
        this.detail = detail;
        this.sellStatus = sellStatus;
        this.regTime = regTime;
    }
}
