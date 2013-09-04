package com.stephenn.roguelike.item

import com.stephenn.roguelike.model.Tile
import scala.util.Random

object ItemPlacer extends ItemPlacer {}

trait ItemPlacer {
  
  def randomInt(n: Int) = Random.nextInt(n)
  
  def placeRandom(grid: Array[Array[Tile]]){
    val r = (1 to randomInt(5)).foreach { _ =>
      placeItemOnRandomTile(Food(), grid)
    }
  }
  
  def placeItemOnRandomTile(item: Item, grid:Array[Array[Tile]]){
    val y = randomInt(grid.length)
    val x = randomInt(grid(y).length)
    
    val tile = grid(y)(x)
    
    if (tile.isWalkable)
    	tile.items = grid(y)(x).items ++ Seq(item)
  }
}