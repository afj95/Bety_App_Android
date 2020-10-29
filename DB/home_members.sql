

SELECT * FROM bety.home_members;

insert into bety.home_members values(null,"038f63a4-abdc-4049-a523-b1e936cf8cd3", "home", "iii@iii.com");

delete from bety.home_members;

delete from bety.home_members where Id = 2;

update home_members set Id = 1 where Id = 46;

ALTER TABLE home_members AUTO_INCREMENT = 1;