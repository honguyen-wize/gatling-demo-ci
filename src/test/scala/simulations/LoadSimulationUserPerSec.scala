package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class LoadSimulationUserPerSec extends Simulation{
  val httpConf = http.baseUrl("http://localhost:8084/app/")
    .header("Accept","application/json")
    .proxy(Proxy("localhost",8866))

  val scn = scenario("Basic Load Simulation")
    .exec(getAllVideoGames())
////    .pause(2)
//    .exec(getSpecificVideoGame())
////    .pause(3)
//    .exec(getAllVideoGames())

  def getAllVideoGames() = {
    exec(http("Get all video games")
      .get("videogames")
      .check(status.is(200)))
  }

  def getSpecificVideoGame()= {
    exec(http("Get specific game")
      .get("videogames/2")
      .check(status.in(200 to 210)))
  }

  setUp(
    scn.inject(
      nothingFor(1.seconds)
//      ,constantUsersPerSec(2) during(10.seconds)
//      ,rampUsersPerSec(5) to (20) during(1.minutes)
//      ,heavisideUsers(60) during(15.seconds)
      ,incrementUsersPerSec(20)
        .times(3)
        .eachLevelLasting(10.seconds)
        .separatedByRampsLasting(20.seconds)
        .startingFrom(5)
    )
  .protocols(httpConf))
}
