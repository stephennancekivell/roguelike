package com.stephenn.roguelike.model

import com.badlogic.gdx.Gdx
import scala.util.Random
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Rectangle
import org.slf4j.LoggerFactory
import com.stephenn.roguelike._
import com.stephenn.roguelike.npc._

class World extends WorldTrait{
  
  val player = new Player
  var playerPos = LevelGenerator.getPlayerStartLoc(grid)
  getTile(playerPos).player = Some(player)
  
  def newEnemyAtRandom = new Enemy(this, LevelGenerator.getRandomWalkable(grid)) 
  
  var npcs = newNpcs 
  
  def newNpcs = {
    val enemies = Seq(newEnemyAtRandom, newEnemyAtRandom, newEnemyAtRandom)
    
    enemies.foreach(e => getTile(e.location).npc = Some(e))
    
    enemies
  }

  var time = 0l

  def endPlayerTurn {
    time += 1
    
    npcs.foreach(_.turn)
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
  
  }
}

trait WorldTrait {
  var grid = generateGrid
  def generateGrid = LevelGenerator.generate
  
  def playerPos: Point
  
  implicit val floatToInt = Util.floatToInt
  
  def isInWorld(p: Point) = {
    p.x >= 0 &&
      p.y >= 0 &&
      p.y < grid.length &&
      p.x < grid(0).length
  }
  
  def isInWorld(p: Vector2) = {
    p.x >= 0 &&
      p.y >= 0 &&
      p.y < grid.length &&
      p.x < grid(0).length
  }
  
  def canMovePlayerTo(p: Point) = {
    isInWorld(p) && getTile(p).isWalkable
  }
  
  def getTile(p: Point) = grid(p.y)(p.x)
  def getTile(v: Vector2) = grid(v.y)(v.x)
  
  def getNeighbouringVectors(p: Vector2) = {
    Seq(
        p.cpy().add(Vector2.X),
        p.cpy().add(Vector2.Y),
        p.cpy().sub(Vector2.X),
        p.cpy().sub(Vector2.Y),
        p.cpy().add(Vector2.X.cpy().add(Vector2.Y)),
        p.cpy().sub(Vector2.X.cpy().sub(Vector2.Y)),
        p.cpy().sub(Vector2.X.cpy().add(Vector2.Y)),
        p.cpy().add(Vector2.X.cpy().sub(Vector2.Y))
        )
  }
  
  def neighbouringVectorsInWorld(from: Vector2) = getNeighbouringVectors(from).filter(isInWorld)
  
  def nextToPlayer(v: Vector2) = neighbouringVectorsInWorld(v).filter(_.equals(playerPos.asVector2)).length > 0
  
}