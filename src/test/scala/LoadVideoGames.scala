
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class LoadVideoGames extends Simulation{
  val httpProtocol = http
    .baseUrl("http://localhost:8084")
    .inferHtmlResources()
    .acceptHeader("application/json")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.9")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36")
//    .proxy(Proxy("localhost",9090))

  val headers_0 = Map(
    "Cache-Control" -> "no-cache",
    "Pragma" -> "no-cache",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
    "sec-ch-ua-mobile" -> "?0")



  val scn = scenario("LocalVideoGame")
    .exec(http("request_0")
      .get("/app/videogames")
      .headers(headers_0))

  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)

}
