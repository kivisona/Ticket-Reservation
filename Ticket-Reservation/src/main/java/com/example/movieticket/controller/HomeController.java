package com.example.movieticket.controller;
import com.example.movieticket.model.Booking;
import com.example.movieticket.service.BookingService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class HomeController {
    private final BookingService bookingService;
    public HomeController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
        }
        return "home";
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.setAttribute("username", username);
        Cookie cookie = new Cookie("loggedInUser", username);
        cookie.setMaxAge(30 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/user/dashboard";
    }
    @GetMapping("/user/dashboard")
    public String dashboard(HttpSession session, Model model, @RequestParam(defaultValue = "0") int page) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        Page<Booking> bookingPage = bookingService.getBookingsByUsername(username, page);
        model.addAttribute("username", username);
        model.addAttribute("bookings", bookingPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", Math.min(bookingPage.getTotalPages(), 10));
        return "dashboard";
    }
    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpSession session) {
        session.invalidate();
        Cookie cookie = new Cookie("loggedInUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/login";
    }
}