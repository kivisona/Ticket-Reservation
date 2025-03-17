package com.example.movieticket.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/payment")
public class PaymentController {
    @PostMapping
    public String showPaymentPage(@RequestParam String username,
                                  @RequestParam String seatDetails,
                                  @RequestParam double totalAmount,
                                  @RequestParam String theaterName,
                                  @RequestParam String showTime,
                                  Model model) {
        double taxAmount = totalAmount * 0.18;
        double finalAmount = totalAmount + taxAmount;
        model.addAttribute("username", username);
        model.addAttribute("seatDetails", seatDetails);
        model.addAttribute("theaterName", theaterName);
        model.addAttribute("showTime", showTime);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("taxAmount", taxAmount);
        model.addAttribute("finalAmount", finalAmount);
        return "payment";
    }
    @PostMapping("/process")
    public String processPayment(@RequestParam String username,
                                 @RequestParam String totalAmount,
                                 @RequestParam String seatDetails,
                                 @RequestParam String theaterName,
                                 @RequestParam String showTime,
                                 @RequestParam String password,
                                 Model model) {
        if (!isValidPassword(username, password)) {
            model.addAttribute("error", "Invalid password! Please try again.");
            return "payment";
        }
        return "redirect:/paypal/pay?amount=" + totalAmount;
    }
    private boolean isValidPassword(String username, String password) {
        return password.equals("1234");
    }
}