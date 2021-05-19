package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class LoadSimulationBasic extends Simulation{
  val httpConf = http.baseUrl("http://localhost:8084/app/")
    .header("Accept","application/json")
    .proxy(Proxy("localhost",8866))

  val scn = scenario("Basic Load Simulation")
    .exec(getAllVideoGames())
////    .pause(1)
//    .exec(getSpecificVideoGame())
////    .pause(1)
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
//      ,atOnceUsers(10)
//      ,nothingFor(3.seconds)
      ,rampUsers(60) during(30.seconds)
    )
  .protocols(httpConf))
}
