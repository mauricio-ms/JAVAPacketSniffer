package br.com.javapacketsniffer.model;

public class HopByHopHeader extends OptionsHeader {

    public HopByHopHeader(final byte[] packet) {
        super(packet, "Hop-by-Hop");
    }
}
