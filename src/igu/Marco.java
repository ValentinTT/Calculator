package igu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Marco extends JFrame{
	private int ancho, ultimo_a;
	private boolean si = false;
	private LienzoPrincipal lienzo;
	public Marco(){
		setBounds(400, 300, 340, 315);
		ancho = Toolkit.getDefaultToolkit().getScreenSize().width;
		setTitle("Calculadora");
		setIconImage(Toolkit.getDefaultToolkit().getImage("src/igu/mons.gif"));
		
		lienzo = new LienzoPrincipal();
		add(lienzo);
		//pack();//Ajusta el tama�o del marco al de los componentes que tiene dentro.
		setMinimumSize(new Dimension(340, 315));
		setPreferredSize(new Dimension(340, 315));
		setMaximumSize(new Dimension(ancho, 315));

		addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				Dimension medida = e.getComponent().getSize();
				lienzo.resizeTodosBotones((int)((medida.getWidth()*medida.getHeight())/10000));
			}
		});
		//setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
class LienzoPrincipal extends JPanel{
	private Teclado teclado;
	private JButton igual;
	private JTextField disp;
	private boolean limpiar = true;//Permite ingresar un nuevo n�mero al display si algun boton *+-/ es precionado.
	private double resultado = 0;//Almacena el resultado de las operaciones
	private String ultimaOp = "";//Almacena la ultima operacion realizada +*-/=
	private String penOp = "";
	private double ultimo = 0;
	
	public LienzoPrincipal(){
		setLayout(new BorderLayout());
		
		disp = new JTextField("0");//Se inicia la pantalla con 0.
		disp.setBackground(new Color(8, 67, 130));
		disp.setForeground(Color.white);
		disp.setFont(new Font("", Font.PLAIN, 50));
		disp.setEditable(false);
		disp.setHorizontalAlignment(JTextField.RIGHT);
		disp.setToolTipText("ValTapTor");

		add(disp, BorderLayout.NORTH);
		
		teclado = new Teclado();//Se crean el lienzo contenedor de los botones de la calculadora.
		add(teclado, BorderLayout.CENTER);

		igual = new JButton("  =  ");
		igual.setBorderPainted(false);
		igual.setFocusPainted(false);
		igual.addActionListener(new AccionOperacion());
		igual.addKeyListener(new AccionTeclado());
		igual.setBackground(new Color(150, 150, 150));
		disp.addKeyListener(new AccionTeclado());
		add(igual, BorderLayout.EAST);
	}
	class Teclado extends JPanel{
		private JButton[][] Botones= new JButton[4][4];//16 Botones que con n�meros y operaciones.
		public Teclado(){
			setLayout(new GridLayout(4, 4));
			setBackground(new Color(150, 150, 150));
			String textoT = "789/456*123-0.@+";//String que contiene el texto de cada boton.
			int aux = 0;//Auxiliar que recorre el String textoT.
			
			for(int i = 0; i < 4; i++){//Inicia la matriz de botones.
				for(int j = 0; j < 4; j++){
					if(textoT.charAt(aux) != '@')//El signo @ sinboliza el boton borrar
						this.AgregarBoton(String.valueOf(textoT.charAt(aux)), i, j);
					else
						this.AgregarBoton("A/C", i, j);
					aux++;
				}
			}
		}
		public void AgregarBoton(String texto, int i, int j){//Resive el titulo del boton y su posicion en la matriz.
			Botones[i][j] = new JButton(texto);
			Botones[i][j].setBorderPainted(false);
			Botones[i][j].setFocusPainted(false);
			add(Botones[i][j]);
			if("1234567890.".contains(texto)){//Si el boton representa un n�mero el oyente es un objeto AccionNumero.
				Botones[i][j].addActionListener(new AccionNumero());
				Botones[i][j].setBackground(new Color(150, 150, 150));
			}
			else if(texto.length() < 2){//Si el texto del boton es mas de un caracter (Borrar)
				Botones[i][j].addActionListener(new AccionOperacion());
				Botones[i][j].setBackground(new Color(222, 85, 0));
			}
			else {//Sino representa un numero el objeto oyente es de tipo AccionOperacion.
				Botones[i][j].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if(disp.getText().length() > 1 && !ultimaOp.contains("="))//Si el display es mayor a 1 o la ultima operacion no fue =
							disp.setText((String) disp.getText().subSequence(0, disp.getText().length()-1));//Borrar el último caracter.
						else disp.setText("0");
					}
				});
				Botones[i][j].setBackground(new Color(150, 150, 150));
			}
			Botones[i][j].addKeyListener(new AccionTeclado());
		}
		public JButton[][] getBotones(){
			return Botones;
		}
	}
	class AccionTeclado extends KeyAdapter{
		public void keyTyped(KeyEvent e){
			if(',' == e.getKeyChar()) e.setKeyChar('.');//La coma es conciderada como .
			else if((int)e.getKeyChar() == 10) e.setKeyChar('=');//El enter es conciderado como = 
			else if((int)e.getKeyChar() == 8){//El delete es conciderado como Borrar
				if(disp.getText().length() > 1 && !ultimaOp.contains("="))//Mismo codigo que en el ActionListener del boton igual
					disp.setText((String) disp.getText().subSequence(0, disp.getText().length()-1));
				else disp.setText("0");
			}
			if("0123456789.".contains("" + e.getKeyChar()))//Si el teclado envia un número realiza la misma operacion que si ubieran presionado el boton en pantalla
				new AccionNumero().displayNumero("" + e.getKeyChar());
			else if("+*/-=".contains("" + e.getKeyChar()))
				new AccionOperacion().displayOperacion(disp.getText(), "" + e.getKeyChar());
		}
	}
	class AccionNumero implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			displayNumero(e.getActionCommand());
		}
		public void displayNumero(String entrada){//Agrega un numero a la pantalla
			if(ultimaOp.contains("=")){//Si la ultima operacion fue = se inicia el display de n�evo en 0
				disp.setText("0");//El 0 no se ve ya que el otro n�mero del siguiente condicional lo reemplaza.
				ultimaOp = "";
			}
			if(disp.getText().equals("0") || limpiar){//Si el display es 0.
				limpiar = false;
				if(entrada.contains("."))disp.setText("0.");//Si el boton presionado es .
				else if(entrada.contains("0"))disp.setText("0");//Evita que se escriba 000000.
				else disp.setText(entrada);//Reemplaza el 0 por el boton presionado
			}
			else if(entrada.contains(".")){//Evita que se escriban muchos ...
				if(!disp.getText().contains(".")) disp.setText(disp.getText() + ".");
			}
			else disp.setText(disp.getText() + entrada);//Concatena el ulimo n�mero presionado.
			ultimo = Double.parseDouble(disp.getText());
		}
	}
	class AccionOperacion implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			displayOperacion(disp.getText(), e.getActionCommand());
		}
		public void displayOperacion(String texto, String nueva){
			if(texto.charAt(texto.length()-1) != '.'){//Entra si el n�mero no termina en .
				limpiar = true;
				if(ultimaOp == "")//Si es la primer operacio, el resultado es el valor del display.
					resultado = Double.parseDouble(texto);
				else if(nueva.contains("=")){//Si el boton fue =.
					if(ultimaOp.contains("=")){//Si se apreto dos veces igual .
						System.out.println(resultado);
						disp.setText("" + ultimo);//El ultimo n�mero ingresado realiza la cuenta +*/- con lo del display. Esto lo que hace es colocar el ultimo numero ingresado(por teclado 0 botones) al display para que se pueda hacer la operacion anterior
						ultimaOp = penOp;//Logra que ultimaOp que es = sea *+/- para realizar la cuenta
						RealizarCuenta();
					}						
					else RealizarCuenta();//Calcula el resultado

					disp.setText(String.valueOf(resultado));//Imprime el resultado.
					limpiar = false;//Permite que se pueda entrar al primer condicional otra ves
					penOp = ultimaOp;//Copia la ultima operacion distinta a = por si se presiona = de nuevo que realice la ultima operacion
				}
				else if(!ultimaOp.contains("="))//Realiza la operacion previa al boton presionado
					RealizarCuenta();
				ultimaOp = nueva;//El boton presionado pasa a ser la operacion previa.
			}
		}
		public void RealizarCuenta(){//Realiza la cuenta que indica ultimaOp.
			if(ultimaOp.contains("+"))
				resultado += Double.parseDouble(disp.getText());
			else if(ultimaOp.contains("-"))
				resultado -= Double.parseDouble(disp.getText());
			else if(ultimaOp.contains("*"))
				resultado *= Double.parseDouble(disp.getText());
			else if(ultimaOp.contains("/"))
				resultado /= Double.parseDouble(disp.getText());
		}
	}
	public void resizeTodosBotones(int size){
		ArrayList<JButton> botones = new ArrayList<JButton>();
		JButton[][] botones_teclado = teclado.getBotones();
		for(JButton[] i: botones_teclado){
			for(JButton j: i){
				botones.add(j);
			}
		}
		botones.add(igual);
		
		for(JButton b: botones){
			b.setFont(new Font("", Font.PLAIN, size));
		}

	}
}
