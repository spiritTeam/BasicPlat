#创建Module表
drop table `plat_module`;

CREATE TABLE `plat_module` (
  `id`  varchar(36) NOT NULL COMMENT 'ID主键,可支持UUID' ,
  `pId`  varchar(36) NOT NULL DEFAULT '0' COMMENT '父ID:本表中的ID(外键)，若是第一级模块，此值为0' ,
  `sort`  int(4) NULL COMMENT '排序号:每级支持99999个结点' ,
  `isValidate`  int(1) NULL DEFAULT 1 COMMENT '是否显示：1有效；2无效' ,
  `moduleTypes`  varchar(3) NULL DEFAULT '100' COMMENT ' 模块类型:100：系统模块200：用户定义模块；可扩充定义' ,
  `moduleName`  varchar(200) NULL DEFAULT NULL COMMENT '模块名称' ,
  `displayName`  varchar(200) NULL DEFAULT NULL COMMENT '模块显示名称' ,
  `url`  varchar(400) NULL COMMENT '功能链接' ,
  `levels`  int(4) NULL COMMENT '层数:从1开始，根结点层数为1' ,
  `style`  int(2) NULL COMMENT '样式:' ,
  `icon`  varchar(400) NULL COMMENT '图标:图标的URL地址/或Css名称' ,
  `descn`  varchar(2000) NULL COMMENT '说明' ,
  PRIMARY KEY (`id`)
);
