/*
 Navicat Premium Data Transfer

 Source Server         : 本地mysql
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : lchat-server

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 24/02/2022 20:13:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for lchat_friend
-- ----------------------------
DROP TABLE IF EXISTS `lchat_friend`;
CREATE TABLE `lchat_friend` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created` bigint DEFAULT NULL,
  `updated` bigint DEFAULT NULL,
  `friend_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `is_agree` int DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of lchat_friend
-- ----------------------------
BEGIN;
INSERT INTO `lchat_friend` (`id`, `created`, `updated`, `friend_id`, `is_agree`, `user_id`) VALUES ('4028d5827d886498017d886fa8f70003', 1638671558885, 1638671558885, 'lcid_IaPj51A2hoRKmN', 1, 'lcid_9LSiF7KQjpKuYR');
INSERT INTO `lchat_friend` (`id`, `created`, `updated`, `friend_id`, `is_agree`, `user_id`) VALUES ('4028d5827da74a36017da74b317c0000', 1639189262701, 1639189262701, 'lcid_02PyG74c97SQS1', 1, 'lcid_9LSiF7KQjpKuYR');
COMMIT;

-- ----------------------------
-- Table structure for lchat_group_info
-- ----------------------------
DROP TABLE IF EXISTS `lchat_group_info`;
CREATE TABLE `lchat_group_info` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created` bigint DEFAULT NULL,
  `updated` bigint DEFAULT NULL,
  `group_creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `group_owner` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of lchat_group_info
-- ----------------------------
BEGIN;
INSERT INTO `lchat_group_info` (`id`, `created`, `updated`, `group_creator`, `group_id`, `group_name`, `group_owner`) VALUES ('364846a6adce964fde68bcc9ad23ffaf', 1640419909234, 1640419909234, 'lcid_9LSiF7KQjpKuYR', 'lcid_group_cQukdBi3GO5zJ5', '测试群组2', 'lcid_9LSiF7KQjpKuYR');
INSERT INTO `lchat_group_info` (`id`, `created`, `updated`, `group_creator`, `group_id`, `group_name`, `group_owner`) VALUES ('4028d5827dae7f35017dae994a0f0015', 1639311821327, 1639311821327, 'lcid_9LSiF7KQjpKuYR', 'lcid_group_4VTwUCDq0Cb9Is', '测试Group', 'lcid_9LSiF7KQjpKuYR');
COMMIT;

-- ----------------------------
-- Table structure for lchat_group_member
-- ----------------------------
DROP TABLE IF EXISTS `lchat_group_member`;
CREATE TABLE `lchat_group_member` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created` bigint DEFAULT NULL,
  `updated` bigint DEFAULT NULL,
  `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `group_member` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of lchat_group_member
-- ----------------------------
BEGIN;
INSERT INTO `lchat_group_member` (`id`, `created`, `updated`, `group_id`, `group_member`) VALUES ('4028d5827dae7f35017dae994a1d0016', 1639311821341, 1639311821341, 'lcid_group_4VTwUCDq0Cb9Is', 'lcid_IaPj51A2hoRKmN');
INSERT INTO `lchat_group_member` (`id`, `created`, `updated`, `group_id`, `group_member`) VALUES ('4028d5827dae7f35017dae994a2b0017', 1639311821355, 1639311821355, 'lcid_group_4VTwUCDq0Cb9Is', 'lcid_02PyG74c97SQS1');
INSERT INTO `lchat_group_member` (`id`, `created`, `updated`, `group_id`, `group_member`) VALUES ('4028d5827dae7f35017dae994a2b0019', 1639311821368, 1639311821368, 'lcid_group_4VTwUCDq0Cb9Is', 'lcid_9LSiF7KQjpKuYR');
INSERT INTO `lchat_group_member` (`id`, `created`, `updated`, `group_id`, `group_member`) VALUES ('63a0ecab089aeda304ff18a404bbeccd', 1640419909248, 1640419909248, 'lcid_group_cQukdBi3GO5zJ5', 'lcid_9LSiF7KQjpKuYR');
INSERT INTO `lchat_group_member` (`id`, `created`, `updated`, `group_id`, `group_member`) VALUES ('9f757ab0a85141c293bb508369057e97', 1640419909255, 1640419909255, 'lcid_group_cQukdBi3GO5zJ5', 'lcid_IaPj51A2hoRKmN');
COMMIT;

-- ----------------------------
-- Table structure for lchat_group_message
-- ----------------------------
DROP TABLE IF EXISTS `lchat_group_message`;
CREATE TABLE `lchat_group_message` (
  `send_id` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `msg_seq` bigint DEFAULT NULL,
  `msg_type` varchar(255) DEFAULT NULL,
  `receive_id` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of lchat_group_message
-- ----------------------------
BEGIN;
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '1234', 1643013787956, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_IaPj51A2hoRKmN', '12345', 1643014575150, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '12345', 1643028169953, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '123456789', 1643036421868, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '1234567', 1643036699406, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '65657567567567rtrtrt', 1643037141809, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '3234edads撒打算打算打算的', 1643037193446, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_02PyG74c97SQS1', '我是小柳', 1643037220452, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '我的话就枯萎或饿哦好闻', 1643037351093, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_02PyG74c97SQS1', '大撒上对的', 1643037398903, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_02PyG74c97SQS1', '大大是大多数大撒上的', 1643037407145, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '撒打算的打算', 1643037423098, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', 'electron！！', 1643038494506, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '123456', 1643128200502, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_02PyG74c97SQS1', '你好大家！！', 1643128235581, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_IaPj51A2hoRKmN', '看的见我吗', 1643128280512, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '图一抬头', 1643128536620, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_IaPj51A2hoRKmN', 'v斑斑驳驳', 1643128679225, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '你们好呀，我是小王！！', 1643129416694, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_IaPj51A2hoRKmN', '你们好呀，我是小G！！', 1643129440726, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_02PyG74c97SQS1', '哈哈哈哈哈，我是小柳！！', 1643129463048, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '哈喽哈喽哈喽', 1643185307646, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_02PyG74c97SQS1', '哈喽哈喽哈喽2', 1643185318806, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_IaPj51A2hoRKmN', '哈喽哈喽哈喽3', 1643185330349, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '这个是一条群聊离线消息！', 1643205640434, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '这个是一条群聊离线消息v2!', 1643205865114, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '这个是一条群聊离线消息v3!', 1643206385878, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '这个是一条群聊离线消息v4!', 1643207616995, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '有人不', 1643207688775, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '我在呢', 1643207695101, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_02PyG74c97SQS1', '我也在呢', 1643207705234, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_IaPj51A2hoRKmN', '我来啦', 1643207742147, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', 'LChat-xw-1643207764562-150-100-undefined', 1643207766807, 'IMAGE', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', 'LChat-xw-1643210664160-150-100-undefined', 1643210664557, 'IMAGE', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_group_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', 'LChat-xw-1643210867848-150-100-undefined', 1643210869706, 'IMAGE', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
COMMIT;

-- ----------------------------
-- Table structure for lchat_message
-- ----------------------------
DROP TABLE IF EXISTS `lchat_message`;
CREATE TABLE `lchat_message` (
  `send_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `msg_seq` bigint DEFAULT NULL,
  `msg_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `receive_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of lchat_message
-- ----------------------------
BEGIN;
INSERT INTO `lchat_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '1234', 1643013787956, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '1234', 1643013787956, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
INSERT INTO `lchat_message` (`send_id`, `message`, `msg_seq`, `msg_type`, `receive_id`, `type`) VALUES ('lcid_9LSiF7KQjpKuYR', '1234', 1643013787956, 'TEXT', 'lcid_group_4VTwUCDq0Cb9Is', 'GROUP');
COMMIT;

-- ----------------------------
-- Table structure for lchat_user_group_message
-- ----------------------------
DROP TABLE IF EXISTS `lchat_user_group_message`;
CREATE TABLE `lchat_user_group_message` (
  `lcid` varchar(255) DEFAULT NULL,
  `group_id` varchar(255) DEFAULT NULL,
  `msg_seq` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of lchat_user_group_message
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for lchat_user_info
-- ----------------------------
DROP TABLE IF EXISTS `lchat_user_info`;
CREATE TABLE `lchat_user_info` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created` bigint DEFAULT NULL,
  `updated` bigint DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `background` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `lcid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of lchat_user_info
-- ----------------------------
BEGIN;
INSERT INTO `lchat_user_info` (`id`, `created`, `updated`, `avatar`, `background`, `description`, `password`, `user_name`, `lcid`) VALUES ('4028d5827d886498017d886bb7ed0000', 1638671300553, 1638671300553, 'https://lchat-server.oss-cn-shenzhen.aliyuncs.com/avatar/default/avatar.jpg', 'https://lchat-server.oss-cn-shenzhen.aliyuncs.com/background/default/background.jpg', NULL, '1234', 'xw', 'lcid_9LSiF7KQjpKuYR');
INSERT INTO `lchat_user_info` (`id`, `created`, `updated`, `avatar`, `background`, `description`, `password`, `user_name`, `lcid`) VALUES ('4028d5827d886498017d886c05c80001', 1638671320519, 1638671320519, 'https://lchat-server.oss-cn-shenzhen.aliyuncs.com/avatar/default/avatar.jpg', 'https://lchat-server.oss-cn-shenzhen.aliyuncs.com/background/default/background.jpg', NULL, '1234', 'xg', 'lcid_IaPj51A2hoRKmN');
INSERT INTO `lchat_user_info` (`id`, `created`, `updated`, `avatar`, `background`, `description`, `password`, `user_name`, `lcid`) VALUES ('4028d5827d886498017d886c22a90002', 1638671327913, 1638671327913, 'https://lchat-server.oss-cn-shenzhen.aliyuncs.com/avatar/default/avatar.jpg', 'https://lchat-server.oss-cn-shenzhen.aliyuncs.com/background/default/background.jpg', NULL, '1234', 'xl', 'lcid_02PyG74c97SQS1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
