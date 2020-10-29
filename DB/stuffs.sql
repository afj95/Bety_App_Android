

select * from bety.stuffs;

DELETE FROM bety.stuffs;

select * from bety.stuffs where userEmail="uuu@uuu.com";

insert into bety.stuffs value(1,"dsd","dsds","uuu@uuu.com");

update bety.stuffs set homeId = "009b8127-ba86-40ff-8aa9-e4fa8d7d28cc" where stuffName = "gg";

update bety.stuffs set stuffQuantity = null;

update bety.stuffs set deleted = false, checked = false where Id = "f282a537-f15a-484d-8846-d0221744be63" and homeId = "96806aae-1894-4e2e-abc9-ff0b0dfd2c94";
update bety.stuffs set deleted = false, checked = false;
