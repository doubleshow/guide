package guide

trait Spatial {

	var height : Property[Int] = 0
	var width : Property[Int] = 0
	var x : Property[Int] = 0
	var y : Property[Int] = 0

}

class Element extends Spatial {

	override def toString() = 
		List("x=" + x, "y=" + y, "width="+width, "height=" + height).mkString("(",",",")")  
}

case class TextElement(var text : Property[String]) extends Element

case class CircleElement extends Element

case class RectangleElement extends Element

object Element {

	def main(args: Array[String]): Unit = {


			var elem = new Element
			elem.height.registerListener((x) => println("height is changed to " + x))

			elem.height := 10
			elem.height = 5
	} 
}