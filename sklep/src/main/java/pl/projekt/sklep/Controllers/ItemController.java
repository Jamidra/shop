package pl.projekt.sklep.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.projekt.sklep.Dtos.ItemDto;
import pl.projekt.sklep.Exceptions.ResourceNotFoundException;
import pl.projekt.sklep.Models.Item;
import pl.projekt.sklep.Services.ItemServiceInterface;
import pl.projekt.sklep.reqs.AddItemRequest;
import pl.projekt.sklep.reqs.ItemUpdateRequest;
import pl.projekt.sklep.responses.ApiResponse;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemServiceInterface itemService;

    public ItemController(ItemServiceInterface itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllItems() {
        List<Item> items = itemService.getAllItems();
        List<ItemDto> convertedItems = itemService.getConvertedItems(items);
        return  ResponseEntity.ok(new ApiResponse("success", convertedItems));
    }

    @GetMapping("item/{itemId}/item")
    public ResponseEntity<ApiResponse> getItemById(@PathVariable Long itemId) {
        try {
            Item item = itemService.getItemById(itemId);
            ItemDto itemDto = itemService.convertToDto(item);
            return  ResponseEntity.ok(new ApiResponse("success", itemDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItem(@RequestBody AddItemRequest item) {
        try {
            Item theItem = itemService.addItem(item);
            ItemDto itemDto = itemService.convertToDto(theItem);
            return ResponseEntity.ok(new ApiResponse("Add item success!", itemDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/item/{itemId}/update")
    public  ResponseEntity<ApiResponse> updateItem(@RequestBody ItemUpdateRequest request, @PathVariable Long itemId) {
        try {
            Item theItem = itemService.updateItem(request, itemId);
            ItemDto itemDto = itemService.convertToDto(theItem);
            return ResponseEntity.ok(new ApiResponse("Update item success!", itemDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/item/{itemId}/delete")
    public ResponseEntity<ApiResponse> deleteItem(@PathVariable Long itemId) {
        try {
            itemService.deleteItemById(itemId);
            return ResponseEntity.ok(new ApiResponse("Delete item success!", itemId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/items/{name}/items")
    public ResponseEntity<ApiResponse> getItemByName(@PathVariable String name){
        try {
            List<Item> items = itemService.getItemsByName(name);
            if (items.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No items found ", null));
            }
            List<ItemDto> convertedItems = itemService.getConvertedItems(items);
            return  ResponseEntity.ok(new ApiResponse("success", convertedItems));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/item/{category}/all/items")
    public ResponseEntity<ApiResponse> findItemByCategory(@PathVariable String category) {
        try {
            List<Item> items = itemService.getItemsByCategory(category);
            if (items.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No items found ", null));
            }
            List<ItemDto> convertedItems = itemService.getConvertedItems(items);
            return  ResponseEntity.ok(new ApiResponse("success", convertedItems));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/item/count/by-name")
    public ResponseEntity<ApiResponse> countItemsByName(@RequestParam String name) {
        try {
            var itemCount = itemService.countItemsByName(name);
            return ResponseEntity.ok(new ApiResponse("Product count!", itemCount));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

}
