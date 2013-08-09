package com.stephenn.roguelike

import org.scalatest.FunSpec
import com.stephenn.roguelike.model.World
import com.stephenn.roguelike.model.RoomMaker
import org.scalatest.matchers.ShouldMatchers

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
    
    it ("should make biggest room is top corner") {
      val roomMaker = new RoomMaker {
        override def randomInt(n: Int) = n-1
      }
      
      val room = roomMaker.generateRoom(50, 50)
      room.getX() should equal (39.0)
      room.getY() should equal (39.0)
      room.height should equal (9)
      room.width should equal (9)
    }
  }
}