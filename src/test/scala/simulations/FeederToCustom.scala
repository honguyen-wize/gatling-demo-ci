package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FeederToCustom extends Simulation{
  val httpConf = http.baseUrl("http://localhost:8084/app/")
    .header("Accept","application/json")
    .proxy(Proxy("localhost",8866))

  var idNumbers = (11 to 1000).iterator

  val feederCustom = Iterator.continually(Map("gameId" -> idNumbers.next()))

  def getSpecificVideoGameFromCsv() = {
    repeat(10){
      feed(feederCustom)
        .exec(http("Get Specific Game")
          .get("videogames/${gameId}")
          .check(status.is(200))
          .check(bodyString.saveAs("response")))

        .exec{mysession => println(mysession("response").as[String]); mysession}


//      .exec(http("Delete Last Posted Game")
//          .delete("videogames/${gameId}")
//          .check(status.is(200)))


//        .pause(1)
    }
  }

  val scn = scenario("CSV Feeder Test")
    .exec(getSpecificVideoGameFromCsv())

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
