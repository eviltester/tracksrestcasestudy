<?php
header('Content-Type: application/json');
$json = <<< EOH
{
  "projects": {
    "project": [
      {
        "id":  1,
        "name": "A New Projectaniheeiadtatd",
        "position": 0,
        "description": "",
        "state": "active",
        "created-at": "2017-06-27T12:25:26+01:00",
        "updated-at": "2017-06-27T12:25:26+01:00"
      },
      {
        "id": 3,
        "name": "the new name aniheeiaosono",
        "position": 1,
        "description": "",
        "state": "active",
        "created-at": "2017-06-27T12:25:27+01:00",
        "updated-at": "2017-06-27T12:25:29+01:00"
      },
      {
        "id": 5,
        "name": "A New Projectaniheeiaoaees",
        "position": 2,
        "description": "",
        "state": "active",
        "created-at": "2017-06-27T12:25:31+01:00",
        "updated-at": "2017-06-27T12:25:31+01:00"
      },
      {
        "id": 6,
        "name": "A New Projectaniheeidrdhtd",
        "position": 3,
        "description": "",
        "state": "active",
        "created-at": "2017-06-27T12:27:42+01:00",
        "updated-at": "2017-06-27T12:27:42+01:00"
      },
      {
        "id": 8,
        "name": "the new name aniheeidrettt",
        "position": 4,
        "description": "",
        "state": "active",
        "created-at": "2017-06-27T12:27:42+01:00",
        "updated-at": "2017-06-27T12:27:45+01:00"
      },
      {
        "id": 10,
        "name": "A New Projectaniheeidrrhad",
        "position": 5,
        "description": "",
        "state": "active",
        "created-at": "2017-06-27T12:27:46+01:00",
        "updated-at": "2017-06-27T12:27:46+01:00"
      }
    ]
  }
}
EOH;
echo($json);
?>