DATA_DIR := ${CURDIR}/src/main/resources/data

default: build

build:
	mvn clean package

run:
	mvn spring-boot:run

data:
	mkdir -p $(DATA_DIR)
	curl https://raw.githubusercontent.com/jpatokal/openflights/master/data/airports.dat -o $(DATA_DIR)/airports.dat
	curl https://raw.githubusercontent.com/jpatokal/openflights/master/data/routes.dat -o $(DATA_DIR)/routes.dat