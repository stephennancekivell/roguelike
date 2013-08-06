package com.stephenn.roguelike

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.LwjglApplication

object Runner {
  def main(args: Array[String]) {
    var cfg = new LwjglApplicationConfiguration()
	cfg.title = "rougelike"
	cfg.useGL20 = true
	cfg.width = 480
	cfg.height = 320
	
//	new LwjglApplication(new RougeLikeGame(), cfg)
    new LwjglApplication(new MyGame(), cfg)
  }
}