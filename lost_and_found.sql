/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : 127.0.0.1:3306
 Source Schema         : lost_and_found

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 17/05/2023 22:47:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods_classify
-- ----------------------------
DROP TABLE IF EXISTS `goods_classify`;
CREATE TABLE `goods_classify`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_classify
-- ----------------------------

-- ----------------------------
-- Table structure for lose_answer
-- ----------------------------
DROP TABLE IF EXISTS `lose_answer`;
CREATE TABLE `lose_answer`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pick_up_question_id` int(11) NOT NULL COMMENT '发布拾主提问表的id（发布拾主提问表的外键）',
  `answer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对拾物提问的回答',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lose_answer_pick_up_question_id`(`pick_up_question_id`) USING BTREE,
  CONSTRAINT `fk_lose_answer_pick_up_question_id` FOREIGN KEY (`pick_up_question_id`) REFERENCES `pick_up_question` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lose_answer
-- ----------------------------

-- ----------------------------
-- Table structure for lose_classify
-- ----------------------------
DROP TABLE IF EXISTS `lose_classify`;
CREATE TABLE `lose_classify`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `lose_goods_id` int(11) NOT NULL COMMENT '发布失物信息表的id（发布失物信息表的外键）',
  `classify_id` int(11) NOT NULL COMMENT '物品分类表的id（物品分类表的外键）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lose_classify_classify_id`(`classify_id`) USING BTREE,
  INDEX `fk_lose_classify_lose_goods_id`(`lose_goods_id`) USING BTREE,
  CONSTRAINT `fk_lose_classify_classify_id` FOREIGN KEY (`classify_id`) REFERENCES `goods_classify` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_lose_classify_lose_goods_id` FOREIGN KEY (`lose_goods_id`) REFERENCES `lose_goods` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lose_classify
-- ----------------------------

-- ----------------------------
-- Table structure for lose_goods
-- ----------------------------
DROP TABLE IF EXISTS `lose_goods`;
CREATE TABLE `lose_goods`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `uid` int(11) NOT NULL COMMENT '发布者ID；用户表丢失物品发布的id（用户表的外键）',
  `goods_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '丢失物品的名称，1-20位的非空字段',
  `goods_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '简述丢失的物品信息',
  `goods_pos` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '简述丢失的物品的大致位置',
  `start_time` datetime NOT NULL COMMENT '丢失物品的大致开始时间',
  `end_time` datetime NOT NULL COMMENT '丢失物品的大致结束时间',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lose_goods_uid`(`uid`) USING BTREE,
  CONSTRAINT `fk_lose_goods_uid` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lose_goods
-- ----------------------------
INSERT INTO `lose_goods` VALUES (1, 1, '魅族手机', '在主教101捡到一部魅族手机，型号为15pro', '主教', '2023-05-15 17:58:00', '2023-05-15 18:30:00', '2023-05-15 17:58:00', '2023-05-15 17:58:00');
INSERT INTO `lose_goods` VALUES (2, 1, '外卖', '在8栋楼下捡到的外卖', '8栋', '2023-05-15 17:58:00', '2023-05-15 18:30:00', '2023-05-15 17:58:00', '2023-05-15 17:58:00');
INSERT INTO `lose_goods` VALUES (3, 2, '三星S7手机', '在操场正门门口，最左侧第三个篮球场捡到', '操场的篮球场', '2023-05-15 17:58:00', '2023-05-15 18:30:00', '2023-05-15 17:58:00', '2023-05-15 17:58:00');

-- ----------------------------
-- Table structure for lose_image
-- ----------------------------
DROP TABLE IF EXISTS `lose_image`;
CREATE TABLE `lose_image`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `lose_goods_id` int(11) NOT NULL COMMENT '发布失物信息表的id（发布失物信息表的外键）',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '失物有关信息的图片链接',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lose_image_lose_goods_id`(`lose_goods_id`) USING BTREE,
  CONSTRAINT `fk_lose_image_lose_goods_id` FOREIGN KEY (`lose_goods_id`) REFERENCES `lose_goods` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lose_image
-- ----------------------------
INSERT INTO `lose_image` VALUES (1, 1, '/images/1/-1569464852.png');
INSERT INTO `lose_image` VALUES (2, 2, '/images/1/-700973502.png');
INSERT INTO `lose_image` VALUES (3, 3, '/images/2/1577278696002.jpg');
INSERT INTO `lose_image` VALUES (4, 3, '/images/2/1563443670343.jpg');

-- ----------------------------
-- Table structure for lose_tag
-- ----------------------------
DROP TABLE IF EXISTS `lose_tag`;
CREATE TABLE `lose_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `lose_goods_id` int(11) NOT NULL COMMENT '发布失物信息表的id（发布失物信息表的外键）',
  `tag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '失物有关信息的标签',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lose_tag_lose_goods_id`(`lose_goods_id`) USING BTREE,
  CONSTRAINT `fk_lose_tag_lose_goods_id` FOREIGN KEY (`lose_goods_id`) REFERENCES `lose_goods` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lose_tag
-- ----------------------------

-- ----------------------------
-- Table structure for pick_up_classify
-- ----------------------------
DROP TABLE IF EXISTS `pick_up_classify`;
CREATE TABLE `pick_up_classify`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pick_up_goods_id` int(11) NOT NULL COMMENT '发布拾物信息表的id（发布拾物信息表的外键）',
  `classify_id` int(11) NOT NULL COMMENT '物品分类表的id（物品分类表的外键）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_pick_up_classify_classify_id`(`classify_id`) USING BTREE,
  INDEX `fk_pick_up_classify_pick_up_goods_id`(`pick_up_goods_id`) USING BTREE,
  CONSTRAINT `fk_pick_up_classify_classify_id` FOREIGN KEY (`classify_id`) REFERENCES `goods_classify` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_pick_up_classify_pick_up_goods_id` FOREIGN KEY (`pick_up_goods_id`) REFERENCES `pick_up_goods` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pick_up_classify
-- ----------------------------

-- ----------------------------
-- Table structure for pick_up_goods
-- ----------------------------
DROP TABLE IF EXISTS `pick_up_goods`;
CREATE TABLE `pick_up_goods`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `uid` int(11) NOT NULL COMMENT '发布者ID；用户表捡到物品发布的id（用户表的外键）',
  `goods_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '捡到物品的名称，1-20位的非空字段',
  `goods_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '简述捡到的物品信息',
  `goods_pos` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '简述捡到的物品的大致位置',
  `start_time` datetime NOT NULL COMMENT '捡到物品的大致开始时间',
  `end_time` datetime NOT NULL COMMENT '捡到物品的大致结束时间',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_pick_up_goods_uid`(`uid`) USING BTREE,
  CONSTRAINT `fk_pick_up_goods_uid` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pick_up_goods
-- ----------------------------

-- ----------------------------
-- Table structure for pick_up_image
-- ----------------------------
DROP TABLE IF EXISTS `pick_up_image`;
CREATE TABLE `pick_up_image`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pick_up_goods_id` int(11) NOT NULL COMMENT '发布拾物信息表的id（发布拾物信息表的外键）',
  `url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '拾物有关信息的图片链接',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_pick_up_image_pick_up_goods_id`(`pick_up_goods_id`) USING BTREE,
  CONSTRAINT `fk_pick_up_image_pick_up_goods_id` FOREIGN KEY (`pick_up_goods_id`) REFERENCES `pick_up_goods` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pick_up_image
-- ----------------------------

-- ----------------------------
-- Table structure for pick_up_question
-- ----------------------------
DROP TABLE IF EXISTS `pick_up_question`;
CREATE TABLE `pick_up_question`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pick_up_goods_id` int(11) NOT NULL COMMENT '发布拾物信息表的id（发布拾物信息表的外键）',
  `question` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对拾物有关信息的提问',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_pick_up_question_pick_up_goods_id`(`pick_up_goods_id`) USING BTREE,
  CONSTRAINT `fk_pick_up_question_pick_up_goods_id` FOREIGN KEY (`pick_up_goods_id`) REFERENCES `pick_up_goods` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pick_up_question
-- ----------------------------

-- ----------------------------
-- Table structure for pick_up_tag
-- ----------------------------
DROP TABLE IF EXISTS `pick_up_tag`;
CREATE TABLE `pick_up_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pick_up_goods_id` int(11) NOT NULL COMMENT '发布拾物信息表的id（发布拾物信息表的外键）',
  `tag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '拾物有关信息的标签',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_pick_up_tag_pick_up_goods_id`(`pick_up_goods_id`) USING BTREE,
  CONSTRAINT `fk_pick_up_tag_pick_up_goods_id` FOREIGN KEY (`pick_up_goods_id`) REFERENCES `pick_up_goods` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pick_up_tag
-- ----------------------------

-- ----------------------------
-- Table structure for temp_chat
-- ----------------------------
DROP TABLE IF EXISTS `temp_chat`;
CREATE TABLE `temp_chat`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `sender_id` int(11) NOT NULL COMMENT '发送人ID,用户表外键',
  `getter_id` int(11) NOT NULL COMMENT '接收人ID,用户表外键',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '发送的内容',
  `send_time` datetime NOT NULL COMMENT '发送信息的时间',
  `messageType` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_temp_chat_getter_id`(`getter_id`) USING BTREE,
  INDEX `fk_temp_chat_sender_id`(`sender_id`) USING BTREE,
  CONSTRAINT `fk_temp_chat_getter_id` FOREIGN KEY (`getter_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_temp_chat_sender_id` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of temp_chat
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'e10adc3949ba59abbe56e057f20f883e' COMMENT '登录密码',
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  `gender` enum('男','女','保密') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '男' COMMENT '性别',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '2011050514', 'e10adc3949ba59abbe56e057f20f883e', '18069078946', '晚上别敲代码', '男', '2023-05-07 18:56:58', '2023-05-07 18:57:07');
INSERT INTO `user` VALUES (2, '2016050621', '4fbdbc33e1226d57a812c02f8fb5590a', '15310467031', '薯片是只猫', '男', '2023-05-14 12:33:07', '2023-05-14 12:33:07');
INSERT INTO `user` VALUES (3, '2011050513', 'e10adc3949ba59abbe56e057f20f883e', '15310467030', '薯片', '男', '2023-05-16 19:12:57', '2023-05-16 19:12:57');

SET FOREIGN_KEY_CHECKS = 1;
