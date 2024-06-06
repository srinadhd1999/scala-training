import scala.collection.mutable._

@main def Underscore: Unit = {

    //Wildcard pattern matching
    val a: Any = "Hello"
    a match {
        case _: Int => println("It's an integer")
        case _ => println("It's something else")
    }

    //Placeholder
    val list = List(1, 2, 3, 4)
    val doubledList = list.map(_ * 2) // Equivalent to list.map(x => x * 2)
    doubledList.foreach(println)
}