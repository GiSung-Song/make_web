package make.web.etc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import make.web.item.dto.ItemSearchDto;
import make.web.item.dto.MainItemDto;
import make.web.item.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping("/")
    public String main(ItemSearchDto dto, Optional<Integer> page, Model model) {
        log.info("홈 화면 이동");

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getMainPage(dto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("dto", dto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

    @GetMapping("/alert")
    public String message(Model model) {
        return "alert";
    }


}
