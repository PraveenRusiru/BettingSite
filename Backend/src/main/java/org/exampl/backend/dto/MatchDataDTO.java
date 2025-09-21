package org.exampl.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.exampl.backend.entity.MatchFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchDataDTO {
    private String id;
    private String tournament;
    private String battingTeam;
    private String battingScore;
    private String bowlingTeam;
    private String bowlingScore;
    private String time;
    private String status;
    private MatchFormat matchFormat;
    private String venue;
}
