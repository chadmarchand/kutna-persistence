create table Name (
    Id bigint(20) not null AUTO_INCREMENT,
    PersonId bigint(20) not null,
    GivenName nvarchar(64) not null,
    FamilyName nvarchar(64) not null,

    PRIMARY KEY (Id),
    UNIQUE KEY uk_Name_PersonId (PersonId),
    CONSTRAINT fk_Name_PersonId FOREIGN KEY (PersonId) REFERENCES Person (Id)
);
