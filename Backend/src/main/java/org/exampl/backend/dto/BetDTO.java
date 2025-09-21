package org.exampl.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.exampl.backend.entity.BetStatus;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BetDTO {
    private String username;
    private Date date;
    private Time time;
    private double stake;
    private double amount;
    private double potentialReturn;
    private BetStatus status;
}
