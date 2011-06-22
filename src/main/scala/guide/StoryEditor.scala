package main.scala.guide

import _root_.guide.{Step, StepEditView, StoryListView, Story, StepFixture}
import java.awt.Dimension
import swing.{Orientation, SplitPane, Frame}

class StoryEditor extends Frame{

  peer.setAlwaysOnTop(true)
  size = new Dimension(800,600)

  var story = new Story
  var step = StepFixture.example
  story.steps += StepFixture.example
  story.steps += StepFixture.example

  val storyListView = new StoryListView(story)

  storyListView.selection.indices += 0

  val stepEditView = new StepEditView(story.steps(0))

  contents = new SplitPane(Orientation.Vertical, storyListView, stepEditView.component)
  storyListView.minimumSize = new Dimension(200,0)

}