package com.joseluisnn.byr;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.joseluisnn.singleton.SingletonBroadcastReceiver;
import com.joseluisnn.singleton.SingletonConfigurationSharedPreferences;

public class PrincipalScreenActivity extends Activity{

	// Variable para recibir una señal de BroadcastReceiver al modificar el tipo de Configuracion
	private BroadcastReceiver myReceiver;
	// Variable que contiene la constante para saber qué Broadcast se ha enviado
	private SingletonBroadcastReceiver sbr;
	
	// Variables para acceder al archivo de configuración
	private SharedPreferences preferenceConfiguracionPrivate;
	private SingletonConfigurationSharedPreferences singleton_csp;
	private int tipoConfig;
	
	// Variables de la interfaz gráfica
	private ImageView b_data;
	private ImageView b_config;
	private ImageView b_inform;
	private ImageView b_graphic;
	//private ImageView b_prevision;
	// Variables para los textView de los botones
	private TextView tvData;
	private TextView tvConfig;
	private TextView tvInform;
	private TextView tvGraphic;
	//private TextView tvPrevision;
	private TextView tvDeveloper;
	
	private long tiempoDePulsacionInicial;	
	
	
	private Animation animacionBotonPulsado,animacionBotonLevantado;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal_screen);
		
		animacionBotonPulsado = AnimationUtils.loadAnimation(this, R.anim.animacion_boton_pulsado);
		animacionBotonLevantado = AnimationUtils.loadAnimation(this, R.anim.animacion_boton_levantado);
		
		// Inicializo las variables de la interfaz
		b_data = (ImageView)findViewById(R.id.imageButtonData);
		b_config = (ImageView)findViewById(R.id.imageButtonConfiguration);
		b_inform = (ImageView)findViewById(R.id.imageButtonInforms);
		b_graphic = (ImageView)findViewById(R.id.imageButtonGraphics);
		//b_prevision = (ImageView)findViewById(R.id.imageButtonPrevision);
		tvData = (TextView)findViewById(R.id.textViewData);
		tvConfig = (TextView)findViewById(R.id.textViewConfiguration);
		tvInform = (TextView)findViewById(R.id.textViewInforms);
		tvGraphic = (TextView)findViewById(R.id.textViewGraphics);
		//tvPrevision = (TextView)findViewById(R.id.textViewPrevision);
		tvDeveloper = (TextView)findViewById(R.id.textViewDeveloper);
		
		// Modifico el tipo de fuente de los TextView
		// Variable para el tipo tipo de fuente
		Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/go_boom.ttf");
		tvData.setTypeface(fuente);
		tvConfig.setTypeface(fuente);
		tvInform.setTypeface(fuente);
		tvGraphic.setTypeface(fuente);
		//tvPrevision.setTypeface(fuente);
		tvDeveloper.setTypeface(fuente);
		
		// Inicio la clase donde están las constantes de BroadcastReceiver
		sbr = new SingletonBroadcastReceiver();
				
		/* Modifico las características al ser pulsado*/
		b_data.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				switch (event.getAction()) {
				
					case MotionEvent.ACTION_DOWN:
						tiempoDePulsacionInicial = event.getEventTime();
						b_data.startAnimation(animacionBotonPulsado);
						break;
					case MotionEvent.ACTION_UP:
						
						if(event.getEventTime()-tiempoDePulsacionInicial<=2000){
							// Lanzo la Actividad DatasActivityScreen 
							b_data.startAnimation(animacionBotonLevantado);
							lanzarGestionDatos();							
						}
						// Si he mantenido el botón pulsado más de dos segundos cancelo la operación
						b_data.startAnimation(animacionBotonLevantado);
						break;
					case MotionEvent.ACTION_CANCEL:
						b_data.startAnimation(animacionBotonLevantado);
						break;
						
				}
				
				return true;
			}
		});
		
		/* Modifico las características al ser pulsado*/
		b_config.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				switch (event.getAction()) {
				
					case MotionEvent.ACTION_DOWN:
						tiempoDePulsacionInicial = event.getEventTime();
						b_config.startAnimation(animacionBotonPulsado);
						break;
					case MotionEvent.ACTION_UP:
						
						if(event.getEventTime()-tiempoDePulsacionInicial<=2000){
							// Lanzo la Actividad ConfigurationActivity 
							b_config.startAnimation(animacionBotonLevantado);
							lanzarConfiguracion();							
						}
						// Si he mantenido el botón pulsado más de dos segundos cancelo la operación
						b_config.startAnimation(animacionBotonLevantado);
						break;
						
				}
				
				return true;
			}
		});
		
		/* Modifico las características al ser pulsado*/
		b_inform.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				switch (event.getAction()) {
				
					case MotionEvent.ACTION_DOWN:
						tiempoDePulsacionInicial = event.getEventTime();
						b_inform.startAnimation(animacionBotonPulsado);
						break;
					case MotionEvent.ACTION_UP:
						
						if(event.getEventTime()-tiempoDePulsacionInicial<=2000){
							// Lanzo la Actividad InformesScreenActivity 
							b_inform.startAnimation(animacionBotonLevantado);
							lanzarInformes();							
						}
						// Si he mantenido el botón pulsado más de dos segundos cancelo la operación
						b_inform.startAnimation(animacionBotonLevantado);
						break;
						
				}
				
				return true;
			}
		});
		
		/* Modifico las características al ser pulsado*/
		b_graphic.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				switch (event.getAction()) {
				
					case MotionEvent.ACTION_DOWN:
						tiempoDePulsacionInicial = event.getEventTime();
						b_graphic.startAnimation(animacionBotonPulsado);
						break;
					case MotionEvent.ACTION_UP:
						
						if(event.getEventTime()-tiempoDePulsacionInicial<=2000){
							// Lanzo la Actividad CreateAccountActivity 
							b_graphic.startAnimation(animacionBotonLevantado);
							//lanzarPantalla GestionDatos();							
						}
						// Si he mantenido el botón pulsado más de dos segundos cancelo la operación
						b_graphic.startAnimation(animacionBotonLevantado);
						break;
						
				}
				
				return true;
			}
		});
		
		/* Modifico las características al ser pulsado*/
		/*b_prevision.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				switch (event.getAction()) {
				
					case MotionEvent.ACTION_DOWN:
						tiempoDePulsacionInicial = event.getEventTime();
						b_prevision.startAnimation(animacionBotonPulsado);
						break;
					case MotionEvent.ACTION_UP:
						
						if(event.getEventTime()-tiempoDePulsacionInicial<=2000){
							// Lanzo la Actividad CreateAccountActivity 
							b_prevision.startAnimation(animacionBotonLevantado);
							//lanzarPantalla GestionDatos();							
						}
						// Si he mantenido el botón pulsado más de dos segundos cancelo la operación
						b_prevision.startAnimation(animacionBotonLevantado);
						break;
						
				}
				
				return true;
			}
		});*/
		
		/*
		 * Según el tipo de configuración habilito los botones adecuados
		 */
		// Obtengo la clase que contiene las constantes para enviar el Broadcast 
		sbr = new SingletonBroadcastReceiver();
		
		// Obtengo los valores que se encuentran en el archivo de configuración
		singleton_csp = new SingletonConfigurationSharedPreferences();
		preferenceConfiguracionPrivate = getSharedPreferences(singleton_csp.NOMBRE_ARCHIVO_CONFIGURACION, Context.MODE_PRIVATE);
		
		// compruebo si ya hay una cuenta creada
		if(preferenceConfiguracionPrivate.contains(singleton_csp.KEY_CUENTA)){
			
			tipoConfig = preferenceConfiguracionPrivate.getInt(singleton_csp.KEY_TIPO_CONFIGURACION, 0);
			
		}else {
			
			/*
			 * -Con el control Editor iniciamos la manipulación de valores; con Editor podemos establecer
			 * tipos string,boolean,float,int y long como pares clave-valor
			 * -Almacenar los datos en Editor crea un elemento Map en memoria.
			 */
			Editor prefEditor = preferenceConfiguracionPrivate.edit();
			
			prefEditor.putBoolean(singleton_csp.KEY_CUENTA, true);
			/*
			 *  El tipo de configuración del usuario será básica hasta que él decida 
			 *  modificarla dentro de la aplicación
			 */
			prefEditor.putInt(singleton_csp.KEY_TIPO_CONFIGURACION,0);
			tipoConfig = 0;
			/*
			 * El informe seleccionado por defecto será el semanal hasta que el usuario lo 
			 * modifique en la configuración
			 * 0: semanal
			 * 1: mensual
			 * 2: trimestral
			 * 3: anual
			 * 4: libre
			 */
			prefEditor.putInt(singleton_csp.KEY_INFORME_POR_DEFECTO, 0);
				
			// Guardo los valores
			prefEditor.commit();
			
		}
		
		/*
		 * @tipoconfig = 0 : configuración básica y deshabilito las gráficas y la previsión
		 * @tipoconfig = 1 : configuración avanzada y habilito las gráficas y la previsión
		 */
		if (tipoConfig == 0){
			//b_prevision.setEnabled(false);
			b_graphic.setEnabled(false);
		}else{			
			//b_prevision.setEnabled(true);
			b_graphic.setEnabled(true);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_principal_screen, menu);
		
		return true;
		
	}
	
	/*
	 * Método que lanza la pantalla de GESTIÓN DE DATOS
	 */
	private void lanzarGestionDatos(){
		
		Intent intent = new Intent(this, DatasActivityScreen.class);
		startActivity(intent);
		
	}
	
	/*
	 * Método que lanza la pantalla de CONFIGURACIÓN
	 */
	private void lanzarConfiguracion(){
		
		Intent intent = new Intent(this, ConfigurationActivity.class);
		
		startActivity(intent);
	}
	
	/*
	 * Método que lanza la pantalla de INFORMES
	 */
	private void lanzarInformes(){
		
		Intent intent = new Intent(this, InformesScreenActivity.class);
		
		startActivity(intent);
	}

	@Override
	protected void onStop() {
		// TODO Registro en onStop() el Broadcast que me indicará cambios en la configuración
		super.onStop();
		
		IntentFilter ifilter = new IntentFilter(sbr.CAMBIO_CONFIGURACION);
		
		myReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// Compruebo si se ha modificado el nombre de usuario o el tipo de configuracion
				
				int cambio = intent.getIntExtra("cambio", 0);
				
				if(cambio == 0){
				
					int tipo_config = intent.getIntExtra("tipo_configuracion", 0);
				
					// Según el tipo de configuración que se le pase por parámetro se ocultan las opciones
					if(tipo_config == 0){// el usuario ha modificado el tipo de configuración
						//b_prevision.setEnabled(false);
						b_graphic.setEnabled(false);
					}else{
						//b_prevision.setEnabled(true);
						b_graphic.setEnabled(true);
					}
					
				}
			}			
		};
		
		// Registro mi BroadcastReceiver
		this.registerReceiver(myReceiver, ifilter);
	}

	@Override
	protected void onRestart() {
		
		/*
		 * Quito el registro de mi BroadcastReceiver cuando mi Actividad pasa de estar oculta 
		 * (o no Activa) por otra Actividad o aplicación a visible (o Activa)
		 */
		this.unregisterReceiver(myReceiver);
		
		super.onRestart();
		
	}

	@Override
	protected void onDestroy() {
		
		/*
		 * Quito el registro de mi BoradcastReceiver cuando mi Actividad pasa de estado Parada a Destruida 
		 */
		this.unregisterReceiver(myReceiver);
		
		super.onDestroy();
		
	}
	
}
