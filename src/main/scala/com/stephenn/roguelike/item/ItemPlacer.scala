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
    val groundTiles = grid.toList.map(_.toList).flatten.filter(_.isGround)
    
    if (groundTiles.length > 0){
      val tile = groundTiles(randomInt(groundTiles.length))
      tile.items = tile.items ++ Seq(item)
    }	
  }
}