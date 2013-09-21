Select * From Timestmp 
Where Time = timest;


Select * From ProximityCount 
Where User1 = you;


Select * From Encounter 
Where User1 = you;
--
Select * From EncounterDetails 
Where User1 = you;


Select User From Timestmp
As user2
Where Time = in_time and abs(lat1-lat2)<1 and abs(long1-long2)<1;
-- If you,user doesn't exist
Insert Into ProximityCount
Values (you, user2, count+1);
-- Else if you,user does exist
Update ProximityCount
Set count=count+1
Where (user1=you and user2= user2) or (user2=you and user1= user2))
--
Insert Into Encounter
Values (you, user2, in_time);
--
Insert Into EncounterDetails
Values (you, user2, in_time, sqrt(Square(lat1-lat2) + Square(long1-long2), lat1, long1, lat2, long2);


Update ProximityCount
Set email2=email, phone1=phone_1, phone2=phone_2, description=desc;