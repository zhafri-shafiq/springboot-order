package com.usermanagement.utils;

import com.usermanagement.enums.Gender;
import com.usermanagement.enums.Status;
import com.usermanagement.models.User;
import com.usermanagement.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class Seeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(Seeder.class);

    private UserRepository userRepository;

    public Seeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void run(ApplicationArguments args) throws ParseException {
        User activeUser = new User();
        activeUser.setName("John Doe");
        activeUser.setEmail("activeuser@email.com");
        activeUser.setGender(Gender.MALE);
        activeUser.setStatus(Status.ACTIVE);
        activeUser.setCreatedAt(new Date());
        activeUser.setUpdatedAt(new Date());
        userRepository.save(activeUser);

        User inactiveUser = new User();
        inactiveUser.setName("Mary What");
        inactiveUser.setEmail("inactiveuser@email.com");
        inactiveUser.setGender(Gender.FEMALE);
        inactiveUser.setStatus(Status.INACTIVE);
        inactiveUser.setCreatedAt(new Date());
        inactiveUser.setUpdatedAt(new Date());
        userRepository.save(inactiveUser);

        userRepository.findAll().forEach(user -> log.info("Preloaded " + user));
    }
}
