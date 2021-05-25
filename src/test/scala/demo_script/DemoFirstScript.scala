package demo_script

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class DemoFirstScript extends Simulation {
  //1 Http conf
  val httpConf = http.baseUrl("http://192.168.1.247:8084/app/")
    .header("Accept","application/json")
    .proxy(Proxy("localhost",8866))

  //2 Scenario
  val scn = scenario("My First Test")
    .exec(http("Get All Games").get("videogames"))
    .pause(2.seconds)
    .exec(http("Get One Game").get("videogames/9"))
    .pause(2.seconds)
  //3 Load Test Simulation Setup
  setUp(
    scn.inject(constantUsersPerSec(10) during(20.seconds))
      .protocols(httpConf)
  )
}
