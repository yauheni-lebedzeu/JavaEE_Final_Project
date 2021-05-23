package com.gmail.yauheniylebedzeu.service.model;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

import static com.gmail.yauheniylebedzeu.service.constant.MessagesPropertyConstant.NOT_EMPTY_FIELD_PROPERTY;
import static com.gmail.yauheniylebedzeu.service.constant.MessagesPropertyConstant.WRONG_ITEM_DESCRIPTION_FORMAT_PROPERTY;
import static com.gmail.yauheniylebedzeu.service.constant.MessagesPropertyConstant.WRONG_ITEM_NAME_FORMAT_PROPERTY;
import static com.gmail.yauheniylebedzeu.service.constant.MessagesPropertyConstant.WRONG_ITEM_PRICE_FORMAT_PROPERTY;
import static com.gmail.yauheniylebedzeu.service.constant.MessagesPropertyConstant.WRONG_MIN_ITEM_PRICE_PROPERTY;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.COUNT_OF_INTEGERS_IN_PRICE;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.COUNT_OF_FRACTIONS_IN_PRICE;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_LENGTH_OF_ITEM_DESCRIPTION;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_LENGTH_OF_ITEM_NAME;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ITEM_DESCRIPTION;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ITEM_NAME;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_ITEM_PRICE;

@Data
public class ItemDTO {

    private Long uniqueNumber;
    private String uuid;

    @NotBlank(message = "{" + NOT_EMPTY_FIELD_PROPERTY + "}")
    @Size(min = MIN_LENGTH_OF_ITEM_NAME, max = MAX_LENGTH_OF_ITEM_NAME, message = "{" + WRONG_ITEM_NAME_FORMAT_PROPERTY + "}")
    private String name;

    @NotBlank(message = "{" + NOT_EMPTY_FIELD_PROPERTY + "}")
    @Size(min = MIN_LENGTH_OF_ITEM_DESCRIPTION, max = MAX_LENGTH_OF_ITEM_DESCRIPTION,
            message = "{" + WRONG_ITEM_DESCRIPTION_FORMAT_PROPERTY + "}")
    private String description;

    @NotNull(message = "{" + NOT_EMPTY_FIELD_PROPERTY + "}")
    @DecimalMin(value = MIN_ITEM_PRICE, inclusive = false, message = "{" + WRONG_MIN_ITEM_PRICE_PROPERTY + "}")
    @Digits(integer = COUNT_OF_INTEGERS_IN_PRICE, fraction = COUNT_OF_FRACTIONS_IN_PRICE,
            message = "{" + WRONG_ITEM_PRICE_FORMAT_PROPERTY + "}")
    private BigDecimal price;
}