package com.example.t1bicing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private MapView mapa;
    private MapController mapController;
    private Context contexto;
    private double latUser;
    private double lonUser;
    private LocationManager locationManager;
    private FloatingActionButton buttonViewList;
    private FloatingActionButton buttonUbi;

    private BasicInfoWindow infoWindow;

    private boolean estatStar = false;
    private List<Estacion> estaciones;
    private SharedPreferences sharedPreferences;
    private String filtrarEstaciones = "todas";
    private EditText distancia;

    private Button aceptar;
    private Button cancelar;

    private Button buttonmasInfo;

    private int maximoEnMetros;

    private List<Estacion> estacionesFiltradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        this.buttonViewList = this.findViewById(R.id.floatingActionButtonViewList);
        this.buttonUbi = this.findViewById(R.id.floatingActionButtonUbi);
        this.mapa = this.findViewById(R.id.mapa);
        this.contexto = this.getApplicationContext();
        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        this.mapController = (MapController) this.mapa.getController();
        this.distancia = this.findViewById(R.id.editTextDistancia);
        this.aceptar = this.findViewById(R.id.buttonAceptar);
        this.cancelar = this.findViewById(R.id.buttonCancelar);


        distancia.setVisibility(View.INVISIBLE);
        aceptar.setVisibility(View.INVISIBLE);
        cancelar.setVisibility(View.INVISIBLE);

        checkPermis();
        Configuration.getInstance().load(contexto, PreferenceManager.getDefaultSharedPreferences(contexto));

        // Aplicar zoom al mapa
        this.mapController.setZoom(15);
        // Coordenadas geográficas del centro de Barcelona
        double latitudCentroB = 41.3888;
        double longitudCentroB = 2.1590;
        this.mapController.setCenter(new GeoPoint(latitudCentroB, longitudCentroB));


        buttonUbi.setOnClickListener(view -> {
            Marker markerUser = new Marker(this.mapa);
            GeoPoint point = new GeoPoint(latUser, lonUser);
            this.mapController.setZoom(20);
            this.mapController.setCenter(point);
            markerUser.setPosition(point);
            markerUser.setTitle("Tu ubicación");
            //añadir marcador al mapa
            this.mapa.getOverlays().add(markerUser);
        });

        aceptar.setOnClickListener(view -> {
            maximoEnMetros = Integer.parseInt(distancia.getText().toString());
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            distancia.setVisibility(View.INVISIBLE);
            aceptar.setVisibility(View.INVISIBLE);
            cancelar.setVisibility(View.INVISIBLE);
            mapa.setAlpha(1f);
            setFiltrarEstaciones("distancia");
            cargarDatosEstaciones(filtrarEstaciones);
        });

        cancelar.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            distancia.setVisibility(View.INVISIBLE);
            aceptar.setVisibility(View.INVISIBLE);
            cancelar.setVisibility(View.INVISIBLE);
            mapa.setAlpha(1f);
        });

        cargarDatosEstaciones(filtrarEstaciones);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.iniciaLoacalitzacio();
        } else {
            Toast.makeText(this, "L'aplicació no pot funcionar sense aquest permís.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latUser = location.getLatitude();
        lonUser = location.getLongitude();
    }

    public void checkPermis() {
        if (ContextCompat.checkSelfPermission(contexto,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // codi si tenim permís
            this.iniciaLoacalitzacio();
            // Obtener la última ubicación conocida
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                latUser = lastLocation.getLatitude();
                lonUser = lastLocation.getLongitude();
            }
        } else {
            // en cas de no tenir, el demanem
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void iniciaLoacalitzacio() {
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000l, 0f, this);
    }

//    public void pararLocalizacion(View view){
//        this.locationManager.removeUpdates(this);
//    }

    public void ajustarEstacionesMasCercanas() {
        double distanciaMinima = Double.MAX_VALUE;
        for (Estacion e1 : estaciones) {
            for (Estacion e2 : estaciones) {
                if (e1 == e2) {
                    continue;
                }
                double distancia = distanciaEntreEstaciones(e1, e2);
                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    e1.setEstacionMasCercana(e2.getNombre());
                }
            }
            distanciaMinima = Double.MAX_VALUE;
        }
    }

    private double distanciaEntreEstaciones(Estacion e1, Estacion e2) {
        final int RADIO_TIERRA = 6371; // en kilómetros
        double latitud1 = Math.toRadians(e1.getLatitud());
        double latitud2 = Math.toRadians(e2.getLatitud());
        double diferenciaLatitud = Math.toRadians(e2.getLatitud() - e1.getLatitud());
        double diferenciaLongitud = Math.toRadians(e2.getLongitud() - e1.getLongitud());
        double a = Math.sin(diferenciaLatitud / 2) * Math.sin(diferenciaLatitud / 2) +
                Math.cos(latitud1) * Math.cos(latitud2) *
                        Math.sin(diferenciaLongitud / 2) * Math.sin(diferenciaLongitud / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distancia = RADIO_TIERRA * c;
        return distancia;
    }

    public void cargarDatosEstaciones(String filtro) {
        estaciones = new ArrayList<>();
        String url = "https://api.bsmsa.eu/ext/api/bsm/gbfs/v2/en/station_information";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final Map<Integer, EstadoEstacion> estadosEstacionesMap = new HashMap<>();
                        try {
                            JSONArray estacionesArray = response.getJSONObject("data").getJSONArray("stations");
                            for (int i = 0; i < estacionesArray.length(); i++) {
                                JSONObject estacionJson = estacionesArray.getJSONObject(i);
                                Estacion estacion = new Estacion();
                                estacion.setId(estacionJson.getInt("station_id"));
                                estacion.setNombre(estacionJson.getString("name"));
                                estacion.setConfiguracionFisica(estacionJson.getString("physical_configuration"));
                                estacion.setLatitud(estacionJson.getDouble("lat"));
                                estacion.setLongitud(estacionJson.getDouble("lon"));
                                estacion.setAltitud(estacionJson.getDouble("altitude"));
                                estacion.setDireccion(estacionJson.getString("address"));
                                estacion.setCodigoPostal(estacionJson.getString("post_code"));
                                estacion.setCapacidad(estacionJson.getInt("capacity"));
                                estacion.setEsEstacionCarga(estacionJson.getBoolean("is_charging_station"));
                                estacion.setDistanciaCercana(estacionJson.getDouble("nearby_distance"));
                                estacion.setEsCompatibleCodigoRide(estacionJson.getBoolean("_ride_code_support"));
                                estaciones.add(estacion);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                        ajustarEstacionesMasCercanas();
                        cargarDatosEstadoEstacion(new EstadoEstacionCallback() {
                            @Override
                            public void onEstadosEstacionesObtenidas(List<EstadoEstacion> estadosEstaciones) {
                                // Crear un mapa que asigne el id de la estación a su correspondiente EstadoEstacion
                                for (EstadoEstacion estadoEstacion : estadosEstaciones) {
                                    estadosEstacionesMap.put(estadoEstacion.getId(), estadoEstacion);
                                }

                                // Asignar el EstadoEstacion correspondiente a cada Estacion
                                for (Estacion estacion : estaciones) {
                                    EstadoEstacion estadoEstacion = estadosEstacionesMap.get(estacion.getId());
                                    if (estadoEstacion != null) {
                                        estacion.setEstadoEstacion(estadoEstacion);
                                    }
                                }
                                creaMarcadoresPersonalizados(filtro);
                            }
                        });

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(contexto, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueueSingleton.getInstance(contexto).getRequestQueue().add(jsonObjectRequest);
    }

    public void cargarDatosEstadoEstacion(EstadoEstacionCallback callback) {
        String url = "https://api.bsmsa.eu/ext/api/bsm/gbfs/v2/en/station_status";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<EstadoEstacion> estadosEstaciones = new ArrayList<>();
                        try {
                            JSONArray estacionesArray = response.getJSONObject("data").getJSONArray("stations");
                            for (int i = 0; i < estacionesArray.length(); i++) {
                                JSONObject estadoEstacionJson = estacionesArray.getJSONObject(i);
                                EstadoEstacion estadoEstacion = new EstadoEstacion();
                                estadoEstacion.setId(estadoEstacionJson.getInt("station_id"));
                                estadoEstacion.setNumeroBicicletas(estadoEstacionJson.getInt("num_bikes_available"));
                                estadoEstacion.setNumeroAnclajes(estadoEstacionJson.getInt("num_docks_available"));
                                estadoEstacion.setStatus(estadoEstacionJson.getString("status"));
                                estadosEstaciones.add(estadoEstacion);

                                JSONObject disponibilidadBicicletasJson = estadoEstacionJson.getJSONObject("num_bikes_available_types");
                                int numBicicletasMecanicas = disponibilidadBicicletasJson.getInt("mechanical");
                                int numBicicletasElectricas = disponibilidadBicicletasJson.getInt("ebike");
                                DisponibilidadDeBicicletas disponibilidad = new DisponibilidadDeBicicletas(numBicicletasMecanicas, numBicicletasElectricas);
                                estadoEstacion.setDisponibilidadDeBicicletas(disponibilidad);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                        callback.onEstadosEstacionesObtenidas(estadosEstaciones);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        RequestQueueSingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);

    }

    private void creaMarcadoresPersonalizados(String criterioSeleccionado) {
        this.mapa.getOverlays().clear();
        cargaEstacionesFavoritas();
        estacionesFiltradas = filtrarEstaciones(criterioSeleccionado);
        agregarMarcadores(estacionesFiltradas);
        buttonViewList.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LlistaActivity.class);
            //ArrayList<Estacion> estacionesList = new ArrayList<>(getEstacionesFiltradas());
            intent.putExtra("estaciones", (Serializable) getEstacionesFiltradas());
            startActivity(intent);
        });

    }

    private List<Estacion> filtrarEstaciones(String criterioSeleccionado) {
        if (criterioSeleccionado.equals("favoritas")) {
            return estacionesFiltradasFavoritas();
        } else if (criterioSeleccionado.equals("abiertas")) {
            return estacionesFiltradasAbiertas();
        } else if (criterioSeleccionado.equals("cerradas")) {
            return estacionesFiltradasCerradas();
        } else if (criterioSeleccionado.equals("distancia")) {
            return estacionesFiltradasPorDistancia();
        } else {
            return estaciones;
        }
    }

    private void agregarMarcadores(List<Estacion> estaciones) {
        for (Estacion estacion : estaciones) {
            Marker marker = crearMarcador(estacion);
            BasicInfoWindow infoWindow = crearInfoWindow(estacion, marker);
            marker.setInfoWindow(infoWindow);
            mapa.getOverlays().add(marker);
        }
    }

    private Marker crearMarcador(Estacion estacion) {
        Marker marker = new Marker(mapa);
        GeoPoint point = new GeoPoint(estacion.getLatitud(), estacion.getLongitud());
        marker.setPosition(point);
        Drawable nuevoIcono = obtenerIconoEstacion(estacion);
        marker.setIcon(nuevoIcono);
        return marker;
    }

    private BasicInfoWindow crearInfoWindow(Estacion estacion, Marker marker) {
        BasicInfoWindow infoWindow = new BasicInfoWindow(R.layout.info_win, mapa) {
            @Override
            public void onOpen(Object item) {
                TextView nombreEstacion = mView.findViewById(R.id.textViewTitulo);
                TextView info = mView.findViewById(R.id.textViewContenido);
                ImageView star = mView.findViewById(R.id.imageViewStarNegra);
                Button buttonMasInfo = mView.findViewById(R.id.buttonMasInfo);
                nombreEstacion.setText(estacion.getNombre());
                info.setText(estacion.getInfo());

                star.setOnClickListener(view -> {
                    boolean esFavorito = estacion.isFavorito();
                    estacion.setFavorito(!esFavorito);
                    Drawable iconoEstacion = obtenerIconoEstacion(estacion);
                    marker.setIcon(iconoEstacion);
                    int iconoEstrella = esFavorito ? R.drawable.baseline_star_outline_24 : R.drawable.baseline_star_rate_24;
                    star.setImageDrawable(getResources().getDrawable(iconoEstrella));
                    sharedPreferences.edit().putBoolean(estacion.getNombre(), !esFavorito).apply();
                });
                int iconoEstrella = estacion.isFavorito() ? R.drawable.baseline_star_rate_24 : R.drawable.baseline_star_outline_24;
                star.setImageDrawable(getResources().getDrawable(iconoEstrella));

                buttonMasInfo.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, MasInfoActivity.class);
                    intent.putExtra("estacion", estacion);
                    startActivity(intent);
                });
            }
        };
        return infoWindow;
    }

    private Drawable obtenerIconoEstacion(Estacion estacion) {
        int drawableId;
        if (estacion.isFavorito()) {
            if (estacion.getEstadoEstacion().getStatus().equals("IN_SERVICE")) {
                drawableId = R.drawable.baseline_location_on_24_amarillo;
            } else {
                drawableId = R.drawable.baseline_location_on_24_negro;
            }
        } else {
            if (estacion.getEstadoEstacion().getStatus().equals("IN_SERVICE")) {
                drawableId = R.drawable.baseline_location_on_24_rojo;
            } else {
                drawableId = R.drawable.baseline_location_on_24_negro;
            }
        }
        return getResources().getDrawable(drawableId);
    }


    private void cargaEstacionesFavoritas() {
        for (Estacion estacion : estaciones) {
            boolean esFavorito = sharedPreferences.getBoolean(estacion.getNombre(), false);
            estacion.setFavorito(esFavorito);
        }
    }


    private List<Estacion> estacionesFiltradasFavoritas() {
        List<Estacion> estacionesFavoritas = new ArrayList<>();
        for (Estacion estacion : estaciones) {
            if (estacion.isFavorito()) {
                estacionesFavoritas.add(estacion);
            }
        }
        return estacionesFavoritas;
    }

    private List<Estacion> estacionesFiltradasAbiertas() {
        List<Estacion> estacionesAbiertas = new ArrayList<>();
        for (Estacion estacion : estaciones) {
            if (estacion.getEstadoEstacion().getStatus().equals("IN_SERVICE")) {
                estacionesAbiertas.add(estacion);
            }
        }
        return estacionesAbiertas;
    }

    private List<Estacion> estacionesFiltradasCerradas() {
        List<Estacion> estacionesCerradas = new ArrayList<>();
        for (Estacion estacion : estaciones) {
            if (!estacion.getEstadoEstacion().getStatus().equals("IN_SERVICE")) {
                estacionesCerradas.add(estacion);
            }
        }
        return estacionesCerradas;
    }

    private List<Estacion> estacionesFiltradasPorDistancia() {
        final double MAX_DISTANCIA = maximoEnMetros / 1000.0; // convertir de metros a kilómetros
        List<Estacion> estacionesFiltradas = new ArrayList<>();

        for (Estacion estacion : estaciones) {
            double distanciaEnKm = distanciaEnKilometros(latUser, lonUser, estacion.getLatitud(), estacion.getLongitud());
            if (distanciaEnKm <= MAX_DISTANCIA) {
                estacionesFiltradas.add(estacion);
            }
        }

        return estacionesFiltradas;
    }

    private double distanciaEnKilometros(double lat1, double lon1, double lat2, double lon2) {
        double radioTierra = 6371; // en kilómetros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanciaEnKm = radioTierra * c;

        return distanciaEnKm;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemActualizar:
                cargarDatosEstaciones(filtrarEstaciones);
                break;
            case R.id.itemEstacionesAbiertas:
                setFiltrarEstaciones("abiertas");
                cargarDatosEstaciones(filtrarEstaciones);
                break;
            case R.id.itemEstacionesFavoritas:
                setFiltrarEstaciones("favoritas");
                cargarDatosEstaciones(filtrarEstaciones);
                break;
            case R.id.itemEstacionesCerradas:
                setFiltrarEstaciones("cerradas");
                cargarDatosEstaciones(filtrarEstaciones);
                break;
            case R.id.itemFiltrarPorDistancia:
                distancia.setVisibility(View.VISIBLE);
                aceptar.setVisibility(View.VISIBLE);
                cancelar.setVisibility(View.VISIBLE);
                mapa.setAlpha(0.2f);
                distancia.setInputType(InputType.TYPE_CLASS_NUMBER);
                distancia.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(distancia, InputMethodManager.SHOW_IMPLICIT); // Muestra el teclado con el EditText enfocado
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setFiltrarEstaciones(String filtrarEstaciones) {
        this.filtrarEstaciones = filtrarEstaciones;
    }

    public List<Estacion> getEstacionesFiltradas() {
        return estacionesFiltradas;
    }
}