package com.joseluisnn.tt;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.joseluisnn.databases.DBAdapter;
import com.joseluisnn.objetos.ValoresElementoListaGD;

public class PestanyaMensualActivity extends Activity {

	// Objetos View
	private Spinner meses;
	private TextView tvTotalIngresos;
	private TextView tvTotalGastos;
	private TextView tvTotalBalance;
	private LinearLayout llListadoIngresos;
	private LinearLayout llListadoGastos;
	private ImageView ivFlechaListadoIngresos;
	private ImageView ivFlechaListadoGastos;
	// Variables que me despliegan/pliegan los Listados de Ingresos y Gastos
	private RelativeLayout rlBarraListadoIngresos;
	private RelativeLayout rlBarraListadoGastos;
	// Adaptador para el spinner los meses
	private ArrayAdapter<String> adapterMeses;
	// Lista para el spinner de los meses
	private ArrayList<String> listaMeses;

	// Variable para la BASE DE DATOS
	private DBAdapter dba;

	// Listas para los Ingresos y los Gastos
	ArrayList<ValoresElementoListaGD> listadoValoresIngresos;
	ArrayList<ValoresElementoListaGD> listadoValoresGastos;

	// Variables para tener el total de ingresos y gastos
	private double totalIngresos;
	private double totalGastos;
	
	// Variable para el formato de los números DOUBLE
	private DecimalFormatSymbols separadores;
	private DecimalFormat numeroAFormatear;

	/*
	 * Variable para captar cuando es la primera ejecución del programa y
	 * controlar los cambios en el Spinner de las semanas
	 */
	private int posicionSpinnerAnterior;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pestanya_mensual);

		/*
		 * Instancio los objetos View
		 */
		meses = (Spinner) findViewById(R.id.spinnerPestanyaIMensual);
		tvTotalIngresos = (TextView) findViewById(R.id.textViewTotalIngresosMensual);
		tvTotalGastos = (TextView) findViewById(R.id.textViewTotalGastosMensual);
		tvTotalBalance = (TextView) findViewById(R.id.textViewTotalBalanceMensual);
		llListadoIngresos = (LinearLayout) findViewById(R.id.linearLayoutListadoIngresosMensual);
		llListadoGastos = (LinearLayout) findViewById(R.id.linearLayoutListadoGastosMensual);
		rlBarraListadoIngresos = (RelativeLayout) findViewById(R.id.relativeLayoutBarraListadoIngresosMensual);
		rlBarraListadoGastos = (RelativeLayout) findViewById(R.id.relativeLayoutBarraListadoGastosMensual);
		ivFlechaListadoIngresos = (ImageView) findViewById(R.id.imageViewListadoIngresosMensual);
		ivFlechaListadoGastos = (ImageView) findViewById(R.id.imageViewListadoGastosMensual);

		// Inicializo los controles del Sipnner de los meses
		posicionSpinnerAnterior = 0;

		// Instancio y creo los meses en la lista
		listaMeses = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		listaMeses.add(obtenerMes(c.get(Calendar.MONTH)) + " de " + c.get(Calendar.YEAR));
		c.add(Calendar.MONTH, 1);
		listaMeses.add(obtenerMes(c.get(Calendar.MONTH)) + " de " + c.get(Calendar.YEAR));
		// Instancio y creo el Adaptador para el spinner
		adapterMeses = new ArrayAdapter<String>(this,
				R.layout.own_spinner, listaMeses);
		adapterMeses
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// le añado el adapter al spinner
		meses.setAdapter(adapterMeses);
		
		// Instancio los formateadores de números
		separadores = new DecimalFormatSymbols();
		separadores.setDecimalSeparator(',');
		separadores.setGroupingSeparator('.');
		numeroAFormatear = new DecimalFormat("###,###.##", separadores);

		// Instancio la Base de Datos
		dba = DBAdapter.getInstance(this);

		// Abro la Base de Datos solo en modo lectura
		dba.openREADWRITE();

		/*
		 * accedo a la BD para que me devuelva los ingresos y gastos en sus
		 * respectivas Listas
		 */
		listadoValoresIngresos = dba.listadoValoresIngresosPorFecha(
				obtenerInicioEnteroFechaMesAnterior(),
				obtenerFinEnteroFechaMesAnterior());
		listadoValoresGastos = dba.listadoValoresGastosPorFecha(
				obtenerInicioEnteroFechaMesAnterior(),
				obtenerFinEnteroFechaMesAnterior());

		cargarLayoutListadoIngresos();
		cargarLayoutListadoGastos();

		/*
		 * Calculo los totales y la diferencia y los actualizo en sus variables
		 */
		setTotalIngresos(calcularTotalIngresos());
		setTotalGastos(calcularTotalGastos());

		/*
		 * Actualizo los TextView que contienen los totales
		 */
		actualizarTotales();

		// Pongo los listados plegados
		llListadoIngresos.setVisibility(View.GONE);
		llListadoGastos.setVisibility(View.GONE);

		/*
		 * Método onClick para plegar/desplegar el listado de Ingresos
		 */
		rlBarraListadoIngresos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Veo cual es el estado del listado de ingresos y luego lo
				// abro o lo cierro
				if (llListadoIngresos.getVisibility() == View.VISIBLE) {
					llListadoIngresos.setVisibility(View.GONE);
					ivFlechaListadoIngresos.setImageDrawable(getResources()
							.getDrawable(android.R.drawable.arrow_down_float));
				} else {
					llListadoIngresos.setVisibility(View.VISIBLE);
					ivFlechaListadoIngresos.setImageDrawable(getResources()
							.getDrawable(android.R.drawable.arrow_up_float));
				}

			}
		});

		/*
		 * Método onClick para plegar/desplegar el listado de Gastos
		 */
		rlBarraListadoGastos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Veo cual es el estado del listado de ingresos y luego lo
				// abro o lo cierro
				if (llListadoGastos.getVisibility() == View.VISIBLE) {
					llListadoGastos.setVisibility(View.GONE);
					ivFlechaListadoGastos.setImageDrawable(getResources()
							.getDrawable(android.R.drawable.arrow_down_float));
				} else {
					llListadoGastos.setVisibility(View.VISIBLE);
					ivFlechaListadoGastos.setImageDrawable(getResources()
							.getDrawable(android.R.drawable.arrow_up_float));
				}
			}
		});

		meses.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				if (posicionSpinnerAnterior != position) {

					if (position == 0) {
						// entra por aquí si se quiere ver el listado de la
						// semana anterior
						// lanzarMensaje("semana anterior");
						posicionSpinnerAnterior = position;

						/*
						 * accedo a la BD para que me devuelva los ingresos y
						 * gastos en sus respectivas Listas
						 */

						listadoValoresIngresos = dba
								.listadoValoresIngresosPorFecha(
										obtenerInicioEnteroFechaMesAnterior(),
										obtenerFinEnteroFechaMesAnterior());
						listadoValoresGastos = dba
								.listadoValoresGastosPorFecha(
										obtenerInicioEnteroFechaMesAnterior(),
										obtenerFinEnteroFechaMesAnterior());

						cargarLayoutListadoIngresos();
						cargarLayoutListadoGastos();

						/*
						 * Calculo los totales y la diferencia y los actualizo
						 * en sus variables
						 */
						setTotalIngresos(calcularTotalIngresos());
						setTotalGastos(calcularTotalGastos());

						/*
						 * Actualizo los TextView que contienen los totales
						 */
						actualizarTotales();

					} else {
						// entra por aquí si se quiere ver el listado de la
						// semana actual
						// lanzarMensaje("semana actual");
						posicionSpinnerAnterior = position;

						/*
						 * accedo a la BD para que me devuelva los ingresos y
						 * gastos en sus respectivas Listas
						 */

						listadoValoresIngresos = dba
								.listadoValoresIngresosPorFecha(
										obtenerInicioEnteroFechaMesActual(),
										obtenerFinEnteroFechaMesActual());
						listadoValoresGastos = dba
								.listadoValoresGastosPorFecha(
										obtenerInicioEnteroFechaMesActual(),
										obtenerFinEnteroFechaMesActual());

						cargarLayoutListadoIngresos();
						cargarLayoutListadoGastos();

						/*
						 * Calculo los totales y la diferencia y los actualizo
						 * en sus variables
						 */
						setTotalIngresos(calcularTotalIngresos());
						setTotalGastos(calcularTotalGastos());

						/*
						 * Actualizo los TextView que contienen los totales
						 */
						actualizarTotales();

					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO no hago nada
			}
		});

		// Cierro la Base de datos
		dba.close();

	}

	/*
	 * Método que me devuelve en un entero el primer dia del mes actual
	 */
	private int obtenerInicioEnteroFechaMesActual() {

		String fecha;
		String month;
		String day;
		int entero_fecha;

		// Obtengo de la variable Calendar la fecha actual en el lunes
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);

		/*
		 * Al mes le sumo +1 porque el mes inicial (Enero) empieza desde cero:0
		 * Si el mes solo tiene un dígito le pongo un cero delante
		 */
		month = "" + (c.get(Calendar.MONTH) + 1);
		if (month.length() == 1) {
			month = "0" + month;
		}

		// Si el día solo tiene un dígito le pongo un cero delante
		day = "" + c.get(Calendar.DAY_OF_MONTH);
		if (day.length() == 1) {
			day = "0" + day;
		}

		// Obtengo la fecha en el formato AAAAMMDD
		fecha = "" + c.get(Calendar.YEAR) + month + day;

		// Paso la fecha a entero
		entero_fecha = Integer.valueOf(fecha).intValue();

		return entero_fecha;

	}

	/*
	 * Método que me devuelve en un entero el último dia de la semana actual
	 * En mi caso me interesa que el útimo día sea el de hoy para que no me muestre
	 * valores futuros
	 */
	private int obtenerFinEnteroFechaMesActual() {

		String fecha;
		String month;
		String day;
		int entero_fecha;

		// Obtengo de la variable Calendar la fecha del día actual
		Calendar c = Calendar.getInstance();
		
		// Aquí se obtendría el último dia del mes actual
		//c.add(Calendar.MONTH, 1);
		//c.set(Calendar.DATE, 1);
		//c.add(Calendar.DATE, -1);

		/*
		 * Al mes le sumo +1 porque el mes inicial (Enero) empieza desde cero:0
		 * Si el mes solo tiene un dígito le pongo un cero delante
		 */
		month = "" + (c.get(Calendar.MONTH) + 1);
		if (month.length() == 1) {
			month = "0" + month;
		}

		/*
		 * Si el día solo tiene un dígito le pongo un cero delante Le estoy
		 * sumando 7 para que me de el útimo día de la semana
		 */
		day = "" + c.get(Calendar.DAY_OF_MONTH);
		if (day.length() == 1) {
			day = "0" + day;
		}

		// Obtengo la fecha en el formato AAAAMMDD
		fecha = "" + c.get(Calendar.YEAR) + month + day;

		// Paso la fecha a entero
		entero_fecha = Integer.valueOf(fecha).intValue();

		return entero_fecha;

	}

	/*
	 * Método que me devuelve en un entero el primer dia del mes anterior
	 */
	private int obtenerInicioEnteroFechaMesAnterior() {

		String fecha;
		String month;
		String day;
		int entero_fecha;

		// Obtengo de la variable Calendar la fecha del primer día del mes
		// anterior
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DATE, 1);

		/*
		 * Al mes le sumo +1 porque el mes inicial (Enero) empieza desde cero:0
		 * Si el mes solo tiene un dígito le pongo un cero delante
		 */
		month = "" + (c.get(Calendar.MONTH) + 1);
		if (month.length() == 1) {
			month = "0" + month;
		}

		// Si el día solo tiene un dígito le pongo un cero delante
		day = "" + c.get(Calendar.DAY_OF_MONTH);
		if (day.length() == 1) {
			day = "0" + day;
		}

		// Obtengo la fecha en el formato AAAAMMDD
		fecha = "" + c.get(Calendar.YEAR) + month + day;

		// Paso la fecha a entero
		entero_fecha = Integer.valueOf(fecha).intValue();

		return entero_fecha;

	}

	/*
	 * Método que me devuelve en un entero el último dia del mes anterior
	 */
	private int obtenerFinEnteroFechaMesAnterior() {

		String fecha;
		String month;
		String day;
		int entero_fecha;

		// Obtengo de la variable Calendar la fecha del último día del mes
		// anterior
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		c.add(Calendar.DATE, -1);

		/*
		 * Al mes le sumo +1 porque el mes inicial (Enero) empieza desde cero:0
		 * Si el mes solo tiene un dígito le pongo un cero delante
		 */
		month = "" + (c.get(Calendar.MONTH) + 1);
		if (month.length() == 1) {
			month = "0" + month;
		}

		// Si el día solo tiene un dígito le pongo un cero delante
		day = "" + c.get(Calendar.DAY_OF_MONTH);
		if (day.length() == 1) {
			day = "0" + day;
		}

		// Obtengo la fecha en el formato AAAAMMDD
		fecha = "" + c.get(Calendar.YEAR) + month + day;

		// Paso la fecha a entero
		entero_fecha = Integer.valueOf(fecha).intValue();

		return entero_fecha;

	}

	/*
	 * Método que me carga en el Layout de listado de Ingresos las fechas,
	 * conceptos y valores asociados a partir de la consulta realizada a la Base
	 * de datos Mostrará valores del mes actual o el mes anterior
	 */
	private void cargarLayoutListadoIngresos() {

		int fechaAnterior = 00000000;

		RelativeLayout rowViewFechas = null;
		RelativeLayout rowViewContenido = null;

		// borro el contenido del Layout para cargarlo
		llListadoIngresos.removeAllViews();

		for (int i = 0; i < listadoValoresIngresos.size(); i++) {

			/*
			 * recupero el RelativeLayout que contiene la fila del contenido
			 * inflamos nuestra fila desde el archivo XML
			 */
			rowViewContenido = (RelativeLayout) LayoutInflater.from(this)
					.inflate(
							this.getResources().getLayout(
									R.layout.listado_informes_contenido), null);
			// Buscamos el View que contendrá el Concepto
			TextView tvConcepto = (TextView) rowViewContenido
					.findViewById(R.id.textViewListadoInformesConcepto);
			// establecemos el contenido
			tvConcepto.setText(listadoValoresIngresos.get(i).getConcepto());
			// Buscamos el View que contendrá la Cantidad
			TextView tvCantidad = (TextView) rowViewContenido
					.findViewById(R.id.textViewListadoInformesCantidad);
			// establecemos el contenido
			tvCantidad.setText("" + numeroAFormatear.format(listadoValoresIngresos.get(i).getCantidad())
					+ "€");

			if (fechaAnterior != listadoValoresIngresos.get(i).getIdFecha()) {

				fechaAnterior = listadoValoresIngresos.get(i).getIdFecha();

				rowViewFechas = (RelativeLayout) LayoutInflater
						.from(this)
						.inflate(
								this.getResources()
										.getLayout(
												R.layout.listado_informes_encabezado_fecha),
								null);

				TextView tvFecha = (TextView) rowViewFechas
						.findViewById(R.id.textViewEncabezadoFechaListadoInformes);
				tvFecha.setText(obtenerCadenaFecha(listadoValoresIngresos
						.get(i).getIdFecha()));

				// Inserto el layout de fecha como cabecera
				llListadoIngresos.addView(rowViewFechas);

				// Inserto el contenido
				llListadoIngresos.addView(rowViewContenido);

			} else {

				// Inserto el contenido
				llListadoIngresos.addView(rowViewContenido);

			}
		}

	}

	/*
	 * Método que me carga en el Layout de listado de Gastos las fechas,
	 * conceptos y valores asociados a partir de la consulta realizada a la Base
	 * de datos Mostrará valores de la semana actual o la semana anterior
	 */
	private void cargarLayoutListadoGastos() {

		int fechaAnterior = 00000000;

		RelativeLayout rowViewFechas = null;
		RelativeLayout rowViewContenido = null;

		// borro el contenido del Layout para cargarlo
		llListadoGastos.removeAllViews();

		for (int i = 0; i < listadoValoresGastos.size(); i++) {

			/*
			 * recupero el RelativeLayout que contiene la fila del contenido
			 * inflamos nuestra fila desde el archivo XML
			 */
			rowViewContenido = (RelativeLayout) LayoutInflater.from(this)
					.inflate(
							this.getResources().getLayout(
									R.layout.listado_informes_contenido), null);
			// Buscamos el View que contendrá el Concepto
			TextView tvConcepto = (TextView) rowViewContenido
					.findViewById(R.id.textViewListadoInformesConcepto);
			// establecemos el contenido
			tvConcepto.setText(listadoValoresGastos.get(i).getConcepto());
			// Buscamos el View que contendrá la Cantidad
			TextView tvCantidad = (TextView) rowViewContenido
					.findViewById(R.id.textViewListadoInformesCantidad);
			// establecemos el contenido
			tvCantidad.setText("" + numeroAFormatear.format(listadoValoresGastos.get(i).getCantidad())
					+ "€");

			if (fechaAnterior != listadoValoresGastos.get(i).getIdFecha()) {

				fechaAnterior = listadoValoresGastos.get(i).getIdFecha();

				rowViewFechas = (RelativeLayout) LayoutInflater
						.from(this)
						.inflate(
								this.getResources()
										.getLayout(
												R.layout.listado_informes_encabezado_fecha),
								null);

				TextView tvFecha = (TextView) rowViewFechas
						.findViewById(R.id.textViewEncabezadoFechaListadoInformes);
				tvFecha.setText(obtenerCadenaFecha(listadoValoresGastos.get(i)
						.getIdFecha()));

				// Inserto el layout de fecha como cabecera
				llListadoGastos.addView(rowViewFechas);

				// Inserto el contenido
				llListadoGastos.addView(rowViewContenido);

			} else {

				// Inserto el contenido
				llListadoGastos.addView(rowViewContenido);

			}
		}

	}

	/*
	 * Método para obtener la fecha en una Cadena Formateada
	 */
	private String obtenerCadenaFecha(int enteroFecha) {

		String fecha = new String("" + enteroFecha);
		String cadenaFecha;
		String year = fecha.substring(0, 4);
		String month = fecha.substring(4, 6);
		String day = fecha.substring(6);
		int y = Integer.valueOf(year).intValue();
		int m = Integer.valueOf(month).intValue();
		int d = Integer.valueOf(day).intValue();

		// Obtengo la variable Calendar
		Calendar c = Calendar.getInstance();
		// Inicio la variable Calendar con la fecha que tengo actualmente
		c.set(y, m - 1, d);

		cadenaFecha = obtenerDiaSemana(c.get(Calendar.DAY_OF_WEEK))
				+ ", " 
				+ c.get(Calendar.DAY_OF_MONTH) + " de "
				+ obtenerMes(c.get(Calendar.MONTH)) + " de "
				+ c.get(Calendar.YEAR);

		return cadenaFecha;
	}

	/*
	 * Método que me devuelve el mes según el entero pasado por parámetro
	 */
	private String obtenerMes(int month) {

		String m = new String();

		switch (month) {

		case 0:
			m = "Enero";
			break;
		case 1:
			m = "Febrero";
			break;
		case 2:
			m = "Marzo";
			break;
		case 3:
			m = "Abril";
			break;
		case 4:
			m = "Mayo";
			break;
		case 5:
			m = "Junio";
			break;
		case 6:
			m = "Julio";
			break;
		case 7:
			m = "Agosto";
			break;
		case 8:
			m = "Septiembre";
			break;
		case 9:
			m = "Octubre";
			break;
		case 10:
			m = "Noviembre";
			break;
		case 11:
			m = "Diciembre";
			break;
		default:
			m = "error";
			break;
		}

		return m;

	}
	
	/*
	 * Método que me devuelve el día de la semana según el entero pasado por parámetro
	 */
	private String obtenerDiaSemana(int day){
		
		String d = new String();		
		
		switch (day) {
		case 1:
			d = "Dom";
			break;
		case 2:
			d = "Lun";
			break;
		case 3:
			d = "Mar";
			break;
		case 4:
			d = "Mié";
			break;
		case 5:
			d = "Jue";
			break;
		case 6:
			d = "Vie";
			break;
		case 7:
			d = "Sáb";
			break;
		default:
			break;
		}
		
		return d;
	}

	/*
	 * Método que me actualiza la variable total de ingresos
	 */
	private void setTotalIngresos(double ing) {
		this.totalIngresos = ing;
	}

	/*
	 * Método que me devuelve el valor total de ingresos
	 */
	private double getTotalIngresos() {
		return this.totalIngresos;
	}

	/*
	 * Método que me actualiza la variable total de gastos
	 */
	private void setTotalGastos(double gas) {
		this.totalGastos = gas;
	}

	/*
	 * Método que me devuelve el valor total de gastos
	 */
	private double getTotalGastos() {
		return this.totalGastos;
	}

	/*
	 * Método que me calcula el total de los Ingresos a partir de la Lista de
	 * Ingresos recuperada de la BD
	 */
	private double calcularTotalIngresos() {

		double total = 0;

		if (!listadoValoresIngresos.isEmpty()) {

			for (int i = 0; i < listadoValoresIngresos.size(); i++) {
				total = total + listadoValoresIngresos.get(i).getCantidad();
			}

		}

		return total;
	}

	/*
	 * Método que me calcula el total de los Gastos a partir de la Lista de
	 * Gastos recuperada de la BD
	 */
	private double calcularTotalGastos() {

		double total = 0;

		if (!listadoValoresGastos.isEmpty()) {

			for (int i = 0; i < listadoValoresGastos.size(); i++) {
				total = total + listadoValoresGastos.get(i).getCantidad();
			}
		}

		return total;
	}

	/*
	 * Método que me actualiza los TextView con los totales
	 */
	private void actualizarTotales() {

		double balance = getTotalIngresos() - getTotalGastos();

		tvTotalIngresos.setText(" " + numeroAFormatear.format(getTotalIngresos()) + " €");
		tvTotalIngresos.setTextColor(this.getResources().getColor(
				R.color.ListadosVerdeOscuro));

		tvTotalGastos.setText(" " + numeroAFormatear.format(getTotalGastos()) + " €");
		tvTotalGastos.setTextColor(this.getResources().getColor(R.color.ListadosRojo));

		tvTotalBalance.setText(" " + numeroAFormatear.format(balance) + " €");
		if (balance >= 0) {
			tvTotalBalance.setTextColor(this.getResources().getColor(
					R.color.ListadosVerdeOscuro));
		} else {
			tvTotalBalance.setTextColor(this.getResources().getColor(
					R.color.ListadosRojo));
		}

	}

	@Override
	protected void onPause() {
		// TODO Cierro la BD en OnPause() cuando la Actividad no está en el foco
		dba.close();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Abro la base de datos mientras la Actividad está Activa
		dba.openREADWRITE();
		super.onResume();
	}

}
