import org.apache.spark.sql._
import org.apache.spark.sql.functions.col

object SparkTask1 {

  private def dfRead(spark: SparkSession, path: String): DataFrame = {
    val df = spark.read.option("header", "true").option("inferSchema", "true").csv(path)
    df
  }

  private def getSparkSession: SparkSession = {
    val sparkSession = SparkSession.builder()
      .appName("Spark Task 5")
      .master("local[*]")
      .config("spark.cassandra.connection.host","cassandra.ap-south-1.amazonaws.com")
      .config("spark.cassandra.connection.port", "9142")
      .config("spark.cassandra.connection.ssl.enabled", "true")
      .config("spark.cassandra.auth.username", "scala-training-user-at-381492062164")
      .config("spark.cassandra.auth.password", "WqN3l0nsq6nuq4moeHSTe5lyg03jwCrJGl9/NPtvS5NYJChrM5flKLWhkPI=")
      .config("spark.cassandra.input.consistency.level", "LOCAL_QUORUM")
      .config("spark.cassandra.connection.ssl.trustStore.path", "/Users/srinadh/cassandra_truststore.jks")
      .config("spark.cassandra.connection.ssl.trustStore.password", "Srinadh@131299")
      .getOrCreate()
    sparkSession
  }

  def main(args: Array[String]): Unit = {
    try {
      val spark = getSparkSession
      val salesDf = dfRead(spark, "/Users/srinadh/Downloads/Sales.csv")
      val productsDf = dfRead(spark, "/Users/srinadh/Downloads/Products.csv").withColumnRenamed("name", "product_name")
      val customersDf = dfRead(spark, "/Users/srinadh/Downloads/Customers.csv").withColumnRenamed("name", "customer_name")
      val productSalesJoinDf = salesDf.join(productsDf, Seq("product_id"), "inner")
      val productSalesTotalDf = productSalesJoinDf.withColumn("sales_amount", col("units")*col("price"))
      val totalPriceCustomersJoinDf = productSalesTotalDf.join(customersDf, Seq("customer_id"), "inner").select(col("transaction_id"), col("product_id"),col("product_name"), col("customer_name"), col("sales_amount"))

      totalPriceCustomersJoinDf.show()
      totalPriceCustomersJoinDf.write
        .format("org.apache.spark.sql.cassandra")
        .options(Map("table" -> "SalesCFamily", "keyspace" -> "tutorialkeyspace"))
        .mode("append")
        .save()

    } catch {
      case e: Exception => println(s"Error Occurred: $e")
    }
  }
}
