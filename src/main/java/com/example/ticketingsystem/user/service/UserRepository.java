package com.example.ticketingsystem.user.service;

import com.example.ticketingsystem.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM USER WHERE USER_NAME=:userName limit 1", nativeQuery = true)
    User findByUserName(@Param("userName") String userName);
}
