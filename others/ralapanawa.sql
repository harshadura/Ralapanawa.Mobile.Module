-- phpMyAdmin SQL Dump
-- version 2.11.6
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 08, 2012 at 06:11 AM
-- Server version: 5.0.51
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `ralapanawa`
--
CREATE DATABASE `ralapanawa` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `ralapanawa`;

-- --------------------------------------------------------

--
-- Table structure for table `capacity_table`
--

CREATE TABLE `capacity_table` (
  `tank id` int(11) NOT NULL,
  `MSI` varchar(30) NOT NULL,
  `hight above sil` varchar(30) NOT NULL,
  `capacity ACFT` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `capacity_table`
--


-- --------------------------------------------------------

--
-- Table structure for table `destric persons`
--

CREATE TABLE `destric persons` (
  `tank id` int(10) NOT NULL,
  `division` varchar(50) NOT NULL,
  `district` varchar(30) NOT NULL,
  `GA ID` int(10) NOT NULL,
  `D.D ID` int(10) NOT NULL,
  `Eng ID` int(10) NOT NULL,
  `TA ID` int(10) NOT NULL,
  `Irrigator ID` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `destric persons`
--


-- --------------------------------------------------------

--
-- Table structure for table `district`
--

CREATE TABLE `district` (
  `district_id` int(20) NOT NULL auto_increment,
  `district_name` varchar(50) NOT NULL,
  `province_id` varchar(50) NOT NULL,
  `deputy_director_id` varchar(50) NOT NULL,
  `Ga_id` varchar(10) NOT NULL,
  PRIMARY KEY  (`district_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `district`
--

INSERT INTO `district` (`district_id`, `district_name`, `province_id`, `deputy_director_id`, `Ga_id`) VALUES
(1, 'Ampara', '2', '', ''),
(2, 'Anuradhapura', '7', '', ''),
(3, 'Batticaloa', '2', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `division`
--

CREATE TABLE `division` (
  `division_id` int(10) NOT NULL auto_increment,
  `division_name` varchar(50) NOT NULL,
  `division_eng_id` varchar(50) NOT NULL,
  `distirct_name` varchar(50) NOT NULL,
  PRIMARY KEY  (`division_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `division`
--

INSERT INTO `division` (`division_id`, `division_name`, `division_eng_id`, `distirct_name`) VALUES
(1, 'chenkallady', '', 'Batticaloa'),
(2, 'padiruppu', '', 'Batticaloa');

-- --------------------------------------------------------

--
-- Table structure for table `mob_privileges`
--

CREATE TABLE `mob_privileges` (
  `prv_id` int(11) NOT NULL auto_increment,
  `rol_id` int(11) NOT NULL,
  `prv_type` int(11) NOT NULL,
  PRIMARY KEY  (`prv_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=53 ;

--
-- Dumping data for table `mob_privileges`
--

INSERT INTO `mob_privileges` (`prv_id`, `rol_id`, `prv_type`) VALUES
(1, 1, 1),
(2, 0, 2),
(3, 2, 3),
(4, 3, 4),
(5, 4, 5),
(6, 5, 6),
(7, 6, 7);

-- --------------------------------------------------------

--
-- Table structure for table `pic_water_level`
--

CREATE TABLE `pic_water_level` (
  `Date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `Depth(feetins)` varchar(30) NOT NULL,
  `tankid` varchar(30) NOT NULL,
  `Waterlevel_id` int(10) NOT NULL auto_increment,
  PRIMARY KEY  (`Waterlevel_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `pic_water_level`
--

INSERT INTO `pic_water_level` (`Date`, `Depth(feetins)`, `tankid`, `Waterlevel_id`) VALUES
('2012-05-31 11:19:22', '6"', 'IR005', 1);

-- --------------------------------------------------------

--
-- Table structure for table `provinces`
--

CREATE TABLE `provinces` (
  `province_id` int(20) NOT NULL auto_increment,
  `Province_name` varchar(50) NOT NULL,
  `Province_Directorid` varchar(20) NOT NULL,
  PRIMARY KEY  (`province_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `provinces`
--

INSERT INTO `provinces` (`province_id`, `Province_name`, `Province_Directorid`) VALUES
(1, 'Central Province', ''),
(2, 'Eastern Province', ''),
(3, 'Northern Province', ''),
(4, 'Southern Province', ''),
(5, 'Western Province', ''),
(6, 'North Western Province', ''),
(7, 'North Central Province', ''),
(8, 'Uva Province', ''),
(9, 'Sabaragamuwa Province', '');

-- --------------------------------------------------------

--
-- Table structure for table `ralapanawa_login`
--

CREATE TABLE `ralapanawa_login` (
  `login_id` int(11) NOT NULL auto_increment,
  `login_name` varchar(100) NOT NULL,
  `rol_id` int(11) NOT NULL,
  `login_username` varchar(50) NOT NULL,
  `login_password` varchar(50) NOT NULL,
  `login_dateofbirth` varchar(50) NOT NULL,
  `login_gender` varchar(30) NOT NULL,
  `login_address` varchar(50) NOT NULL,
  `login_hpphone` int(11) NOT NULL,
  `login_mpphone` int(11) NOT NULL,
  `login_pcode` int(11) NOT NULL,
  `login_email` varchar(30) NOT NULL,
  `workposition` varchar(100) NOT NULL,
  `Empid` varchar(50) NOT NULL,
  `imagename` varchar(50) NOT NULL,
  `division` varchar(100) NOT NULL,
  `district` varchar(100) NOT NULL,
  `province` varchar(100) NOT NULL,
  PRIMARY KEY  (`login_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=34 ;

--
-- Dumping data for table `ralapanawa_login`
--

INSERT INTO `ralapanawa_login` (`login_id`, `login_name`, `rol_id`, `login_username`, `login_password`, `login_dateofbirth`, `login_gender`, `login_address`, `login_hpphone`, `login_mpphone`, `login_pcode`, `login_email`, `workposition`, `Empid`, `imagename`, `division`, `district`, `province`) VALUES
(21, 'Sajev Lucksman', 1, 'lucksman', '123456', '23-09-1989', 'male', '15 Cyril lane Batticaloa', 652224562, 777806031, 30000, 'sajev@hmail.com', 'Engineer', 'IR0004', 'sajev.jpg', 'Chenkallady', 'Batticaloa', 'Eastern'),
(27, 'saja', 0, 'saja', 'Taxi31ghj', 'sas', 'sas', 'sa', 0, 0, 0, 'saja@gmail.com', '', '', '', '', '', ''),
(19, 'Sajev luks', 1, 'saj', '123456', '1 Jan 1989', 'Male', 'wellawatte', 777806031, 777806031, 777806031, 'sajev.it@gmail.com', '', '', '', '', '', ''),
(32, 'isuru madumal', 0, 'isuru123', '9192290', ' May 1989', 'Male', '233 mayamawatha,kiribathggoda', 112919229, 783908388, 6611, 'imadumal@yahoo.com', '', '', '', '', '', ''),
(33, 'isuru madumal', 0, 'isuru', '919229', ' Jan 1980', 'Male', '233,mayamaeatha,kiribathgoda', 112919229, 777806031, 6611, 'imadumal@yahoo.com', '', '', '', '', '', ''),
(31, 'sajanth lucksman', 0, 'saja', '123456', 'Jan 1980', 'Male', '15 cyril lanne bATTICALOA', 652224562, 715797667, 30000, 'ghj', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `tanks`
--

CREATE TABLE `tanks` (
  `Tank id` int(10) NOT NULL,
  `tank_name` varchar(50) NOT NULL,
  `Division` varchar(50) NOT NULL,
  `Tank name` varchar(30) NOT NULL,
  `F.S.D` varchar(30) NOT NULL,
  `capasity` varchar(30) NOT NULL,
  `com.Area` varchar(30) NOT NULL,
  `Province` varchar(30) NOT NULL,
  `District` varchar(30) NOT NULL,
  `tankmobileno` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tanks`
--


-- --------------------------------------------------------

--
-- Table structure for table `water_issue`
--

CREATE TABLE `water_issue` (
  `Tank id` int(10) NOT NULL,
  `start date` date NOT NULL,
  `end date` date NOT NULL,
  `month` varchar(30) NOT NULL,
  `year` varchar(30) NOT NULL,
  `Acreage cultivated` varchar(30) NOT NULL,
  `clerk approved` varchar(30) NOT NULL,
  `Eng approved` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `water_issue`
--


-- --------------------------------------------------------

--
-- Table structure for table `water_level`
--

CREATE TABLE `water_level` (
  `tankid` varchar(10) NOT NULL,
  `waterlevel_id` int(10) NOT NULL auto_increment,
  `Empid` varchar(10) NOT NULL,
  `Datetime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `Depth(feetins)` varchar(30) NOT NULL,
  `capacity(ac.ft)` varchar(30) NOT NULL,
  `slucieop L.B` varchar(30) NOT NULL,
  `slucieop R.B` varchar(30) NOT NULL,
  `Remarks` varchar(30) NOT NULL,
  `GPS` varchar(30) NOT NULL,
  PRIMARY KEY  (`waterlevel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `water_level`
--

