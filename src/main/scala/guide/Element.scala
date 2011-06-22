package guide

import _root_.guide.Property._

trait Spatial {

	var height : Property[Int] = 0
	var width : Property[Int] = 0
	var x : Property[Int] = 0
	var y : Property[Int] = 0

}

class Element extends Spatial {

	override def toString() = 
		List("x=" + x, "y=" + y, "width="+width, "height=" + height).mkString("(",",",")")

  def copy = Element((x,y,width,height))
}

object Element {

  def apply(b: (Int, Int, Int, Int)) : Element =
    new Element {
         this.x = b._1; this.y = b._2; this.width = b._3; this.height = b._3
    }

}

case class TextElement(var text : Property[String]) extends Element {
  override def copy = TextElement(text, (x,y,width,height))
}

object TextElement {

  def apply(text: String, b: (Int, Int, Int, Int)) : TextElement =
    new TextElement(text) {
         this.x = b._1; this.y = b._2; this.width = b._3; this.height = b._3
    }

}

class CircleElement extends Element {
  override def copy = CircleElement((x,y,width,height))
}

object CircleElement {

  def apply(b: (Int, Int, Int, Int)) : CircleElement =
    new CircleElement {
         this.x = b._1; this.y = b._2; this.width = b._3; this.height = b._3
    }
}

class RectangleElement extends Element {
  override def copy = RectangleElement((x,y,width,height))
}

object RectangleElement {

  def apply(b: (Int, Int, Int, Int)) : RectangleElement =
    new RectangleElement {
         this.x = b._1; this.y = b._2; this.width = b._3; this.height = b._3
    }
}

//object Element {
//
//	def main(args: Array[String]): Unit = {
//
//
//			var elem = new Element
//			elem.height.registerListener((x) => println("height is changed to " + x))
//
//			elem.height := 10
//			elem.height = 5
//	}
//}