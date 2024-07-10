# Tracks Rest API Case Study

Tracks REST API Case Study in Java using RestAssured

- `src` folder contains the Java code for the case study
- `postman` folder contains the saved collection for Postman REST GUI
- `editions` contains the code released with each edition of the book

For more details see the support page:

* [compendiumdev.co.uk/page/tracksrestsupport](http://compendiumdev.co.uk/page/tracksrestsupport)


## Tracks Docker

I now recommend using Docker to run Tracks, it seems easier than the Virtual Machine approach.

To run the v2.3.0 tests with Docker.

- Install Docker
- run the command to start tracks v 2.3.0 in Docker
  - `docker run -p 80:80 eviltester/tracks:2.3.0`
- visit `http://localhost:80` and create an admin user named `user` with a password `bitnami`
  - Change the URL in the code `TestEnvDefaults.java` in `test\java\api\version_2_3_0` from `` to `http://localhost:80`
    - If you created an admin user with a different username and password then change the username and password in the 


For more information on the docker images used here, see:

- https://github.com/eviltester/tracksdocker

## Mock Tracks

There are some sections in the code that use a mocktracks setup that used to be deployed to `compendiumdev.co.uk`

These are now available in a docker image.

Run with:

```
docker run -d -p 80:80 eviltester/mocktracks
```

Then the endpoints listed in the test code can be found at:

- http://localhost:80/apps/mocktracks/projectsjson.php
- http://localhost:80/apps/mocktracks/projectsxml.php
- http://localhost:80/apps/mocktracks/reflect.php

In the example code, replacing `http://compendiumdev.co.uk` with `http://localhost:80`

## Editions Folder

The `editions` folder contains the code published with, and released for, each edition of the book "Automating and Testing a REST API".

The main source may be updated to keep pace with updated versions of Java or supporting libraries.

Changes in main source that differ from the book source in editions:

- changes from `editions\edition001`
    - `pom.xml` amended to use target Java `1.14` rather than `1.7`
    - `pom.xml` amended to use updated version of Junit 4
      - `api\version_2_3_0\environmentconfig` changed so `TestEnvDefaults.java` uses `localhost:80` as the URL


## Deploy

To deploy:

`docker tag tracks eviltester/tracks`

`docker tag tracks eviltester/tracks:version`