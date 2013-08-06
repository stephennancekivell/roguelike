package com.stephenn.roguelike

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.Rectangle
import model.World
import com.stephenn.roguelike.model.Tile

class WorldRenderer(world: World) {
  
  val TILE_WIDTH = 1f
  
  var CAMERA_WIDTH = 10f
  var CAMERA_HEIGHT = 7f
  var cam: OrthographicCamera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT)
  cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0)
  cam.update
  
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
      spriteBatch.draw(fly, x, y, 1, 1)
    } else {
      if (t.isGround) {
        spriteBatch.draw(ground1, x, y, 1, 1)
      }
    }
  }
  
  var fly: TextureRegion = _
  var ground1: TextureRegion = _
  val spriteBatch = new SpriteBatch()
  def loadTextures {
    val atlas = new TextureAtlas(Gdx.files.internal("nethack.atlas"))
    fly = atlas.findRegion("fly")
    ground1 = atlas.findRegion("ground1")
  }
  loadTextures
  
  def zoomIn {
    cam.zoom += 0.1f
    cam.update
  }
  
  def zoomOut {
    cam.zoom -= 0.1f
    cam.update
  }
}