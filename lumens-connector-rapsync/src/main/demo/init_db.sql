--change database to arvhive mode
shutdown immediate;
shutdown normal;
start mount
alter database archivelog;  --alter database noarchivelog;
alter database open;  --or archive log list