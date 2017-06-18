package com.techmonad.service

import akka.actor.ActorSystem

trait ActorSystemProvider {

  val actorSystem: ActorSystem

}
