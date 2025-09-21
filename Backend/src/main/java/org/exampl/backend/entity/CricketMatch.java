package org.exampl.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CricketMatch {
    @Id
    @Column(name="matchId")
    private String matchId;
    private String matchSeries;
    private String team1;
    private String team2;
    private String location;
    @Enumerated(EnumType.STRING)
    private MatchFormat matchFormat;
    @Column(name="isLive")
    private boolean isLive;
}
