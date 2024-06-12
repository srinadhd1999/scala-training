import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets


object SparkTask4 {

  def main(args: Array[String]): Unit =  {
    try {
      val spark = SparkSession.builder()
        .appName("Spark Task 4")
        .getOrCreate()

      val employeeDf = spark.read.format("jdbc")
        .option("url", "jdbc:mysql://35.197.52.85:3306/tasks")
        .option("dbtable", "employee")
        .option("user", "admin")
        .option("password", "Password@131299")
        .load()

      employeeDf.show()

      val departmentDf = spark.read.format("jdbc")
        .option("url", "jdbc:mysql://35.197.52.85:3306/tasks")
        .option("dbtable", "department")
        .option("user", "admin")
        .option("password", "Password@131299")
        .load()

      departmentDf.show()

      val employeeListDf = employeeDf.groupBy("department_id").agg(collect_list(struct(col("employee_id"), col("first_name"), col("last_name"), col("email"), col("salary"))).as("employees"))

      employeeListDf.show()

      val joinedDf = employeeListDf.join(departmentDf, Seq("department_id"), "inner").select("department_id", "department_name", "employees")

      joinedDf.show()

      val outputPath = "hdfs:///task/output/finalJson"

//      val finalJsonDf = joinedDf.withColumn("finalJson", to_json(struct(col("department_id"), col("department_name"), col("employees"))))

//      val storeJsonDf = joinedDf.select("finalJson")

      joinedDf.write.mode("overwrite").json(outputPath)

//      val finalJsonArrayDf = finalJsonDf.agg(collect_list(col("finalJson")).alias("jsonArray"))
//
//      finalJsonArrayDf.show()
//
//      val finalJsonArrayRow = finalJsonArrayDf.selectExpr("concat('[', concat_ws(',', jsonArray), ']') as jsonArrayString").collect()(0)
//      val finalJsonArrayString = finalJsonArrayRow.getString(0)
//
//      println(finalJsonArrayString)
//
//      val hadoopConf = new Configuration()
//      val hdfs = FileSystem.get(hadoopConf)
//      val outputStream = hdfs.create(new Path(outputPath))
//      val writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)
//      writer.write(finalJsonArrayString)

    } catch {
      case e: Exception => println(s"Error occurred: $e")
    }
  }
}