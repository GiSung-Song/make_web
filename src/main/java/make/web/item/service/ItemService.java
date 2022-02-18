package make.web.item.service;

import lombok.RequiredArgsConstructor;
import make.web.item.dto.ItemFormDto;
import make.web.item.dto.ItemImgDto;
import make.web.item.dto.ItemSearchDto;
import make.web.item.dto.MainItemDto;
import make.web.item.entity.Item;
import make.web.item.entity.ItemImg;
import make.web.member.entity.Member;
import make.web.item.repository.ItemImgRepository;
import make.web.item.repository.ItemRepository;
import make.web.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ItemService {

    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList, String email) throws Exception {
        Item item = itemFormDto.toEntity();
        Member member = memberRepository.findByEmail(email);
        item.addMember(member);
        itemRepository.save(item);

        for(int i=0; i<itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.itemSet(item);

            if(i == 0)
                itemImg.ynSet("Y");
            else
                itemImg.ynSet("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public String getSeller(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        String email = item.getMember().getEmail();

        return email;
    }


    @Transactional(readOnly = true)
    public ItemFormDto getItemInfo(Long itemId) {
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }

    public Long updateItem(ItemFormDto dto, List<MultipartFile> fileList) throws Exception {

        Item item = itemRepository.findById(dto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(dto);

        List<Long> imgId = dto.getItemImgIds();

        for(int i=0; i< fileList.size(); i++) {
            itemImgService.updateItemImg(imgId.get(i), fileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getSellPage(ItemSearchDto itemSearchDto, Pageable pageable, Long memberId) {
        return itemRepository.getSellPage(itemSearchDto, pageable, memberId);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainPage(ItemSearchDto dto, Pageable pageable) {
        return itemRepository.getMainPage(dto, pageable);
    }

}
