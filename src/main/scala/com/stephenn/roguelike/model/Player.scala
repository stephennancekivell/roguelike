package com.stephenn.roguelike.model

import com.badlogic.gdx.Gdx
import com.stephenn.roguelike.item.Item

class Player(world: WorldTrait) extends Character {
  var maxHp = 10
  var hp = maxHp
  var hitDamage = 5
  var nutrition = 100
  var items: Seq[Item] = Seq()
  
  def die {
    //TODO game over screen.
    Gdx.app.exit
  }
  
  def endTurn {
    if (world.time % 20 == 0 && hp < maxHp) {
      hp += 1
    }
    nutrition -=1
  }
}