package com.example.movieticket.controller;
import com.example.movieticket.model.Booking;
import com.example.movieticket.service.BookingService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/booking")
public class BookingController {
    private final APIContext apiContext;
    private final BookingService bookingService;
    public BookingController(APIContext apiContext, BookingService bookingService) {
        this.apiContext = apiContext;
        this.bookingService = bookingService;
    }
    @GetMapping
    public String showBookingPage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", username);
        return "booking";
    }
    @PostMapping("/save")
    public String saveBooking(@RequestParam String theaterName,
                              @RequestParam String showTime,
                              @RequestParam String seatDetails,
                              @RequestParam double totalAmount,
                              HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login"; // ✅ Ensure user is logged in
        }
        Booking booking = new Booking();
        booking.setUsername(username);
        booking.setTheaterName(theaterName);
        booking.setShowTime(showTime);
        booking.setSeatDetails(seatDetails);
        booking.setTotalAmount(totalAmount);
        bookingService.saveBooking(booking);
        session.setAttribute("latestBooking", booking);
        return "redirect:/user/dashboard";
    }
    @PostMapping("/pay")
    public String createPayment(@RequestParam String theaterName,
                                @RequestParam String showTime,
                                @RequestParam String seatDetails,
                                @RequestParam double totalAmount,
                                HttpSession session,
                                Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login"; // ✅ Ensure user is logged in
        }
        try {
            Amount amount = new Amount();
            amount.setCurrency("USD");
            amount.setTotal(String.format("%.2f", totalAmount));
            Transaction transaction = new Transaction();
            transaction.setDescription("Movie Ticket Payment");
            transaction.setAmount(amount);
            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);
            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");
            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);
            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl("http://localhost:8080/booking/cancel");
            redirectUrls.setReturnUrl("http://localhost:8080/booking/success");
            payment.setRedirectUrls(redirectUrls);
            Payment createdPayment = payment.create(apiContext);
            for (Links link : createdPayment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    //Save booking details temporarily in session
                    Booking tempBooking = new Booking();
                    tempBooking.setUsername(username);
                    tempBooking.setTheaterName(theaterName);
                    tempBooking.setShowTime(showTime);
                    tempBooking.setSeatDetails(seatDetails);
                    tempBooking.setTotalAmount(totalAmount);
                    session.setAttribute("tempBooking", tempBooking);
                    return "redirect:" + link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            model.addAttribute("error", "Payment error occurred: " + e.getMessage());
            return "payment-failed";
        }
        return "redirect:/booking";
    }
    @GetMapping("/success")
    public String successPayment(@RequestParam("paymentId") String paymentId,
                                 @RequestParam("PayerID") String payerId,
                                 HttpSession session,
                                 Model model) {
        try {
            Payment payment = new Payment();
            payment.setId(paymentId);
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);
            Payment executedPayment = payment.execute(apiContext, paymentExecution);
            model.addAttribute("payment", executedPayment);
            Booking completedBooking = (Booking) session.getAttribute("tempBooking");
            if (completedBooking != null) {
                bookingService.saveBooking(completedBooking);
                session.setAttribute("latestBooking", completedBooking);
                session.removeAttribute("tempBooking");
            }
            return "payment-success";
        } catch (PayPalRESTException e) {
            model.addAttribute("error", "Payment execution error: " + e.getMessage());
            return "payment-failed";
        }
    }
    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            return "redirect:/user/dashboard";
        }
        model.addAttribute("booking", booking);
        return "edit";
    }
    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        if (booking != null) {
            bookingService.deleteBooking(id);
            double refundAmount = booking.getTotalAmount() - 50;
            model.addAttribute("refundAmount", refundAmount);
            return "refund";
        }
        return "redirect:/user/dashboard";
    }
    @GetMapping("/cancel-confirmation/{id}")
    public String showCancelPage(@PathVariable Long id, Model model) {
        model.addAttribute("bookingId", id);
        return "cancel";
    }
    @PostMapping("/delete/{id}")
    public String deleteBooking(@PathVariable("id") Long id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        if (booking != null) {
            bookingService.deleteBooking(id);
            double refundAmount = booking.getTotalAmount() - 50;
            model.addAttribute("refundAmount", refundAmount);
            return "refund";
        }
        return "redirect:/user/dashboard";
    }
    @GetMapping("/cancel")
    public String cancelPayment() {
        return "payment-cancel";
    }
}