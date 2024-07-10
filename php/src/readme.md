This is the mock tracks php endpoints that used to be at `compendiumdev.co.uk/apps/mocktracks`.

These have been added to a docker image to make them easier to access.

## Run from docker hub

```
docker run -d -p 80:80 eviltester/mocktracks
```

## Run from source

```
$ docker build -t mocktracks .
$ docker run -d -p 80:80 --name mocktracks mocktracks
```

Then the urls mentioned in the book can be used to mock tracks.

But instead of http://compendiumdev.co.uk/apps/mocktracks/projectsjson.php it would be

- http://localhost:80/apps/mocktracks/projectsjson.php
- http://localhost:80/apps/mocktracks/projectsxml.php
- http://localhost:80/apps/mocktracks/reflect.php


## Deploy

```
docker tag mocktracks eviltester/mocktracks:latest
```