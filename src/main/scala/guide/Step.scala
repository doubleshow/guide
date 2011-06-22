package guide

import java.awt.{Dimension,Point}
import java.awt.Color
import java.awt.Rectangle
import java.awt.image.BufferedImage
import com.sun.awt.AWTUtilities
import java.awt.Graphics2D
import javax.swing.{JWindow, ImageIcon}
import swing._
import event._
import collection.mutable.{ArrayBuffer, HashMap}
import javax.imageio.ImageIO
import java.io.File

trait StepEvent extends Event {
  val source: Step
}

case class ElementAdded(source: Step, e: Element) extends StepEvent
case class ElementRemoved(source: Step, e: Element) extends StepEvent


class Step {

  //var elements = ArrayBuffer.empty[Element]
  var contextImage : BufferedImage = null

  object elements extends ArrayBuffer[Element] with Publisher {
    override def +=(e:Element) : this.type = {
      super.+=(e)
      publish(new ElementAdded(Step.this, e))
      this
    }
  }
  
  override def toString() = {
    
    	val r = for ( element <- elements) yield {    		
    	  ""+element    	  
    	}
    	r.mkString("(","\n",")")
  }
  
}

class CustomPanel extends Panel with SequentialContainer.Wrapper {
  override lazy val peer = {
		  val p = new javax.swing.JPanel with SuperMixin 
		  p.setLayout(null)
		  p
  }  
}

class StepOverlayView(var step : Step){
  
  var panel = new CustomPanel
  panel.preferredSize = new Dimension(500,500)
  
  step.elements.foreach( { element => 
    
  	var comp = View.create(element).component
  	panel.contents += comp 

  })
  
  def start {
    
    var win = new TransparentWindow
    win.add(panel.peer)
    win.setAlwaysOnTop(true)
    win.setVisible(true)
  }
}

object Step {

   def makeStep : Step = {
      var step = new Step

    	var element = new Element{x = 150; y = 151; width = 210; height = 20}

    	//step.elements += element

    	var textElement = new TextElement("some text") {
    		x = 10; y = 10; width = 100; height = 110;
    	}

    	step.elements += textElement

    	var circle = new CircleElement {
    	  x = 50; y = 200; width = 100; height = 100
    	}

    	step.elements += circle

    	var rect = new RectangleElement {
    	  x = 100; y = 300; width = 100; height = 50
    	}

    	step.elements += rect

    	step.contextImage = ImageIO.read(new File("screen1.png"))

    	step
  }

  def main(args : Array[String]) {

    val win = new Frame{
      location = new Point(100,100)
      size = new Dimension(500,500)
    }
    win.visible = true
    win.peer.setAlwaysOnTop(true)

    val step = makeStep
    val view = new StepEditView(step)
    view.component.preferredSize = new Dimension(500,500)
    win.contents = view.component


  }

}

class TransparentWindow extends JWindow {

	AWTUtilities.setWindowOpaque(this, false)
	setBounds(new Rectangle(50,50,500,500))    

}


class StepThumbView(val step : Step) extends CustomPanel {
//  var step_ : Step = null
//  
//  def step = step_  
//  def step_= (step: Step) {
//    step_ = step
    
	peer.removeAll()

    step.elements.foreach( { element =>     
    	var view = View.create(element)    	
  		var comp : scala.swing.Component = view.component  		
  		contents += comp 
    })
    
    val img = new ImageIcon(step.contextImage)

    val l = new Label  
    l.peer.setIcon(img)
    l.peer.setSize(img.getIconWidth,img.getIconHeight)
    contents += l
    
  //}
  
  override def paintChildren(g : Graphics2D) {
    
    if (step == null)
      return
	  
	  val size = peer.getSize();
	  val contentSize = new Dimension(step.contextImage.getWidth, step.contextImage.getHeight)
    
	  val scalex = 1f* size.width / contentSize.width
	  val scaley = 1f* size.height / contentSize.height
	  val minscale = Math.min(scalex,scaley)

	  val height = contentSize.height * minscale
	  val width = contentSize.width * minscale

	  val x = size.width/2 - width/2
	  val y = size.height/2 - height/2 
	  
	  g.translate(x,y)
	  g.scale(minscale,minscale)
	  
	  super.paintChildren(g)
  }
    
}

class StepEditView (var step : Step) extends Reactor{

  var panel = new CustomPanel
  panel.preferredSize = new Dimension(500,500)

  val viewMap = new HashMap[Component,ElementView]
  val elementViewMap = new HashMap[Element,ElementView]

  step.elements.foreach( insertViewForElement)

  listenTo(step.elements)
  reactions += {
    case ElementAdded(src, e) => {
      //println("StepEditView is notified an element is added to a step")
      insertViewForElement(e)
    }
  }


  var selectionList = new ListView(step.elements) {
      //selection.intervalMode = ListView.IntervalMode.MultiInterval
    selection.intervalMode = ListView.IntervalMode.Single
  }


  object selection {

    def indices = selectionList.selection.indices
    def items = selectionList.selection.items
    def clear() = selectionList.selectIndices()

    def -=(e: Element): this.type = {
      val i = step.elements.indexOf(e)
      if (i != -1)
        indices -= i
      this }

    def +=(e: Element): this.type = {
      val i = step.elements.indexOf(e)
      if (i != -1)
        indices += i
      this }

  }

//
//  def selectElement( e: Element) = {
//    val i = step.elements.indexOf(e)
//    if (i != -1)
//      selection.indices += i
//  }

  listenTo(selectionList.selection)

  reactions += {

    case ListSelectionChanged(listView, range, adjusting)  => {
      if (! adjusting) {

        //println("list selection changed: range = " + range)

        for (i <- range if i < step.elements.size) {
          val e = step.elements(i)
          var v = elementViewMap(e)
          v.component.foreground = Color.red
        }

        for (i <- selection.indices) {
          val e = step.elements(i)
          var v = elementViewMap(e)
          v.component.background = Color.green
          v.component.foreground = Color.green
        }



      }
    }

  }


  def viewForElement(e:Element) : ElementView = elementViewMap(e)

  

  private def insertViewForElement( e: Element){
      val view = View.create(e)
  		val comp : scala.swing.Component = view.component

  		viewMap += comp -> view
      elementViewMap += e -> view

      comp.focusable = true
  		listenTo(comp.mouse.clicks)
  		listenTo(comp.mouse.moves)
      listenTo(comp.keys)

  		//panel.contents +=:(comp)
      panel.peer.add(comp.peer,0)
      if (panel.peer.isDisplayable)
        panel.repaint()
  }

  panel.focusable = true
  listenTo(panel.keys)
  
  var dragged : Component = null
  var pressedPoint : Point = null
  var pressedLocation : Point = null

  private def elementFor(c: Component) : Element = {
    viewMap(c).element
  }

  def deleteSelected : Unit = {

    def deleteElement(e : Element) {
      step.elements -= e
      selection.clear()
      panel.contents -= (elementViewMap(e).component)
      panel.repaint

      elementViewMap -= e
    }

    selection.items.foreach(deleteElement)
  }

  val componentKeyReaction : PartialFunction[Event,Unit] = {


    case KeyPressed(c, key, mod, location) => {

      key match {
        case Key.Delete => deleteSelected
        case Key.BackSpace => deleteSelected

      }
       println("user pressed :" )


    }

  }
  
  val draggedMoveReaction : PartialFunction[Event,Unit] = {
  case MousePressed(e, point, mod, clicks, popup) => {

      selection += elementFor(e)

      // this is necessary for receiving key events
      e.requestFocus()

    	pressedLocation = e.location
      
        val location = e.locationOnScreen
    	point.translate(location.x,location.y)
    	
    	pressedPoint = point
    	
    	//println("pressed " + e)
    	
      }
    
    case MouseReleased(e, point, mod, clicks, popup) =>{
    	dragged = null
    	//println("released " + e)
    }

    case MouseDragged(e, point, mod) =>{
    	dragged = e
      
    	val location = e.locationOnScreen
    	point.translate(location.x,location.y)
    	
    	val dx = point.x - pressedPoint.x
    	val dy = point.y - pressedPoint.y
    	
    	if (dragged != null){
    	  
    	  var view = viewMap(dragged)
    	  view.element.x := (pressedLocation.x + dx)
    	  view.element.y := (pressedLocation.y + dy)
    	  
    	  
    	}

    	//println("dragged " + point)
    }

    case MouseMoved(e, point, mod) =>{
    	
    	//println("moved " + e)
    }
    
    
  }
  
  reactions += draggedMoveReaction
  reactions += componentKeyReaction
    
    
    
    
  
  val img = new ImageIcon(step.contextImage)
  
  val l = new Label  
  l.peer.setIcon(img)
  l.peer.setSize(img.getIconWidth,img.getIconHeight)
  panel.contents += l
  
  
  def component = panel
  
}

