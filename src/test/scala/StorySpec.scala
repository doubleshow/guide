package test.scala

import _root_.guide._
import org.scalatest.matchers.{ShouldMatchers, MustMatchers}
import org.scalatest.{FunSuite, FlatSpec, GivenWhenThen, FeatureSpec}

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