package org.pipeman.pipo.tps;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;

public class Lag {

    public static double getTPS() {
        Spark spark = SparkProvider.get();

        DoubleStatistic<StatisticWindow.TicksPerSecond> tps = spark.tps();
        return tps.poll(StatisticWindow.TicksPerSecond.MINUTES_5);
    }
}