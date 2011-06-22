package guide
import scala.swing.MainFrame
import scala.swing.Orientation
import scala.swing.SplitPane

object StoryBuilder {
  def main(args : Array[String]) : Unit = {}
}


class StoryBuilder(var story: Story) extends MainFrame {
  
  var listView = new StoryListView(story)
  var editView = new StepThumbView(story.steps(0))
  
  contents = new SplitPane(Orientation.Horizontal, listView, editView)
  
  
  
}
