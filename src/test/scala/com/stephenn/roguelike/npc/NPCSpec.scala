package com.stephenn.roguelike.npc

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import com.stephenn.roguelike.model._
import com.stephenn.roguelike._
import com.badlogic.gdx.math.Vector2

class NPCSpec extends FunSpec with ShouldMatchers  {
  
  def npc(worldIn: WorldTrait = WorldSpecHelpers.tinyWorld) = new NPC {
    println("startof newNPC")
    val world = worldIn
    
    var location = Point(0,0)
    var hp = 0
    var hitDamage = 0
    
    this.addToWorld
  }
  
  describe("npc") {
    it("should pick the closest location") {
      val closest = npc().stepTowards(new Vector2(2,2), new Vector2(8,8)).get
      
      closest.x should equal(7)
      closest.y should equal(7)
    }
    
    it("should do nothing when next to player") {
      val n = npc()
      n.location = Point(2,2)
      n.world.playerPos = Point(3,3)
      
      n.turn
      
      n.location.x should equal(2)
      n.location.y should equal(2)
    }
    
    it("should step towards player") {
      val n = npc()
      n.location = Point(2,2)
      n.world.playerPos = Point(4,4)
      
      n.turn
      
      n.location.x should equal(3)
      n.location.y should equal(3)
    }
    
    it("should add itself to the world on creation") {
      val n = npc()
      n.world.npcs.length should equal(1)
      n.world.getTile(n.location).npc should equal(Some(n))
    }
    
    it("at dead it npc should remove from the world and tile") {
      val n = npc()
      n.die
      
      n.world.npcs.length should equal(0)
      n.world.getTile(n.location).npc should equal(None)
    }
  }
}