package com.example.scheduleapi.lv2.controller;

import com.example.scheduleapi.lv1.dto.ScheduleRequestDto;
import com.example.scheduleapi.lv1.dto.ScheduleResponseDto;
import com.example.scheduleapi.lv1.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/lv2")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto) {
        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<ScheduleResponseDto>> readScheduleByPublisherAndDate(
            @RequestParam String publisher,
            @RequestParam(defaultValue = "0001-01-01") String startDate,
            @RequestParam(required = false) String endDate
    ) {
        if (endDate == null || endDate.isBlank()) endDate = LocalDate.now().toString();
        return ResponseEntity.ok(scheduleService.filterSchedulesByPublisherAndDate(publisher, startDate, endDate));
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<ScheduleResponseDto> readScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.findScheduleById(id));
    }
}
