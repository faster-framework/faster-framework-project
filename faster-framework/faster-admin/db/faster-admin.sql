/*
 Navicat MySQL Data Transfer

 Source Server         :
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           :
 Source Schema         :

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 16/08/2018 15:46:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(64) NOT NULL COMMENT '权限名称',
  `code` varchar(64) NOT NULL COMMENT '权限code',
  `parent_id` bigint(20) NOT NULL COMMENT '父id',
  `parent_ids` longtext NOT NULL COMMENT '父id列表（排序使用）',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '最后更新人',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_date` datetime NOT NULL COMMENT '最后更新时间',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `remark` varchar(10240) DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（0.,否 1.是）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_permission` VALUES (37614050900705280, '权限管理', 'permissions:manage', 0, '[0]', 32907538873712640, 32907538873712640, '2018-06-11 17:17:46', '2018-06-11 17:17:46', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37614233789136896, '权限列表', 'permissions:list', 37614050900705280, '[0],[37614050900705280]', 32907538873712640, 32907538873712640, '2018-06-11 17:18:30', '2018-06-11 17:18:30', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37614578846138368, '权限添加', 'permissions:add', 37614050900705280, '[0],[37614050900705280]', 32907538873712640, 32907538873712640, '2018-06-11 17:19:52', '2018-06-11 17:19:52', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37614579462701056, '权限删除', 'permissions:delete', 37614050900705280, '[0],[37614050900705280]', 32907538873712640, 32907538873712640, '2018-06-11 17:19:52', '2018-06-11 17:19:52', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37614579856965632, '权限编辑', 'permissions:modify', 37614050900705280, '[0],[37614050900705280]', 32907538873712640, 32907538873712640, '2018-06-11 17:19:52', '2018-06-11 17:19:52', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37614580247035904, '权限详情', 'permissions:info', 37614050900705280, '[0],[37614050900705280]', 32907538873712640, 32907538873712640, '2018-06-11 17:19:53', '2018-06-11 17:19:53', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37615037732356096, '角色管理', 'roles:manage', 0, '[0]', 32907538873712640, 0, '2018-06-11 17:21:42', '2018-07-23 17:40:06', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37615359716491264, '角色列表', 'roles:list', 37615037732356096, '[0],[37615037732356096]', 32907538873712640, 32907538873712640, '2018-06-11 17:22:58', '2018-06-11 17:22:58', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37615360337248256, '角色添加', 'roles:add', 37615037732356096, '[0],[37615037732356096]', 32907538873712640, 32907538873712640, '2018-06-11 17:22:59', '2018-06-11 17:22:59', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37615360702152704, '角色删除', 'roles:delete', 37615037732356096, '[0],[37615037732356096]', 32907538873712640, 32907538873712640, '2018-06-11 17:22:59', '2018-06-11 17:22:59', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37615361062862848, '角色编辑', 'roles:modify', 37615037732356096, '[0],[37615037732356096]', 32907538873712640, 32907538873712640, '2018-06-11 17:22:59', '2018-06-11 17:22:59', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37615361457127424, '角色详情', 'roles:info', 37615037732356096, '[0],[37615037732356096]', 32907538873712640, 32907538873712640, '2018-06-11 17:22:59', '2018-06-11 17:22:59', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37615593091760128, '用户管理', 'users:manage', 0, '[0]', 32907538873712640, 32907538873712640, '2018-06-11 17:23:54', '2018-06-11 17:23:54', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37615786340122624, '用户列表', 'users:list', 37615593091760128, '[0],[37615593091760128]', 32907538873712640, 32907538873712640, '2018-06-11 17:24:40', '2018-06-11 17:24:40', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37615787048960000, '用户添加', 'users:add', 37615593091760128, '[0],[37615593091760128]', 32907538873712640, 32907538873712640, '2018-06-11 17:24:40', '2018-06-11 17:24:40', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37615787480973312, '用户删除', 'users:delete', 37615593091760128, '[0],[37615593091760128]', 32907538873712640, 32907538873712640, '2018-06-11 17:24:40', '2018-06-11 17:24:40', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37615787912986624, '用户编辑', 'users:modify', 37615593091760128, '[0],[37615593091760128]', 32907538873712640, 32907538873712640, '2018-06-11 17:24:41', '2018-06-11 17:24:41', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37615788382748672, '用户详情', 'users:info', 37615593091760128, '[0],[37615593091760128]', 32907538873712640, 32907538873712640, '2018-06-11 17:24:41', '2018-06-11 17:24:41', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37616009674227712, '用户修改密码', 'users:password:change', 37615593091760128, '[0],[37615593091760128]', 32907538873712640, 32907538873712640, '2018-06-11 17:25:33', '2018-06-11 17:25:33', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37616123360837632, '用户重置密码', 'users:password:reset', 37615593091760128, '[0],[37615593091760128]', 32907538873712640, 32907538873712640, '2018-06-11 17:26:00', '2018-06-11 17:26:00', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37616523644239872, '用户选择权限', 'roles:permissions:choose', 37615037732356096, '[0],[37615037732356096]', 32907538873712640, 32907538873712640, '2018-06-11 17:27:36', '2018-06-11 17:27:36', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (37616673557053440, '用户选择角色', 'users:roles:choose', 37615593091760128, '[0],[37615593091760128]', 32907538873712640, 32907538873712640, '2018-06-11 17:28:12', '2018-06-11 17:28:12', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (102551194987134976, '用户角色列表', 'users:roles:list', 37615593091760128, '[0],[37615593091760128]', 0, 0, '2018-12-07 21:54:48', '2018-12-07 21:54:48', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (43409414580862976, '角色权限列表', 'roles:permissions:list', 37615037732356096, '[0],[37615037732356096]', 32907538873712640, 32907538873712640, '2018-06-11 17:27:36', '2018-06-11 17:27:36', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (47660043591483392, '权限树', 'permissions:tree', 37614050900705280, '[0],[37614050900705280]', 0, 0, '2018-07-09 10:36:58', '2018-07-09 10:36:58', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (221334243177922560, '字典管理', 'sysDict:manage', 0, '[0]', 0, 0, '2019-10-31 16:36:14', '2019-10-31 16:36:14', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (221334624826032128, '字典列表', 'sysDict:list', 221334243177922560, '[0],[221334243177922560]', 0, 0, '2019-10-31 16:37:45', '2019-10-31 16:37:45', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (221334692169777152, '字典添加', 'sysDict:add', 221334243177922560, '[0],[221334243177922560]', 0, 0, '2019-10-31 16:38:01', '2019-10-31 16:38:01', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (221334755814146048, '字典编辑', 'sysDict:modify', 221334243177922560, '[0],[221334243177922560]', 0, 0, '2019-10-31 16:38:16', '2019-10-31 16:38:16', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (221334915914924032, '字典删除', 'sysDict:delete', 221334243177922560, '[0],[221334243177922560]', 0, 0, '2019-10-31 16:38:54', '2019-10-31 16:38:54', 0, NULL, 0);
INSERT INTO `sys_permission` VALUES (221334992398057472, '字典详情', 'sysDict:info', 221334243177922560, '[0],[221334243177922560]', 0, 0, '2019-10-31 16:39:12', '2019-10-31 16:39:12', 0, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(64) NOT NULL COMMENT '角色名称',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '最后更新人',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_date` datetime NOT NULL COMMENT '最后更新时间',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `remark` varchar(10240) DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（0.,否 1.是）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (0, '超级管理员', NULL, NULL, '2018-05-29 17:35:47', '2018-05-29 17:35:47', 0, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `permission_id` bigint(20) NOT NULL COMMENT '权限id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '最后更新人',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_date` datetime NOT NULL COMMENT '最后更新时间',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `remark` varchar(10240) DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（0.,否 1.是）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色权限关系表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `account` varchar(64) NOT NULL COMMENT '账号',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `name` varchar(64) NOT NULL COMMENT '姓名',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '最后更新人',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_date` datetime NOT NULL COMMENT '最后更新时间',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `remark` varchar(10240) DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（0.,否 1.是）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account` (`account`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (0, 'admin', '21232f297a57a5a743894a0e4a801fc3', '超级管理员', NULL, NULL, '2018-05-29 17:35:46', '2018-05-29 17:35:46', 0, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '最后更新人',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_date` datetime NOT NULL COMMENT '最后更新时间',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `remark` varchar(10240) DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（0.,否 1.是）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户角色关系表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (0, 0, 0, NULL, NULL, '2018-06-11 17:47:05', '2018-06-11 17:47:08', 0, NULL, 0);
COMMIT;


-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL,
  `name` varchar(128) DEFAULT NULL COMMENT '字典key',
  `type` varchar(128) DEFAULT NULL COMMENT '类型',
  `dict_value` longtext COMMENT '字典值',
  `show_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '展示状态（0.不展示 1.展示）',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '最后更新人',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_date` datetime NOT NULL COMMENT '最后更新时间',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（0.,否 1.是）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`) USING BTREE,
  KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典表';


SET FOREIGN_KEY_CHECKS = 1;
