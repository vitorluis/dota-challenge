create sequence dota_combat_log_sequence start 1 increment 1;
create sequence dota_match_sequence start 1 increment 1;

create table dota_combat_log
(
    id              int8 not null,
    ability         text,
    ability_level   int4,
    actor           text,
    damage          int4,
    item            text,
    target          text,
    entry_timestamp int8 not null,
    entry_type      text not null,
    match_id        int8 not null,
    primary key (id)
);
create table dota_match
(
    id int8 not null,
    primary key (id)
);

alter table dota_combat_log
    add constraint FKseontgnel0gwsb1taexjnvjbe foreign key (match_id) references dota_match;
