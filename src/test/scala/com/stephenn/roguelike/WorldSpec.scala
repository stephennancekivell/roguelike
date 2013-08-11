package com.stephenn.roguelike

import org.scalatest.FunSpec
import com.stephenn.roguelike.model.World
import com.stephenn.roguelike.model.RoomMaker
import org.scalatest.matchers.ShouldMatchers
import com.badlogic.gdx.math.Rectangle

class WorldSpec extends FunSpec with ShouldMatchers {
  
  describe("world generation") {
    it("should make smallest in bottom corner rooms") {
      val roomMaker = new RoomMaker {
        override def randomInt(n: Int) = 0
      }
      
      val room = roomMaker.generateRoom(50, 50)
      room.getX() should equal (0.0)
      room.getY() should equal (0.0)
      room.height should equal (2)
      room.width should equal (2)
    }
    
    it("should make biggest room is top corner") {
      val roomMaker = new RoomMaker {
        override def randomInt(n: Int) = n-1
      }
      
      val room = roomMaker.generateRoom(50, 50)
      room.getX() should equal (39.0)
      room.getY() should equal (39.0)
      room.height should equal (9)
      room.width should equal (9)
    }
    
    it ("should stop overlapping in X") {
      val roomMaker = new RoomMaker {}
      val rooms = Seq(
        new Rectangle(3,1, 5, 5),
      	new Rectangle(0,2, 5, 5),
      	new Rectangle(2,3, 5, 5))
      
      val nonOverlapping = roomMaker.stopOverlappingX(rooms)
      nonOverlapping(0).getX() should equal (0)
      nonOverlapping(0).getY() should equal (2)
      
      nonOverlapping(1).getX() should equal (5.0)
      nonOverlapping(1).getY() should equal (3)
      
      nonOverlapping(2).getX() should equal (10.0)
      nonOverlapping(2).getY() should equal (1)
    }
    
    it ("filters rooms that dont fit on the map") {
      val roomMaker = new RoomMaker {}
      val rooms = Seq(
          new Rectangle(1,1,5,5),
          new Rectangle(9,9,5,5))
          
      val filtered = roomMaker.withoutOutsideBounds(rooms, 10, 10)
      
      filtered.size should equal(1)
      filtered(0).getX should equal(1)
      filtered(0).getY should equal(1)
    }
  }
}