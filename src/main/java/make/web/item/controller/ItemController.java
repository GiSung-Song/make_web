package make.web.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import make.web.item.dto.ItemFormDto;
import make.web.item.dto.ItemSearchDto;
import make.web.item.entity.Item;
import make.web.item.service.ItemService;
import make.web.member.dto.MemberFormDto;
import make.web.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/new")
    public String createItem(@ModelAttribute("item") ItemFormDto itemFormDto) {
        log.info("아이템 등록 페이지로 이동");
        return "item/itemForm";
    }

    @PostMapping("/new")
    public String itemCreate(@Valid @ModelAttribute("item") ItemFormDto itemFormDto,BindingResult bindingResult,
                             Model model, RedirectAttributes redirectAttributes, Principal principal,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if(bindingResult.hasErrors()) {
            log.info("validation error");
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            log.info("첫번째 상품 이미지가 없는 경우 error");
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 등록입니다.");

            return "item/itemForm";
        }

        try {
            String email = principal.getName();
            Long itemId = itemService.saveItem(itemFormDto, itemImgFileList, email);
            log.info("itemId = {}", itemId);

            redirectAttributes.addFlashAttribute("msg", "상품 등록이 완료되었습니다.");
            redirectAttributes.addFlashAttribute("url", "/item");
            redirectAttributes.addFlashAttribute("id", itemId);

        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/alert";
    }

    @GetMapping("/{itemId}")
    public String itemInfo(@PathVariable("itemId") Long itemId, Model model, RedirectAttributes redirectAttributes, Principal principal) {

        log.info("아이템 정보 접속");

        try {
            ItemFormDto itemFormDto = itemService.getItemInfo(itemId);
            model.addAttribute("item", itemFormDto);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg", "상품이 없습니다.");
            redirectAttributes.addFlashAttribute("url", "/");

            return "redirect:/alert";
        }

        String seller = itemService.getSeller(itemId);

        if(principal != null) {
            String login = principal.getName();

            if (login.equals(seller)) {
                model.addAttribute("seller", "seller");
            }
            else {
                model.addAttribute("email", seller);
            }
        } else {
            model.addAttribute("email", null);
        }

        return "item/itemInfo";
    }

    @GetMapping("/{itemId}/edit")
    public String editItem(@PathVariable("itemId") Long itemId, Model model, Principal principal,
                             RedirectAttributes redirectAttributes) {

            if(principal == null) {
                redirectAttributes.addFlashAttribute("msg", "수정 할 권한이 없습니다.");
                redirectAttributes.addFlashAttribute("url", "/item");
                redirectAttributes.addFlashAttribute("id", itemId);

                return "redirect:/alert";
            }

            String login = principal.getName(); //로그인 한 사용자 이메일
            String seller = itemService.getSeller(itemId); //판매자 이메일

            if(!login.equals(seller)) { //판매자와 로그인 한 사용자의 이메일이 다르다면 수정 할 권한이 없음.

                redirectAttributes.addFlashAttribute("msg", "수정 할 권한이 없습니다.");
                redirectAttributes.addFlashAttribute("url", "/item");
                redirectAttributes.addFlashAttribute("id", itemId);

                return "redirect:/alert";
            }

        try {
            ItemFormDto itemFormDto = itemService.getItemInfo(itemId);
            model.addAttribute("item", itemFormDto);
        } catch (EntityNotFoundException e) {

            redirectAttributes.addFlashAttribute("msg", "상품이 없습니다.");
            redirectAttributes.addFlashAttribute("url", "/item");
            redirectAttributes.addFlashAttribute("id", itemId);

            return "redirect:/alert";
        }

        log.info("수정 폼으로 이동");

        return "item/itemForm";
    }

    @PostMapping("/{itemId}/edit")
    public String itemEdit(@PathVariable("itemId") Long itemId, @Valid @ModelAttribute("item") ItemFormDto itemFormDto,
                           BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> imgList,
                           Model model, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            log.info("validation error");
            return "item/itemForm";
        }

        if(imgList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMsg", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, imgList);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        log.info("수정 완료");

        redirectAttributes.addFlashAttribute("msg", "상품 수정이 완료되었습니다.");
        redirectAttributes.addFlashAttribute("url", "/item");
        redirectAttributes.addFlashAttribute("id", itemId);

        return "redirect:/alert";
    }

    @GetMapping(value = {"/sell", "/sell/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, Principal principal,
                             @PathVariable("page") Optional<Integer> page, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5); //한 페이지에 5개씩 배치

        Long memberId = memberService.getMember(principal.getName()).getId();

        MemberFormDto member = memberService.getMember(principal.getName());


        Page<Item> items = itemService.getSellPage(itemSearchDto, pageable, memberId);

        model.addAttribute("items", items);
        model.addAttribute("dto", itemSearchDto);
        model.addAttribute("maxPage", 5); //페이지 수를 5개씩 끊음

        return "item/itemSellList";
    }

}
