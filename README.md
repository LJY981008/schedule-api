# 스케줄 관리 API 명세서

스케줄 관리 시스템의 API 엔드포인트 및 요청/응답 스키마 명세서

**기본 URL:** `/schedules`

## 1. 스케줄 생성 (`POST /schedules`)

* **요청 방식:** `POST`
* **Content-Type:** `application/json`
* **요청 바디:**

```json
{
  "password": "string",
  "userId": 0,
  "email": "string@example.com",
  "contents": "string (최대 200자)",
  "publisher": "string",
  "title": "string"
}
```
* **요청 바디 파라미터:**

|파라미터|타입|필수|설명|
|---------|--------|---|--------------------|
|password|string|Yes|스케줄의 비밀번호|
|userId|integer|Yes|스케줄을 작성하는 사용자의 아이디|
|email|string||스케줄 작성자의 이메일|
|contents|string||스케줄의 내용(최대 200자)|
|publisher|string||스케줄의 작성자|
|title|string||스케줄의 제목|

응답 상태 코드: 201 Created

* **응답 바디:**
```json

{
  "scheduleId": 0,
  "publisher": "string",
  "password": "string",
  "title": "string",
  "contents": "string",
  "updatedDate": "yyyy-MM-dd"
}
```
* **응답 바디 파라미터:**

|파라미터|타입|설명|
|---------|--------|--------------------|
|scheduleId|integer|스케줄의 고유 식별자|
|publisher|string|스케줄의 게시자|
|password|string|스케줄의 비밀번호|
|title|string|스케줄의 제목|
|contents|string|스케줄의 내용|
|updatedDate|string|스케줄의 최종 수정일 (yyyy-MM-dd)|

* **예외 응답:** </br>
400 Bad Request: 유효성 검사 실패 시
```json

{
  "fieldName": "error message",
  "anotherFieldName": "another error message"
}
```
## 2. 사용자별/날짜별 스케줄 조회 (GET /schedules)
* **요청 방식:** GET

* **쿼리 파라미터:**

|파라미터|타입|필수|기본값|설명|
|---------|--------|---|---|--------------------|
|userId|integer|Yes||조회할 사용자의 ID|
|startDate|string||0001-01-01|조회 시작 날짜 (yyyy-MM-dd)|
|endDate|string||현재 날짜|조회 종료 날짜 (yyyy-MM-dd)|

응답 상태 코드: 200 OK

* **응답 바디:** 배열 형태의 ScheduleResponseDto 리스트 (스케줄 생성 응답 바디 참조)

## 3. 특정 스케줄 ID 조회 (GET /schedules/{scheduleId})
* **요청 방식:** GET
* **경로 변수:** </br>
scheduleId (필수, integer): 조회할 스케줄의 ID

응답 상태 코드: 200 OK

* **응답 바디:** ScheduleResponseDto (스케줄 생성 응답 바디 파라미터 참조)

* **예외 응답:**
404 Not Found: 해당 scheduleId의 스케줄이 없을 경우
```json

{
  "status": "NOT_FOUND",
  "message": "schedule not found"
}
```
## 4. 페이지별 스케줄 조회 (GET /schedules/posts)
* **요청 방식:** GET

* **쿼리 파라미터:**

|파라미터|타입|기본값|설명|
|---------|--------|---|--------------------|
|pageNumber|integer|0|조회할 페이지 번호|
|pageSize|integer|10|한 페이지당 보여줄 개수|

응답 상태 코드: 200 OK

* **응답 바디:** 배열 형태의 ScheduleResponseDto 리스트 (스케줄 생성 응답 바디 파라미터 참조)

## 5. 특정 스케줄 ID 수정 (PUT /schedules/{scheduleId})
* **요청 방식:** PUT
* **Content-Type:** application/json
* **경로 변수:** </br>
scheduleId (필수, integer): 수정할 스케줄의 ID

* **요청 바디:**
```json

{
  "password": "string",
  "contents": "updated string",
  "publisher": "updated publisher",
  "title": "updated title"
}
```

* **요청 바디 파라미터:**

|파라미터|타입|필수|설명|
|---------|--------|---|--------------------|
|password|string|Yes|스케줄 수정 권한 확인 비밀번호|
|contents|string||수정할 스케줄 내용 (최대 200자)|
|publisher|string||수정할 스케줄 게시자|
|title|string||수정할 스케줄 제목|

응답 상태 코드: 200 OK

* **응답 바디:** string - "업데이트 성공"

* **예외 응답:** </br>
400 Bad Request: 유효성 검사 실패 시 (스케줄 생성 응답 바디 파라미터 참조)</br>
400 Bad Request: 비밀번호 불일치 시
```json

{
  "status": "BAD_REQUEST",
  "message": "password mismatch"
}
```
* `404 Not Found`: 해당 `scheduleId`의 스케줄이 없을 경우 (특정 스케줄 ID 조회 예외 응답 참조)
## 6. 특정 스케줄 ID 삭제 (DELETE /schedules/{scheduleId})
* **요청 방식:** DELETE
* **경로 변수:**
scheduleId (필수, integer): 삭제할 스케줄의 ID
* **쿼리 파라미터:**
password (필수, string): 스케줄 삭제 권한 확인 비밀번호

응답 상태 코드: 200 OK
* **응답 바디:** 없음 (200 No Content)

* **예외 응답:**
400 Bad Request: 비밀번호 불일치 시
```json

{
  "status": "BAD_REQUEST",
  "message": "password mismatch"
}
```
* `404 Not Found`: 해당 `scheduleId`의 스케줄이 없을 경우 (특정 스케줄 ID 조회 예외 응답 참조)
* **참고**
모든 요청 및 응답은 기본적으로 JSON 형식

날짜 형식은 yyyy-MM-dd

필수 파라미터는 명시되어 있으며, 선택 파라미터는 필요에 따라 포함가능

예외 응답의 경우, status 필드는 HTTP 상태 코드의 영문 명칭을, message 필드는 오류에 대한 상세 설명을 포함

* **UserService 관련 API**

회원 가입 (내부 로직): 스케줄 생성 요청 시, 제공된 사용자 정보(userId)가 존재하지 않으면 새로운 사용자를 등록, 별도의 회원 가입 API 엔드포인트는 없음

## 트러블 슈팅

[SQL에 컬럼을 동적으로 반영하면 생기는 문제](https://t-era.tistory.com/223)

[@Valid를 사용해 요청 검수하는 방법](https://t-era.tistory.com/220)
