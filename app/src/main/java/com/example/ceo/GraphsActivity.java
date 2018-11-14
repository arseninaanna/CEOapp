package com.example.ceo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.ceo.requests.BackendAPI;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
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
import java.util.concurrent.TimeUnit;

public class GraphsActivity extends AppCompatActivity {

    GraphView happiness;
    GraphView income;
    GraphView orders;
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        happiness = findViewById(R.id.happiness);
        income = findViewById(R.id.income);
        orders = findViewById(R.id.orders);

        // Add the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.graphs_activity_label);
        setSupportActionBar(toolbar);

        // Set back button on toolbar
        toolbar.setNavigationOnClickListener(v -> finish());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Graphs settings
        graphSettings(happiness, getResources().getString(R.string.customer_happiness_label), "Happiness", "Last 7 days");
        graphSettings(income, getResources().getString(R.string.income_number), "Income", "Last 7 days");
        graphSettings(orders, getResources().getString(R.string.orders_number_label), "Orders", "Last 7 days");

        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                makeHTTPGet(response -> {
                    JSONArray array = response;
                    System.out.println("Graph: " + array.toString());
                    try {
                        ArrayList<double[][]> list = parseJSON(array);
                        redrawGraphs(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 0, 1, TimeUnit.DAYS);

    }

    public void makeHTTPGet(Response.Listener<JSONArray> respCb){
        BackendAPI api = new BackendAPI(this);
        api.get("/graph", respCb, error -> {
            Toast.makeText(getBaseContext(), "Problems with server, sorry cannot render",
                    Toast.LENGTH_LONG).show();
            scheduler.shutdown();
            finish();
        });
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
                data[0][j] = Double.parseDouble(x.get(j).toString());
            }
            for (int j = 0; j < y.length(); j++){
                data[1][j] = Double.parseDouble(y.get(j).toString());
            }
            list.add(data);
        }
        return list;
    }

    public void redrawGraphs(ArrayList<double[][]> list){
        happiness.removeAllSeries();
        income.removeAllSeries();
        orders.removeAllSeries();
        for(int i = 0; i < 3; i++){
            double[][] data = list.get(i);
            double[] x = data[0];
            double[] y = data[1];
            if(i == 0){
                happiness.addSeries(addSeries(x, y));
            }
            else if(i == 1){
                income.addSeries(addBarSeries(x, y));
            }
            else {
                orders.addSeries(addBarSeries(x, y));
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

    public void graphSettings(GraphView graph, String title, String y_axis, String x_axis){
        graph.getGridLabelRenderer().setGridColor(getColor(R.color.black));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(getColor(R.color.black));
        graph.getGridLabelRenderer().setVerticalLabelsColor(getColor(R.color.black));
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(getColor(R.color.black));
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getColor(R.color.black));
        graph.setTitleColor(getColor(R.color.black));

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(x_axis);
        gridLabel.setVerticalAxisTitle(y_axis);

        graph.setTitle(title);
        graph.setTitleTextSize(52);

    }

}
