package com.usermanagement.services;

import com.usermanagement.enums.Status;
import com.usermanagement.events.UserCreatedEvent;
import com.usermanagement.models.User;
import com.usermanagement.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User create(User newUser) {
        newUser.setCreatedAt(new Date());
        newUser.setUpdatedAt(new Date());
        newUser.setStatus(Status.INACTIVE);
        User user = userRepository.save(newUser);
        newUser.onCreated();
        return user;
    }

    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return null;
        } else {
            return user.get();
        }
    }

    @Transactional
    public User updateById(Long id, User userRequest) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(userRequest.getName());
                    user.setEmail(userRequest.getEmail());
                    user.setGender(userRequest.getGender());
                    user.setStatus(userRequest.getStatus());
                    user.setUpdatedAt(new Date());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    userRequest.setId(id);
                    userRequest.setStatus(Status.INACTIVE);
                    userRequest.setCreatedAt(new Date());
                    userRequest.setUpdatedAt(new Date());
                    return userRepository.save(userRequest);
                });
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Handled UserCreatedEvent");
    }

}
