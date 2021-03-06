package org.woym.common.objects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.woym.common.objects.spec.IActivityObject;
import org.woym.common.objects.spec.IMemento;
import org.woym.common.objects.spec.IMementoObject;

/**
 * Superklasse für alle Personen des Personals.
 * 
 * @author Adrian
 *
 */
@Entity
@Inheritance
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING, length = 20)
public abstract class Employee extends org.woym.common.objects.Entity implements
		IActivityObject, IMementoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9008279410416213060L;

	public static final int SCALE = 2;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Der Name dieses Lehrers.
	 */
	@Column(nullable = false)
	private String name;

	/**
	 * Das Kürzel. Darf in der Datenbank nicht null sein und ist einzigartig.
	 */
	@Column(unique = true, nullable = false)
	private String symbol;

	/**
	 * Anzahl der Wochenstunden. Darf nicht null sein. Intern immer in
	 * Zeistunden.
	 */
	@Column(nullable = false, scale = SCALE, precision = 10, columnDefinition="DECIMAL(10, 2)")
	private BigDecimal hoursPerWeek = new BigDecimal("0").setScale(SCALE);

	/**
	 * Anzahl der Stunden, die die Person des Personals auf Aktivitäten verteilt
	 * ist. Bei Erzeugung sind dies null Stunden.
	 */
	@Column(nullable = false, scale = SCALE, precision = 10, columnDefinition="DECIMAL(10, 2)")
	private BigDecimal allocatedHours = new BigDecimal("0").setScale(SCALE);

	/**
	 * Die anrechenbaren Ersatzleistungen des Lehrers.
	 */
	@ElementCollection
	@OneToMany
	@OrderBy("value")
	private List<ChargeableCompensation> compensations = new ArrayList<ChargeableCompensation>();

	/**
	 * Die möglichen Stundeninhalte dieser Person des Personals.
	 */
	@ManyToMany
	@OrderBy("name")
	protected List<ActivityType> possibleActivityTypes = new ArrayList<ActivityType>();

	/**
	 * Eine Liste von {@link TimePeriod}-Objekten, welche die Zeitwünsche des
	 * Lehrers darstellen.
	 */
	@ElementCollection
	@OneToMany
	@OrderBy("day, startTime")
	private List<TimePeriod> timeWishes = new ArrayList<>();

	public Employee() {
	}

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol.toUpperCase();
	}

	public BigDecimal getHoursPerWeek() {
		return hoursPerWeek.setScale(SCALE);
	}

	public void setHoursPerWeek(BigDecimal hoursPerWeek) {
		this.hoursPerWeek = hoursPerWeek.setScale(SCALE);
	}

	public BigDecimal getAllocatedHours() {
		return allocatedHours.setScale(SCALE);
	}

	public void setAllocatedHours(BigDecimal allocatedHours) {
		this.allocatedHours = allocatedHours.setScale(SCALE);
	}

	public List<ChargeableCompensation> getCompensations() {
		return compensations;
	}

	public void setCompensations(List<ChargeableCompensation> compensations) {
		this.compensations = compensations;
	}

	public List<ActivityType> getPossibleActivityTypes() {
		return possibleActivityTypes;
	}

	public void setPossibleActivityTypes(
			List<ActivityType> possibleActivityTypes) {
		this.possibleActivityTypes = possibleActivityTypes;
	}

	public List<TimePeriod> getTimeWishes() {
		return timeWishes;
	}

	public void setTimeWishes(List<TimePeriod> timeWishes) {
		this.timeWishes = timeWishes;
	}

	/**
	 * Überschreiben der toString-Methode, um in der DEBUG-Ausgabe des Loggers
	 * die Objekteigenschaften identifizieren zu können.
	 */
	@Override
	public String toString() {
		String hpw = hoursPerWeek.setScale(2).toPlainString();
		if (name == null) {
			return symbol + " (" + hpw + " Wochenstunden)";
		}
		return name + ", " + symbol + " (" + hpw + " Wochenstunden)";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	/**
	 * Gibt {@code true} zurück, wenn das übergebene Object == diesem Objekt
	 * oder das übergebene Object eine Instanz von {@linkplain Employee} ist und
	 * {@linkplain Employee#symbol} bei ignorierter Groß- und Kleinschreibung
	 * denselben Wert besitzt. Ansonsten wird {@code false} zurückgegeben.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Employee other = (Employee) obj;
		if (symbol == null) {
			if (other.symbol != null) {
				return false;
			}
		} else if (!symbol.toUpperCase().equals(other.symbol.toUpperCase())) {
			return false;
		}
		return true;
	}

	/**
	 * Fügt das übergebenene {@linkplain ChargeableCompensation}-Objekt der
	 * entsprechenden Liste hinzu, sofern es noch nicht darin vorhanden ist.
	 * 
	 * @param compensation
	 *            - das hinzuzufügende Objekt
	 * @return {@code true}, wenn das Objekt sich noch nicht in der Liste
	 *         befindet und hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean add(final ChargeableCompensation compensation) {
		if (!compensations.contains(compensation)) {
			compensations.add(compensation);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain ChargeableCompensation}-Objekt aus
	 * der entsprechenden Liste.
	 * 
	 * @param compensation
	 *            - das zu entfernden Objekt
	 * @return {@code true}, wenn das Objekt entfernt wurde, ansonsten
	 *         {@code false}
	 */
	public boolean remove(final ChargeableCompensation compensation) {
		return compensations.remove(compensation);
	}

	/**
	 * Gibt {@code true} zurück, wenn sich das übergebene
	 * {@linkplain ChargeableCompensation}-Objekt in der Liste befindet,
	 * ansonsten {@code false}.
	 * 
	 * @param compensation
	 *            - das Objekt, für das geprüft werden soll, ob es sich in der
	 *            Liste befindet
	 * @return {@code true}, wenn das Objekt sich in der Liste befindet,
	 *         ansonsten {@code false}
	 */
	public boolean contains(final ChargeableCompensation compensation) {
		return compensations.contains(compensation);
	}

	/**
	 * Fügt das übergebenene {@linkplain ActivityType}-Objekt der entsprechenden
	 * Liste hinzu, sofern es noch nicht darin vorhanden ist.
	 * 
	 * @param activityType
	 *            - das hinzuzufügende Objekt
	 * @return {@code true}, wenn das Objekt sich noch nicht in der Liste
	 *         befindet und hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean add(ActivityType activityType) {
		if (!this.possibleActivityTypes.contains(activityType)) {
			this.possibleActivityTypes.add(activityType);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain ActivityType}-Objekt aus der
	 * entsprechenden Liste.
	 * 
	 * @param activityType
	 *            - das zu entfernden Objekt
	 * @return {@code true}, wenn das Objekt entfernt wurde, ansonsten
	 *         {@code false}
	 */
	public boolean remove(final ActivityType activityType) {
		return possibleActivityTypes.remove(activityType);
	}

	/**
	 * Gibt {@code true} zurück, wenn sich das übergebene
	 * {@linkplain ActivityType}-Objekt in der Liste befindet, ansonsten
	 * {@code false}.
	 * 
	 * @param activityType
	 *            - das Objekt, für das geprüft werden soll, ob es sich in der
	 *            Liste befindet
	 * @return {@code true}, wenn das Objekt sich in der Liste befindet,
	 *         ansonsten {@code false}
	 */
	public boolean contains(final ActivityType activityType) {
		return possibleActivityTypes.contains(activityType);
	}

	/**
	 * Fügt das übergebenene {@linkplain TimePeriod}-Objekt der entsprechenden
	 * Liste hinzu, sofern es noch nicht darin vorhanden ist.
	 * 
	 * @param timePeriod
	 *            - das hinzuzufügende Objekt
	 * @return {@code true}, wenn das Objekt sich noch nicht in der Liste
	 *         befindet und hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean add(final TimePeriod timePeriod) {
		if (!timeWishes.contains(timePeriod)) {
			timeWishes.add(timePeriod);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain TimePeriod}-Objekt aus der
	 * entsprechenden Liste.
	 * 
	 * @param timePeriod
	 *            - das zu entfernden Objekt
	 * @return {@code true}, wenn das Objekt entfernt wurde, ansonsten
	 *         {@code false}
	 */
	public boolean remove(final TimePeriod timePeriod) {
		return timeWishes.remove(timePeriod);
	}

	/**
	 * Gibt {@code true} zurück, wenn sich das übergebene
	 * {@linkplain TimePeriod}-Objekt in der Liste befindet, ansonsten
	 * {@code false}.
	 * 
	 * @param timePeriod
	 *            - das Objekt, für das geprüft werden soll, ob es sich in der
	 *            Liste befindet
	 * @return {@code true}, wenn das Objekt sich in der Liste befindet,
	 *         ansonsten {@code false}
	 */
	public boolean contains(final TimePeriod timePeriod) {
		return timeWishes.contains(timePeriod);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Memento createMemento() {
		return new Memento(this);
	}

	/**
	 * Bei Übergabe von {@code null} oder einem Parameter, der nicht vom Typ
	 * {@linkplain Memento} ist, wird eine {@linkplain IllegalArgumentException}
	 * geworfen. Ansonsten wird der Status des Objektes auf den des übergebenen
	 * Memento-Objektes gesetzt.
	 * 
	 * @param memento
	 *            - das {@linkplain Memento}-Objekt, von welchem dieses Objekt
	 *            den Status annehmen soll
	 */
	@Override
	public void setMemento(IMemento memento) {
		if (memento == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		if (memento instanceof Memento) {
			Memento actualMemento = (Memento) memento;
			id = actualMemento.id;
			name = actualMemento.name;
			symbol = actualMemento.symbol;
			hoursPerWeek = actualMemento.hoursPerWeek;
			allocatedHours = actualMemento.allocatedHours;
			compensations = new ArrayList<ChargeableCompensation>(
					actualMemento.compensations);
			possibleActivityTypes = new ArrayList<ActivityType>(
					actualMemento.possibleActivityTypes);
			timeWishes = new ArrayList<TimePeriod>(actualMemento.timeWishes);
		} else {
			throw new IllegalArgumentException("Only " + Memento.class
					+ " as parameter allowed.");
		}
	}

	/**
	 * Die Memento-Klasse zu {@linkplain Employee}.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento implements IMemento {

		private final Long id;

		private final String name;

		private final String symbol;

		private final BigDecimal hoursPerWeek;

		private final BigDecimal allocatedHours;

		private final List<ChargeableCompensation> compensations;

		private final List<ActivityType> possibleActivityTypes;

		private final List<TimePeriod> timeWishes;

		Memento(Employee originator) {
			if (originator == null) {
				throw new IllegalArgumentException();
			}
			id = originator.id;
			name = originator.name;
			symbol = originator.symbol;
			hoursPerWeek = originator.hoursPerWeek;
			allocatedHours = originator.allocatedHours;
			compensations = new ArrayList<ChargeableCompensation>(
					originator.compensations);
			possibleActivityTypes = new ArrayList<ActivityType>(
					originator.possibleActivityTypes);
			timeWishes = new ArrayList<TimePeriod>(originator.timeWishes);
		}
	}

}
