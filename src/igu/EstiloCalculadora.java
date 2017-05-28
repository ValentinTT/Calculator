package igu;

import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

public class EstiloCalculadora extends DefaultMetalTheme{
	public String getName() { return "EstiloCalculadora"; }
	
	private final ColorUIResource primary1 = new ColorUIResource(194, 194, 194);
	private final ColorUIResource primary2 = getPrimary1();
	private final ColorUIResource primary3 = new ColorUIResource(130, 70, 8);

	private final ColorUIResource secondary1 = getPrimary2();
	private final ColorUIResource secondary2 = new ColorUIResource(170, 170, 170);
	private final ColorUIResource secondary3 = getPrimary2();

	private final ColorUIResource black = new ColorUIResource(0, 0, 0);
	private final ColorUIResource white = getPrimary2();

	protected ColorUIResource getPrimary1() { return primary1; }
	protected ColorUIResource getPrimary2() { return primary2; }
	protected ColorUIResource getPrimary3() { return primary3; }

	protected ColorUIResource getSecondary1() { return secondary1; }
	protected ColorUIResource getSecondary2() { return secondary2; }
	protected ColorUIResource getSecondary3() { return secondary3; }
	   
	protected ColorUIResource getBlack() { return black; }
	protected ColorUIResource getWhite() { return white; }
}
/*name=Payaso indica que el tema se llama Payaso.
primary1=200,0,66 es el color del borde externo
primary2=0,0,255 es el color del borde interno y el foco
primary3=0,255,0 es el color de la barra de titulo
secondary1=255,255,0 color del borde de los botones, labels, etc
secondary2=0,255,255 color de los botones al estar presionados
secondary3=255,0,255 color de fondo de los botones, labels, etc
black=255,255,200 color de las letras sobre las cajas de texto, etc
white=25,80,150 color del fondo de las cajas de texto, areas, etc
*/

