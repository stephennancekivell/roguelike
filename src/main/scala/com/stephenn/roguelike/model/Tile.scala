package com.stephenn.roguelike.model

import com.stephenn.roguelike.npc._
import com.stephenn.roguelike.item.Item

case class Tile(
    var player: Option[Player] = None,
    var npc: Option[NPC] = None,
    var isGround: Boolean = false,
    var items: Seq[Item] = Seq()
    ){
  def isWalkable = isGround && player.isEmpty && npc.isEmpty
  
  def canSeeThrough = isGround
}