package com.example.jpastudy.service;

import com.example.jpastudy.constant.ItemSellStatus;
import com.example.jpastudy.dto.ItemFormDto;
import com.example.jpastudy.entity.Item;
import com.example.jpastudy.entity.ItemImage;
import com.example.jpastudy.repository.ItemImageRepository;
import com.example.jpastudy.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemServiceTest {
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImageRepository itemImageRepository;

    List<MultipartFile> createMultipartFiles() throws Exception{
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i=0;i<5;i++){
            String path="C:/shop/item";
            String imageName="image"+i+".jpg";
            MockMultipartFile multipartFile= new MockMultipartFile(path, imageName,
                    "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username="admin", roles="ADMIN")
    void saveItem() throws Exception{
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemName("테스트 상품");
        itemFormDto.setItemDetail("테스트 상품의 디테일");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId= itemService.saveItem(itemFormDto, multipartFileList);

        List<ItemImage> itemImageList= itemImageRepository.findByItemIdOrderByItemIdAsc(itemId);

        Item item=itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getItemName(), item.getItemName());
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImageList.get(0).getOriginalImageName());
    }
}
