package org.app.schemas.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class XmlDateUtils {
	private static DatatypeFactory newInstance;
	static {
		try {
			newInstance = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			// system should failed on when this happen.
			throw new IllegalArgumentException(
					"refer to javax.xml.datatype.DatatypeFactory for potential fault.",
					e);
		}
	}

	public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
		if (date == null)
			return null;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return newInstance.newXMLGregorianCalendar(calendar);
	}

	public static XMLGregorianCalendar toXMLGregorianCalendarByTimeZone(
			Date date, TimeZone timeZone) {
		if (date == null)
			return null;
		GregorianCalendar calendar = new GregorianCalendar(timeZone);
		calendar.setTime(date);
		return newInstance.newXMLGregorianCalendar(calendar);
	}

	public static XMLGregorianCalendar toXMLGregorianCalendar(String date) {
		if (date == null)
			return null;
		return newInstance.newXMLGregorianCalendar(date);
	}

	public static XMLGregorianCalendar toXMLGregorianCalendar(
			GregorianCalendar c) {
		if (c == null)
			return null;
		return newInstance.newXMLGregorianCalendar(c);
	}

	public static XMLGregorianCalendar toXMLGregorianCalendar(Timestamp t) {
		if (t == null)
			return null;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(t);
		return newInstance.newXMLGregorianCalendar(calendar);
	}

	public static GregorianCalendar toCalendar(XMLGregorianCalendar xmlCalendar) {
		if (xmlCalendar == null)
			return null;
		return xmlCalendar.toGregorianCalendar();
	}

	public static Date toDate(XMLGregorianCalendar xmlCalendar) {
		if (xmlCalendar == null)
			return null;
		return xmlCalendar.toGregorianCalendar().getTime();
	}

	public static Timestamp toTimeStamp(XMLGregorianCalendar xmlCalendar) {
		if (xmlCalendar == null)
			return null;
		return new Timestamp(xmlCalendar.toGregorianCalendar()
				.getTimeInMillis());
	}

}
