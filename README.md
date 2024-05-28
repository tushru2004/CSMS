# CSMS

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Build Status](https://travis-ci.org/username/repo.svg?branch=main)](https://travis-ci.org/username/repo)
[![GitHub issues](https://img.shields.io/github/issues/username/repo.svg)](https://github.com/username/repo/issues)

## Table of Contents

- [CSMS](#CSMS)
    - [About](#about)
    - [Architecture](#architecture)
    - [Getting Started](#getting-started)
        - [Prerequisites](#prerequisites)
        - [Installation](#installation)
    - [Usage](#usage)


## About

ChargePoint assignment. Consists of two services that used to authenticate a user 
for charging a car.

## Architecture
Application consists of two separate services, transaction_service and authentication_service are in their sub folders respectively.
Docker compose is used to instantiate all dependent services like kafka.  

## Limitations/Assumptions
Invalid Case means  -- Identifier is invalid. Does not meet the identifier length requirements<br>
Unknown Case means -- Identifier is valid but not present/registered in the system<br>
Initial Start up for docker-compose up --build command might take about 2 min.

## Getting Started

### Prerequisites

```sh
Postman - For ease of testing
MAC - Great to have, but not necessary
Docker version 25.0.3
Docker Compose version v2.24.5-desktop.1
```

### installation
```dockerfile
git clone git@github.com:tushru2004/CSMS.git
cd CSMS
docker-compose up --build
# Give 2 min for services to start up. Might be longer depending on internet speed and processor speed
```
### Usage
### Accepted case -- User valid and card works

```
curl --location 'localhost:4040/transaction/authorize' \
--header 'Content-Type: application/json' \
--data '{
"stationUuid": "25aac66b-6051-478a-95e2-6d3aa343b025",
"driverIdentifier": {"id": "V4Lk7C9QzN2tMh0GJwXcR3u1pBv5YsWnK8j3T2"}
}'
```
![Accepted](Accepted.png)

### Rejected -- Card doesn't work
```
curl --location 'localhost:4040/transaction/authorize' \
--header 'Content-Type: application/json' \
--data '{
"stationUuid": "25aac66b-6051-478a-95e2-6d3aa343b025",
"driverIdentifier": {"id": "xY4B8zP0mN5qKdL2Hf9Z"}
}'
```
![Rejected](Rejected.png)

### Unknown -- Identifier is valid but not present/registered in the system
```
curl --location 'localhost:4040/transaction/authorize' \
--header 'Content-Type: application/json' \
--data '{
"stationUuid": "25aac66b-6051-478a-95e2-6d3aa343b025",
"driverIdentifier": {"id": "ZLLk7C9QzN2tMh0GJwXcR3u1pBv5YsWfnK8j3RT"}
}'
```
![Unknown](Unknown.png)

### Invalid -- Identifier is invalid. Does not meet the identifier length requirements
```
curl --location 'localhost:4040/transaction/authorize' \
--header 'Content-Type: application/json' \
--data '{
"stationUuid": "25aac66b-6051-478a-95e2-6d3aa343b025",
"driverIdentifier": {"id": "tt34235"}
}'
```
![Invalid](Invalid.png)
