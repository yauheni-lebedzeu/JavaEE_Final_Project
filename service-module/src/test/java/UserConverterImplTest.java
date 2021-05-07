import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.impl.UserConverterImpl;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserConverterImplTest {

    private final UserConverterImpl userConverter = new UserConverterImpl();

    @Test
    void shouldConvertUserDTOToUserAndGetNotNullObject() {
        UserDTO userDTO = new UserDTO();
        User user = userConverter.convertUserDTOtoUser(userDTO);
        assertNotNull(user);
    }

    @Test
    void shouldConvertUserDTOToUserAndReturnRightFirstName() {
        UserDTO userDTO = new UserDTO();
        String firstName = "test first name";
        userDTO.setFirstName(firstName);
        User user = userConverter.convertUserDTOtoUser(userDTO);
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    void shouldConvertUserDTOToUserAndReturnRightLastName() {
        UserDTO userDTO = new UserDTO();
        String lastName = "test last name";
        userDTO.setLastName(lastName);
        User user = userConverter.convertUserDTOtoUser(userDTO);
        assertEquals(lastName, user.getLastName());
    }

    @Test
    void shouldConvertUserDTOToUserAndReturnRightPatronymic() {
        UserDTO userDTO = new UserDTO();
        String patronymic = "test patronymic";
        userDTO.setPatronymic(patronymic);
        User user = userConverter.convertUserDTOtoUser(userDTO);
        assertEquals(patronymic, user.getPatronymic());
    }

    @Test
    void shouldConvertUserDTOToUserAndReturnRightEmail() {
        UserDTO userDTO = new UserDTO();
        String email = "test email";
        userDTO.setEmail(email);
        User user = userConverter.convertUserDTOtoUser(userDTO);
        assertEquals(email, user.getEmail());
    }

    @Test
    void shouldConvertUserDTOToUserAndReturnRightPassword() {
        UserDTO userDTO = new UserDTO();
        String password = "test password";
        userDTO.setPassword(password);
        User user = userConverter.convertUserDTOtoUser(userDTO);
        assertEquals(password, user.getPassword());
    }
}