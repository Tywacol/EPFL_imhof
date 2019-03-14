package ch.epfl.imhof.geometry;							

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Un polygone à trous.
 * 
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 */
public final class Polygon {
	/**
	 * L'enveloppe du polygone.
	 */
	private final ClosedPolyLine shell;

	/**
	 * Les trous du polygone.
	 */
	private final List<ClosedPolyLine> holes;

	/**
	 * Construit le polygon à partir d'une enveloppe et de trous.
	 * 
	 * @param shell
	 *            polyline fermée representant l'enveloppe
	 * @param holes
	 *            liste de trous formée de polygones fermés
	 */
	public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
		this.shell = shell;															
		this.holes = Collections.unmodifiableList(new ArrayList<ClosedPolyLine>(holes));
	}

	/**
	 * Crée un polygon sans trous.
	 * 
	 * @param shell
	 *            l'enveloppe du polygon
	 */
	public Polygon(ClosedPolyLine shell) {			
		this(shell, Collections.unmodifiableList(new ArrayList<>()));   
			
	}																							

	/**
	 * @return l'envelopppe du polygon
	 */
	public ClosedPolyLine shell() {
		return shell;
	}

	/**
	 * @return la liste de trous
	 */
	public List<ClosedPolyLine> holes() {
		return holes;																	
	}														

}
