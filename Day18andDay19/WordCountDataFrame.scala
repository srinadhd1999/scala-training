import org.apache.spark.sql.{SparkSession, functions => F}

object WordCountDataFrame {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("WordCountDataFrame").master("local[*]").getOrCreate()

    // Read the text file into a DataFrame
    val input = spark.read.text("/Users/srinadh/Documents/scala-training/scala-training/inputFile.txt")

    // Transformations
    val words = input.select(F.explode(F.split(F.col("value"), " ")).as("word")) // Split lines into words
    val wordCounts = words.groupBy("word").count() // Count occurrences of each word

    // Action
    wordCounts.show() // Show results

    spark.stop()
  }
}