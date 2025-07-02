package com.example.formworkflow.controller;

import com.example.formworkflow.dto.FormSubmissionRequest;
import com.example.formworkflow.dto.FormSubmissionResponse;
import com.example.formworkflow.service.FormSubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/forms/data")
@RequiredArgsConstructor
public class FormSubmissionController {

    private final FormSubmissionService formSubmissionService;

//    @PostMapping("/_submit")
//    public ResponseEntity<?> submit(@RequestBody FormSubmissionRequest request) {
//        formSubmissionService.submitForm(request);
//        return ResponseEntity.ok(Map.of("ResponseInfo", Map.of("status", "form submitted")));
//    }

    @PostMapping("/_submit")
    public ResponseEntity<FormSubmissionResponse> submitForm(@Valid @RequestBody FormSubmissionRequest request) {
        return ResponseEntity.ok(formSubmissionService.submitForm(request));
    }

}