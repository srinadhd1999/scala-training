import scala.io.StdIn.* 
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer

class Organisation {
    var employeeMap: Map[String, ListBuffer[(Int, String, String)]]  = Map()
    var hierarchyMap: Map[String, ListBuffer[String]] = Map("Organisation" -> ListBuffer[String]())
    var empDirectMap: Map[String, ListBuffer[(Int, String, String)]] = Map()

    //Heirarchy Map Populator - populates the hierarchy map based on the parent department
    def hierarchyMapPopulator(parentDepartment: String, department: String): Unit = {
        if(hierarchyMap.contains(parentDepartment)) {   
            hierarchyMap.get(parentDepartment) match {
                case Some(depList) =>
                    if (depList.contains(department)) {
                        println()
                    } else {
                        hierarchyMap.get(parentDepartment).foreach(_.append(department))
                    }
                case None =>
                    println(s"No departments found for parent department: $parentDepartment")
            }
            if(!hierarchyMap.contains(department)) {
                hierarchyMap += (department -> mutable.ListBuffer[String]())
            }
        } else {
            if(!hierarchyMap.get("Organisation").contains(department)) {
                hierarchyMap.get("Organisation").foreach(_.append(parentDepartment))
            }
            hierarchyMap += (parentDepartment -> mutable.ListBuffer[String](department))
            hierarchyMap += (department -> mutable.ListBuffer[String]())
        }
    }

    //Appends the employee record to the emp details map
    def empAppender(empTuple: (Int, String, String), department: String, parentDepartment: String): Unit = {
        if(employeeMap.contains(department)) {
            employeeMap.get(department).foreach(_.append(empTuple))
        } else {
            employeeMap += (department -> mutable.ListBuffer(empTuple))
        }
        println(employeeMap)
    }

    //returns the formatted string
    def returnString(empTuple: (Int, String, String, String)): String = {
        val sno = empTuple(0)
        val name = empTuple(1)
        val city = empTuple(2)
        return s"($sno, $name, $city)"
    }

    //prints the organisation chart
    def printOrgChart(org: String, indent: String): Unit = {
        if(org != "Organisation") {
            println( indent + "|--" + org)
        } else {
            println(indent + org)
        }
        hierarchyMap.get(org) match {
        case Some(subDepartments) =>
            if(subDepartments.length>0) {
                for (subDept <- subDepartments) {
                    printOrgChart(subDept, indent + "|  ")
                }
            } else {
                employeeMap.get(org) match {
                case Some(employees) =>
                    if(employees.length>0) {
                        var first = true
                        for (emp <- employees) {
                            println(indent + "|  |-- " + emp)
                        }
                    }
                case None => 
                    println(indent + s"No sub-departments found for $org")
                }
            }
        case None =>
          println(indent + s"No sub-departments found for $org")
      }
    }
}

class Employee(sno: Int, name: String, city: String, department: String, parentDepartment: String) extends Organisation 

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
        println("Enter parent department")
        val parentDepartment: String = readLine()
        val employee: Employee  = new Employee(sno, name, city, department, parentDepartment)
        organisation.hierarchyMapPopulator(parentDepartment, department)
        organisation.empAppender((sno, name, city), department, parentDepartment)
        organisation.printOrgChart("Organisation","")
        println("if you want to enroll an employee please type yes or else type exit")
        decision = readLine()
    }
}