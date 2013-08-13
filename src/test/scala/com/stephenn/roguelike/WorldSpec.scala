package com.stephenn.roguelike

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import com.stephenn.roguelike.model._


class WorldSpec extends FunSpec with ShouldMatchers {
  def groundTile = {
    val t = new Tile
    t.isGround = true
    t
  }
  
  def tinyWorld = new WorldTrait {
    override def generateGrid = Array.fill(2,2)(new Tile)
  }
  
  def tinyWorldOfGround = new WorldTrait {
    override def generateGrid = Array.fill(2,2)(groundTile)
  }
  
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
  }
}