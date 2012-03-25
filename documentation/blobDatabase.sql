# Base Reference: http://lists.mysql.com/java/6017
# Modifications by: James Oravec
--drop table Blobs;
create table Blobs (
 blobId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 userId INT NOT NULL,
 blobFileName varchar(50),
 blobBinary blob,
 blobCaption varchar(100));
