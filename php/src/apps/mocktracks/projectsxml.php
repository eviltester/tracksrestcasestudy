<?php
header('Content-Type: application/xml');
$xml = <<< EOH
<?xml version="1.0" encoding="UTF-8"?>
<projects type="array">
    <project>
        <id type="integer">1</id>
        <name>A New Projectaniheeiadtatd</name>
        <position type="integer">0</position>
        <description nil="true"/>
        <state>active</state>
        <created-at type="dateTime">2017-06-27T12:25:26+01:00</created-at>
        <updated-at type="dateTime">2017-06-27T12:25:26+01:00</updated-at>
        <default-context-id type="integer" nil="true"/>
        <completed-at type="dateTime" nil="true"/>
        <default-tags nil="true"/>
        <last-reviewed type="dateTime" nil="true"/>
    </project>
    <project>
        <id type="integer">3</id>
        <name>the new name aniheeiaosono</name>
        <position type="integer">1</position>
        <description nil="true"/>
        <state>active</state>
        <created-at type="dateTime">2017-06-27T12:25:27+01:00</created-at>
        <updated-at type="dateTime">2017-06-27T12:25:29+01:00</updated-at>
        <default-context-id type="integer" nil="true"/>
        <completed-at type="dateTime" nil="true"/>
        <default-tags nil="true"/>
        <last-reviewed type="dateTime" nil="true"/>
    </project>
    <project>
        <id type="integer">5</id>
        <name>A New Projectaniheeiaoaees</name>
        <position type="integer">2</position>
        <description nil="true"/>
        <state>active</state>
        <created-at type="dateTime">2017-06-27T12:25:31+01:00</created-at>
        <updated-at type="dateTime">2017-06-27T12:25:31+01:00</updated-at>
        <default-context-id type="integer" nil="true"/>
        <completed-at type="dateTime" nil="true"/>
        <default-tags nil="true"/>
        <last-reviewed type="dateTime" nil="true"/>
    </project>
    <project>
        <id type="integer">6</id>
        <name>A New Projectaniheeidrdhtd</name>
        <position type="integer">3</position>
        <description nil="true"/>
        <state>active</state>
        <created-at type="dateTime">2017-06-27T12:27:42+01:00</created-at>
        <updated-at type="dateTime">2017-06-27T12:27:42+01:00</updated-at>
        <default-context-id type="integer" nil="true"/>
        <completed-at type="dateTime" nil="true"/>
        <default-tags nil="true"/>
        <last-reviewed type="dateTime" nil="true"/>
    </project>
    <project>
        <id type="integer">8</id>
        <name>the new name aniheeidrettt</name>
        <position type="integer">4</position>
        <description nil="true"/>
        <state>active</state>
        <created-at type="dateTime">2017-06-27T12:27:42+01:00</created-at>
        <updated-at type="dateTime">2017-06-27T12:27:45+01:00</updated-at>
        <default-context-id type="integer" nil="true"/>
        <completed-at type="dateTime" nil="true"/>
        <default-tags nil="true"/>
        <last-reviewed type="dateTime" nil="true"/>
    </project>
    <project>
        <id type="integer">10</id>
        <name>A New Projectaniheeidrrhad</name>
        <position type="integer">5</position>
        <description nil="true"/>
        <state>active</state>
        <created-at type="dateTime">2017-06-27T12:27:46+01:00</created-at>
        <updated-at type="dateTime">2017-06-27T12:27:46+01:00</updated-at>
        <default-context-id type="integer" nil="true"/>
        <completed-at type="dateTime" nil="true"/>
        <default-tags nil="true"/>
        <last-reviewed type="dateTime" nil="true"/>
    </project>
</projects>
EOH;
echo($xml);
?>