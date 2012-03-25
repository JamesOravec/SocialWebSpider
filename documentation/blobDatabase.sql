# Reference: http://lists.mysql.com/java/6017
#drop table Blobs;
create table Blobs
(member_name char(20) not null,
 picture_filename char(50),
 picture mediumblob,
 picture_caption blob,
 audio_filename char(50),
 audio mediumblob,
 audio_caption blob,
 primary key(member_name));
 
 