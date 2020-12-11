package com.example.demo.controller;

import com.example.demo.model.ArquivosMinIO;
import com.example.demo.model.DiretoriosMinIO;
import com.example.demo.service.BucketMinIOService;
import com.example.demo.service.ObjectMinIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/objects")
public class ObjectsMinIOController {

    @Autowired
    private ObjectMinIOService objectMinIOService;

    @GetMapping("/all")
    public List<ArquivosMinIO> findAllObjectsByBuckets(@RequestParam(value = "nameBucket") String nameBucket) {
        return objectMinIOService.listObjects(nameBucket);
    }

    @PostMapping("/upload")
    public Map<String, String> uploadFile(@RequestPart(value = "file") MultipartFile files,
                                          @RequestParam(value = "nameBucket") String nameBucket) {
        objectMinIOService.uploadFile(files, nameBucket);
        Map<String, String> result = new HashMap<>();
        result.put("key", files.getOriginalFilename());
        return result;
    }

    @GetMapping("/download")
    public ResponseEntity<?> download(@RequestParam(value = "nameFile") String nameFile,
                                      @RequestParam(value = "nameBucket") String nameBucket) throws IOException {
        byte[] data = objectMinIOService.downloadFile(nameFile, nameBucket);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + nameFile + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete")
    public Map<String, String> deleteObject(@RequestParam(value = "nameBucket") String nameBucket,
                                            @RequestParam(value = "nameObject") String nameObject) {
        String message = objectMinIOService.delete(nameBucket, nameObject);
        Map<String, String> map = new HashMap<>();
        map.put("message", message != null ? message : "Ocorreu um erro inesperado!");
        return map;
    }
}
