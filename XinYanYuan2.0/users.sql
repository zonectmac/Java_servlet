/*
Navicat MySQL Data Transfer

Source Server         : linhuaming
Source Server Version : 50087
Source Host           : localhost:3306
Source Database       : xinyanyuan

Target Server Type    : MYSQL
Target Server Version : 50087
File Encoding         : 65001

Date: 2015-10-22 10:49:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `_userId` int(11) NOT NULL auto_increment,
  `_userPhone` varchar(20) NOT NULL default '1300000000',
  `_password` varchar(20) NOT NULL default '123456',
  `_channel` varchar(50) NOT NULL,
  `_termno` varchar(50) NOT NULL,
  PRIMARY KEY  (`_userId`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', '13843838438', '123456', 'AND', '0x0e15a5c5f9');
INSERT INTO `users` VALUES ('4', '13015627931', '123123', 'ADR', 'f6-09-d8-1e-4b-e6');
INSERT INTO `users` VALUES ('5', '18081595850', '123123', 'AND', '192.168.1.10');
INSERT INTO `users` VALUES ('6', '18081595857', '123456', 'HUAWEI P7-L09', '10.0.2.2');
INSERT INTO `users` VALUES ('7', '18898859888', '123456', 'ADR', '52-54-00-12-34-56');
INSERT INTO `users` VALUES ('8', '15898859888', '123123', 'ADR', '46-85-e1-11-73-d8');
INSERT INTO `users` VALUES ('9', '13268101222', '123456', 'ADR', 'termno');
INSERT INTO `users` VALUES ('10', '18664305113', '888888', 'ADR', '46-85-e1-11-73-d8');
INSERT INTO `users` VALUES ('11', '1234567', '123456', 'ADR', 'termno');
INSERT INTO `users` VALUES ('12', '1', '12', 'ADR', 'termno');
INSERT INTO `users` VALUES ('13', '258369147', '123123', 'ADR', '58-02-03-04-05-07');
