import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.util.GenericOptionsParser
import scala.jdk.CollectionConverters._

// This class performs the map operation, translating raw input into the key-value
// pairs we will feed into our reduce operation.
class TokenizerMapper extends Mapper[Object, Text, Text, IntWritable] {
  val one = new IntWritable(1)
  val day = new Text

  override def map(
      key: Object,
      value: Text,
      context: Mapper[Object, Text, Text, IntWritable]#Context
  ) = {
    try {
      day.set(value.toString().split(",")(2).takeWhile(_ != 'T'))
    } catch {
      case e: ArrayIndexOutOfBoundsException => day.set("NONE")
    }
    context.write(day, one)
  }
}

// This class performs the reduce operation, iterating over the key-value pairs
// produced by our map operation to produce a result. In this case we just
// calculate a simple total for each day seen.
class IntSumReducer extends Reducer[Text, IntWritable, Text, IntWritable] {
  override def reduce(
      key: Text,
      values: java.lang.Iterable[IntWritable],
      context: Reducer[Text, IntWritable, Text, IntWritable]#Context
  ) = {
    val sum = values.asScala.foldLeft(0) { (t, i) => t + i.get }
    context.write(key, new IntWritable(sum))
  }
}

// This class configures and runs the job with the map and reduce classes we've
// specified above.
object DailyTrips {

  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    val otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs
    if (otherArgs.length != 2) {
      println("Usage: dailytrips <in> <out>")
      sys.exit(2)
    }
    val job = Job.getInstance(conf, "daily trips")

    job.setNumReduceTasks(3)
    job.setJarByClass(classOf[TokenizerMapper])
    job.setMapperClass(classOf[TokenizerMapper])
    job.setCombinerClass(classOf[IntSumReducer])
    job.setReducerClass(classOf[IntSumReducer])
    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[IntWritable])
    FileInputFormat.addInputPath(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path((args(1))))
    if (job.waitForCompletion(true)) sys.exit(0) else sys.exit(1)
  }
}
