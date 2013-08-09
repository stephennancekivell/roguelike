package com.stephenn.roguelike

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.LwjglApplication

object Runner {
  def main(args: Array[String]) {
    var cfg = new LwjglApplicationConfiguration()
	cfg.title = "rougelike"
	cfg.useGL20 = true
	cfg.width = 1280
	cfg.height = 768
	
    new LwjglApplication(new MyGame(), cfg)
  }
}