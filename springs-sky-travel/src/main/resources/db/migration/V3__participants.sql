create table participants
(
    full_name      varchar(255),
    age            integer not null,
    reservation_id bigint  not null
);

alter table participants
    add constraint FK_PARTICIPANTS_ON_RESERVATION foreign key (reservation_id) references reservations (id);