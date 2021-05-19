package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

class AddPauseTime extends Simulation{
  val httpConf = http.baseUrl("http://localhost:8084/app/")
    .header("Accept","application/json")
    .proxy(Proxy("localhost",8866))

  val scn = scenario("Video Game API Test With Pause")

    .exec(http("Get all video games - 1st call")
    .get("videogames"))
    .pause(3)

    .exec(http("Get specific game")
    .get("videogames/3"))
    .pause(1,10)

    .exec(http("Get all video game - 2nd call")
    .get("videogames"))
    .pause(3000.milliseconds)

  setUp(
    scn.inject(atOnceUsers(2))
  ).protocols(httpConf)

}
