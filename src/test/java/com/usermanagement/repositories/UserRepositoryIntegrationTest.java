package com.usermanagement.repositories;

import com.usermanagement.enums.Gender;
import com.usermanagement.enums.Status;
import com.usermanagement.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindAll_thenReturnAllUsers() {
        // given
        User user = new User();
        user.setName("test user 1");
        user.setEmail("testuser1@email.com");
        user.setGender(Gender.MALE);
        user.setStatus(Status.INACTIVE);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        User[] users = new User[1];
        users[0] = user;

        entityManager.persist(user);
        entityManager.flush();

        // when
        List<User> found = userRepository.findAll();

        // then
        Assertions.assertArrayEquals(users, found.toArray());
    }

    @Test
    public void givenUserIsPersisted_whenFindById_thenReturnUser() {
        // given
        User user = new User();
        user.setName("test user 1");
        user.setEmail("testuser1@email.com");
        user.setGender(Gender.MALE);
        user.setStatus(Status.INACTIVE);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findById(user.getId());

        // then
        if (found.isPresent()) {
            found.map(actual -> {
                Assertions.assertEquals(user.getName(), actual.getName());
                return actual;
            });
        }
    }

    @Test
    public void whenUserIsSaved_thenUserIsPersisted() {
        User user = new User();
        user.setName("test user 1");
        user.setEmail("testuser1@email.com");
        user.setGender(Gender.MALE);
        user.setStatus(Status.INACTIVE);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        // when
        userRepository.save(user);

        // then
        Assertions.assertEquals(user, userRepository.findById(user.getId()).get());
    }

    @Test
    public void givenUserIsPersisted_whenDelete_thenUserIsNotPersisted() {
        User user = new User();
        user.setName("test user 1");
        user.setEmail("testuser1@email.com");
        user.setGender(Gender.MALE);
        user.setStatus(Status.INACTIVE);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        userRepository.save(user);
        Assertions.assertEquals(user, userRepository.findById(user.getId()).get());

        // when
        userRepository.delete(user);

        // then
        Optional<User> result = userRepository.findById(user.getId());
        Assertions.assertTrue(result.isEmpty());
    }
}