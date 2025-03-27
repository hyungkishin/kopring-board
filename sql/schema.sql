CREATE DATABASE IF NOT EXISTS myboard
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE myboard;

CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON myboard.* TO 'user'@'%';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS `articles`
(
    `article_id` BIGINT UNSIGNED NOT NULL PRIMARY KEY COMMENT '게시글 id',
    `title`      VARCHAR(100)    NOT NULL COMMENT '게시글 제목',
    `content`    VARCHAR(3000)   NOT NULL COMMENT '게시글 내용',
    `board_id`   BIGINT          NOT NULL COMMENT '',
    `writer_id`  BIGINT          NOT NULL COMMENT '글쓴이 id',
    `created_at` DATETIME        NOT NULL COMMENT '생성 시각',
    `updated_at` DATETIME        NOT NULL COMMENT '최근 수정 시각'
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4 COMMENT = '게시글';

CREATE TABLE IF NOT EXISTS `comments`
(

    `comment_id`        BIGINT UNSIGNED NOT NULL PRIMARY KEY COMMENT '댓글 id',
    `content`           VARCHAR(3000)   NOT NULL COMMENT '상세 내용',
    `article_id`        BIGINT          NOT NULL COMMENT '게시글 id',
    `parent_comment_id` BIGINT          NOT NULL COMMENT '상위 댓글 id',
    `writer_id`         BIGINT          NOT NULL COMMENT '글쓴이 id',
    `deleted`           BOOLEAN         NOT NULL COMMENT '삭제 여부',
    `created_at`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
    `updated_at`        TIMESTAMP       NULL COMMENT '최근 수정 시각'
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4 COMMENT = '댓글';

select *
from comments;

CREATE INDEX IDX_ARTICLE_ID_PARENT_COMMENT_ID_COMMENT_ID
    ON comments (article_id asc, parent_comment_id asc, comment_id asc);

CREATE TABLE IF NOT EXISTS `comments_v2`
(
    `comment_id`        BIGINT UNSIGNED                                       NOT NULL PRIMARY KEY COMMENT '댓글 id',
    `content`           VARCHAR(3000)                                         NOT NULL COMMENT '상세 내용',
    `article_id`        BIGINT                                                NOT NULL COMMENT '게시글 id',
    `parent_comment_id` BIGINT                                                NOT NULL COMMENT '상위 댓글 id',
    `path`              VARCHAR(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '댓글 경로',
    `writer_id`         BIGINT                                                NOT NULL COMMENT '글쓴이 id',
    `deleted`           BOOLEAN                                               NOT NULL COMMENT '삭제 여부',
    `created_at`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
    `updated_at`        TIMESTAMP                                             NULL COMMENT '최근 수정 시각'
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4 COMMENT = '댓글';

CREATE UNIQUE INDEX IDX_ARTICLE_ID_PATH ON comments_v2 (article_id asc, path asc)