package com.stephenn.rougelike

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.LwjglApplication

object Runner {
  def main(args: Array[String]) {
    var cfg = new LwjglApplicationConfiguration()
	cfg.title = "rougelike"
	cfg.useGL20 = false
	cfg.width = 480
	cfg.height = 320
	
	new LwjglApplication(new RougelikeGame(), cfg)
  }
}