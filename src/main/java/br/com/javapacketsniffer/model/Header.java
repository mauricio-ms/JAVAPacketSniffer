package br.com.javapacketsniffer.model;

public abstract class Header {

    private final byte[] packet;

    private final String name;

    Header(final byte[] packet, final String name) {
        this.packet = packet;
        this.name = name;
    }

    public byte[] getPacket() {
        return packet;
    }

    public String getName() {
        return name;
    }

    public abstract int getLength();

    public abstract int getNextHeader();

    public abstract String getDescription();
}
