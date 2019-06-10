package br.com.javapacketsniffer.helper;

import org.junit.Test;

import static org.junit.Assert.*;

public class BitsManipulatorTest {

    @Test
    // TODO 4 0U 5
    public void getFirst5FromInt51ShouldReturn6() {
        // Arrange
        final int firstExpected = 6;
        final byte bits = 51;
        final int n = 5;
        // Act
        final int first = BitsManipulator.getFirstN(bits, n);
        // Assert
        assertEquals(firstExpected, first);
    }

    @Test
    public void getBytesBetweenInterval() {
        // Arrange
        final byte[] bytesExpected = {
                // 11111111
                (byte) 255
        };
        final byte[] bytes = {
                // 00001111
                Integer.valueOf(15).byteValue(),
                // 11110000
                Integer.valueOf(240).byteValue()
        };
        final int start = 4;
        final int end = 12;
        // Act
        final byte[] bytesActual = BitsManipulator.getBetween(bytes, start, end);
        // Assert
        assertEquals(bytesExpected, bytesActual);
    }

}