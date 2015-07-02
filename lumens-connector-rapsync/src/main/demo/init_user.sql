create user LUMENS identified by lumens;
grant connect, sysdba,resource to LUMENS;
GRANT SELECT ON V_$DATABASE TO LUMENS;
