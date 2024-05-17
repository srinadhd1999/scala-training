import scala.io.StdIn.* 
import scala.collection.mutable.ArrayBuffer

class Organisation {
    val payEmpBuffer: ArrayBuffer[(Int, String, String, String)] = ArrayBuffer()
    val marEmpBuffer: ArrayBuffer[(Int, String, String, String)] = ArrayBuffer()
    val advEmpBuffer: ArrayBuffer[(Int, String, String, String)] = ArrayBuffer()
    val salesEmpBuffer: ArrayBuffer[(Int, String, String, String)] = ArrayBuffer()
    val salesManBuffer: ArrayBuffer[(Int, String, String, String)] = ArrayBuffer()

    def returnString(empTuple: (Int, String, String, String)): String = {
        val sno = empTuple(0)
        val name = empTuple(1)
        val city = empTuple(2)
        return s"($sno, $name, $city)"
    }

    def deptFinder(empTuple: (Int, String, String, String)): Unit = {
        if(empTuple(3) == "Payments") {
            this.payEmpBuffer.append(empTuple)
        }
        else if(empTuple(3) == "Marketing") {
            this.marEmpBuffer.append(empTuple)
        }
        else if(empTuple(3) == "Advertisements") {
            this.advEmpBuffer.append(empTuple)
        }
        else if(empTuple(3) == "Sales") {
            this.salesEmpBuffer.append(empTuple)
        }
        else if(empTuple(3) == "SalesManagement") {
            this.salesManBuffer.append(empTuple)
        }
    }

    def printOrgChart(): Unit = {
        println("Organisation")
        println("|__ Finance")
        println("   |__ Payments")
        for(i <- 0 until this.payEmpBuffer.length) {
        println("   |   |__ "+this.returnString(this.payEmpBuffer(i)))
        }
        println("   |")
        println("   |__ Sales")
        println("       |__ Marketing")
        for(i <- 0 until this.marEmpBuffer.length) {
        println("       |   |__ "+this.returnString(this.marEmpBuffer(i)))  
        }
        println("       |")
        for(i <- 0 until this.salesEmpBuffer.length) {
        println("       |__ "+this.returnString(this.salesEmpBuffer(i)))  
        }
        println("       |")
        println("       |__ Advertisements")
        for(i <- 0 until this.advEmpBuffer.length) {
        println("       |   |__ "+this.returnString(this.advEmpBuffer(i)))  
        }
        println("       |")
        println("       |__ Sales Management")
        for(i <- 0 until this.salesManBuffer.length) {
        println("       |   |__ "+this.returnString(this.salesManBuffer(i)))  
        }
    }
}

class Employee(sno: Int, name: String, city: String, department: String) extends Organisation {
    
    def deptAllocator(organisation: Organisation): Unit = {
        val empTuple = (this.sno, this.name, this.city, this.department)
        organisation.deptFinder(empTuple)
    }
}

@main def doIt() = {
    val organisation: Organisation = new Organisation()
    println("if you want to enroll an employee please type yes or else type exit")
    var decision: String = readLine()
    while(decision != "exit") {
        println("Enter the employee details")
        println("Enter sno")
        val sno: Int = readInt()
        println("Enter name")
        val name: String = readLine()
        println("Enter city")
        val city: String = readLine()
        println("Enter department")
        val department: String = readLine()
        val employee: Employee  = new Employee(sno, name, city, department)
        employee.deptAllocator(organisation)
        organisation.printOrgChart()
        println("if you want to enroll an employee please type yes or else type exit")
        decision = readLine()
    }
}