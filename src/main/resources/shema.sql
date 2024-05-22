CREATE TABLE PUBLIC.USERS (
	ID INTEGER NOT NULL,
	NAME VARCHAR_IGNORECASE(255) NOT NULL,
	EMAIL VARCHAR_IGNORECASE(512) NOT NULL,
	CONSTRAINT USERS_PK PRIMARY KEY (ID),
	CONSTRAINT USERS_UNIQUE UNIQUE (EMAIL)
);
CREATE UNIQUE INDEX PRIMARY_KEY_4 ON PUBLIC.USERS (ID);
CREATE UNIQUE INDEX USERS_UNIQUE_INDEX_4 ON PUBLIC.USERS (EMAIL);
CREATE TABLE PUBLIC.ITEMS (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME VARCHAR_IGNORECASE(255) NOT NULL,
	DESCRIPTION VARCHAR_IGNORECASE(512) NOT NULL,
	AVAILABLE BOOLEAN,
	OWNER_ID INTEGER NOT NULL,
	REQUEST INTEGER,
	CONSTRAINT ITEMS_PK PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX PRIMARY_KEY_42 ON PUBLIC.ITEMS (ID);
CREATE TABLE PUBLIC.BOOKINGS (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	START_DATE TIMESTAMP NOT NULL,
	END_DATE TIMESTAMP NOT NULL,
	ITEM_ID INTEGER NOT NULL,
	BOOKER_ID INTEGER NOT NULL,
	STATUS VARCHAR_IGNORECASE(20) NOT NULL,
	CONSTRAINT BOOKINGS_PK PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX PRIMARY_KEY_A ON PUBLIC.BOOKINGS (ID);
CREATE TABLE PUBLIC.REQUESTS (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	DESCRIPTION VARCHAR_IGNORECASE(512) NOT NULL,
	REQUESTOR_ID INTEGER NOT NULL,
	CONSTRAINT REQUESTS_PK PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX PRIMARY_KEY_1 ON PUBLIC.REQUESTS (ID);
CREATE TABLE PUBLIC.COMMENTS (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	TEXT VARCHAR_IGNORECASE(255) NOT NULL,
	ITEM_ID INTEGER NOT NULL,
	AUTHOR_ID INTEGER NOT NULL,
	CONSTRAINT COMMENTS_PK PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX PRIMARY_KEY_AB ON PUBLIC.COMMENTS (ID);