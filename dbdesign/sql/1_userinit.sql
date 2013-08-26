conn system/q;
create user hrcms identified by q;
grant connect to hrcms;

conn / as sysdba;
grant create table to hrcms;
grant resource to hrcms;

conn hrcms/q;