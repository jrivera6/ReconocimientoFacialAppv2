package pe.jrivera6.reconocimientofacialapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import pe.jrivera6.reconocimientofacialapp.R;
import pe.jrivera6.reconocimientofacialapp.models.Datos;
import pe.jrivera6.reconocimientofacialapp.services.ApiService;
import pe.jrivera6.reconocimientofacialapp.services.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraficaActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private BarChart barChart;
    private static final String TAG = GraficaActivity.class.getSimpleName();
    private String[] months = new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo"};
    //Eje Y
    private int[] sale = new int[]{25, 20, 38, 10, 15};
    private int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.BLACK, Color.DKGRAY};
    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        barChart = (BarChart) findViewById(R.id.chart);

        initialize();

    }

    @Override
    public void onBackPressed() {
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            super.onBackPressed();
            Intent intent = new Intent(GraficaActivity.this, MainActivity.class);
            startActivity(intent);
            return;
        }else {
            Toasty.info(this,"Presionar dos veces para salir",Toast.LENGTH_SHORT,true).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }

    private Chart getSameChart(Chart chart, String description, int textColor, int background, int animateY, boolean leyenda) {
        chart.getDescription().setText(description);
        chart.getDescription().setTextColor(textColor);
        chart.getDescription().setTextSize(8);
        chart.setBackgroundColor(background);
        chart.animateY(animateY);

        //Validar porque la grafica de radar y dispersion tiene dos datos especificos y la leyenda se crea de acuerdo a esos datos.
        legend(chart);
        return chart;
    }

    private void legend(Chart chart) {
/*        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry> entries = new ArrayList<>();
        for (int i = 0; i < months.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = months[i];
            entries.add(entry);
        }
        legend.setCustom(entries);*/
    }

    private ArrayList<BarEntry> getBarEntries() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < sale.length; i++)
            entries.add(new BarEntry(i, sale[i]));
        return entries;
    }


    //Eje horizontal o eje X
    private void axisX(XAxis axis) {
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setEnabled(true);
    }

    //Eje Vertical o eje Y lado izquierdo
    private void axisLeft(YAxis axis) {
        axis.setSpaceTop(30);
        axis.setAxisMinimum(0);
        axis.setGranularity(10);
    }

    //Eje Vertical o eje Y lado Derecho
    private void axisRight(YAxis axis) {
        axis.setEnabled(false);
    }

    //Crear graficas
    public void createCharts() {
        //BarChart
        barChart = (BarChart) getSameChart(barChart, "Series", Color.RED, Color.WHITE, 3000, true);
        barChart.setDrawGridBackground(false);
        barChart.setData(getBarData());
        barChart.invalidate();
        barChart.getLegend().setEnabled(true);
        axisLeft(barChart.getAxisLeft());
        axisRight(barChart.getAxisRight());

    }

    //Carasteristicas comunes en dataset
    private DataSet getDataSame(DataSet dataSet) {
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10);
        return dataSet;
    }

    private BarData getBarData() {
        BarDataSet barDataSet = (BarDataSet) getDataSame(new BarDataSet(getBarEntries(), ""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.45f);
        return barData;
    }


    private  void initialize() {

        Long id = sharedPreferences.getLong("captura_id",0);

        if (id == 0){
            Toasty.error(this,"No hay graficos por ahora",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(GraficaActivity.this,MainActivity.class);
            startActivity(intent);
            return;
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);


        Call<List<Datos>> call = service.getDatos(id);

        call.enqueue(new Callback<List<Datos>>() {
            @Override
            public void onResponse(Call<List<Datos>> call, Response<List<Datos>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<Datos> datos = response.body();

                        Log.d(TAG, "datos: " + datos);
                        int i;

                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<LegendEntry> entries2 = new ArrayList<>();


                        for ( i=0; i < datos.size(); i++) {
                            Datos data = datos.get(i);

                            Integer y = data.getCantidad_rostros();
                            entries.add(new BarEntry(i, y));
                            String x = data.getEstado_rostro();

                            LegendEntry entry = new LegendEntry();
                            entry.formColor = colors[i];
                            entry.label = x;
                            entries2.add(entry);

                        }
                        BarDataSet barDataSet = (BarDataSet) getDataSame(new BarDataSet(entries, ""));
                        barDataSet.setBarShadowColor(Color.GRAY);
                        BarData barData = new BarData(barDataSet);
                        barData.setBarWidth(0.45f);


                        Legend legend = barChart.getLegend();
                        legend.setForm(Legend.LegendForm.LINE);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                        legend.setCustom(entries2);
                        YAxis yAxis = new YAxis();
                        yAxis.setSpaceTop(30);
                        yAxis.setAxisMinimum(0);
                        yAxis.setGranularity(10);
                        axisLeft(yAxis);

                        axisRight(barChart.getAxisRight());
                        barChart = (BarChart) getSameChart(barChart, "Series", Color.RED, Color.WHITE, 2000, true);

                        barChart.setDrawGridBackground(false);
                        barChart.setData(barData);
                        barChart.getLegend().setEnabled(true);
                        barChart.invalidate();




                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(GraficaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Datos>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(GraficaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }
}
