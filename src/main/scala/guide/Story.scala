package guide
import scala.swing.ListView
import scala.swing.ListView.AbstractRenderer
import scala.swing.BorderPanel
import javax.swing.BorderFactory
import scala.swing.Component
import java.awt.Dimension
import collection.mutable.ArrayBuffer

class Story {

	var steps = ArrayBuffer.empty[Step]

}

class ListTileView extends BorderPanel {  
	border = BorderFactory.createEmptyBorder(20,20,20,20)
}


class StoryListView(var story : Story) extends ListView(story.steps) {
    
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