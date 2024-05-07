package com.parking.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.management.controllers.AuthController;
import com.parking.management.services.AuthService;
import com.parking.management.dto.RegistrationDTO;
import com.parking.management.dto.LoginDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AuthController.class)
public class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        when(authService.registrationUser(any(String.class), any(String.class), any(String.class)))
                .thenReturn("mock-token");
        when(authService.loginUser(any(String.class), any(String.class)))
                .thenReturn("mock-token");
        when(authService.registrationUser("username", "password", "email@example.com"))
                .thenThrow(new IllegalArgumentException("Username already taken"));
    }

    @Test
    public void testRegisterUser() throws Exception {
        RegistrationDTO request = new RegistrationDTO("username", "password", "email@example.com");

        mockMvc.perform(post("/api/auth/registration/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-token"));
    }

    @Test
    public void testRegisterUserWithExistingEmail() throws Exception {
        RegistrationDTO request = new RegistrationDTO("username", "password", "email@example.com");

        mockMvc.perform(post("/api/auth/registration/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username already taken"));
    }

    @Test
    public void testLoginUser() throws Exception {
        LoginDTO loginDTO = new LoginDTO("email@example.com", "password");

        mockMvc.perform(post("/api/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-token"));
    }

    @Test
    public void testLoginUserWithWrongCredentials() throws Exception {
        LoginDTO loginDTO = new LoginDTO("wrongemail@example.com", "wrongPassword");

        mockMvc.perform(post("/api/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }
}
