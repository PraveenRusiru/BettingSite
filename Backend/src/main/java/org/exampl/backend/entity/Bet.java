package org.exampl.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Bet {
    @Id
    @Column(name="betId")
    private String betId;
    private Date date;
    private Time time;
    private double stake;
    private double amount;
    @Column(name="potentialReturn")
    private double potentialReturn;
    @Enumerated(EnumType.STRING)
    private BetStatus status;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="marketId")
    private Market market;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userId")
    private User user;
}
