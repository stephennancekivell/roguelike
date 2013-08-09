package com.stephenn.roguelike.util

import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage

object SpriteSplitter {
  def main(args: Array[String]) {
    
    val sprites = split(loadImage("assets/nethack-3.4.3-32x32.png"), 1280/32, 960/32)
    
    var i=0
    sprites.foreach { s =>
      save(s, s"out/$i.png")
      i += 1
    }
  }

  def loadImage(path: String) = {
    ImageIO.read(new File(path))
  }
  
  def save(img: BufferedImage, path: String) {
    ImageIO.write(img, "png", new File(path))
  }

  def split(img: BufferedImage, cols: Int, rows: Int) = {
    val w = img.getWidth / cols
    val h = img.getHeight / rows
    
    val seq = for (y <- 0 to rows) yield {
      for (x <- 0 to cols) yield {
        val bf = new BufferedImage(w, h, img.getType())
        val g = bf.createGraphics()
        g.drawImage(img, 0, 0, w, h, w * x, h * y, w * x + w, h * y + h, null)
        g.dispose()
        bf
      } 
    }
    seq.flatten
  }
}