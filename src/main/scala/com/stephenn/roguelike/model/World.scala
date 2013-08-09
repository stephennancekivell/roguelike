package com.stephenn.roguelike.model

import com.badlogic.gdx.Gdx
import com.stephenn.roguelike.Point
import scala.util.Random
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Rectangle

class World {
  var grid = World.generate
  
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

object World {
  def generateRooms(mapWidth: Int, mapHeight: Int) = {
    
    case class room(height: Int, width: Int, corner: Vector2)
    
    val rooms = (0 until 5).map { _ =>
      new Rectangle(
          Random.nextInt(mapWidth),
          Random.nextInt(mapHeight),
          2 + Random.nextInt(8),
          2 + Random.nextInt(8)
          )
    }
    
    //stop the rooms overlapping
    for (a <- 0 to rooms.length-1){
      rooms(a)
      for (b <- 0 to rooms.length-1) {
        if (a != b){
          if (rooms(a).overlaps(rooms(b))){
            val aAndB = List(rooms(a), rooms(b))
            
            val sortedY = aAndB.sortBy(_.getY())
            sortedY(1).setY(sortedY(1).getY() + (sortedY(0).height - sortedY(0).getY()))
            
            val sortedX = aAndB.sortBy(_.getX())
            sortedX(1).setX(sortedX(1).getX() + (sortedX(0).width - sortedX(0).getX()))
          }
        }
      }
    }
    
    Gdx.app.log("World", rooms.toString)
    
    rooms
  }
  
  def makeGridFromRooms(rooms: Seq[Rectangle], maxX: Int, maxY: Int) = {
    var grid = Array.fill(maxY, maxX)(new Tile)
    
    rooms.foreach(room =>{
      for(x <- room.getX.toInt to room.getX.toInt+room.width.toInt; y <- room.getY.toInt to room.getY.toInt+room.height.toInt) {
        Gdx.app.log("world", s"ground: $x, $y")
        if (x < maxX && y < maxY){
          grid(y)(x).isGround = true
        }
      }
    })
    
    grid
  }
  
  def generate = {
    val max = 50
    
    val rooms = generateRooms(max, max)
    makeGridFromRooms(rooms, max, max)
  }
  
  def generateTiny = {
    Array.fill(10, 10) {
      val t = new Tile
      t.isGround = true
      t
    }
  } 
  
  
}