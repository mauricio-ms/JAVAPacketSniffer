package br.com.javapacketsniffer.model;

import br.com.javapacketsniffer.helper.BitsManipulator;
import org.jnetpcap.packet.format.FormatUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ipv6 {

    private static final int FIRST_BYTE_HEADER = 14;

    public static final int ADDRESS_LENGTH_IN_BYTES = 16;

    private final byte[] packet;

    private final int version;

    private final int trafficClass;

    private final int flowLabel;

    private final short length;

    private int nextHeader;

    private final short hopLimit;

    private final byte[] sourceAddress;

    private final byte[] destinationAddress;

    private final String sourceAddressFormatted;

    private final String destinationAddressFormatted;

    private final List<Header> extensionHeaders;

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
        extensionHeaders = obtainExtensionHeaders();
    }

    private int obtainVersion() {
        return getByte(0)>>4;
    }

    private int obtainTrafficClass() {
        return getShort(0)&4080;
    }

    private int obtainFlowLabel() {
        return ByteBuffer.wrap(new byte[] {0, (byte) (getByte(1)&15), getByte(2), getByte(3)}).getInt();
    }

    private short obtainLength() {
        return getShort(4);
    }

    private int obtainNextHeader() {
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

    private List<Header> obtainExtensionHeaders() {
        final List<Header> extensionHeaders = new ArrayList<>();

        int offset = FIRST_BYTE_HEADER+40;
        Header header = obtainNextHeader(nextHeader, offset);
        while(header != null) {
            extensionHeaders.add(header);
            offset = offset + header.getLength();
            nextHeader = header.getNextHeader();
            header = obtainNextHeader(nextHeader, offset);
        }

        return extensionHeaders;
    }

    private Header obtainNextHeader(final int nextHeader, final int offset) {
        if( nextHeader == 0 ) {
            return new HopByHopHeader(Arrays.copyOfRange(packet, offset, packet.length));
        } else if( nextHeader == 60 ) {
            return new DestinationOptionsHeader(Arrays.copyOfRange(packet, offset, packet.length));
        } else if( nextHeader != 58 ) {
            System.out.println("OTHER HEADER: " + nextHeader);
        }
        return null;
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

    public int getTrafficClass() {
        return trafficClass;
    }

    public int getFlowLabel() {
        return flowLabel;
    }

    public short getLength() {
        return length;
    }

    public int getNextHeader() {
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

    public List<Header> getExtensionHeaders() {
        return extensionHeaders;
    }
}
