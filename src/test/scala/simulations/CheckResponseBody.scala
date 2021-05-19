package simulations
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CheckResponseBody extends Simulation {
  val httpConf = http.baseUrl("http://localhost:8084/app/")
    .header("Accept", "application/json")
    .proxy(Proxy("localhost", 8866))

  val scn = scenario("Check Json Path")

    .exec(http("Get specific game")
      .get("videogames/3")
      .check(jsonPath("$.name").is("Tetris")))

      .exec(http("Get all video games")
        .get("videogames")
        .check(jsonPath("$[3].id").saveAs("gameId")))

      .exec(http("Get Game by extracted id")
        .get("videogames/${gameId}")
        .check(jsonPath("$.name").is("Super Mario 64")))

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
}