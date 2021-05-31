package com.gmail.yauheniylebedzeu.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.converter.impl.BindingResultConverterImpl;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.exception.UserNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.model.ErrorsDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.web.validator.UserValidator;
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

import java.util.Collections;
import java.util.List;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.USERS_CONTROLLER_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class,
        controllers = UserAPIController.class)
@Import({UserValidator.class, BindingResultConverterImpl.class})
@ActiveProfiles(value = "test")
public class UserAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void shouldVerifyThatBusinessLogicIsCalledWhenWeRequestAddUser() throws Exception {
        UserDTO user = getUserWithValidFields();
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        );
        verify(userService, times(1)).add(user);
    }

    @Test
    void shouldAddUserWithValidFields() throws Exception {
        UserDTO user = getUserWithValidFields();
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldNotAddUserWithNullEmail() throws Exception {
        UserDTO user = getUserWithValidFields();
        user.setEmail(null);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("email", "This field cannot be empty or consist of only spaces!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldNotAddUserWithEmailInWrongFormat() throws Exception {
        UserDTO user = getUserWithValidFields();
        String email = "@WrongEmail";
        user.setEmail(email);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("email", "Email was entered in the wrong format!");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldNotAddUserWithEmailThatAlreadyExistsInDatabase() throws Exception {
        UserDTO user = getUserWithValidFields();
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("email", "User with such an email address already exists!");
        String email = user.getEmail();
        UserDTO anotherUser = new UserDTO();
        when(userService.findByEmail(email)).thenReturn(anotherUser);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldNotAddUserWithNullPassword() throws Exception {
        UserDTO user = getUserWithValidFields();
        user.setPassword(null);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("password", "This field cannot be empty or consist of only spaces!");
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldNotAddUserWithPasswordInWrongFormat() throws Exception {
        UserDTO user = getUserWithValidFields();
        String password = "Wrong test password";
        user.setPassword(password);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("password", "Password was entered in the wrong format!\n Password must " +
                "contain at least one digit (0-9),\n at least one lowercase Latin character (a-z),\n at least one " +
                "uppercase Latin character (A-Z),\n at least one special character like ! @ # & ( ).\n And must be " +
                "between 8 and 20 characters long.");
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldNotAddUserWithNullFirstName() throws Exception {
        UserDTO user = getUserWithValidFields();
        user.setFirstName(null);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("firstName", "This field cannot be empty or consist of only spaces!");
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldNotAddUserWithFirstNameInWrongFormat() throws Exception {
        UserDTO user = getUserWithValidFields();
        String firstName = "(((WrongTestFirstName)))";
        user.setFirstName(firstName);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("firstName", "The first name must contain only Latin letters\n and" +
                " be between 1 and 20 characters long!");
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldNotAddUserWithNullLastName() throws Exception {
        UserDTO user = getUserWithValidFields();
        user.setLastName(null);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("lastName", "This field cannot be empty or consist of only spaces!");
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldNotAddUserWithLastNameInWrongFormat() throws Exception {
        UserDTO user = getUserWithValidFields();
        String lastName = "(((WrongTestLastName)))";
        user.setLastName(lastName);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("lastName", "The last name must contain only Latin letters\n and" +
                " be between 1 and 40 characters long!");
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldNotAddUserWithNullPatronymic() throws Exception {
        UserDTO user = getUserWithValidFields();
        user.setPatronymic(null);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("patronymic", "This field cannot be empty or consist of only spaces!");
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldNotAddUserWithPatronymicInWrongFormat() throws Exception {
        UserDTO user = getUserWithValidFields();
        String patronymic = "(((WrongTestPatronymic)))";
        user.setPatronymic(patronymic);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("patronymic", "The patronymic must contain only Latin letters\n and" +
                " be between 1 and 40 characters long!");
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldNotAddUserWithNullAddress() throws Exception {
        UserDTO user = getUserWithValidFields();
        user.setAddress(null);
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldNotAddUserWithAddressInWrongFormat() throws Exception {
        UserDTO user = getUserWithValidFields();
        String address = "too long address, too long address, too long address";
        user.setAddress(address);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("address", "The maximum length of the address must be 40 characters!");
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldNotAddUserWithNullPhoneNumber() throws Exception {
        UserDTO user = getUserWithValidFields();
        user.setPhoneNumber(null);
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldNotAddUserWithPhoneNumberInWrongFormat() throws Exception {
        UserDTO user = getUserWithValidFields();
        String phoneNumber = "111-11-hg";
        user.setPhoneNumber(phoneNumber);
        ErrorsDTO errors = new ErrorsDTO();
        errors.addErrorMessage("phoneNumber", "The phone number must match the following pattern:\n" +
                " +375(XX)XXX-XX-XX or +375XXXXXXXXX");
        String email = user.getEmail();
        when(userService.findByEmail(email)).thenThrow(UserNotFoundModuleException.class);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldRequestGetUsers() throws Exception {
        mockMvc.perform(get(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatBusinessLogicIsCalledWhenWeRequestGetUsers() throws Exception {
        mockMvc.perform(get(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON));
        verify(userService, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyCollectionWhenWeRequestGetUsers() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());
        MvcResult mvcResult = mockMvc.perform(get(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(Collections.emptyList()));
    }

    @Test
    void shouldReturnCollectionOfUsersWhenWeRequestGetUsers() throws Exception {
        UserDTO user = getUserWithValidFields();
        List<UserDTO> users = Collections.singletonList(user);
        when(userService.findAll()).thenReturn(users);
        MvcResult mvcResult = mockMvc.perform(get(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(users));
    }

    private UserDTO getUserWithValidFields() {
        UserDTO user = new UserDTO();
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");
        user.setPatronymic("TestPatronymic");
        user.setRole(RoleDTOEnum.ADMIN);
        user.setEmail("Test@email.com");
        user.setPassword("Test1234!");
        user.setAddress("TestAddress");
        user.setPhoneNumber("+375291111111");
        return user;
    }
}
