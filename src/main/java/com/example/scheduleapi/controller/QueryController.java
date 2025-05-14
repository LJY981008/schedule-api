package com.example.scheduleapi.controller;

import com.example.scheduleapi.dto.ResponseDto;
import com.example.scheduleapi.service.port.ScheduleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 스케줄 관련 API(SELECT)를 제공하는 컨트롤러
 */
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class QueryController {

    private final ScheduleQueryService scheduleQueryService;

    @GetMapping
    public ResponseEntity<List<ResponseDto>> getSchedulesByUserAndDateRange(@RequestParam Long userId, @RequestParam(required = false, defaultValue = "0001-01-01") String startDate, @RequestParam(required = false) String endDate) {
        if (endDate == null || endDate.isBlank()) endDate = LocalDate.now().toString();
        return ResponseEntity.ok(scheduleQueryService.findSchedulesByUserAndDateRange(userId, startDate, endDate));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ResponseDto> getScheduleById(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleQueryService.findScheduleByScheduleId(scheduleId));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<ResponseDto>> getSchedulesByPage(@RequestParam(defaultValue = "0") Long pageNumber, @RequestParam(defaultValue = "10") Long pageSize) {
        return ResponseEntity.ok(scheduleQueryService.findSchedulesByPage(pageNumber, pageSize));
    }
}
