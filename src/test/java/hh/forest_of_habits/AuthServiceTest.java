package hh.forest_of_habits;

import hh.forest_of_habits.dto.request.AuthRequest;
import hh.forest_of_habits.dto.request.RegistrationRequest;
import hh.forest_of_habits.dto.response.AuthResponse;
import hh.forest_of_habits.entity.User;
import hh.forest_of_habits.exception.AuthenticationException;
import hh.forest_of_habits.exception.EmailAlreadyExistsException;
import hh.forest_of_habits.exception.UserAlreadyExistsException;
import hh.forest_of_habits.service.AuthService;
import hh.forest_of_habits.service.UserService;
import hh.forest_of_habits.utils.JwtTokenUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JwtTokenUtils tokenUtils;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Логин незарегистрированного юзера")
    public void loginUserNotExists() {
        AuthRequest authRequest = AuthRequest.builder().build();
        Mockito.when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException(""));

        assertThrows(AuthenticationException.class, () -> authService.login(authRequest));
    }

    @Test
    @DisplayName("Аутентификация зарегистрированного юзера")
    public void loginUserExists() {
        String username = "username";
        Authentication auth = mock(Authentication.class);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username, "", List.of()
        );
        AuthRequest authRequest = AuthRequest.builder().username(username).password("").build();

        Mockito.when(auth.getPrincipal()).thenReturn(userDetails);
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(auth);

        assertEquals(username, authService.login(authRequest).getUserName());
    }

    @Test
    @DisplayName("Регистрация с уже существующим логином")
    public void registrationLoginAlreadyExists() {
        RegistrationRequest registrationRequest = RegistrationRequest.builder().build();

        Mockito.when(userService.findByName(any())).thenReturn(Optional.of(User.builder().build()));
        assertThrows(UserAlreadyExistsException.class, () -> authService.registration(registrationRequest));
    }

    @Test
    @DisplayName("Регистрация с уже существующим логином")
    public void registrationEmailAlreadyExists() {
        RegistrationRequest registrationRequest = RegistrationRequest.builder().build();

        Mockito.when(userService.findByName(any())).thenReturn(Optional.empty());
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.of(User.builder().build()));
        assertThrows(EmailAlreadyExistsException.class, () -> authService.registration(registrationRequest));
    }

    @Test
    @DisplayName("Регистрация нового пользователя")
    public void registrationNewUser() {
        String username = "username";
        String password = "password";
        String email = "email@mail.ru";
        String mockToken = "token";
        Boolean agreementConfirmation = true;
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .username(username)
                .password(password)
                .email(email)
                .agreementConfirmation(agreementConfirmation)
                .build();

        Mockito.when(tokenUtils.generateToken(any())).thenReturn(mockToken);

        AuthResponse authResponse = authService.registration(registrationRequest);

        ArgumentCaptor<RegistrationRequest> argument = ArgumentCaptor.forClass(RegistrationRequest.class);
        Mockito.verify(userService).createNewUser(argument.capture());
        assertEquals(username, argument.getValue().getUsername());
        assertEquals(username, authResponse.getUserName());
        assertEquals(mockToken, authResponse.getToken());
    }
}