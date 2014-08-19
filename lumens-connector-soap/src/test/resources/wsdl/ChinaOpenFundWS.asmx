<?xml version="1.0" encoding="utf-8"?>
<wsdl:definitions xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:tm="http://microsoft.com/wsdl/mime/textMatching/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/" xmlns:tns="http://WebXml.com.cn/" xmlns:s="http://www.w3.org/2001/XMLSchema" xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" targetNamespace="http://WebXml.com.cn/" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">
  <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;a href="http://www.webxml.com.cn/" target="_blank"&gt;WebXml.com.cn&lt;/a&gt; &lt;strong&gt;中国开放式基金数据 WEB 服务&lt;/strong&gt;，当日的最新开放式基金净值数据每天15：30以后及时更新。输出数据包括：证券代码、证券简称、单位净值、累计单位净值、前单位净值、净值涨跌额、净值增长率(%)、净值日期。此中国开放式基金数据WEB服务仅作为用户获取信息之目的，并不构成投资建议。&lt;a href="http://www.webxml.com.cn/" target="_blank"&gt;WebXml.com.cn&lt;/a&gt; 和/或其各供应商不为本页面提供信息的错误、残缺、延迟或因依靠此信息所采取的任何行动负责。&lt;strong&gt;市场有风险，投资需谨慎&lt;/strong&gt;。&lt;br /&gt;只有商业用户可获得此中国开放式基金数据Web Services的全部功能，若有需要测试、开发和使用请QQ：8409035或&lt;a href="http://www.webxml.com.cn/zh_cn/contact_us.aspx" target="_blank"&gt;联系我们&lt;/a&gt;获得用户ID。&lt;br /&gt;&lt;strong&gt;使用本站 WEB 服务请注明或链接本站：http://www.webxml.com.cn/ 感谢大家的支持&lt;/strong&gt;！&lt;br /&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
  <wsdl:types>
    <s:schema elementFormDefault="qualified" targetNamespace="http://WebXml.com.cn/">
      <s:element name="getFundCodeNameDataSet">
        <s:complexType />
      </s:element>
      <s:element name="getFundCodeNameDataSetResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="getFundCodeNameDataSetResult">
              <s:complexType>
                <s:sequence>
                  <s:element ref="s:schema" />
                  <s:any />
                </s:sequence>
              </s:complexType>
            </s:element>
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="getFundCodeNameString">
        <s:complexType />
      </s:element>
      <s:element name="getFundCodeNameStringResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="getFundCodeNameStringResult" type="tns:ArrayOfString" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="ArrayOfString">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="unbounded" name="string" nillable="true" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:element name="getOpenFundDataSet">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="userID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="getOpenFundDataSetResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="getOpenFundDataSetResult">
              <s:complexType>
                <s:sequence>
                  <s:element ref="s:schema" />
                  <s:any />
                </s:sequence>
              </s:complexType>
            </s:element>
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="getOpenFundString">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="userID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="getOpenFundStringResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="getOpenFundStringResult" type="tns:ArrayOfString" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="getOpenFundDataSetByCode">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="userID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="FundCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="getOpenFundDataSetByCodeResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="getOpenFundDataSetByCodeResult">
              <s:complexType>
                <s:sequence>
                  <s:element ref="s:schema" />
                  <s:any />
                </s:sequence>
              </s:complexType>
            </s:element>
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="getOpenFundStringByCode">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="userID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="FundCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="getOpenFundStringByCodeResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="getOpenFundStringByCodeResult" type="tns:ArrayOfString" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="DataSet" nillable="true">
        <s:complexType>
          <s:sequence>
            <s:element ref="s:schema" />
            <s:any />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="ArrayOfString" nillable="true" type="tns:ArrayOfString" />
    </s:schema>
  </wsdl:types>
  <wsdl:message name="getFundCodeNameDataSetSoapIn">
    <wsdl:part name="parameters" element="tns:getFundCodeNameDataSet" />
  </wsdl:message>
  <wsdl:message name="getFundCodeNameDataSetSoapOut">
    <wsdl:part name="parameters" element="tns:getFundCodeNameDataSetResponse" />
  </wsdl:message>
  <wsdl:message name="getFundCodeNameStringSoapIn">
    <wsdl:part name="parameters" element="tns:getFundCodeNameString" />
  </wsdl:message>
  <wsdl:message name="getFundCodeNameStringSoapOut">
    <wsdl:part name="parameters" element="tns:getFundCodeNameStringResponse" />
  </wsdl:message>
  <wsdl:message name="getOpenFundDataSetSoapIn">
    <wsdl:part name="parameters" element="tns:getOpenFundDataSet" />
  </wsdl:message>
  <wsdl:message name="getOpenFundDataSetSoapOut">
    <wsdl:part name="parameters" element="tns:getOpenFundDataSetResponse" />
  </wsdl:message>
  <wsdl:message name="getOpenFundStringSoapIn">
    <wsdl:part name="parameters" element="tns:getOpenFundString" />
  </wsdl:message>
  <wsdl:message name="getOpenFundStringSoapOut">
    <wsdl:part name="parameters" element="tns:getOpenFundStringResponse" />
  </wsdl:message>
  <wsdl:message name="getOpenFundDataSetByCodeSoapIn">
    <wsdl:part name="parameters" element="tns:getOpenFundDataSetByCode" />
  </wsdl:message>
  <wsdl:message name="getOpenFundDataSetByCodeSoapOut">
    <wsdl:part name="parameters" element="tns:getOpenFundDataSetByCodeResponse" />
  </wsdl:message>
  <wsdl:message name="getOpenFundStringByCodeSoapIn">
    <wsdl:part name="parameters" element="tns:getOpenFundStringByCode" />
  </wsdl:message>
  <wsdl:message name="getOpenFundStringByCodeSoapOut">
    <wsdl:part name="parameters" element="tns:getOpenFundStringByCodeResponse" />
  </wsdl:message>
  <wsdl:message name="getFundCodeNameDataSetHttpGetIn" />
  <wsdl:message name="getFundCodeNameDataSetHttpGetOut">
    <wsdl:part name="Body" element="tns:DataSet" />
  </wsdl:message>
  <wsdl:message name="getFundCodeNameStringHttpGetIn" />
  <wsdl:message name="getFundCodeNameStringHttpGetOut">
    <wsdl:part name="Body" element="tns:ArrayOfString" />
  </wsdl:message>
  <wsdl:message name="getOpenFundDataSetHttpGetIn">
    <wsdl:part name="userID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="getOpenFundDataSetHttpGetOut">
    <wsdl:part name="Body" element="tns:DataSet" />
  </wsdl:message>
  <wsdl:message name="getOpenFundStringHttpGetIn">
    <wsdl:part name="userID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="getOpenFundStringHttpGetOut">
    <wsdl:part name="Body" element="tns:ArrayOfString" />
  </wsdl:message>
  <wsdl:message name="getOpenFundDataSetByCodeHttpGetIn">
    <wsdl:part name="userID" type="s:string" />
    <wsdl:part name="FundCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="getOpenFundDataSetByCodeHttpGetOut">
    <wsdl:part name="Body" element="tns:DataSet" />
  </wsdl:message>
  <wsdl:message name="getOpenFundStringByCodeHttpGetIn">
    <wsdl:part name="userID" type="s:string" />
    <wsdl:part name="FundCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="getOpenFundStringByCodeHttpGetOut">
    <wsdl:part name="Body" element="tns:ArrayOfString" />
  </wsdl:message>
  <wsdl:message name="getFundCodeNameDataSetHttpPostIn" />
  <wsdl:message name="getFundCodeNameDataSetHttpPostOut">
    <wsdl:part name="Body" element="tns:DataSet" />
  </wsdl:message>
  <wsdl:message name="getFundCodeNameStringHttpPostIn" />
  <wsdl:message name="getFundCodeNameStringHttpPostOut">
    <wsdl:part name="Body" element="tns:ArrayOfString" />
  </wsdl:message>
  <wsdl:message name="getOpenFundDataSetHttpPostIn">
    <wsdl:part name="userID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="getOpenFundDataSetHttpPostOut">
    <wsdl:part name="Body" element="tns:DataSet" />
  </wsdl:message>
  <wsdl:message name="getOpenFundStringHttpPostIn">
    <wsdl:part name="userID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="getOpenFundStringHttpPostOut">
    <wsdl:part name="Body" element="tns:ArrayOfString" />
  </wsdl:message>
  <wsdl:message name="getOpenFundDataSetByCodeHttpPostIn">
    <wsdl:part name="userID" type="s:string" />
    <wsdl:part name="FundCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="getOpenFundDataSetByCodeHttpPostOut">
    <wsdl:part name="Body" element="tns:DataSet" />
  </wsdl:message>
  <wsdl:message name="getOpenFundStringByCodeHttpPostIn">
    <wsdl:part name="userID" type="s:string" />
    <wsdl:part name="FundCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="getOpenFundStringByCodeHttpPostOut">
    <wsdl:part name="Body" element="tns:ArrayOfString" />
  </wsdl:message>
  <wsdl:portType name="ChinaOpenFundWSSoap">
    <wsdl:operation name="getFundCodeNameDataSet">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;获得中国开放式基金的基金代号和基金名称 DataSet。&lt;/h3&gt;&lt;p&gt;输入参数：无；返回数据：DataSet，结构为：Item(FundCode)=基金代号，Ietm(FundName)=基金名称。&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getFundCodeNameDataSetSoapIn" />
      <wsdl:output message="tns:getFundCodeNameDataSetSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="getFundCodeNameString">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;获得中国开放式基金的基金代号和基金名称 String()。&lt;/h3&gt;&lt;p&gt;输入参数：无；返回数据：一个一维字符串数组 String()，结构为：基金代号@基金名称。&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getFundCodeNameStringSoapIn" />
      <wsdl:output message="tns:getFundCodeNameStringSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSet">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;获得全部中国开放式基金数据 DataSet。（免费用户只能获得最新 10 条数据）&lt;/h3&gt;&lt;p&gt;输入参数：userID = 商业用户ID；返回数据：DataSet，结构为：Item(FundCode)=基金代号，Ietm(FundName)=基金名称，Ietm(NetWorth_1)=前单位净值，Ietm(NetWorth_2)=单位净值，Ietm(NetWorth_3)=累计单位净值，Ietm(WorthUp)=净值涨跌额，Ietm(WorthPercent)=净值增长率（%），Ietm(WorthDate)=净值日期，Ietm(ModifyDate)=数据更新时间。&lt;strong&gt;免费用户不需要输入 userID 参数&lt;/strong&gt;&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getOpenFundDataSetSoapIn" />
      <wsdl:output message="tns:getOpenFundDataSetSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="getOpenFundString">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;获得全部中国开放式基金数据 String()。（免费用户只能获得最新 10 条数据）&lt;/h3&gt;&lt;p&gt;输入参数：userID = 商业用户ID；返回数据：一个一维字符串数组 String()，结构为：基金代号@基金名称@前单位净值@单位净值，@累计单位净值@净值涨跌额@净值增长率（%）@净值日期@数据更新时间。&lt;strong&gt;免费用户不需要输入 userID 参数&lt;/strong&gt;&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getOpenFundStringSoapIn" />
      <wsdl:output message="tns:getOpenFundStringSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSetByCode">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;通过开放式基金代号查询基金数据 DataSet。（免费用户不能使用）&lt;/h3&gt;&lt;p&gt;输入参数：userID = 商业用户ID；返回数据：DataSet，结构为：同获得全部中国开放式基金数据 DataSet。&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getOpenFundDataSetByCodeSoapIn" />
      <wsdl:output message="tns:getOpenFundDataSetByCodeSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="getOpenFundStringByCode">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;通过开放式基金代号查询基金数据 String()。（免费用户不能使用）&lt;/h3&gt;&lt;p&gt;输入参数：userID = 商业用户ID；返回数据：一个一维字符串数组 String()，结构为：获得全部中国开放式基金数据 String()。&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getOpenFundStringByCodeSoapIn" />
      <wsdl:output message="tns:getOpenFundStringByCodeSoapOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:portType name="ChinaOpenFundWSHttpGet">
    <wsdl:operation name="getFundCodeNameDataSet">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;获得中国开放式基金的基金代号和基金名称 DataSet。&lt;/h3&gt;&lt;p&gt;输入参数：无；返回数据：DataSet，结构为：Item(FundCode)=基金代号，Ietm(FundName)=基金名称。&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getFundCodeNameDataSetHttpGetIn" />
      <wsdl:output message="tns:getFundCodeNameDataSetHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="getFundCodeNameString">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;获得中国开放式基金的基金代号和基金名称 String()。&lt;/h3&gt;&lt;p&gt;输入参数：无；返回数据：一个一维字符串数组 String()，结构为：基金代号@基金名称。&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getFundCodeNameStringHttpGetIn" />
      <wsdl:output message="tns:getFundCodeNameStringHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSet">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;获得全部中国开放式基金数据 DataSet。（免费用户只能获得最新 10 条数据）&lt;/h3&gt;&lt;p&gt;输入参数：userID = 商业用户ID；返回数据：DataSet，结构为：Item(FundCode)=基金代号，Ietm(FundName)=基金名称，Ietm(NetWorth_1)=前单位净值，Ietm(NetWorth_2)=单位净值，Ietm(NetWorth_3)=累计单位净值，Ietm(WorthUp)=净值涨跌额，Ietm(WorthPercent)=净值增长率（%），Ietm(WorthDate)=净值日期，Ietm(ModifyDate)=数据更新时间。&lt;strong&gt;免费用户不需要输入 userID 参数&lt;/strong&gt;&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getOpenFundDataSetHttpGetIn" />
      <wsdl:output message="tns:getOpenFundDataSetHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="getOpenFundString">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;获得全部中国开放式基金数据 String()。（免费用户只能获得最新 10 条数据）&lt;/h3&gt;&lt;p&gt;输入参数：userID = 商业用户ID；返回数据：一个一维字符串数组 String()，结构为：基金代号@基金名称@前单位净值@单位净值，@累计单位净值@净值涨跌额@净值增长率（%）@净值日期@数据更新时间。&lt;strong&gt;免费用户不需要输入 userID 参数&lt;/strong&gt;&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getOpenFundStringHttpGetIn" />
      <wsdl:output message="tns:getOpenFundStringHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSetByCode">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;通过开放式基金代号查询基金数据 DataSet。（免费用户不能使用）&lt;/h3&gt;&lt;p&gt;输入参数：userID = 商业用户ID；返回数据：DataSet，结构为：同获得全部中国开放式基金数据 DataSet。&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getOpenFundDataSetByCodeHttpGetIn" />
      <wsdl:output message="tns:getOpenFundDataSetByCodeHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="getOpenFundStringByCode">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;通过开放式基金代号查询基金数据 String()。（免费用户不能使用）&lt;/h3&gt;&lt;p&gt;输入参数：userID = 商业用户ID；返回数据：一个一维字符串数组 String()，结构为：获得全部中国开放式基金数据 String()。&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getOpenFundStringByCodeHttpGetIn" />
      <wsdl:output message="tns:getOpenFundStringByCodeHttpGetOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:portType name="ChinaOpenFundWSHttpPost">
    <wsdl:operation name="getFundCodeNameDataSet">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;获得中国开放式基金的基金代号和基金名称 DataSet。&lt;/h3&gt;&lt;p&gt;输入参数：无；返回数据：DataSet，结构为：Item(FundCode)=基金代号，Ietm(FundName)=基金名称。&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getFundCodeNameDataSetHttpPostIn" />
      <wsdl:output message="tns:getFundCodeNameDataSetHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="getFundCodeNameString">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;获得中国开放式基金的基金代号和基金名称 String()。&lt;/h3&gt;&lt;p&gt;输入参数：无；返回数据：一个一维字符串数组 String()，结构为：基金代号@基金名称。&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getFundCodeNameStringHttpPostIn" />
      <wsdl:output message="tns:getFundCodeNameStringHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSet">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;获得全部中国开放式基金数据 DataSet。（免费用户只能获得最新 10 条数据）&lt;/h3&gt;&lt;p&gt;输入参数：userID = 商业用户ID；返回数据：DataSet，结构为：Item(FundCode)=基金代号，Ietm(FundName)=基金名称，Ietm(NetWorth_1)=前单位净值，Ietm(NetWorth_2)=单位净值，Ietm(NetWorth_3)=累计单位净值，Ietm(WorthUp)=净值涨跌额，Ietm(WorthPercent)=净值增长率（%），Ietm(WorthDate)=净值日期，Ietm(ModifyDate)=数据更新时间。&lt;strong&gt;免费用户不需要输入 userID 参数&lt;/strong&gt;&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getOpenFundDataSetHttpPostIn" />
      <wsdl:output message="tns:getOpenFundDataSetHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="getOpenFundString">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;获得全部中国开放式基金数据 String()。（免费用户只能获得最新 10 条数据）&lt;/h3&gt;&lt;p&gt;输入参数：userID = 商业用户ID；返回数据：一个一维字符串数组 String()，结构为：基金代号@基金名称@前单位净值@单位净值，@累计单位净值@净值涨跌额@净值增长率（%）@净值日期@数据更新时间。&lt;strong&gt;免费用户不需要输入 userID 参数&lt;/strong&gt;&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getOpenFundStringHttpPostIn" />
      <wsdl:output message="tns:getOpenFundStringHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSetByCode">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;通过开放式基金代号查询基金数据 DataSet。（免费用户不能使用）&lt;/h3&gt;&lt;p&gt;输入参数：userID = 商业用户ID；返回数据：DataSet，结构为：同获得全部中国开放式基金数据 DataSet。&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getOpenFundDataSetByCodeHttpPostIn" />
      <wsdl:output message="tns:getOpenFundDataSetByCodeHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="getOpenFundStringByCode">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;br /&gt;&lt;h3&gt;通过开放式基金代号查询基金数据 String()。（免费用户不能使用）&lt;/h3&gt;&lt;p&gt;输入参数：userID = 商业用户ID；返回数据：一个一维字符串数组 String()，结构为：获得全部中国开放式基金数据 String()。&lt;/p&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
      <wsdl:input message="tns:getOpenFundStringByCodeHttpPostIn" />
      <wsdl:output message="tns:getOpenFundStringByCodeHttpPostOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:binding name="ChinaOpenFundWSSoap" type="tns:ChinaOpenFundWSSoap">
    <soap:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="getFundCodeNameDataSet">
      <soap:operation soapAction="http://WebXml.com.cn/getFundCodeNameDataSet" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getFundCodeNameString">
      <soap:operation soapAction="http://WebXml.com.cn/getFundCodeNameString" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSet">
      <soap:operation soapAction="http://WebXml.com.cn/getOpenFundDataSet" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundString">
      <soap:operation soapAction="http://WebXml.com.cn/getOpenFundString" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSetByCode">
      <soap:operation soapAction="http://WebXml.com.cn/getOpenFundDataSetByCode" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundStringByCode">
      <soap:operation soapAction="http://WebXml.com.cn/getOpenFundStringByCode" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="ChinaOpenFundWSSoap12" type="tns:ChinaOpenFundWSSoap">
    <soap12:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="getFundCodeNameDataSet">
      <soap12:operation soapAction="http://WebXml.com.cn/getFundCodeNameDataSet" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getFundCodeNameString">
      <soap12:operation soapAction="http://WebXml.com.cn/getFundCodeNameString" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSet">
      <soap12:operation soapAction="http://WebXml.com.cn/getOpenFundDataSet" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundString">
      <soap12:operation soapAction="http://WebXml.com.cn/getOpenFundString" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSetByCode">
      <soap12:operation soapAction="http://WebXml.com.cn/getOpenFundDataSetByCode" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundStringByCode">
      <soap12:operation soapAction="http://WebXml.com.cn/getOpenFundStringByCode" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="ChinaOpenFundWSHttpGet" type="tns:ChinaOpenFundWSHttpGet">
    <http:binding verb="GET" />
    <wsdl:operation name="getFundCodeNameDataSet">
      <http:operation location="/getFundCodeNameDataSet" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getFundCodeNameString">
      <http:operation location="/getFundCodeNameString" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSet">
      <http:operation location="/getOpenFundDataSet" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundString">
      <http:operation location="/getOpenFundString" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSetByCode">
      <http:operation location="/getOpenFundDataSetByCode" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundStringByCode">
      <http:operation location="/getOpenFundStringByCode" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="ChinaOpenFundWSHttpPost" type="tns:ChinaOpenFundWSHttpPost">
    <http:binding verb="POST" />
    <wsdl:operation name="getFundCodeNameDataSet">
      <http:operation location="/getFundCodeNameDataSet" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getFundCodeNameString">
      <http:operation location="/getFundCodeNameString" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSet">
      <http:operation location="/getOpenFundDataSet" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundString">
      <http:operation location="/getOpenFundString" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundDataSetByCode">
      <http:operation location="/getOpenFundDataSetByCode" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="getOpenFundStringByCode">
      <http:operation location="/getOpenFundStringByCode" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:service name="ChinaOpenFundWS">
    <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;a href="http://www.webxml.com.cn/" target="_blank"&gt;WebXml.com.cn&lt;/a&gt; &lt;strong&gt;中国开放式基金数据 WEB 服务&lt;/strong&gt;，当日的最新开放式基金净值数据每天15：30以后及时更新。输出数据包括：证券代码、证券简称、单位净值、累计单位净值、前单位净值、净值涨跌额、净值增长率(%)、净值日期。此中国开放式基金数据WEB服务仅作为用户获取信息之目的，并不构成投资建议。&lt;a href="http://www.webxml.com.cn/" target="_blank"&gt;WebXml.com.cn&lt;/a&gt; 和/或其各供应商不为本页面提供信息的错误、残缺、延迟或因依靠此信息所采取的任何行动负责。&lt;strong&gt;市场有风险，投资需谨慎&lt;/strong&gt;。&lt;br /&gt;只有商业用户可获得此中国开放式基金数据Web Services的全部功能，若有需要测试、开发和使用请QQ：8409035或&lt;a href="http://www.webxml.com.cn/zh_cn/contact_us.aspx" target="_blank"&gt;联系我们&lt;/a&gt;获得用户ID。&lt;br /&gt;&lt;strong&gt;使用本站 WEB 服务请注明或链接本站：http://www.webxml.com.cn/ 感谢大家的支持&lt;/strong&gt;！&lt;br /&gt;&lt;br /&gt;&amp;nbsp;</wsdl:documentation>
    <!--<wsdl:port name="ChinaOpenFundWSSoap" binding="tns:ChinaOpenFundWSSoap">
      <soap:address location="http://www.webxml.com.cn/WebServices/ChinaOpenFundWS.asmx" />
    </wsdl:port>  -->
    <wsdl:port name="ChinaOpenFundWSSoap12" binding="tns:ChinaOpenFundWSSoap12">
      <soap12:address location="http://www.webxml.com.cn/WebServices/ChinaOpenFundWS.asmx" />
    </wsdl:port>
    <!--<wsdl:port name="ChinaOpenFundWSHttpGet" binding="tns:ChinaOpenFundWSHttpGet">
      <http:address location="http://www.webxml.com.cn/WebServices/ChinaOpenFundWS.asmx" />
    </wsdl:port>
    <wsdl:port name="ChinaOpenFundWSHttpPost" binding="tns:ChinaOpenFundWSHttpPost">
      <http:address location="http://www.webxml.com.cn/WebServices/ChinaOpenFundWS.asmx" />
    </wsdl:port> -->
  </wsdl:service>
</wsdl:definitions>