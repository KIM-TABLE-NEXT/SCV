package com.sparta.scv.user.repository;

import com.sparta.scv.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  UserNamePassword findByUsername(String name);
}
