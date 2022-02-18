package make.web.service;

import make.web.item.repository.ItemImgRepository;
import make.web.item.repository.ItemRepository;
import make.web.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> mockFile() throws Exception {
        List<MultipartFile> fileList = new ArrayList<>();

        for(int i=0; i<5; i++) {
            String path = "C:/web/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
            fileList.add(multipartFile);
        }

        return fileList;
    }

/*
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void 아이템_저장_테스트() throws Exception {
        ItemFormDto dto = new ItemFormDto();
        dto.setItemNm("테스트 상품");
        dto.setSellStatus(SellStatus.SELL);
        dto.setPrice("10000");
        dto.setRegion("서울");
        dto.setDetail("상품내용");


        List<MultipartFile> multipartFiles = mockFile();
        Long itemId = itemService.saveItem(dto, multipartFiles); //아이템 저장 후 item_id를 return

        List<ItemImg> imgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(dto.getItemNm(), item.getItemNm());
        assertEquals(dto.getRegion(), item.getRegion());
        assertEquals(multipartFiles.get(0).getOriginalFilename(),
                imgList.get(0).getOriImgName());

    }
*/
}