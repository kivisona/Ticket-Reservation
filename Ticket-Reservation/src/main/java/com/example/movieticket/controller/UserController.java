package com.example.movieticket.controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import com.example.movieticket.model.User;
import com.example.movieticket.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/signup")
    public String showSignUpPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }
    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        if (userService.isUserExists(user.getUsername(), user.getMobileNumber())) {
            redirectAttributes.addFlashAttribute("error", "User already exists! Please login.");
            return "redirect:/user/login";
        }
        userService.registerUser(user);
        return "redirect:/user/login";
    }
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String mobileNumber,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttributes) {
        // Simulate user validation (replace this with your actual validation logic)
        User user = userService.findByUsernameAndMobile(username, mobileNumber);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            Cookie cookie = new Cookie("loggedInUser", username);
            cookie.setMaxAge(30 * 60);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/user/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or mobile number.");
            return "redirect:/user/login";
        }
    }
}