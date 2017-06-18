package com.techmonad.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.{DebuggingDirectives, LogEntry}
import akka.http.scaladsl.server.{Route, RouteResult}
import com.techmonad.campaign.HotelDealCampaign
import com.techmonad.service.ActorSystemProvider

/**
  * mixed all the campaigns trait
  */
trait CampaignApi extends HotelDealCampaign {
  self: ActorSystemProvider =>


  private def homeRoute: Route = pathSingleSlash {
    complete("Welcome to Campaign  api!!")
  }

  def apiRoute = homeRoute ~ pathPrefix("v1") {
    //aggregate all the campaigns here with the help of ~ method
    super[HotelDealCampaign].route
  }

  def apiRouteWithLogging = DebuggingDirectives.logRequestResult(logRequestAndResponse _)(apiRoute)

  /** *
    *
    * Enable logging
    * Log every request & response
    */
  def logRequestAndResponse(req: HttpRequest): RouteResult => Option[LogEntry] = {
    case RouteResult.Complete(res) =>
      warn(s"Request : $req")
      warn(s"Response : $res")
      None
    case rejected =>
      warn(s"Rejected Request : $req")
      None
  }

}
