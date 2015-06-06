# OSMBufferServer

Serwer proxy  korzysta z serwera bazodanowego Oracle, należy zatem zainstalować serwer Oracle Database 11gExpress Edition.
Ponadto  należy  zainstalować  środowisko programistyczne  Oracle  SQL  Developer, za  pomocą  którego trzeba  będzie  stworzyć odpowiednią bazę danych.
Proces instalacji serwera bazodanowego oraz środowiska SQL Developer opisano tutaj: http://blog.dataconsulting.pl/2012/02/kurs-sql-online-oracle-instalacja-oracle-11gxe/ 

Po instalacji należy dodać nowego użytkownika bazy danych, oraz nowe połączenie z bazą. 
Użytkownikowi należy nadać nazwę OSMBUFFERSERVER oraz hasło system.
Aby serwer proxy mógł korzystać z bazy danych, trzeba stworzyć strukturę bazy. 
Można to zrobić wywołując skrypt „create_tables.sql”, skrypt powinien wyglądać podobnie do następującego:

CREATE TABLE DBPATH(PAHT_ID Number,
START_LATITUDE Float,
START_LONGITUDE Float,
END_LATITUDE Float,
END_LONGITUDE Float,
ADD_DATE Timestamp,
TIMES_USED Number,
Primary Key (PATH_ID));

CREAE TABLE DBMAPPOSITION(POSITION_ID Number,
LATITUDE Float,
LONGITUDE Float,
PATH_ID Number,
IDX Number,
Primary Key (POSITION_ID));
