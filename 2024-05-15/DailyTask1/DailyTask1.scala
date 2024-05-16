import scala.collection.mutable.Map
import scala.io.StdIn.readLine


def methodWithCallBackParameter(data: Map[String, (Int, Int)], arr: Array[Array[String]], callBack: Array[Array[String]] => String): Unit = {
    println("Enter the seat numbers in space separated values,if you don't want to book any tickets please enter null")
    var seats: String = readLine()
    while(seats!="null") {
        var seatsSplit: Array[String] = seats.split(" ")

        for(i <- 0 until seatsSplit.length) {
            var a=data(seatsSplit(i))(0)
            var b=data(seatsSplit(i))(1)
            if(arr(a)(b)!="X") {
                arr(a)(b)="X"
            }
            else {
                var seatNumber: String = seatsSplit(i);
                println(s"seat number $seatNumber already booked, choose other seat")
            }
        }
        println("booking completed")
        println(callBack(arr))
        println("Enter the seat numbers in space separated values, if you don't want to book any tickets, please enter null")
        seats= readLine()
        if(seats == "null") {
            println("Exiting, Thanks for Booking!")
        }
    }
}

def callBackFunction(arr: Array[Array[String]]): String = {
    println("Available seats")
    println("X - Booked, Remaining - Available")
    for(i <- 0 until 10) {
        for(j <- 0 until 10) {
            print(arr(i)(j)+" ")
        }
        println()
    }
    return "Done"
}

@main def matrixTask() = {
    val arr: Array[Array[String]] = Array.ofDim[String](10,10)
    val map = Map[String,(Int, Int)]()
    var k=1
    for(i <- 0 until 10) {
        for(j <- 0 until 10) {
            arr(i)(j)=k.toString()
            map(k.toString())=(i,j)
            k=k+1
        }
    }
    println("Available seats")
    println("X - Booked, Remaining - Available")
    for(i <- 0 until 10) {
        for(j <- 0 until 10) {
            print(arr(i)(j)+" ")
        }
        println()
    }
    methodWithCallBackParameter(map, arr, callBackFunction)
} 