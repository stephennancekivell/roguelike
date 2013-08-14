package com.stephenn.roguelike.npc

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import com.stephenn.roguelike.model._
import com.stephenn.roguelike._
import com.badlogic.gdx.math.Vector2

class NPCSpec extends FunSpec with ShouldMatchers {
  
  def groundTile = {
    val t = new Tile
    t.isGround = true
    t
  }
  
  def tinyWorld = new WorldTrait {
    override def generateGrid = Array.fill(9,9)(groundTile)
    var playerPos = Point(0,0)
  }
  
  def npc = new NPC {
    val world = tinyWorld
    var location = Point(0,0)
  }
  
  describe("npc") {
    it("should pick the closest location") {
      val closest = npc.stepTowards(new Vector2(2,2), new Vector2(8,8)).get
      
      closest.x should equal(7)
      closest.y should equal(7)
    }
    
    it("should do nothing when next to player") {
      val n = npc
      n.location = Point(2,2)
      n.world.playerPos = Point(3,3)
      
      n.turn
      
      n.location.x should equal(2)
      n.location.y should equal(2)
    }
    
    it("should step towards player") {
      val n = npc
      n.location = Point(2,2)
      n.world.playerPos = Point(4,4)
      
      n.turn
      
      n.location.x should equal(3)
      n.location.y should equal(3)
    }
  }
}