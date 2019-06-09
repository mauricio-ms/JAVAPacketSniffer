package br.com.javapacketsniffer.view;

public class TableColumns {

    private Integer packetNumber;

    private String packetSourceIP;

    private String packetDestinationIP;

    private String packetProtocol;

    private Integer packetLength;

    private String packetInfo;

    public TableColumns(Integer packetNumber, String packetSourceIP, String packetDestinationIP, String packetProtocol, int packetLength, String packetInfo) {
        this.packetNumber = packetNumber;
        this.packetSourceIP = packetSourceIP;
        this.packetDestinationIP = packetDestinationIP;
        this.packetProtocol = packetProtocol;
        this.packetLength = packetLength;
        this.packetInfo = packetInfo;
    }


    public Integer getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(Integer packetNumber) {
        this.packetNumber = packetNumber;
    }

    public String getPacketSourceIP() {
        return packetSourceIP;
    }

    public void setPacketSourceIP(String packetSourceIP) {
        this.packetSourceIP = packetSourceIP;
    }

    public String getPacketDestinationIP() {
        return packetDestinationIP;
    }

    public void setPacketDestinationIP(String packetDestinationIP) {
        this.packetDestinationIP = packetDestinationIP;
    }

    public String getPacketProtocol() {
        return packetProtocol;
    }

    public void setPacketProtocol(String packetProtocol) {
        this.packetProtocol = packetProtocol;
    }

    public Integer getPacketLength() {
        return packetLength;
    }

    public void setPacketLength(Integer packetLength) {
        this.packetLength = packetLength;
    }

    public String getPacketInfo() {
        return packetInfo;
    }

    public void setPacketInfo(String packetInfo) {
        this.packetInfo = packetInfo;
    }
}
