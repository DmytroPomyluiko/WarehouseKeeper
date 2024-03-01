**WarehouseKeeper**
-----
Manager warehouse places
----
`username - admin
password - 12345`
## Contents
+ [Overview](#Overview)
+ [Use cases](#Use-cases)
+ [GRUD end points](#Rest-end-points) 
+ [Used Technologies](#Used-Technologies)
+ [Project structure](#Project-structure)
+ [Application startup](#Application-startup)

<a name="Overview"></a>
## Overview
WarehouseKeeper is a web service designed for the organization of warehouse spaces. It facilitates the management of storage cells, assignment of temporary owners for warehouse spaces, and provides comprehensive monitoring of the warehouse.
<br><br>Service includes such tools as:
- Email validation
- Password hashing (BCrypt)

## Use cases
When you enter the application website you enter like a guest, and you have access only to these actions:
* Login
* Register as a new user
* About

When you will log in as a user with role <b>"USER"</b>, you could perform these actions:
// TODO
* Logout

When you will log in as a user with role <b>"ADMIN"</b>, you could perform these actions:
* See all existing customers and storages
* Create, modify and delete
* Get user info by full name
* Search by Name(first 2 characters)
* Logout

<a name="Rest-end-points"></a>
## GRUD end points:
```
POST: /login - all

```

<a name="Used-Technologies"></a>
## Used Technologies
* Java version 17
* Spring boot(Spring web, security)
* Hibernate
* PostgresSQL
* Maven
* Maven Checkstyle Plugin

<a name="Project-structure"></a>
## Project structure
Project implemented refers to an n-tier structure and has three layers:

1. Data access layer (DAO)
1. Application layer (service)
1. Presentation layer (controllers)

Table relations

![Table relations](images/entitySchema.png)

<a name="Application-startup"></a>
## Application startup
1. Install Postgres
1. Load dependencies which are described in `pom.xml`
1. Change `username`, `password`, and `URL` values in the `resources/db.properties` file to open a connection with your database
1. Launch the application and start using it at `http://localhost:%your_port%`

