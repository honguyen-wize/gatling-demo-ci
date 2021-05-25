package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://computer-database.gatling.io") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .proxy(Proxy("localhost",8866))

  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
    .exec(http("Get all computers") // Get all computers
      .get("/"))
    .pause(5) // Note that Gatling has recorder real time pauses
    .exec(http("Get all macbook computers") // Get all macbook computers
      .get("/computers?f=macbook"))
    .pause(3)
    .exec(http("Get Macbook Pro") // Get Macbook Pro (id=6)
      .get("/computers/6"))
    .pause(3)
    .exec(http("Add a new computer") // Add a new computer
      .post("/computers")
      .formParam("""name""", """Beautiful Computer""") // Note the triple double quotes: used in Scala for protecting a whole chain of characters (no need for backslash)
      .formParam("""introduced""", """2012-05-30""")
      .formParam("""discontinued""", """""")
      .formParam("""company""", """37"""))

  setUp(
    scn.inject(
//      atOnceUsers(1),
      nothingFor(3.seconds)
      ,rampUsers(1) during(10.seconds)
    )
      .protocols(httpProtocol)
  )
}
