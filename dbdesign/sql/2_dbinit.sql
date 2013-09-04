DROP TABLE DICT_职称 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: DICT_职称                                              */
/*==============================================================*/
CREATE TABLE DICT_职称
  (
    职称   VARCHAR2(255) NOT NULL,
    职称等级 SMALLINT NOT NULL,
    CONSTRAINT DICT_职称 PRIMARY KEY (职称, 职称等级)
  );

DROP TABLE DICT_党政职务 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: DICT_党政职务                                          */
/*==============================================================*/
CREATE TABLE DICT_党政职务
  (
    党政职务   VARCHAR2(100) NOT NULL,
    党政职务级别 SMALLINT NOT NULL,
    职工号    VARCHAR2(10),
    CONSTRAINT DICT_党政职务 PRIMARY KEY (党政职务, 党政职务级别)
  );

DROP TABLE DICT_人员类别代码 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: DICT_人员类别代码                                       */
/*==============================================================*/
CREATE TABLE DICT_人员类别代码
  (
    "人员类别代码" VARCHAR2(50),
    "人员类别名称" VARCHAR2(50)
  );

DROP TABLE DICT_编制类别代码 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: DICT_编制类别代码                                       */
/*==============================================================*/
CREATE TABLE DICT_编制类别代码
  (
    编制类别代码 VARCHAR2(50)
  );

DROP TABLE DICT_来源类别代码 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: DICT_来源类别代码                                       */
/*==============================================================*/
CREATE TABLE DICT_来源类别代码
  (
    来源类别代码 VARCHAR2(50)
  );

DROP TABLE DICT_家庭出身代码 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: DICT_家庭出身代码                                           */
/*==============================================================*/
CREATE TABLE DICT_家庭出身代码
  (
    家庭出身代码 VARCHAR2(50)
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
    在岗状态代码 VARCHAR2(50) NOT NULL,
    在岗状态名称 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_在岗状态 PRIMARY KEY (在岗状态代码, 在岗状态名称)
  );
/*==============================================================*/
/* Table: DICT_所有制性质代码                                          */
/*==============================================================*/
CREATE TABLE DICT_所有制性质代码
  (
    所有制性质代码 VARCHAR2(50) NOT NULL,
    所有制性质名称 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_所有制性质代码 PRIMARY KEY (所有制性质代码, 所有制性质名称)
  );
/*==============================================================*/
/* Table: DICT_教职工类别代码                                          */
/*==============================================================*/
CREATE TABLE DICT_教职工类别代码
  (
    教职工代码 VARCHAR2(50) NOT NULL,
    教职工类别 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_教职工类别代码 PRIMARY KEY (教职工代码, 教职工类别)
  );
/*==============================================================*/
/* Table: DICT_编制类别                                             */
/*==============================================================*/
CREATE TABLE DICT_编制类别
  (
    编制代码 VARCHAR2(50) NOT NULL,
    编制类别 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_编制类别 PRIMARY KEY (编制代码, 编制类别)
  );
/*==============================================================*/
/* Table: DICT_人员统计分类代码                                         */
/*==============================================================*/
CREATE TABLE DICT_人员统计分类代码
  (
    人员统计分类代码 VARCHAR2(50) NOT NULL,
    人员统计分类名称 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_人员统计分类代码 PRIMARY KEY (人员统计分类代码, 人员统计分类名称)
  );
/*==============================================================*/
/* Table: DICT_薪酬来源代码                                           */
/*==============================================================*/
CREATE TABLE DICT_薪酬来源代码
  (
    薪酬来源代码 VARCHAR2(50) NOT NULL,
    薪酬来源名称 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_薪酬来源代码 PRIMARY KEY (薪酬来源代码, 薪酬来源名称)
  );
/*==============================================================*/
/* Table: DICT_岗位代码                                             */
/*==============================================================*/
CREATE TABLE DICT_岗位代码
  (
    岗位代码 VARCHAR2(50) NOT NULL,
    岗位名称 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_岗位代码 PRIMARY KEY (岗位代码, 岗位名称)
  );
/*==============================================================*/
/* Table: DICT_身份类别                                             */
/*==============================================================*/
CREATE TABLE DICT_身份类别
  (
    "身份类别代码" VARCHAR2(20) NOT NULL,
    "身份类别名称" VARCHAR2(50) NOT NULL,
    CONSTRAINT DICT_身份类别 PRIMARY KEY (身份类别代码)
  );

DROP TABLE DICT_专业 CASCADE CONSTRAINTS;
CREATE TABLE DICT_专业
  (
    学科 VARCHAR2(255) NOT NULL,
    专业 VARCHAR2(255) NOT NULL,
    CONSTRAINT PK_DICT_专业 PRIMARY KEY (学科, 专业)
  );
DROP TABLE DICT_健康状况 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: DICT_健康状况                                             */
/*==============================================================*/
CREATE TABLE DICT_健康状况
  (
    健康状况 VARCHAR2(50) NOT NULL,
    CONSTRAINT PK_DICT_健康状况 PRIMARY KEY (健康状况)
  );
DROP TABLE DICT_国家 CASCADE CONSTRAINTS;
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
DROP TABLE DICT_学位 CASCADE CONSTRAINTS;
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
DROP TABLE DICT_学历 CASCADE CONSTRAINTS;
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
DROP TABLE DICT_学科 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: DICT_学科                                               */
/*==============================================================*/
CREATE TABLE DICT_学科
  (
    学科名 VARCHAR2(255),
    学科代码 VARCHAR2(50)
  );
DROP TABLE DICT_所在单位 CASCADE CONSTRAINTS;
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
DROP TABLE DICT_籍贯 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: DICT_籍贯                                               */
/*==============================================================*/
CREATE TABLE DICT_籍贯
  (
    代码 INTEGER NOT NULL,
    名称 VARCHAR2(255) NOT NULL,
    CONSTRAINT PK_DICT_籍贯 PRIMARY KEY (代码, 名称)
  );
DROP TABLE DICT_职称 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: DICT_职称                                               */
/*==============================================================*/
CREATE TABLE DICT_职称
  (
    职称   VARCHAR2(255) NOT NULL,
    职称等级 SMALLINT NOT NULL,
    CONSTRAINT PK_DICT_职称 PRIMARY KEY (职称, 职称等级)
  );

DROP TABLE 人员性质 CASCADE CONSTRAINTS;
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

DROP TABLE 个人概况 CASCADE CONSTRAINTS;
CREATE TABLE 个人概况
  (
    婚姻状况    VARCHAR2(255) NOT NULL,
    国家代码    VARCHAR2(50),
    国家      VARCHAR2(255),
    学习形式    SMALLINT,
    外侨代码    VARCHAR2(50),
    政治面_职工号 VARCHAR2(10),
    代码      INTEGER,
    名称      VARCHAR2(255),
    健康状况    VARCHAR2(50),
    全局编号    VARCHAR2(64),
    职工号     VARCHAR2(10) NOT NULL,
    最后修改时间  VARCHAR2(25),
    姓名      VARCHAR2(60),
    姓名拼音    VARCHAR2(240),
    曾用名     VARCHAR2(60),
    出生日期    VARCHAR2(10),
    性别      VARCHAR2(20),
    国籍      SMALLINT,
    民族编号    SMALLINT,
    籍贯      SMALLINT,
    出生地     SMALLINT,
    照片 BLOB,
    身份证号     VARCHAR2(72),
    参加工作年月   VARCHAR2(6),
    血型       VARCHAR2(10),
    家庭出身     SMALLINT,
    本人成分     SMALLINT,
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
    来源类别     VARCHAR2(2),
    编制类别     VARCHAR2(100),
    来校时间     DATE,
    人员类别     INTEGER
  );
  
  DROP TABLE 专业技术职称 CASCADE CONSTRAINTS;
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

DROP TABLE 政治面貌 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 政治面貌                                               */
/*==============================================================*/
CREATE TABLE 政治面貌
  (
    全局编号      VARCHAR2(64),
    职工号       VARCHAR2(10) NOT NULL,
    党政职务      VARCHAR2(100),
    党政职务级别    SMALLINT,
    最后修改时间    VARCHAR2(25),
    政治面貌      VARCHAR2(50),
    参加日期      DATE,
    参加党派时所在单位 VARCHAR2(120),
    介绍人       VARCHAR2(60),
    转正日期      DATE,
    异常类别      SMALLINT,
    备注        VARCHAR2(500),
    CONSTRAINT 政治面貌 PRIMARY KEY (职工号)
  );

DROP TABLE 任职单位 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 任职单位                                                  */
/*==============================================================*/
CREATE TABLE 任职单位
  (
    全局编号     VARCHAR2(64),
    职工号      VARCHAR2(10) NOT NULL,
    最后修改时间   VARCHAR2(25),
    人事关系所在单位 NUMBER(1,0),
    调动时间     DATE,
    单位       VARCHAR2(255) NOT NULL,
    DIC_单位   VARCHAR2(255),
    岗位       VARCHAR2(100),
    科室       VARCHAR2(100),
    学科       VARCHAR2(255),
    专业       VARCHAR2(255),
    科室名称     VARCHAR2(120),
    备注       VARCHAR2(500),
    从事学科     SMALLINT,
    从事专业     VARCHAR2(6),
    主要岗位     SMALLINT,
    兼职岗位     SMALLINT,
    CONSTRAINT PK_任职单位 PRIMARY KEY (职工号, 单位)
  );
DROP TABLE 党政职务 CASCADE CONSTRAINTS;

  
DROP TABLE 来校信息 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 来校信息                                               */
/*==============================================================*/
CREATE TABLE 来校信息
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10),
    最后修改时间 VARCHAR2(25),
    最近来校   CHAR(10),
    来校年月   DATE,
    来源类别   VARCHAR2(2),
    来源地区   SMALLINT,
    来校原因   VARCHAR2(160),
    原单位名称  VARCHAR2(120),
    原党政职务  SMALLINT,
    原技术职务  SMALLINT,
    原从事学科  SMALLINT,
    备注     VARCHAR2(500),
    CONSTRAINT 来校信息 UNIQUE (职工号, 来校年月)
  );

/*==============================================================*/
/* Table: 党政职务                                                  */
/*==============================================================*/
CREATE TABLE 党政职务
  (
    全局编号     VARCHAR2(64),
    职工号      VARCHAR2(10),
    最后修改时间   VARCHAR2(25),
    职务类别     SMALLINT,
    职务名称     SMALLINT,
    任职日期     DATE,
    任职部门     VARCHAR2(120),
    任职方式     NUMBER(1,0),
    任职原因     VARCHAR2(160),
    任职文号     VARCHAR2(48),
    职务级别     SMALLINT,
    职位分类     SMALLINT,
    任职批准单位   VARCHAR2(120),
    职务变动类别   SMALLINT,
    当前任职状况   NUMBER(1,0),
    职务排序     VARCHAR2(4),
    主管或从事的工作 VARCHAR2(80),
    免职日期     DATE,
    免职方式     NUMBER(1,0),
    免职原因     NUMBER(1,0),
    免职文号     VARCHAR2(48),
    免职批准单位   VARCHAR2(120),
    备注       VARCHAR2(500)
  );
DROP TABLE 出国进修 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 出国（境）进修                                               */
/*==============================================================*/
CREATE TABLE 出国进修
  (
    全局编号      VARCHAR2(64),
    职工号       VARCHAR2(10),
    最后修改时间    VARCHAR2(25),
    出国日期      DATE,
    出国目的      VARCHAR2(360),
    出国国别      SMALLINT,
    所去单位英文名称  VARCHAR2(360),
    所去单位中文名称  VARCHAR2(240),
    指导教师      VARCHAR2(120),
    指导教师EMAIL VARCHAR2(240),
    派出单位      VARCHAR2(120),
    经费项目渠道    VARCHAR2(120),
    出国经费来源    NUMBER(1,0),
    审批单位      VARCHAR2(120),
    审批日期      DATE,
    审批文号      VARCHAR2(48),
    批准期限      VARCHAR2(3),
    批延期限      VARCHAR2(3),
    学习工作内容    VARCHAR2(160),
    学习工作业绩    VARCHAR2(2400),
    学习方式      SMALLINT,
    应归日期      DATE,
    状态        SMALLINT,
    相应日期      DATE,
    经济关系      NUMBER(1,0),
    停薪年月      DATE,
    恢复薪金年月    DATE,
    担保人       DATE,
    担保人所在单位   SMALLINT,
    回国入境日期    DATE,
    备注        VARCHAR2(500)
  );
DROP TABLE 国内进修培训 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 国内进修培训                                                */
/*==============================================================*/
CREATE TABLE 国内进修培训
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10),
    最后修改时间 VARCHAR2(25),
    进修性质   SMALLINT,
    学习方式   SMALLINT,
    学习起始年月 DATE,
    学习终止年月 DATE,
    总学时    SMALLINT,
    学习内容   VARCHAR2(160),
    进修班名称  VARCHAR2(80),
    主办单位   VARCHAR2(120),
    主办单位性质 SMALLINT,
    在学单位   VARCHAR2(120),
    在学单位类别 NUMBER(1,0),
    进修结果   NUMBER(1,0),
    备注     VARCHAR2(500)
  );

DROP TABLE 学历学位 CASCADE CONSTRAINTS;
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

DROP TABLE 家庭成员 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 家庭成员                                                  */
/*==============================================================*/
CREATE TABLE 家庭成员
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10),
    最后修改时间 VARCHAR2(25),
    主要成员姓名 VARCHAR2(60),
    称谓     VARCHAR2(100),
    出生时间   DATE,
    工作单位   VARCHAR2(120),
    备注     VARCHAR2(500)
  );
DROP TABLE 工人技术等级 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 工人技术等级                                                */
/*==============================================================*/
CREATE TABLE 工人技术等级
  (
    全局编号     VARCHAR2(64),
    职工号      VARCHAR2(10),
    最后修改时间   VARCHAR2(25),
    现技术等级    NUMBER(1,0),
    工人技术等级资格 NUMBER(1,0),
    资格取得日期   DATE,
    取得资格途径   VARCHAR2(100),
    资格评审单位   CHAR(10),
    聘用工人技术等级 NUMBER(1,0),
    起聘日期     DATE,
    聘用单位     VARCHAR2(120),
    聘用情况     NUMBER(1,0),
    解聘日期     DATE,
    解聘原因     VARCHAR2(160),
    工人工种名称   SMALLINT,
    备注       VARCHAR2(500)
  );
DROP TABLE 惩处 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 惩处                                                    */
/*==============================================================*/
CREATE TABLE 惩处
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10),
    最后修改时间 VARCHAR2(25),
    惩处名称   VARCHAR2(80),
    惩处原因   VARCHAR2(160),
    惩处内容   VARCHAR2(160),
    惩处单位   VARCHAR2(120),
    惩处文号   VARCHAR2(48),
    惩处日期   DATE,
    惩处撤销日期 DATE,
    惩处撤销文号 VARCHAR2(48),
    惩处撤销原因 VARCHAR2(160),
    备注     VARCHAR2(500)
  );
DROP TABLE 指导博士后 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 指导博士后                                                 */
/*==============================================================*/
CREATE TABLE 指导博士后
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10),
    最后修改时间 VARCHAR2(25),
    博士后职工号 VARCHAR2(10),
    流动站名称  VARCHAR2(400),
    所在学院   SMALLINT,
    研究项目   VARCHAR2(500),
    进站时间   DATE,
    进站学科   SMALLINT,
    博士后类型  SMALLINT,
    备注     VARCHAR2(500)
  );
DROP TABLE 指导国内访问学者 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 指导国内访问学者                                              */
/*==============================================================*/
CREATE TABLE 指导国内访问学者
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10),
    最后修改时间 VARCHAR2(25),
    推荐单位   VARCHAR2(400),
    接受单位   SMALLINT,
    访问学者   VARCHAR2(10),
    课题     VARCHAR2(500),
    访学时间   DATE,
    访学证书号  VARCHAR2(60),
    备注     VARCHAR2(500)
  );
DROP TABLE 来校信息 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 来校信息                                                  */
/*==============================================================*/
CREATE TABLE 来校信息
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10),
    最后修改时间 VARCHAR2(25),
    最近来校   VARCHAR2(10),
    来校年月   DATE,
    来源类别   VARCHAR2(2),
    来源地区   SMALLINT,
    来校原因   VARCHAR2(160),
    原单位名称  VARCHAR2(120),
    原党政职务  SMALLINT,
    原技术职务  SMALLINT,
    原从事学科  SMALLINT,
    备注     VARCHAR2(500),
    CONSTRAINT AK_来校信息_来校信息 UNIQUE (职工号, 来校年月)
  );
DROP TABLE 来校前工作简历 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 来校前工作简历                                               */
/*==============================================================*/
CREATE TABLE 来校前工作简历
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10),
    最后修改时间 VARCHAR2(25),
    起始年月   DATE,
    截止年月   DATE,
    所在单位名称 VARCHAR2(120),
    从事工作内容 VARCHAR2(120),
    曾任党政职务 SMALLINT,
    曾任技术职务 SMALLINT,
    证明人    VARCHAR2(60),
    备注     VARCHAR2(500)
  );
DROP TABLE 校内异动 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 校内异动                                                  */
/*==============================================================*/
CREATE TABLE 校内异动
  (
    全局编号       VARCHAR2(64),
    职工号        VARCHAR2(10),
    最后修改时间     VARCHAR2(25),
    任职单位记录全局编号 VARCHAR2(64),
    校内调出单位     SMALLINT,
    校内调出所室名称   VARCHAR2(120),
    校内调入单位     SMALLINT,
    校内调入所室名称   VARCHAR2(120),
    编制异动类别     NUMBER(1,0),
    编制异动年月     DATE,
    编制异动原因     VARCHAR2(160),
    离岗年月       DATE,
    离岗原因       NUMBER(1,0),
    返岗年月       DATE,
    病休起始年月     DATE,
    病休诊断医院     VARCHAR2(120),
    病休诊断证明     VARCHAR2(1200),
    恢复工作年月     DATE,
    康复诊断医院     VARCHAR2(120),
    康复诊断证明     VARCHAR2(1200),
    备注         VARCHAR2(500)
  );
DROP TABLE 离校 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 离校                                                    */
/*==============================================================*/
CREATE TABLE 离校
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10),
    最后修改时间 VARCHAR2(25),
    离校年月   DATE,
    离校原因   SMALLINT,
    离校去向   VARCHAR2(120),
    备注     VARCHAR2(500)
  );
DROP TABLE 考核 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 考核                                                    */
/*==============================================================*/
CREATE TABLE 考核
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10),
    最后修改时间 VARCHAR2(25),
    考核时间   DATE,
    考核类别   SMALLINT,
    考核岗位   VARCHAR2(120),
    考核内容   VARCHAR2(500),
    考核等级   SMALLINT,
    考核结果   SMALLINT,
    备注     VARCHAR2(500)
  );
DROP TABLE 联系方式 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 联系方式                                                  */
/*==============================================================*/
CREATE TABLE 联系方式
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10),
    最后修改时间 VARCHAR2(25),
    单位电话   VARCHAR2(120),
    家庭电话   VARCHAR2(120),
    手机     VARCHAR2(120),
    传真     VARCHAR2(120),
    电子信箱   VARCHAR2(120),
    主页地址   VARCHAR2(240),
    通信地址   VARCHAR2(240),
    家庭住址   VARCHAR2(120),
    邮政编码   VARCHAR2(12),
    备注     VARCHAR2(500)
  );
DROP TABLE 聘用合同 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 聘用合同                                                  */
/*==============================================================*/
CREATE TABLE 聘用合同
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10),
    最后修改时间 VARCHAR2(25),
    合同类别   SMALLINT,
    起聘日期   DATE,
    终聘日期   DATE,
    合同编号   VARCHAR2(200),
    合同文件   VARCHAR2(200),
    备注     VARCHAR2(500),
    聘用岗位   VARCHAR2(32)
  );
DROP TABLE 荣誉性奖励 CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: 荣誉性奖励                                                 */
/*==============================================================*/
CREATE TABLE 荣誉性奖励
  (
    全局编号   VARCHAR2(64),
    职工号    VARCHAR2(10),
    最后修改时间 VARCHAR2(25),
    获奖项目   VARCHAR2(120),
    奖励级别   SMALLINT,
    获奖类别   NUMBER(1,0),
    奖励方式   VARCHAR2(40),
    奖励名称   VARCHAR2(120),
    奖励名称分类 SMALLINT,
    颁奖单位   VARCHAR2(120),
    颁奖日期   SMALLINT,
    荣誉称号   VARCHAR2(120),
    获荣誉日期  DATE,
    获奖角色   SMALLINT,
    备注     VARCHAR2(500)
  );