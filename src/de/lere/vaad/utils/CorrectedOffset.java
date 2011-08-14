package de.lere.vaad.utils;

import java.lang.reflect.Field;

import de.lere.vaad.EndOfTheWorldException;

import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class CorrectedOffset {

	
	static Offset getOffsetForCoordinate(Coordinates aCoordinate,
			OffsetFactory of, int x, int y) {

		Offset offset = of.getOffset(aCoordinate, x, y);

		if (offset.getNode() != aCoordinate) {

			String fieldName = "node";
			Object newFieldValue = aCoordinate;

			replaceOffsetFieldValue(offset, fieldName, newFieldValue);
		}

		if (offset.getReferenceMode() != Offset.NODE_REFERENCE) {
			String name = "referenceMode";
			int nodeReferenceVal = Offset.NODE_REFERENCE;
			replaceOffsetFieldValue(offset, name, nodeReferenceVal);
		}

		Offset correctedOffset = offset;
		return correctedOffset;
	}

	private static void replaceOffsetFieldValue(Offset offsetInstance,
			String fieldName, Object newFieldValue) {
		try {
			Field nodeField = Offset.class.getDeclaredField(fieldName);
			nodeField.setAccessible(true);
			nodeField.set(offsetInstance, newFieldValue);
		} catch (Exception e) {
			throw new EndOfTheWorldException(e);
		}
	}

	public static Offset getOffsetForCoordinate(Coordinates aCoordinate, int x,
			int y) {
		return getOffsetForCoordinate(aCoordinate, new OffsetFactory() {

			@Override
			public Offset getOffset(Coordinates aCoordinate, int x, int y) {
				return new Offset(x, y, aCoordinate, "");
			}
		}, x, y);
	}
	
	public static Offset getOffsetForCoordinate(Coordinates coord){
		return getOffsetForCoordinate(coord, 0, 0);			
	}

}
