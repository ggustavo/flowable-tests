package com.example.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.FlowableWorkflowService;

@RestController
public class FlowableWorkflowController {
    @Autowired
    private FlowableWorkflowService service;

    String dest = "./src/main/resources";

    @PostMapping("/start/all")
    public ResponseEntity<?> start() {
        var result = service.startAllFlows();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/start/names")
    public ResponseEntity<?> startByNames(@RequestBody List<String> names) {
        var result = service.startByNames(names);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(service.allTasks());
    }

    @PostMapping("/deploy")
    public ResponseEntity<?> deploy(@RequestBody List<String> names) {
        int deployed = 0;

        try {
            deployed = service.deployByNames(names);
        } catch (Exception e) {
            // TODO: handle exception
        }

        // var result = service.startAllFlows();
        return ResponseEntity.ok(deployed);
    }

    @PostMapping("/sendFlows")
    public ResponseEntity<?> send(@RequestParam("file") MultipartFile file) {
        List<String> arrayNames = new ArrayList<>();

        try {
            arrayNames = unzip(file.getInputStream(), dest);
        } catch (Exception e) {
            // TODO: handle exception
        }

        // var result = service.startAllFlows();
        return ResponseEntity.ok(arrayNames.size());
    }

    private List<String> unzip(InputStream zipFilePath, String destDir) throws IOException {
        List<String> arrayNames = new ArrayList();

        File destDirectory = new File(destDir);
        if (!destDirectory.exists()) {
            destDirectory.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(zipFilePath);
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            String filePath = destDir + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                arrayNames.add(entry.getName());
                extractFile(zipIn, filePath);
            } else {
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();

        return arrayNames;
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
