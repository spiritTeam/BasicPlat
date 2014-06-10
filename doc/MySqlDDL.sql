#创建User表
drop table plat_user;
CREATE TABLE plat_user(
  id        varchar(36) NOT NULL  COMMENT 'ID主键,可支持UUID' ,
  loginName varchar(100) NOT NULL COMMENT '用户登录称' ,
  userName  varchar(100) NOT NULL COMMENT '用户名' ,
  password  varchar(100)          COMMENT '密码' ,
  PRIMARY KEY (id)
);
ALTER TABLE plat_user ADD UNIQUE INDEX I_PU_PASSWORD (loginName);

insert into plat_user(id, loginName , userName, password) values('1001', 'test001', 'test001','123456');
insert into plat_user(id, loginName , userName, password) values('1002', 'test002', 'test002','123456');
insert into plat_user(id, loginName , userName, password) values('1003', 'test003', 'test003','123456');
insert into plat_user(id, loginName , userName, password) values('1004', 'test004', 'test004','123456');

#创建Module表
drop table plat_module;

CREATE TABLE plat_module (
  id          varchar(36) NOT NULL             COMMENT 'ID主键,可支持UUID' ,
  pId         varchar(36) NOT NULL DEFAULT '0' COMMENT '父ID:本表中的ID(外键)，若是第一级模块，此值为0' ,
  sort        int(4) NULL                      COMMENT '排序号:每级支持99999个结点' ,
  isValidate  int(1) NULL DEFAULT 1            COMMENT '是否显示：1有效；2无效' ,
  moduleType  int(2) NULL DEFAULT 2            COMMENT ' 模块类型:1：系统模块2：用户定义模块；可扩充定义' ,
  moduleName  varchar(200) NULL DEFAULT NULL   COMMENT '模块名称' ,
  displayName varchar(200) NULL DEFAULT NULL   COMMENT '模块显示名称' ,
  url         varchar(400) NULL                COMMENT '功能链接' ,
  levels      int(4) NULL                      COMMENT '层数:从1开始，根结点层数为1' ,
  style       int(2) NULL                      COMMENT '样式:' ,
  icon        varchar(400) NULL                COMMENT '图标:图标的URL地址/或Css名称' ,
  descn       varchar(2000) NULL               COMMENT '说明' ,
  PRIMARY KEY (id)
);

insert into plat_module(id,pId,sort,isValidate,moduleType,moduleName,displayName,url,levels,style,icon,descn)
values('1000', '0', 1, 1, 2, 'demo', '功能演示', null, 1, null, null, '功能演示大菜单，因此没有url');

insert into plat_module(id,pId,sort,isValidate,moduleType,moduleName,displayName,url,levels,style,icon,descn)
values('1001', '1000', 3, 1, 2, 'misDemo', 'MIS演示', null, 2, null, null, 'MIS演示');
insert into plat_module(id,pId,sort,isValidate,moduleType,moduleName,displayName,url,levels,style,icon,descn)
values('1002', '1000', 1, 1, 2, 'gisDemo', 'GIS演示', null, 2, null, null, 'GIS演示');
insert into plat_module(id,pId,sort,isValidate,moduleType,moduleName,displayName,url,levels,style,icon,descn)
values('1003', '1000', 2, 1, 2, 'padDemo', 'PAD演示', '/SimulPad.do', 2, null, 'PAD.jpg', 'PAD演示，用.do测试');

insert into plat_module(id,pId,sort,isValidate,moduleType,moduleName,displayName,url,levels,style,icon,descn)
values('1004', '1001', 1, 1, 2, 'showWindow', '窗口功能', '/misDemo/dialogANDwin.jsp', 3, null, null, '窗口功能演示');
insert into plat_module(id,pId,sort,isValidate,moduleType,moduleName,displayName,url,levels,style,icon,descn)
values('1005', '1001', 2, 1, 2, 'misDemoMemo', '对框架功能的说明', '/misDemo/memo.jsp', 3, null, null, '对框架功能的说明');

insert into plat_module(id,pId,sort,isValidate,moduleType,moduleName,displayName,url,levels,style,icon,descn)
values('1006', '0', 1, 1, 2, 'portal', 'portlet模块测试', null, 1, null, null, '门户模块测试');
insert into plat_module(id,pId,sort,isValidate,moduleType,moduleName,displayName,url,levels,style,icon,descn)
values('1007', '1007', 1, 1, 2, 'portal1', 'portlet1', '/portDemo/portlet1.do', 2, null, null, '小入口1，采用.do');
insert into plat_module(id,pId,sort,isValidate,moduleType,moduleName,displayName,url,levels,style,icon,descn)
values('1008', '1007', 1, 1, 2, 'portal1', 'portlet2', '/portDemo/portlet2.jsp', 2, null, null, '小入口2，采用jsp');

insert into plat_module(id,pId,sort,isValidate,moduleType,moduleName,displayName,url,levels,style,icon,descn)
values('1009', '2007', 1, 1, 2, 'error1', '错误结点', null, 1, null, null, '造一个错误结点，以便测试');

insert into plat_module(id,pId,sort,isValidate,moduleType,moduleName,displayName,url,levels,style,icon,descn)
values('1010', '0', 1, 1, 2, 'SystemManager', '系统管理', null, 1, null, null, '功能演示大菜单，因此没有url');

insert into plat_module(id,pId,sort,isValidate,moduleType,moduleName,displayName,url,levels,style,icon,descn)
values('1011', '1010', 1, 1, 2, 'ModuleManager', '模块管理', '/moduleManage/moduleMainPage.jsp', 2, null, null, '窗口功能演示');
insert into plat_module(id,pId,sort,isValidate,moduleType,moduleName,displayName,url,levels,style,icon,descn)
values('1012', '1011', 1, 1, 2, 'ModuleManager', '模块管理', '/moduleManage/moduleMainPage.jsp', 3, null, null, '模块管理');
