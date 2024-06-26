package hh.forest_of_habits.service;

import hh.forest_of_habits.dto.request.RegistrationRequest;
import hh.forest_of_habits.dto.response.UserInfoResponse;
import hh.forest_of_habits.entity.User;
import hh.forest_of_habits.mapper.UserMapper;
import hh.forest_of_habits.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    UserMapper mapper;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Поиск существующего пользователя")
    public void findUserExists() {
        String username = "username";

        User user = new User();
        user.setName(username);

        Mockito.when(userRepository.findByName(username)).thenReturn(Optional.of(user));

        assertEquals(
                username,
                userService.findByName(username).orElseGet(User::new).getName()
        );
    }

    @Test
    @DisplayName("Поиск несуществующего пользователя")
    public void findUserNotExists() {
        String username = "username";

        Mockito.when(userRepository.findByName(username)).thenReturn(Optional.empty());

        Assertions.assertTrue(
                userService.findByName(username).isEmpty()
        );
    }

    @Test
    @DisplayName("Получение UserDetails существующего юзера")
    public void getUserDetailsUserExists() {
        String username = "username";
        String password = "password";

        User user = User.builder()
                .name(username)
                .password(password)
                .build();

        Mockito.when(userRepository.findByName(username)).thenReturn(Optional.of(user));

        assertEquals(
                username,
                userService.loadUserByUsername(username).getUsername()
        );
        assertEquals(
                password,
                userService.loadUserByUsername(username).getPassword()
        );
    }

    @Test
    @DisplayName("Получение UserDetails несуществующего юзера")
    public void getUserDetailsUserNotExists() {
        String username = "username";

        Mockito.when(userRepository.findByName(username)).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(username)
        );
    }

    @Test
    @DisplayName("Сохранение юзера")
    public void saveUser() {
        String username = "username";
        String password = "password";
        String encoded = "encoded";

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .username(username)
                .password(password)
                .build();

        Mockito.when(passwordEncoder.encode(any(String.class))).thenReturn(encoded);

        userService.createNewUser(registrationRequest);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(argument.capture());
        assertEquals(username, argument.getValue().getName());
        assertEquals(encoded, argument.getValue().getPassword());
    }

    @Test
    @DisplayName("Получение информации о пользователе")
    void getUserInfo() {
        String username = "username";

        User user = new User();
        user.setName(username);

        UserInfoResponse response = UserInfoResponse.builder()
                .username(username)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "");
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByName(username)).thenReturn(Optional.of(user));
        when(mapper.map(any(User.class))).thenReturn(response);

        var actual = userService.getUserInfo();

        assertEquals(username, actual.getUsername());
    }
}
