package com.techmonad.campaign

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import com.techmonad.service.ActorSystemProvider
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatest.BeforeAndAfterAll

import scala.concurrent.duration._

class HotelDealCampaignTest extends AnyWordSpecLike with ScalatestRouteTest with Matchers with BeforeAndAfterAll {

  implicit val defaultTimeout = RouteTestTimeout(10.seconds)

  val hotelDealCampaignApi = new HotelDealCampaign with ActorSystemProvider {
    val actorSystem = system
  }

  "HotelDealCampaignApi routes" should {

    "disable campaign" in {
      Get("/HotelDealCampaign/flip?enabled=false") ~> hotelDealCampaignApi.route ~> check {
        responseAs[String] shouldEqual "HotelDealCampaign has been disabled successfully!"
      }
    }

    "enable campaign" in {
      Get("/HotelDealCampaign/flip?enabled=true") ~> hotelDealCampaignApi.route ~> check {
        responseAs[String] shouldEqual "HotelDealCampaign has been enabled successfully!"
      }
    }

    "refresh  campaign detail" in {
      Get("/HotelDealCampaign/refresh") ~> hotelDealCampaignApi.route ~> check {
        responseAs[String] shouldEqual "HotelDealCampaign detail has been updated successfully"
      }
    }

    "get score when countryId exists " in {
      val requestJson = """[{"hotelId":1232 ,"countryId":10}]"""
      Post("/HotelDealCampaign/score", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> hotelDealCampaignApi.route ~> check {
        responseAs[String] shouldEqual """[{"countryId":10,"score":3}]"""
      }
    }

    "get score when hotelId exists " in {
      val requestJson = """[{"hotelId":12 ,"countryId":101010}]"""
      Post("/HotelDealCampaign/score", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> hotelDealCampaignApi.route ~> check {
        responseAs[String] shouldEqual """[{"hotelId":12,"score":5}]"""
      }
    }

    "get score when both exists " in {
      val requestJson = """[{"hotelId":12 ,"countryId":10}]"""
      Post("/HotelDealCampaign/score", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> hotelDealCampaignApi.route ~> check {
        responseAs[String] shouldEqual """[{"hotelId":12,"score":5}]"""
      }
    }

    "get list of scores " in {
      val requestJson = """[{"hotelId":1232 ,"countryId":10},{"hotelId":12 ,"countryId":101010}, {"hotelId":12 ,"countryId":10},{"hotelId":12211 ,"countryId":101010}]"""
      Post("/HotelDealCampaign/score", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> hotelDealCampaignApi.route ~> check {
        responseAs[String] shouldEqual """[{"countryId":10,"score":3},{"hotelId":12,"score":5},{"hotelId":12,"score":5}]"""
      }
    }

  }

  override def afterAll() = {
    system.terminate()
  }

}
