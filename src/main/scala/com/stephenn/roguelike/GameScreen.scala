package com.stephenn.roguelike

import com.badlogic.gdx.Screen
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL10
import com.stephenn.roguelike.model.World
import com.badlogic.gdx.Input.Keys

class GameScreen extends Screen with InputProcessor {
  
  var renderer: WorldRenderer = _
  var world: World = _
  
  def resize(width: Int, height: Int) {
    
  }
  
  def render (delta: Float) {
    
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1)
	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)
    
    renderer.render(world)
  }
  
  def show(){
    Gdx.input.setInputProcessor(this)
    world = new World
    renderer = new WorldRenderer(world)
  }
  
  def hide() = Gdx.input.setInputProcessor(null)
  
  def pause() {}
  
  def resume() {}
  
  def dispose = Gdx.input.setInputProcessor(null)
  
  def keyDown(code: Int) = {
    code match {
      case 71 => renderer.zoomIn
      case 72 => renderer.zoomOut
      case Keys.UP => world.playerUp
      case Keys.DOWN => world.playerDown
      case Keys.LEFT => world.playerLeft
      case Keys.RIGHT => world.playerRight
      case Keys.ESCAPE => Gdx.app.exit()
      case Keys.NUM_1 => world.reRender
      case Keys.NUM_2 => world.press2
      case _ => 
    }
    System.out.println("keyDown: "+code)
    true
  }
  
  def keyUp(code: Int) = {
    true
  }
  
  def  keyTyped(character: Char) = false
  
  def touchDown(x: Int, y: Int, pointer: Int, button: Int) = false
  def touchUp(x: Int, y: Int, pointer: Int, button: Int) = false
  def touchDragged(x: Int, y: Int, pointer: Int) = false
  def touchMoved(x: Int, y: Int) = false
  
  def scrolled(amount: Int) = false
  
  def mouseMoved(x: Int, y: Int) = false

}