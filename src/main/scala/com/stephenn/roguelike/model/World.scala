package com.stephenn.roguelike.model

import com.badlogic.gdx.Gdx
import com.stephenn.roguelike.Point
import scala.util.Random
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Rectangle
import org.slf4j.LoggerFactory
import com.stephenn.roguelike.LevelGenerator

class World extends WorldTrait{
  
  val player = new Player
  var playerPos = LevelGenerator.getPlayerStartLoc(grid)
  getTile(playerPos).player = Some(player)

  var time = 0l

  def endPlayerTurn {
    time += 1
  }

  def playerUp = movePlayer(Point(0, 1))
  def playerDown = movePlayer(Point(0, -1))
  def playerRight = movePlayer(Point(1, 0))
  def playerLeft = movePlayer(Point(-1, 0))

  def movePlayer(mod: Point) = {
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

  def reRender = {
    grid = LevelGenerator.generate
    movePlayer(Point(0, 0))
  }

  def press2 {
    LevelGenerator.drawPath(Point(0, 0), Point(10, 5), grid)
  }
}

trait WorldTrait {
  var grid = generateGrid
  def generateGrid = LevelGenerator.generate
  
  def isInWorld(p: Point) = {
    p.x >= 0 &&
      p.y >= 0 &&
      p.y < grid.length &&
      p.x < grid(0).length
  }
  
  def canMovePlayerTo(p: Point) = {
    isInWorld(p) && getTile(p).isWalkable
  }
  
  def getTile(p: Point) = grid(p.y)(p.x)
}