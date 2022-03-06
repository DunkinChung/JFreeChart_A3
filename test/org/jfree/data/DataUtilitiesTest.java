package org.jfree.data;

//import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.assertThrows;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.InvalidParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataUtilitiesTest {

	private Values2D value;

	@BeforeEach
	void setUp() throws Exception {
		value = mock(Values2D.class);
		when(value.getColumnCount()).thenReturn(4);
		when(value.getRowCount()).thenReturn(3);
		when(value.getValue(0, 2)).thenReturn(5);
		when(value.getValue(1, 2)).thenReturn(7);
		when(value.getValue(2, 2)).thenReturn(1);

	}

	@Test
	void calculateColumnTotalTest() {
		double actual = DataUtilities.calculateColumnTotal(value, 2);

		double expected = 13.0;
		verify(value, times(3)).getValue(anyInt(), anyInt());
		assertEquals(expected, actual);
	}

	@Test
	void calculateRowTotalTest() {
		try {
			double actual = DataUtilities.calculateRowTotal(value, 0);

			double expected = 5.0;
			assertEquals(expected, actual);
		} catch (IndexOutOfBoundsException e) {
			fail(e.toString());
		}
	}

	@Test
	void createNumberArrayTypeTest() {
		double[] data = { 1, 2, 3, 4, 5 };
		Number[] test = DataUtilities.createNumberArray(data);
		
		Number[] num = {1,2,3,4};
		assertEquals(test.getClass(), num.getClass());
	}
	
	@Test
	void createNumberArrayValueTest() {
		double[] data = { 1.0, 2.0, 3.0, 4.0, 5.0 };
		Number[] expected = { 1.0, 2.0, 3.0, 4.0, 5.0 };
		
		Number[] test = DataUtilities.createNumberArray(data);

		assertArrayEquals(expected, test);
	}
	
	@Test
	void createNumberArrayNullTest() {
		assertThrows(IllegalArgumentException.class, () -> DataUtilities.createNumberArray(null));
	}

	@Test
	void createNumberArray2DTest() {
		double[][] data = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
		Number[][] actual = DataUtilities.createNumberArray2D(data);
		
		Number[][] test = {};
		assertEquals(test.getClass(), actual.getClass());
	}
	
	@Test
	void createNumberArray2DNullTest() {
		assertThrows(IllegalArgumentException.class, () -> DataUtilities.createNumberArray2D(null));
	}

	@Test
	void getCumulativePercentagesTest() {
		KeyedValues table = mock(KeyedValues.class);

		when(table.getKey(0)).thenReturn(0);
		when(table.getKey(1)).thenReturn(1);
		when(table.getKey(2)).thenReturn(2);

		when(table.getIndex(0)).thenReturn(0);
		when(table.getIndex(1)).thenReturn(1);
		when(table.getIndex(2)).thenReturn(2);

		when(table.getValue(0)).thenReturn(5);
		when(table.getValue(1)).thenReturn(9);
		when(table.getValue(2)).thenReturn(2);
		
		when(table.getItemCount()).thenReturn(3);

		KeyedValues actual = DataUtilities.getCumulativePercentages(table);
		Double[] received = new Double[3];
		
		for(int i = 0; i < actual.getItemCount(); i++) {
			received[i] = (double) actual.getValue(i);
		}
		
		System.out.println(actual.getItemCount());
		
		double[] expectedValues = { 0.3125, 0.875, 1.0 };
		
		assertEquals(expectedValues, received);
	}
	
	@Test
	void getCumulativePercentagesNullTest() {
		assertThrows(IllegalArgumentException.class, () -> DataUtilities.getCumulativePercentages(null));
	}
	
}

