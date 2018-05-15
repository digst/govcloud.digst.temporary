#!/usr/bin/env bash

java -cp /home/datascience/.ivy2/cache/org.scala-lang/scala-library/jars/scala-library-2.12.6.jar:streaming-producer-1.0-SNAPSHOT-jar-with-dependencies.jar  app.RunApp  /home/datascience/GovCloudApplications/src/test/resources/config/producer.properties /home/datascience/GovCloudApplications/src/test/resources/config/topics.properties /home/datascience/GovCloudApplications/src/test/resources/database/
