--check archive mode
select name, log_mode from v$database;
SELECT * FROM sys.v$instance;
select * from session_privs;
SELECT supplemental_log_data_min FROM sys.v_$database;
select t.COLUMN_NAME, t.DATA_TYPE, t.DATA_LENGTH from user_tab_columns t where t.TABLE_NAME = 'V_$LOGMNR_CONTENTS';
SELECT directory_path FROM sys.dba_directories WHERE DIRECTORY_NAME='UTL_FILE_DIR';