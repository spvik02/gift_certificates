create table if not exists tag
(
    id   bigserial primary key,
    name varchar(255) not null unique
);

create table if not exists gift_certificate
(
    id               bigserial primary key,
    name             varchar(255)   not null unique,
    description      text,
    price            numeric(10, 2) not null,
    duration         int,
    create_date      TIMESTAMP,
    last_update_date TIMESTAMP
);

create table if not exists tag_in_certificate
(
    certificate_id bigint not null,
    tag_id         bigint not null,
    PRIMARY KEY (certificate_id, tag_id)
)