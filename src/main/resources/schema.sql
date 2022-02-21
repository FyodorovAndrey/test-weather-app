CREATE TABLE IF NOT EXISTS weather_statistic
(
    id          BIGINT NOT NULL,
    city_name   VARCHAR (255),
    date        DATE ,
    temperature DOUBLE,
    PRIMARY KEY (id)
);

CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1;