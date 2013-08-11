package com.stephenn.roguelike

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.Gdx

class SpriteCache {
  val atlas = new TextureAtlas(Gdx.files.internal("nethack.atlas"))
  val fly = atlas.findRegion("fly")
  val ground1 = atlas.findRegion("ground1")
  val bee = atlas.findRegion("bee")
  val black = atlas.findRegion("black")
  val wall = atlas.findRegion("wall")
}