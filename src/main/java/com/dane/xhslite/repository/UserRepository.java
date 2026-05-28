package com.dane.xhslite.repository;

import com.dane.xhslite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
