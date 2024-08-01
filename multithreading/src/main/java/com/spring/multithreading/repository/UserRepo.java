package com.spring.multithreading.repository;

import com.spring.multithreading.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
