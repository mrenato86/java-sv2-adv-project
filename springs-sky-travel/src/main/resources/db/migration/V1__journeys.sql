create table journeys
(
    id                    bigint not null auto_increment,
    destination           varchar(255),
    journey_description   varchar(255),
    journey_method        varchar(255),
    departure_date        date,
    number_of_nights      integer,
    price_per_participant integer,
    primary key (id)
);