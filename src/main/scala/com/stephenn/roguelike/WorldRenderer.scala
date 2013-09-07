package com.stephenn.roguelike

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.stephenn.roguelike.model._
import com.badlogic.gdx.Gdx

class WorldRenderer(world: WorldTrait) {
  
  val TILE_WIDTH = 1f
  
  val CAMERA_WIDTH = 20f
  val CAMERA_HEIGHT = 14f
  val cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT)
  cam.zoom = 3
  cam.update
  
  var spriteCache: SpriteCache = new SpriteCache
  val spriteBatch = new SpriteBatch()
  
  def render {
    cam.position.set(world.playerPos.x, world.playerPos.y, 0)
    cam.update
    
    spriteBatch.begin
    spriteBatch.setProjectionMatrix(cam.combined)
    spriteBatch.setColor(1, 1, 1, 1)
    
    drawTiles(world, world.currentlyInSight)
    
    spriteBatch.end
    
    spriteBatch.begin
    spriteBatch.setProjectionMatrix(cam.combined)
    spriteBatch.setColor(1, 1f, 1f, 0.3f)
    
    drawOutOfSight(world)
    
    spriteBatch.end
  }
  
  def drawOutOfSight(world: WorldTrait) {
    val tiles = world.grid
    for (y <- 0 to tiles.length -1){
      for (x <- 0 to tiles(y).length -1) {
        if (!world.currentlyInSight.contains(Point(x,y)) && world.haveSeen.contains(Point(x,y))) {
         drawOutOfSightTile(tiles(y)(x), x, y) 
        }
      }
    }
  }
  
  def drawTiles(world: WorldTrait, points: Seq[Point]){
    points.foreach { p =>
      drawTile(world.getTile(p), p.x, p.y)
    }
  }
  
  def drawOutOfSightTile(t: Tile, x: Int, y: Int) {
    if (t.player.isDefined) {
      spriteBatch.draw(spriteCache.fly, x, y, 1, 1)
    } else if (t.isGround) {
      spriteBatch.draw(spriteCache.ground1, x, y, 1, 1)
    } else {
      spriteBatch.draw(spriteCache.wall, x, y, 1, 1)
    }
  }
  
  def drawTile(t: Tile, x: Int, y: Int) {
    if (t.player.isDefined) {
      spriteBatch.draw(spriteCache.fly, x, y, 1, 1)
    } else if (t.npc.isDefined){
      spriteBatch.draw(spriteCache.bee, x, y, 1, 1)
    } else if (t.items.length > 0) {
      spriteBatch.draw(spriteCache.orange, x, y, 1, 1)
    } else if (t.isGround) {
      spriteBatch.draw(spriteCache.ground1, x, y, 1, 1)
    } else {
      spriteBatch.draw(spriteCache.wall, x, y, 1, 1)
    }
  }
  
  def zoomIn = zoom(-0.4f)
  def zoomOut = zoom(0.4f)
  
  def zoom(mod: Float) {
    cam.zoom += mod
    cam.update
  }
}