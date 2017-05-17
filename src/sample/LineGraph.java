package sample;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.jnetpcap.util.PcapPacketArrayList;
import sample.Filereading.PcapFile;
import java.io.File;
import java.util.ArrayList;

/*
*
* Alex Lilley, Alex Mori, Ethan Balzer
*
* */

public class LineGraph {

    private static ArrayList<Long> timeDifs;

    public static LineChart LineChart(File file) throws Exception {
        PcapFile myfile = new PcapFile(file.toString());
        PcapPacketArrayList mypackets = myfile.readOfflineFiles();
        timeDifs = new ArrayList<>(FindTimeDiff.findTimeDif(mypackets));
        NumberAxis xAxis = new NumberAxis(0, timeDifs.size(), 10);

        xAxis.setLabel("Packet Num");
        Long maxValue = 0L;
        timeDifs.set(0, 0L);
        maxValue = getMaxValue(maxValue);

        NumberAxis yAxis = new NumberAxis(0, maxValue*1.1, maxValue/25);
        yAxis.setLabel("Time (MicroSeconds)");
        LineChart lineChart = new LineChart(xAxis, yAxis);

        lineChart.getData().add(getSeries(lineChart));
        return lineChart;
    }

    private static XYChart.Series getSeries(LineChart lineChart) {
        int index = 0;
        lineChart.setTitle("Times in microseconds");
        XYChart.Series series = new XYChart.Series();
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);

        for(Long entry : timeDifs){
            index+=1;
            series.getData().add(new XYChart.Data<>(index, entry));
        }
        return series;
    }

    private static long getMaxValue(Long maxValue) {
        for(Long entry: timeDifs){
            if (entry > maxValue){
                maxValue = entry;
            }
        }
        return maxValue;
    }
}
