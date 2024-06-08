import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.sum

object SparkTask2 {
  def main(args: Array[String]): Unit = {
    try {
      val sparkSession = SparkSession.builder()
        .appName("Spark")
        .master("local[2]")
        .config("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
        .getOrCreate()

      sparkSession.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", sys.env.getOrElse("AWS_ACCESS_KEY", ""))
      sparkSession.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", sys.env.getOrElse("AWS_SECRET_KEY", ""))
      sparkSession.sparkContext.hadoopConfiguration.set("fs.s3a.endpoint", "s3.ap-south-1.amazonaws.com")
      sparkSession.sparkContext.hadoopConfiguration.set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")

      val employeesData = sparkSession.read.format("jdbc")
        .option("url", sys.env.getOrElse("JDBC_URL", ""))
        .option("dbtable", "employees")
        .option("user", sys.env.getOrElse("DB_USER", ""))
        .option("password", sys.env.getOrElse("DB_PASS", ""))
        .load()

      val employeesFilteredData = employeesData.filter("salary > 60000")

      employeesFilteredData.show()

      employeesFilteredData.write.mode("overwrite").csv("s3a://scala-task-bucket/employeesFilteredData")

      val employeesAggData = employeesFilteredData.groupBy("department").agg(sum("salary").alias("aggSalary")).select("department", "aggSalary")

      employeesAggData.show()

      employeesAggData.write.mode("overwrite").csv("s3a://scala-task-bucket/employeesAggData")
    }
    catch {
      case e: Exception => println(s"Exception occurred: $e")
    }
  }
}
