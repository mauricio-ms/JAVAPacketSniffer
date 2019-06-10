package br.com.javapacketsniffer.helper;

import java.nio.ByteBuffer;

public final class BitsManipulator {

    private static final int BYTE_LENGTH = 8;

    private BitsManipulator() {
    }

    /**
     * Create a mask of ones to a byte, apply the mask in the bits
     * and moves the result to obtain only the first n bits wished
     *
     * @param bits A byte
     * @param n The first n bits taht should be returned
     * @return The first n bits of the byte
     */
    public static int getFirstN(final byte bits, final int n) {
        final int maskToByte = ~(1<< BYTE_LENGTH);
        return maskToByte & bits >> BYTE_LENGTH-n;
    }

    public static byte[] getBetween(final byte[] bytes, final int start, final int end) {
        final int length = end - start;
        final int startByteIndex = start/BYTE_LENGTH;
        final int endByteIndex = end/BYTE_LENGTH;

        final byte[] bytesBetween = new byte[length];
        if( startByteIndex == endByteIndex ) {
            int count = 0;
            for (int i = start; i <=end; i++) {
//                bytesBetween[count++] = bytes
            }
        }

        for (int i = start; i < end; i++) {

        }

//        ByteBuffer.wrap(bytes).get(bytes, start, length);
        return bytesBetween;
    }

//    private static byte[] getBetween(final byte[] bytes, final byte[] bytesBetween, final int start, final int end) {

}
