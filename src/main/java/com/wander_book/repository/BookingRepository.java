//package com.wander_book.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
//    List<BookedRoom> findByGuestEmail(String email);
//
//    Optional<BookedRoom> findByBookingConfirmationCode(String confirmationCode);
//}
