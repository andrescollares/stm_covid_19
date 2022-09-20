import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.util.GenericOptionsParser
import scala.jdk.CollectionConverters._
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapred
import scala.io.Source
import java.net.URI
import java.io.File

// This class performs the map operation, translating raw input into the key-value
// pairs we will feed into our reduce operation.
// OutputKey will be neighbour+date on a single string in order to reduce it
class TokenizerMapper1 extends Mapper[Object, Text, Text, IntWritable] {
  val one = new IntWritable(1)
  val day_nb_st = new Text
  var invMap: Map[String, String] = null

  override def setup(
      context: Mapper[Object, Text, Text, IntWritable]#Context
  ) = {
    val nbh_stp_file = new File("./nbh_stp")
    invMap = {
      for {
        l <- Source.fromFile(nbh_stp_file, "UTF-8").getLines
      } yield {
        var sp = l.split(":")
        (sp(0), sp(1))
      }
    }.toMap
  }

  override def map(
      key: Object,
      value: Text,
      context: Mapper[Object, Text, Text, IntWritable]#Context
  ) = {
    // A través del hashmap se asignan barrios a cada valor
    try {
      val row_elems = value.toString().split(",")
      day_nb_st.set(
        row_elems(2).takeWhile(_ != 'T') + "_" + (invMap
          .getOrElse(row_elems(11), "OTHER"))
      )
    } catch {
      case e: ArrayIndexOutOfBoundsException => day_nb_st.set("NONE")
    } finally {
      context.write(day_nb_st, one)
    }
  }
}

// This class performs the reduce operation, iterating over the key-value pairs
// produced by our map operation to produce a result. In this case we just
// calculate a simple total for each day seen.
class IntSumReducer1 extends Reducer[Text, IntWritable, Text, IntWritable] {
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
object CityMovementNeighborhood {

  def main(args: Array[String]): Unit = {

    val conf = new Configuration()
    val otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs
    if (otherArgs.length != 2) {
      println("Usage: dailytrips <in> <out>")
      sys.exit(2)
    }
    val job = Job.getInstance(conf, "movement neighborhood")

    job.addCacheFile(
      new URI("/data/parada-barrio.data" + "#nbh_stp")
    )
    job.setNumReduceTasks(3)
    job.setJarByClass(classOf[TokenizerMapper1])
    job.setMapperClass(classOf[TokenizerMapper1])
    job.setCombinerClass(classOf[IntSumReducer1])
    job.setReducerClass(classOf[IntSumReducer1])
    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[IntWritable])
    FileInputFormat.addInputPath(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path((args(1))))
    if (job.waitForCompletion(true)) sys.exit(0) else sys.exit(1)
  }
}
