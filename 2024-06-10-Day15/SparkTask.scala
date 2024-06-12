import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.functions._

object SparkTask3 {
  def main(args: Array[String]): Unit = {
    try {
      val spark = SparkSession.builder()
        .appName("Spark Task 3")
        .getOrCreate()

      val inputPath = "hdfs:///employees.csv"
      val outputPath = "hdfs:///employees.json"

      val df = spark.read
        .option("header", "true")
        .option("inferSchema", "true")
        .csv(inputPath)

      val filteredDf = df.filter(col("salary") > 55000)

      filteredDf.show()

      val aggData = filteredDf.groupBy("department").agg(sum("salary").alias("aggSalaries")).select("department", "aggSalaries")

      aggData.show()

      aggData.write
        .mode("overwrite")
        .json(outputPath)

      aggData.write.format("jdbc")
        .option("url", "jdbc:mysql://35.197.52.85:3306/tasks")
        .option("dbtable", "agg_salaries")
        .option("user", "admin")
        .option("password", "Password@131299")
        .mode(SaveMode.Overwrite) // Overwrite existing data
        .save()

      spark.stop()
    }
    catch {
      case e: Exception => println(s"Exception occurred: $e")
    }
  }
}
