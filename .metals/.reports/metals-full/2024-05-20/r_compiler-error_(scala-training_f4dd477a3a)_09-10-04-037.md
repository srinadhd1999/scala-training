file://<WORKSPACE>/2024-05-17-Day4/Day4Task.scala
### dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition doIt is defined in
  <WORKSPACE>/2024-05-16-Day3/Day3Task.scala
and also in
  <WORKSPACE>/2024-05-16-Day3/Day3TaskUpdated.scala
One of these files should be removed from the classpath.

occurred in the presentation compiler.

action parameters:
offset: 2308
uri: file://<WORKSPACE>/2024-05-17-Day4/Day4Task.scala
text:
```scala
import scala.io.Source
import scala.collection.mutable.ListBuffer
import scala.io.StdIn._

case class EmployeeDetails(sno: Int, name: String, country: String, salary: Int, department: String) 

@main def scalaCsvTask() = {

    //Reading CSV file
    val reader = Source.fromFile("<WORKSPACE>/2024-05-17/data.csv").getLines
    val headerLine = reader.take(1).next
    val empObjList: ListBuffer[EmployeeDetails] = ListBuffer()
    val empList: ListBuffer[(Int, String, String, Int, String)] = ListBuffer()
    for(l <- reader) {
        var employee: EmployeeDetails = new EmployeeDetails(l.split(",")(0).toInt, l.split(",")(1), l.split(",")(2), l.split(",")(3).toInt, l.split(",")(4))
        empObjList += employee
        empList += ((l.split(",")(0).toInt, l.split(",")(1), l.split(",")(2), l.split(",")(3).toInt, l.split(",")(4)))
    }
    println()
    println("=========== Printing the Employee obj details as per the case class ===================")
    println()
    empObjList.foreach(println)
    println()
    println("=========== Printing the Employee details stored in the list ===================")
    println()
    empList.foreach(println)
    println()
    println("Please enter the salary to filter")
    val filterSalary = readInt()
    println("=============== Filtering of data based on salary ==============")
    println()   
    val salaryGreaterFilter = empList.filter(x => x(3) > filterSalary)
    salaryGreaterFilter.foreach(println)
    println()
    println("============== Filtering of data based on departmemt using EmployeeDetails object ===================")
    println()
    println("Please enter the department to filter")
    val filterDepartment = readLine()
     val departmentFilter = empObjList.filter(x => x.department == filterDepartment)
    departmentFilter.foreach(println)
    println()
    println("============== Formattted report ===================")
    println()
    println("S.No | Name      | Country      | Salary      | Department")
    val formattedEmployees = empObjList.map { emp =>
            s"${emp.sno} | ${emp.name} | ${emp.country} | ${emp.salary} | ${emp.department}"
        }
    formattedEmployees.foreach(println)
    println()
    println("========== Printing average of salary and number @@of employees by department ==========")
    println()

    println("Department | TotalSalary | AverageSalary | NumberOfEmployees")
    val groupedByDepartment1 = empObjList.groupBy(_.department)
    for((department, employeesList) <- groupedByDepartment1) {
        val totalSalary = employeesList.map(_.salary).sum
        val totalEmployees = employeesList.size
        val averageSalary = if (totalEmployees > 0) totalSalary/totalEmployees else 0 
        println(s"$department | $totalSalary | $averageSalary | $totalEmployees")
    }
    println()
}

```



#### Error stacktrace:

```

```
#### Short summary: 

dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition doIt is defined in
  <WORKSPACE>/2024-05-16-Day3/Day3Task.scala
and also in
  <WORKSPACE>/2024-05-16-Day3/Day3TaskUpdated.scala
One of these files should be removed from the classpath.