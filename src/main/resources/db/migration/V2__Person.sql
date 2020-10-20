create table Person (
    Id bigint(20) not null AUTO_INCREMENT,
    IsEmployed bit not null default 0,

    PRIMARY KEY (Id)
);
