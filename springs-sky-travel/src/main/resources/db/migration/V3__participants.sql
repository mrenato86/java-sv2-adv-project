create table participants
(
    reservation_id bigint  not null,
    full_name      varchar(255),
    age            integer not null
);

alter table participants
    add constraint FK_PARTICIPANTS_ON_RESERVATION foreign key (reservation_id) references reservations (id);