import com.gmail.yauheniylebedzeu.repository.UserRepository;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.UserConverter;
import com.gmail.yauheniylebedzeu.service.exception.UserNotFoundException;
import com.gmail.yauheniylebedzeu.service.impl.UserServiceImpl;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void add() {
    }

    @Test
    void shouldFindUserByEmail() {
        String email = "Uaer@email.ru";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        when(userConverter.convertUserToUserDTO(user)).thenReturn(userDTO);
        UserDTO resultUserDTO = userService.getByEmail(email);
        assertEquals(email, resultUserDTO.getEmail());
    }

    @Test
    void shouldFindUserByEmailAndGetEmptyOptionalObject() {
        String email = "Uaer@email.ru";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getByEmail(email));
    }

    @Test
    void shouldGetCountOfUsers() {
        Long countOfUsers = 5L;
        when(userRepository.getCountOfEntities()).thenReturn(countOfUsers);
        Long resultCountOfUsers = userService.getCountOfUsers();
        assertEquals(countOfUsers, resultCountOfUsers);
    }

    @Test
    void findAll() {
    }

    @Test
    void changePasswordByUuid() {
    }

    @Test
    void changeRoleByUuid() {
    }

    @Test
    void removeByUuid() {
    }
}