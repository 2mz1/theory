CREATE TABLE todo (
  `id`      INT(10) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT 'ID',
  `title`   VARCHAR(188) NOT NULL                    COMMENT '제목',
  `content` TEXT                                     COMMENT '내용',
  `status`  ENUM('TODO', 'PROGRESS', 'DONE')         COMMENT '상태 값',
  `created` DATETIME NOT NULL                        COMMENT '생성일시',
  `updated` DATETIME NOT NULL                        COMMENT '생성일시',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
