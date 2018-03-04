/*
SQLyog Ultimate v8.32 
MySQL - 5.0.16 : Database - pipeline
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
USE `pipeline`;

/*Data for the table `plugininfo` */

insert  into `plugininfo`(`id`,`description`,`disabled`,`icon`,`jobType`,`name`,`priority`,`wiki`) values (1,'在云端进行编译，并把产出上传至临时产品库，临时保留7天,需要在代码库的根目录下提交build.sh文件。',NULL,NULL,'COMPILE',NULL,NULL,'https://github.com/z521598/pipeline/wiki/%E7%BC%96%E8%AF%91%E6%8F%92%E4%BB%B6%E4%BD%BF%E7%94%A8%E6%89%8B%E5%86%8C');
insert  into `plugininfo`(`id`,`description`,`disabled`,`icon`,`jobType`,`name`,`priority`,`wiki`) values (2,'将构建产出拷贝一份到正式产品库，永久保留，需要在Job之前配置”编译构建”插件。',NULL,NULL,'RELEASE',NULL,NULL,'https://github.com/z521598/pipeline/wiki/%E5%8F%91%E7%89%88%E6%8F%92%E4%BB%B6%E4%BD%BF%E7%94%A8%E6%89%8B%E5%86%8C');
insert  into `plugininfo`(`id`,`description`,`disabled`,`icon`,`jobType`,`name`,`priority`,`wiki`) values (3,'将零散的Jenkins Job集成在流水线上，支持动态的自动化的进行参数传递，需要提前接入Jenkins服务资源。',NULL,NULL,'JENKINS_JOB',NULL,NULL,'https://github.com/z521598/pipeline/wiki/Jenkins-Job%E6%8F%92%E4%BB%B6%E4%BD%BF%E7%94%A8%E6%89%8B%E5%86%8C');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
