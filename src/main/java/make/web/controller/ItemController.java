package make.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import make.web.dto.ItemFormDto;
import make.web.entity.Member;
import make.web.service.ItemService;
import make.web.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/new")
    public String createItem(@ModelAttribute("item") ItemFormDto itemFormDto) {
        log.info("아이템 등록 페이지로 이동");
        return "item/createItemForm";
    }

    @PostMapping("/new")
    public String itemCreate(@Valid @ModelAttribute("item") ItemFormDto itemFormDto,BindingResult bindingResult,
                             Model model, RedirectAttributes redirectAttributes,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if(bindingResult.hasErrors()) {
            log.info("validation error");
            return "item/createItemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            log.info("첫번째 상품 이미지가 없는 경우 error");
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 등록입니다.");

            return "item/createItemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/createItemForm";
        }

        redirectAttributes.addFlashAttribute("msg", "상품 등록이 완료되었습니다.");
        redirectAttributes.addFlashAttribute("url", "/");

        return "redirect:/message";
    }

}
