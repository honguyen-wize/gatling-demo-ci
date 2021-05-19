package simulations
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random
class CustomFeederJsonTemplate extends Simulation{
  val httpConf = http.baseUrl("http://localhost:8084/app/")
    .header("Accept","application/json")
    .proxy(Proxy("localhost",8866))

  var idNumbers = (11 to 13).iterator
  val myRandom = new Random()
  val now = LocalDate.now()
  val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  def randomString(length: Int)= {
    myRandom.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  def getRandomDate(startDate: LocalDate, random: Random) = {
    startDate.minusDays(random.nextInt(30)).format(pattern)
  }

  val customFeeder = Iterator.continually(Map(
    "gameId" -> idNumbers.next(),
    "name" -> ("Game-" + randomString(5)),
    "releaseDate" -> getRandomDate(now, myRandom),
    "reviewScore" -> myRandom.nextInt(100),
    "category" ->  ("category-" + randomString(6)),
    "rating" -> ("rating-" + randomString(4)),
  ))

  def postNewGame () = {
    repeat(3){
      feed(customFeeder)
        .exec(http("Post new game with json template")
          .post("/videogames")
          .body(ElFileBody("bodies/newGameTemplateBody.json"))
          .asJson
          .check(status.is(200)))
        .pause(1)
    }
  }

  var scn = scenario("POST NEW GAME WITH JSON TEMPLATE")
    .exec(postNewGame())

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
