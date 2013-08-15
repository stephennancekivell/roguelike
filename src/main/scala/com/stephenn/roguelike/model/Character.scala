package com.stephenn.roguelike.model

trait Character {
  var hp: Int
  def hitDamage: Int
  
  def die
  
  def hit(c: Character) {
    c.hp -= hitDamage
    if (c.hp <= 0) {
      c.die
    }
  }
}