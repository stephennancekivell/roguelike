package com.stephenn.roguelike

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import com.stephenn.roguelike.model._
import com.badlogic.gdx.math.Vector2
import com.stephenn.roguelike.npc._
import scala.collection.mutable.Buffer


object WorldSpecHelpers extends WorldSpecHelpers

trait WorldSpecHelpers {
  
  def mkWorld(size:Int, mkTile:() => Tile) = new WorldTrait {
    override def generateGrid = LevelGenerator.mkGrid(size, mkTile)
    var playerPos = Point(0,0)
    var npcs = Seq[NPC]()
    val player = new Player(this)
  }
  
  def groundTile = {
    val t = new Tile
    t.isGround = true
    t
  }
  
  def tinyWorld = mkWorld(2, () => new Tile)
    
  def tinyWorldOfGround = mkWorld(2, () => groundTile)
  
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
    it("can see everything in a empty room") {
      val w = mkWorld(50, () => groundTile)
      w.inLineOfSight(w.playerPos.asVector2).length should equal(2500)
    }
    
    it("cannot see in world of walls") {
      val w = mkWorld(50, () => new Tile)
      w.inLineOfSight(w.playerPos.asVector2).length should equal(0)
    }
    
    it("can see only in the little room") {
      val w = mkWorld(10, () => new Tile)
      w.playerPos = Point(5,5)
      w.getTile(w.playerPos).isGround = true
      w.getNeighbouringVectors(new Vector2(5,5)).foreach(w.getTile(_).isGround = true)
      
      w.inLineOfSight(w.playerPos.asVector2).toList.sortBy(_.y).sortBy(_.x) should equal(
          List(Point(4,4), Point(4,5), Point(4,6), Point(5,4), Point(5,5), Point(5,6), Point(6,4), Point(6,5), Point(6,6))
          )
    }
    
    it("can see until the wall") {
      val w = mkWorld(10, () => groundTile)
      w.getTile(Point(5, 0)).isGround = false
      
      val set = scala.collection.mutable.Set[Point]()
      w.inRay(Point(0,0).asVector2, new Vector2(1,0), set)
      
      set.toList.sortBy(_.x) should equal(List(Point(1,0), Point(2,0), Point(3,0), Point(4,0)))
    }
  }
}