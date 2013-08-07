package com.stephenn.roguelike

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.Gdx

class SpriteCache {
  var fly: TextureRegion = _
  var ground1: TextureRegion = _
  
  def loadTextures {
    val atlas = new TextureAtlas(Gdx.files.internal("nethack.atlas"))
    fly = atlas.findRegion("fly")
    ground1 = atlas.findRegion("ground1")
  }
  loadTextures

}