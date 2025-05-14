package com.example.scheduleapi.controller;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.dto.ScheduleResponseDto;
import com.example.scheduleapi.exceptions.custom.ValidationException;
import com.example.scheduleapi.service.port.ScheduleCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 스케줄 관련 API를 제공하는 컨트롤러
 */
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class CommandController {

    private final ScheduleCommandService scheduleCommandService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @Valid @RequestBody ScheduleRequestDto dto, BindingResult bindingResult) {
        handleValidationErrors(bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleCommandService.createSchedule(dto));
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<String> updateSchedule(@Valid @RequestBody ScheduleRequestDto requestDto, BindingResult bindingResult, @PathVariable Long scheduleId) {
        handleValidationErrors(bindingResult);
        scheduleCommandService.updateScheduleById(requestDto, scheduleId);
        return ResponseEntity.ok("업데이트 성공");
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteScheduleById(
            @PathVariable Long scheduleId,
            @RequestParam String password) {
        scheduleCommandService.deleteScheduleById(scheduleId, password);
        return ResponseEntity.ok().build();
    }

    private void handleValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ValidationException(errors, "유효성 검사 실패");
        }
    }
}