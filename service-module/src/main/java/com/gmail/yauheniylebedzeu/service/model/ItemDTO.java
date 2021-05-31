package com.gmail.yauheniylebedzeu.service.model;

import com.gmail.yauheniylebedzeu.service.constant.ValidationConstant;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.COUNT_OF_FRACTIONS_IN_PRICE;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.COUNT_OF_INTEGERS_IN_PRICE;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_LENGTH_OF_ITEM_DESCRIPTION;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_LENGTH_OF_ITEM_NAME;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_ITEM_PRICE;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ITEM_DESCRIPTION;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ITEM_NAME;

@Data
public class ItemDTO {

    private Long uniqueNumber;
    private String uuid;

    @NotBlank(message = "This field cannot be empty or consist of only spaces!")
    @Size(min = MIN_LENGTH_OF_ITEM_NAME, max = MAX_LENGTH_OF_ITEM_NAME,
            message = "The item name must be between {min} and {max} characters long!")
    private String name;

    @NotBlank(message = "This field cannot be empty or consist of only spaces!")
    @Size(min = MIN_LENGTH_OF_ITEM_DESCRIPTION, max = MAX_LENGTH_OF_ITEM_DESCRIPTION,
            message = "The item description must be between {min} and {max} characters long!")
    private String description;

    @NotNull(message = "This field cannot be empty!")
    @DecimalMin(value = MIN_ITEM_PRICE, inclusive = false, message = "The item price must be greater than {value}!")
    @DecimalMax(value = "10000.00", message = "The item price must not be greater than {value}!")
    @Digits(integer = COUNT_OF_INTEGERS_IN_PRICE, fraction = COUNT_OF_FRACTIONS_IN_PRICE,
            message = "The price of the item must contain no more than {integer} integers and no more than {fraction} fractions!")
    private BigDecimal price;

    @NotNull(message = "This field cannot be empty!")
    @Min(value = ValidationConstant.MIN_ITEM_QUANTITY_IN_STOCK, message = "Minimum allowed quantity is {value}!")
    @Max(value = ValidationConstant.MAX_ITEM_QUANTITY_IN_STOCK, message = "Maximum allowed quantity is {value}!")
    private Integer quantityInStock;
    private Boolean isDeleted;
}