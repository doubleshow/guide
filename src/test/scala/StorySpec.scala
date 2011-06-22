package test.scala

import _root_.guide._
import org.scalatest.matchers.{ShouldMatchers}
import java.awt.{Dimension}
import swing.Frame
import org.scalatest._
import main.scala.guide.StoryEditor

//class StorySpec extends FlatSpec with GivenWhenThen with ShouldMatchers{
//
//  "A Story" should "be initialized with no step" in {
//      var story = new Story()
//      story.steps.size should be (0)
//  }
//}

class StoryFunSpec extends FunSuite with ShouldMatchers{

  test("story is created empty") {
    var story = new Story
    story.steps.size should be (0)
  }

  test("a new step is added to the story") {
    var story = new Story
    val step = new Step
    story.steps += step
    story.steps.size should be (1)
  }
}

class StoryEditorInteractionSpec extends FunSuite {

  test("StoryEditor's look and feel") {
    val se = new StoryEditor
    se.visible = true
  }
}

class StoryEditorFeatureSpec extends FeatureSpec with ShouldMatchers with GivenWhenThen with BeforeAndAfter {

  var editor : StoryEditor = _

  before {
     editor = new StoryEditor
     editor.visible = true
  }

  after {
    editor.dispose
  }

  feature("User can select a step from the list view"){

    scenario("User click on the first step"){


    }

    scenario("User starts the editor empty"){



    }

    scenario("Selection match"){


      //editor.currentStep should be
      println(editor.storyListView.selection.items(0))
      println(editor.stepEditView.step)


    }

  }


}


class StoryListViewInteractionSpec extends FeatureSpec with ShouldMatchers with GivenWhenThen with BeforeAndAfter {

  var win: Frame  = _
  var view: StoryListView = _
  var story: Story = _

  before {
      win = new Frame{
        visible = true
        peer.setAlwaysOnTop(true)
      }

      //step = StepFixture.example
      story = new Story
      view = new StoryListView(story)
      story.steps += StepFixture.example
      story.steps += StepFixture.example
      story.steps += StepFixture.example
      story.steps += StepFixture.example

      view.preferredSize = new Dimension(200,600)
      win.contents = view
  }

  after {

  }

  feature("User can see a vertical list of steps"){

    scenario("Some steps are loaded"){
      //win.visible = true

      var step1 = StepFixture.example
      var step2 = StepFixture.example

      var step = story.steps(0)

      //story.steps += step1
      win.contents = view
      step.elements += CircleElement((150,200,200,200))

      val s1 = story.steps(1)
      val e = s1.elements(0)
      s1.elements -= e


      win.visible = true
      //st
    }
  }
}


class StoryListViewSpec extends FeatureSpec with ShouldMatchers with GivenWhenThen {

  feature("StoryListView's listmodel is linked to a story's steps"){

    scenario("Steps are added/inserted/deleted"){

      given("an empty story is created")
      val story = new Story

      when("a StoryListView is created for that story")
      var view = new StoryListView(story)

      def listmodel = view.peer.getModel

      then("this StoryListView should contain an empty listmodel")
      listmodel.getSize should be (0)

      when("a step is added to the story")
      story.steps += new Step

      then("the listmodel should have exactly one element")
      listmodel.getSize should be (1)

      when("anoter step is added to the story")
      story.steps += new Step

      then("the listmodel should have exactly two element")
      listmodel.getSize should be (2)

      val firstStep = listmodel.getElementAt(0)
      val secondStep = listmodel.getElementAt(1)

      given("a new step to insert")
      val stepToInsert = new Step

      when("this new step is inserted after the first element")
      story.steps.insertAll(1, List(stepToInsert))

      then("the listmodel should have 3 elements")
      listmodel.getSize should be (3)

      and("the 1st element in the listmodel should still be the same")
      listmodel.getElementAt(0) should be theSameInstanceAs (firstStep)

      and("the 3rd element in the listmodel should be the 2nd element before")
      listmodel.getElementAt(2) should be theSameInstanceAs (secondStep)

      and("the 2nd element in the listmodel should be the newly inserted element")
      listmodel.getElementAt(1) should be theSameInstanceAs (stepToInsert)

      when("the 1st step is removed")
      story.steps.remove(0)

      then("the listmodel should have 2 elements")
      listmodel.getSize should be (2)


    }

  }


}

//class StackSpec extends FlatSpec with ShouldMatchers {
//
//  "A Views should be updated when objects' locations and sizes should be updated when A Stack" should "pop values in last-in-first-out order" in {
//    val stack = new Stack[Int]
//    stack.push(1)
//    stack.push(2)
//    stack.pop() should equal (2)
//    stack.pop() should equal (1)
//  }
//
//  it should "throw NoSuchElementException if an empty stack is popped" in {
//    val emptyStack = new Stack[String]
//    evaluating { /*emptyStack.pop()*/ } should produce [NoSuchElementException]
//  }
//}