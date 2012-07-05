# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table contact (
  id                        bigint not null,
  name                      varchar(255),
  title                     varchar(255),
  email                     varchar(255),
  constraint pk_contact primary key (id))
;

create sequence contact_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists contact;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists contact_seq;

