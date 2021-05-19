gatling-maven-plugin-demo
=========================

Simple showcase of a maven project using the gatling-maven-plugin.

To start the APIs server, run the commands

    $cd /Documents/Projects/automation/Gatling/VideoGameDB
    $mvn spring-boot:run

To test it out, simply execute the following command:


    $mvn gatling:test -Dgatling.simulationClass=simulations.CsvFeeder

or simply:

    $mvn gatling:test
