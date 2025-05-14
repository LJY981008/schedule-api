USE schedules;

CREATE TABLE post(
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '식별자',
    publisher VARCHAR(100) NOT NULL COMMENT '작성자',
    password VARCHAR(100) NOT NULL COMMENT '비밀번호',
    title VARCHAR(100) NOT NULL COMMENT '제목',
    contents TEXT COMMENT '일정',
    updated_date DATE NOT NULL COMMENT '최종 수정일',
    user_id BIGINT COMMENT '유저 id'
);

CREATE TABLE user(
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '유저 식별자',
    email VARCHAR(100) NOT NULL COMMENT '이메일',
    registration_date DATE NOT NULL COMMENT '가입일',
    modification_date DATE NOT NULL COMMENT '수정일',
    name VARCHAR(100) NOT NULL COMMENT '이름'
);

ALTER TABLE post
    ADD CONSTRAINT fk_user_id
        FOREIGN KEY (user_id) REFERENCES user(user_id);
