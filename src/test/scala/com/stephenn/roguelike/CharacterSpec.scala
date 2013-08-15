package com.stephenn.roguelike

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import com.stephenn.roguelike.model._
import com.stephenn.roguelike._

class CharacterSpec extends FunSpec with ShouldMatchers {
  
  def character = new Character {
    var hp = 10
    var hitDamage = 1
    var isDead = false
    def die {isDead = true}
  }
  
  describe("Character") {
    it("should take damage when hit") {
      val a = character
      val b = character
      
      b.hp should equal(10)
      
      a.hit(b)
      b.hp should equal(9)
    }
    
    it("should die when damage is <= 0") {
      val a = character
      val b = character
      
      b.hp should equal(10)
      b.isDead should equal(false)
      
      for (i <- 0 to 9){
        a.hit(b)
      }
      b.isDead should equal(true)
    }
  }
}