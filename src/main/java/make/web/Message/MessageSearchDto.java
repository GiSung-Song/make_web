package make.web.Message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageSearchDto {

    private String searchDateType; //날짜로 검색

    private boolean searchConfirm; //읽기 확인 여부로 검색

    private String searchQuery = "";

}
