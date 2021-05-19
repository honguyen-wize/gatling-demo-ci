package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

class CodeReuseWithObjects extends Simulation{
  val httpConf = http.baseUrl("http://localhost:8084/app/")
    .header("Accept","application/json")
    .proxy(Proxy("localhost",8866))

  val scn = scenario("Video Game API Test with Reusable Methods")
    .exec(getAllVideoGames())
    .pause(2)
    .exec(getSpecificVideoGame())
    .pause(1,5)
    .exec(getAllVideoGames())

  def getAllVideoGames() = {
    repeat(3){
      exec(http("Get all video games")
        .get("videogames")
        .check(status.is(200)))
    }
  }

  def getSpecificVideoGame()= {
    repeat(2){
      exec(http("Get specific game")
        .get("videogames/3")
        .check(status.in(200 to 210)))
    }
  }

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
