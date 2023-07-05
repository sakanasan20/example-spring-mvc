DROP TABLE IF EXISTS beer;

DROP TABLE IF EXISTS customer;

CREATE TABLE beer (
	id 					varchar(36) NOT NULL,
	beer_name 			varchar(50) NOT NULL,
	beer_style 			tinyint NOT NULL,
	created_date 		datetime(6),
	price 				decimal(38,2) NOT NULL,
	quantity_on_hand 	int,
	upc 				varchar(255) NOT NULL,
	update_date 		datetime(6),
	version				int,
	primary key (id)
) engine=InnoDB;

CREATE TABLE customer (
	id 				varchar(36) NOT NULL,
	created_date 	datetime(6),
	name 			varchar(255),
	update_date 	datetime(6),
	version 		int,
	primary key (id)
) engine=InnoDB;