package br.com.javapacketsniffer.model;

public class DestinationOptionsHeader extends OptionsHeader {

    public DestinationOptionsHeader(final byte[] packet) {
        super(packet, "Destination Options");
    }
}
