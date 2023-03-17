package com.usermanagement.services;

import com.usermanagement.enums.Gender;
import com.usermanagement.enums.Status;
import com.usermanagement.models.User;
import com.usermanagement.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private UserService service = new UserService(userRepository);

    @Test
    void whenFindAll_thenReturnAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setName("test user 1");
        user.setEmail("testuser1@email.com");
        user.setGender(Gender.MALE);
        user.setStatus(Status.INACTIVE);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        User[] users = new User[1];
        users[0] = user;

        //given
        Mockito.when(userRepository.findAll()).thenReturn(List.of(users));

        //when
        List<User> result = service.findAll();

        //then
        Assertions.assertArrayEquals(users, result.toArray());
    }

    @Test
    void whenFindById_thenReturnUser() {
        User user = new User();
        user.setId(1L);
        user.setName("test user 1");
        user.setEmail("testuser1@email.com");
        user.setGender(Gender.MALE);
        user.setStatus(Status.INACTIVE);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        Optional<User> userOptional = Optional.ofNullable(user);

        //given
        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);

        //when
        User result = service.findById(1L);

        //then
        Assertions.assertEquals(user.getId(), result.getId());
        Assertions.assertEquals(user.getName(), result.getName());
        Assertions.assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void whenCreate_thenReturnUser() {
        User user = new User();
        user.setId(1L);
        user.setName("test user 1");
        user.setEmail("testuser1@email.com");
        user.setGender(Gender.MALE);
        user.setStatus(Status.INACTIVE);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        Optional<User> userOptional = Optional.ofNullable(user);

        //given
        Mockito.when(userRepository.save(user)).thenReturn(user);

        //when
        User result = service.create(user);

        //then
        Assertions.assertEquals(user.getId(), result.getId());
        Assertions.assertEquals(user.getName(), result.getName());
        Assertions.assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void whenUpdateById_thenReturnUser() throws CloneNotSupportedException {
        User inactiveUser = new User();
        inactiveUser.setId(1L);
        inactiveUser.setName("test user 1");
        inactiveUser.setEmail("testuser1@email.com");
        inactiveUser.setGender(Gender.MALE);
        inactiveUser.setStatus(Status.INACTIVE);
        inactiveUser.setCreatedAt(new Date());
        inactiveUser.setUpdatedAt(new Date());

        User activeUser = (User) inactiveUser.clone();
        activeUser.setStatus(Status.ACTIVE);

        Assertions.assertEquals(Status.INACTIVE, inactiveUser.getStatus());

        //given
        Mockito.when(userRepository.findById(inactiveUser.getId())).thenReturn(Optional.of(inactiveUser));
        Mockito.when(userRepository.save(inactiveUser)).thenReturn(activeUser);

        //when
        User result = service.updateById(inactiveUser.getId(), activeUser);

        //then
        Assertions.assertEquals(Status.ACTIVE, result.getStatus());
    }
}