package com.labhesh.secondhandstore.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.labhesh.secondhandstore.dtos.AddItemDto;
import com.labhesh.secondhandstore.dtos.UpdateItemDto;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.models.Category;
import com.labhesh.secondhandstore.models.Item;
import com.labhesh.secondhandstore.repos.CategoryRepo;
import com.labhesh.secondhandstore.repos.ItemRepo;
import com.labhesh.secondhandstore.utils.ImageService;
import com.labhesh.secondhandstore.utils.SuccessResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepo itemRepo;
    private final CategoryRepo categoryRepo;
    private final ImageService imageService;

    // add item
    public ResponseEntity<?> addItem(AddItemDto dto) throws InternalServerException, BadRequestException {

        try {
            Category category = categoryRepo.findCategory(UUID.fromString(dto.getCategoryId()))
                    .orElseThrow(() -> new BadRequestException("Category not found"));
            Item item = Item.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .price(dto.getPrice())
                    .currencyCode(dto.getCurrencyCode())
                    .files(imageService.saveImage(dto.getImage()))
                    .createdDate(LocalDateTime.now())
                    .category(category)
                    .build();
            itemRepo.save(item);
            return ResponseEntity.ok(new SuccessResponse("Item added successfully", item, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // get all
    public ResponseEntity<?> allItems() {
        List<Item> items = itemRepo.findAllItems();
        return ResponseEntity.ok(items);
    }

    // get by id
    public ResponseEntity<?> itemById(String id) throws BadRequestException, InternalServerException {
        try {
            return itemRepo.findItem(UUID.fromString(id))
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new BadRequestException("Item not found"));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // update by id
    public ResponseEntity<?> updateItem(String id, UpdateItemDto dto)
            throws BadRequestException, InternalServerException, IOException {
                try {
            Item item = itemRepo.findItem(UUID.fromString(id))
                    .orElseThrow(() -> new BadRequestException("Item not found"));
            if (dto.getCategoryId() != null) {
                Category category = categoryRepo.findCategory(UUID.fromString(dto.getCategoryId()))
                        .orElseThrow(() -> new BadRequestException("Category not found"));
                item.setCategory(category);
            }
            item.setName(dto.getName() != null ? dto.getName() : item.getName());
            item.setDescription(dto.getDescription() != null ? dto.getDescription() : item.getDescription());
            item.setPrice(dto.getPrice() != null ? dto.getPrice() : item.getPrice());
            item.setCurrencyCode(dto.getCurrencyCode() != null ? dto.getCurrencyCode() : item.getCurrencyCode());
            item.setFiles(dto.getImage() != null ? imageService.saveImage(dto.getImage()) : item.getFiles());
            item.setUpdatedDate(LocalDateTime.now());
            itemRepo.save(item);
            return ResponseEntity.ok(new SuccessResponse("Item updated successfully", item, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // delete by id
    public ResponseEntity<?> deleteItem(String id) throws BadRequestException, InternalServerException {
        try {
            return itemRepo.findItem(UUID.fromString(id))
                    .map(item -> {
                        item.setDeletedDate(LocalDateTime.now());
                        itemRepo.save(item);
                        return ResponseEntity.ok().build();
                    })
                    .orElseThrow(() -> new BadRequestException("Item not found"));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

    }

    // search and filter
    public ResponseEntity<?> searchItems(String name, String category, Double minPrice, Double maxPrice)
            throws InternalServerException {
        // if minPrice is greater than maxPrice, swap them
        if (minPrice > maxPrice) {
            Double temp = minPrice;
            minPrice = maxPrice;
            maxPrice = temp;
        }
        try {
            return ResponseEntity.ok(itemRepo.searchItems(name, UUID.fromString(category), minPrice, maxPrice));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

}
