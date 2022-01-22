package make.web.service;

import lombok.RequiredArgsConstructor;
import make.web.dto.ItemFormDto;
import make.web.entity.Item;
import make.web.entity.ItemImg;
import make.web.repository.ItemImgRepository;
import make.web.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ItemService {

    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;
    private final ItemRepository itemRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemFormDto.toEntity();
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

}
