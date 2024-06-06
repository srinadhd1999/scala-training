import scala.collection.mutable.ArrayBuffer

val partialFunction: PartialFunction[Int, String] = {
    //It is defined only for the part where the marks between 1 and 10 
    case marks: Int if marks>=1 && marks<=10 => s"Marks within range: $marks"
}

@main def PartialFunction: Unit = {
    // We can use isDefinedAt method to check whether the method is defined for a particular part or not 
    println("Partial Function tutorial")
    println(partialFunction.isDefinedAt(20))
    println(partialFunction.isDefinedAt(6))
    println(partialFunction(5))

    val list: ArrayBuffer[Int] = ArrayBuffer(1,2,11,12)
    val result = list.collect(partialFunction)
    println(result)
}

