package org.exampl.backend.controller;

import lombok.RequiredArgsConstructor;
import org.exampl.backend.dto.ApiResponse;
import org.exampl.backend.dto.BetDTO;
import org.exampl.backend.service.BetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bet")
@RequiredArgsConstructor
public class BetController {
    private final BetService betService;
    @PostMapping("/placeBet")
    public ResponseEntity<ApiResponse> placeBet(@RequestBody BetDTO betDTO){
        boolean isPlaced = betService.placeBet(betDTO);
        if(isPlaced){
            return ResponseEntity.ok(new ApiResponse(200,"OK",true));
        }
        return ResponseEntity.badRequest().build();
    }
}
