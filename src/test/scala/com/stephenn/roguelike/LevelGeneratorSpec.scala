package com.stephenn.roguelike

import org.scalatest.FunSpec
import com.stephenn.roguelike.model.World
import org.scalatest.matchers.ShouldMatchers
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.stephenn.roguelike.model.Tile

class LevelGeneratorSpec extends FunSpec with ShouldMatchers {
  
  describe("level generation") ({
    it("should make smallest in bottom corner rooms") {
      val levelGenerator = new LevelGenerator {
        override def randomInt(n: Int) = 0
      }
      
      val room = levelGenerator.generateRoom(50, 50)
      room.getX() should equal (0.0)
      room.getY() should equal (0.0)
      room.height should equal (levelGenerator.roomMinHeightWidth)
      room.width should equal (levelGenerator.roomMinHeightWidth)
    }
    
    it("should make biggest room is top corner") {
      val levelGenerator = new LevelGenerator {
        override def randomInt(n: Int) = n-1
      }
      
      val room = levelGenerator.generateRoom(50, 50)
      room.getX() should equal (39.0)
      room.getY() should equal (39.0)
      room.height should equal (levelGenerator.roomMaxHeightWidth)
      room.width should equal (levelGenerator.roomMaxHeightWidth)
    }
    
    it ("should stop overlapping in X") ({
      val levelGenerator = new LevelGenerator {}
      val rooms = Seq(
        new Rectangle(3, 1, 5, 5),      	new Rectangle(0, 2, 5, 5),      	new Rectangle(2, 3, 5, 5))
      
      val nonOverlapping = levelGenerator.stopOverlappingX(rooms)
      nonOverlapping(0).getX() should equal (0)
      nonOverlapping(0).getY() should equal (2)
      
      nonOverlapping(1).getX() should equal (5.0)
      nonOverlapping(1).getY() should equal (3)
      
      nonOverlapping(2).getX() should equal (10.0)
      nonOverlapping(2).getY() should equal (1)
    })
    
    it ("filters rooms that dont fit on the map") ({
      val levelGenerator = new LevelGenerator {}
      val rooms = Seq(
          new Rectangle(1, 1, 5, 5),          new Rectangle(9, 9, 5, 5))
          
      val filtered = levelGenerator.withoutOutsideBounds(rooms, 10, 10)
      
      filtered.size should equal(1)
      filtered(0).getX should equal(1)
      filtered(0).getY should equal(1)
    })
    
    it("makes a map of tiles from rooms") ({
      val levelGenerator = new LevelGenerator {}
      
      val grid = levelGenerator.makeGridFromRooms(Seq(new Rectangle(1, 1, 4, 4)), 10, 10)
      
      levelGenerator.gridToString(grid) should equal (Array(
	      "          ",
	      "          ",
	      "          ",
	      "          ",
	      "          ",
	      " ####     ",
	      " #..#     ",
	      " #..#     ",
	      " ####     ",
	      "          ").mkString("\n"))
    })
    
    it("finds the shortest path") ({
      val levelGenerator = new LevelGenerator {}
      
      val path = levelGenerator.goToAll(Seq(new Vector2(5, 5)), Seq(
          new Vector2(7, 7),          new Vector2(6, 6),          new Vector2(2, 2)
          ))
          
      path.length should equal(4)
      path(0) should equal(new Vector2(5, 5))
      path(1) should equal(new Vector2(6, 6))
      path(2) should equal(new Vector2(7, 7))
      path(3) should equal(new Vector2(2, 2))
    })
    
    it("makes a path") {
      val levelGenerator = new LevelGenerator {}
      
      val grid = Array.fill(6, 6)(new Tile)
      
      levelGenerator.drawPath(Point(1,1), Point(5,5), grid)
      
      levelGenerator.gridToString(grid) should equal (Array(
	      "     .",
	      "     .",
	      "     .",
	      "     .",
	      " .....",
	      "      ").mkString("\n"))
    }
  })
}