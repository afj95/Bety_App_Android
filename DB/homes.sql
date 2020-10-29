

SELECT * FROM bety.homes;

UPDATE bety.users SET homeId_FK = null, homeAdminId_FK = null WHERE ID = "1";

DELETE FROM bety.homes ;

DELETE FROM bety.homes where Id = "5994ec54-1332-4ead-9f14-337dd6a47e7c";

UPDATE bety.homes SET adminEmail = "hhh@hhh.com" WHERE homeName = "hh";

insert into bety.homes values("1","name1", 0, 0);
