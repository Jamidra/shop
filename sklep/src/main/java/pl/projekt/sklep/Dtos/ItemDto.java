package pl.projekt.sklep.Dtos;

import pl.projekt.sklep.Models.Category;

import java.math.BigDecimal;
import java.util.List;

public class ItemDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
