package com.stephenn.roguelike.item

import org.scalatest.FunSpec
import com.stephenn.roguelike.model.World
import org.scalatest.matchers.ShouldMatchers
import com.stephenn.roguelike.LevelGenerator
import com.stephenn.roguelike.model.Tile

class ItemPlacerSpec extends FunSpec with ShouldMatchers {
  describe("Item placement") {
    it("should put food one space") {
      val grid = LevelGenerator.mkGrid(3, () => Tile(isGround = true))
      
      ItemPlacer.placeItemOnRandomTile(Food(), grid)
      
      grid.toList.map(_.toList).flatten.filter(_.items.length > 0).length should equal(1)
    }
    
    it("cant put the food anywhere") {
      val grid = LevelGenerator.mkGrid(3, () => Tile(isGround = false))
      
      ItemPlacer.placeItemOnRandomTile(Food(), grid)
      
      grid.toList.map(_.toList).flatten.filter(_.items.length > 0).length should equal(0)
    }
  }
}