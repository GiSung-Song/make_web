package make.web.controller;

import lombok.extern.slf4j.Slf4j;
import make.web.dto.ItemFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @GetMapping("/new")
    public String createItem(@ModelAttribute("item") ItemFormDto itemFormDto) {
        log.info("아이템 등록 페이지로 이동");
        return "item/createItemForm";
    }

}
