package com.stephenn.roguelike.npc

import com.stephenn.roguelike.model._
import com.stephenn.roguelike._

class Enemy(val world: WorldTrait, var location: Point) extends NPC {
  var hp = 1
  var hitDamage = 1
  
  this.addToWorld
}