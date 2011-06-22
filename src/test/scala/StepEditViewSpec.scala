package test.scala

import _root_.guide._
import org.scalatest.matchers.{MatchResult, BeMatcher, ShouldMatchers}
import java.awt.{Dimension, Point}
import javax.imageio.ImageIO
import java.io.File
import swing.{Frame, UIElement, Component}
import swing.event.{KeyPressed, MousePressed}
import org.scalatest._

class SimpleWindow extends Frame {
  location = new Point(100,100)
  size = new Dimension(500,500)
}

class StepEditViewSpec extends FeatureSpec with ShouldMatchers with GivenWhenThen with BeforeAndAfter {

  object SingleSpec extends Tag("SingleSpec")

  override def run (testName: Option[String], reporter: Reporter, stopper: Stopper,
                    filter: Filter, configMap: Map[String, Any], distributor: Option[Distributor],
                    tracker: Tracker) : Unit = {
    super.run(testName, reporter, stopper, new Filter(Some(Set("SingleSpec")),Set.empty[String]),
      configMap, distributor, tracker)
  }

  class VisibilityMatcher extends BeMatcher[UIElement] {
    def apply(left: UIElement) =
      MatchResult( left.visible, left.toString + " was visible", left.toString + " was invisible")

  }

  val visible = new VisibilityMatcher

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

  var win: SimpleWindow = _
  var view: StepEditView = _
  var step: Step = _

  before {
      win = new SimpleWindow
      win.visible = true
      win.peer.setAlwaysOnTop(true)

      step = makeStep
      view = new StepEditView(step)
      view.component.preferredSize = new Dimension(500,500)
      win.contents = view.component
  }

  after {
   win.dispose
  }

  def pressLeftMouseButton(c : Component) {
    var m : scala.swing.event.Key.Modifiers = 0
    val evt = new java.awt.event.MouseEvent(c.peer, 0, 0, 0, 0, 0, 1, false);
    c.mouse.clicks.publish(new MousePressed(evt))
  }

  def pressDeleteKey(c : Component){
    var m : scala.swing.event.Key.Modifiers = 0
    val evt = new java.awt.event.KeyEvent(c.peer, 0, 0, 0, java.awt.event.KeyEvent.VK_DELETE, 0);
    c.mouse.clicks.publish(new KeyPressed(evt))
  }

  feature("User can interactively add elements") {

    scenario("User clicks on a location to insert a text element") {

      given("a new text element")
      val e = new TextElement("New Text") {
        x = 10; y = 50; width = 200; height = 200;
      }

      step.elements += e

    }


  }

  feature("User can interactively select/delete an element"){

    scenario("User clicks on an element to select it"){

      given("the first element")
      val e0 = step.elements(0)
      val c = view.viewForElement(e0).component

      when("the user clicks on it")
      pressLeftMouseButton(c)

      // focus can not be checked in parallel
      c should be ('hasFocus)

      view.selection.items should contain (e0)
    }

    scenario("User clicks on an element to select it and delete it"){

      given("the first element")
      val e0 = step.elements(0)
      val c = view.viewForElement(e0).component

      when("the user clicks on it and press delete")
      pressLeftMouseButton(c)

      // focus can not be checked in parallel
      //c should be ('hasFocus)

      pressDeleteKey(c)

      then("the element should no longer exist")
      step.elements should not (contain (e0))

      and("the element should not be selected")
      view.selection.items should not (contain (e0))

      val e1 = step.elements(0)
      val c1 = view.viewForElement(e1).component

      when("the user clicks on another element and press delete")
      pressLeftMouseButton(c1)

      // focus can not be checked in parallel
      //c1 should be ('hasFocus)

      pressDeleteKey(c1)

      then("that element should no longer be part of the step")
      step.elements should not (contain (e1))

      and("should no longer be selected")
      view.selection.items should not (contain (e1))

    }


  }



  //object SingleFeatureSpec extends Tag("scala.test.SingleFeatureSpec")



  feature("Elements can be deleted from the edit view"){

    scenario("selecting elements in single selection mode"){

      given("two elements in the step e1 and e2")
      val e1 = view.step.elements(1)
      val e2 = view.step.elements(2)

      when("the e1 element is selected")
      view.selection += e1

      then("the size of selection is 1")
      view.selection.indices should have size(1)

      and("the list of selected indices is 1")
      view.selection.indices should contain(1)

      and("the list of selected items contains e1")
      view.selection.items should contain(e1)

      when("the e2 element is selected")
      view.selection += e2

      and("the size of selection is still 1")
      view.selection.indices should have size(1)

      and("the list of selected indices contains 2")
      view.selection.indices should contain(2)

      and("the list of selected items contains e2")
      view.selection.items should contain(e2)

      val n = view.step.elements.size

      when("the selected element is deleted")
      view.deleteSelected

      then("the step should have one fewer elements")
      view.step.elements should have size(n-1)

      and("nothing should be selected")
      view.selection.indices should have size(0)
      view.selection.items should have size(0)

      and("the step should no longer contain the deleted element")
      view.step.elements should (not contain(e2))
    }

    scenario("deleting all elements one by one") {

      val e0 = view.step.elements(0)

      when("the first element is selected and deleted for all elements")
      for (i <- 1 to view.step.elements.size){
        view.selection.indices += 0
        view.deleteSelected
      }

      then("the step should have no element")
      view.step.elements should be ('empty)

      and("the view for e0 should no longer exist")
      evaluating  { view.viewForElement(e0) } should produce [NoSuchElementException]
    }

  }

  feature("New elements can be added"){

    scenario("adding a new text element", SingleSpec){

      given("a new text element")
      val e = new TextElement("New Text") {
        x = 10; y = 50; width = 10; height = 10;
      }

      evaluating {view.viewForElement(e)} should produce [NoSuchElementException]

      when("this element is added to the step")
      step.elements += e

      then("a view is creatd for this step")
      view.viewForElement(e)

      and("this view should be selected")

    }
  }

}