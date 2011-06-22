package test.scala

import _root_.guide.{View, Element}
import org.scalatest.matchers.{MustMatchers, ShouldMatchers}
import org.scalatest.{GivenWhenThen, FlatSpec, FeatureSpec}


class ModelViewSpec extends FeatureSpec with GivenWhenThen with MustMatchers{

  feature("A View can be automatically updated when its data Model is modified"){

    scenario("An element's spatial properties are changed") {

      given("an element and its view")
      var element = new Element{
        x = 10; y = 10; width = 100; height = 100
      }
      var view = View(element)

      when("the element's location is changed to (20,20)")
      element.x := 20
      element.y := 20

      then("the view's location should be (20,20)")
      view.component.location.x must be (20)
      view.component.location.y must be (20)

      when("the element's size is changed to (50,50)")
      element.width := 50
      element.height := 50

      then("the view's size should be (50,50)")
      view.component.size.width must be (50)
      view.component.size.height must be (50)


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