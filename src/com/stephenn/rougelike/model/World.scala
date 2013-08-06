package com.stephenn.rougelike.model

class World {
  var grid = Array.fill(10, 10)(new Tile)
  
  val player = new Player
  
  var playerPos = (4,4)
  def playerUp = playerPos = (playerPos._1, playerPos._2 +1)
  def playerDown = playerPos = (playerPos._1, playerPos._2 -1)
  def playerRight = playerPos = (playerPos._1+1, playerPos._2)
  def playerLeft = playerPos = (playerPos._1-1, playerPos._2)
  
}