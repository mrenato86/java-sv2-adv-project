create table reservations
(
    id                bigint not null auto_increment,
    contact_person    varchar(255),
    requested_service varchar(255),
    full_price        integer,
    journey_id        bigint,
    primary key (id)
);

alter table reservations
    add constraint FK_RESERVATIONS_ON_JOURNEY foreign key (journey_id) references journeys (id);