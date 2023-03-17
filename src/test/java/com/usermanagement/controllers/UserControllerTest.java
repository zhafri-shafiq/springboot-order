package com.usermanagement.controllers;

import com.usermanagement.enums.Gender;
import com.usermanagement.enums.Status;
import com.usermanagement.models.User;
import com.usermanagement.services.UserService;
import com.usermanagement.utils.aop.AuditLoggable;
import com.usermanagement.utils.aop.AuditLoggableImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserModelAssembler assembler;

    @MockBean
    private AuditLoggable auditLoggable;

    @Test
    public void givenUser_whenGetUser_thenReturnJson() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setName("test user 1");
        user.setEmail("testuser1@email.com");
        user.setGender(Gender.MALE);
        user.setStatus(Status.INACTIVE);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        EntityModel<User> entityModel = EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("users")
        );

        //given
        given(userService.findById(1L)).willReturn(user);
        given(assembler.toModel(user)).willReturn(entityModel);

        //when-then
        mvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(user.getName())))
                .andExpect(jsonPath("email", is(user.getEmail())));
    }

    @Test
    public void givenWrongUser_whenGetUser_thenReturn404() throws Exception {

        //given
        given(userService.findById(2L)).willReturn(null);

        //when-then
        mvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUser_whenPostUser_thenStatus201() throws Exception {
        User user = new User();
        user.setName("test user 1");
        user.setEmail("testuser1@email.com");
        user.setGender(Gender.MALE);
        EntityModel<User> entityModel = EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("users")
        );

        given(userService.create(user)).willReturn(user);
        given(assembler.toModel(user)).willReturn(entityModel);


        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"test user 1\", \"email\": \"testuser1@email.com\", \"gender\": \"MALE\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name", is(user.getName())))
                .andExpect(jsonPath("email", is(user.getEmail())));
    }

    @Test
    public void givenUser_whenPutUser_thenStatus201() throws Exception {
        User user = new User();
        user.setName("test user 1");
        user.setEmail("testuser1@email.com");
        user.setGender(Gender.MALE);
        EntityModel<User> entityModel = EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("users")
        );

        given(userService.updateById(1L, user)).willReturn(user);
        given(assembler.toModel(user)).willReturn(entityModel);


        mvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"test user 1\", \"email\": \"testuser1@email.com\", \"gender\": \"MALE\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name", is(user.getName())))
                .andExpect(jsonPath("email", is(user.getEmail())));
    }

    @Test
    public void givenUser_whenDeleteUser_thenStatus204() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("test user 1");
        user.setEmail("testuser1@email.com");
        user.setGender(Gender.MALE);

        given(userService.findById(1L)).willReturn(user);


        mvc.perform(delete("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenWrongUser_whenDeleteUser_thenStatus404() throws Exception {

        given(userService.findById(2L)).willReturn(null);


        mvc.perform(delete("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}