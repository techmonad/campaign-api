package com.techmonad.campaign.state

import akka.actor.{Actor, ActorRef, Props}
import com.techmonad.json.JsonHelper._
import com.techmonad.logger.Logging
import com.techmonad.util.ConfigUtility

import scala.io.Source


/**
  * Handle all the state changes & Maintain latest state of HotelDeal Campaign
  */
class HotelDealInfoActor extends Actor with Logging {

  import HotelDealInfoActor._

  /**
    * States of HotelDeal Campaign
    */

  var isEnabled = true

  var hotelIds: Set[Int] = _

  var countryIds: Set[Int] = _

  var hotelScore: Int = _

  var countryScore: Int = _

  /**
    * ---- End -------
    */

  def receive: Receive = {
    case Flip(flag) =>
      flip(flag, sender)

    case Refresh =>
      refresh(sender())

    case Status =>
      sender ! Status(isEnabled)

    case ScoreRequest(hotelAndCountryIds) =>
      debug("Getting request for score " + hotelAndCountryIds)
      sender ! getScore(hotelAndCountryIds)
    case invalidMessage =>
      warn(s"Message is invalid [$invalidMessage]")

  }

  def flip(flag: Boolean, senderRef: ActorRef) = {
    isEnabled = flag
    val message =
      if (flag)
        "HotelDealCampaign has been enabled successfully!"
      else
        "HotelDealCampaign has been disabled successfully!"
    senderRef ! message
  }

  def refresh(senderRef: ActorRef) = {
    val message = if (init())
      "HotelDealCampaign detail has been updated successfully"
    else
      "Not able to update HotelDealCampaign detail"
    senderRef ! message
  }

  def init() =
    try {
      val detailJson = Source.fromFile(detailFilePath).getLines().mkString
      info(s"campaign detail json [$detailJson]")
      val hotelDealInfo = parse(detailJson).extract[HotelDealInfo]
      hotelIds = hotelDealInfo.hotelIds.toSet
      countryIds = hotelDealInfo.countryIds.toSet
      hotelScore = hotelDealInfo.hotelScore
      countryScore = hotelDealInfo.countryScore
      true
    } catch {
      case ex: Exception =>
        error("Error in initialization of HotelDeal campaign", ex)
        false

    }

  def getScore(hotelAndCountryIds: List[HotelAndCountryDetail]): List[Score] =
    hotelAndCountryIds.map { case req@HotelAndCountryDetail(hotelId, countryId) =>
      val currentHotelScore =
        if (hotelIds.contains(hotelId))
          Some(hotelScore)
        else
          None
      val currentCountryScore =
        if (countryIds.contains(countryId))
          Some(countryScore)
        else
          None
      Score(currentHotelScore, currentCountryScore, req)
    }

  override def preStart(): Unit = {
    init()
  }

}

object HotelDealInfoActor {

  val detailFilePath = ConfigUtility.getStringConf("campaign.hoteldeal.detail.file.path")

  def props() = Props[HotelDealInfoActor]

  case class ScoreRequest(hotelAndCountryIds: List[HotelAndCountryDetail])

  case class HotelAndCountryDetail(hotelId: Int, countryId: Int)

  case class Score(hotelScore: Option[Int], countryScore: Option[Int], request: HotelAndCountryDetail)

  case class Status(flag: Boolean)

  case class Flip(flag: Boolean)

  case class HotelDealInfo(hotelIds: List[Int], countryIds: List[Int], hotelScore: Int, countryScore: Int)

  case object Refresh

  case object Status

}
