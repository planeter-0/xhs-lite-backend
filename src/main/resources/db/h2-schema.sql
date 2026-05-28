DROP TABLE IF EXISTS user_follow;
DROP TABLE IF EXISTS notes;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(64) NOT NULL,
    avatar_url VARCHAR(255),
    bio VARCHAR(512),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_nickname ON users (nickname);

CREATE TABLE user_follow (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    follower_id BIGINT NOT NULL,
    followee_id BIGINT NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_follower_followee UNIQUE (follower_id, followee_id),
    CONSTRAINT chk_follow_self CHECK (follower_id <> followee_id)
);

CREATE INDEX idx_follow_follower_status ON user_follow (follower_id, status);
CREATE INDEX idx_follow_followee_status ON user_follow (followee_id, status);

CREATE TABLE notes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(120) NOT NULL,
    content VARCHAR(2048) NOT NULL,
    tags VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_notes_user_id ON notes (user_id);
CREATE INDEX idx_notes_created_at ON notes (created_at);
