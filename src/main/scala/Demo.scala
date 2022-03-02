import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.arangodb.commons.{ArangoClient, ArangoDBConf}


object Demo {
  val password: String = System.getProperty("password")
  val endpoints: String = System.getProperty("endpoints")
  val sparkMaster: String = System.getProperty("sparkMaster", "local[*]")
  val dbName: String = System.getProperty("dbName")

  require(endpoints != null)
  require(sparkMaster != null)

  val spark: SparkSession = SparkSession.builder
    .appName("arangodb-demo")
    .master(sparkMaster)
    .config(new SparkConf()
      .setJars(Seq("lib/arangodb-spark-datasource-3.1_2.12-1.1.1-jar-with-dependencies.jar"))
      //            .setJars(Seq("https://repo1.maven.org/maven2/com/arangodb/arangodb-spark-datasource-3.1_2.12/1.1.1/arangodb-spark-datasource-3.1_2.12-1.1.1-jar-with-dependencies.jar"))
      .set("spark.kubernetes.container.image", "spark:v3.1.2")
      .set("spark.kubernetes.context", "minikube")
      .set("spark.kubernetes.namespace", "spark-demo")
//      .set("spark.kubernetes.executor.deleteOnTermination", "false")
      .set("spark.executor.instances", "4")
    )
    .getOrCreate

  val options = Map(
    "database" -> dbName,
    "password" -> password,
    "endpoints" -> endpoints
  )

  def main(args: Array[String]): Unit = {
    val client = ArangoClient(ArangoDBConf(spark.conf.getAll ++ options))
    val col = client.arangoDB.db(dbName).collection("actionMovies")
    if (col.exists()) col.drop()

    ReadDemo.readDemo()
    ReadWriteDemo.readWriteDemo()
    spark.stop
  }

}
