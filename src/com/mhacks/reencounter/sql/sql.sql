Create Database ReEncounter;

Select * From UserInfo;
Select * From Timestmp;
Select * From ProximityCount;
Select * From Blocked;
Select * From EncounterDetails;
Select * From Encounter;
Select * From Contacts;
Select * From Message;
Select * From MessageCheck;
Select * From Users;

Drop Table UserInfo;
Drop Table Timestmp;
Drop Table ProximityCount;
Drop Table Blocked;
Drop Table EncounterDetails;
Drop Table Encounter;
Drop Table Contacts;
Drop Table Message;
Drop Table MessageCheck;
Drop Table Users;

CREATE TABLE Users (
    User     VarChar(30) Primary Key Not Null,
    Email    VarChar(50) Unique Not Null,
    Password Char(20)    Not Null
);

CREATE TABLE UserInfo (
    User        VarChar(30)  Primary Key Not Null,
    Email       VarChar(50)  Not Null,
    Email2      VarChar(50)  Not Null,
    Name        VarChar(50)  Not Null,
    Phone       VarChar(25)  Not Null,
    Phone2      VarChar(25)  Not Null,
	Message     Boolean      Default 0 Not Null,
    Description VarChar(255) Not Null,
    FOREIGN KEY (User) REFERENCES Users (User),
    FOREIGN KEY (Email) REFERENCES Users (Email)
);

CREATE TABLE Timestmp (
    User      VarChar(30)   Not Null,
    Time      DateTime      Not Null,
    Latitude  Decimal(10,7) Not Null,
    Longitude Decimal(10,7) Not Null,
    CONSTRAINT pk_Timestmp PRIMARY KEY (User, Time)
);

CREATE TABLE ProximityCount (
    User1 VarChar(30) Not Null,
    User2 VarChar(30) Not Null,
    Times Integer     Not Null,
    FOREIGN KEY (User1) REFERENCES Users (User),
    FOREIGN KEY (User2) REFERENCES Users (User),
    CONSTRAINT pk_ProximityCount PRIMARY KEY (User1, User2)
);

CREATE TABLE Encounter (
    User1 VarChar(30) Not Null,
    User2 VarChar(30) Not Null,
    Time  DateTime    Not Null,
    FOREIGN KEY (User1) REFERENCES Users (User),
    FOREIGN KEY (User2) REFERENCES Users (User),
    CONSTRAINT pk_Encounter PRIMARY KEY (User1, User2, Time)
);

CREATE TABLE EncounterDetails (
    User1      VarChar(30)   Not Null,
    User2      VarChar(30)   Not Null,
    Time       DateTime      Not Null,
    Distance   Decimal(10,7) Not Null,
    Latitude1  Decimal(10,7) Not Null,
    Longitude1 Decimal(10,7) Not Null,
    Latitude2  Decimal(10,7) Not Null,
    Longitude2 Decimal(10,7) Not Null,
    FOREIGN KEY (User1) REFERENCES Users (User),
    FOREIGN KEY (User2) REFERENCES Users (User),
    CONSTRAINT pk_EncounterDetails PRIMARY KEY (User1, User2, Time)
);

CREATE TABLE Blocked (
    User1  VarChar(30) Not Null,
    User2  VarChar(30) Not Null,
    Block1 Boolean     Default 0 Not Null,
    Block2 Boolean     Default 0 Not Null,
    FOREIGN KEY (User1) REFERENCES Users (User),
    FOREIGN KEY (User2) REFERENCES Users (User),
    CONSTRAINT pk_Blocked PRIMARY KEY (User1, User2)
);

CREATE TABLE Contacts (
    User    VarChar(30)  Not Null,
    Contact VarChar(30)  Not Null,
    FOREIGN KEY (User) REFERENCES Users (User),
    FOREIGN KEY (Contact) REFERENCES Users (User),
    CONSTRAINT pk_Contacts PRIMARY KEY (User, Contact)
);

CREATE TABLE Message (
    Sender    VarChar(30)  Not Null,
    Recipient VarChar(30)  Not Null,
    Time      DateTime     Not Null,
    Message   VarChar(255) Not Null,
    FOREIGN KEY (Sender) REFERENCES Users (User),
    FOREIGN KEY (Recipient) REFERENCES Users (User),
    CONSTRAINT pk_Message PRIMARY KEY (Sender, Recipient, Time)
);

CREATE TABLE MessageCheck (
    User1      VarChar(30)  Not Null,
    User2      VarChar(30)  Not Null,
    LastCheck1 DateTime     Not Null,
    LastCheck2 DateTime     Not Null,
    LastNew1   DateTime     Not Null,
    LastNew2   DateTime     Not Null,
    FOREIGN KEY (User1) REFERENCES Users (User),
    FOREIGN KEY (User2) REFERENCES Users (User),
    CONSTRAINT pk_MessageCheck PRIMARY KEY (User1, User2)
);