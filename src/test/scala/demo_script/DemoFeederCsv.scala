package demo_script

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DemoFeederCsv extends Simulation{
  val httpConf = http.baseUrl("http://192.168.1.247:8084/app/")
    .header("Accept","application/json")
    .proxy(Proxy("localhost",8866))

  val feederCSV = csv("testdata/gameCsvFile.csv").circular

  def getSpecificVideoGameFromCsv() = {
    repeat(10){
      feed(feederCSV)
        .exec(http("Get Specific Game")
          .get("videogames/${gameId}")
          .check(jsonPath("$.name").is("${gameName}"))
          .check(status.is(200))
        )
        .pause(1)
    }
  }

  val scn = scenario("CSV Feeder Test")
      .exec(getSpecificVideoGameFromCsv())

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
