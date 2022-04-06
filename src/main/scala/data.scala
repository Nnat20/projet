import org.apache.spark.sql._
import org.apache.log4j.Logger
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming._
import org.apache.log4j._
import org.apache.log4j.LogManager



object data {
  var ss: SparkSession = null
  var spConf : SparkConf = null

  private var trace_log : Logger = LogManager.getLogger("Logger_Console")
  /**
   * fonction qui initialise et instancie une session spark
   *
   * @param env : variable qui indique l'environnement sur lequel notre application est déployéé.
   *            Si env est true alors l'appli est déployé en local sinon elle est deployé sur un cluster
   */
  def Session_Spark(env: Boolean = true): SparkSession = {
    if (env == true) {
      System.setProperty("hadoop.home.dir", "C:/Hadoop")
      ss = SparkSession.builder
        .master("local[*]")
        .config("spark.sql.crossJoin.enabled", true)
        //.enableHiveSupport()
        .getOrCreate()

    } else {
      ss = SparkSession.builder
        .appName("Mon application Spark")
        .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        .config("spark.sql.crossJoin.enabled", true)
        //.enableHiveSupport()
        .getOrCreate()
    }
    ss
  }

    /**
     * fonction qui initialise le contexte Spark Streaming
     * @param env : environnement sur lequel est déployé notre application. Si true, alors on est en localhost
     * @param duree_batch : c'est le SparkStreamingBatchDuration - où la durée du micro-batch
     * @return : la fonction renvoie en résultat une instance du contexte Streaming
     */

    def getSparkStreamingContext (env : Boolean = true, duree_batch : Int) : StreamingContext = {
      trace_log.info("initialisation du contexte Spark Streaming")
      if (env) {
        spConf = new SparkConf().setMaster("local[*]")
          .setAppName("Mon application streaming")
      } else {
        spConf = new SparkConf().setAppName("Mon application streaming")
      }
      trace_log.info(s"la durée du micro-bacth Spark est définie à : $duree_batch secondes")
      val ssc : StreamingContext = new StreamingContext(spConf, Seconds(duree_batch))

      return ssc

    }






}
