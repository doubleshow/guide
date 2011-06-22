package test.scala

import _root_.guide._
import _root_.guide.Property._
import org.scalatest.{GivenWhenThen, FeatureSpec}
import org.scalatest.matchers.{MatchResult, BeMatcher, ShouldMatchers}
import swing.{Frame,  UIElement}
import java.awt.{Dimension, Point, Component}
import scala.swing.Button
import swing.event.MousePressed
import java.awt.event.InputEvent

import org.sikuli.script.natives.Vision
import org.sikuli.script.OpenCV
import javax.imageio.ImageIO
import java.io.File
import com.wapmx.nativeutils.jniloader.NativeLoader

/**
 * Created by IntelliJ IDEA.
 * User: tomyeh
 * Date: 6/20/11
 * Time: 1:57 PM
 * To change this template use File | Settings | File Templates.
 */



import org.sikuli.script.Screen

class SwingSpec extends FeatureSpec with ShouldMatchers with GivenWhenThen {

  class VisibilityMatcher extends BeMatcher[UIElement] {
    def apply(left: UIElement) =
      MatchResult( left.visible, left.toString + " was visible", left.toString + " was invisible")

  }

  class SimpleWindow extends Frame {
    location = new Point(100,100)
    size = new Dimension(500,500)
  }

  val visible = new VisibilityMatcher

  def makeStep : Step = {
      var step = new Step

    	var element = new Element{x = 150; y = 151; width = 210; height = 20}

    	step.elements += element

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

  feature("Simple Swing Window"){

    scenario("Window is shown"){

      given("A simple JWindow")
      var win = new SimpleWindow

      when("made visible")
      win.visible = true
      win.peer.setAlwaysOnTop(true)


      var step = makeStep

      var view = new StepThumbView(makeStep)
      view.preferredSize = new Dimension(500,500)

      win.contents = view

      then("test")
      win should be (visible)



      Thread.sleep(1000)

      //val screen = new Screen

//      try{
      //NativeLoader.loadLibrary("VisionProxy");
         //Debug.info("Sikuli vision engine loaded.");
//      }
//      catch( e){
//         e.printStackTrace();
//      }
//      Debug.ENABLE_PROFILING = true;

      //screen.click("clickme.png",0)
//      val img = ImageIO.read(new File("clickme.png"))
//      val mat = OpenCV.convertBufferedImageToMat(img)
//      Vision.analyze(mat)

//      val t = new Thread{
//
//        override def run() {
//          val r = new java.awt.Robot
//          r.mouseMove(160,130)
//          r.waitForIdle()

//          r.mousePress(InputEvent.BUTTON1_MASK)
//          r.mouseRelease(InputEvent.BUTTON1_MASK)
//          r.mousePress(InputEvent.BUTTON1_MASK)
//          r.mouseRelease(InputEvent.BUTTON1_MASK)
//
//          r.waitForIdle()
//
//          r.mousePress(InputEvent.BUTTON1_MASK)
//          r.mouseRelease(InputEvent.BUTTON1_MASK)
      //  }
//      }
   //   Thread.sleep(1000)

     // win.button.text should be ("hello world")


     // win.dispose()

    }


  }
}