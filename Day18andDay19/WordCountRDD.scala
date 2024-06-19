import org.apache.spark.{SparkConf, SparkContext}

object WordCountRDD {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WordCountRDD").setMaster("local[*]")
    val sc = new SparkContext(conf)

    // Read the text file into an RDD
    val input = sc.textFile("/Users/srinadh/Documents/scala-training/scala-training/inputFile.txt")

    // Transformations
    val words = input.flatMap(line => line.split(" ")) // Split lines into words
    val wordCounts = words.map(word => (word, 1)).reduceByKey(_ + _) // Count occurrences of each word

    // Action
    wordCounts.collect().foreach(println) // Collect and print results

    sc.stop()
  }
}