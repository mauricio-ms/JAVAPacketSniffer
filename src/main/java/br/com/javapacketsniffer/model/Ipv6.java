package br.com.javapacketsniffer.model;

public class Ipv6 {

    private final byte[] packet;

    private final int version;

    public Ipv6(final byte[] packet) {
        this.packet = packet;
//        this.version = obtainVersion();
        this.version = 0;
    }

    private int obtainVersion() {
        return getFirstNBits(packet[1], 4);
    }

    private int getFirstNBits(final byte bits, final int n) {
        final int mask = 1<<n-1;
        return bits & mask;
    }

    public static void main(String[] args) {
        System.out.println(new Ipv6(null).getFirstNBits((byte) 51, 3));
    }

}
