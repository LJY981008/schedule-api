package com.example.scheduleapi.controller;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.dto.ScheduleResponseDto;
import com.example.scheduleapi.service.port.ScheduleCommandService;
import com.example.scheduleapi.util.RequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 스케줄 관련 API(UPDATE)를 제공하는 컨트롤러
 */
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class CommandController {

    private final RequestValidator requestValidator;
    private final ScheduleCommandService scheduleCommandService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @Valid @RequestBody ScheduleRequestDto dto, BindingResult bindingResult) {
        requestValidator.requestValidateErrorToMap(bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleCommandService.createSchedule(dto));
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<String> updateSchedule(@Valid @RequestBody ScheduleRequestDto requestDto, BindingResult bindingResult, @PathVariable Long scheduleId) {
        requestValidator.requestValidateErrorToMap(bindingResult);
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


}