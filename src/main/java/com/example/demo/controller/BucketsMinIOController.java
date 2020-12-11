package com.example.demo.controller;

import com.example.demo.model.DiretoriosMinIO;
import com.example.demo.service.BucketMinIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buckets")
public class BucketsMinIOController {

    @Autowired
    private BucketMinIOService bucketMinIOService;

    @GetMapping("/all")
    public ResponseEntity<List<DiretoriosMinIO>> findAllBuckets() {
        return ResponseEntity.ok(bucketMinIOService.getAllBuckets());
    }

    @PostMapping("/create")
    public Map<String, String> createBucket(@RequestParam(value = "nameBucket") String nameBucket) {
        String message = bucketMinIOService.create(nameBucket);
        Map<String, String> map = new HashMap<>();
        map.put("message", message != null ? message : "Ocorreu um erro inesperado!");
        return map;
    }

    @DeleteMapping("/delete")
    public Map<String, String> deleteBucket(@RequestParam(value = "nameBucket") String nameBucket) {
        String message = bucketMinIOService.delete(nameBucket);
        Map<String, String> map = new HashMap<>();
        map.put("message", message != null ? message : "Ocorreu um erro inesperado!");
        return map;
    }
}
