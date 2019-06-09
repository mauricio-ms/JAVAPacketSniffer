import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jnetpcap.packet.Payload;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;
import org.jnetpcap.protocol.sigtran.Sctp;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

import java.util.ArrayList;


public class PacketSniffingWindow extends Application {

    private int networkInterfaceIndex;
    private static ArrayList<PcapPacket> packets = new ArrayList<>();
    private TableView<TableColumns> table = new TableView<TableColumns>();
    private Button applyFilterButton;
    private Button sniffButton;
    private Button stopButton;
    private static ObservableList<TableColumns> data = FXCollections.observableArrayList();
    private static int packetNo = 0;
    TextArea packetProtocolDetails = new TextArea();
    TextArea packetDataField = new TextArea();

    public PacketSniffingWindow(int index1) throws Exception {
        networkInterfaceIndex = index1;
        start(new Stage());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Packet Sniffing");
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(buildTopHBox());
        borderPane.setCenter(buildCenter());
        borderPane.setBottom(buildButtom());
        primaryStage.setScene(new Scene(borderPane, 1000, 700));
        primaryStage.show();
        JNetPCapWork.capturePackets(networkInterfaceIndex, "");
    }

    private HBox buildTopHBox() {
        TextArea filterTextArea = new TextArea();
        filterTextArea.setPromptText("Enter an expression to filter captured packets ....");
        filterTextArea.setMaxHeight(40);
        filterTextArea.setMinWidth(900);
        applyFilterButton = new Button("APPLY");
        applyFilterButton.setPrefSize(75, 40);
        applyFilterButton.setDisable(true);
        applyFilterButton.setOnAction(event -> {
            stopButton.setDisable(false);
            sniffButton.setDisable(true);
            applyFilterButton.setDisable(true);
            packets.clear();
            packetNo = 0;
            data.clear();
            packetDataField.clear();
            packetProtocolDetails.clear();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data saved as Pcap format in project directory", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
            String filterExpression = filterTextArea.getText();
            JNetPCapWork.capturePackets(networkInterfaceIndex, filterExpression);
        });
        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(10, 10, 10, 10));
        topHBox.setSpacing(10);
        topHBox.setStyle("-fx-background-color: #336699;");
        topHBox.setHgrow(applyFilterButton, Priority.ALWAYS);
        topHBox.setFillHeight(true);
        topHBox.getChildren().addAll(filterTextArea, applyFilterButton);
        return topHBox;
    }

    private HBox buildButtom() {
        HBox bottomHBox = new HBox();
        bottomHBox.setStyle("-fx-background-color: #336699;");
        bottomHBox.setSpacing(10);
        bottomHBox.setPadding(new Insets(10, 10, 10, 10));
        bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
        sniffButton = new Button("SNIFF");
        stopButton = new Button("STOP");
        sniffButton.setDisable(true);
        sniffButton.setOnAction(event -> {
            stopButton.setDisable(false);
            sniffButton.setDisable(true);
            applyFilterButton.setDisable(true);
            packets.clear();
            packetNo = 0;
            data.clear();
            packetDataField.clear();
            packetProtocolDetails.clear();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data saved as Pcap format in project directory", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
            JNetPCapWork.capturePackets(networkInterfaceIndex, "");
        });
        stopButton.setOnAction(event -> {
            sniffButton.setDisable(false);
            applyFilterButton.setDisable(false);
            stopButton.setDisable(true);
            JNetPCapWork.stopSniffing();
        });
        sniffButton.setPrefSize(100, 20);
        stopButton.setPrefSize(100, 20);
        bottomHBox.getChildren().addAll(sniffButton, stopButton);
        return bottomHBox;
    }

    private VBox buildCenter() {
        VBox centerVBox = new VBox();
        table.setEditable(true);
        table.setRowFactory(tv -> {
            TableRow<TableColumns> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    TableColumns clickedRow = row.getItem();
                    printInfo(clickedRow.getPacketNumber());
                }
            });
            return row;
        });
        centerVBox.setSpacing(10);
        centerVBox.setPadding(new Insets(10, 10, 10, 10));
        TableColumn firstCol = new TableColumn("No.");
        //firstNameCol.setMinWidth(100);
        centerVBox.setStyle("-fx-background-color: #336699;");
        firstCol.setCellValueFactory(new PropertyValueFactory<TableColumns, Integer>("packetNumber"));
        TableColumn secondCol = new TableColumn("Source");
        secondCol.setCellValueFactory(new PropertyValueFactory<TableColumns, String>("packetSourceIP"));
        secondCol.setMinWidth(300);
        TableColumn thirdCol = new TableColumn("Destination");
        thirdCol.setCellValueFactory(new PropertyValueFactory<TableColumns, String>("packetDestinationIP"));
        thirdCol.setMinWidth(300);
        TableColumn fourthCol = new TableColumn("Protocol");
        fourthCol.setCellValueFactory(new PropertyValueFactory<TableColumns, String>("packetProtocol"));
        fourthCol.setMinWidth(200);
        TableColumn fifthCol = new TableColumn("Length");
        fifthCol.setCellValueFactory(new PropertyValueFactory<TableColumns, String>("packetLength"));
        table.setItems(data);
        table.getColumns().addAll(firstCol, secondCol, thirdCol, fourthCol, fifthCol);
        packetProtocolDetails.setWrapText(true);
        packetDataField.setWrapText(true);
        centerVBox.getChildren().addAll(table, packetProtocolDetails, packetDataField);
        return centerVBox;
    }

    private void printInfo(Integer packetLength) {
        Ip4 ip4 = new Ip4();
        Ip6 ip6 = new Ip6();
        Ethernet eth = new Ethernet();
        Tcp tcp = new Tcp();
        Udp udp = new Udp();
        Arp arp = new Arp();
        Payload payload = new Payload();
        byte[] payloadContent;
        boolean readData = false;
        PcapPacket pcappacket = packets.get(packetLength);
        packetDataField.clear();
        packetProtocolDetails.clear();
        packetDataField.setText(pcappacket.toHexdump());
        if (pcappacket.hasHeader(ip4)) {
            if (pcappacket.hasHeader(ip4)) {
                packetProtocolDetails.appendText("IP type:\t" + ip4.typeEnum() + "\n");
                packetProtocolDetails.appendText("IP src:\t-\t" + FormatUtils.ip(ip4.source()) + "\n");
                packetProtocolDetails.appendText("IP dst:\t-\t" + FormatUtils.ip(ip4.destination()) + "\n");
                readData = true;
            }
        } else if (pcappacket.hasHeader(ip6)) {
            if (pcappacket.hasHeader(ip6)) {
                packetProtocolDetails.appendText("IP type:\t" + ip6.version() + "\n");
                packetProtocolDetails.appendText("IP src:\t-\t" + FormatUtils.ip(ip6.source()) + "\n");
                packetProtocolDetails.appendText("IP dst:\t-\t" + FormatUtils.ip(ip6.destination()) + "\n");
                readData = true;
            }
        }
        if (pcappacket.hasHeader(eth) && readData) {
            packetProtocolDetails.appendText("Ethernet type:\t" + eth.typeEnum() + "\n");
            packetProtocolDetails.appendText("Ethernet src:\t" + FormatUtils.mac(eth.source()) + "\n");
            packetProtocolDetails.appendText("Ethernet dst:\t" + FormatUtils.mac(eth.destination()) + "\n");
        }
        if (pcappacket.hasHeader(tcp) && readData) {
            packetProtocolDetails.appendText("TCP src port:\t" + tcp.source() + "\n");
            packetProtocolDetails.appendText("TCP dst port:\t" + tcp.destination() + "\n");
        } else if (pcappacket.hasHeader(udp) && readData) {
            packetProtocolDetails.appendText("UDP src port:\t" + udp.source() + "\n");
            packetProtocolDetails.appendText("UDP dst port:\t" + udp.destination() + "\n");
        }
			/*			if (pcappacket.hasHeader(rip) &&
							readData == true) {
							System.out.println("RIP count:\t" + rip.count());
							System.out.println("RIP header:\t" + rip.getHeader());
							} */
        if (pcappacket.hasHeader(arp) && readData) {
            //packetProtocolDetails.appendText("ARP decode header:\t" + arp.decodeHeader()+"\n");
            packetProtocolDetails.appendText("ARP hardware type:\t" + arp.hardwareType() + "\n");
            packetProtocolDetails.appendText("ARP hw type descr:\t" + arp.hardwareTypeDescription() + "\n");
            packetProtocolDetails.appendText("ARP hw type enum:\t" + arp.hardwareTypeEnum() + "\n");
            packetProtocolDetails.appendText("ARP hlen:\t-\t" + arp.hlen() + "\n");
            packetProtocolDetails.appendText("ARP operation:\t-\t" + arp.operation() + "\n");
            packetProtocolDetails.appendText("ARP plen:\t-\t" + arp.plen() + "\n");
            packetProtocolDetails.appendText("ARP protocol type:\t" + arp.protocolType() + "\n");
            packetProtocolDetails.appendText("ARP prtcl type descr:\t" + arp.protocolTypeDescription() + "\n");
            packetProtocolDetails.appendText("ARP prtcl type enum:\t" + arp.protocolTypeEnum() + "\n");
            packetProtocolDetails.appendText("ARP sha:\t-\t" + FormatUtils.mac(arp.sha()) + "\n");
            packetProtocolDetails.appendText("ARP sha length:\t-\t" + arp.shaLength() + "\n");
            packetProtocolDetails.appendText("ARP spa:\t-\t" + FormatUtils.ip(arp.spa()) + "\n");
            packetProtocolDetails.appendText("ARP spa length:\t-\t" + arp.spaLength() + "\n");
            packetProtocolDetails.appendText("ARP spa offset:\t-\t" + arp.spaOffset() + "\n");
            packetProtocolDetails.appendText("ARP tha:\t-\t" + FormatUtils.mac(arp.tha()) + "\n");
            packetProtocolDetails.appendText("ARP tha length:\t-\t" + arp.thaLength() + "\n");
            packetProtocolDetails.appendText("ARP tha offset:\t-\t" + arp.thaOffset() + "\n");
            packetProtocolDetails.appendText("ARP tpa:\t-\t" + FormatUtils.ip(arp.tpa()) + "\n");
            packetProtocolDetails.appendText("ARP tpa length:\t-\t" + arp.tpaLength() + "\n");
            packetProtocolDetails.appendText("ARP tpa offset:\t-\t" + arp.tpaOffset() + "\n");
            System.out.println("ARP Packet!");
            readData = true;
        }
        if (pcappacket.hasHeader(payload) && readData) {
            payloadContent = payload.getPayload();
            packetProtocolDetails.appendText("Payload:\n");
            for (int x = 0; x < payloadContent.length; x++) {
                packetProtocolDetails.appendText(payload.toHexdump());
            }
        }
    }


    public static void sendDataToUI(PcapPacket packet) {
        packets.add(packet);
        byte[] data1 = packet.getByteArray(0, packet.size()); // the package data
        String hexDump = packet.toHexdump();
        Ip4 ip4 = new Ip4();
        String sourceIP = "-";
        String destinationIP = "-";
        if (packet.hasHeader(ip4)) {
            sourceIP = org.jnetpcap.packet.format.FormatUtils.ip(ip4.source());
            destinationIP = org.jnetpcap.packet.format.FormatUtils.ip(ip4.destination());
        } else {
            Ip6 ip6 = new Ip6();
            if (packet.hasHeader(ip6)) {
                final byte[] first = packet.getByteArray(0, 16);

                int sumFirst = 0;

                for (int i = 0; i < first.length; i++) {
                    sumFirst += first[i];
                }

                System.out.println(Integer.toHexString(sumFirst));

                System.out.println(FormatUtils.hexdump(first));
                sourceIP = org.jnetpcap.packet.format.FormatUtils.ip(ip6.source());
                destinationIP = org.jnetpcap.packet.format.FormatUtils.ip(ip6.destination());
            }
        }
        data.add(new TableColumns(packetNo++, sourceIP, destinationIP,
                getProtocol(packet), packet.getCaptureHeader().caplen(), "info"));


    }

    private static String getProtocol(PcapPacket packet) {
        Tcp tcp = new Tcp();
        Udp udp = new Udp();
        Sctp sctp = new Sctp();
        if (packet.hasHeader(tcp)) {
            switch (tcp.source()) {
                case 80:
                    return "HTTP/TCP";
                case 23:
                    return "Telnet/TCP";
                case 21:
                    return "FTP/TCP";
                case 22:
                    return "SSH/TCP";
                case 25:
                    return "SMTP/TCP";
                case 69:
                    return "TFTP/TCP";
                case 110:
                    return "POP3/TCP";
                case 115:
                    return "SFTP/TCP";
                case 546:
                    return "DHCP/TCP";
                case 443:
                    return "HTTPS/TCP";
                case 5813:
                    return "ICMPD/TCP";
                case 123:
                    return "NTP/TCP";
                default:
                    return "TCP";
            }
        } else if (packet.hasHeader(udp)) {
            switch (udp.source()) {
                case 80:
                    return "HTTP/UDP";
                case 23:
                    return "Telnet/UDP";
                case 21:
                    return "FTP/UDP";
                case 22:
                    return "SSH/UDP";
                case 25:
                    return "SMTP/UDP";
                case 69:
                    return "TFTP/UDP";
                case 110:
                    return "POP3/UDP";
                case 115:
                    return "SFTP/UDP";
                case 546:
                    return "DHCP/UDP";
                case 443:
                    return "HTTPS/UDP";
                case 5813:
                    return "ICMPD/UDP";
                case 123:
                    return "NTP/UDP";
                default:
                    return "UDP";
            }
        } else if (packet.hasHeader(sctp)) {
            return "SCTP";
        }
        return "unknown";
    }
}