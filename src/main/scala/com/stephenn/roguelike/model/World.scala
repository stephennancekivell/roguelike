package com.stephenn.roguelike.model

import com.badlogic.gdx.Gdx
import scala.util.Random
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Rectangle
import org.slf4j.LoggerFactory
import com.stephenn.roguelike._
import com.stephenn.roguelike.npc._
import scala.collection.mutable.Buffer

class World extends WorldTrait{
  
  val player = new Player(this)
  var playerPos = LevelGenerator.getPlayerStartLoc(grid).get
  getTile(playerPos).player = Some(player)
  
  var npcs: Seq[NPC] = Seq()
  
  npcs = newNpcs 
  
  def newNpcs = {
    Seq(newEnemyAtRandom, newEnemyAtRandom, newEnemyAtRandom).flatten
  }

  def endPlayerTurn {
    time += 1
    
    npcs.foreach(_.turn)
    spawnEnemies
    player.endTurn
    currentlyInSight = inLineOfSightFrom(playerPos.asVector2)
  }
  
  var currentlyInSight = Seq[Point]()

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
  
  def player: Player
  
  var playerPos: Point
  
  var time = 0l
  
  var npcs: Seq[NPC] 
  
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
  
  def newEnemyAtRandom = LevelGenerator.getRandomWalkable(grid).headOption.map(new Enemy(this, _))
  
  def spawnEnemies {
    if (time % 20 == 0){
      newEnemyAtRandom
    }
  }
  
  def inLineOfSightFrom(from: Vector2) = {
    val all = inLineOfSight(from, getRightFrom) ++
	    inLineOfSight(from, getLeftFrom) ++
	    inLineOfSight(from, getUpFrom) ++
	    inLineOfSight(from, getDownFrom)
	
	val points = all.map(v => Point(v.x, v.y))
	points.distinct
  }
  
  def getRightFrom(from: Vector2) = {
    Seq(
    		from.cpy.add(Vector2.X),
    		from.cpy.add(Vector2.X).add(Vector2.Y),
    		from.cpy.add(Vector2.X).sub(Vector2.Y)
    )
  }
  
  def getLeftFrom(from: Vector2) = {
    Seq(
    		from.cpy.sub(Vector2.X),
    		from.cpy.sub(Vector2.X).add(Vector2.Y),
    		from.cpy.sub(Vector2.X).sub(Vector2.Y)
    )
  }
  
  def getUpFrom(from: Vector2) = {
    Seq(
    		from.cpy.add(Vector2.Y),
    		from.cpy.add(Vector2.Y).add(Vector2.X),
    		from.cpy.add(Vector2.Y).sub(Vector2.X)
    )
  }
  
  def getDownFrom(from: Vector2) = {
    Seq(
    		from.cpy.sub(Vector2.Y),
    		from.cpy.sub(Vector2.Y).add(Vector2.X),
    		from.cpy.sub(Vector2.Y).sub(Vector2.X)
    )
  }
  
  def inLineOfSight(from: Vector2, get: (Vector2) => Seq[Vector2], haveBeen:Buffer[Vector2] = Buffer()):Seq[Vector2] = {
    if (haveBeen.contains(from)){
      Seq()
    } else  {
	    get(from).filter( t => 
	      (isInWorld(t) && getTile(t).isGround)
	    ).map { b =>
	      haveBeen += b
	      inLineOfSight(b, get, haveBeen ++ Seq(from)) ++ Seq(b)
	    }.flatten
    }
  }
}