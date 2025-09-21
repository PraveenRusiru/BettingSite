package org.exampl.backend.controller;

import lombok.RequiredArgsConstructor;
import org.exampl.backend.dto.ApiResponse;
import org.exampl.backend.dto.AuthDTO;
import org.exampl.backend.dto.UpdateDataDTO;
import org.exampl.backend.dto.UserDataDTO;
import org.exampl.backend.entity.Role;
import org.exampl.backend.service.AuthServiceImpl;
import org.exampl.backend.service.UserDataServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userData")
@RequiredArgsConstructor
public class userDataController {
    private final UserDataServiceImpl userDataServiceImpl;
    private final AuthServiceImpl authServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> getAllData(@RequestParam("username") String username) {
        UserDataDTO allUserData = userDataServiceImpl.getAllUserData(username);
        if (allUserData == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponse(200,"OK",allUserData));
    }
    @PostMapping("/checkPassword")
    public ResponseEntity<ApiResponse> checkPassword(@RequestBody AuthDTO authDTO) {
        boolean isValid = userDataServiceImpl.validateCurrentPassword(authDTO);
        if (isValid) {
            return ResponseEntity.ok(new ApiResponse(200,"OK",isValid));
        }else{
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/updateData")
    public ResponseEntity<ApiResponse> updateData(@RequestBody UpdateDataDTO updateDataDTO) {
//        String id=userDataService.findUserIdByUsername(updateDataDTO.getUsername());
//        if (id!=null) {

        UserDataDTO allUserData = userDataServiceImpl.getAllUserData(updateDataDTO.getOldUsername());
        System.out.println("userId "+updateDataDTO.getId()+" username "+updateDataDTO.getOldUsername()+" email "+updateDataDTO.getEmail()+" nic "+updateDataDTO.getNic()+" password "+updateDataDTO.getNewPassword());

        if(updateDataDTO.getNewPassword()!=null && updateDataDTO.getConfirmnewPassword()!=null) {
            if(updateDataDTO.getNewPassword().equals(updateDataDTO.getConfirmnewPassword())){
                updateDataDTO.setRole(Role.USER);
                boolean isUpdated = userDataServiceImpl.updateUserData(updateDataDTO);
                if (isUpdated) {
                    return ResponseEntity.ok(new ApiResponse(200,"OK",isUpdated));
                }
                else{
                    return ResponseEntity.badRequest().build();
                }
            }
            return ResponseEntity.badRequest().build();

        }

//        updateDataDTO.setNewPassword(allUserData.getPassword());
        updateDataDTO.setRole(Role.USER);
        boolean isUpdated = userDataServiceImpl.updateUserData(updateDataDTO);
        if (isUpdated) {
            return ResponseEntity.ok(new ApiResponse(200,"OK",isUpdated));
        }
        else{
            return ResponseEntity.badRequest().build();
        }

    }
}
