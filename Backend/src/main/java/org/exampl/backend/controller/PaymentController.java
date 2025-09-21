package org.exampl.backend.controller;

import lombok.RequiredArgsConstructor;
import org.exampl.backend.dto.ApiResponse;
import org.exampl.backend.dto.PayhereDTO;
import org.exampl.backend.dto.PaymentDTO;
import org.exampl.backend.dto.UserDataDTO;
import org.exampl.backend.service.PaymentService;
import org.exampl.backend.service.UserDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RequestMapping("/payment")
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final UserDataService userDataService;
    @PostMapping("/generateCode")
    public ResponseEntity<ApiResponse> deposit(@RequestBody PaymentDTO paymentDTO) throws Exception {
        String paymentID=paymentService.getNextPaymentId();
        String hashCode = generateHash("", paymentID, paymentDTO.getAmount(), "LKR", "");
        System.out.println("hashCode:"+hashCode);
        if(hashCode == null || hashCode.equals("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        PayhereDTO payhereDTO = new PayhereDTO();
        UserDataDTO allUserData = userDataService.getAllUserData(paymentDTO.getUsername());
        payhereDTO.setPaymentId(paymentID);
        payhereDTO.setAmount(paymentDTO.getAmount());
        payhereDTO.setHash(hashCode);
        payhereDTO.setEmail(allUserData.getEmail());
        payhereDTO.setUsername(allUserData.getUsername());
        return ResponseEntity.ok(new ApiResponse(200, "OK", payhereDTO));
    }
    public  String generateHash(String merchantId,
                                      String orderId,
                                      double amount,
                                      String currency,
                                      String merchantSecret) throws Exception {

        // Format amount to 2 decimal places (same as PHP number_format)
        String formattedAmount = new BigDecimal(amount)
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString();
        System.out.println("formattedAmount:"+formattedAmount);
        // Inner MD5 (merchantSecret) in uppercase
        String innerMd5 = md5(merchantSecret).toUpperCase();

        // Concatenate all parts
        String rawString = merchantId + orderId + formattedAmount + currency + innerMd5;

        // Outer MD5 in uppercase
        return md5(rawString).toUpperCase();
    }

    // Reusable MD5 helper
    private  String md5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    @PostMapping("/processPayment")
    public ResponseEntity<ApiResponse> processPayment(@RequestBody PaymentDTO paymentDTO) {
        System.out.println("paymentDTO:"+paymentDTO.toString());
        UserDataDTO userDataDTO = paymentService.processPayment(paymentDTO);
        System.out.println("isSavedPayment :"+userDataDTO.getBalance());
        return userDataDTO!=null ? ResponseEntity.ok(new ApiResponse(200, "OK", userDataDTO)) : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
