package com.sawitPro.services;

import com.sawitPro.helpers.JwtUtil;
import com.sawitPro.models.User;
import com.sawitPro.repository.UserRepository;
import com.sawitPro.viewModels.Login;
import com.sawitPro.viewModels.Registration;
import com.sawitPro.viewModels.UpdateName;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private final String NAME = "ADMIN";
    private final String JWT = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIwODIyMTA0OTE4OTAiLCJpYXQiOjE2ODQxNTY4OTYsImV4cCI6MTY4NDE3NDg5Nn0.AZ7lsOg3jwc3bxUOU-XgWOKNiHVejW868QMLKRyQivn_UVZj-3_aRgqf9WrY5rlfVEOMPNPXUvH8I8caqrdlJD0X60vtMITKCxLmqefwqMvL0KD5M6XFLTPBhRN5AUp51Z1WlFaLqfVzb-3qA-60EHdTwkIkZX-8GbpOVp20JkA4Gz0ZlRhRMG5oJ5KQhnibSEOEdm4ZCNISfSkLIEeE-VFB4BJy7H6IvPHibbs0Zdj4MGUE7iU-3Dz_RAG1RhhLEtXbuQdMJGCa7xCshiGMOnNX5aowgXX0AVixEgO4Libdykt2UNRDOL4HJRS0x6f-_pkx_d3PnuH49oIZMgKuy6cJ0wKJpGfYGQKGysgvvVzga7_y2ZuRx0S-p1SLhOFyK6aVtfMMYvyMgZZoiam_NuFKpWCD18hSdtFOjtAovXdWBTAOBMmmJVyO8gVn9KFM2u3VgzhAWRDz07kQHtY5KPDNAjrHYpKSLo07a6qKp86JlpPsFB36WWdqag0qLGFeHMwITZuPI98wfldTj9HhQEmEwGy9K5gaV8SPFX2aruflvsOogQCK9Swj46HYDcgmivdcwoiOW5mX0q3RjM2zvEf3AqexVsDCnLcIRls-36tKm4-tYJJ9tOyL-ZwveL2leNeUynpMDPCV54GgntppVIXzxN3bRLeOgMWvKEC4r-E";
    private final String BEARER = "Bearer " + JWT;
    private final String PHONE_NUMBER = "082210102020";
    private final String PASSWORD = "Password123";

    @Test
    public void getNameTest() {
        when(jwtUtil.validateAuthorizationHeader(anyString())).thenReturn(true);
        when(jwtUtil.getTokenFromAuthorizationHeader(BEARER)).thenReturn(JWT);
        when(jwtUtil.getPhoneNumberFromToken(JWT)).thenReturn(PHONE_NUMBER);
        User user = new User();
        user.setName(NAME);
        when(userRepository.findUserByPhoneNumber(PHONE_NUMBER)).thenReturn(user);

        String name = userService.getName(BEARER);
        Assert.assertEquals(NAME, name);
    }

    @Test
    public void updateNameTest() {
        when(jwtUtil.validateAuthorizationHeader(anyString())).thenReturn(true);
        when(jwtUtil.getTokenFromAuthorizationHeader(BEARER)).thenReturn(JWT);
        when(jwtUtil.getPhoneNumberFromToken(JWT)).thenReturn(PHONE_NUMBER);
        User user = new User();
        user.setName(NAME);
        when(userRepository.findUserByPhoneNumber(PHONE_NUMBER)).thenReturn(user);

        user.setName("ADMIN2");
        when(userRepository.save(user)).thenReturn(user);

        UpdateName updateName = new UpdateName();
        updateName.setName("ADMIN2");

        Boolean result = userService.updateName(BEARER, updateName);
        Assert.assertTrue(result);
    }

    @Test
    public void loginTest() {
        when(jwtUtil.validateAuthorizationHeader(anyString())).thenReturn(true);
        when(jwtUtil.getTokenFromAuthorizationHeader(BEARER)).thenReturn(JWT);
        when(jwtUtil.getPhoneNumberFromToken(JWT)).thenReturn(PHONE_NUMBER);

        User user = new User();
        user.setName(NAME);
        user.setPassword(PASSWORD);

        when(userRepository.findUserByPhoneNumber(PHONE_NUMBER)).thenReturn(user);
        when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user)).thenReturn(JWT);

        Login login = new Login();
        login.setPassword(PASSWORD);
        login.setPhoneNumber(PHONE_NUMBER);
        String jwt = userService.login(login);
        Assert.assertEquals(JWT, jwt);
    }

    @Test
    public void registrationTest() {
        when(jwtUtil.validateAuthorizationHeader(anyString())).thenReturn(true);
        when(jwtUtil.getTokenFromAuthorizationHeader(BEARER)).thenReturn(JWT);
        when(jwtUtil.getPhoneNumberFromToken(JWT)).thenReturn(PHONE_NUMBER);

        User user = new User();
        user.setName(NAME);
        user.setPassword(PASSWORD);
        user.setPhoneNumber(PHONE_NUMBER);

        when(userRepository.findUserByPhoneNumber(PHONE_NUMBER)).thenReturn(null);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(jwtUtil.generateToken(user)).thenReturn(JWT);
        when(userRepository.save(user)).thenReturn(user);

        Registration registration = new Registration();
        registration.setPassword(PASSWORD);
        registration.setPhoneNumber(PHONE_NUMBER);
        registration.setName(NAME);
        Boolean result = userService.registration(registration);
        Assert.assertTrue(result);
    }
}
