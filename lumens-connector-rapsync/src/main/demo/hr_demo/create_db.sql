create or replace PROCEDURE drop_hr_demo_db
is  
  current_name user_tables.table_name%type;
  cursor cursor_name is
      select table_name from user_tables;  
BEGIN
  open cursor_name;
  loop
    fetch cursor_name into current_name;
    dbms_output.put_line(current_name);
  
  end loop;
  close cursor_name;  
END;
  
execute drop_hr_demo_db();