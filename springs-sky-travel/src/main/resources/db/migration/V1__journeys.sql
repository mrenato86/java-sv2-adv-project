CREATE TABLE IF NOT EXISTS `journeys`
(
    `id`                    bigint(20) NOT NULL AUTO_INCREMENT,
    `destination`           varchar(255) DEFAULT NULL,
    `description`           varchar(255) DEFAULT NULL,
    `departure_date`        date         DEFAULT NULL,
    `method`                varchar(255) DEFAULT NULL,
    `number_of_nights`      int(11)      DEFAULT NULL,
    `price_per_participant` int(11)      DEFAULT NULL,
    PRIMARY KEY (`id`)
);