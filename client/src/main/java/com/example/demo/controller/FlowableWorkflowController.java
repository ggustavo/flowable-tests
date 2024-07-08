package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@RestController
public class FlowableWorkflowController {

    public static final MediaType JSON = MediaType.get("application/json");
    OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(Duration.ofSeconds(24 * 60 * 60)).build();
    
    @Value("${api}")
    String api;

    @PostMapping("/start/all")
    public ResponseEntity<?> start() {
        
        Request request = new Request.Builder()
                .post(RequestBody.create("", JSON))
                .url(api + "/start/all")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return ResponseEntity.ok().body(response.body().string());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/deploy")
    public ResponseEntity<?> ok() {
        
        Request request = new Request.Builder()
                .post(RequestBody.create("", JSON))
                .url(api + "/deploy")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return ResponseEntity.ok().body(response.body().string());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        
        Request request = new Request.Builder()
                .url(api + "/all")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return ResponseEntity.ok().body(response.body().string());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
