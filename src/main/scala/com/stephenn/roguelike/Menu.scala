package com.stephenn.roguelike

import com.badlogic.gdx.Input.Keys
import scala.collection.mutable.Stack

case class Menu {
  var isOpen = false
  
  val stack: Stack[Seq[MenuOption]] = Stack()
  
  def options = stack.top
  
  def keyDown(code: Int) = {
    code match {
      case code if (Keys.NUM_0 to (Keys.NUM_0 + options.length)).contains(code)  => {
        options(code - Keys.NUM_0).function(this)
      }
      
      case Keys.ESCAPE if stack.length == 1 => isOpen = false
      case Keys.ESCAPE if stack.length > 1 => stack.pop
      
      case _ => {}
    }
    true
  }
}

object Menu {
  def apply(options: Seq[MenuOption]) = {
    val m = new Menu()
    m.stack.push(options)
    m
  }
}

case class MenuOption(description: String, function: (Menu) => Unit)
