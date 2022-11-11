CREATE DATABASE  IF NOT EXISTS `treasuremaniac` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `treasuremaniac`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: treasuremaniac
-- ------------------------------------------------------
-- Server version	5.5.36-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `heroes`
--

DROP TABLE IF EXISTS `heroes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `heroes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `power` int(11) NOT NULL,
  `defense` int(11) NOT NULL,
  `speed` int(11) NOT NULL,
  `awareness` int(11) NOT NULL,
  `avatar` varchar(255) NOT NULL,
  `score` int(11) NOT NULL,
  `round` int(11) DEFAULT NULL,
  `alive` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=193 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `heroes`
--

LOCK TABLES `heroes` WRITE;
/*!40000 ALTER TABLE `heroes` DISABLE KEYS */;
INSERT INTO `heroes` VALUES (1,'PJ',30,30,30,30,'RangerAvatar.gif',18555,11,''),(2,'Steve',30,30,30,30,'MageAvatar.gif',21000,11,''),(3,'Robin',30,30,30,30,'MageAvatar.gif',19250,11,''),(4,'Simon',30,30,30,30,'WarriorAvatar.gif',15752,11,''),(192,'GOD',30,30,30,30,'WarriorAvatar.gif',37050,11,'');
/*!40000 ALTER TABLE `heroes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `heroes_has_treasures`
--

DROP TABLE IF EXISTS `heroes_has_treasures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `heroes_has_treasures` (
  `Hero_id` int(11) NOT NULL,
  `Treasure_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`Hero_id`,`Treasure_id`),
  KEY `fk_Heroes_has_Treasures_Treasure1_idx` (`Treasure_id`),
  KEY `fk_Heroes_has_Treasures_Heroes1.idx` (`Hero_id`),
  CONSTRAINT `fk_Heroes_has_Treasures_Heroes1` FOREIGN KEY (`Hero_id`) REFERENCES `heroes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Heroes_has_Treasures_Treasure1` FOREIGN KEY (`Treasure_id`) REFERENCES `treasures` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `heroes_has_treasures`
--

LOCK TABLES `heroes_has_treasures` WRITE;
/*!40000 ALTER TABLE `heroes_has_treasures` DISABLE KEYS */;
/*!40000 ALTER TABLE `heroes_has_treasures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `monsters`
--

DROP TABLE IF EXISTS `monsters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `monsters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `power` int(11) NOT NULL,
  `defense` int(11) NOT NULL,
  `speed` int(11) NOT NULL,
  `awareness` int(11) NOT NULL,
  `avatar` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `monsters`
--

LOCK TABLES `monsters` WRITE;
/*!40000 ALTER TABLE `monsters` DISABLE KEYS */;
INSERT INTO `monsters` VALUES (31,'Foulspawn Goblin',7,4,2,0,'MonsterWarrior.gif'),(32,'Armored Goblin',4,8,0,4,'MonsterWarrior.gif'),(33,'Horrid Tree',10,6,4,8,'EvilTree.gif'),(34,'Deadly Spider',12,4,5,5,'Spider.gif'),(35,'Hellspawn Phoenix',14,8,9,5,'Phoenix.gif'),(36,'Muscular Cow',13,8,4,6,'Minotaur.gif'),(37,'Strengthened Gnome',14,12,8,10,'Fighter.gif'),(38,'Black Widow',8,4,6,4,'Spider.gif'),(39,'Ne\'Flav',6,2,10,8,'Alien.gif'),(40,'Hollow Tree',6,4,8,15,'EvilTree.gif'),(41,'Minotaur',2,4,3,3,'Minotaur.gif'),(42,'Spider',3,2,4,1,'Spider.gif'),(43,'Underling',3,2,2,3,'Alien.gif'),(44,'Goblin',2,2,2,2,'MonsterWarrior.gif'),(45,'Blind Tree',6,8,2,0,'EvilTree.gif'),(46,'Overpowered Hero',15,16,17,15,'Fighter.gif'),(47,'Alien Scout',1,2,4,1,'Alien.gif'),(48,'Walking Oak',1,3,1,1,'EvilTree.gif'),(49,'Ninja Student',1,1,3,3,'Fighter.gif'),(50,'Lesser Minotaur',3,1,1,1,'Minotaur.gif'),(51,'Phoenix Chick',2,1,2,2,'Phoenix.gif'),(52,'Weak Spider',3,0,1,3,'Spider.gif'),(53,'Phoenix',8,5,9,8,'Phoenix.gif'),(54,'Greater Demon',12,5,5,5,'Minotaur.gif'),(55,'Gro Natsuk',13,13,2,2,'MonsterWarrior.gif'),(56,'Aragog',8,8,12,12,'Spider.gif'),(57,'Phoenix Lord',16,10,8,8,'Phoenix.gif'),(58,'Great Deku Tree',11,18,6,7,'EvilTree.gif'),(59,'Neila',12,9,8,15,'Alien.gif'),(61,'Hobgoblin',10,10,10,10,'MonsterWarrior.gif'),(62,'Angry Minotaur',8,15,10,8,'Minotaur.gif'),(63,'Dracul',15,12,8,9,'Minotaur.gif'),(64,'Magna The Warrior',10,15,8,9,'Fighter.gif'),(65,'Incendia',8,8,15,12,'Phoenix.gif'),(66,'Na\'Kiel',10,15,2,2,'Alien.gif'),(67,'Angry Yew',15,10,5,5,'EvilTree.gif'),(68,'Naswati',15,8,5,15,'Fighter.gif'),(69,'Gargoyle',12,3,1,0,'Minotaur.gif'),(70,'Borkul',10,2,2,7,'MonsterWarrior.gif'),(71,'Ignis',10,5,0,9,'Phoenix.gif'),(72,'Arachno',8,8,5,2,'Spider.gif'),(73,'Xeno Gears',22,22,30,30,'Boss.gif');
/*!40000 ALTER TABLE `monsters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `treasures`
--

DROP TABLE IF EXISTS `treasures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `treasures` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `goldvalue` int(11) NOT NULL,
  `description` varchar(255) NOT NULL,
  `power` int(11) NOT NULL,
  `defense` int(11) NOT NULL,
  `speed` int(11) NOT NULL,
  `awareness` int(11) NOT NULL,
  `avatar` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `treasures`
--

LOCK TABLES `treasures` WRITE;
/*!40000 ALTER TABLE `treasures` DISABLE KEYS */;
INSERT INTO `treasures` VALUES (20,'Ebony Boots',600,'Light, but very strong',0,3,1,1,'boots_blackBoots.png'),(21,'Iron Boots',200,'Heavy, but quite strong',0,3,0,0,'boots_IronBoots.gif'),(23,'Leather Boots',400,'The finest leather',0,1,1,1,'boots_LeatherBoots.png'),(24,'Fur Boots',300,'warm and light, but poor protection',0,0,1,1,'boots_FurBoots.png'),(25,'Steel Boots',300,'Heavy and very strong',0,3,-1,0,'boots_SteelBoots.png'),(26,'Wooden Bow',100,'Poorly made bow',1,0,0,0,'bow_CommonBow.gif'),(27,'Crossbow',200,'A well made wooden crossbow',2,0,0,1,'bow_Crossbow.gif'),(28,'Falcon Crossbow',500,'A high quality crossbow, true power',3,0,1,1,'bow_FalconCrossbow.gif'),(29,'Heroes Bow',300,'A fine made bow, powerfull but slow',3,0,0,0,'bow_HeroesBow.gif'),(30,'Leather Armor',400,'Light and quite strong',0,2,1,1,'chest_LeatherArmour.png'),(31,'Black Mage Robe',500,'Eminates a powerfull aura',1,1,0,3,'chest_MageArmour.gif'),(32,'Iron Pauldron',200,'Looks intimidating but heavy',1,1,0,0,'chest_IronArmour.gif'),(33,'Iron Cuirass',500,'Very reliable armor, light and strong',0,3,1,0,'chest_IronCuirass.gif'),(34,'Vinking Helm',200,'A simple iron helmet',0,1,0,0,'helm_VikingHelmet.gif'),(35,'Ebony Helm',500,'Strong and reliable',0,3,0,1,'helm_BlackHelm.gif'),(36,'Corrupted Helm',300,'Has a certain aura around it',2,2,-1,-1,'helm_CorruptedHelm.gif'),(37,'Imperial Helm',200,'Strong, but poor vision',0,3,0,-1,'helm_ImperialHelm.gif'),(38,'Feather Helm',1000,'Beautifull, but poor quality',-1,-1,-1,-1,'helm_LegionHelm.gif'),(39,'Wooden Staff',100,'Basic, but good',1,1,0,0,'staff_BasicStaff.gif'),(40,'Soul Staff',400,'Strong, but feeds on your soul',3,-1,1,1,'staff_SoulStaff.gif'),(41,'Fire Staff',300,'Very hot, but slow in usage',3,0,0,0,'staff_FireStaff.gif'),(42,'Ice Staff',300,'Great staff to defend yourself',0,3,0,0,'staff_IceStaff.gif'),(43,'The Butcher',300,'Heavy but very sharp',3,0,0,0,'sword_BlackButcherSword.gif'),(44,'Demon Slayer',400,'Known for its magical power',3,0,0,1,'sword_DemonSword.gif'),(45,'Skeleton Sword',400,'Light and powerfull',2,0,1,1,'sword_SkeletonSword.png'),(46,'TreasureManiac',1500,'The best sword there is',3,1,2,2,'sword_TreasureManiacSword.gif'),(47,'Master Sword',500,'Made to keep evil at bay',3,0,1,1,'sword_MasterSword.gif'),(48,'Shiny Sword',2000,'Looks nice, but that\'s all',-2,-2,-2,-2,'sword_ShinySword.gif'),(49,'Wooden Shield',200,'Basic, every warrior needs one',0,2,0,0,'shield_WoodenShield.gif'),(50,'Heaven Defender',500,'A shield made by the gods',0,3,0,1,'shield_HolyShield.gif'),(51,'Steel shield',200,'Good old trusty steel',0,3,0,-1,'shield_EnglishShield.gif'),(52,'Shield Of Arav',2500,'It once wielded great power',-3,-1,-1,-1,'shield_OldShield.gif'),(53,'King\'s Shield',400,'Gives the user a boost of power',2,3,-1,0,'shield_LionShield.png'),(54,'Snake Shield',600,'Curses the one who wields it',0,3,-1,-1,'shield_SnakeShield.png'),(55,'Horleon',300,'The crossing of a tiger and a horse',0,0,2,2,'mount_TigerHorse.gif'),(56,'Pegasi',1000,'A beatifull flying horse',0,0,3,3,'mount_Pegasus.png'),(57,'Dreamstride',400,'A fine horse',0,1,1,1,'mount_HorseSteelArmour.gif'),(58,'Gold Coins',500,'Shiny!',0,0,0,0,'item_Coins.gif'),(59,'Chest Of Golf',1600,'Jackpot!',0,0,0,0,'item_chest.gif'),(60,'Daimond',600,'Strong and beatifull',0,0,0,0,'item_Diamond.gif'),(61,'Emerald',550,'Green and shiny',0,0,0,0,'item_Emerald.gif'),(62,'Golden Statue',5000,'Can\'t imagine anything more valuable',-3,-3,-3,-3,'item_GoldStatue.gif'),(63,'Ruby',500,'Has a bright red color',0,0,0,0,'item_Ruby.gif'),(64,'Silver Goblet',800,'Quite heavy for a goblet',-1,0,-1,0,'item_SilverGoblet.gif'),(65,'Small Totem',350,'An old and powerfull relic',1,1,1,1,'item_SmallTotem.gif'),(66,'Old Runestones',450,'Old runestones, has a powerfull aura',0,3,-1,3,'item_StoneRelics.png'),(67,'Crystal ring',1200,'Has a beatifull gem, sadly it is cursed',-2,0,0,0,'jewelry_CrystalRing.gif'),(68,'Dragon Necklace',400,'Pure power  trapped in a necklace',3,0,0,0,'jewelry_DragonNecklace.gif'),(69,'Enchanted Ring',1000,'My precious!',-3,3,0,0,'jewelry_GoldRing.png'),(70,'Cross Necklace',250,'Holds holy, protective powers',0,2,0,1,'jewelry_HolyNecklace.gif'),(71,'Magic Ring',400,'Gives you psychic powers',1,1,0,1,'jewelry_SilverRing.gif'),(72,'Star Necklace',300,'Makes you lighter, maybe a bit to light',-1,-1,2,2,'jewelry_StarNecklace.gif');
/*!40000 ALTER TABLE `treasures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `treasures_has_monsters`
--

DROP TABLE IF EXISTS `treasures_has_monsters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `treasures_has_monsters` (
  `Treasure_id` int(11) NOT NULL,
  `Monster_id` int(11) NOT NULL,
  PRIMARY KEY (`Treasure_id`,`Monster_id`),
  KEY `fk_Treasures_has_Monsters_Monsters1_idx` (`Monster_id`),
  KEY `fk_Treasures_has_Monsters_Treasures1_idx` (`Treasure_id`),
  CONSTRAINT `fk_Treasures_has_Monsters_Monsters1` FOREIGN KEY (`Monster_id`) REFERENCES `monsters` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Treasures_has_Monsters_Treasures1` FOREIGN KEY (`Treasure_id`) REFERENCES `treasures` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `treasures_has_monsters`
--

LOCK TABLES `treasures_has_monsters` WRITE;
/*!40000 ALTER TABLE `treasures_has_monsters` DISABLE KEYS */;
INSERT INTO `treasures_has_monsters` VALUES (24,31),(26,31),(33,31),(25,32),(32,32),(44,32),(31,33),(35,33),(39,33),(38,34),(44,34),(57,34),(58,34),(36,35),(61,35),(67,35),(29,36),(30,36),(31,36),(53,36),(38,37),(41,37),(54,37),(72,37),(40,38),(43,38),(50,38),(21,39),(40,39),(53,39),(42,40),(45,40),(50,40),(34,41),(47,41),(49,41),(27,42),(40,42),(61,42),(23,43),(37,43),(39,43),(48,44),(65,44),(69,44),(35,45),(39,45),(55,45),(20,46),(31,46),(46,46),(52,46),(37,47),(45,47),(64,47),(32,48),(66,48),(68,48),(21,49),(28,49),(33,49),(27,50),(30,50),(53,50),(35,51),(43,51),(48,51),(37,52),(44,52),(63,52),(31,53),(42,53),(69,53),(71,53),(25,54),(29,54),(32,54),(38,54),(28,55),(37,55),(48,55),(51,55),(52,55),(25,56),(32,56),(39,56),(56,56),(28,57),(36,57),(46,57),(54,57),(30,58),(34,58),(52,58),(57,58),(33,59),(60,59),(61,59),(64,59),(21,61),(26,61),(32,61),(39,61),(27,62),(37,62),(65,62),(71,62),(25,63),(45,63),(63,63),(65,63),(28,64),(33,64),(43,64),(50,64),(20,65),(31,65),(35,65),(41,65),(58,66),(70,66),(71,66),(27,67),(39,67),(61,67),(24,68),(29,68),(33,68),(30,69),(47,69),(57,69),(59,69),(34,70),(40,70),(46,70),(52,70),(66,70),(48,71),(50,71),(65,71),(20,72),(41,72),(49,72),(61,72),(46,73),(62,73);
/*!40000 ALTER TABLE `treasures_has_monsters` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-05-13 15:16:00
