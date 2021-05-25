package demo_script

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class DemoCheckResponseBodyAndExtract extends Simulation{
  val httpConf = http.baseUrl("http://192.168.1.247:8084/app/")
    .header("Accept", "application/json")
    .proxy(Proxy("localhost", 8866))

  val extractedIndex = "2";
  val expectedExtractedGameName = "Tetris";

  val scn = scenario("Check Json Path")
    .exec(http("Get all video games")
      .get("videogames")
      .check(jsonPath("$["+extractedIndex+"].id").saveAs("gameId")) // extract game id at the extractedIndex
    )
    .exec(http("Get Game by extracted id")
      .get("videogames/${gameId}") // get specific game from the extracted game id
      .check(jsonPath("$.name").is(expectedExtractedGameName)) // check game name in the response body
    )

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
