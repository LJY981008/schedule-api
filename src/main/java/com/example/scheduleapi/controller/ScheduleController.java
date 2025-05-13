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

/**
 * 스케줄 관련 API를 제공하는 컨트롤러
 */
@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final UserService userService;

    public ScheduleController(ScheduleService scheduleService, UserService userService) {
        this.scheduleService = scheduleService;
        this.userService = userService;
    }

    /**
     * 새로운 스케줄을 생성하고 등록
     * 요청 데이터의 유효성을 검사하고, 새로운 사용자인 경우 등록
     *
     * @param dto           생성할 스케줄 정보를 담은 DTO
     * @param bindingResult 요청 데이터의 유효성 검사 결과
     * @return 생성된 스케줄 (201 Created)
     * @throws ValidationException 요청 데이터 유효성 검사 실패 시 발생
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto dto, BindingResult bindingResult) {
        handleValidationErrors(bindingResult);
        userService.registerUserIfNew(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(dto));
    }

    /**
     * 특정 사용자의 스케줄 목록을 지정된 날짜 범위 내에서 조회
     * 종료 날짜가 제공되지 않으면 현재 날짜까지의 스케줄을 조회
     *
     * @param userId    조회할 사용자의 ID
     * @param startDate 조회 시작 날짜 (yyyy-MM-dd 형식, 기본값: 0001-01-01)
     * @param endDate   조회 종료 날짜 (yyyy-MM-dd 형식, 선택 사항, 기본값: 현재 날짜)
     * @return 조회된 스케줄 목록 (200 OK)
     */
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getSchedulesByUserAndDateRange(@RequestParam Long userId, @RequestParam(defaultValue = "0001-01-01") String startDate, @RequestParam(required = false) String endDate) {
        if (endDate == null || endDate.isBlank()) endDate = LocalDate.now().toString();
        return ResponseEntity.ok(scheduleService.findSchedulesByUserAndDateRange(userId, startDate, endDate));
    }

    /**
     * 특정 ID의 스케줄 정보를 조회
     *
     * @param scheduleId 조회할 스케줄의 ID
     * @return 조회된 스케줄 (200 OK)
     */
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getScheduleById(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findScheduleByUserId(scheduleId));
    }

    /**
     * 스케줄 목록을 페이지별로 조회
     *
     * @param pageNumber 조회할 페이지 번호 (기본값: 0)
     * @param pageSize   한 페이지당 보여줄 스케줄 개수 (기본값: 10)
     * @return 조회된 스케줄 목록 (200 OK)
     */
    @GetMapping("/posts")
    public ResponseEntity<List<ScheduleResponseDto>> getSchedulesByPage(@RequestParam(defaultValue = "0") Long pageNumber, @RequestParam(defaultValue = "10") Long pageSize) {
        return ResponseEntity.ok(scheduleService.findSchedulesByPage(pageNumber, pageSize));
    }

    /**
     * 특정 ID의 스케줄 정보를 수정
     * 요청 데이터의 유효성을 검사
     *
     * @param requestDto    수정할 스케줄 정보를 담은 DTO
     * @param scheduleId    수정할 스케줄의 ID
     * @param bindingResult 요청 데이터의 유효성 검사 결과
     * @return 업데이트 성공 시 메시지 (200 OK)
     * @throws ValidationException 요청 데이터 유효성 검사 실패 시 발생
     */
    @PutMapping("/{scheduleId}")
    public ResponseEntity<String> updateSchedule(@Valid @RequestBody ScheduleRequestDto requestDto, @PathVariable Long scheduleId, BindingResult bindingResult) {
        handleValidationErrors(bindingResult);
        scheduleService.updateScheduleById(requestDto, scheduleId);
        return ResponseEntity.ok("업데이트 성공");
    }

    /**
     * 특정 ID의 스케줄 정보를 삭제
     * 삭제를 위해 비밀번호를 확인 (현재는 비밀번호 확인 로직 없음)
     *
     * @param password   삭제 요청 시 필요한 비밀번호
     * @param scheduleId 삭제할 스케줄의 ID
     * @return 삭제 성공 시 (200 OK)
     */
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteScheduleById(
            @PathVariable Long scheduleId,
            @RequestParam String password) {
        scheduleService.deleteScheduleById(scheduleId, password);
        return ResponseEntity.ok().build();
    }

    /**
     * 요청 데이터의 유효성 검사 결과를 처리하고, 에러가 있을 경우 ValidationException을 실행
     *
     * @param bindingResult 유효성 검사 결과
     * @throws ValidationException 유효성 검사 실패 시 발생
     */
    private void handleValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ValidationException(errors, "유효성 검사 실패");
        }
    }

}