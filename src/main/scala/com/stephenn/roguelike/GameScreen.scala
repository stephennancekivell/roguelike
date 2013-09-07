package com.stephenn.roguelike

import com.badlogic.gdx.Screen
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL10
import com.stephenn.roguelike.model.World
import com.badlogic.gdx.Input.Keys

class GameScreen extends Screen with InputProcessor {
  
  var renderer: WorldRenderer = _
  var overlayRenderer: OverlayRenderer = _
  var world: World = _
  
  def render (delta: Float) {
    
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1)
	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)
    
    renderer.render
    overlayRenderer.render(menu.isOpen, menu)
  }
  
  def show(){
    Gdx.input.setInputProcessor(this)
    world = new World
    renderer = new WorldRenderer(world)
    overlayRenderer = new OverlayRenderer(world)
  }
  
  def keyDown(code: Int) = {
    System.out.println("keyDown: "+code)
    
    if (code == Keys.ESCAPE && menu.isOpen == false){
      menu.isOpen = true
    } else {
      menu.isOpen match {
	      case false => playerKey(code)
	      case true => menu.keyDown(code)
	    }
    }
    
    true
  }
  
  def playerKey(code: Int) = {
    code match {
      case Keys.UP => world.playerUp
      case Keys.DOWN => world.playerDown
      case Keys.LEFT => world.playerLeft
      case Keys.RIGHT => world.playerRight
      case Keys.P => world.pickup
      case Keys.NUM_2 => world.press2
      case _ => 
    }
    true
  }
  
  def menuKey(code: Int) = {
    code match {
      case 71 => renderer.zoomIn
      case 72 => renderer.zoomOut
      case Keys.NUM_1 => System.exit(0)
      case Keys.NUM_2 => System.exit(0)
      case _ => 
    }
    true
  }
  
  val options2 = Seq(MenuOption("Zoom In", (m: Menu) => renderer.zoomIn))
  val newMenu = MenuOption("new menu", (m: Menu) => {
    m.stack.push(options2)
    
    {}
  	}
  )
  
  val options = Seq(MenuOption("Escape", (m: Menu) => System.exit(0)), MenuOption("Zoom In", (m: Menu) => renderer.zoomIn), MenuOption("Zoom Out", (m: Menu) => renderer.zoomOut), newMenu)
  val menu = Menu(options)
  
  
  def hide() = Gdx.input.setInputProcessor(null)
  def pause() {}
  def resume() {}
  def dispose = Gdx.input.setInputProcessor(null)
  def resize(width: Int, height: Int) {}
  def keyUp(code: Int) = true
  def keyTyped(character: Char) = false
  def touchDown(x: Int, y: Int, pointer: Int, button: Int) = false
  def touchUp(x: Int, y: Int, pointer: Int, button: Int) = false
  def touchDragged(x: Int, y: Int, pointer: Int) = false
  def touchMoved(x: Int, y: Int) = false
  def scrolled(amount: Int) = false
  def mouseMoved(x: Int, y: Int) = false

}