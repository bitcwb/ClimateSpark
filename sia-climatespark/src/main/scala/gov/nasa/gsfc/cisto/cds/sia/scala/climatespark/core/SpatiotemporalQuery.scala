package gov.nasa.gsfc.cisto.cds.sia.scala.climatespark.core

import gov.nasa.gsfc.cisto.cds.sia.core.config.HadoopConfiguration
import gov.nasa.gsfc.cisto.cds.sia.scala.climatespark.core.io.datastructure.Cell4D
import gov.nasa.gsfc.cisto.cds.sia.scala.climatespark.core.io.datastructure.CellFactory._
import gov.nasa.gsfc.cisto.cds.sia.scala.climatespark.functions.ClimateRDDFunction._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext

/**
  * Created by Fei Hu on 12/22/16.
  */
object SpatiotemporalQuery {

  def main(args: Array[String]) {
    ///var/lib/hadoop-hdfs/0326/properties/sia_analytics.properties
    val input = Array[String]("/Users/feihu/Documents/IDEAProjects/ClimateSpark/properties/sia_analytics.properties")
    // val input = Array[String]("/home/u17/indexing/ClimateSpark/properties/sia_analytics.properties")
    val hadoopConf = new HadoopConfiguration(input).getHadoopConf
    //val sc = new SparkContext()

    //val sc = new ClimateSparkContext(configFile, "local[6]", "test")
    val climateSparkContext = new ClimateSparkContext(hadoopConf, "local[6]", "test")
    //val climateSparkContext = new ClimateSparkContext(sc, hadoopConf)
    val sqlContext = new SQLContext(climateSparkContext.getSparkContext)
    import sqlContext.implicits._

    val climateRDD = climateSparkContext.getClimateRDD

    val monthlyAvg = climateRDD.monthlyAvg(1)

    val df = monthlyAvg.toDF("VarName", "Time", "Avg")
    df.show()

    val monthlyMeanArray = monthlyAvg.map(tuple => tuple._3).collect()

    println("Monthly Avg: ", monthlyMeanArray.sum/monthlyMeanArray.size)


    val average = climateRDD.average

    average.collect().foreach(println)

    //   KE|198001|8.21638184207709E34|


    //val cellRDD:RDD[Cell4D] = climateRDD.getCells.map(cell => cell.asInstanceOf[Cell4D])
    //val pointRDD = cellRDD.map(cell => (cell.d0, cell.d1, -90.0 + 0.5*cell.d2, -180.0 + 0.625*cell.d3, cell.value))

    // val df = sqlContext.createDataFrame(cellRDD)
    // val df = pointRDD.toDF("date", "hour", "lat", "lon", "value")
    // df.cache()
    // df.registerTempTable("merra")

    // sqlContext.sql("SELECT d0 AS D0, d1 AS D1, d2 AS D2, d3 AS D3, value AS Value FROM merra").show(200)
    // sqlContext.sql("SELECT d0 as Time, avg(value) as Mean FROM merra GROUP BY d0").show()
    // sqlContext.sql("SELECT min(value) FROM merra").show()
  }
}
