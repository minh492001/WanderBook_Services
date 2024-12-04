package com.wander_book.model.branch;


import com.wander_book.model.comon.BaseEntity;
import com.wander_book.model.room.Room;
import com.wander_book.model.service_provide.ServiceProvide;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "branches")
public class Branch extends BaseEntity {

    private String branchName;
    private String city;
    private String address;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "branch_services",
            joinColumns = @JoinColumn(name = "branch_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id"))
    private Collection<ServiceProvide> serviceProvides = new HashSet<>();

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Room> rooms;
}
