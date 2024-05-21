import java.sql._
import scala.collection.mutable.ListBuffer

def backTrack(empNames: ListBuffer[String], finalResult: ListBuffer[ListBuffer[String]], supportList: ListBuffer[String], ind: Int, nEmp: Int): Unit = {
    if(supportList.length == nEmp) {
        val intList: ListBuffer[String] = ListBuffer()
        for(name <- supportList) {
            intList += (name)
        }
        finalResult += intList
    }
    if(ind != empNames.length) {
        for(i <- ind until empNames.length) {
            supportList += (empNames(i))
            backTrack(empNames, finalResult, supportList, i+1, nEmp)
            supportList.remove(supportList.length-1)
        }
    }
}

def getConnectionObj: Connection = {
    Class.forName("com.mysql.cj.jdbc.Driver")
    val url = "jdbc:mysql://hadoop-server.mysql.database.azure.com:3306/srinadh"
    val username = "sqladmin"
    val password = "Password@12345"
    val connection: Connection = DriverManager.getConnection(url, username, password)
    return connection
}

@main def Backtracking() = {
    val connection: Connection = getConnectionObj
    val statement: Statement = connection.createStatement()
    val selectQuery = "select * from employeethreaded where department = \'Sales\'"
    val resultSet = statement.executeQuery(selectQuery)

    val empNames: ListBuffer[String] = ListBuffer()

    while(resultSet.next()) {
        val name = resultSet.getString("name")
        empNames += (name)
    }

    println(empNames)

    var finalResult: ListBuffer[ListBuffer[String]] = ListBuffer()
    if(empNames.length<=3) {
        println(empNames)
    } else {
        backTrack(empNames, finalResult, ListBuffer(), 0, 3)
        for(empName <- finalResult) {
            println(empName(0) + ", " + empName(1) + ", " + empName(2))
        }
    }

    connection.close()
    statement.close()
}