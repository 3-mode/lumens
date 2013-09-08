/* Entity table */
DROP TABLE 个人概况 CASCADE CONSTRAINTS;
DROP TABLE 人员性质 CASCADE CONSTRAINTS;
DROP TABLE 专业技术职称 CASCADE CONSTRAINTS;
DROP TABLE 来校信息 CASCADE CONSTRAINTS;
DROP TABLE 任职单位 CASCADE CONSTRAINTS;
DROP TABLE 政治面貌 CASCADE CONSTRAINTS;
DROP TABLE 学历学位 CASCADE CONSTRAINTS;
/* Dict table */
DROP TABLE DICT_职称 CASCADE CONSTRAINTS;
DROP TABLE DICT_党政职务 CASCADE CONSTRAINTS;
DROP TABLE DICT_人员类别 CASCADE CONSTRAINTS;
DROP TABLE DICT_编制类别 CASCADE CONSTRAINTS;
DROP TABLE DICT_来源类别 CASCADE CONSTRAINTS;
DROP TABLE DICT_家庭出身 CASCADE CONSTRAINTS;
DROP TABLE DICT_婚姻状况 CASCADE CONSTRAINTS;
DROP TABLE DICT_港澳台侨外 CASCADE CONSTRAINTS;
DROP TABLE DICT_在岗状态 CASCADE CONSTRAINTS;
DROP TABLE DICT_所有制性质 CASCADE CONSTRAINTS;
DROP TABLE DICT_教职工类别 CASCADE CONSTRAINTS;
DROP TABLE DICT_编制类别 CASCADE CONSTRAINTS;
DROP TABLE DICT_人员统计分类 CASCADE CONSTRAINTS;
DROP TABLE DICT_薪酬来源 CASCADE CONSTRAINTS;
DROP TABLE DICT_岗位 CASCADE CONSTRAINTS;
DROP TABLE DICT_身份类别 CASCADE CONSTRAINTS;
DROP TABLE DICT_专业 CASCADE CONSTRAINTS;
DROP TABLE DICT_国家 CASCADE CONSTRAINTS;
DROP TABLE DICT_健康状况 CASCADE CONSTRAINTS;
DROP TABLE DICT_学位 CASCADE CONSTRAINTS;
DROP TABLE DICT_学历 CASCADE CONSTRAINTS;
DROP TABLE DICT_学科 CASCADE CONSTRAINTS;
DROP TABLE DICT_所在单位 CASCADE CONSTRAINTS;
DROP TABLE DICT_籍贯 CASCADE CONSTRAINTS;
DROP TABLE DICT_职称 CASCADE CONSTRAINTS;
/* ====================================================================== */
/* 外链字段如下                                                              */
/* 所在单位, 籍贯，科室名称,政治面貌, 最后学历,最高学位,最后学历毕业学校,最后学历毕业年月*/
/* 现职称,现专业技术职务级别,专业技术职务资格评定日期,聘任职称, 聘任职称等级,现职级日期  */
/* 从事专业,主要岗位,人员状态,党政职务,党政职务级别,身份类别                        */
/* ======================================================================= */
CREATE TABLE 个人概况
  (
    职工号    VARCHAR2(10) NOT NULL,
    姓名     VARCHAR2(60),
    性别     VARCHAR2(10),
    出生日期   VARCHAR2(8),
    身份证号   VARCHAR2(20),
    民族     VARCHAR2(100) 来校年月 DATE,
    参加工作年月 VARCHAR2(6),
    健康状况   VARCHAR2(50),
    外侨情况   VARCHAR2(50),
    婚姻状况   VARCHAR2(255),
    来源类别   VARCHAR2(2),
    编制类别   VARCHAR2(100),
    人员类别   VARCHAR2(20) 国家 VARCHAR2(255),
    学习形式   VARCHAR2(50),
    全局编号   VARCHAR2(64),
    最后修改时间 VARCHAR2(25),
    姓名拼音   VARCHAR2(240),
    曾用名    VARCHAR2(60),
    出生地    VARCHAR2(200),
    照片 BLOB,
    血型       VARCHAR2(10),
    家庭出身     VARCHAR2(50),
    本人成分     VARCHAR2(50),
    户口所在地    VARCHAR2(240),
    转干时间     DATE,
    聘干时间     DATE,
    首次任博导年月  DATE,
    首次任硕导年月  DATE,
    档案号      VARCHAR2(200),
    内部编号     VARCHAR2(64),
    保留字段     VARCHAR2(300),
    人事关系转入时间 DATE,
    备注       VARCHAR2(500),
  );
/*==============================================================*/
/* Table: 学历学位                                                  */
/*==============================================================*/
CREATE TABLE 学历学位
  (
    全局编号     VARCHAR2(64),
    职工号      VARCHAR2(10) NOT NULL,
    最后修改时间   VARCHAR2(25),
    学历       VARCHAR2(100) NOT NULL,
    所学专业     VARCHAR2(120),
    入学年月     DATE,
    学习形式     SMALLINT,
    学习方式     SMALLINT,
    学制       SMALLINT,
    毕业院校     VARCHAR2(255),
    毕业年月     DATE,
    毕肄业学校或单位 VARCHAR2(360),
    学位       VARCHAR2(100) NOT NULL,
    学位授予日期   DATE,
    学位授予国家   SMALLINT,
    学位授予单位   VARCHAR2(360),
    备注       VARCHAR2(500),
    CONSTRAINT 学历学位 PRIMARY KEY (职工号, 学历, 学位)
  );
/*==============================================================*/
/* Table: 人员性质                                                  */
/*==============================================================*/
CREATE TABLE 人员性质
  (
    岗位代码     VARCHAR2(50),
    岗位名称     VARCHAR2(100),
    教职工代码    VARCHAR2(50),
    教职工类别    VARCHAR2(100),
    人员统计分类代码 VARCHAR2(50),
    人员统计分类名称 VARCHAR2(100),
    所有制性质代码  VARCHAR2(50),
    所有制性质名称  VARCHAR2(100),
    编制代码     VARCHAR2(50),
    薪酬来源代码   VARCHAR2(50),
    薪酬来源名称   VARCHAR2(100),
    在岗状态代码   VARCHAR2(50),
    在岗状态名称   VARCHAR2(100),
    全局编号     VARCHAR2(64),
    职工号      VARCHAR2(10),
    最后修改时间   VARCHAR2(25),
    现记录      NUMBER(1,0),
    调整时间     DATE,
    编制类别     VARCHAR2(100),
    岗位       VARCHAR2(100),
    人员类别     INTEGER,
    人员状态     INTEGER,
    薪酬来源     INTEGER,
    所有制性质    INTEGER,
    统计分类     INTEGER,
    备注       VARCHAR2(500),
    身份类别     VARCHAR2(50)
  );
CREATE TABLE 专业技术职称
  (
    职称       VARCHAR2(255),
    职称等级     SMALLINT,
    全局编号     VARCHAR2(64),
    职工号      VARCHAR2(10),
    最后修改时间   VARCHAR2(25),
    资格评定日期   VARCHAR2(14),
    专业技术职务级别 VARCHAR2(50),
    任职资格名称   VARCHAR2(255),
    取得资格途径   VARCHAR2(100),
    评审单位     VARCHAR2(120),
    取得资格日期   DATE,
    聘任职称等级   VARCHAR2(2),
    聘任职称     VARCHAR2(3),
    聘任单位     VARCHAR2(120),
    聘任日期     DATE,
    聘任情况     VARCHAR2(255),
    解聘日期     DATE,
    解聘原因     VARCHAR2(160),
    备注       VARCHAR2(500),
    CONSTRAINT 专业技术职称 UNIQUE (职工号, 职称)
  );
/*==============================================================*/
/* Table: DICT_人员类别代码                                       */
/*==============================================================*/
CREATE TABLE DICT_人员类别
  (
    人员类别代码 VARCHAR2(50),
    人员类别名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: 任职单位                                                  */
/*==============================================================*/
CREATE TABLE 任职单位
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10) NOT NULL,
    最后修改时间 VARCHAR2(25),
    人事关系所在单位 NUMBER(1, 0),
    调动时间 DATE,
    单位   VARCHAR2(255) NOT NULL,
    岗位   VARCHAR2(100),
    科室   VARCHAR2(100),
    学科   VARCHAR2(255),
    专业   VARCHAR2(255),
    科室名称 VARCHAR2(120),
    备注   VARCHAR2(500),
    从事学科 SMALLINT,
    从事专业 VARCHAR2(6),
    主要岗位 SMALLINT,
    兼职岗位 SMALLINT,
    CONSTRAINT 任职单位 PRIMARY KEY (职工号, 单位)
  );
/*==============================================================*/
/* Table: DICT_党政职务                                             */
/*==============================================================*/
CREATE TABLE DICT_党政职务
  (
    党政职务   VARCHAR2(100) NOT NULL,
    党政职务级别 SMALLINT NOT NULL,
    职工号    VARCHAR2(10),
    CONSTRAINT DICT_党政职务 PRIMARY KEY (党政职务, 党政职务级别)
  );
/*==============================================================*/
/* Table: DICT_本人成分代码                                            */
/*==============================================================*/
CREATE TABLE DICT_本人成分代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_学习形式代码                                               */
/*==============================================================*/
CREATE TABLE DICT_学习形式代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_职称                                               */
/*==============================================================*/
CREATE TABLE DICT_职称
  (
    职称   VARCHAR2(255) NOT NULL,
    职称等级 SMALLINT NOT NULL,
    CONSTRAINT DICT_职称 PRIMARY KEY (职称, 职称等级)
  );
/*==============================================================*/
/* Table: DICT_出国目的代码                                          */
/*==============================================================*/
CREATE TABLE DICT_出国目的代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_人员类别                                          */
/*==============================================================*/
CREATE TABLE DICT_人员类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_编制类别代码                                           */
/*==============================================================*/
CREATE TABLE DICT_编制类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_来源类别代码                                           */
/*==============================================================*/
CREATE TABLE DICT_来源类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_家庭出身代码                                           */
/*==============================================================*/
CREATE TABLE DICT_家庭出身代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_婚姻状况代码                                           */
/*==============================================================*/
CREATE TABLE DICT_婚姻状况代码
  (
    婚姻状况 VARCHAR2(255) NOT NULL,
    CONSTRAINT DICT_婚姻状况代码 PRIMARY KEY (婚姻状况)
  );
/*==============================================================*/
/* Table: DICT_港澳台侨外代码                                          */
/*==============================================================*/
CREATE TABLE DICT_港澳台侨外代码
  (
    外侨代码 VARCHAR2(50) NOT NULL,
    CONSTRAINT DICT_港澳台侨外代码 PRIMARY KEY (外侨代码)
  );
/*==============================================================*/
/* Table: DICT_在岗状态                                             */
/*==============================================================*/
CREATE TABLE DICT_在岗状态
  (
    代码 VARCHAR2(50) NOT NULL,
    名称 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_在岗状态 PRIMARY KEY (在岗状态代码, 在岗状态名称)
  );
/*==============================================================*/
/* Table: DICT_所有制性质代码                                          */
/*==============================================================*/
CREATE TABLE DICT_所有制性质
  (
    代码 VARCHAR2(50) NOT NULL,
    名称 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_所有制性质代码 PRIMARY KEY (所有制性质代码, 所有制性质名称)
  );
/*==============================================================*/
/* Table: DICT_教职工类别代码                                          */
/*==============================================================*/
CREATE TABLE DICT_教职工类别
  (
    代码 VARCHAR2(50) NOT NULL,
    类别 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_教职工类别代码 PRIMARY KEY (教职工代码, 教职工类别)
  );
/*==============================================================*/
/* Table: DICT_编制类别                                             */
/*==============================================================*/
CREATE TABLE DICT_编制类别
  (
    代码 VARCHAR2(50) NOT NULL,
    类别 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_编制类别 PRIMARY KEY (编制代码, 编制类别)
  );
/*==============================================================*/
/* Table: DICT_人员统计分类代码                                         */
/*==============================================================*/
CREATE TABLE DICT_人员统计分类代码
  (
    代码 VARCHAR2(50) NOT NULL,
    名称 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_人员统计分类代码 PRIMARY KEY (人员统计分类代码, 人员统计分类名称)
  );
/*==============================================================*/
/* Table: DICT_薪酬来源代码                                           */
/*==============================================================*/
CREATE TABLE DICT_薪酬来源代码
  (
    代码 VARCHAR2(50) NOT NULL,
    名称 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_薪酬来源代码 PRIMARY KEY (薪酬来源代码, 薪酬来源名称)
  );
/*==============================================================*/
/* Table: DICT_岗位代码                                             */
/*==============================================================*/
CREATE TABLE DICT_岗位代码
  (
    代码 VARCHAR2(50) NOT NULL,
    名称 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_岗位代码 PRIMARY KEY (岗位代码, 岗位名称)
  );
/*==============================================================*/
/* Table: DICT_身份类别                                             */
/*==============================================================*/
CREATE TABLE DICT_身份类别
  (
    代码 VARCHAR2(20) NOT NULL,
    名称 VARCHAR2(50) NOT NULL,
    CONSTRAINT DICT_身份类别 PRIMARY KEY (身份类别代码)
  );
/*==============================================================*/
/* Table: DICT_专业                                             */
/*==============================================================*/
CREATE TABLE DICT_专业
  (
    学科 VARCHAR2(255) NOT NULL,
    专业 VARCHAR2(255) NOT NULL,
    CONSTRAINT PK_DICT_专业 PRIMARY KEY (学科, 专业)
  );
/*==============================================================*/
/* Table: DICT_健康状况                                             */
/*==============================================================*/
CREATE TABLE DICT_健康状况
  (
    代码    VARCHAR2(50)
    健康状况 VARCHAR2(50) NOT NULL,
    CONSTRAINT PK_DICT_健康状况 PRIMARY KEY (健康状况)
  );
/*==============================================================*/
/* Table: DICT_国家                                               */
/*==============================================================*/
CREATE TABLE DICT_国家
  (
    国家代码 VARCHAR2(50) NOT NULL,
    国家   VARCHAR2(255) NOT NULL,
    CONSTRAINT PK_DICT_国家 PRIMARY KEY (国家代码, 国家)
  );
/*==============================================================*/
/* Table: DICT_学习形式                                             */
/*==============================================================*/
CREATE TABLE DICT_学习形式
  (
    学习形式 SMALLINT NOT NULL,
    CONSTRAINT PK_DICT_学习形式 PRIMARY KEY (学习形式)
  );
/*==============================================================*/
/* Table: DICT_学位                                               */
/*==============================================================*/
CREATE TABLE DICT_学位
  (
    学位     VARCHAR2(100) NOT NULL,
    职工号    VARCHAR2(10),
    学历     VARCHAR2(100),
    学历学_学位 VARCHAR2(100),
    CONSTRAINT PK_DICT_学位 PRIMARY KEY (学位)
  );
/*==============================================================*/
/* Table: DICT_学历                                               */
/*==============================================================*/
CREATE TABLE DICT_学历
  (
    学历     VARCHAR2(100) NOT NULL,
    职工号    VARCHAR2(10),
    学历学_学历 VARCHAR2(100),
    学位     VARCHAR2(100),
    CONSTRAINT PK_DICT_学历 PRIMARY KEY (学历)
  );
/*==============================================================*/
/* Table: DICT_学科                                               */
/*==============================================================*/
CREATE TABLE DICT_学科
  (
    学科名  VARCHAR2(255),
    学科代码 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_所在单位                                             */
/*==============================================================*/
CREATE TABLE DICT_所在单位
  (
    单位     VARCHAR2(255) NOT NULL,
    岗位     VARCHAR2(100) NOT NULL,
    科室     VARCHAR2(100) NOT NULL,
    职工号    VARCHAR2(10),
    任职单_单位 VARCHAR2(255),
    CONSTRAINT PK_DICT_所在单位 PRIMARY KEY (单位, 岗位, 科室)
  );
/*==============================================================*/
/* Table: DICT_籍贯                                               */
/*==============================================================*/
CREATE TABLE DICT_籍贯
  (
    代码 INTEGER NOT NULL,
    名称 VARCHAR2(255) NOT NULL,
    CONSTRAINT PK_DICT_籍贯 PRIMARY KEY (代码, 名称)
  );
/*==============================================================*/
/* Table: DICT_职称                                               */
/*==============================================================*/
CREATE TABLE DICT_职称
  (
    职称   VARCHAR2(255) NOT NULL,
    职称等级 SMALLINT NOT NULL,
    CONSTRAINT PK_DICT_职称 PRIMARY KEY (职称, 职称等级)
  );
