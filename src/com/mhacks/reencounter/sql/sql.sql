CREATE TABLE Users (
	User		VarChar(30) Primary Key,
	Email		VarChar(50) Unique Not Null,
	Password	Char(20) Not Null
);

CREATE TABLE UserInfo (
	User		VarChar(30) Primary Key,
	Name		VarChar(50),
	Email2		VarChar(50),
	Phone1		INTEGER,
	Phone2		INTEGER,
	Description	VarChar(255),
	FOREIGN KEY (User) REFERENCES Users (User)
);

CREATE TABLE Timestmp (
	User		VarChar(30) Primary Key,
	Time		Timestamp,
	Latitude	Decimal,
	Longitude	Decimal
);

CREATE TABLE ProximityCount (
	User1	VarChar(30),
	User2	VarChar(30),
	Count	Integer,
	FOREIGN KEY (User1, User2) REFERENCES Users (User, User),
	CONSTRAINT pk_ProximityCount PRIMARY KEY (User1, User2)
);

CREATE TABLE Encounter (
	User1	VarChar(30),
	User2	VarChar(30),
	Time	Timestamp,
	FOREIGN KEY (User1, User2) REFERENCES Users (User, User),
	FOREIGN KEY (Time) REFERENCES Timestmp (Time),
	CONSTRAINT pk_Encounter PRIMARY KEY (User1, User2, Time)
);

CREATE TABLE EncounterDetails (
	User1		VarChar(30),
	User2		VarChar(30),
	Time		Timestamp,
	Distance	Decimal,
	Latitude1	Decimal,
	Longitude1	Decimal,
	Latitude2	Decimal,
	Longitude2	Decimal,
	FOREIGN KEY (User1, User2) REFERENCES Users (User, User),
	FOREIGN KEY (Time) REFERENCES Timestmp (Time),
	CONSTRAINT pk_EncounterDetails PRIMARY KEY (User1, User2, Time)
);
