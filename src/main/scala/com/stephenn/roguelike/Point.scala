package com.stephenn.roguelike

import com.badlogic.gdx.math.Vector2

case class Point(x: Int, y: Int) {
  def add(p: Point) = {
    Point(p.x + this.x, p.y + this.y)
  }
  
  def asVector2 = {
    new Vector2(x,y)
  }
}

object Point {
  def apply(v: Vector2) = {
    new Point(v.x.toInt, v.y.toInt)
  }
}