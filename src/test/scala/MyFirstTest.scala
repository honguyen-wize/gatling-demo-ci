import io.gatling.core.Predef._
import io.gatling.http.Predef._

class MyFirstTest extends Simulation{

  //1 Http conf
  val httpConf = http.baseUrl("http://localhost:8084/app/")
    .header("Accept","application/json")
    .proxy(Proxy("localhost",8866))

  //2 Scenario
  val scn = scenario("My First Test")
    .exec(http("Get All Games").get("videogames"))
    .exec(http("Get One Game").get("videogames/9"))

  //3 Setup
  setUp(
    scn.inject(atOnceUsers(10))
      .protocols(httpConf)
  )

}
