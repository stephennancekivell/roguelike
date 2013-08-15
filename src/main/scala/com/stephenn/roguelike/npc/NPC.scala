package com.stephenn.roguelike.npc

import com.stephenn.roguelike._
import com.stephenn.roguelike.model._
import com.badlogic.gdx.math.Vector2

trait NPC extends Character {
  var location: Point
  val world: WorldTrait
  
  def addToWorld = {
    world.getTile(location).npc = Some(this)
    world.npcs = world.npcs ++ Seq(this)
  }
  
  implicit val floatToInt = Util.floatToInt
  
  def die {
    world.getTile(location).npc = None
    world.npcs = world.npcs.filter(_ != this)
  }
  
  def stepTowards(to: Vector2, from: Vector2) = {
    val nearVectors = world.neighbouringVectorsInWorld(from)
    val walkable = nearVectors.map(v => v -> world.getTile(v)).filter {case (v,t) => t.isWalkable}
    
    val costs = walkable.map { case (v,t) =>
      (v.dst(to), v,t)
    }
    
    val sortedCosts = costs.sortBy { x => x._1 }
    sortedCosts.headOption.map(_._2)
  }
  
  def goTo(p: Point) {
    world.getTile(location).npc = None
    location = p
    world.getTile(p).npc = Some(this)
  }
  
  def turn {
    // if close stop
    if (world.nextToPlayer(location.asVector2)) {
      hit(world.player)
      
    } else { // if can step closer do.
      stepTowards(world.playerPos.asVector2, location.asVector2) match {
        case Some(v) => {
          goTo(Point(v.x,v.y))
        }
        case _ => Unit
      }
    }
  }
}