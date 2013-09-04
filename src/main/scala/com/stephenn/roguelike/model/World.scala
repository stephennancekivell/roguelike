package com.stephenn.roguelike.model

import com.badlogic.gdx.Gdx
import scala.util.Random
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Rectangle
import org.slf4j.LoggerFactory
import com.stephenn.roguelike._
import com.stephenn.roguelike.npc._
import scala.collection.mutable.Buffer
import scala.util.control.Breaks
import scala.collection.mutable.Set
import com.stephenn.roguelike.RayTracer

class World extends WorldTrait{
  
  val player = new Player(this)
  var playerPos = LevelGenerator.getPlayerStartLoc(grid).get
  getTile(playerPos).player = Some(player)
  
  npcs = newNpcs
  
  def newNpcs = {
    Seq(newEnemyAtRandom, newEnemyAtRandom, newEnemyAtRandom).flatten
  }
  
  updateLineOfSight

  def endPlayerTurn {
    time += 1
    
    npcs.foreach(_.turn)
    spawnEnemies
    player.endTurn
    updateLineOfSight
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
      if (isInWorld(p) && getTile(p).npc.isDefined){
        player.hit(getTile(p).npc.get)
      }
      false
    }
  }
  
  def pickup {
    getTile(playerPos).items.headOption match {
      case Some(item) => {
        getTile(playerPos).items = getTile(playerPos).items.tail
        player.items = player.items ++ Seq(item)
      }
      case _ => {}
    }
  }
  
  def updateLineOfSight {
    currentlyInSight = RayTracer.inLineOfSight(playerPos.asVector2, this)
    haveSeen ++= currentlyInSight
  }

  def reRender = { //debug code
    grid = LevelGenerator.generate
    playerPos = LevelGenerator.getPlayerStartLoc(grid).get
    getTile(playerPos).player = Some(player)
  }
  
  def press2 {
    logger.debug("playerPos "+ this.playerPos)
    logger.debug("currentlyInSight " + Point.sort(this.currentlyInSight))
    logger.debug("map "+ LevelGenerator.gridToString(grid))
  }
}

trait WorldTrait {
  var grid = generateGrid
  def generateGrid = LevelGenerator.generate
  
  def player: Player
  
  var playerPos: Point
  
  var npcs: Seq[NPC] = Seq()
  
  var time = 0l
  
  var currentlyInSight = Seq[Point]()
  
  val haveSeen = Set[Point]() // at least once
  
  val logger = LoggerFactory.getLogger(classOf[World])
  
  implicit val floatToInt = Util.floatToInt
  
  
  
  //---------------
  // Enemy spawning
  
  def newEnemyAtRandom = LevelGenerator.getRandomWalkable(grid).headOption.map(new Enemy(this, _))
  
  def spawnEnemies {
    if (time % 20 == 0){
      newEnemyAtRandom
    }
  }
  
  //--------
  // Helpers
  
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