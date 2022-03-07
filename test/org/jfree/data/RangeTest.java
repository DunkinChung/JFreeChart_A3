package org.jfree.data;

//import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import java.security.InvalidParameterException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RangeTest {

	private Range exampleRange;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	
	@ParameterizedTest
	@CsvSource({"0, 5, 0, 5, 0, 5, same range", 
		"-5, 9, 9, -7, -7, 9, lower bound",
		"-2, 11, -2, 20, -2, 20, upper bound",
		"-10, 100, 5, 50, -10, 100, subset"})
	void combineTest(int lb1, int ub1, int lb2, int ub2, int expectedlb, int expectedub, String msg) {
		Range range1 = new Range(lb1, ub1);
		Range range2 = new Range(lb1, ub2);
		
		Range actual = Range.combine(range1, range2);
		Range expected = new Range(expectedlb, expectedub);
		
		
		assertEquals(expected, actual);
	}
	
	
//constrain test
	@ParameterizedTest
	@CsvSource({ "1, 5, 3.0, 3.0, small value", "1, 100, 50.5, 50.5, decimal value",
			"10000, 2147483647, 1073746823.5, 1073746823.5, max integer upperbound", 
			"-1, 1, 1, 1, negative lowerbound",
			"0, 10, 11, 10, above upperbound",
			"0, 10, -5, 0, below lowerbound"})
	void constrainTest(int lb, int ub, double entry, double expected, String msg) {
		Range central = new Range(lb, ub);

		assertEquals(expected, central.constrain(entry));
	}

//contains test
	@ParameterizedTest
	@CsvSource({ "1, 5, 3.0, true, small value", "1, 100, 50.5, true, decimal value",
			"10000, 2147483647, 1073746823.5, true, max integer upperbound", 
			"-1, 1, 1, true, negative lowerbound",
			"1, 5, 8, false, Wrong value", 
			"1, 5, 4, true, wrong boolean"})
	void containsTest(int lb, int ub, double value, boolean expected, String msg) {
		Range central = new Range(lb, ub);

		assertEquals(expected, central.contains(value));
	}

//equals test
	@ParameterizedTest
	@CsvSource({ "1, 5, 1, 5, true, small value", 
		"1, 100, -3, 99, false, not matching",
			"10000, 2147483647,10000, 2147483647, true, max integer upperbound", 
			"1, 5, 1, 5, true, they equal" })
	void equalsTest(int lb, int ub, int lbe, int ube, boolean expected, String msg) {
		Range central = new Range(lb, ub);
		Range expectedRange = new Range(lbe, ube);

		assertEquals(expected, central.equals(expectedRange));
	}
	
	@Test
	void equalsInvalidObjectTest() {
		Range range1 = new Range(1,2);
		
		assertFalse(range1.equals(null));
	}

//expand test
	@ParameterizedTest
	@CsvSource({ "1, 5, 1.0, 5.0, -3, 21, small value", 
		"-1, 5, 1.0, 5.0, -7, 29, negative lower",
			"1, 5, 1.0, 5.0, -6, 21, mismatch" })
	void expandTest(int lb, int ub, double lowerMargin, double upperMargin, double lbe, double ube, String msg) {
		Range central = new Range(lb, ub);
		Range expectedRange = new Range(lbe, ube);

		assertEquals(expectedRange, Range.expand(central, lowerMargin, upperMargin));
	}

	@Test
	void expandNullTest() {
		assertThrows(IllegalArgumentException.class, () -> Range.expand(null, 1, 2));
	}
	
//expand to include test
	@ParameterizedTest
	@CsvSource({ "1, 5, 1.0, 7.0, 7, small value", 
		"1, 100, 1.0, 100.0, 50.5, decimal value",
			"10000, 2147483647, 10000, 2147483647, 1073746823.5, max integer upperbound",
			"0, 5, -2, 5, -2, lowerbound"})
	void expandToIncludeTest(int lb, int ub, double lbe, double ube, double expected, String msg) {
		Range central = new Range(lb, ub);
		Range expectedRange = new Range(lbe, ube);

		assertEquals(expectedRange, Range.expandToInclude(central, expected));
	}
	
	@Test
	void expandToIncludeNullTest() {
		assertNotNull(Range.expandToInclude(null, 5));
	}

//getCentralValue test
	@ParameterizedTest
	@CsvSource({ "1, 5, 3.0, small value", 
		"1, 100, 50.5, decimal value",
			"10000, 2147483647, 1073746823.5, max integer upperbound" })
	void getCentralValueTest(int lb, int ub, double expected, String msg) {
		Range central = new Range(lb, ub);

		assertEquals(expected, central.getCentralValue());
	}

//getLength test
	@ParameterizedTest
	@CsvSource({ "-1, 1, 2, negative lowerbound", 
		"1, 1, 0, same lb and up", 
		"-2, 1000, 1002, large" })
	void getLengthTest(int lb, int ub, double expected, String msg) {
		Range length = new Range(lb, ub);

		assertEquals(expected, length.getLength());
	}

//getLowerBound test
	@ParameterizedTest
	@CsvSource({ "-1, 100, -1, negative lowerbound", 
		"-2147483648, 1, -2147483648, Min INT",
			"2147483647, 2147483647, 2147483647, Max INT" })
	void getLowerBoundTest(int lb, int ub, double expected, String msg) {
		Range lower = new Range(lb, ub);

		assertEquals(expected, lower.getLowerBound());
	}

//getUpperBound test
	@ParameterizedTest
	@CsvSource({ "-1, 100, 100, negative lowerbound", 
		"-2147483648, 1, 1, Min INT",
			"-2147483647, 2147483647, 2147483647, Max INT" })
	void getUpperBoundTest(int lb, int ub, double expected, String msg) {
		Range upper = new Range(lb, ub);

		assertEquals(expected, upper.getUpperBound());
	}

//intersects Test
	@ParameterizedTest
	@CsvSource({ "1, 2, 3, 4, false, No intersect", 
		"1, 4, 2, 3, true, Inside intersect",
			"1, 2, 2, 3, true, Overlap Intersect", 
			"1, 3, 2, 4, true, Lower bound intersect",
			"2, 4, 1, 3, true, Upper bound intersect" })
	void intersectsTest(double lb, double ub, double lower, double upper, boolean expected, String msg) {
		Range exampleRange = new Range(lb, ub);

		assertEquals(expected, exampleRange.intersects(lower, upper));
	}

//shift test
	@ParameterizedTest
	@CsvSource({ "1, 1, 3, 3, 2, Same digits, move by 2", 
		"2, 4, 5, 7, 3, move by 3",
			"-3, -1, -1, 1, 2, move from negative to positive" })
	void shiftTest(double exampleLB, double exampleUB, double lower, double upper, double shiftAmt, String msg) {

		Range exampleRange = new Range(exampleLB, exampleUB);
		Range expectedRange = new Range(lower, upper);
		assertEquals(expectedRange, Range.shift(exampleRange, shiftAmt));
	}

//shift null throw exception
	@Test
	void shiftInvalidInputTest() {
		assertThrows(InvalidParameterException.class, () -> Range.shift(null, 3));
	}

//shift crossing test
	@ParameterizedTest
	@CsvSource({ "1, 1, 3, 3, 2, true, Crossing allowed but no crossing",
			"2, 4, 5, 7, 3, false, Crossing not allowed but no crossing",
			"-2, -1, 1, 2, 3, true, Crossing allowed and actually crossing",
			"-3, -1, 0, 0, 4, false, Crossing not allowed and actually crossing" })
	void shiftCrossingTest(double exampleLB, double exampleUB, double lower, double upper, double shiftAmt,
			boolean crossingAllowed, String msg) {

		Range exampleRange = new Range(exampleLB, exampleUB);
		Range expectedRange = new Range(lower, upper);
		assertEquals(expectedRange, Range.shift(exampleRange, shiftAmt, crossingAllowed));
	}

//toString test

	@Test
	void testToString() {
		exampleRange = new Range(-1, 1);
		assertEquals("Range[-1.0,1.0]", exampleRange.toString(), "The toString gives the expected output");
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

}