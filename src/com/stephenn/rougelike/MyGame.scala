package com.stephenn.rougelike

import com.badlogic.gdx.Game

class MyGame extends Game {
  
  override def create = {
    setScreen(new GameScreen)
  }

}