package com.mate.helgather.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@PropertySource(value = "application.yml")
public class ServerController {

    @Value("${serverName}")
    private String serverName;
    private Integer visitedCount = 0;

    @GetMapping("/getServerInfo")
    public ResponseEntity<Map<String, String>> getServerInfo(){
        visitedCount++;

        Map<String, String> serverInfo = new HashMap<>();
        serverInfo.put("ServerName:", serverName);
        serverInfo.put("visitedCount:", visitedCount.toString());

        return ResponseEntity.ok(serverInfo);
    }
}
