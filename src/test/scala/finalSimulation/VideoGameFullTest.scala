package finalSimulation

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random
import scala.concurrent.duration._

class VideoGameFullTest extends Simulation {

  val httpConf = http
    .baseUrl("http://192.168.1.247:8084/app/")
    .header("Accept", "application/json")
//    .proxy(Proxy("localhost",8866))

  /*** Variables ***/
  // runtime variables
  def ramUserCount: Int = getProperty("RAM_USERS", "20").toInt
  def rampDuration: Int = getProperty("RAM_DURATION", "10").toInt
  def heavisideUserCount: Int = getProperty("HEAVISIDE_USERS", "100").toInt
  def heavisideDuration: Int = getProperty("HEAVISIDE_DURATION", "20").toInt

  // other variables
  var idNumbers = (11 to 10000).iterator
  val rnd = new Random()
  val now = LocalDate.now()
  val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  /*** Helper Methods ***/
  private def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  def randomString(length: Int) = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  def getRandomDate(startDate: LocalDate, random: Random): String = {
    startDate.minusDays(random.nextInt(30)).format(pattern)
  }

  /*** Custom Feeder ***/
  val customFeeder = Iterator.continually(Map(
    "gameId" -> idNumbers.next(),
    "name" -> ("Game-" + randomString(5)),
    "releaseDate" -> getRandomDate(now, rnd),
    "reviewScore" -> rnd.nextInt(100),
    "category" -> ("Category-" + randomString(6)),
    "rating" -> ("Rating-" + randomString(4))
  ))

  /*** Before ***/
  before {
    println(s"Running test with RAM_USERS: ${ramUserCount} users")
    println(s"Running test with RAMP_DURATION: ${rampDuration} seconds")
    println(s"Running test with HEAVISIDE_USERS: ${heavisideUserCount} users")
    println(s"Running test with HEAVISIDE_DURATION: ${heavisideDuration} seconds")
  }

  /*** HTTP Calls ***/
  def getAllVideoGames() = {
    exec(
      http("Get All Video Games")
        .get("videogames")
        .check(status.is(200)))
  }

  def postNewGame() = {
    feed(customFeeder).
      exec(http("Post New Game")
        .post("videogames")
        .body(ElFileBody("bodies/newGameTemplateBody.json")).asJson //template file goes in gating/resources/bodies
        .check(status.is(200)))
  }

  def getLastPostedGame() = {
    exec(http("Get Last Posted Game")
      .get("videogames/${gameId}")
      .check(jsonPath("$.name").is("${name}"))
      .check(status.is(200)))
  }

  def deleteLastPostedGame() = {
    exec(http("Delete Last Posted Game")
      .delete("videogames/${gameId}")
      .check(status.is(200)))
  }

  /*** Scenario Design ***/
  val scn = scenario("Video Game DB")
//    .forever() {
      .exec(getAllVideoGames())
        .pause(2)
        .exec(postNewGame())
        .pause(2)
        .exec(getLastPostedGame())
        .pause(2)
        .exec(deleteLastPostedGame())
//    }

  /*** Setup Load Simulation ***/
  setUp(
    scn.inject(
      nothingFor(3.seconds)
      ,rampUsers(ramUserCount) during (rampDuration.seconds)
      ,heavisideUsers(heavisideUserCount) during(heavisideDuration.seconds)
    )
  ).protocols(httpConf)
//    .maxDuration(120.seconds)
      .assertions(
        global.responseTime.max.lt(500),
        global.failedRequests.percent.lt(1)
      )


  /*** After ***/
  after {
    println("Stress test completed")
  }

}
