package com.example.jpastudy.service;

import com.example.jpastudy.entity.ItemImage;
import com.example.jpastudy.repository.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.File;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImageService {
    @Value("${itemImageLocation}")
    private String itemImageLocation;

    private final ItemImageRepository itemImageRepository;

    private final FileService fileService;

    public void saveItemImage(ItemImage itemImage, MultipartFile itemImageFile) throws Exception{
        String originalImageName=itemImageFile.getOriginalFilename();
        String imageName="";
        String imageUrl="";

        if(!StringUtils.isEmpty(originalImageName)){
            imageName=fileService.uploadFile(itemImageLocation, originalImageName, itemImageFile.getBytes());
            imageUrl="/images/item/"+imageName;
        }

        itemImage.updateItemImg(originalImageName, imageName, imageUrl);
        itemImageRepository.save(itemImage);
    }

    public void updateItemImage(Long itemId, MultipartFile itemImageFile) throws Exception{
        if(!itemImageFile.isEmpty()){
            ItemImage savedItemImage= itemImageRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);
            if(!StringUtils.isEmpty(savedItemImage.getImageName())){
                fileService.deleteFile(itemImageLocation+"/"+savedItemImage.getImageName());
            }
            String originalName=itemImageFile.getOriginalFilename();
            String imageName=fileService.uploadFile(itemImageLocation, originalName, itemImageFile.getBytes());
            String imageUrl= "/images/item/"+imageName;
            savedItemImage.updateItemImg(originalName, imageName, imageUrl);
        }

    }
}
