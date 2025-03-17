package com.example.movieticket.service;
import com.example.movieticket.model.Booking;
import com.example.movieticket.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    @Transactional
    public Booking saveBooking(Booking booking) {
        if (booking == null || booking.getUsername() == null || booking.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Invalid booking data! Username is required.");
        }
        return bookingRepository.save(booking);
    }
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    @Transactional
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
    public List<Booking> getBookingsByUsername(String username) {
        return bookingRepository.findByUsername(username);
    }
    public Booking getBookingById(Long id) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        return optionalBooking.orElse(null);
    }
    public Page<Booking> getBookingsByUsername(String username, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return bookingRepository.findByUsername(username, pageable);
    }
}