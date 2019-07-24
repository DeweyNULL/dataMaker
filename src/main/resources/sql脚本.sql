-- auto-generated definition
create table DATA_PROJECT
(
  ID           int auto_increment
  comment '主键id'
    primary key,
  PORJECT_NAME varchar(200) not null
  comment '项目名字',
  DATASOURCE   varchar(200) not null
  comment '数据库url',
  USERNAME     varchar(50)  not null
  comment '数据库登录用户名',
  PASSWORD     varchar(50)  not null
  comment '数据库密码',
  TYPE         varchar(20)  not null
  comment '数据库类型'
);

