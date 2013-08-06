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
  
  grid(4)(4).player = Some(player)
  
  var playerPos = Point(4,4)
  
  def getTile(p: Point) = grid(p.y)(p.x)
  
  def playerUp = movePlayer(Point(0,1))
  def playerDown = movePlayer(Point(0,-1))
  def playerRight = movePlayer(Point(1,0))
  def playerLeft = movePlayer(Point(-1,0))
  
  def movePlayer(mod: Point) {
    getTile(playerPos).player = None
    playerPos = playerPos.add(mod)
    getTile(playerPos).player = Some(player)
  }
}