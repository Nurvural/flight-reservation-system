package com.flightreservation.user_service.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    @Column(name="passport_number")
    private String passportNumber;

    @Column(name="date_of_birth")
    private LocalDate dateOfBirth;

    private String nationality;

    @Column(name="loyalty_points")
    private Integer loyaltyPoints;
}
