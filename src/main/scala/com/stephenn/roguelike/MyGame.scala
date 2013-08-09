package com.stephenn.roguelike

import com.badlogic.gdx.Game

class MyGame extends Game {
  
  override def create = {
    setScreen(new GameScreen)
  }

}