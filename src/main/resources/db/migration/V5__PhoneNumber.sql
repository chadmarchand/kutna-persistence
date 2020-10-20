create table PhoneNumber (
    Id bigint(20) not null AUTO_INCREMENT,
    PersonId bigint(20) not null,
    TypeId bigint(20) not null,
    Number nvarchar(64) not null,

    PRIMARY KEY (Id),
    UNIQUE KEY uk_PhoneNumber_PersonId_TypeId_Number (PersonId, TypeId, Number),
    CONSTRAINT fk_PhoneNumber_PersonId FOREIGN KEY (PersonId) REFERENCES Person (Id),
    CONSTRAINT fk_PhoneNumber_TypeId FOREIGN KEY (TypeId) REFERENCES PhoneNumberType (Id)
);
