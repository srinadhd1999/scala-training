import scala.io.Source
import java.sql.{Connection, Statement, ResultSet, DriverManager}
import scala.collection.mutable.ListBuffer
import scala.collection.mutable

case class Departments(dno: Int, dname: String)

case class Employees(sno: Int, name: String, city: String, salary: Int, department: String) 

var empDeptMap: Map[String, ListBuffer[(Int, String, String, Int, String)]] = Map()

def getConnection: Connection = {
    Class.forName("com.mysql.cj.jdbc.Driver")
    val url = "jdbc:mysql://hadoop-server.mysql.database.azure.com:3306/srinadh"
    val username = "sqladmin"
    val password = "Password@12345"
    val connection: Connection = DriverManager.getConnection(url, username, password)
    return connection
}

def mapDeptAndEmp(empObjList: ListBuffer[Employees]): Unit = {
    val connection: Connection = getConnection
    val statement: Statement = connection.createStatement()
    var deptMap: Map[String, Int] = Map[String, Int]()
    var id=1
    var record = 1
    for(emp <- empObjList) {
        if(!deptMap.contains(emp.department)) {
            deptMap += (emp.department -> id)
            // populateDepartmentsTable(id, emp.department, connection, statement)
            id = id + 1
        }
    }
    for(emp <- empObjList) {
        // populateEmployeeTable(emp, deptMap.getOrElse(emp.department, -1), connection, statement)
        // println(s"inserted record $record")
        if(empDeptMap.contains(emp.department)) {
            empDeptMap.get(emp.department).foreach(_.append((emp.sno, emp.name, emp.city, emp.salary, emp.department)))
        } else {
            empDeptMap += (emp.department -> mutable.ListBuffer((emp.sno, emp.name, emp.city, emp.salary, emp.department)))
        }
        record = record + 1
    }
}

def printChart(): Unit = {
    for((dept, empList) <- empDeptMap) {
        println("|- "+dept)
        for(emp <- empList) {
            println(s"|  |-- ${emp(1)}")
        }
    }
}

def populateDepartmentsTable(id: Int, department: String, connection: Connection, statement: Statement): Unit = {
    val insertSQL =s"INSERT INTO departments (id, department) VALUES ($id, \"$department\")"
    statement.executeUpdate(insertSQL)
}

def populateEmployeeTable(emp: Employees, id: Int, connection: Connection, statement: Statement): Unit = {
    val insertSQL =s"INSERT INTO employees (empId, name, city, salary, deptId, department) VALUES (${emp.sno}, \"${emp.name}\", \"${emp.city}\" , ${emp.salary}, $id,\"${emp.department}\")"
    statement.executeUpdate(insertSQL)
}

@main def DatabaseTask() = {
    //Reading CSV file
    val reader = Source.fromFile("/Users/srinadh/Documents/scala-training/scala-training/2024-05-20-Day5/data.csv").getLines
    val headerLine = reader.take(1).next
    val empObjList: ListBuffer[Employees] = ListBuffer()
    val empList: ListBuffer[(Int, String, String, Int, String)] = ListBuffer()
    var departments: ListBuffer[String] = ListBuffer()
    var finalEmployeesList: ListBuffer[(Int, String, String, Int, Int, String)] = ListBuffer()
    for(l <- reader) {
        var employee: Employees = new Employees(l.split(",")(0).toInt, l.split(",")(1), l.split(",")(2), l.split(",")(3).toInt, l.split(",")(4))
        empObjList += employee
        empList += ((l.split(",")(0).toInt, l.split(",")(1), l.split(",")(2), l.split(",")(3).toInt, l.split(",")(4)))
    }
    mapDeptAndEmp(empObjList)
    printChart()
}
