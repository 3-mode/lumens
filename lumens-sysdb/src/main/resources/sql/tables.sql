CREATE TABLE LUNS_PROJECT (
    "ID" BIGINT NOT NULL,
    "NAME" VARCHAR(512) NOT NULL,
    "DESCRIPTION" VARCHAR(1024),
    "DATA" LONG VARCHAR,
    "LAST_MODIF_TIME" TIMESTAMP,
    PRIMARY KEY (ID)
);
CREATE TABLE LUNS_JOB (
    "ID" BIGINT NOT NULL,
    "NAME" VARCHAR(512) NOT NULL,
    "DESCRIPTION" VARCHAR(1024),
    "REPEAT_COUNT" INTEGER,
    "INTERVAL" INTEGER,
    "START_TIME" TIMESTAMP,
    "END_TIME" TIMESTAMP,
    PRIMARY KEY (ID)
);
CREATE TABLE LUNS_RELATION (
    "JOB_ID" BIGINT NOT NULL,
    "PROJECT_ID" BIGINT NOT NULL,
    PRIMARY KEY (JOB_ID,PROJECT_ID)
);
CREATE TABLE LUNS_INOUTLOG (
    "LOG_ID" BIGINT NOT NULL,
    "COMPONENT_ID" BIGINT NOT NULL,
    "COMPONENT_NAME" VARCHAR(512) NOT NULL,
    "TARGET_NAME" VARCHAR(512),
    "DIRECTION" VARCHAR(5) NOT NULL,
    "LAST_MODIF_TIME" TIMESTAMP,
    "DATA" LONG VARCHAR,
    "PROJECT_ID" BIGINT NOT NULL,
    "PROJECT_NAME" VARCHAR(512) NOT NULL,
     PRIMARY KEY (LOG_ID)
);

/* Insert a demo project for CSV connector */
INSERT INTO LUMENS.LUNS_PROJECT (ID, "NAME", DESCRIPTION, "DATA") 
	VALUES (1421324074892, 'Test CSV', '', '{"project":{"name":"Test CSV","description":"","datasource":[{"id":1421324030231,"type":"type-text","name":"Test CSV","description":"","target":[],"position":{"x":471,"y":144},"property":[{"name":"EscapeChar","type":"String","value":"\\\\"},{"name":"FiledDelimiter","type":"String","value":","},{"name":"SchemaPath","type":"String","value":"X:\\PRODUCT\\3MODE\\lumens\\lumens-connector-text\\src\\test\\resources\\delimited\\incsv_schema.xml"},{"name":"Path","type":"String","value":"c:\\testcsv.txt"},{"name":"Encoding","type":"String","value":"UTF-8"},{"name":"LineDelimiter","type":"String","value":""},{"name":"AlwaysQuoteMode","type":"Boolean","value":"false"}],"format_list":[{"direction":"IN","format_entry":[{"name":"write test csv","format":{"form":"Struct","name":"TextMessage","type":"None","property":[{"name":"SchemaEncoding","type":"String","value":"UTF-8"}],"format":[{"form":"Struct","name":"TextParams","type":"None","format":[{"form":"Field","name":"Operation","type":"String"}]},{"form":"Field","name":"number","type":"Integer","property":[{"name":"precision","type":"Integer","value":"-1"},{"name":"length","type":"Integer","value":"16"},{"name":"nullable","type":"String","value":"false"},{"name":"key","type":"String","value":"true"}]},{"form":"Field","name":"text","type":"String","property":[{"name":"length","type":"Integer","value":"255"},{"name":"nullable","type":"String","value":"true"},{"name":"key","type":"String","value":"false"}]},{"form":"Field","name":"date","type":"Date","property":[{"name":"pattern","type":"String","value":"dd-MM-yyyy"},{"name":"length","type":"Integer","value":"16"},{"name":"nullable","type":"String","value":"false"},{"name":"key","type":"String","value":"false"}]}]},"direction":"IN"}]}]}],"transformer":[{"id":1421324114404,"type":"type-transformer","name":"Drive test write a csv","description":"","target":[{"id":1421324030231}],"position":{"x":58,"y":52},"transform_rule_entry":[{"name":"write_test_csv_script","target_id":1421324030231,"source_format_name":"","target_format_name":"write test csv","transform_rule":{"name":"TextMessage","transform_rule_item":{"format_name":"TextMessage","transform_rule_item":[{"format_name":"TextParams","transform_rule_item":[{"format_name":"Operation","script":"\"Append\""}]},{"format_name":"number","script":"21212"},{"format_name":"text","script":"\"hello csv\""},{"format_name":"date","script":"return new Timestamp(System.currentTimeMillis())"}]}},"$$hashKey":"00L"}],"property":[]}],"start_entry":[]}}');
