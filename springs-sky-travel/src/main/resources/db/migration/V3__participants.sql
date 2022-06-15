CREATE TABLE IF NOT EXISTS `participants`
(
    `name`           varchar(255) DEFAULT NULL,
    `age`            int(11)    NOT NULL,
    `reservation_id` bigint(20) NOT NULL,
    KEY FK_PARTICIPANTS_ON_RESERVATION (`reservation_id`),
    CONSTRAINT FK_PARTICIPANTS_ON_RESERVATION FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`id`)
);