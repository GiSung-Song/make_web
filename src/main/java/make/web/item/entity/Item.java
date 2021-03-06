package make.web.item.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import make.web.etc.constant.SellStatus;
import make.web.item.dto.ItemFormDto;
import make.web.etc.entity.BaseEntity;
import make.web.member.entity.Member;

import javax.persistence.*;

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
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //상품 코드

    @Column(nullable = false, length = 30)
    private String itemNm; //상품 이름

    @Column(nullable = false)
    private int price; //상품 가격

    @Lob
    @Column(nullable = false)
    private String detail; //상품 설명

    @Column(nullable = false)
    private String region; //거래 지역

   @Enumerated(EnumType.STRING)
    private SellStatus sellStatus; //판매 상태

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //회원이 item 판매

    @Builder
    public Item(String itemNm, int price, String detail, SellStatus sellStatus, String region) {
        this.itemNm = itemNm;
        this.price = price;
        this.detail = detail;
        this.sellStatus = sellStatus;
        this.region = region;
    }

    public void addMember(Member member) {
        this.member = member;
    }

    public void updateItem(ItemFormDto dto) {
        this.itemNm = dto.getItemNm();
        this.price = Integer.parseInt(dto.getPrice());
        this.region = dto.getRegion();
        this.detail = dto.getDetail();
        this.sellStatus = dto.getSellStatus();
    }
}
