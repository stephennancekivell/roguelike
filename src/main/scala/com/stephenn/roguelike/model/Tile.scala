package com.stephenn.roguelike.model

import com.stephenn.roguelike.npc._

case class Tile(
    var player: Option[Player] = None,
    var npc: Option[NPC] = None,
    var isGround: Boolean = false
    ){
  def isWalkable = isGround && player.isEmpty && npc.isEmpty
  
  def canSeeThrough = isGround
}