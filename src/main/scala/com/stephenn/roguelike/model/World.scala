package com.stephenn.roguelike.model

import com.badlogic.gdx.Gdx
import com.stephenn.roguelike.Point
import scala.util.Random
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Rectangle
import org.slf4j.LoggerFactory

class World {
  var grid = World.generate

  val player = new Player

  getTile(Point(4, 4)).player = Some(player)

  var playerPos = Point(4, 4)

  def getTile(p: Point) = grid(p.y)(p.x)

  var time = 0l

  def endPlayerTurn {
    time += 1
  }

  def playerUp = movePlayer(Point(0, 1))
  def playerDown = movePlayer(Point(0, -1))
  def playerRight = movePlayer(Point(1, 0))
  def playerLeft = movePlayer(Point(-1, 0))

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
      p.y < grid.length &&
      p.x < grid(0).length
  }

  def reRender = {
    grid = World.generate
    movePlayer(Point(0, 0))
  }

  def press2 {
    World.drawPath(Point(0, 0), Point(10, 5), grid)
  }
}

object World extends RoomMaker {

  def generate = {
    val max = 50

    val rooms = generateRooms(max, max)
    val grid = makeGridFromRooms(rooms, max, max)
    
    connect(rooms, grid)
    
    grid
  }

  def generateTiny = {
    Array.fill(10, 10) {
      val t = new Tile
      t.isGround = true
      t
    }
  }

  def makePathBetween(a: Rectangle, b: Rectangle, grid: Array[Array[Tile]]) {
    val from = Point(a.x.toInt + Random.nextInt(a.width.toInt), a.y.toInt + Random.nextInt(a.height.toInt))
    val to = Point(b.x.toInt + Random.nextInt(b.width.toInt), b.y.toInt + Random.nextInt(b.height.toInt))
    drawPath(from, to, grid)
  }

  
}

trait RoomMaker {

  val logger = LoggerFactory.getLogger(this.getClass())

  def randomInt(n: Int) = Random.nextInt(n)

  val roomMaxHeightWidth = 10
  val roomMinHeightWidth = 4
  def generateRoom(mapWidth: Int, mapHeight: Int) = {
    new Rectangle(
      randomInt(mapWidth - roomMaxHeightWidth),
      randomInt(mapHeight - roomMaxHeightWidth),
      roomMinHeightWidth + randomInt(roomMaxHeightWidth - roomMinHeightWidth + 1),
      roomMinHeightWidth + randomInt(roomMaxHeightWidth - roomMinHeightWidth + 1))
  }

  def generateRooms(mapWidth: Int, mapHeight: Int) = {
    val rooms = (0 until 10).map { _ =>
      generateRoom(mapWidth, mapHeight)
    }

    val nonOverlapping = stopOverlapping(rooms)

    withoutOutsideBounds(nonOverlapping, mapWidth, mapHeight)
  }

  def stopOverlapping(rooms: Seq[Rectangle]) = stopOverlappingY(stopOverlappingX(rooms))

  def stopOverlappingX(rooms: Seq[Rectangle]) = stopOverLappingDirection(rooms, (r: Rectangle) => r.getX, (r: Rectangle) => r.width, (r: Rectangle, f: Float) => r.setX(f))
  def stopOverlappingY(rooms: Seq[Rectangle]) = stopOverLappingDirection(rooms, (r: Rectangle) => r.getY, (r: Rectangle) => r.height, (r: Rectangle, f: Float) => r.setY(f))

  def stopOverLappingDirection(rooms: Seq[Rectangle], getDirection: (Rectangle => Float), getSize: (Rectangle => Float), setDirection: (Rectangle, Float) => Unit): Seq[Rectangle] = {
    if (rooms.length == 1) {
      rooms
    } else {
      val sorted = rooms.sortBy(getDirection)
      val head = sorted.head
      val tail = sorted.tail
      sorted.tail.foreach(room => {
        if (room.overlaps(sorted.head)) {
          setDirection(room, getDirection(sorted.head) + getSize(sorted.head))
        }
      })
      Seq(sorted.head) ++ stopOverLappingDirection(sorted.tail, getDirection, getSize, setDirection)
    }
  }

  def withoutOutsideBounds(rooms: Seq[Rectangle], maxX: Int, maxY: Int) = {
    rooms.filter { room =>
      room.getX + room.width < maxX &
        room.getY + room.height < maxY
    }
  }

  def makeGridFromRooms(rooms: Seq[Rectangle], maxX: Int, maxY: Int) = {
    val grid = Array.fill(maxY, maxX)(new Tile)

    rooms.foreach { room =>
      for (x <- room.getX.toInt to room.getX.toInt + room.width.toInt - 1) {
        grid(room.getY.toInt)(x).isWall = true
        grid(room.getY.toInt + room.height.toInt - 1)(x).isWall = true
      }

      for (y <- room.getY.toInt to room.getY.toInt + room.height.toInt - 1) {
        grid(y)(room.getX.toInt).isWall = true
        grid(y)(room.getX.toInt + room.width.toInt - 1).isWall = true
      }
    }

    rooms.foreach { room =>
      for (x <- room.getX.toInt + 1 to room.getX.toInt + room.width.toInt - 2; y <- room.getY.toInt + 1 to room.getY.toInt + room.height.toInt - 2) {
        if (x < maxX && y < maxY) {
          grid(y)(x).isGround = true
        }
      }
    }

    grid
  }

  def gridToString(grid: Array[Array[Tile]]) = {
    grid.map { col =>
      val chars = col.map { t =>
        if (t.player.isDefined) {
          '@'
        } else if (t.isGround) {
          '.'
        } else if (t.isWall) {
          '#'
        } else {
          ' '
        }
      }
      new String(chars)
    }.reverse.mkString("\n")
  }

  implicit def intFromFloat(f: Float) = f.toInt

  def connect(rooms: Seq[Rectangle], grid:Array[Array[Tile]]) = {
    val vectors = rooms.map { room =>
      new Vector2(room.getX, room.getY)
    }
    
    val path = goToAll(Seq(vectors.head), vectors.tail)
    
    val path2 = path map { v =>
      Point(v.x, v.y)
    }
    
    for(i <- 0 to path2.length-2) {
      drawPath(path2(i), path2(i+1), grid)
    }

  }

  def goToAll(path: Seq[Vector2], to: Seq[Vector2]): Seq[Vector2] = {
    if (to.length == 1) {
      path ++ to
    } else {
      val from = path.last
      val options = {
        to.map { t =>
          val remaining = to.filter(_ != t)
          val h = remaining.map(t.dst).sum
          val g = from.dst(t)

          (h + g, t, remaining)
        }
      }

      val sorted = options.sortBy(_._1)

      goToAll(path ++ Seq(sorted.head._2), sorted.head._3)
    }
  }
  
  
  def drawPath(a: Point, b: Point, grid: Array[Array[Tile]]) {
    val xSorted = Seq(a, b).sortBy(_.x)
    for (x <- xSorted(0).x to xSorted(1).x) {
      grid(xSorted(0).y)(x).isGround = true
    }

    val ySorted = Seq(a, b).sortBy(_.y)
    for (y <- ySorted(0).y to ySorted(1).y) {
      grid(y)(xSorted(1).x).isGround = true
    }
  }

}