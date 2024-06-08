import org.apache.spark.Success
import org.apache.spark.sql.{SaveMode, SparkSession}

import scala.util.Success

object SparkTask1 {
  def main(args: Array[String]): Unit = {
    try {
      val spark = SparkSession.builder()
        .appName("S3 Example with Spark")
        .master("local[*]")
        .config("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
        .getOrCreate()

      spark.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", sys.env.getOrElse("AWS_ACCESS_KEY", ""))
      spark.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", sys.env.getOrElse("AWS_SECRET_KEY", ""))
      spark.sparkContext.hadoopConfiguration.set("fs.s3a.endpoint", "s3.amazonaws.com")
      spark.sparkContext.hadoopConfiguration.set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")


      val s3Data = spark.read.json("s3a://scala-task-bucket/employees.json")

      val s3FilteredData = s3Data.filter("department = 'Finance'").filter("salary>60000").select("employee_id", "first_name", "last_name", "email", "salary")

      s3FilteredData.show()

      s3Data.write.format("jdbc")
        .option("url", sys.env.getOrElse("JDBC_URL", ""))
        .option("dbtable", "employees1")
        .option("user", sys.env.getOrElse("DB_USER", ""))
        .option("password", sys.env.getOrElse("DB_PASS", ""))
        .mode(SaveMode.Overwrite) // Overwrite existing data
        .save()
    }
    catch {
      case e: Exception => println(s"Exception occurred: $e")
    }
  }
}
