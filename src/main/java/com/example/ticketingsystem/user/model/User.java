package com.example.ticketingsystem.user.model;

import com.example.ticketingsystem.user.enums.Role;
import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table
@Data
public class User {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Id
    @GeneratedValue
    private long id;

    private Role role;

    private String Name;

    @Column(unique=true)
    private String userName;

    private String password;

    @Email
    private String mailId;

    public boolean isCustomer() {
        if(this.role == Role.CUSTOMER)
            return true;
        return  false;
    }

    public void SetPassword(){
        this.password = encodedPassword();
    }

    public boolean passwordMatch(String rawPassword) {
        return BCrypt.checkpw(rawPassword, this.password);
    }

    public String encodedPassword(){
        return  BCrypt.hashpw(this.password, BCrypt.gensalt());
    }
}
