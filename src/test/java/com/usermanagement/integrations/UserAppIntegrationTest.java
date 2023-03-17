package com.usermanagement.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usermanagement.enums.Gender;
import com.usermanagement.enums.Status;
import com.usermanagement.models.User;
import com.usermanagement.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserAppIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @BeforeEach
    void createUser() throws Exception {
        User activeUser = new User();
        activeUser.setId(1L);
        activeUser.setName("test user 1");
        activeUser.setEmail("testuser1@email.com");
        activeUser.setGender(Gender.MALE);
        activeUser.setStatus(Status.ACTIVE);

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(activeUser)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUserCreateThroughAllLayers() throws Exception {
        User inactiveUser = new User();
        inactiveUser.setId(2L);
        inactiveUser.setName("test user 2");
        inactiveUser.setEmail("testuser2@email.com");
        inactiveUser.setGender(Gender.MALE);

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(inactiveUser)))
                .andExpect(status().isCreated());

        User user = userService.findById(2L);
        Assertions.assertEquals(inactiveUser.getName(), user.getName());
    }

    @Test
    void testUserFindByIdThroughAllLayers() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/users/4")
                        .contentType("application/json"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        User user = objectMapper.readValue(content, User.class);

        Assertions.assertEquals(4L, user.getId());
        Assertions.assertEquals("test user 1", user.getName());
    }

    @Test
    void testUserUpdateThroughAllLayers() throws Exception {
        User inactiveUser = new User();
        inactiveUser.setName("test user 3");
        inactiveUser.setEmail("testuser3@email.com");
        inactiveUser.setGender(Gender.MALE);

        mockMvc.perform(put("/api/users/3")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(inactiveUser)))
                .andExpect(status().isCreated());

        User user = userService.findById(3L);
        Assertions.assertEquals(inactiveUser.getName(), user.getName());
    }

    @Test
    void testUserDeleteThroughAllLayers() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                        .contentType("application/json"))
                .andExpect(status().isNoContent());
    }
}
