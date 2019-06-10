package br.com.javapacketsniffer.model;

import br.com.javapacketsniffer.helper.BitsManipulator;
import org.jnetpcap.packet.format.FormatUtils;

import java.nio.ByteBuffer;

public class Ipv6 {

    // TODO - não sei pq diabo começa do byte 14
    private static final int FIRST_BYTE_HEADER = 14;

    public static final int ADDRESS_LENGTH_IN_BYTES = 16;

    private final byte[] packet;

    private final int version;

    private final short trafficClass;

    private final int flowLabel;

    private final short length;

    private final short nextHeader;

    private final short hopLimit;

    private final byte[] sourceAddress;

    private final byte[] destinationAddress;

    private final String sourceAddressFormatted;

    private final String destinationAddressFormatted;

    public Ipv6(final byte[] packet) {
        this.packet = packet;
        version = obtainVersion();
        trafficClass = obtainTrafficClass();
        flowLabel = obtainFlowLabel();
        length = obtainLength();
        nextHeader = obtainNextHeader();
        hopLimit = obtainHopLimit();
        sourceAddress = obtainSourceAddress();
        destinationAddress = obtainDestinationAddress();
        sourceAddressFormatted = formatAddress(sourceAddress);
        destinationAddressFormatted = formatAddress(destinationAddress);
    }

    private int obtainVersion() {
        return getByte(0)>>4;
    }

    private short obtainTrafficClass() {
        return 0;
    }

    private int obtainFlowLabel() {
        return ByteBuffer.wrap(new byte[] {0, (byte) (getByte(1)>>4), getByte(2), getByte(3)}).getInt();
    }

    private short obtainLength() {
        return getShort(4);
    }

    private short obtainNextHeader() {
        return getByte(6);
    }

    private short obtainHopLimit() {
        return getByte(7);
    }

    private byte[] obtainSourceAddress() {
        return get(8, ADDRESS_LENGTH_IN_BYTES);
    }

    private byte[] obtainDestinationAddress() {
        return get(24, ADDRESS_LENGTH_IN_BYTES);
    }

    private String formatAddress(final byte[] address) {
        return FormatUtils.ip(address);
    }

    private byte getByte(final int index) {
        return ByteBuffer.wrap(packet).get(FIRST_BYTE_HEADER + index);
    }

    private short getShort(final int index) {
        return ByteBuffer.wrap(packet).getShort(FIRST_BYTE_HEADER + index);
    }

    private byte[] get(final int index, final int length) {
        final byte[] target = new byte[length];
        ByteBuffer.wrap(packet, FIRST_BYTE_HEADER + index, length).get(target);
        return target;
    }

    public int getVersion() {
        return version;
    }

    public short getTrafficClass() {
        return trafficClass;
    }

    public int getFlowLabel() {
        return flowLabel;
    }

    public short getLength() {
        return length;
    }

    public short getNextHeader() {
        return nextHeader;
    }

    public short getHopLimit() {
        return hopLimit;
    }

    public byte[] getSourceAddress() {
        return sourceAddress;
    }

    public byte[] getDestinationAddress() {
        return destinationAddress;
    }

    public String getSourceAddressFormatted() {
        return sourceAddressFormatted;
    }

    public String getDestinationAddressFormatted() {
        return destinationAddressFormatted;
    }
}
