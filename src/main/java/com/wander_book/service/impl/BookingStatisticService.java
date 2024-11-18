//package com.wander_book.service.impl;
//
//import com.wander_book.repository.*;
//import com.wander_book.service.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class BookingStatisticService implements IBookingStatisticService {
//
//    private final BranchRepository branchRepository;
//    private final RoomRepository roomRepository;
//    private final BookingRepository bookingRepository;
//    private final UserRepository userRepository;
//    private final ServiceProvideRepository serviceProvideRepository;
//
//    @Override
//    public Map<String, Object> getBranchStatistics() {
//        Map<String, Object> branchStats = new HashMap<>();
//        branchStats.put("totalBranches", branchRepository.count());
//
//        branchRepository.findAll().forEach(branch -> {
//            Map<String, Object> branchData = new HashMap<>();
//            branchData.put("totalRooms", roomRepository.countByBranch(branch));
//            branchData.put("roomTypes", roomRepository.countRoomsByTypeInBranch(branch));
//            branchData.put("availableServices", serviceProvideRepository.countByBranch(branch));
//            branchStats.put(branch.getBranchName(), branchData);
//        });
//
//        return branchStats;
//    }
//
//    @Override
//    public Map<String, Object> getRoomStatistics() {
//        return Map.of();
//    }
//
//    @Override
//    public Map<String, Object> getSystemStatistics() {
//        return Map.of();
//    }
//
//    @Override
//    public Map<String, Object> getBookingStatistics() {
//        return Map.of();
//    }
//
//    @Override
//    public Map<String, Object> getCustomerStatistics() {
//        return Map.of();
//    }
//
//    @Override
//    public Map<String, Object> getServiceStatistics() {
//        return Map.of();
//    }
//}
