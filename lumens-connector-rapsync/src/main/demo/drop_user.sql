CREATE OR REPLACE PROCEDURE drop_user(user_name V$SESSION.USERNAME%TYPE) is 
  dropper varchar2(100);
BEGIN 
  dropper := 'drop user '||user_name||' CASCADE';
  EXECUTE IMMEDIATE dropper;
END;

CREATE OR REPLACE PROCEDURE kill_session(user_name V$SESSION.USERNAME%TYPE) is 
  id  V$SESSION.SID%TYPE;
  serial V$SESSION.SERIAL#%TYPE;
  killer varchar2(100);  
BEGIN 
  SELECT SID, SERIAL# into id, serial FROM V$SESSION WHERE USERNAME=user_name;
  killer:='ALTER SYSTEM KILL SESSION '''||id||','||serial||'''';
  EXECUTE IMMEDIATE killer;
END;

execute kill_session('LUMENS');
execute drop_User('LUMENS');
