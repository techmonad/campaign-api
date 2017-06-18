package com.techmonad.api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import com.techmonad.service.ActorSystemProvider
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._


class CampaignApiIntegrationTest extends WordSpecLike with ScalatestRouteTest with Matchers with BeforeAndAfterAll {

  implicit val defaultTimeout = RouteTestTimeout(10.seconds)

  val campaignApi = new CampaignApi with ActorSystemProvider {
    val actorSystem = system
  }

  "CampaignApi routes" should {

    "get welcome message" in {
      Get("/") ~> campaignApi.apiRouteWithLogging ~> check {
        responseAs[String] shouldEqual "Welcome to Agoda api!!"
      }
    }
  }

  "disable a campaign" in {
    Get("/v1/HotelDealCampaign/flip?enabled=false") ~> campaignApi.apiRouteWithLogging ~> check {
      responseAs[String] shouldEqual "HotelDealCampaign has been disabled successfully!"
    }
  }

  "enable a campaign" in {
    Get("/v1/HotelDealCampaign/flip?enabled=true") ~> campaignApi.apiRouteWithLogging ~> check {
      responseAs[String] shouldEqual "HotelDealCampaign has been enabled successfully!"
    }
  }

  "refresh HotelDealCampaign detail" in {
    Get("/v1/HotelDealCampaign/refresh") ~> campaignApi.apiRouteWithLogging ~> check {
      responseAs[String] shouldEqual "HotelDealCampaign detail has been updated successfully"
    }
  }

  "get score when countryId exists " in {
    val requestJson = """[{"hotelId":1232 ,"countryId":10}]"""
    Post("/v1/HotelDealCampaign/score", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> campaignApi.apiRouteWithLogging ~> check {
      responseAs[String] shouldEqual """[{"countryId":10,"score":3}]"""
    }
  }

  "get score when hotelId exists " in {
    val requestJson = """[{"hotelId":12 ,"countryId":101010}]"""
    Post("/v1/HotelDealCampaign/score", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> campaignApi.apiRouteWithLogging ~> check {
      responseAs[String] shouldEqual """[{"hotelId":12,"score":5}]"""
    }
  }

  "get score when both exists " in {
    val requestJson = """[{"hotelId":12 ,"countryId":10}]"""
    Post("/v1/HotelDealCampaign/score", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> campaignApi.apiRouteWithLogging ~> check {
      responseAs[String] shouldEqual """[{"hotelId":12,"score":5}]"""
    }
  }

  "get list of scores " in {
    val requestJson = """[{"hotelId":1232 ,"countryId":10},{"hotelId":12 ,"countryId":101010}, {"hotelId":12 ,"countryId":10},{"hotelId":12211 ,"countryId":101010}]"""
    Post("/v1/HotelDealCampaign/score", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> campaignApi.apiRouteWithLogging ~> check {
      responseAs[String] shouldEqual """[{"countryId":10,"score":3},{"hotelId":12,"score":5},{"hotelId":12,"score":5}]"""
    }
  }


  override def afterAll() = {
    system.terminate()
  }
}