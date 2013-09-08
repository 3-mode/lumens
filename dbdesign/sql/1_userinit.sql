/*==============================================================*/
/* 说明                                                         */
/* sqlplus: @c:\1_userinit.sql                                  */
/* sqlplus: start c:\1_userinit.sql                             */
/*==============================================================*/

conn system/q;
create user hrcms identified by hrcms;
grant connect to hrcms;

conn / as sysdba;
grant create table to hrcms;
grant resource to hrcms;

conn hrcms/q;