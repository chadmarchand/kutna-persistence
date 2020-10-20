create table Address (
    Id bigint(20) not null AUTO_INCREMENT,
    PersonId bigint(20) not null,
    Address nvarchar(64) not null,

    PRIMARY KEY (Id),
    UNIQUE KEY uk_Address_PersonId (PersonId),
    CONSTRAINT fk_Address_PersonId FOREIGN KEY (PersonId) REFERENCES Person (Id)
);
