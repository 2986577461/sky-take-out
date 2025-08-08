create table if not exists user
(
    id          bigint auto_increment primary key,
    user_name   varchar(30) unique not null,
    nick_name   varchar(30),
    password    varchar(80),
    phonenumber varchar(11),
    sex         varchar(1),
    email       varchar(20) unique not null
);

insert into user (user_name, nick_name, password, phonenumber, sex, email)
values ('123456', '超超', '$2a$10$8OVqXgG4u2RNTSFD0NB2KOBRn8ZAv3EX4nxFQfmJUcqRLRsAoeVYO', '18189735419', '男',
        '2986577461@qqc.com');
insert into user (user_name, nick_name, password, phonenumber, sex, email)
values ('1', '吴jc', '$2a$10$8OVqXgG4u2RNTSFD0NB2KOBRn8ZAv3EX4nxFQfmJUcqRLRsAoeVYO', '18534523534', '男',
        '1284823@qq.com');

create table if not exists donation_project
(
    id               bigint auto_increment primary key,
    name             varchar(50)   not null,
    type             varchar(20)   not null,
    issuer           varchar(20)   not null,
    issuer_id        bigint        not null,
    current_fund     int           not null,
    number_of_people int           not null,
    book_money       int           not null,
    teach_tool_money int           not null,
    live_money       int           not null,
    medical_money    int           not null,
    project_content  varchar(5000) not null,
    create_date_time datetime      not null,
    deadline         datetime      not null,
    status           tinyint(1)    not null default 1,
    fund_target      int as (book_money + teach_tool_money + live_money + medical_money) stored
);

create table if not exists personal_donation
(
    id               bigint auto_increment primary key,
    project_id       bigint      not null,
    donator          varchar(20) not null,
    donator_id       bigint      not null,
    type             tinyint(1)  not null,
    amount           int         not null,
    create_date_time datetime    not null
);

desc donation_project;
