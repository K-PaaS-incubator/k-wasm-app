CREATE TABLE `FILE`
(
    `FILE_SEQ`         bigint(20) NOT NULL AUTO_INCREMENT,
    `EXT`              varchar(5) COLLATE utf8_unicode_ci   NOT NULL,
    `SERVER_FILE_NAME` varchar(30) COLLATE utf8_unicode_ci  NOT NULL,
    `SERVER_FILE_URL`  varchar(50) COLLATE utf8_unicode_ci  NOT NULL,
    `ORG_FILE_NAME`    varchar(100) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`FILE_SEQ`)
)