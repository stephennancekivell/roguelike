package com.stephenn.roguelike.model

import com.badlogic.gdx.Gdx

class Player(world: WorldTrait) extends Character {
  var maxHp = 10
  var hp = maxHp
  var hitDamage = 5
  
  def die {
    //TODO game over screen.
    Gdx.app.exit
  }
  
  def endTurn {
    if (world.time % 20 == 0 && hp < maxHp) {
      hp += 1
    }
  }
}