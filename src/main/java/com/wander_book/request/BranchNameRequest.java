package com.wander_book.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchNameRequest {
    private long id;
    private String name;
}

