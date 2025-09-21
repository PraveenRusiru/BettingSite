package org.exampl.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpcomingMatchDTO {
    private String matchId;
    private String match;
    private String superEvent;
    private String location;
    private String startDate;
    private String eventStatus;
    private String teamA;
    private String teamB;
}
