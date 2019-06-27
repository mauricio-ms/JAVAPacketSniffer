package br.com.javapacketsniffer.model;

import java.util.Arrays;

public class OptionsHeader extends Header {

    private final byte[] remainingPacket;

    private final int nextHeader;

    private final int length;

    public OptionsHeader(final byte[] packet, final String name) {
        super(packet, name);
        nextHeader = packet[0];
        length = packet[1];
        remainingPacket = Arrays.copyOfRange(packet, length+1, packet.length);
    }

    public byte[] getRemainingPacket() {
        return remainingPacket;
    }

    public int getNextHeader() {
        return nextHeader;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String getDescription() {
        return new StringBuilder()
                .append(String.format("Header - %s\n", getName()))
                .append(String.format("Next Header: %d\n", nextHeader))
                .append(String.format("Length: %d\n", length))
                .toString();
    }
}
