package com.usermanagement.controllers;

import com.usermanagement.exceptions.UserNotFoundException;
import com.usermanagement.models.User;
import com.usermanagement.services.UserService;
import com.usermanagement.utils.aop.AuditLoggable;
import com.usermanagement.utils.aop.LogMethodDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/users")
public class UserController {

    private UserService userService;

    private UserModelAssembler assembler;

    @Autowired
    public UserController(UserService userService, UserModelAssembler assembler) {
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping
    @LogMethodDetails
    public CollectionModel<EntityModel<User>> getAllUsers() {
        List<EntityModel<User>> users = userService.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return assembler.toModel(user);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @PostMapping
    @LogMethodDetails
    public ResponseEntity<?> createUser(@Valid @RequestBody User newUser) {
//        ((AuditLoggable)userService).auditLog(newUser, "INSERT");
        User user = userService.create(newUser);
        EntityModel<User> entityModel = assembler.toModel(user);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userRequest) {
//        ((AuditLoggable)userService).auditLog(userRequest, "UPDATE");
        User updatedUser = userService.updateById(id, userRequest);

        EntityModel<User> entityModel = assembler.toModel(updatedUser);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            userService.delete(user);
            return ResponseEntity.noContent().build();
        } else {
            throw new UserNotFoundException(id);
        }
    }
}
