package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

class CheckResponseCode extends Simulation{
  val httpConf = http.baseUrl("http://localhost:8084/app/")
    .header("Accept","application/json")
    .proxy(Proxy("localhost",8866))

  val scn = scenario("Video Game API Test With Pause")

    .exec(http("Get all video games - 1st call")
      .get("videogames")
        .check(status.is(200)))
    .pause(2)

    .exec(http("Get specific game")
      .get("videogames/3")
        .check(status.in(200 to 210)))
    .pause(1,5)

    .exec(http("Get all video game - 2nd call")
      .get("videogames")
        .check(status.not(400), status.not(500)))
    .pause(3000.milliseconds)

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
