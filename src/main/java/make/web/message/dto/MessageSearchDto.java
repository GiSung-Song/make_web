package make.web.message.dto;

import lombok.Getter;
import lombok.Setter;
import make.web.message.MessageStatus;

@Getter
@Setter
public class MessageSearchDto {

    private String searchDateType; //날짜로 검색

    private MessageStatus searchConfirm; //읽기 확인 여부로 검색

    private String searchQuery = "";

}
