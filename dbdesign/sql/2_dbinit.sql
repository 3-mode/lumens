/* =======================================================================*/
/*                                清空实体表                                */
/* =======================================================================*/
DROP TABLE 个人概况 CASCADE CONSTRAINTS;
DROP TABLE 党政职务 CASCADE CONSTRAINTS;
DROP TABLE 人员性质 CASCADE CONSTRAINTS;
DROP TABLE 专业技术职称 CASCADE CONSTRAINTS;
DROP TABLE 来校信息 CASCADE CONSTRAINTS;
DROP TABLE 任职单位 CASCADE CONSTRAINTS;
DROP TABLE 政治面貌 CASCADE CONSTRAINTS;
DROP TABLE 学历学位 CASCADE CONSTRAINTS;
DROP TABLE 出国进修 CASCADE CONSTRAINTS;
DROP TABLE 国内进修培训 CASCADE CONSTRAINTS;
DROP TABLE 家庭成员 CASCADE CONSTRAINTS;
DROP TABLE 工人技术等级 CASCADE CONSTRAINTS;
DROP TABLE 惩处 CASCADE CONSTRAINTS;
DROP TABLE 指导博士后 CASCADE CONSTRAINTS;
DROP TABLE 指导国内访问学者 CASCADE CONSTRAINTS;
DROP TABLE 来校前工作简历 CASCADE CONSTRAINTS;
DROP TABLE 校内异动 CASCADE CONSTRAINTS;
DROP TABLE 离校 CASCADE CONSTRAINTS;
DROP TABLE 考核 CASCADE CONSTRAINTS;
DROP TABLE 联系方式 CASCADE CONSTRAINTS;
DROP TABLE 聘用合同 CASCADE CONSTRAINTS;
DROP TABLE 荣誉性奖励 CASCADE CONSTRAINTS;
/* =======================================================================*/
/*                                清空字典表                                */
/* =======================================================================*/
DROP TABLE DICT_国家代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_出国目的代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_干部职务名称代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_干部职务级别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_文化程度代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_中华人民共和国学位代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_荣誉称号和荣誉奖章代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_政治面貌代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_语种名称代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_中国各民族名称罗马字母拼写法和代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_人的性别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_中华人民共和国行政区划代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_社会兼职代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_婚姻状况代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_职称代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_家庭出身代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_中国语种代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_本人成分代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_职工身份类别 CASCADE CONSTRAINTS;
DROP TABLE DICT_申请表类型代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_授奖等级代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_成果获奖类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_奖励类型代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_指导研究生类型代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_教学类型代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_鉴定结论代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_成果类型代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_完成形式代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_学生类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_学科门类代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_密级代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_项目类型代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_项目经费来源代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_计划完成情况代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_论文报告形式代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_法律状态代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_考核情况代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_社会经济效益代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_任课课程类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_任课课程分类代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_任课课程性质代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_任课课程形式代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_任课课程精品代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_任课角色代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_协作单位类型代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_专利类型代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_论文级别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_著作类型代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_本科生学科专业代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_出版社级别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_批准形式代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_项目来源代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_署名单位代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_操作名称代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_登记表类型代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_职称级别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_人员代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_专业技术职务级别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_行业工种类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_聘任情况代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_取得资格途径代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_教师资格类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_离退类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_离岗原因代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_编制异动代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_博士后类型代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_政治面貌异常类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_工人技术等级职务代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_人员统计分类代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_所有制性质代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_在岗状态代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_教职工类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_血型代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_教职工来源代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_学科专业代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_离校离职原因代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_房屋产权代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_房屋位置状况代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_房屋类型代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_建筑物结构代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_外语程度代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_称谓代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_角色代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_奖励代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_教师获奖类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_奖励级别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_高层次人才奖励类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_学习方式代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_学习形式代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_职级代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_免职原因代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_免职方式代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_任职状况代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_职务变动类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_任职方式代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_职位分类代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_出国经费来源代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_出国状态代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_机构代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_进修性质代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_单位性质代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_在学单位类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_教育结果代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_考核类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_考核等级代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_考核结果代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_合同类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_职务类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_职务类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_人员类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_本人成分代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_学习形式代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_来源类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_港澳台侨外代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_编制类别代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_薪酬来源代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_岗位代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_健康状况代码 CASCADE CONSTRAINTS;
DROP TABLE DICT_所在单位 CASCADE CONSTRAINTS;
DROP TABLE DICT_籍贯 CASCADE CONSTRAINTS;
DROP TABLE DICT_党政职务 CASCADE CONSTRAINTS;
DROP TABLE DICT_在岗状态 CASCADE CONSTRAINTS;
DROP TABLE DICT_身份类别 CASCADE CONSTRAINTS;
/* ========================================================================== */
/*                                   实体表                                    */
/* ========================================================================== */
/* ========================================================================== */
/* 外链字段如下                                                                 */
/* 所在单位, 籍贯，科室名称,政治面貌, 最后学历,最高学位,最后学历毕业学校,最后学历毕业年月*/
/* 现职称,现专业技术职务级别,专业技术职务资格评定日期,聘任职称, 聘任职称等级,现职级日期  */
/* 从事专业,主要岗位,人员状态,党政职务,党政职务级别,身份类别                         */
/* ========================================================================== */
CREATE TABLE 个人概况
  (
    职工号    VARCHAR2(10) NOT NULL,
    姓名     VARCHAR2(60),
    性别     VARCHAR2(10),
    出生日期   VARCHAR2(8),
    身份证号   VARCHAR2(20),
    民族     VARCHAR2(100),
    来校年月   DATE,
    参加工作年月 VARCHAR2(6),
    健康状况   VARCHAR2(50),
    外侨情况   VARCHAR2(50),
    婚姻状况   VARCHAR2(255),
    来源类别   VARCHAR2(2),
    编制类别   VARCHAR2(100),
    人员类别   VARCHAR2(20),
    国家     VARCHAR2(255),
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
    备注       VARCHAR2(500)
  );
/*==============================================================*/
/* Table: 学历学位                                                */
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
/* Table: 人员性质                                                */
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
/*==============================================================*/
/* Table: 专业技术职称                                                */
/*==============================================================*/
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
/* Table: 任职单位                                                */
/*==============================================================*/
CREATE TABLE 任职单位
  (
    全局编号     VARCHAR2(64),
    职工号      VARCHAR2(10) NOT NULL,
    最后修改时间   VARCHAR2(25),
    人事关系所在单位 NUMBER(1, 0),
    调动时间     DATE,
    单位       VARCHAR2(255) NOT NULL,
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
    CONSTRAINT 任职单位 PRIMARY KEY (职工号, 单位)
  );
/*==============================================================*/
/* Table: 党政职务                                               */
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
/*==============================================================*/
/* Table: 出国（境）进修                                           */
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
/*==============================================================*/
/* Table: 国内进修培训                                            */
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
/*==============================================================*/
/* Table: 家庭成员                                                */
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
/*==============================================================*/
/* Table: 工人技术等级                                            */
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
/*==============================================================*/
/* Table: 惩处                                                   */
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
/*==============================================================*/
/* Table: 指导博士后                                              */
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
/*==============================================================*/
/* Table: 指导国内访问学者                                         */
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
/*==============================================================*/
/* Table: 来校前工作简历                                           */
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
/*==============================================================*/
/* Table: 校内异动                                                */
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
/*==============================================================*/
/* Table: 离校                                                   */
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
/*==============================================================*/
/* Table: 考核                                                   */
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
/*==============================================================*/
/* Table: 联系方式                                                */
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
/*==============================================================*/
/* Table: 聘用合同                                                */
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
/*==============================================================*/
/* Table: 荣誉性奖励                                              */
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
/*==============================================================*/
/*                        字典 国家标准                           */
/*==============================================================*/
/*==============================================================*/
/* Table: DICT_国家代码                                              */
/* GB_T2659                                                     */
/*==============================================================*/
CREATE TABLE DICT_国家代码
  (
    代码 VARCHAR2(20) NOT NULL,
    名称   VARCHAR2(300) NOT NULL,
    CONSTRAINT PK_DICT_国家 PRIMARY KEY (代码)
  );
/*==============================================================*/
/* Table: DICT_出国目的代码                                       */
/* GB_T10301                                                    */
/*==============================================================*/
CREATE TABLE DICT_出国目的代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(100)
  );
/*==============================================================*/
/* Table: DICT_健康状况代码                                       */
/*  GB_T4767                                                    */
/*==============================================================*/
CREATE TABLE DICT_健康状况代码
  (
    代码 VARCHAR2(20) NOT NULL,
    名称 VARCHAR2(50) NOT NULL,
    CONSTRAINT PK_DICT_健康状况 PRIMARY KEY (健康状况)
  );
/*==============================================================*/
/* Table: DICT_干部职务名称代码                                    */
/* GB_T12403                                                    */
/*==============================================================*/
CREATE TABLE DICT_干部职务名称代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_干部职务级别代码                                    */
/* GB_T12407                                                    */
/*==============================================================*/
CREATE TABLE DICT_干部职务级别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_文化程度代码                                       */
/* GB_T12407                                                    */
/*==============================================================*/
CREATE TABLE DICT_文化程度代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_中华人民共和国学位代码                               */
/* GB_T6864                                                     */
/*==============================================================*/
CREATE TABLE DICT_中华人民共和国学位代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_荣誉称号和荣誉奖章代码                               */
/* GB_T8560                                                     */
/*==============================================================*/
CREATE TABLE DICT_荣誉称号和荣誉奖章代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_政治面貌代码                                       */
/* GB_T4762                                                     */
/*==============================================================*/
CREATE TABLE DICT_政治面貌代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_语种名称代码                                       */
/* GB_T4880                                                     */
/*==============================================================*/
CREATE TABLE DICT_语种名称代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_中国各民族名称罗马字母拼写法和代码                     */
/* GB_T3304                                                     */
/*==============================================================*/
CREATE TABLE DICT_中国各民族名称罗马字母拼写法和代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_人的性别代码                                       */
/* GB_T2261                                                     */
/*==============================================================*/
CREATE TABLE DICT_人的性别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_中华人民共和国行政区划代码                            */
/* GB_T2260                                                     */
/*==============================================================*/
CREATE TABLE DICT_中华人民共和国行政区划代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_社会兼职代码                                       */
/* GB_T12408                                                    */
/*==============================================================*/
CREATE TABLE DICT_社会兼职代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(100)
  );
/*==============================================================*/
/* Table: DICT_婚姻状况代码                                       */
/* GB_T4766                                                     */
/*==============================================================*/
CREATE TABLE DICT_婚姻状况代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_职称代码                                           */
/* GB_T8561_2001                                                */
/*==============================================================*/
CREATE TABLE DICT_职称代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_家庭出身代码                                       */
/* GB_T4765                                                     */
/*==============================================================*/
CREATE TABLE DICT_家庭出身代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_中国语种代码                                       */
/* GB_T4881                                                      */
/* 原表为空                                                      */
/*==============================================================*/
CREATE TABLE DICT_中国语种代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_本人成分代码                                       */
/* GB_T4764                                                     */
/*==============================================================*/
CREATE TABLE DICT_本人成分代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/*==============================================================*/
/*                          字典自定义代码                         */
/*==============================================================*/
/*==============================================================*/
/*==============================================================*/
/* Table: DICT_职工身份类别                                       */
/*==============================================================*/
CREATE TABLE DICT_职工身份类别
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_申请表类型代码                                      */
/*==============================================================*/
CREATE TABLE DICT_申请表类型代码
  (
    代码 VARCHAR2(50),
    名称 VARCHAR2(255)
  );
/*==============================================================*/
/* Table: DICT_授奖等级代码                                       */
/*==============================================================*/
CREATE TABLE DICT_授奖等级代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_成果获奖类别代码                                    */
/*==============================================================*/
CREATE TABLE DICT_成果获奖类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_奖励类型代码                                       */
/*==============================================================*/
CREATE TABLE DICT_奖励类型代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_指导研究生类型代码                                      */
/*==============================================================*/
CREATE TABLE DICT_指导研究生类型代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_教学类型代码                                       */
/*==============================================================*/
CREATE TABLE DICT_教学类型代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_鉴定结论代码                                       */
/*==============================================================*/
CREATE TABLE DICT_鉴定结论代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_成果类型代码                                       */
/*==============================================================*/
CREATE TABLE DICT_成果类型代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_完成形式代码                                       */
/*==============================================================*/
CREATE TABLE DICT_完成形式代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_学生类别代码                                       */
/*==============================================================*/
CREATE TABLE DICT_学生类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_学科门类代码                                        */
/*==============================================================*/
CREATE TABLE DICT_学科门类代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_密级代码                                          */
/*==============================================================*/
CREATE TABLE DICT_密级代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_项目类型代码                                        */
/*==============================================================*/
CREATE TABLE DICT_项目类型代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_项目经费来源代码                                    */
/*==============================================================*/
CREATE TABLE DICT_项目经费来源代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_计划完成情况代码                                    */
/*==============================================================*/
CREATE TABLE DICT_计划完成情况代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_论文报告形式代码                                    */
/*==============================================================*/
CREATE TABLE DICT_论文报告形式代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_法律状态代码                                        */
/*==============================================================*/
CREATE TABLE DICT_法律状态代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_考核情况代码                                        */
/*==============================================================*/
CREATE TABLE DICT_考核情况代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_社会经济效益代码                                    */
/*==============================================================*/
CREATE TABLE DICT_社会经济效益代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_任课课程类别代码                                     */
/*==============================================================*/
CREATE TABLE DICT_任课课程类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_任课课程分类代码                                    */
/*==============================================================*/
CREATE TABLE DICT_任课课程分类代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_任课课程性质代码                                    */
/*==============================================================*/
CREATE TABLE DICT_任课课程性质代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_任课课程形式代码                                    */
/*==============================================================*/
CREATE TABLE DICT_任课课程形式代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_任课课程精品代码                                    */
/*==============================================================*/
CREATE TABLE DICT_任课课程精品代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_任课角色代码                                       */
/*==============================================================*/
CREATE TABLE DICT_任课角色代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_协作单位类型代码                                    */
/*==============================================================*/
CREATE TABLE DICT_协作单位类型代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_专利类型代码                                       */
/*==============================================================*/
CREATE TABLE DICT_专利类型代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_论文级别代码                                        */
/*==============================================================*/
CREATE TABLE DICT_论文级别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_著作类型代码                                       */
/*==============================================================*/
CREATE TABLE DICT_著作类型代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_本科生学科专业代码                                   */
/*==============================================================*/
CREATE TABLE DICT_本科生学科专业代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_出版社级别代码                                      */
/*==============================================================*/
CREATE TABLE DICT_出版社级别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_批准形式代码                                       */
/*==============================================================*/
CREATE TABLE DICT_批准形式代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_项目来源代码                                       */
/*==============================================================*/
CREATE TABLE DICT_项目来源代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_署名单位代码                                       */
/*==============================================================*/
CREATE TABLE DICT_署名单位代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_操作名称代码                                           */
/*==============================================================*/
CREATE TABLE DICT_操作名称代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_登记表类型代码                                      */
/*==============================================================*/
CREATE TABLE DICT_登记表类型代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_职称级别代码                                           */
/*==============================================================*/
CREATE TABLE DICT_职称级别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_人员代码                                           */
/* 对应 职工号                                                    */
/* 原表 属性1 对应身份证号                                          */
/*==============================================================*/
CREATE TABLE DICT_人员代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_专业技术职务级别代码                                 */
/*==============================================================*/
CREATE TABLE DICT_专业技术职务级别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );

/*==============================================================*/
/* Table: DICT_行业工种类别代码                                    */
/*==============================================================*/
CREATE TABLE DICT_行业工种类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_聘任情况代码                                       */
/*==============================================================*/
CREATE TABLE DICT_聘任情况代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_取得资格途径代码                                    */
/*==============================================================*/
CREATE TABLE DICT_取得资格途径代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_教师资格类别代码                                    */
/*==============================================================*/
CREATE TABLE DICT_教师资格类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_离退类别代码                                       */
/*==============================================================*/
CREATE TABLE DICT_离退类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_离岗原因代码                                        */
/*==============================================================*/
CREATE TABLE DICT_离岗原因代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_编制异动代码                                        */
/*==============================================================*/
CREATE TABLE DICT_编制异动代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_博士后类型代码                                     */
/*==============================================================*/
CREATE TABLE DICT_博士后类型代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_政治面貌异常类别代码                                */
/*==============================================================*/
CREATE TABLE DICT_政治面貌异常类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_工人技术等级、职务代码                               */
/*==============================================================*/
CREATE TABLE DICT_工人技术等级职务代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_人员统计分类代码                                    */
/*==============================================================*/
CREATE TABLE DICT_人员统计分类代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_所有制性质代码                                      */
/*==============================================================*/
CREATE TABLE DICT_所有制性质代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_在岗状态代码                                       */
/*==============================================================*/
CREATE TABLE DICT_在岗状态代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_教职工类别代码                                     */
/*==============================================================*/
CREATE TABLE DICT_教职工类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_血型代码                                           */
/*==============================================================*/
CREATE TABLE DICT_血型代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_教职工来源代码                                      */
/*==============================================================*/
CREATE TABLE DICT_教职工来源代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_学科专业代码                                       */
/*==============================================================*/
CREATE TABLE DICT_学科专业代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_离校离职原因代码                                    */
/*==============================================================*/
CREATE TABLE DICT_离校离职原因代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_房屋产权代码                                       */
/*==============================================================*/
CREATE TABLE DICT_房屋产权代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_房屋位置状况                                       */
/*==============================================================*/
CREATE TABLE DICT_房屋位置状况代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_房屋类型代码                                       */
/*==============================================================*/
CREATE TABLE DICT_房屋类型代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_建筑物结构代码                                      */
/*==============================================================*/
CREATE TABLE DICT_建筑物结构代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_外语程度代码                                       */
/*==============================================================*/
CREATE TABLE DICT_外语程度代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_称谓代码                                           */
/*==============================================================*/
CREATE TABLE DICT_称谓代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_角色代码                                           */
/*==============================================================*/
CREATE TABLE DICT_角色代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_奖励代码                                           */
/*==============================================================*/
CREATE TABLE DICT_奖励代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_教师获奖类别代码                                    */
/*==============================================================*/
CREATE TABLE DICT_教师获奖类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_奖励级别代码                                       */
/*==============================================================*/
CREATE TABLE DICT_奖励级别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(100)
  );
/*==============================================================*/
/* Table: DICT_高层次人才奖励类别代码                               */
/*==============================================================*/
CREATE TABLE DICT_高层次人才奖励类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_学习方式代码                                       */
/*==============================================================*/
CREATE TABLE DICT_学习方式代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_学习形式代码                                       */
/*==============================================================*/
CREATE TABLE DICT_学习形式代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_职级代码                                           */
/*==============================================================*/
CREATE TABLE DICT_职级代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_免职原因代码                                       */
/*==============================================================*/
CREATE TABLE DICT_免职原因代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(100)
  );
/*==============================================================*/
/* Table: DICT_免职方式代码                                       */
/*==============================================================*/
CREATE TABLE DICT_免职方式代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_任职状况代码                                           */
/*==============================================================*/
CREATE TABLE DICT_任职状况代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_职务变动类别代码                                    */
/*==============================================================*/
CREATE TABLE DICT_职务变动类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_任职方式代码                                       */
/*==============================================================*/
CREATE TABLE DICT_任职方式代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_职位分类代码                                       */
/*==============================================================*/
CREATE TABLE DICT_职位分类代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(100)
  );
/*==============================================================*/
/* Table: DICT_出国经费来源代码                                       */
/*==============================================================*/
CREATE TABLE DICT_出国经费来源代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_出国状态代码                                       */
/*==============================================================*/
CREATE TABLE DICT_出国状态代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_机构代码                                           */
/*==============================================================*/
CREATE TABLE DICT_机构代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_进修性质代码                                       */
/*==============================================================*/
CREATE TABLE DICT_进修性质代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_单位性质代码                                       */
/*==============================================================*/
CREATE TABLE DICT_单位性质代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_在学单位类别代码                                    */
/*==============================================================*/
CREATE TABLE DICT_在学单位类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_教育结果代码                                       */
/*==============================================================*/
CREATE TABLE DICT_教育结果代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_考核类别代码                                       */
/*==============================================================*/
CREATE TABLE DICT_考核类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_考核等级代码                                       */
/*==============================================================*/
CREATE TABLE DICT_考核等级代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_考核结果代码                                       */
/*==============================================================*/
CREATE TABLE DICT_考核结果代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_合同类别代码                                       */
/*==============================================================*/
CREATE TABLE DICT_合同类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_合同类别代码                                       */
/*==============================================================*/
CREATE TABLE DICT_职务类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_人员类别代码                                       */
/*==============================================================*/
CREATE TABLE DICT_人员类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_本人成分代码                                       */
/*==============================================================*/
CREATE TABLE DICT_本人成分代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_学习形式代码                                       */
/*==============================================================*/
CREATE TABLE DICT_学习形式代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_来源类别代码                                       */
/*==============================================================*/
CREATE TABLE DICT_来源类别代码
  (
    代码 VARCHAR2(20),
    名称 VARCHAR2(50)
  );
/*==============================================================*/
/* Table: DICT_港澳台侨外代码                                      */
/*==============================================================*/
CREATE TABLE DICT_港澳台侨外代码
  (
    代码 VARCHAR2(20) NOT NULL,
    名称 VARCHAR2(100) NOT NULL,
  );
/*==============================================================*/
/* Table: DICT_编制类别代码                                           */
/*==============================================================*/
CREATE TABLE DICT_编制类别代码
  (
    代码 VARCHAR2(50) NOT NULL,
    类别 VARCHAR2(100) NOT NULL,
  );
/*==============================================================*/
/* Table: DICT_薪酬来源代码                                       */
/*==============================================================*/
CREATE TABLE DICT_薪酬来源代码
  (
    代码 VARCHAR2(50) NOT NULL,
    名称 VARCHAR2(100) NOT NULL,
  );
/*==============================================================*/
/* Table: DICT_岗位代码                                          */
/*==============================================================*/
CREATE TABLE DICT_岗位代码
  (
    代码 VARCHAR2(50) NOT NULL,
    名称 VARCHAR2(100) NOT NULL,
    CONSTRAINT DICT_岗位代码 PRIMARY KEY (代码, 名称)
  );

/*==============================================================*/
/*                        有待进一步评估                          */
/*==============================================================*/
/*==============================================================*/
/* Table: DICT_所在单位                                          */
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
/* Table: DICT_籍贯                                              */
/*==============================================================*/
CREATE TABLE DICT_籍贯
  (
    代码 INTEGER NOT NULL,
    名称 VARCHAR2(255) NOT NULL,
    CONSTRAINT PK_DICT_籍贯 PRIMARY KEY (代码, 名称)
  );  
/*==============================================================*/
/* Table: DICT_党政职务                                           */
/*==============================================================*/
CREATE TABLE DICT_党政职务
  (
    党政职务   VARCHAR2(100) NOT NULL,
    党政职务级别 SMALLINT NOT NULL,
    职工号    VARCHAR2(10),
    CONSTRAINT DICT_党政职务 PRIMARY KEY (党政职务, 党政职务级别)
  );  

/*==============================================================*/
/* Table: DICT_身份类别                                           */
/*==============================================================*/
CREATE TABLE DICT_在岗状态
  (
    代码 VARCHAR2(50) NOT NULL,
    名称 VARCHAR2(500) NOT NULL,
  ); 
/*==============================================================*/
/* Table: DICT_身份类别                                           */
/*==============================================================*/
CREATE TABLE DICT_身份类别
  (
    代码 VARCHAR2(20) NOT NULL,
    名称 VARCHAR2(500) NOT NULL,
  ); 
/*==============================================================*/
/*                         冗余的表，考虑删除                      */
/*==============================================================*/
/*==============================================================*/
/* Table: DICT_专业, 跟学科专业代码重复                            */
/*==============================================================*/
CREATE TABLE DICT_专业
  (
    学科 VARCHAR2(255) NOT NULL,
    专业 VARCHAR2(255) NOT NULL,
    CONSTRAINT PK_DICT_专业 PRIMARY KEY (学科, 专业)
  );
