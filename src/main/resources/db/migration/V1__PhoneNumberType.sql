create table PhoneNumberType (
    Id bigint(20) not null AUTO_INCREMENT,
    Name nvarchar(64) not null,

    PRIMARY KEY (Id),
    UNIQUE KEY uk_PhoneNumberType_Name (Name)
);

insert into PhoneNumberType (Name) values
    ('Home'),
    ('Work'),
    ('Mobile');
