Create Database ReEncounter;

Select * From UserInfo;
Select * From Timestmp;
Select * From ProximityCount;
Select * From EncounterDetails;
Select * From Encounter;
Select * From Users;

Drop Table UserInfo;
Drop Table Timestmp;
Drop Table ProximityCount;
Drop Table Blocked;
Drop Table EncounterDetails;
Drop Table Encounter;
Drop Table Users;

CREATE TABLE Users (
	User		VarChar(30) Primary Key,
	Email		VarChar(50) Unique Not Null,
	Password	Char(20) Not Null
);

CREATE TABLE UserInfo (
	User		VarChar(30) Primary Key,
	Email		VarChar(50),
	Email2		VarChar(50),
	Name		VarChar(50),
	Phone		VarChar(25),
	Phone2		VarChar(25),
	Description	VarChar(255),
	FOREIGN KEY (User) REFERENCES Users (User),
	FOREIGN KEY (Email) REFERENCES Users (Email)
);

CREATE TABLE Timestmp (
	User		VarChar(30),
	Time		DateTime,
	Latitude	Decimal(14,8),
	Longitude	Decimal(14,8),
	CONSTRAINT pk_Timestmp PRIMARY KEY (User, Time)
);

CREATE TABLE ProximityCount (
	User1	VarChar(30),
	User2	VarChar(30),
	Times	Integer,
	FOREIGN KEY (User1) REFERENCES Users (User),
	FOREIGN KEY (User2) REFERENCES Users (User),
	CONSTRAINT pk_ProximityCount PRIMARY KEY (User1, User2)
);

CREATE TABLE Encounter (
	User1	VarChar(30),
	User2	VarChar(30),
	Time	DateTime,
	FOREIGN KEY (User1) REFERENCES Users (User),
	FOREIGN KEY (User2) REFERENCES Users (User),
	CONSTRAINT pk_Encounter PRIMARY KEY (User1, User2, Time)
);

CREATE TABLE EncounterDetails (
	User1		VarChar(30),
	User2		VarChar(30),
	Time		DateTime,
	Distance	Decimal(14,8),
	Latitude1	Decimal(14,8),
	Longitude1	Decimal(14,8),
	Latitude2	Decimal(14,8),
	Longitude2	Decimal(14,8),
	FOREIGN KEY (User1) REFERENCES Users (User),
	FOREIGN KEY (User2) REFERENCES Users (User),
	CONSTRAINT pk_EncounterDetails PRIMARY KEY (User1, User2, Time)
);

CREATE TABLE Blocked (
	User1  VarChar(30),
	User2  VarChar(30),
	Block1 Boolean Default 0,
	Block2 Boolean Default 0,
	FOREIGN KEY (User1) REFERENCES Users (User),
	FOREIGN KEY (User2) REFERENCES Users (User),
	CONSTRAINT pk_Blocked PRIMARY KEY (User1, User2)
);