package com.gmail.yauheniylebedzeu.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauheniylebedzeu.service.ItemService;
import com.gmail.yauheniylebedzeu.service.converter.impl.BindingResultConverterImpl;
import com.gmail.yauheniylebedzeu.service.model.ErrorsDTO;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import com.gmail.yauheniylebedzeu.web.constant.TestConstant;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

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
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("This field cannot be empty or consist of only spaces!");
        errors.addError("name", errorMessages);
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
        item.setName("     ");
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("This field cannot be empty or consist of only spaces!");
        errors.addError("name", errorMessages);
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
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("This field cannot be empty or consist of only spaces!");
        errors.addError("description", errorMessages);
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
        item.setDescription("     ");
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("This field cannot be empty or consist of only spaces!");
        errors.addError("description", errorMessages);
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
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("This field cannot be empty!");
        errors.addError("price", errorMessages);
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
        item.setPrice(new BigDecimal("0"));
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("The item price must be greater than 0.00!");
        errors.addError("price", errorMessages);
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
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("The item price must not be greater than 10000.00!");
        errors.addError("price", errorMessages);
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
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("The price of the item must contain no more than 5 integers and no more than 2 fractions!");
        errors.addError("price", errorMessages);
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
        return item;
    }
}