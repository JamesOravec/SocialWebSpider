# Reference: http://lists.mysql.com/java/6017
--drop table Blobs;
create table Blobs (
 blobId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 member_name char(20),
 picture_filename char(50),
 picture mediumblob,
 picture_caption blob);
