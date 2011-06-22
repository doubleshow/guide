package guide
import scala.swing.ListView.AbstractRenderer
import javax.swing.BorderFactory
import java.awt.Dimension
import collection.mutable.ArrayBuffer
import swing.event.Event
import swing._

trait StoryEvent extends Event {
  val source: Story
}

case class StepAdded(source: Story, step: Step) extends StoryEvent
case class StepRemoved(source: Story, step: Step) extends StoryEvent
case class StepModified(source: Story, step: Step) extends StoryEvent


class Story {

	object steps extends ArrayBuffer[Step] with Publisher with Reactor {

    reactions += {
      case e: ElementRemoved => {
        println("removed")
        publish(new StepModified(Story.this, e.source))
      }
      case e: ElementAdded => {
        println("added")
        publish(new StepModified(Story.this, e.source))
      }
    }

    override def +=(step : Step) : this.type = {
        super.+=(step)
        listenTo(step.elements)
        publish(new StepAdded(Story.this, step))
        this
    }
  }

}

class ListTileView extends BorderPanel {  
	border = BorderFactory.createEmptyBorder(20,20,20,20)
}


class StoryListView(var story : Story) extends ListView(story.steps) with Reactor{

  listenTo(story.steps)
  reactions += {
    case StepModified(story: Story, step: Step) => {
      println("modified: " + step + step.hashCode)
      repaint
    }
  }
    
	class StepListItemRenderer(var c: ListTileView) extends AbstractRenderer[Step,Component](c){

		def configure(list: ListView[_], isSelected: Boolean, focused: Boolean,
				step: Step, index: Int) {

			c.layout(new StepThumbView(step)) = BorderPanel.Position.Center
			c.preferredSize = new Dimension(250,200)

		}          
	}


	var tileView = new ListTileView
	renderer = new StepListItemRenderer(tileView)

  
}