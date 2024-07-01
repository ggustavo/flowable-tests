package com.example.demo.controller;


import com.example.demo.service.FlowableWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class FlowableWorkflowController {
    @Autowired
    private FlowableWorkflowService service;
    
    @PostMapping("/start/all")
    public ResponseEntity<?> start(){
    	var result = service.startAllFlows();
    	return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(service.allTasks());
    }
    
    @PostMapping("/deploy")
    public ResponseEntity<?> deploy(){
    	var result = service.startAllFlows();
    	return ResponseEntity.ok(result);
    }
    
    /*

    @PostMapping("/bigboy/{id}")
    public void bigboyWithId(@PathVariable(name = "id") String id) throws IOException {
        System.out.println("Start bigboy test with id: " + id);
        service.startBigBoy(id);
    }

    
    @PostMapping("/bigboy")
    public void bigboyWithoutId() throws IOException {
        System.out.println("Start bigboy test without id");
        service.startBigBoy(null);

    }
    */
}
