package guide

// adapted from http://blog.schauderhaft.de/2011/05/01/binding-scala-objects-to-swing-components/

class Property[T](var value : T) {
    var listeners = List[T => Unit]()
 
    def apply() = value
 
    def :=(aValue : T) {
        if (value != aValue) {
            value = aValue
            fireEvent
        }
    }

    //override def ==(aValue : AnyRef) : Boolean = value == aValue
    def ===(aProperty : Property[T]) = value == aProperty.value
 
    def registerListener(l : T => Unit) {
        listeners = l :: listeners
    }
 
    private def fireEvent {
        listeners.foreach(_(value))
    }
    
    override def toString() = value.toString()

}


trait PropertyOwner {
    implicit val THE_OWNER = this
}

object Property {
    implicit def apply[T](t : T) : Property[T] =
        new Property(t : T)
 
    implicit def toT[T](p : Property[T]) : T = p()
}