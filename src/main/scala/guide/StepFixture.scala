package guide

import javax.imageio.ImageIO
import java.io.File

/**
 * Created by IntelliJ IDEA.
 * User: tomyeh
 * Date: 6/21/11
 * Time: 10:19 PM
 * To change this template use File | Settings | File Templates.
 */

object StepFixture {

  def example : Step = {
    var step = new Step

    var element = Element((150,10,200,200))
    step.elements += element

    var textElement = TextElement("some text", (10,10,100,100))
    step.elements += textElement

    var circle = CircleElement((50,200,100,100))
    step.elements += circle

    var rect = RectangleElement((100,300,100,50))
    step.elements += rect

    step.contextImage = ImageIO.read(new File("screen1.png"))

    step
  }

}