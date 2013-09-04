package com.stephenn.roguelike.item

case class Food(val nutrition: Int = 100) extends Item {

}

trait Item {}