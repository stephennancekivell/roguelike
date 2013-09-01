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
    currentlyInSight = inLineOfSight2(playerPos.asVector2)
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
    this.precision -= 100
    Gdx.app.log("precision", ""+precision)
  
  }
}

trait WorldTrait {
  var grid = generateGrid
//  def generateGrid = LevelGenerator.generate
  def generateGrid = LevelGenerator.generateEmpty// .generateRandom
  
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
  
  val logger = LoggerFactory.getLogger(classOf[World])
  
  var precision = 800
  def inLineOfSight2(from: Vector2) = {
    val vectors = (1 to precision + 1).map(x => {
      val v = Vector2.X.cpy()
      v.setAngle((360f / precision) * x)
      v
    })
    
    val set = Set[Point]()

    vectors.foreach { dv =>
      inRay(from, dv, set)
    }
    
    set.toSeq
  }

  def inRay(v: Vector2, dv: Vector2): Seq[Vector2] = {
    val n = v.cpy().add(dv)

    if (this.isInWorld(n) && this.getTile(n).canSeeThrough) {
      Seq(n) ++ inRay(n, dv)
    } else {
      Seq()
    }
  }
  
  def inRay(v: Vector2, dv: Vector2, set: Set[Point]) {
    val n = v.cpy().add(dv)
    
    while(isInWorld(n) && getTile(n).canSeeThrough){
      set.add(Point(n))
      
      n.add(dv)
    }
  }
  
  def inLineOfSight3 = {
    decent1(this.playerPos.asVector2, endSlope = new Vector2(0,1))
  }
  
  def decent1(start: Vector2, endSlope: Vector2): Seq[Point] = {
    //http://roguebasin.roguelikedevelopment.org/index.php?title=FOV_using_recursive_shadowcasting
    logger.debug("decent1 "+start+" " +endSlope)
    val startSlope = new Vector2(-1,1)
    var scanStart = start.cpy
    var scanEnd = start.cpy
    
    val inSight = Buffer[Point]()
    
    scala.util.control.Breaks.breakable {
      while (true) {
        logger.debug("" + scanStart)
        val y = scanStart.y.toInt
        for (x <- scanStart.x.toInt to scanEnd.x.toInt) {
          val p = Point(x, y)
          logger.debug("p" + p)
          
          var allBlocked = true

          if (this.isInWorld(p) && this.getTile(p).canSeeThrough) {
            inSight.append(p)
            allBlocked = false
          } else {
            if (this.isInWorld(p) && !this.getTile(p).canSeeThrough) {
//              if (p.x == scanStart.x.toInt){
//                scanStart.x += 1
//                startSlope.set((p.x.toFloat - start.x)/(p.y.toFloat - start.y), 1)
//              }
              
              val newEndSlope = endSlope.cpy().set((p.x.toFloat - start.x)/(p.y.toFloat - start.y), 1)
               
              inSight.appendAll(decent1(p.asVector2.add(startSlope), newEndSlope))

            } else {
              
            }
            if (allBlocked && x == scanEnd.x.toInt){
              scala.util.control.Breaks.break
            }
          }
        }
        scanStart.add(startSlope)
        scanEnd.add(endSlope)
      }
    }
    
    inSight.toSeq
  }
}