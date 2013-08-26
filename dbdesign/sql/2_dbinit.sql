create table 个人概况 
(
   婚姻状况             VARCHAR2(255)        not null,
   国家代码                 VARCHAR2(50),
   国家                   VARCHAR2(255),
   学习形式                 SMALLINT,
   外侨代码             VARCHAR2(50),
   政治面_职工号              VARCHAR2(10),
   代码                   INTEGER,
   名称                   VARCHAR2(255),
   健康状况             VARCHAR2(50),
   全局编号                 VARCHAR2(64),
   职工号                  VARCHAR2(10)         not null,
   最后修改时间               VARCHAR2(25),
   姓名                   VARCHAR2(60),
   姓名拼音                 VARCHAR2(240),
   曾用名                  VARCHAR2(60),
   出生日期                 VARCHAR2(8),
   性别                   VARCHAR2(20),
   国籍                   SMALLINT,
   民族编号                 SMALLINT,
   籍贯                   SMALLINT,
   出生地                  SMALLINT,
   照片                   BLOB,
   身份证号                 VARCHAR2(72),
   参加工作年月               VARCHAR2(6),
   血型                   VARCHAR2(10),
   家庭出身                 SMALLINT,
   本人成分                 SMALLINT,
   户口所在地                VARCHAR2(240),
   转干时间                 DATE,
   聘干时间                 DATE,
   首次任博导年月              DATE,
   首次任硕导年月              DATE,
   档案号                  VARCHAR2(200),
   内部编号                 VARCHAR2(64),
   保留字段                 VARCHAR2(300),
   人事关系转入时间             DATE,
   备注                   VARCHAR2(500),
   来源类别                 VARCHAR2(2),
   编制类别                 VARCHAR2(100),
   来校时间                 DATE,
   人员类别                 INTEGER
);


create table 专业技术职称 
(
   职称               VARCHAR2(255),
   职称等级             SMALLINT,
   全局编号                 VARCHAR2(64),
   职工号                  VARCHAR2(10),
   最后修改时间               VARCHAR2(25),
   资格评定日期         VARCHAR2(14),
   专业技术职务级别             VARCHAR2(50),
   任职资格名称               VARCHAR2(255),
   取得资格途径               VARCHAR2(100),
   评审单位                 VARCHAR2(120),
   取得资格日期               DATE,
   聘任职称等级               VARCHAR2(2),
   聘任职称                 VARCHAR2(3),
   聘任单位                 VARCHAR2(120),
   聘任日期                 DATE,
   聘任情况                 VARCHAR2(255),
   解聘日期                 DATE,
   解聘原因                 VARCHAR2(160),
   备注                   VARCHAR2(500), 
   constraint 专业技术职称 unique (职工号, 职称)
);


/*==============================================================*/
/* Table: DICT_职称                                               */
/*==============================================================*/
create table DICT_职称 
(
   职称                   VARCHAR2(255)        not null,
   职称等级                 SMALLINT             not null,
   constraint DICT_职称 primary key (职称, 职称等级)
);


/*==============================================================*/
/* Table: 政治面貌                                                  */
/*==============================================================*/
create table 政治面貌 
(
   全局编号                 VARCHAR2(64),
   职工号                  VARCHAR2(10)         not null,
   党政职务             VARCHAR2(100),
   党政职务级别           SMALLINT,
   最后修改时间               VARCHAR2(25),
   政治面貌                 VARCHAR2(50),
   参加日期                 DATE,
   参加党派时所在单位            VARCHAR2(120),
   介绍人                  VARCHAR2(60),
   转正日期                 DATE,
   异常类别                 SMALLINT,
   备注                   VARCHAR2(500),
   constraint 政治面貌 primary key (职工号)
);

/*==============================================================*/
/* Table: DICT_党政职务                                             */
/*==============================================================*/
create table DICT_党政职务 
(
   党政职务                 VARCHAR2(100)        not null,
   党政职务级别               SMALLINT             not null,
   职工号                  VARCHAR2(10),
   constraint DICT_党政职务 primary key (党政职务, 党政职务级别)
);

/*==============================================================*/
/* Table: 来校信息                                                  */
/*==============================================================*/
create table 来校信息 
(
   全局编号                 VARCHAR2(64),
   职工号                  VARCHAR2(10),
   最后修改时间               VARCHAR2(25),
   最近来校                 CHAR(10),
   来校年月                 DATE,
   来源类别                 VARCHAR2(2),
   来源地区                 SMALLINT,
   来校原因                 VARCHAR2(160),
   原单位名称                VARCHAR2(120),
   原党政职务                SMALLINT,
   原技术职务                SMALLINT,
   原从事学科                SMALLINT,
   备注                   VARCHAR2(500),
   constraint 来校信息 unique (职工号, 来校年月)
);

/*==============================================================*/
/* Table: 任职单位                                                  */
/*==============================================================*/
create table 任职单位 
(
   全局编号                 VARCHAR2(64),
   职工号                  VARCHAR2(10)         not null,
   最后修改时间               VARCHAR2(25),
   人事关系所在单位             RAW(1),
   调动时间                 DATE,
   单位                   VARCHAR2(255)        not null,
   岗位                   VARCHAR2(100),
   科室                   VARCHAR2(100),
   学科                   VARCHAR2(255),
   专业                   VARCHAR2(255),
   科室名称                 VARCHAR2(120),
   备注                   VARCHAR2(500),
   从事学科                 SMALLINT,
   从事专业                 VARCHAR2(6),
   主要岗位                 SMALLINT,
   兼职岗位                 SMALLINT,
   constraint 任职单位 primary key (职工号, 单位)
);


/*==============================================================*/
/* Table: DICT_人员类别代码                                           */
/*==============================================================*/
create table DICT_人员类别代码 
(
   人员类别代码               VARCHAR2(50)
);


/*==============================================================*/
/* Table: DICT_编制类别代码                                           */
/*==============================================================*/
create table DICT_编制类别代码 
(
   编制类别代码               VARCHAR2(50)
);


/*==============================================================*/
/* Table: DICT_来源类别代码                                           */
/*==============================================================*/
create table DICT_来源类别代码 
(
   来源类别代码               VARCHAR2(50)
);


/*==============================================================*/
/* Table: 学历学位                                                  */
/*==============================================================*/
create table 学历学位 
(
   全局编号                 VARCHAR2(64),
   职工号                  VARCHAR2(10)         not null,
   最后修改时间               VARCHAR2(25),
   学历                   VARCHAR2(100)        not null,
   所学专业                 VARCHAR2(120),
   入学年月                 DATE,
   学习形式                 SMALLINT,
   学习方式                 SMALLINT,
   学制                   SMALLINT,
   毕业院校                 VARCHAR2(255),
   毕业年月                 DATE,
   毕肄业学校或单位             VARCHAR2(360),
   学位                   VARCHAR2(100)        not null,
   学位授予日期               DATE,
   学位授予国家               SMALLINT,
   学位授予单位               VARCHAR2(360),
   备注                   VARCHAR2(500),
   constraint 学历学位 primary key (职工号, 学历, 学位)
);



/*==============================================================*/
/* Table: DICT_家庭出身代码                                           */
/*==============================================================*/
create table DICT_家庭出身代码 
(
   家庭出身代码               VARCHAR2(50)
);

/*==============================================================*/
/* Table: DICT_婚姻状况代码                                           */
/*==============================================================*/
create table DICT_婚姻状况代码 
(
   婚姻状况                 VARCHAR2(255)        not null,
   constraint DICT_婚姻状况代码 primary key (婚姻状况)
);

/*==============================================================*/
/* Table: DICT_港澳台侨外代码                                          */
/*==============================================================*/
create table DICT_港澳台侨外代码 
(
   外侨代码                 VARCHAR2(50)         not null,
   constraint DICT_港澳台侨外代码 primary key (外侨代码)
);


/*==============================================================*/
/* Table: DICT_在岗状态                                             */
/*==============================================================*/
create table DICT_在岗状态 
(
   在岗状态代码               VARCHAR2(50)         not null,
   在岗状态名称               VARCHAR2(100)        not null,
   constraint DICT_在岗状态 primary key (在岗状态代码, 在岗状态名称)
);

/*==============================================================*/
/* Table: DICT_所有制性质代码                                          */
/*==============================================================*/
create table DICT_所有制性质代码 
(
   所有制性质代码              VARCHAR2(50)         not null,
   所有制性质名称              VARCHAR2(100)        not null,
   constraint DICT_所有制性质代码 primary key (所有制性质代码, 所有制性质名称)
);

/*==============================================================*/
/* Table: DICT_教职工类别代码                                          */
/*==============================================================*/
create table DICT_教职工类别代码 
(
   教职工代码                VARCHAR2(50)         not null,
   教职工类别                VARCHAR2(100)        not null,
   constraint DICT_教职工类别代码 primary key (教职工代码, 教职工类别)
);

/*==============================================================*/
/* Table: DICT_编制类别                                             */
/*==============================================================*/
create table DICT_编制类别 
(
   编制代码                 VARCHAR2(50)         not null,
   编制类别                 VARCHAR2(100)        not null,
   constraint DICT_编制类别 primary key (编制代码, 编制类别)
);


/*==============================================================*/
/* Table: DICT_人员统计分类代码                                         */
/*==============================================================*/
create table DICT_人员统计分类代码 
(
   人员统计分类代码             VARCHAR2(50)         not null,
   人员统计分类名称             VARCHAR2(100)        not null,
   constraint DICT_人员统计分类代码 primary key (人员统计分类代码, 人员统计分类名称)
);

/*==============================================================*/
/* Table: DICT_薪酬来源代码                                           */
/*==============================================================*/
create table DICT_薪酬来源代码 
(
   薪酬来源代码               VARCHAR2(50)         not null,
   薪酬来源名称               VARCHAR2(100)        not null,
   constraint DICT_薪酬来源代码 primary key (薪酬来源代码, 薪酬来源名称)
);


/*==============================================================*/
/* Table: DICT_岗位代码                                             */
/*==============================================================*/
create table DICT_岗位代码 
(
   岗位代码                 VARCHAR2(50)         not null,
   岗位名称                 VARCHAR2(100)        not null,
   constraint DICT_岗位代码 primary key (岗位代码, 岗位名称)
);

/*==============================================================*/
/* Table: DICT_身份类别                                             */
/*==============================================================*/
create table DICT_身份类别 
(
   身份类别                 VARCHAR2(50)         not null,
   constraint DICT_身份类别 primary key (身份类别)
);


/*==============================================================*/
/* Table: 人员性质                                                  */
/*==============================================================*/
create table 人员性质 
(
   岗位代码                 VARCHAR2(50),
   岗位名称                 VARCHAR2(100),
   教职工代码                VARCHAR2(50),
   教职工类别                VARCHAR2(100),
   人员统计分类代码             VARCHAR2(50),
   人员统计分类名称             VARCHAR2(100),
   所有制性质代码              VARCHAR2(50),
   所有制性质名称              VARCHAR2(100),
   编制代码                 VARCHAR2(50),
   薪酬来源代码               VARCHAR2(50),
   薪酬来源名称               VARCHAR2(100),
   在岗状态代码               VARCHAR2(50),
   在岗状态名称               VARCHAR2(100),
   全局编号                 VARCHAR2(64),
   职工号                  VARCHAR2(10),
   最后修改时间               VARCHAR2(25),
   现记录                  RAW(1),
   调整时间                 DATE,
   编制类别                 VARCHAR2(100),
   岗位                   VARCHAR2(100),
   人员类别                 INTEGER,
   人员状态                 INTEGER,
   薪酬来源                 INTEGER,
   所有制性质                INTEGER,
   统计分类                 INTEGER,
   备注                   VARCHAR2(500),
   身份类别                 VARCHAR2(50)
);