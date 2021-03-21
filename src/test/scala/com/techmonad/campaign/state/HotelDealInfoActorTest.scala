package com.techmonad.campaign.state

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestActors, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike


class HotelDealInfoActorTest
  extends TestKit(ActorSystem("HotelDealInfoActorTest"))
    with ImplicitSender
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  import HotelDealInfoActor._

  val hotelDealInfoActor: TestActorRef[HotelDealInfoActor] = TestActorRef(props())

  "HotelDealInfoActor " should {

    "get current status of campaign" in {
      hotelDealInfoActor ! Status
      assert(hotelDealInfoActor.underlyingActor.isEnabled)
      expectMsg(Status(true))

    }

    "flip the status of campaign " in {
      assert(hotelDealInfoActor.underlyingActor.isEnabled)
      hotelDealInfoActor ! Flip(false)
      assert(!hotelDealInfoActor.underlyingActor.isEnabled)
      expectMsg("HotelDealCampaign has been disabled successfully!")
    }

    "refresh the state" in {
      hotelDealInfoActor ! Refresh
      expectMsg("HotelDealCampaign detail has been updated successfully")
    }

    "get score when hotel id exist " in {
      hotelDealInfoActor ! ScoreRequest(List(HotelAndCountryDetail(12, 90909)))
      expectMsg(List(Score(Some(5), None, HotelAndCountryDetail(12, 90909))))
    }

    "get score when  country id exist " in {
      hotelDealInfoActor ! ScoreRequest(List(HotelAndCountryDetail(43434, 10)))
      expectMsg(List(Score(None, Some(3), HotelAndCountryDetail(43434, 10))))
    }

    "get score when both exits" in {
      hotelDealInfoActor ! ScoreRequest(List(HotelAndCountryDetail(23, 14)))
      expectMsg(List(Score(Some(5), Some(3), HotelAndCountryDetail(23, 14))))

    }

  }


  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }


}
