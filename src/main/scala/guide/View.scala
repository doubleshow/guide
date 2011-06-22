package guide

import scala.swing.Component
import scala.swing.Label
import scala.swing.Button
import java.awt.{Dimension, Point, Color}
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.BasicStroke
import java.awt.Rectangle
import java.awt.geom.Ellipse2D

abstract class View {  
	def component : Component
}

object View {
  
  def create(element : Element) : ElementView = {
    
    var view = element match {    	  
    	  case e:TextElement => {new TextElementView(e)}
    	  case e:CircleElement => {new CircleElementView(e)}
    	  case e:RectangleElement => {new RectangleElementView(e)}
    	  case e:Element => {new ElementView(e)}
    }    
    view
  }

  def apply(element : Element) : ElementView = {
      create(element)
  }
}

class ElementView(val element: Element) extends View{

  
	def component : Component = button 

	var button = new Button{
		text = "Element"
		background = Color.green
		foreground = Color.blue
	
	
		// TODO: figure out a way to remove this replicated code
		peer.setLocation(new Point(element.x, element.y))
		peer.setSize(new Dimension(element.width, element.height))
	}
	
	
	def repaintForTransparentWindow(c : Component){
	  
	  	val ancestor = c.peer.getTopLevelAncestor()
	  	ancestor match {
	  	  case win:TransparentWindow => win.repaint() //<- this allows overlay to be drawn correctly
	  	  case _ => "do nothing"
	  	}

	}
	
	def updateX(c : Component, newValue : Int) {
		var location = c.peer.getLocation() 
		c.peer.setLocation(newValue, location.y)
		repaintForTransparentWindow(c)
	}
	
	def updateY(c : Component, newValue : Int){
		var location = c.peer.getLocation() 
		c.peer.setLocation(location.x, newValue)
		repaintForTransparentWindow(c)
	}
	
	def updateWidth(c : Component, newValue : Int){
		var size = c.peer.getSize() 
		c.peer.setSize(newValue, size.height)
		repaintForTransparentWindow(c)
	}
	
	def updateHeight(c : Component, newValue : Int){
		var size = c.peer.getSize() 
		c.peer.setSize(size.width, newValue)
		repaintForTransparentWindow(c)
	}
	
	element.x.registerListener(x => updateX(component,x))
	element.y.registerListener(y => updateY(component,y))
	element.width.registerListener(w => updateWidth(component,w))
	element.height.registerListener(h => updateHeight(component,h))

}


class CircleElementView(val circleElement: CircleElement) extends ElementView(circleElement){
  
	var comp = new Component{
	  
		foreground = Color.red

	  
	  	peer.setLocation(new Point(element.x, element.y))
		peer.setSize(new Dimension(element.width, element.height))


		override def paintComponent(g : Graphics2D) {
			super.paintComponent(g)

			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON)       

			val pen = new BasicStroke(3.0F)
			g.setStroke(pen)

			val r = new Rectangle(circleElement.x,circleElement.y,circleElement.width,circleElement.height)
			r.grow(-2,-2);

			g.translate(2, 2)
			val ellipse = new Ellipse2D.Double(0,0,r.width-1,r.height-1)
			g.draw(ellipse)


		}

	}
	
	override def component : Component = comp 
  
}

class RectangleElementView(val rectElement: RectangleElement) extends ElementView(rectElement){
  
	var comp = new Component{
	  
	  	foreground = Color.red

	  
	  	peer.setLocation(new Point(element.x, element.y))
		peer.setSize(new Dimension(element.width, element.height))

		override def paintComponent(g : Graphics2D) {
			super.paintComponent(g)

			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON)       

			val pen = new BasicStroke(3.0F)
			g.setStroke(pen)

			val r = rectElement
			g.drawRect(1,1,r.width-3,r.height-3)

		}

	}
	
	override def component : Component = comp 
  
}

class TextElementView(val textElement: TextElement) extends ElementView(textElement){

	var label = new Label{
		text = textElement.text
		opaque = true
		background = Color.yellow
		foreground = Color.black

		peer.setSize(peer.getPreferredSize())
		peer.setLocation(new Point(element.x, element.y))
	}

	override def component : Component = label

	textElement.text.registerListener(t => label.text = t)  
}

