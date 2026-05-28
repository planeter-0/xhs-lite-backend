INSERT INTO users (id, nickname, avatar_url, bio, created_at) VALUES
(1, 'dane_dev', 'https://img.example.com/1.png', 'Java backend learner', CURRENT_TIMESTAMP),
(2, 'amy_java', 'https://img.example.com/2.png', 'Spring Boot engineer', CURRENT_TIMESTAMP),
(3, 'tim_data', 'https://img.example.com/3.png', 'Data + backend hybrid', CURRENT_TIMESTAMP),
(4, 'sara_api', 'https://img.example.com/4.png', 'API design enthusiast', CURRENT_TIMESTAMP);

INSERT INTO user_follow (id, follower_id, followee_id, status, created_at, updated_at) VALUES
(1, 1, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 2, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 4, 1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO notes (id, user_id, title, content, tags, created_at, updated_at) VALUES
(1, 2, 'Spring Boot 异常处理实践', '通过 ControllerAdvice 和统一返回体，可以快速提升 API 稳定性和可维护性。', 'springboot,backend,error', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 3, 'MySQL 复合索引避坑', '关注关系场景中，follower_id + status 的复合索引比单列索引更稳定。', 'mysql,index,backend', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 1, '后端实习项目准备清单', '作品集项目最好包含数据库建模、接口文档、测试覆盖和可运行说明。', 'intern,project,java', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 4, '接口分页设计笔记', '统一分页参数 page/size，并返回 totalElements 和 totalPages，前端联调更顺畅。', 'api,pagination', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
