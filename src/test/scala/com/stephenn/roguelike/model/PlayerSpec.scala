package com.stephenn.roguelike.model

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.stephenn.roguelike._

class PlayerSpec extends FunSpec with ShouldMatchers {
  describe("player"){
    it("should heal") {
      val w = WorldSpecHelpers.tinyWorld
      val p = new Player(w)
      
      p.hp = 7
      w.time = 20
      p.endTurn
      
      p.hp should equal(8)
    }
  }
}