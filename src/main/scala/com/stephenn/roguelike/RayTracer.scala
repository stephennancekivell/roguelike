package com.stephenn.roguelike

import com.badlogic.gdx.math.Vector2
import com.stephenn.roguelike.model.WorldTrait
import scala.collection.mutable.Set
import scala.collection.mutable.Buffer

object RayTracer {
  val precision = 800
  def inLineOfSight(from: Vector2, world: WorldTrait) = {
    val centered = from.cpy.add(new Vector2(0.5f, 0.5f)) // center line of sight from middle of tile.
    val vectors = (1 to precision + 1).map(x => {
      val v = Vector2.X.cpy()
      v.setAngle((360f / precision) * x)
      v
    })
    
    val set = Set[Point]()

    vectors.foreach { dv =>
      inRay(centered, dv, set, world)
    }
    
    set.toSeq
  }
  
  def inRay(v: Vector2, dv: Vector2, set: Set[Point], world: WorldTrait) {
    val n = v.cpy()
    
    while(world.isInWorld(n) && world.getTile(n).canSeeThrough){
      set.add(Point(n))
      
      n.add(dv)
    }
  }
}

object ShadowCaster {
  def decent1(start: Vector2, endSlope: Vector2, world: WorldTrait): Seq[Point] = {
    //http://roguebasin.roguelikedevelopment.org/index.php?title=FOV_using_recursive_shadowcasting
    
    val startSlope = new Vector2(-1,1)
    var scanStart = start.cpy
    var scanEnd = start.cpy
    
    val inSight = Buffer[Point]()
    
    scala.util.control.Breaks.breakable {
      while (true) {
        
        val y = scanStart.y.toInt
        for (x <- scanStart.x.toInt to scanEnd.x.toInt) {
          val p = Point(x, y)
          
          
          var allBlocked = true

          if (world.isInWorld(p) && world.getTile(p).canSeeThrough) {
            inSight.append(p)
            allBlocked = false
          } else {
            if (world.isInWorld(p) && !world.getTile(p).canSeeThrough) {
//              if (p.x == scanStart.x.toInt){
//                scanStart.x += 1
//                startSlope.set((p.x.toFloat - start.x)/(p.y.toFloat - start.y), 1)
//              }
              
              val newEndSlope = endSlope.cpy().set((p.x.toFloat - start.x)/(p.y.toFloat - start.y), 1)
               
              inSight.appendAll(decent1(p.asVector2.add(startSlope), newEndSlope, world))

            } else {
              
            }
            if (allBlocked && x == scanEnd.x.toInt){
              scala.util.control.Breaks.break
            }
          }
        }
        scanStart.add(startSlope)
        scanEnd.add(endSlope)
      }
    }
    
    inSight.toSeq
  }
}