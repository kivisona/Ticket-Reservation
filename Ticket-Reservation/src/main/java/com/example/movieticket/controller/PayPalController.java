package com.example.movieticket.controller;
import com.example.movieticket.service.PayPalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/paypal")
public class PayPalController {
    @Autowired
    private PayPalService payPalService;
    private static final String SUCCESS_URL = "http://localhost:8080/paypal/success";
    private static final String CANCEL_URL = "http://localhost:8080/paypal/cancel";
    @GetMapping("/pay")
    public String pay(@RequestParam("amount") double amount) {
        try {
            Payment payment = payPalService.createPayment(
                    amount, "USD", "paypal",
                    "sale", "Movie Ticket Payment",
                    CANCEL_URL, SUCCESS_URL
            );
            for (var link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
    @GetMapping("/success")
    public String success(@RequestParam("paymentId") String paymentId,
                          @RequestParam("PayerID") String payerId,
                          Model model) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                model.addAttribute("message", "Payment successful!");
                return "payment-success";
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        model.addAttribute("message", "Payment successful!.");
        return "payment-failure";
    }
    @GetMapping("/cancel")
    public String cancel(Model model) {
        model.addAttribute("message", "Payment was cancelled.");
        return "payment-failure";
    }
}