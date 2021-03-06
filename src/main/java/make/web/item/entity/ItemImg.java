package make.web.item.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import make.web.etc.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "item_img")
@Getter
@NoArgsConstructor
public class ItemImg extends BaseEntity {

    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName; //이미지 파일명

    private String oriImgName; //원본 이미지 파일명명

    private String imgUrl; //이미지 조회 경로

    private String imgYn; //대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public void itemSet(Item item) {
        this.item = item;
    }

    public void ynSet(String imgYn) {
        this.imgYn = imgYn;
    }

}
