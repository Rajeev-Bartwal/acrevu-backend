package com.acrevu.acrevu_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dealer_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealerPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;      // RESIDENTIAL / COMMERCIAL / INDUSTRIAL
    private String listingType;   // RENT / SALE / PURCHASE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
