package com.techmonad.campaign

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

import scala.concurrent.Future


trait Campaign {

  private[campaign] def name: String

  private[campaign] def rules: Route

  private[campaign] def isEnabled: Future[Boolean]

  private[campaign] def refresh: Future[String]

  private[campaign] def flip(flag: Boolean): Future[String]

  private[campaign] def refreshRoute: Route = path("refresh") {
    complete(refresh)
  }

  private[campaign] def flipRoute: Route = path("flip") {
    parameter('enabled.as[Boolean]) { flag =>
      complete(flip(flag))
    }
  }

  def route: Route =
    pathPrefix(name) {
      flipRoute ~ authorizeAsync(isEnabled) {
        refreshRoute ~ rules
      }
    }

}

