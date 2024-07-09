package com.example.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
public class FlowableWorkflowController {

    public static final MediaType JSON = MediaType.get("application/json");
    public static final MediaType FILE = MediaType.get("application/zip");
    OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(Duration.ofSeconds(24 * 60 * 60)).build();

    @Value("${api}")
    String api;

    @PostMapping("/start/all")
    public ResponseEntity<?> start() {

        Request request = new Request.Builder()
                .post(okhttp3.RequestBody.create("", JSON))
                .url(api + "/start/all")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return ResponseEntity.ok().body(response.body().string());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/start/names")
    public ResponseEntity<?> startByNames(@RequestBody List<String> names) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(names);

            Request request = new Request.Builder()
                    .post(okhttp3.RequestBody.create(json, JSON))
                    .url(api + "/start/names")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return ResponseEntity.ok().body(response.body().string());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(null);
            }

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/deploy")
    public ResponseEntity<?> deploy(@RequestBody List<String> names) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(names);

            Request request = new Request.Builder()
                    .post(okhttp3.RequestBody.create(json, JSON))
                    .url(api + "/deploy")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return ResponseEntity.ok().body(response.body().string());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(null);
            }

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/sendFlows")
    public ResponseEntity<?> sendFlows(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }

        // Convert MultipartFile to File
        File convertedFile = convertMultiPartToFile(file);

        // Create RequestBody instance from file
        okhttp3.RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", convertedFile.getName(),
                        okhttp3.RequestBody.create(convertedFile, FILE))
                .build();

        Request request = new Request.Builder()
                .post(requestBody)
                .url(api + "/sendFlows")
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

    private File convertMultiPartToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try {

            FileOutputStream fos = new FileOutputStream(convertedFile);

            fos.write(file.getBytes());
        } catch (Exception e) {
            return null;

        }
        return convertedFile;
    }

}
