package com.stephenn.roguelike.model

import com.badlogic.gdx.Gdx

class Player extends Character {
  var hp = 10
  var hitDamage = 5
  
  def die {
    //TODO game over screen.
    Gdx.app.exit
  }
}