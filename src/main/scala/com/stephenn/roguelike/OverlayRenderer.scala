package com.stephenn.roguelike

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.stephenn.roguelike.model._
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.Gdx

class OverlayRenderer(world: World) {
  val cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
  val spriteBatch = new SpriteBatch()
  val font = new BitmapFont()
  
  def render {
    spriteBatch.begin
    spriteBatch.setProjectionMatrix(cam.combined)
    
    drawStats(world)
    
    spriteBatch.end
  }
  
  def drawStats(world: World) {    
    font.draw(spriteBatch, "T: "+world.time, (Gdx.graphics.getWidth()/2) -50, (Gdx.graphics.getHeight()/2) -50)
  }
}