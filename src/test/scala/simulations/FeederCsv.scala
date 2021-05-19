package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FeederCsv extends Simulation{
  val httpConf = http.baseUrl("http://localhost:8084/app/")
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
          .check(bodyString.saveAs("response")))

        .exec{mysession => println(mysession("response").as[String]); mysession}

        .pause(1)
    }
  }

  val scn = scenario("CSV Feeder Test")
      .exec(getSpecificVideoGameFromCsv())

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
