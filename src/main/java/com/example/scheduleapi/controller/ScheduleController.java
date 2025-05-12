package com.example.scheduleapi.controller;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.dto.ScheduleResponseDto;
import com.example.scheduleapi.exceptions.ValidationException;
import com.example.scheduleapi.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto) {
        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> readScheduleByPublisherAndDate(
            @RequestParam String publisher,
            @RequestParam(defaultValue = "0001-01-01") String startDate,
            @RequestParam(required = false) String endDate
    ) {
        if (endDate == null || endDate.isBlank()) endDate = LocalDate.now().toString();
        return ResponseEntity.ok(scheduleService.filterSchedulesByPublisherAndDate(publisher, startDate, endDate));
    }

    @GetMapping("/{id}")
        public ResponseEntity<ScheduleResponseDto> readScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.findScheduleById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSchedule(
            @Valid @RequestBody ScheduleRequestDto requestDto,
            @PathVariable Long id,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            throw new ValidationException(errors, "유효성 검사 실패");
        }
        scheduleService.updateSchedule(requestDto, id);

        return new ResponseEntity<>("업데이트 성공", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
