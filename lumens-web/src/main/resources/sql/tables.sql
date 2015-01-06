CREATE TABLE LUNS_PROJECT (
    "ID" BIGINT NOT NULL,
    "NAME" VARCHAR(512) NOT NULL,
    "DESCRIPTION" VARCHAR(1024),
    "DATA" LONG VARCHAR,
    "LAST_MODIF_TIME" TIMESTAMP,
    PRIMARY KEY (ID)
);
CREATE TABLE LUNS_JOB (
    "JOB_ID" BIGINT NOT NULL,
    "NAME" VARCHAR(512) NOT NULL,
    "DESCRIPTION" VARCHAR(1024),
    "REPEAT_MODE" INTEGER,
    "START_TIME" TIMESTAMP,
    "END_TIME" TIMESTAMP,
    PRIMARY KEY (JOB_ID)
);
CREATE TABLE LUNS_INOUTLOG (
    "COMPNENT_ID" BIGINT NOT NULL,
    "DIRECTION" VARCHAR(5) NOT NULL,
    "TARGET_NAME" VARCHAR(512) NOT NULL,
    "DATA" LONG VARCHAR,
    "LAST_MODIF_TIME" TIMESTAMP
     PRIMARY KEY (ID)
);

