package org.exampl.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Market {
    @Id
    @Column(name="marketId")
    private String marketId;
    private String type;
    private double odd;
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="matchId")
    private CricketMatch cricketMatch;

}
