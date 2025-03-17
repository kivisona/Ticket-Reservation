package com.example.movieticket.service;
import com.example.movieticket.model.User;
import com.example.movieticket.repository.UserRepository;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public boolean isUserExists(String username, String mobileNumber) {
        return userRepository.findByUsernameAndMobileNumber(username, mobileNumber) != null;
    }
    public void registerUser(User user) {
        if (!isUserExists(user.getUsername(), user.getMobileNumber())) {
            userRepository.save(user);
        }
    }
    public User findByUsernameAndMobile(String username, String mobileNumber) {
        User user = userRepository.findByUsernameAndMobileNumber(username, mobileNumber);
        if (user == null) {
            System.out.println("User not found for: " + username + " | " + mobileNumber);
        } else {
            System.out.println("User found: " + user.getUsername());
        }
        return user;
    }
}