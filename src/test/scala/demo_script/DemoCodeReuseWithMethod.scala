package demo_script

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DemoCodeReuseWithMethod extends Simulation{
  val httpConf = http.baseUrl("http://192.168.1.247:8084/app/")
    .header("Accept","application/json")
    .proxy(Proxy("localhost",8866))

  val scn = scenario("Video Game API Test with Reusable Methods")
    .exec(getAllVideoGames())
    .pause(2)
    .exec(getSpecificVideoGame())
    .pause(2)
    .exec(getAllVideoGames())
    .pause(2)

  def getAllVideoGames() = {
    repeat(2){
      exec(http("Get all video games")
        .get("videogames")
        .check(status.is(200)))
    }
  }

  def getSpecificVideoGame()= {
    repeat(3){
      exec(http("Get specific game")
        .get("videogames/3")
        .check(status.in(200 to 210)))
    }
  }

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
