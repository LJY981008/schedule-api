package com.example.scheduleapi.controller;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.dto.ScheduleResponseDto;
import com.example.scheduleapi.exceptions.ValidationException;
import com.example.scheduleapi.service.ScheduleService;
import com.example.scheduleapi.service.UserService;
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
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final UserService userService;

    public ScheduleController(ScheduleService scheduleService, UserService userService) {
        this.scheduleService = scheduleService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto dto, BindingResult bindingResult) {
        handleValidationErrors(bindingResult);
        userService.registerUserIfNew(dto);
        return new ResponseEntity<>(scheduleService.createSchedule(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> readScheduleByPublisherAndDate(@RequestParam Long user_id, @RequestParam(defaultValue = "0001-01-01") String startDate, @RequestParam(required = false) String endDate) {
        if (endDate == null || endDate.isBlank()) endDate = LocalDate.now().toString();
        return ResponseEntity.ok(scheduleService.findSchedulesByUserAndDateRange(user_id, startDate, endDate));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> readScheduleById(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findScheduleByUserId(scheduleId));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<ScheduleResponseDto>> readScheduleByPage(@RequestParam(defaultValue = "0") Long pageNumber, @RequestParam(defaultValue = "10") Long pageSize) {
        return ResponseEntity.ok(scheduleService.findSchedulesByPage(pageNumber, pageSize));
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<String> updateSchedule(@Valid @RequestBody ScheduleRequestDto requestDto, @PathVariable Long scheduleId, BindingResult bindingResult) {
        handleValidationErrors(bindingResult);
        scheduleService.updateScheduleById(requestDto, scheduleId);
        return new ResponseEntity<>("업데이트 성공", HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteScheduleById(@RequestParam String password, @PathVariable Long scheduleId) {
        scheduleService.deleteScheduleById(scheduleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void handleValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ValidationException(errors, "유효성 검사 실패");
        }
    }

}
