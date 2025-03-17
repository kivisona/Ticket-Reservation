package com.example.movieticket.repository;
import com.example.movieticket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndMobileNumber(String username, String mobileNumber);
}
