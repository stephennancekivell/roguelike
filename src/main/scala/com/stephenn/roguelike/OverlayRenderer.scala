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
  
  def render(isMenuOpen: Boolean, menu: Menu) {
    spriteBatch.begin
    spriteBatch.setProjectionMatrix(cam.combined)
    
    drawStats(world)
    
    if (isMenuOpen){
      drawMenu(world, menu)
    }
    
    spriteBatch.end
  }
  
  def drawStats(world: WorldTrait) {
    font.draw(spriteBatch, "HP: "+world.player.hp, (Gdx.graphics.getWidth()/2) -50, (Gdx.graphics.getHeight()/2) -30)
    font.draw(spriteBatch, "T: "+world.time, (Gdx.graphics.getWidth()/2) -50, (Gdx.graphics.getHeight()/2) -50)
    font.draw(spriteBatch, "N: "+world.player.nutrition, (Gdx.graphics.getWidth()/2) -50, (Gdx.graphics.getHeight()/2) -70)
  }
  
  def drawMenu(world: WorldTrait, menu: Menu) {
    for (i <- 0 to menu.options.length-1) {
      font.draw(spriteBatch, s"$i) "+menu.options(i).description, (Gdx.graphics.getWidth()/2) * -1 + 50, (Gdx.graphics.getHeight()/2) -30 - (20 * i))
    }
  }
}