gatling-maven-plugin-demo
=========================

Simple showcase of a maven project using the gatling-maven-plugin.

To start the APIs server, run the commands

    $cd /Documents/Projects/automation/Gatling/VideoGameDB
    $mvn spring-boot:run

To test it out, simply execute the following command:

    $ mvn gatling:test -Dgatling.simulationClass=finalSimulation.VideoGameFullTest
    $ mvn gatling:test -Dgatling.simulationClass=finalSimulation.VideoGameFullTest -DRAM_USERS=20 -DRAM_DURATION=20 -DHEAVISIDE_USERS=100 -DHEAVISIDE_DURATION=20


or simply:

    $mvn gatling:test
