#DB 생성
DROP DATABASE IF EXISTS SB_AM;
CREATE DATABASE SB_AM;
USE SB_AM;

#게시물 테이블 생성
CREATE TABLE article(
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    title CHAR(100) NOT NULL,
    `body` CHAR(100) NOT NULL
);

# 게시물 테스트 데이터 생성
INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목1',
`body` = '내용1';

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목2',
`body` = '내용2';

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목3',
`body` = '내용3';

#회원 테이블 생성
CREATE TABLE `member`(
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(20) NOT NULL,
    loginPw CHAR(60) NOT NULL,
    `authLevel` SMALLINT(2) UNSIGNED DEFAULT 3 COMMENT '권한 레벨 (3=일반, 7=관리자)',
    `name` CHAR(20) NOT NULL,
    nickname CHAR(20) NOT NULL,
    cellphoneNum CHAR(20) NOT NULL,
    email CHAR(50) NOT NULL,
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '탈퇴여부 (0=탈퇴 전,1=탈퇴 후)',
    delDate DATETIME COMMENT '탈퇴날짜'
);

# 회원 테스트 데이터 생성 (관리자)
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'admin',
loginPw = 'admin',
`authLevel` = 7,
`name` = '관리자',
nickname = '관리자',
cellphoneNum = '01012345678',
email = 'kimpk0416@gmail.com';

# 회원 테스트 데이터 생성 (일반)
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'test1',
loginPw = 'test1',
`authLevel` = 3,
`name` = '사용자1',
nickname = '사용자1',
cellphoneNum = '01098765432',
email = 'kimpk0416@gmail.com';

# 회원 테스트 데이터 생성 (일반)
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'test2',
loginPw = 'test2',
`authLevel` = 3,
`name` = '사용자2',
nickname = '사용자2',
cellphoneNum = '01012341234',
email = 'kimpk0416@gmail.com';

SELECT * FROM article;

SELECT * FROM `member`;

SELECT LAST_INSERT_ID();