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
