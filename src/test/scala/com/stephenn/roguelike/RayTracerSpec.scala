package com.stephenn.roguelike

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import com.stephenn.roguelike.model._
import com.badlogic.gdx.math.Vector2

class RayTracerSpec extends FunSpec with ShouldMatchers with WorldSpecHelpers {
  
  describe("line of sight") {
    it("can see everything in a empty room") {
      val w = mkWorld(50, () => Tile(isGround = true))
      RayTracer.inLineOfSight(w.playerPos.asVector2, w).length should equal(2500)
    }
    
    it("cannot see in world of walls") {
      val w = mkWorld(50, () => new Tile)
      RayTracer.inLineOfSight(w.playerPos.asVector2, w).length should equal(0)
    }
    
    it("can see only in the little room") {
      val w = mkWorld(10, () => new Tile)
      w.playerPos = Point(5,5)
      w.getTile(w.playerPos).isGround = true
      w.getNeighbouringVectors(new Vector2(5,5)).foreach(w.getTile(_).isGround = true)
      
      val points = RayTracer.inLineOfSight(w.playerPos.asVector2, w)
      Point.sort(points) should equal(
          List(Point(4,4), Point(5,4), Point(6,4), Point(4,5), Point(5,5), Point(6,5), Point(4,6), Point(5,6), Point(6,6))
          )
    }
    
    it("can see until the wall") {
      val w = mkWorld(10, () => Tile(isGround = true))
      w.getTile(Point(5, 0)).isGround = false
      
      val set = scala.collection.mutable.Set[Point]()
      RayTracer.inRay(Point(0,0).asVector2, new Vector2(1,0), set, w)
      
      set.toList.sortBy(_.x) should equal(List(Point(0,0), Point(1,0), Point(2,0), Point(3,0), Point(4,0)))
    }
    
    it("can see from end of hallway") {
      val w = tinyWorld
      w.grid = LevelGenerator.gridFromString(Array(
          ".....",
          ".....",
          "  .  "
          ))
          
      w.logger.debug(LevelGenerator.gridToString(w.grid))
      
      w.getTile(Point(1,0)).isGround should equal(false)
      w.getTile(Point(2,0)).isGround should equal(true)
      w.getTile(Point(2,2)).isGround should equal(true)
      w.getTile(Point(3,2)).isGround should equal(true)
      w.getTile(Point(3,2)).isGround should equal(true)
      
      val points = RayTracer.inLineOfSight(new Vector2(2f,0f), w)
      Point.sort(points) should equal(
          List(Point(2,0), Point(0,1), Point(1,1), Point(2,1), Point(3,1), Point(4,1), Point(0,2), Point(1,2), Point(2,2), Point(3,2), Point(4,2))
          )
    }
  }

}