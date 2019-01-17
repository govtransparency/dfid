
Data Collection System (DCS)
=======================================

This project contains a collection of java components that are used by the project to fetch, convert and clean data on development aid funded contracts and projects. 

As it's based on an architecture of data collection system for a Digiwhist project ror more details about the design of this architecture, please see [Digiwhist Workpackage 2.8 (PDF)](https://github.com/digiwhist/wp2_documents/blob/master/d2_8.pdf).

REQUIREMENTS
-------------------------------------------------------
- Postgresql 9.4 and higher
- RabbitMQ 3.6
- Java 10
- Maven
- Python 3 for the scripts

ARCHITECTURE
-------------------------------------------------------
DCS is organised as a series of Maven projects. These can be built using the `mvn compile` command. 

Data processing stages
-------------------------------------------------------

* Raw - downloading of raw (HTML, XML, etc.) files from internet
* Parsed - conversion of unstructured data to structured format (all values in text format)
* Clean - conversion of text values into proper data types, standardizing of enumeration values etc.

Workers
-------------------------------------------------------
Each above described stage of data is processed by a standalone program called worker

- Crawler - crawls a website, FTP server or reads from an API and passes information of what should be downloaded to Downloader
                 - in some specific cases Crawler also serves as a Downloader
- Downloader - reads information passed by a crawler and downloads and stores data to a DB. Tells parser which records can be parsed.
- Parser - creates structured data from unstructured data. Tells cleaner which records can be cleaned.
- Cleaner - does the cleaning job and tells matcher which records can be matched.

Worker names are derived from a package structure of DCS. Worker names that processes World Bank contracts source are: 

- eu.dfid.worker.wb.raw.WBContractCrawler
- eu.dfid.worker.wb.raw.WBContractDownloader
- eu.dfid.worker.wb.raw.WBContractParser
- eu.dfid.worker.wb.raw.WBContractCleaner

Storage
-------------------------------------------------------
Each tender record has it's copy on each stage of data processing. These are stored in separate DB tables. The names of tables are:

- raw_data
- parsed_tender
- clean_tender

Project level data are stored in tables

- raw_data
- parsed_project
- clean_project

Create script is located in dfid-dataaccess\src\main\resources\migrations\001_base.sql

Each table row will contain meta-data about the tender, along with a blob of structured JSON. 

Communication
-------------------------------------------------------
DDCS uses RabitMQ messaging system to ensure communication between workers.
Each time some record is processed on a specific level of data processing, proper program publishes a message containing ID of a tender record which should be processed on a next level. Such message is used by a next level worker to retrieve the right record

The crawler's queue is slightly different from other step's queues. An empty json message *{}* initiates the job, however, the crawling date (start and end) can be specified too with json parameters *startDate* and *endDate*.


How to build and run?
-------------------------------------------------------

The **docker-compose.yaml** file can be used, to launch the environment. In case you have the required subsystems already running or want install manually, the DCS can use them too.

Either way, the DCS configuration must be changed, dfid-worker and dfid-api directories (*xxx.properties files*), to allow access to these resources. You can change some configuration in the *docker-compose.yaml* too.

After the required subsystems are up and running, connect to the database server (create a database/schema if they are not exists) and run the *dfid-dataaccess\src\main\resources\migrations\001_base.sql* script to prepare the database structure.

Re-check all the configurations (*../resources/xxx.properties* file) then build and package it (the dfid-worker's package can be created with `mvn package appassembler:assemble`)

The appassembler creates a package, that you can easily copy for example to your server. To start a worker just run:

`../appassembler/bin/dfid_worker public eu.dfid.worker.idb.raw.IDBAwardedContractsGoodsAndWorksCrawler`

Where *public* is the property file name in the package

Okay, everything is up and running, but we have to send some message to the workers to begin working. To do so, open the RabbitMQ admin page in your browser, locate the queue with the name '.init' at the end, a send an empty json: *{}*.

**WARNING!** Always start the whole process (crawler, downloader, parser, cleaner, etc.) to let them create the message queues!

