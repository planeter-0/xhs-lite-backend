CREATE DATABASE IF NOT EXISTS xhs_lite DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE xhs_lite;

DROP TABLE IF EXISTS user_follow;
DROP TABLE IF EXISTS notes;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname VARCHAR(64) NOT NULL,
    avatar_url VARCHAR(255) DEFAULT NULL,
    bio VARCHAR(512) DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_users_nickname (nickname)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_follow (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    follower_id BIGINT NOT NULL,
    followee_id BIGINT NOT NULL,
    status TINYINT NOT NULL DEFAULT 1 COMMENT '1-following,0-unfollowed',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_follower_followee UNIQUE (follower_id, followee_id),
    CONSTRAINT chk_follow_self CHECK (follower_id <> followee_id),
    CONSTRAINT fk_follow_follower FOREIGN KEY (follower_id) REFERENCES users (id),
    CONSTRAINT fk_follow_followee FOREIGN KEY (followee_id) REFERENCES users (id),
    INDEX idx_follow_follower_status (follower_id, status),
    INDEX idx_follow_followee_status (followee_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE notes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(120) NOT NULL,
    content TEXT NOT NULL,
    tags VARCHAR(255) DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_notes_user FOREIGN KEY (user_id) REFERENCES users (id),
    INDEX idx_notes_user_id (user_id),
    INDEX idx_notes_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO users (id, nickname, avatar_url, bio) VALUES
(1, 'dane_dev', 'https://img.example.com/1.png', 'Java backend learner'),
(2, 'amy_java', 'https://img.example.com/2.png', 'Spring Boot engineer'),
(3, 'tim_data', 'https://img.example.com/3.png', 'Data + backend hybrid'),
(4, 'sara_api', 'https://img.example.com/4.png', 'API design enthusiast');

INSERT INTO user_follow (follower_id, followee_id, status) VALUES
(1, 2, 1),
(1, 3, 1),
(2, 1, 1),
(4, 1, 0);

INSERT INTO notes (user_id, title, content, tags) VALUES
(2, 'Spring Boot 异常处理实践', '通过 ControllerAdvice 和统一返回体，可以快速提升 API 稳定性和可维护性。', 'springboot,backend,error'),
(3, 'MySQL 复合索引避坑', '关注关系场景中，follower_id + status 的复合索引比单列索引更稳定。', 'mysql,index,backend'),
(1, '后端实习项目准备清单', '作品集项目最好包含数据库建模、接口文档、测试覆盖和可运行说明。', 'intern,project,java'),
(4, '接口分页设计笔记', '统一分页参数 page/size，并返回 totalElements 和 totalPages，前端联调更顺畅。', 'api,pagination');
