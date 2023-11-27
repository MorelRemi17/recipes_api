package com.scub.recipes;

import com.scub.recipies.Main;
import com.scub.recipies.model.User;
import com.scub.recipies.repository.UserRepository;
import com.scub.recipies.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = Main.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserById() throws Exception {
        Long userId = 1L;
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk());
    }

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(15L);
        user.setName("Remi Morel");
        user.setEmail("remi.morel@example.com");
        user.setPassword("dzqdzq1351");
    }

    @Test
    public void whenSavingUserWithEmailThatExists_thenThrowException() {
        User user = new User();
        user.setEmail("existingMail@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        Exception exception = assertThrows(Exception.class, () -> {
            userService.saveUser(user);
        });

        assertEquals("L'email est déjà utilisé par un autre utilisateur.", exception.getMessage());
        verify(userRepository).findByEmail(anyString());
    }


    @Test
    public void whenAuthenticatingWithValidCredentials_thenReturnUser() {
        when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(user);

        User authenticatedUser = userService.authenticate(user.getEmail(), user.getPassword());

        assertNotNull(authenticatedUser);
        assertEquals(user.getEmail(), authenticatedUser.getEmail());
        verify(userRepository, times(1)).findByEmailAndPassword(user.getEmail(), user.getPassword());
    }
}
