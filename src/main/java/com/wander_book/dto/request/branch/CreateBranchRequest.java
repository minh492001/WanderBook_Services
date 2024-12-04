package com.wander_book.dto.request.branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBranchRequest {
    private String branchName;
    private String city;
    private String address;
}
