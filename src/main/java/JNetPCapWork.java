import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;

public class JNetPCapWork {

    private static List<PcapIf> allDevs;
    private static String n;
    private static StringBuilder errbuf;
    private static Pcap pcap;
    private static Thread t1;
    private static PcapBpfProgram program = new PcapBpfProgram();
    private static int optimize = 0;         // 0 = false
    private static int netmask = 0xFFFFFF00; // 255.255.255.0

    public static ArrayList<String> fetchAvailableInterfaces(){
        allDevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        ArrayList<String> returnArrayList=new ArrayList<>();
        errbuf = new StringBuilder(); // For any error msgs
        int r = Pcap.findAllDevs(allDevs, errbuf);
        if (r != Pcap.OK || allDevs.isEmpty()) {
            returnArrayList.add("Can't read list of devices, error is "+
                    errbuf.toString());
            return returnArrayList;
        }
        int i = 0;
        for (PcapIf device : allDevs) {
            String description = (device.getDescription() != null) ? device
                    .getDescription() : "No description available";
            returnArrayList.add("#"+i+" "+device.getName()+" ["+description+"]");i++;
        }
        return returnArrayList;
    }

    public static void capturePackets(int networkInterfaceIndex,String filterExpression){
        PcapIf device = allDevs.get(networkInterfaceIndex);
        int snaplen = 64 * 1024; // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 10 ; // 10 seconds in millis
        pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
        if (pcap == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error while opening device for capture: "
                    + errbuf.toString(), ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
            return;
        }

        if (!filterExpression.equals("")) {
            if (pcap.compile(program, filterExpression, optimize, netmask) != Pcap.OK) {
                Alert alert = new Alert(Alert.AlertType.ERROR, pcap.getErr()+ " , not a valid expression", ButtonType.OK);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    alert.close();
                }
                return;
            }
            if (pcap.setFilter(program) != Pcap.OK) {
                Alert alert = new Alert(Alert.AlertType.ERROR, pcap.getErr(), ButtonType.OK);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    alert.close();
                }
                return;
            }
        }

        String ofile = "tmp-capture-file.cap";
        PcapDumper dumper = pcap.dumpOpen(ofile); // output file
        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
            public void nextPacket(PcapPacket packet, String user) {
                PacketSniffingWindow.sendDataToUI(packet);
                dumper.dump(packet);
            }
        };
        t1 = new Thread(new Runnable() {
            private volatile boolean stopSniffing=false;
            public void run() {
                pcap.loop(pcap.LOOP_INFINITE, jpacketHandler, "jNetPcap");
                File file = new File(ofile);
                System.out.printf("%s file has %d bytes in it!\n", ofile, file.length());
                System.out.println(file.getAbsolutePath());
                dumper.close();
                pcap.close();
                /*if (file.exists()) {
                    file.delete(); // Cleanup
                }*/
            }
        });
        t1.start();
    }

    public static void stopSniffing(){
        pcap.breakloop();
    }

}
