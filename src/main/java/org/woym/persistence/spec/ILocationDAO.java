package org.woym.persistence.spec;

import java.util.List;

import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Location;
import org.woym.common.objects.Room;

/**
 * Dieses Interface beschreibt Methoden, die von einem {@linkplain Location}
 * Data-Access-Object zu implementieren wären.
 * 
 * @author adrian
 *
 */
public interface ILocationDAO {

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen {@linkplain Location}
	 * -Objekte nach Namen sortiert zurück. Sind keine vorhanden wird, eine
	 * leere Liste zurückgegeben. Tritt bei der Anfrage ein Fehler auf, wird
	 * eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste der vorhandenen {@linkplain Location}-Objekte nach Namen
	 *         sortiert oder leere Liste, wenn keine vorhanden sind
	 * @throws DatasetException
	 */
	public List<Location> getAllLocations() throws DatasetException;

	/**
	 * Sucht in der Datenbank nach einem Standort mit dem übergebenen Namen.
	 * Existiert kein Standort mit dem übergebenen Namen wird {@code null}
	 * zurückgegeben. Ansonsten der gefundene Standort. Groß- und
	 * Kleinschreibung wird ignoriert.
	 * 
	 * @param name
	 *            - Name des gesuchten Standortes
	 * @return den Standort mit dem übergebenen Namen oder {@code null}, wenn
	 *         kein solcher existiert
	 */
	public Location getOneLocation(String name) throws DatasetException;

	/**
	 * Sucht in der Datenbank nach dem Standort, zu welchem der übergebene Raum
	 * gehört. Gehört der Raum zu keinem Standort, wird {@code null}
	 * zurückgegeben.
	 * 
	 * @param room
	 *            - der Raum, zu welchem der Standort gesucht wird
	 * @return den Standort, zu welchem der übergebene Raum gehört oder
	 *         {@code null}
	 * @throws DatasetException
	 */
	public Location getOneLocation(Room room) throws DatasetException;

}
