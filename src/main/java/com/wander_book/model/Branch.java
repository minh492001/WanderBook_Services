package com.wander_book.model;


import com.wander_book.model.comon.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
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
    private Collection<Service> services = new HashSet<>();

    public Branch(String branchName, String city, String address) {
        super();
        this.branchName = branchName;
        this.city = city;
        this.address = address;
    }
}
