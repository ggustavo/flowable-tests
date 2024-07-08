package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.FlowableWorkflowService;

@RestController
public class FlowableWorkflowController {
    @Autowired
    private FlowableWorkflowService service;

    @PostMapping("/start/all")
    public ResponseEntity<?> start() {
        var result = service.startAllFlows();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(service.allTasks());
    }

    @PostMapping("/deploy")
    public ResponseEntity<?> deploy() {
        var result = service.startAllFlows();
        return ResponseEntity.ok(result);
    }
}
