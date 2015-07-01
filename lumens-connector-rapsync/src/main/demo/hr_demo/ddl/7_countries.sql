--------------------------------------------------------
--  DDL for Table COUNTRIES
--------------------------------------------------------
  CREATE TABLE "LUMENS"."COUNTRIES" 
   (	"COUNTRY_ID" CHAR(2 BYTE), 
	"COUNTRY_NAME" VARCHAR2(40 BYTE), 
	"REGION_ID" NUMBER, 
	 CONSTRAINT "COUNTRY_C_ID_PK" PRIMARY KEY ("COUNTRY_ID") ENABLE
   ) TABLESPACE "USERS";

   COMMENT ON COLUMN "LUMENS"."COUNTRIES"."COUNTRY_ID" IS 'Primary key of countries table';
 
   COMMENT ON COLUMN "LUMENS"."COUNTRIES"."COUNTRY_NAME" IS 'Country name';
 
   COMMENT ON COLUMN "LUMENS"."COUNTRIES"."REGION_ID" IS 'Region ID for the country. Foreign key to region_id column in the departments table.';
 
   COMMENT ON TABLE "LUMENS"."COUNTRIES"  IS 'country table. Contains 25 rows. References with locations table.';
 
  ALTER TABLE "LUMENS"."COUNTRIES" MODIFY ("COUNTRY_ID" CONSTRAINT "COUNTRY_ID_NN" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table COUNTRIES
--------------------------------------------------------

  ALTER TABLE "LUMENS"."COUNTRIES" ADD CONSTRAINT "COUNTR_REG_FK" FOREIGN KEY ("REGION_ID")
	  REFERENCES "LUMENS"."REGIONS" ("REGION_ID") ENABLE;
