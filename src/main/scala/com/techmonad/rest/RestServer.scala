package com.techmonad.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.techmonad.api.CampaignApi
import com.techmonad.logger.Logging
import com.techmonad.service.ActorSystemProvider


object RestServer extends App with Logging {

  implicit val system = ActorSystem()

  implicit val materializer = ActorMaterializer()

  val campaignApi = new CampaignApi with ActorSystemProvider {
    val actorSystem = system
  }

  info("RestServer has been started....")

  Http().bindAndHandle(campaignApi.apiRouteWithLogging, "0.0.0.0", 9000)
}