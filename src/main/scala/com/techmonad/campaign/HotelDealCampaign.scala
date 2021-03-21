package com.techmonad.campaign


import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import com.techmonad.campaign.state.HotelDealInfoActor
import com.techmonad.constant.Constants._
import com.techmonad.json.JsonHelper._
import com.techmonad.logger.Logging
import com.techmonad.service.ActorSystemProvider

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

trait HotelDealCampaign extends Campaign with Logging {
  self: ActorSystemProvider =>

  import HotelDealInfoActor._
  import actorSystem.dispatcher

  implicit val timeout = Timeout(10 second)

  lazy private val campaignDetail = actorSystem.actorOf(props())

  private[campaign] def name: String = "HotelDealCampaign"

  private[campaign] def isEnabled: Future[Boolean] = ask(campaignDetail, Status).mapTo[Status].map(_.flag)

  private[campaign] def refresh: Future[String] = ask(campaignDetail, Refresh).mapTo[String]

  private[campaign] def flip(flag: Boolean): Future[String] = ask(campaignDetail, Flip(flag)).mapTo[String]


  private[campaign] def rules = post {
    path("score") {
      entity(as[String]) { request =>
        complete {
          process(request)
        }
      }
    }
  }

  private def getScore(scoreRequest: ScoreRequest): Future[List[Score]] =
    ask(campaignDetail, scoreRequest).mapTo[List[Score]]

  private def process(request: String): Future[HttpResponse] =
    jsonParser(request).fold(error => Future.successful(HttpResponse(StatusCodes.BadRequest, entity = error)),
      requestParams => {
        val scores = getScore(requestParams).map {
          _.map {
            _ match {
              case Score(Some(hotelScore), Some(countryScore), req) =>
                if (hotelScore > countryScore)
                  Map(HOTEL_ID -> req.hotelId, SCORE -> hotelScore)
                else
                  Map(COUNTRY_ID -> req.hotelId, SCORE -> countryScore)
              case Score(Some(hotelScore), None, req) =>
                Map(HOTEL_ID -> req.hotelId, SCORE -> hotelScore)
              case Score(None, Some(countryScore), req) =>
                Map(COUNTRY_ID -> req.countryId, SCORE -> countryScore)
              case _ => Map.empty[String, Int]
            }
          }
        }
        scores.map { scoreList => HttpResponse(StatusCodes.OK, entity = write(scoreList.filter(_.size > 0))) }
      }
    )


  private def jsonParser(json: String): Either[String, ScoreRequest] =
    try {
      debug("request json "+ json)
      val scoreRequest = parse(json).extract[List[HotelAndCountryDetail]]
      Right(ScoreRequest(scoreRequest))
    } catch {
      case ex: Exception =>
        error(s"Invalid json  [$json]", ex)
        Left("Request json is not valid")
    }

}

