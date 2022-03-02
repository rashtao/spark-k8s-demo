import org.apache.spark.sql.DataFrame

object WriteDemo {
  val saveOptions: Map[String, String] = Demo.options ++ Map(
    "table.shards" -> "9",
    "confirmTruncate" -> "true",
    "overwriteMode" -> "replace"
  )

  def saveDF(df: DataFrame, tableName: String, tableType: String = "document"): Unit =
    df
      .write
      .mode("overwrite")
      .format("com.arangodb.spark")
      .options(saveOptions ++ Map(
        "table" -> tableName,
        "table.type" -> tableType
      ))
      .save()
}
