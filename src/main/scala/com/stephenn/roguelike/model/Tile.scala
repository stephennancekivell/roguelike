package com.stephenn.roguelike.model

class Tile {
  var player: Option[Player] = None
  
  // var items
  // var npc
  
  var isGround = false
  
  def isWalkable = isGround
  
  // groundType?
}