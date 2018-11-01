package com.example.ceo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GraphsActivity extends AppCompatActivity {

    String url = "http://ec2-18-222-89-34.us-east-2.compute.amazonaws.com/graph";
    GraphView happiness = findViewById(R.id.happiness);
    GraphView orders = findViewById(R.id.orders);
    GraphView income = findViewById(R.id.income);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        // Add the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.graphs_activity_label);
        setSupportActionBar(toolbar);

        // Set back button on toolbar
        toolbar.setNavigationOnClickListener(v -> finish());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Graphs settings
        graphSettings(happiness, getResources().getString(R.string.customer_happiness_label));
        graphSettings(orders, getResources().getString(R.string.orders_number_label));
        graphSettings(income, getResources().getString(R.string.income_number));

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        /*scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                HTTPService service = new HTTPService();
                JSONArray array = service.getHTTP(url, getApplicationContext(), "data");
                try {
                    ArrayList<double[][]> list = parseJSON(array);
                    redrawGraphs(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.DAYS);*/

        double x1[] = {2014, 2015, 2016, 2017, 2018};
        double y1[] = {3.4, 3.6, 4.0, 4.1, 4.35};
        happiness.addSeries(addSeries(x1, y1));

        double y2[] = {300456, 356523, 420546, 490523, 545622};
        double x2[] = {2014, 2015, 2016, 2017, 2018};
        orders.addSeries(addSeries(x2, y2));

        double x3[] = {2014, 2015, 2016, 2017, 2018};
        double y3[] = {50000, 27000, 49000, 53400, 55000};
        income.addSeries(addBarSeries(x3, y3));

    }

    public ArrayList<double[][]> parseJSON(JSONArray array) throws JSONException {
        ArrayList<double[][]> list = new ArrayList<>();
        double[][] data;
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            JSONArray x = obj.getJSONArray("x");
            JSONArray y = obj.getJSONArray("y");
            data = new double[2][y.length()];
            for (int j = 0; j < x.length(); j++){
                data[0][j] = (Double) x.get(j);
            }
            for (int j = 0; j < y.length(); j++){
                data[1][j] = (Double) y.get(j);
            }
            list.add(data);
        }
        return list;
    }

    public void redrawGraphs(ArrayList<double[][]> list){
        happiness.removeAllSeries();
        orders.removeAllSeries();
        income.removeAllSeries();
        for(int i = 0; i < 3; i++){
            double[][] data = list.get(i);
            double[] x = data[0];
            double[] y = data[1];
            if(i == 0){
                happiness.addSeries(addSeries(x, y));
            }
            else if(i == 1){
                orders.addSeries(addSeries(x, y));
            }
            else {
                income.addSeries(addSeries(x, y));
            }
        }
    }

    public LineGraphSeries<DataPoint> addSeries(double x[], double y[]) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < x.length; i++) {
            DataPoint point = new DataPoint(x[i], y[i]);
            series.appendData(point, true, 100, false);

        }

        return series;
    }

    public BarGraphSeries<DataPoint> addBarSeries(double x[], double y[]) {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
        for (int i = 0; i < x.length; i++) {
            DataPoint point = new DataPoint(x[i], y[i]);
            series.appendData(point, true, 100, false);
            series.setSpacing(3);
        }
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        return series;
    }

    public void graphSettings(GraphView graph, String title){
        graph.getGridLabelRenderer().setGridColor(getColor(R.color.black));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(getColor(R.color.black));
        graph.getGridLabelRenderer().setVerticalLabelsColor(getColor(R.color.black));
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(getColor(R.color.black));
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getColor(R.color.black));
        graph.setTitleColor(getColor(R.color.black));

        graph.setTitle(title);
        graph.setTitleTextSize(52);

    }

}
