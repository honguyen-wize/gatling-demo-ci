package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CheckResponseBodyAndDebug extends Simulation {
  val httpConf = http.baseUrl("http://localhost:8084/app/")
    .header("Accept", "application/json")
    .proxy(Proxy("localhost", 8866))

  val scn = scenario("Check Json Path")

    .exec(http("Get all video games")
      .get("videogames")
      .check(jsonPath("$[3].id").saveAs("gameId")))

    .exec{mysession => println(mysession); mysession}

    .exec(http("Get Game by extracted id")
      .get("videogames/${gameId}")
      .check(jsonPath("$.name").is("Super Mario 64"))
      .check(bodyString.saveAs("response")))

    .exec{mysession => println(mysession("response").as[String]); mysession}

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
}