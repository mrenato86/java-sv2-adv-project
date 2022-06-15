CREATE TABLE IF NOT EXISTS `reservations`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT,
    `contact_person`    varchar(255) DEFAULT NULL,
    `requested_service` varchar(255) DEFAULT NULL,
    `full_price`        int(11)      DEFAULT NULL,
    `journey_id`        bigint(20)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY FK_RESERVATIONS_ON_JOURNEY (`journey_id`),
    CONSTRAINT FK_RESERVATIONS_ON_JOURNEY FOREIGN KEY (`journey_id`) REFERENCES `journeys` (`id`)
);