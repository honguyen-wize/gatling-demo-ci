package demo_script

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class DemoRuntimeParameters extends Simulation{

  private def getProperty(propertyName: String, defaultValue: String)= {
    Option(System.getenv(propertyName)) // get from the env configuration
      .orElse(Option(System.getProperty(propertyName))) // get from the variable passed from the command line
      .getOrElse(defaultValue) // if nothing got, get the default value
  }

  def userCount: Int = getProperty("USERS", "100").toInt
  def ramDuration: Int = getProperty("RAM_DURATION", "10").toInt

  before{
    println(s"Running test with ${userCount} users")
    println(s"Ramping users over ${ramDuration} seconds")
  }

  val httpConf = http.baseUrl("http://192.168.1.247:8084/app/")
    .header("Accept","application/json")
    .proxy(Proxy("localhost",8866))

  val scn = scenario("Runtime parameter")
      .exec(getAllVideoGames())

  def getAllVideoGames() = {
    exec(http("Get all video games")
      .get("videogames")
      .check(status.is(200)))
  }

  setUp(
    scn.inject(
      nothingFor(3.seconds)
      ,rampUsers(userCount) during(ramDuration.seconds)
    )
  ).protocols(httpConf)

  // mvn gatling:test -Dgatling.simulationClass=demo_script.DemoRuntimeParameters
  // mvn gatling:test -Dgatling.simulationClass=demo_script.DemoRuntimeParameters -DUSERS=200 -DRAM_DURATION=20

}