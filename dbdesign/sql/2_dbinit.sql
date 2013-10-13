/* =======================================================================*/
/*                                清空实体表                                */
/* =======================================================================*/

/* DROP TABLE 来校信息 CASCADE CONSTRAINTS; */
/* DROP TABLE 政治面貌 CASCADE CONSTRAINTS;*/
drop table 个人概况 cascade constraints;
drop table 党政职务 cascade constraints;
drop table 人员性质 cascade constraints;
drop table 专业技术职称 cascade constraints;
drop table 任职单位 cascade constraints;

drop table 学历学位 cascade constraints;
drop table 出国进修 cascade constraints;
drop table 国内进修培训 cascade constraints;
drop table 家庭成员 cascade constraints;
drop table 工人技术等级 cascade constraints;
drop table 惩处 cascade constraints;
drop table 指导博士后 cascade constraints;
drop table 指导国内访问学者 cascade constraints;
drop table 来校前工作简历 cascade constraints;
drop table 校内异动 cascade constraints;
drop table 离校 cascade constraints;
drop table 考核 cascade constraints;
drop table 联系方式 cascade constraints;
drop table 聘用合同 cascade constraints;
drop table 荣誉性奖励 cascade constraints;
/* =======================================================================*/
/*                                清空字典表                                */
/* =======================================================================*/
drop table DICT_国家代码 cascade constraints;
drop table DICT_出国目的代码 cascade constraints;
drop table DICT_干部职务名称代码 cascade constraints;
drop table DICT_干部职务级别代码 cascade constraints;
drop table DICT_文化程度代码 cascade constraints;
drop table DICT_学位代码 cascade constraints;
drop table DICT_荣誉称号和奖章 cascade constraints;
drop table DICT_政治面貌代码 cascade constraints;
drop table DICT_语种名称代码 cascade constraints;
drop table DICT_民族名称代码 cascade constraints;
drop table DICT_人的性别代码 cascade constraints;
drop table DICT_中国行政区划代码 cascade constraints;
drop table DICT_社会兼职代码 cascade constraints;
drop table DICT_婚姻状况代码 cascade constraints;
drop table DICT_职称代码 cascade constraints;
drop table DICT_家庭出身代码 cascade constraints;
drop table DICT_中国语种代码 cascade constraints;
drop table DICT_本人成分代码 cascade constraints;
drop table DICT_职工身份类别 cascade constraints;
drop table DICT_申请表类型代码 cascade constraints;
drop table DICT_授奖等级代码 cascade constraints;
drop table DICT_成果获奖类别代码 cascade constraints;
drop table DICT_奖励类型代码 cascade constraints;
drop table DICT_指导研究生类型 cascade constraints;
drop table DICT_教学类型代码 cascade constraints;
drop table DICT_鉴定结论代码 cascade constraints;
drop table DICT_成果类型代码 cascade constraints;
drop table DICT_完成形式代码 cascade constraints;
drop table DICT_学生类别代码 cascade constraints;
drop table DICT_学科门类代码 cascade constraints;
drop table DICT_密级代码 cascade constraints;
drop table DICT_项目类型代码 cascade constraints;
drop table DICT_项目经费来源代码 cascade constraints;
drop table DICT_计划完成情况代码 cascade constraints;
drop table DICT_论文报告形式代码 cascade constraints;
drop table DICT_法律状态代码 cascade constraints;
drop table DICT_考核情况代码 cascade constraints;
drop table DICT_社会经济效益代码 cascade constraints;
drop table DICT_任课课程类别代码 cascade constraints;
drop table DICT_任课课程分类代码 cascade constraints;
drop table DICT_任课课程性质代码 cascade constraints;
drop table DICT_任课课程形式代码 cascade constraints;
drop table DICT_任课课程精品代码 cascade constraints;
drop table DICT_任课角色代码 cascade constraints;
drop table DICT_协作单位类型代码 cascade constraints;
drop table DICT_专利类型代码 cascade constraints;
drop table DICT_论文级别代码 cascade constraints;
drop table DICT_著作类型代码 cascade constraints;
drop table DICT_学科专业代码 cascade constraints;
drop table DICT_出版社级别代码 cascade constraints;
drop table DICT_批准形式代码 cascade constraints;
drop table DICT_项目来源代码 cascade constraints;
drop table DICT_署名单位代码 cascade constraints;
drop table DICT_操作名称代码 cascade constraints;
drop table DICT_登记表类型代码 cascade constraints;
drop table DICT_职称级别代码 cascade constraints;
drop table DICT_人员代码 cascade constraints;
drop table DICT_专业技术职务级别 cascade constraints;
drop table DICT_行业工种类别代码 cascade constraints;
drop table DICT_聘任情况代码 cascade constraints;
drop table DICT_取得资格途径代码 cascade constraints;
drop table DICT_教师资格类别代码 cascade constraints;
drop table DICT_离退类别代码 cascade constraints;
drop table DICT_离岗原因代码 cascade constraints;
drop table DICT_编制异动代码 cascade constraints;
drop table DICT_博士后类型代码 cascade constraints;
drop table DICT_政治面貌异常代码 cascade constraints;
drop table DICT_工人技术等级代码 cascade constraints;
drop table DICT_人员统计分类代码 cascade constraints;
drop table DICT_所有制性质代码 cascade constraints;
drop table DICT_在岗状态代码 cascade constraints;
drop table DICT_教职工类别代码 cascade constraints;
drop table DICT_血型代码 cascade constraints;
drop table DICT_教职工来源代码 cascade constraints;
drop table DICT_离校离职原因代码 cascade constraints;
drop table DICT_房屋产权代码 cascade constraints;
drop table DICT_房屋位置状况代码 cascade constraints;
drop table DICT_房屋类型代码 cascade constraints;
drop table DICT_建筑物结构代码 cascade constraints;
drop table DICT_外语程度代码 cascade constraints;
drop table DICT_称谓代码 cascade constraints;
drop table DICT_角色代码 cascade constraints;
drop table DICT_奖励代码 cascade constraints;
drop table DICT_教师获奖类别代码 cascade constraints;
drop table DICT_奖励级别代码 cascade constraints;
drop table DICT_高层次人才奖励 cascade constraints;
drop table DICT_学习方式代码 cascade constraints;
drop table DICT_学习形式代码 cascade constraints;
drop table DICT_职级代码 cascade constraints;
drop table DICT_免职原因代码 cascade constraints;
drop table DICT_免职方式代码 cascade constraints;
drop table DICT_任职状况代码 cascade constraints;
drop table DICT_职务变动类别代码 cascade constraints;
drop table DICT_任职方式代码 cascade constraints;
drop table DICT_职位分类代码 cascade constraints;
drop table DICT_出国经费来源代码 cascade constraints;
drop table DICT_出国状态代码 cascade constraints;
drop table DICT_机构代码 cascade constraints;
drop table DICT_进修性质代码 cascade constraints;
drop table DICT_单位性质代码 cascade constraints;
drop table DICT_在学单位类别代码 cascade constraints;
drop table DICT_教育结果代码 cascade constraints;
drop table DICT_考核类别代码 cascade constraints;
drop table DICT_考核等级代码 cascade constraints;
drop table DICT_考核结果代码 cascade constraints;
drop table DICT_合同类别代码 cascade constraints;
drop table DICT_职务类别代码 cascade constraints;
drop table DICT_人员类别代码 cascade constraints;
drop table DICT_来源类别代码 cascade constraints;
drop table DICT_港澳台侨外代码 cascade constraints;
drop table DICT_编制类别代码 cascade constraints;
drop table DICT_薪酬来源代码 cascade constraints;
drop table DICT_岗位代码 cascade constraints;
drop table DICT_健康状况代码 cascade constraints;
drop table DICT_所在单位 cascade constraints;
drop table DICT_籍贯 cascade constraints;
drop table DICT_党政职务 cascade constraints;
drop table DICT_身份类别 cascade constraints;
drop table DICT_专业 cascade constraints;
/* ========================================================================== */
/*                                   实体表                                    */
/* ========================================================================== */
/* ========================================================================== */
/* 外链字段如下                                                                 */
/* 所在单位, 籍贯，科室名称,政治面貌, 最后学历,最高学位,最后学历毕业学校,最后学历毕业年月*/
/* 现职称,现专业技术职务级别,专业技术职务资格评定日期,聘任职称, 聘任职称等级,现职级日期  */
/* 从事专业,主要岗位,人员状态,党政职务,党政职务级别,身份类别                         */
/* ========================================================================== */
create table 个人概况
  (
    职工号    varchar2(10) not null,
    姓名     varchar2(60),
    性别     varchar2(10),
    出生日期   varchar2(8),
    身份证号   varchar2(20),
    民族     varchar2(100),
    来校年月   date,
    参加工作年月 varchar2(6),
    健康状况   varchar2(50),
    外侨情况   varchar2(50),
    婚姻状况   varchar2(255),
    来源类别   varchar2(2),
    编制类别   varchar2(100),
    人员类别   varchar2(20),
    国家     varchar2(255),
    学习形式   varchar2(50),
    全局编号   varchar2(64),
    最后修改时间 varchar2(25),
    姓名拼音   varchar2(240),
    曾用名    varchar2(60),
    出生地    varchar2(200),
    照片 blob,
    血型       varchar2(10),
    家庭出身     varchar2(50),
    本人成分     varchar2(50),
    户口所在地    varchar2(240),
    转干时间     date,
    聘干时间     date,
    首次任博导年月  date,
    首次任硕导年月  date,
    档案号      varchar2(200),
    内部编号     varchar2(64),
    保留字段     varchar2(300),
    人事关系转入时间 date,
    备注       varchar2(500)
  );
/*==============================================================*/
/* Table: 学历学位                                                */
/*==============================================================*/
create table 学历学位
  (
    全局编号     varchar2(64),
    职工号      varchar2(10) not null,
    最后修改时间   varchar2(25),
    学历       varchar2(100) not null,
    所学专业     varchar2(120),
    入学年月     date,
    学习形式     smallint,
    学习方式     smallint,
    学制       smallint,
    毕业院校     varchar2(255),
    毕业年月     date,
    毕肄业学校或单位 varchar2(360),
    学位       varchar2(100) not null,
    学位授予日期   date,
    学位授予国家   smallint,
    学位授予单位   varchar2(360),
    备注       varchar2(500),
    constraint 学历学位 primary key (职工号, 学历, 学位)
  );
/*==============================================================*/
/* Table: 人员性质                                                */
/*==============================================================*/
create table 人员性质
  (
    岗位代码     varchar2(50),
    岗位名称     varchar2(100),
    教职工代码    varchar2(50),
    教职工类别    varchar2(100),
    人员统计分类代码 varchar2(50),
    人员统计分类名称 varchar2(100),
    所有制性质代码  varchar2(50),
    所有制性质名称  varchar2(100),
    编制代码     varchar2(50),
    薪酬来源代码   varchar2(50),
    薪酬来源名称   varchar2(100),
    在岗状态代码   varchar2(50),
    在岗状态名称   varchar2(100),
    全局编号     varchar2(64),
    职工号      varchar2(10),
    最后修改时间   varchar2(25),
    现记录      number(1,0),
    调整时间     date,
    编制类别     varchar2(100),
    岗位       varchar2(100),
    人员类别     integer,
    人员状态     integer,
    薪酬来源     integer,
    所有制性质    integer,
    统计分类     integer,
    备注       varchar2(500),
    身份类别     varchar2(50)
  );
/*==============================================================*/
/* Table: 专业技术职称                                                */
/*==============================================================*/
create table 专业技术职称
  (
    职称       varchar2(255),
    职称等级     smallint,
    全局编号     varchar2(64),
    职工号      varchar2(10),
    最后修改时间   varchar2(25),
    资格评定日期   varchar2(14),
    专业技术职务级别 varchar2(50),
    任职资格名称   varchar2(255),
    取得资格途径   varchar2(100),
    评审单位     varchar2(120),
    取得资格日期   date,
    聘任职称等级   varchar2(2),
    聘任职称     varchar2(3),
    聘任单位     varchar2(120),
    聘任日期     date,
    聘任情况     varchar2(255),
    解聘日期     date,
    解聘原因     varchar2(160),
    备注       varchar2(500),
    constraint 专业技术职称 unique (职工号, 职称)
  );
/*==============================================================*/
/* Table: 任职单位                                                */
/*==============================================================*/
create table 任职单位
  (
    全局编号     varchar2(64),
    职工号      varchar2(10) not null,
    最后修改时间   varchar2(25),
    人事关系所在单位 number(1, 0),
    调动时间     date,
    单位       varchar2(255) not null,
    岗位       varchar2(100),
    科室       varchar2(100),
    学科       varchar2(255),
    专业       varchar2(255),
    科室名称     varchar2(120),
    备注       varchar2(500),
    从事学科     smallint,
    从事专业     varchar2(6),
    主要岗位     smallint,
    兼职岗位     smallint,
    constraint 任职单位 primary key (职工号, 单位)
  );
/*==============================================================*/
/* Table: 党政职务                                               */
/*==============================================================*/
create table 党政职务
  (
    全局编号     varchar2(64),
    职工号      varchar2(10),
    最后修改时间   varchar2(25),
    职务类别     smallint,
    职务名称     smallint,
    任职日期     date,
    任职部门     varchar2(120),
    任职方式     number(1,0),
    任职原因     varchar2(160),
    任职文号     varchar2(48),
    职务级别     smallint,
    职位分类     smallint,
    任职批准单位   varchar2(120),
    职务变动类别   smallint,
    当前任职状况   number(1,0),
    职务排序     varchar2(4),
    主管或从事的工作 varchar2(80),
    免职日期     date,
    免职方式     number(1,0),
    免职原因     number(1,0),
    免职文号     varchar2(48),
    免职批准单位   varchar2(120),
    备注       varchar2(500)
  );
/*==============================================================*/
/* Table: 出国（境）进修                                           */
/*==============================================================*/
create table 出国进修
  (
    全局编号      varchar2(64),
    职工号       varchar2(10),
    最后修改时间    varchar2(25),
    出国日期      date,
    出国目的      varchar2(360),
    出国国别      smallint,
    所去单位英文名称  varchar2(360),
    所去单位中文名称  varchar2(240),
    指导教师      varchar2(120),
    指导教师EMAIL varchar2(240),
    派出单位      varchar2(120),
    经费项目渠道    varchar2(120),
    出国经费来源    number(1,0),
    审批单位      varchar2(120),
    审批日期      date,
    审批文号      varchar2(48),
    批准期限      varchar2(3),
    批延期限      varchar2(3),
    学习工作内容    varchar2(160),
    学习工作业绩    varchar2(2400),
    学习方式      smallint,
    应归日期      date,
    状态        smallint,
    相应日期      date,
    经济关系      number(1,0),
    停薪年月      date,
    恢复薪金年月    date,
    担保人       date,
    担保人所在单位   smallint,
    回国入境日期    date,
    备注        varchar2(500)
  );
/*==============================================================*/
/* Table: 国内进修培训                                            */
/*==============================================================*/
create table 国内进修培训
  (
    全局编号   varchar2(64),
    职工号    varchar2(10),
    最后修改时间 varchar2(25),
    进修性质   smallint,
    学习方式   smallint,
    学习起始年月 date,
    学习终止年月 date,
    总学时    smallint,
    学习内容   varchar2(160),
    进修班名称  varchar2(80),
    主办单位   varchar2(120),
    主办单位性质 smallint,
    在学单位   varchar2(120),
    在学单位类别 number(1,0),
    进修结果   number(1,0),
    备注     varchar2(500)
  );
/*==============================================================*/
/* Table: 家庭成员                                                */
/*==============================================================*/
create table 家庭成员
  (
    全局编号   varchar2(64),
    职工号    varchar2(10),
    最后修改时间 varchar2(25),
    主要成员姓名 varchar2(60),
    政治面貌    varchar2(20),
    称谓     varchar2(100),
    出生时间   varchar2(20),
    工作单位   varchar2(120),
    备注     varchar2(500)
  );
/*==============================================================*/
/* Table: 工人技术等级                                            */
/*==============================================================*/
create table 工人技术等级
  (
    全局编号     varchar2(64),
    职工号      varchar2(10),
    最后修改时间   varchar2(25),
    现技术等级    number(1,0),
    工人技术等级资格 number(1,0),
    资格取得日期   date,
    取得资格途径   varchar2(100),
    资格评审单位   char(10),
    聘用工人技术等级 number(1,0),
    起聘日期     date,
    聘用单位     varchar2(120),
    聘用情况     number(1,0),
    解聘日期     date,
    解聘原因     varchar2(160),
    工人工种名称   smallint,
    备注       varchar2(500)
  );
/*==============================================================*/
/* Table: 惩处                                                   */
/*==============================================================*/
create table 惩处
  (
    全局编号   varchar2(64),
    职工号    varchar2(10),
    最后修改时间 varchar2(25),
    惩处名称   varchar2(80),
    惩处原因   varchar2(160),
    惩处内容   varchar2(160),
    惩处单位   varchar2(120),
    惩处文号   varchar2(48),
    惩处日期   date,
    惩处撤销日期 date,
    惩处撤销文号 varchar2(48),
    惩处撤销原因 varchar2(160),
    备注     varchar2(500)
  );
/*==============================================================*/
/* Table: 指导博士后                                              */
/*==============================================================*/
create table 指导博士后
  (
    全局编号   varchar2(64),
    职工号    varchar2(10),
    最后修改时间 varchar2(25),
    博士后职工号 varchar2(10),
    流动站名称  varchar2(400),
    所在学院   smallint,
    研究项目   varchar2(500),
    进站时间   date,
    进站学科   smallint,
    博士后类型  smallint,
    备注     varchar2(500)
  );
/*==============================================================*/
/* Table: 指导国内访问学者                                         */
/*==============================================================*/
create table 指导国内访问学者
  (
    全局编号   varchar2(64),
    职工号    varchar2(10),
    最后修改时间 varchar2(25),
    推荐单位   varchar2(400),
    接受单位   smallint,
    访问学者   varchar2(10),
    课题     varchar2(500),
    访学时间   date,
    访学证书号  varchar2(60),
    备注     varchar2(500)
  );
/*==============================================================*/
/* Table: 来校前工作简历                                           */
/*==============================================================*/
create table 来校前工作简历
  (
    全局编号   varchar2(64),
    职工号    varchar2(10),
    最后修改时间 varchar2(25),
    起始年月   date,
    截止年月   date,
    所在单位名称 varchar2(120),
    从事工作内容 varchar2(120),
    曾任党政职务 smallint,
    曾任技术职务 smallint,
    证明人    varchar2(60),
    备注     varchar2(500)
  );
/*==============================================================*/
/* Table: 校内异动                                                */
/*==============================================================*/
create table 校内异动
  (
    全局编号       varchar2(64),
    职工号        varchar2(10),
    最后修改时间     varchar2(25),
    任职单位记录全局编号 varchar2(64),
    校内调出单位     smallint,
    校内调出所室名称   varchar2(120),
    校内调入单位     smallint,
    校内调入所室名称   varchar2(120),
    编制异动类别     number(1,0),
    编制异动年月     date,
    编制异动原因     varchar2(160),
    离岗年月       date,
    离岗原因       number(1,0),
    返岗年月       date,
    病休起始年月     date,
    病休诊断医院     varchar2(120),
    病休诊断证明     varchar2(1200),
    恢复工作年月     date,
    康复诊断医院     varchar2(120),
    康复诊断证明     varchar2(1200),
    备注         varchar2(500)
  );
/*==============================================================*/
/* Table: 离校                                                   */
/*==============================================================*/
create table 离校
  (
    全局编号   varchar2(64),
    职工号    varchar2(10),
    最后修改时间 varchar2(25),
    离校年月   date,
    离校原因   smallint,
    离校去向   varchar2(120),
    备注     varchar2(500)
  );
/*==============================================================*/
/* Table: 考核                                                   */
/*==============================================================*/
create table 考核
  (
    全局编号   varchar2(64),
    职工号    varchar2(10),
    最后修改时间 varchar2(25),
    考核时间   date,
    考核类别   smallint,
    考核岗位   varchar2(120),
    考核内容   varchar2(500),
    考核等级   smallint,
    考核结果   smallint,
    备注     varchar2(500)
  );
/*==============================================================*/
/* Table: 联系方式                                                */
/*==============================================================*/
create table 联系方式
  (
    全局编号   varchar2(64),
    职工号    varchar2(10),
    最后修改时间 varchar2(25),
    单位电话   varchar2(120),
    家庭电话   varchar2(120),
    手机     varchar2(120),
    传真     varchar2(120),
    电子信箱   varchar2(120),
    主页地址   varchar2(240),
    通信地址   varchar2(240),
    家庭住址   varchar2(120),
    邮政编码   varchar2(12),
    备注     varchar2(500)
  );
/*==============================================================*/
/* Table: 聘用合同                                                */
/*==============================================================*/
create table 聘用合同
  (
    全局编号   varchar2(64),
    职工号    varchar2(10),
    最后修改时间 varchar2(25),
    合同类别   smallint,
    起聘日期   date,
    终聘日期   date,
    合同编号   varchar2(200),
    合同文件   varchar2(200),
    备注     varchar2(500),
    聘用岗位   varchar2(32)
  );
/*==============================================================*/
/* Table: 荣誉性奖励                                              */
/*==============================================================*/
create table 荣誉性奖励
  (
    全局编号   varchar2(64),
    职工号    varchar2(10),
    最后修改时间 varchar2(25),
    获奖项目   varchar2(120),
    奖励级别   smallint,
    获奖类别   number(1,0),
    奖励方式   varchar2(40),
    奖励名称   varchar2(120),
    奖励名称分类 smallint,
    颁奖单位   varchar2(120),
    颁奖日期   smallint,
    荣誉称号   varchar2(120),
    获荣誉日期  date,
    获奖角色   smallint,
    备注     varchar2(500)
  );
/*==============================================================*/
/*                        字典 国家标准                           */
/*==============================================================*/
/*==============================================================*/
/* Table: DICT_国家代码                                              */
/* GB_T2659                                                     */
/*==============================================================*/
create table DICT_国家代码
  (
    代码 varchar2(20) not null,
    名称   varchar2(300) not null,
    constraint PK_DICT_国家 primary key (代码)
  );
/*==============================================================*/
/* Table: DICT_出国目的代码                                       */
/* GB_T10301                                                    */
/*==============================================================*/
create table DICT_出国目的代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_健康状况代码                                       */
/*  GB_T4767                                                    */
/*==============================================================*/
create table DICT_健康状况代码
  (
    代码 varchar2(20) not null,
    名称 varchar2(50) not null
  );
/*==============================================================*/
/* Table: DICT_干部职务名称代码                                    */
/* GB_T12403                                                    */
/*==============================================================*/
create table DICT_干部职务名称代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_干部职务级别代码                                    */
/* GB_T12407                                                    */
/*==============================================================*/
create table DICT_干部职务级别代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_文化程度代码                                       */
/* GB_T12407                                                    */
/*==============================================================*/
create table DICT_文化程度代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_中华人民共和国学位代码                               */
/* GB_T6864                                                     */
/*==============================================================*/
create table DICT_学位代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_荣誉称号和荣誉奖章代码                               */
/* GB_T8560                                                     */
/*==============================================================*/
create table DICT_荣誉称号和奖章
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_政治面貌代码                                       */
/* GB_T4762                                                     */
/*==============================================================*/
create table DICT_政治面貌代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_语种名称代码                                       */
/* GB_T4880                                                     */
/*==============================================================*/
create table DICT_语种名称代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_中国各民族名称罗马字母拼写法和代码                     */
/* GB_T3304                                                     */
/*==============================================================*/
create table DICT_民族名称代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_人的性别代码                                       */
/* GB_T2261                                                     */
/*==============================================================*/
create table DICT_人的性别代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_中华人民共和国行政区划代码                            */
/* GB_T2260                                                     */
/*==============================================================*/
create table DICT_中国行政区划代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_社会兼职代码                                       */
/* GB_T12408                                                    */
/*==============================================================*/
create table DICT_社会兼职代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_婚姻状况代码                                       */
/* GB_T4766                                                     */
/*==============================================================*/
create table DICT_婚姻状况代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_职称代码                                           */
/* GB_T8561_2001                                                */
/*==============================================================*/
create table DICT_职称代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_家庭出身代码                                       */
/* GB_T4765                                                     */
/*==============================================================*/
create table DICT_家庭出身代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_中国语种代码                                       */
/* GB_T4881                                                      */
/* 原表为空                                                      */
/*==============================================================*/
create table DICT_中国语种代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_本人成分代码                                       */
/* GB_T4764                                                     */
/*==============================================================*/
create table DICT_本人成分代码
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/*==============================================================*/
/*                          字典自定义代码                         */
/*==============================================================*/
/*==============================================================*/
/*==============================================================*/
/* Table: DICT_职工身份类别                                       */
/*==============================================================*/
create table DICT_职工身份类别
  (
    代码 varchar2(20),
    名称 varchar2(50)
  );
/*==============================================================*/
/* Table: DICT_申请表类型代码                                      */
/*==============================================================*/
create table DICT_申请表类型代码
  (
    代码 varchar2(50),
    名称 varchar2(255)
  );
/*==============================================================*/
/* Table: DICT_授奖等级代码                                       */
/*==============================================================*/
create table DICT_授奖等级代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_成果获奖类别代码                                    */
/*==============================================================*/
create table DICT_成果获奖类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_奖励类型代码                                       */
/*==============================================================*/
create table DICT_奖励类型代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_指导研究生类型代码                                      */
/*==============================================================*/
create table DICT_指导研究生类型
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_教学类型代码                                       */
/*==============================================================*/
create table DICT_教学类型代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_鉴定结论代码                                       */
/*==============================================================*/
create table DICT_鉴定结论代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_成果类型代码                                       */
/*==============================================================*/
create table DICT_成果类型代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_完成形式代码                                       */
/*==============================================================*/
create table DICT_完成形式代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_学生类别代码                                       */
/*==============================================================*/
create table DICT_学生类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_学科门类代码                                        */
/*==============================================================*/
create table DICT_学科门类代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_密级代码                                          */
/*==============================================================*/
create table DICT_密级代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_项目类型代码                                        */
/*==============================================================*/
create table DICT_项目类型代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_项目经费来源代码                                    */
/*==============================================================*/
create table DICT_项目经费来源代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_计划完成情况代码                                    */
/*==============================================================*/
create table DICT_计划完成情况代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_论文报告形式代码                                    */
/*==============================================================*/
create table DICT_论文报告形式代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_法律状态代码                                        */
/*==============================================================*/
create table DICT_法律状态代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_考核情况代码                                        */
/*==============================================================*/
create table DICT_考核情况代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_社会经济效益代码                                    */
/*==============================================================*/
create table DICT_社会经济效益代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_任课课程类别代码                                     */
/*==============================================================*/
create table DICT_任课课程类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_任课课程分类代码                                    */
/*==============================================================*/
create table DICT_任课课程分类代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_任课课程性质代码                                    */
/*==============================================================*/
create table DICT_任课课程性质代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_任课课程形式代码                                    */
/*==============================================================*/
create table DICT_任课课程形式代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_任课课程精品代码                                    */
/*==============================================================*/
create table DICT_任课课程精品代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_任课角色代码                                       */
/*==============================================================*/
create table DICT_任课角色代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_协作单位类型代码                                    */
/*==============================================================*/
create table DICT_协作单位类型代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_专利类型代码                                       */
/*==============================================================*/
create table DICT_专利类型代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_论文级别代码                                        */
/*==============================================================*/
create table DICT_论文级别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_著作类型代码                                       */
/*==============================================================*/
create table DICT_著作类型代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );

/*==============================================================*/
/* Table: DICT_出版社级别代码                                      */
/*==============================================================*/
create table DICT_出版社级别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_批准形式代码                                       */
/*==============================================================*/
create table DICT_批准形式代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_项目来源代码                                       */
/*==============================================================*/
create table DICT_项目来源代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_署名单位代码                                       */
/*==============================================================*/
create table DICT_署名单位代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_操作名称代码                                           */
/*==============================================================*/
create table DICT_操作名称代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_登记表类型代码                                      */
/*==============================================================*/
create table DICT_登记表类型代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_职称级别代码                                           */
/*==============================================================*/
create table DICT_职称级别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_人员代码                                           */
/* 对应 职工号                                                    */
/* 原表 属性1 对应身份证号                                          */
/*==============================================================*/
create table DICT_人员代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_专业技术职务级别代码                                 */
/*==============================================================*/
create table DICT_专业技术职务级别
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );

/*==============================================================*/
/* Table: DICT_行业工种类别代码                                    */
/*==============================================================*/
create table DICT_行业工种类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_聘任情况代码                                       */
/*==============================================================*/
create table DICT_聘任情况代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_取得资格途径代码                                    */
/*==============================================================*/
create table DICT_取得资格途径代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_教师资格类别代码                                    */
/*==============================================================*/
create table DICT_教师资格类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_离退类别代码                                       */
/*==============================================================*/
create table DICT_离退类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_离岗原因代码                                        */
/*==============================================================*/
create table DICT_离岗原因代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_编制异动代码                                        */
/*==============================================================*/
create table DICT_编制异动代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_博士后类型代码                                     */
/*==============================================================*/
create table DICT_博士后类型代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_政治面貌异常类别代码                                */
/*==============================================================*/
create table DICT_政治面貌异常代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_工人技术等级、职务代码                               */
/*==============================================================*/
create table DICT_工人技术等级代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_人员统计分类代码                                    */
/*==============================================================*/
create table DICT_人员统计分类代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_所有制性质代码                                      */
/*==============================================================*/
create table DICT_所有制性质代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_在岗状态代码                                       */
/*==============================================================*/
create table DICT_在岗状态代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_教职工类别代码                                     */
/*==============================================================*/
create table DICT_教职工类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_血型代码                                           */
/*==============================================================*/
create table DICT_血型代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_教职工来源代码                                      */
/*==============================================================*/
create table DICT_教职工来源代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_学科专业代码                                       */
/*==============================================================*/
create table DICT_学科专业代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_离校离职原因代码                                    */
/*==============================================================*/
create table DICT_离校离职原因代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_房屋产权代码                                       */
/*==============================================================*/
create table DICT_房屋产权代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_房屋位置状况                                       */
/*==============================================================*/
create table DICT_房屋位置状况代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_房屋类型代码                                       */
/*==============================================================*/
create table DICT_房屋类型代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_建筑物结构代码                                      */
/*==============================================================*/
create table DICT_建筑物结构代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_外语程度代码                                       */
/*==============================================================*/
create table DICT_外语程度代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_称谓代码                                           */
/*==============================================================*/
create table DICT_称谓代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_角色代码                                           */
/*==============================================================*/
create table DICT_角色代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_奖励代码                                           */
/*==============================================================*/
create table DICT_奖励代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_教师获奖类别代码                                    */
/*==============================================================*/
create table DICT_教师获奖类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_奖励级别代码                                       */
/*==============================================================*/
create table DICT_奖励级别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_高层次人才奖励类别代码                               */
/*==============================================================*/
create table DICT_高层次人才奖励
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_学习方式代码                                       */
/*==============================================================*/
create table DICT_学习方式代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_学习形式代码                                       */
/*==============================================================*/
create table DICT_学习形式代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_职级代码                                           */
/*==============================================================*/
create table DICT_职级代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_免职原因代码                                       */
/*==============================================================*/
create table DICT_免职原因代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_免职方式代码                                       */
/*==============================================================*/
create table DICT_免职方式代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_任职状况代码                                           */
/*==============================================================*/
create table DICT_任职状况代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_职务变动类别代码                                    */
/*==============================================================*/
create table DICT_职务变动类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_任职方式代码                                       */
/*==============================================================*/
create table DICT_任职方式代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_职位分类代码                                       */
/*==============================================================*/
create table DICT_职位分类代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_出国经费来源代码                                       */
/*==============================================================*/
create table DICT_出国经费来源代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_出国状态代码                                       */
/*==============================================================*/
create table DICT_出国状态代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_机构代码                                           */
/*==============================================================*/
create table DICT_机构代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_进修性质代码                                       */
/*==============================================================*/
create table DICT_进修性质代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_单位性质代码                                       */
/*==============================================================*/
create table DICT_单位性质代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_在学单位类别代码                                    */
/*==============================================================*/
create table DICT_在学单位类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_教育结果代码                                       */
/*==============================================================*/
create table DICT_教育结果代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_考核类别代码                                       */
/*==============================================================*/
create table DICT_考核类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_考核等级代码                                       */
/*==============================================================*/
create table DICT_考核等级代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_考核结果代码                                       */
/*==============================================================*/
create table DICT_考核结果代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_合同类别代码                                       */
/*==============================================================*/
create table DICT_合同类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_合同类别代码                                       */
/*==============================================================*/
create table DICT_职务类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_人员类别代码                                       */
/*==============================================================*/
create table DICT_人员类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );

/*==============================================================*/
/* Table: DICT_来源类别代码                                       */
/*==============================================================*/
create table DICT_来源类别代码
  (
    代码 varchar2(20),
    名称 varchar2(100)
  );
/*==============================================================*/
/* Table: DICT_港澳台侨外代码                                      */
/*==============================================================*/
create table DICT_港澳台侨外代码
  (
    代码 varchar2(20) not null,
    名称 varchar2(100) not null
  );
/*==============================================================*/
/* Table: DICT_编制类别代码                                           */
/*==============================================================*/
create table DICT_编制类别代码
  (
    代码 varchar2(50) not null,
    类别 varchar2(100) not null
  );
/*==============================================================*/
/* Table: DICT_薪酬来源代码                                       */
/*==============================================================*/
create table DICT_薪酬来源代码
  (
    代码 varchar2(50) not null,
    名称 varchar2(100) not null
  );
/*==============================================================*/
/* Table: DICT_岗位代码                                          */
/*==============================================================*/
create table DICT_岗位代码
  (
    代码 varchar2(50) not null,
    名称 varchar2(100) not null
  );

/*==============================================================*/
/*                        有待进一步评估                          */
/*==============================================================*/
/*==============================================================*/
/* Table: DICT_所在单位                                          */
/*==============================================================*/
create table DICT_所在单位
  (
    单位     varchar2(255) not null,
    岗位     varchar2(100) not null,
    科室     varchar2(100) not null,
    职工号    varchar2(10),
    任职单_单位 varchar2(255),
    constraint PK_DICT_所在单位 primary key (单位, 岗位, 科室)
  );
/*==============================================================*/
/* Table: DICT_籍贯                                              */
/*==============================================================*/
create table DICT_籍贯
  (
    代码 integer not null,
    名称 varchar2(255) not null,
    constraint PK_DICT_籍贯 primary key (代码, 名称)
  );  
/*==============================================================*/
/* Table: DICT_党政职务                                           */
/*==============================================================*/
create table DICT_党政职务
  (
    党政职务   varchar2(100) not null,
    党政职务级别 smallint not null,
    职工号    varchar2(10),
    constraint DICT_党政职务 primary key (党政职务, 党政职务级别)
  );  

/*==============================================================*/
/* Table: DICT_身份类别                                           */
/*==============================================================*/
create table DICT_身份类别
  (
    代码 varchar2(20) not null,
    名称 varchar2(500) not null
  ); 
/*==============================================================*/
/*                         冗余的表，考虑删除                      */
/*==============================================================*/
/*==============================================================*/
/* Table: DICT_专业, 跟学科专业代码重复                            */
/*==============================================================*/
create table DICT_专业
  (
    学科 varchar2(255) not null,
    专业 varchar2(255) not null
  );
