package test.scala

import _root_.guide._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import scala.swing.Reactor
import collection.mutable.ListBuffer

class StepSpec extends FlatSpec with ShouldMatchers{

  "A Step" should "be created empty without any element" in {
      val step = new Step
      step.elements should be ('empty)
    }

  it should "allow an element to be added to it" in {
      val step = new Step
      step.elements += Element((50,50,100,100))
      step.elements should have size(1)
  }

  it should "allow a copy to be made" in {
      val step = StepFixture.example
      val copy = step.copy

      copy.elements.size should be > 0
      copy.elements.size should be (step.elements.size)
      copy.elements should not be ('sameElements(step.elements))

      assert((copy.elements zip step.elements).forall ( p => p._1.x === p._2.x))
  }

  class StepEventRecorder(val step : Step) extends Reactor {

      var eventsReceived = ListBuffer[StepEvent]()

      listenTo(step.elements)
      reactions += {
        case e: ElementAdded => eventsReceived += e
        case e: ElementRemoved => eventsReceived += e
      }
  }

  it should "allow content to be cleared" in {
    val step = StepFixture.example
    val n = step.elements.size

    val eventRecorder = new StepEventRecorder(step)

    step.elements.clear
    step.elements should be ('empty)

    eventRecorder.eventsReceived should have size(n)
    assert(eventRecorder.eventsReceived.forall(e => e.isInstanceOf[ElementRemoved]))
  }

  it should "notify reactors when elements are added/removed"  in {

      val step = new Step

      val eventRecorder = new StepEventRecorder(step)

      val e =  Element((50,50,100,100))

      step.elements += e
      assert(eventRecorder.eventsReceived.last.isInstanceOf[ElementAdded])

      step.elements -= e
      assert(!eventRecorder.eventsReceived.last.isInstanceOf[ElementAdded])
      assert(eventRecorder.eventsReceived.last.isInstanceOf[ElementRemoved])
  }

}