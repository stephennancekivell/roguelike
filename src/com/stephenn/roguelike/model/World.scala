package com.stephenn.roguelike.model

import com.badlogic.gdx.Gdx

case class Point(x: Int, y: Int) {
  def add(p: Point) = {
    Point(p.x + this.x, p.y + this.y)
  }
}

class World {
  var grid = Array.fill(10, 10)(new Tile)
  
  val player = new Player
  
  getTile(Point(4,4)).player = Some(player)
  
  var playerPos = Point(4,4)
  
  def getTile(p: Point) = grid(p.y)(p.x)
  
  var time = 0l
  
  def endPlayerTurn {
    time += 1
  }
  
  def playerUp = movePlayer(Point(0,1))
  def playerDown = movePlayer(Point(0,-1))
  def playerRight = movePlayer(Point(1,0))
  def playerLeft = movePlayer(Point(-1,0))
  
  def movePlayer(mod: Point) {
    val p = playerPos.add(mod)
    if (canMovePlayerTo(p)) {
      getTile(playerPos).player = None
      playerPos = p
      getTile(playerPos).player = Some(player)
      
      endPlayerTurn
      true
    } else {
      false
    }
  }
  
  def canMovePlayerTo(p: Point) = {
    isInWorld(p)
  }
  
  def isInWorld(p: Point) = {
    p.x >= 0 &&
    p.y >= 0 &&
    p.y < grid.length&&
    p.x < grid(0).length 
  }
}