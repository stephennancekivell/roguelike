package com.stephenn.roguelike

case class Point(x: Int, y: Int) {
  def add(p: Point) = {
    Point(p.x + this.x, p.y + this.y)
  }
}