package com.wander_book.service;

import java.util.Map;

public interface IBookingStatisticService {

    Map<String, Object> getBranchStatistics();

    Map<String, Object> getRoomStatistics();

    Map<String, Object> getSystemStatistics();

    Map<String, Object> getBookingStatistics();

    Map<String, Object> getCustomerStatistics();

    Map<String, Object> getServiceStatistics();
}
