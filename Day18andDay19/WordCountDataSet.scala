import org.apache.spark.sql.{SparkSession, Dataset, Encoders}

object WordCountDataset {
  case class Line(value: String)
  case class WordCount(word: String, count: Long)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("WordCountDataset").master("local[*]").getOrCreate()

    import spark.implicits._

    // Read the text file into a Dataset
    val input: Dataset[Line] = spark.read.textFile("/Users/srinadh/Documents/scala-training/scala-training/inputFile.txt").map(Line(_))

    // Transformations
    val words = input.flatMap(line => line.value.split(" ")).withColumnRenamed("value", "word")
    val wordCounts = words.groupBy("word").count().as[WordCount] // Count occurrences of each word

    // Action
    wordCounts.show() // Show results

    spark.stop()
  }
}
