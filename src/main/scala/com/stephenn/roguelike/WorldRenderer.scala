package com.stephenn.roguelike

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.stephenn.roguelike.model.Tile

import model.World

class WorldRenderer(world: World) {
  
  val TILE_WIDTH = 1f
  
  val CAMERA_WIDTH = 20f
  val CAMERA_HEIGHT = 14f
  val cam: OrthographicCamera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT)
  cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0)
  cam.update
  
  var spriteCache: SpriteCache = new SpriteCache
  val spriteBatch = new SpriteBatch()
  
  def render(tiles: Array[Array[Tile]])  {
    spriteBatch.begin
    spriteBatch.setProjectionMatrix(cam.combined)
    
    for (y <- 0 to tiles.length -1){
      for (x <- 0 to tiles(y).length -1) {
        drawTile(tiles(y)(x), x, y)
      }
    }
    
    spriteBatch.end
  }
  
  def drawTile(t: Tile, x: Int, y: Int) {
    if (t.player.isDefined) {
      spriteBatch.draw(spriteCache.fly, x, y, 1, 1)
    } else {
      if (t.isGround) {
        spriteBatch.draw(spriteCache.ground1, x, y, 1, 1)
      }
    }
  }
  
  def zoomIn = zoom(0.4f)
  def zoomOut = zoom(-0.4f)
  
  def zoom(mod: Float) {
    cam.zoom += mod
    cam.update
  }
}