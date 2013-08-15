package com.stephenn.roguelike

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import com.stephenn.roguelike.model._
import com.badlogic.gdx.math.Vector2
import com.stephenn.roguelike.npc._
import scala.collection.mutable.Buffer


object WorldSpecHelpers extends WorldSpecHelpers

trait WorldSpecHelpers {
  def groundTile = {
    val t = new Tile
    t.isGround = true
    t
  }
  
  def tinyWorld = new WorldTrait {
    override def generateGrid = Array.fill(2,2)(new Tile)
    var playerPos = Point(0,0)
    var npcs = Seq[NPC]()
    val player = new Player(this)
  }
  
  def tinyWorldOfGround = new WorldTrait {
    override def generateGrid = Array.fill(2,2)(groundTile)
    var playerPos = Point(0,0)
    var npcs = Seq[NPC]()
    val player = new Player(this)
  }
}

class WorldSpec extends FunSpec with ShouldMatchers with WorldSpecHelpers {
  implicit val floatToInt = Util.floatToInt
  
  describe("player movement") {
    it("should stop the player moving where they cannot go") {
      val world = tinyWorld
      
      world.canMovePlayerTo(Point(0,0)) should equal(false)
      world.grid(0)(0).isGround = true
      world.canMovePlayerTo(Point(0,0)) should equal(true)
    }
    
    it("cant walk off the grid") {
      val world = tinyWorldOfGround
      
      world.canMovePlayerTo(Point(-1,-1)) should equal(false)
      world.canMovePlayerTo(Point(2,2)) should equal(false)
      world.canMovePlayerTo(Point(0,0)) should equal(true)
      world.canMovePlayerTo(Point(1,1)) should equal(true)
    }
  }
  
  describe("helpers") {
    it("should know where the end of the grid is") {
      val world = tinyWorld
      
      world.isInWorld(Point(-1,-1)) should equal(false)
      world.isInWorld(Point(2,2)) should equal(false)
      world.isInWorld(Point(0,0)) should equal(true)
      world.isInWorld(Point(1,1)) should equal(true)
    }
    
    it("should know what neighbouring vectors are") {
      val n = tinyWorld.getNeighbouringVectors(new Vector2(5,5))
      
      n.map { v => (v.x.toInt, v.y.toInt) } should equal(Seq(
          (6,5),
          (5,6),
          (4,5),
          (5,4),
          (6,6),
          (4,6),
          (4,4),
          (6,4)
          ))
    }
    
    it("spawns new enemies") {
      val w = tinyWorldOfGround
      
      w.spawnEnemies
      w.time should equal(0)
      w.npcs.length should equal(1)
      
      w.time = 2
      w.spawnEnemies
      w.npcs.length should equal(1)
      
      w.time = 20
      w.spawnEnemies
      w.npcs.length should equal(2)
    }
  }
  
  describe("line of sight") {
    it("should getLeftFrom") {
      val v = tinyWorldOfGround.getLeftFrom(new Vector2(0,0))
      v.map(v => Point(v.x, v.y)) should equal (Seq(
          Point(-1,0),
          Point(-1,1),
          Point(-1,-1)
          ))
    }
    
    it("inLineOfSight") {
      val w = tinyWorldOfGround
      val v = w.inLineOfSight(new Vector2(0,0), w.getRightFrom)
      
      v.map(v => Point(v.x, v.y)) should equal (Seq(
          Point(1,0),
          Point(1,1)
          ))
    }
    
    it("should know") {
      val w = tinyWorldOfGround
      val points = w.inLineOfSightFrom(new Vector2(1,1))
      
      points should equal(Seq(
          Point(0,1),
          Point(0,0),
          Point(1,0)
          ))
    }
    
    it("knows when its not") {
      val w = tinyWorld
      val points = w.inLineOfSightFrom(new Vector2(1,1))
      
      points should equal(Seq())
    }
    
    it("works on a real map") {
      val w = new World
      val v = w.inLineOfSightFrom(w.playerPos.asVector2)
      v.length > 0 should equal(true)
    }
  }
}