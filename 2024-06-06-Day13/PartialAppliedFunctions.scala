import scala.collection.mutable.ArrayBuffer

def method(a: Int, b: Int, c: Int): Int = {
    return a * b * c
}

def multiplyBy2(x: Int, y: Int): Int = {
    return x*y
}

@main def PartialAppliedFunctions = {
    val method1 = method(2,_,_)
    println(method1(3,4))
    val method2 = method(3,4,_)
    println(method2(5))
    println(method(5,6,7))
    println("-----")
    val list: ArrayBuffer[Int] = ArrayBuffer(1,2,3,4,5)
    val method3 = multiplyBy2(2, _)
    list.map(method3).foreach(println)
}