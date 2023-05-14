package com.sawitPro.repository;

import com.sawitPro.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("Select u from User u Where u.phoneNumber = ?1")
    User findUserByPhoneNumber(String phoneNumber);
}
