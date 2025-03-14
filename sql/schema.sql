CREATE DATABASE IF NOT EXISTS myboard
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE myboard;

CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON myboard.* TO 'user'@'%';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS `articles`
(
    `article_id` BIGINT        NOT NULL PRIMARY KEY COMMENT '게시글 id',
    `title`      VARCHAR(100)  NOT NULL COMMENT '게시글 제목',
    `content`    VARCHAR(3000) NOT NULL COMMENT '게시글 내용',
    `board_id`   BIGINT        NOT NULL COMMENT '',
    `writer_id`  BIGINT        NOT NULL COMMENT '글쓴이 id',
    `created_at` DATETIME      NOT NULL COMMENT '생성 시각',
    `updated_at` DATETIME      NOT NULL COMMENT '최근 수정 시각'
    ) ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4 COMMENT = '게시글';