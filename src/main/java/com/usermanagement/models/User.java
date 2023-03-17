package com.usermanagement.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.usermanagement.enums.Gender;
import com.usermanagement.enums.Status;
import com.usermanagement.events.UserCreatedEvent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Date;

@Entity
@Data
@Table(name = "users")
public class User extends AbstractAggregateRoot<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private Gender gender;

    private Status status;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public void onCreated() {
        registerEvent(new UserCreatedEvent());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        User user = new User();
        user.setId(this.id);
        user.setName(this.name);
        user.setGender(this.gender);
        user.setStatus(this.status);
        user.setCreatedAt(this.createdAt);
        user.setUpdatedAt(this.updatedAt);
        return user;
    }
}
