package com.gmail.yauheniylebedzeu.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauheniylebedzeu.service.ItemService;
import com.gmail.yauheniylebedzeu.service.converter.impl.BindingResultConverterImpl;
import com.gmail.yauheniylebedzeu.service.model.ErrorsDTO;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_ITEM_QUANTITY_IN_STOCK;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_LENGTH_OF_ITEM_DESCRIPTION;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_LENGTH_OF_ITEM_NAME;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_ITEM_QUANTITY_IN_STOCK;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ITEM_DESCRIPTION;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ITEM_NAME;
import static com.gmail.yauheniylebedzeu.web.constant.TestConstant.TEST_UUID;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ITEMS_CONTROLLER_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class,
        controllers = ItemAPIController.class)
@Import(BindingResultConverterImpl.class)
@ActiveProfiles(value = "test")
public class ItemAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Test
    void shouldRequestGetItems() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatBusinessLogicIsCalledWhenWeRequestGetItems() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        );
        verify(itemService, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyCollectionWhenWeRequestGetItems() throws Exception {
        when(itemService.findAll()).thenReturn(Collections.emptyList());
        MvcResult mvcResult = mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(Collections.emptyList()));
    }

    @Test
    void shouldReturnCollectionOfItemsWhenWeRequestGetItems() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        List<ItemDTO> items = Collections.singletonList(item);
        when(itemService.findAll()).thenReturn(items);
        MvcResult mvcResult = mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString)
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(items));
    }

    @Test
    void shouldRequestGetItem() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatBusinessLogicIsCalledWhenWeRequestGetItem() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        );
        verify(itemService, times(1)).findByUuid(TEST_UUID);
    }

    @Test
    void shouldReturnItemDTOWhenWeRequestGetItem() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        when(itemService.findByUuid(TEST_UUID)).thenReturn(item);
        MvcResult mvcResult = mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString)
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(item));
    }

    @Test
    void shouldRequestDeleteItem() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatBusinessLogicIsCalledWhenWeRequestedDeleteItem() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        );
        verify(itemService, times(1)).removeByUuid(TEST_UUID);
    }

    @Test
    void shouldRequestAddItem() throws Exception {
        ItemDTO item = new ItemDTO();
        mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        );
    }

    @Test
    void shouldVerifyThatBusinessLogicIsCalledWhenWeRequestedAddItem() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        );
        verify(itemService, times(1)).add(item);
    }

    @Test
    void shouldAddItemWithValidFields() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldAddItemWithNullName() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setName(null);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("name", "This field cannot be empty or consist of only spaces!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithEmptyName() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setName("");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).contains("name", "This field cannot be empty or consist of only spaces!",
                "The item name must be between 2 and 50 characters long!");
    }

    @Test
    void shouldAddItemWithOnlySpacesInName() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setName(StringUtils.repeat(" ", MIN_LENGTH_OF_ITEM_NAME + 1));
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("name", "This field cannot be empty or consist of only spaces!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithTooLongName() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setName(StringUtils.repeat("N", MAX_LENGTH_OF_ITEM_NAME + 1));
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("name", "The item name must be between 2 and 50 characters long!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithTooShortName() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setName(StringUtils.repeat("N", MIN_LENGTH_OF_ITEM_NAME - 1));
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("name", "The item name must be between 2 and 50 characters long!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithNullDescription() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setDescription(null);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("description", "This field cannot be empty or consist of only spaces!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithEmptyDescription() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setDescription("");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).contains("description", "This field cannot be empty or consist of only spaces!",
                "The item description must be between 5 and 200 characters long!");
    }

    @Test
    void shouldAddItemWithOnlySpacesInDescription() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setDescription(StringUtils.repeat(" ", MIN_LENGTH_OF_ITEM_DESCRIPTION + 1));
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("description", "This field cannot be empty or consist of only spaces!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithTooLongDescription() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setDescription(StringUtils.repeat("D", MAX_LENGTH_OF_ITEM_DESCRIPTION + 1));
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("description", "The item description must be between 5 and 200" +
                " characters long!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithTooShortDescription() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setDescription(StringUtils.repeat("D", MIN_LENGTH_OF_ITEM_DESCRIPTION - 1));
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("description", "The item description must be between 5 and 200" +
                " characters long!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithNullPrice() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setPrice(null);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("price", "This field cannot be empty!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithWrongMinPrice() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setPrice(BigDecimal.ZERO);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("price", "The item price must be greater than 0.00!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithWrongMaxPrice() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setPrice(new BigDecimal("10000.01"));
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("price", "The item price must not be greater than 10000.00!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithWrongPriceFormat() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setPrice(new BigDecimal("100.333"));
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("price", "The price of the item must contain no more than 5 integers" +
                " and no more than 2 fractions!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithNullQuantityInStock() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setQuantityInStock(null);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("quantityInStock", "This field cannot be empty!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithWrongMinQuantityInStock() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setQuantityInStock(MIN_ITEM_QUANTITY_IN_STOCK - 1);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("quantityInStock", "Minimum allowed quantity is 5!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddItemWithWrongMaxQuantityInStock() throws Exception {
        ItemDTO item = getItemDTOWithValidFields();
        item.setQuantityInStock(MAX_ITEM_QUANTITY_IN_STOCK + 1);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("quantityInStock", "Maximum allowed quantity is 99!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    private ItemDTO getItemDTOWithValidFields() {
        ItemDTO item = new ItemDTO();
        item.setName("Item");
        item.setDescription("Test description");
        item.setPrice(new BigDecimal("125.00"));
        item.setQuantityInStock(MIN_ITEM_QUANTITY_IN_STOCK);
        return item;
    }
}