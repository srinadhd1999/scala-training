import scala.io.Source
import java.sql.{Connection, Statement, ResultSet, DriverManager}
import scala.collection.mutable.ListBuffer
import scala.collection.mutable
import java.util.concurrent._
import java.time.LocalDateTime


case class DepartmentsThread(dno: Int, dname: String)

case class EmployeesThreaded(sno: Int, name: String, city: String, salary: Int, department: String) 

var empThreadDeptMap: Map[String, ListBuffer[(Int, String, String, Int, String)]] = Map()

def getDbConnection: Connection = {
    Class.forName("com.mysql.cj.jdbc.Driver")
    val url = "jdbc:mysql://hadoop-server.mysql.database.azure.com:3306/srinadh"
    val username = "sqladmin"
    val password = "Password@12345"
    val connection: Connection = DriverManager.getConnection(url, username, password)
    return connection
}

def mapDeptAndEmpThread(empObjList: ListBuffer[EmployeesThreaded]): Unit = {
    val connection: Connection = getDbConnection
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
    val pool: ExecutorService = Executors.newFixedThreadPool(5)
    for(emp <- empObjList) {
        pool.submit(new Runnable {
            def run(): Unit = {
                populateThreadedEmployeeTable(emp, deptMap.getOrElse(emp.department, -1), connection, statement)
            }
        })
        if(empThreadDeptMap.contains(emp.department)) {
            empThreadDeptMap.get(emp.department).foreach(_.append((emp.sno, emp.name, emp.city, emp.salary, emp.department)))
        } else {
            empThreadDeptMap += (emp.department -> mutable.ListBuffer((emp.sno, emp.name, emp.city, emp.salary, emp.department)))
        }
        record = record + 1
    }
    pool.shutdown()
    pool.awaitTermination(Long.MaxValue, TimeUnit.NANOSECONDS)
    connection.close()
    statement.close()
}

def printEmpChart(): Unit = {
    for((dept, empList) <- empThreadDeptMap) {
        println("|- "+dept)
        for(emp <- empList) {
            println(s"|  |-- ${emp(1)}")
        }
    }
}

def populateThreadedDepartmentsTable(id: Int, department: String, connection: Connection, statement: Statement): Unit = {
    val insertSQL =s"INSERT INTO departments (id, department) VALUES ($id, \"$department\")"
    statement.executeUpdate(insertSQL)
}

def populateThreadedEmployeeTable(emp: EmployeesThreaded, id: Int, connection: Connection, statement: Statement): Unit = {
    val timestamp: LocalDateTime = LocalDateTime.now()
    val insertSQL =s"INSERT INTO employeethreaded (empId, name, city, salary, deptId, department, threadname, timestamp) VALUES (${emp.sno}, \"${emp.name}\", \"${emp.city}\" , ${emp.salary}, $id,\"${emp.department}\", \"${Thread.currentThread().getName()}\", \"${timestamp}\")"
    statement.executeUpdate(insertSQL)
    println("insert row successfully")
}

@main def ThreadTask() = {
    //Reading CSV file
    val reader = Source.fromFile("/Users/srinadh/Documents/scala-training/scala-training/2024-05-21-Day6/data.csv").getLines
    val headerLine = reader.take(1).next
    val empObjList: ListBuffer[EmployeesThreaded] = ListBuffer()
    val empList: ListBuffer[(Int, String, String, Int, String)] = ListBuffer()
    var departments: ListBuffer[String] = ListBuffer()
    var finalEmployeesList: ListBuffer[(Int, String, String, Int, Int, String)] = ListBuffer()
    for(l <- reader) {
        var employee: EmployeesThreaded = new EmployeesThreaded(l.split(",")(0).toInt, l.split(",")(1), l.split(",")(2), l.split(",")(3).toInt, l.split(",")(4))
        empObjList += employee
        empList += ((l.split(",")(0).toInt, l.split(",")(1), l.split(",")(2), l.split(",")(3).toInt, l.split(",")(4)))
    }
    mapDeptAndEmpThread(empObjList)
    printEmpChart()
}