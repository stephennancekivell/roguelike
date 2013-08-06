package com.stephenn.rougelike

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.Rectangle;
import model.World

class WorldRenderer(world: World) {
  
  val TILE_WIDTH = 1f
  
  var CAMERA_WIDTH = 10f
  var CAMERA_HEIGHT = 7f
  var cam: OrthographicCamera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT)
  cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0)
  cam.update
  
  val debugRenderer = new ShapeRenderer()
  
  def render {
    debugRenderer.setProjectionMatrix(cam.combined)
    
    drawGrid
//    drawPlayer
    drawFlyPlayer
  }
  
  def drawGrid {
    debugRenderer.begin(ShapeType.Rectangle)
    debugRenderer.setColor(0, 1, 0, 1)
    for (x <- 0 to world.grid.length -2){
      for (y <- 0 to world.grid(x).length -2){
        debugRenderer.setColor(0, 1, 0, 1)
        debugRenderer.rect(x*TILE_WIDTH, y*TILE_WIDTH, 1, 1)
      }
    }
    debugRenderer.end
  }
  
  def drawPlayer {
    debugRenderer.begin(ShapeType.Circle)
    debugRenderer.setColor(Color.BLUE)
    debugRenderer.circle(world.playerPos._1*TILE_WIDTH, world.playerPos._2*TILE_WIDTH, TILE_WIDTH)
    debugRenderer.end
  }
  
  var fly: TextureRegion = _
  val spriteBatch = new SpriteBatch()
  def loadTextures {
    val atlas = new TextureAtlas(Gdx.files.internal("nethack.atlas"))
    fly = atlas.findRegion("fly")
  }
  loadTextures
  
  def drawFlyPlayer {
    spriteBatch.begin
    spriteBatch.setProjectionMatrix(cam.combined)
    spriteBatch.draw(fly, world.playerPos._1, world.playerPos._2, 1, 1)
    
    spriteBatch.end
  }
  
  def zoomIn {
    cam.zoom += 0.1f
    cam.update
  }
  
  def zoomOut {
    cam.zoom -= 0.1f
    cam.update
  }
}