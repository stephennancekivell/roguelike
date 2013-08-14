package com.stephenn.roguelike.model

import com.stephenn.roguelike.npc._

class Tile {
  var player: Option[Player] = None
  var npc: Option[NPC] = None
  
  // var items
  // var npc
  
  var isGround = false
  
  def isWalkable = isGround && player.isEmpty && npc.isEmpty
  
  // groundType?
}