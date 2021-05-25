package demo_script

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class DemoLoadSimulation extends Simulation{
  val httpConf = http.baseUrl("http://192.168.1.247:8084/app/")
    .header("Accept","application/json")
    .proxy(Proxy("localhost",8866))

  val scn = scenario("Basic Load Simulation")
    .exec(getAllVideoGames())
    .pause(2)
    .exec(getSpecificVideoGame())
    .pause(2)
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
      nothingFor(2.seconds)
      ,atOnceUsers(10)
//      ,rampUsers(500) during(30.seconds)
//      ,constantUsersPerSec(10) during(20.seconds)
//      ,rampUsersPerSec(5) to (20) during(1.minutes)
//      ,heavisideUsers(500) during(60.seconds)
//      ,incrementUsersPerSec(20)
//        .times(3)
//        .eachLevelLasting(10.seconds)
//        .separatedByRampsLasting(20.seconds)
//        .startingFrom(5)
    )
  .protocols(httpConf))
}
