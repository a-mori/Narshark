package sample.Filereading;

import org.jnetpcap.*;
import org.jnetpcap.packet.*;
import org.jnetpcap.util.PcapPacketArrayList;

/*
*
* Alex Lilley, Alex Mori, Ethan Balzer
*
* */

public class PcapFile {

    private String FileAddress = "test1.pcap";

    public PcapFile(String FileAddress)
  {
              this.FileAddress = FileAddress;
            }

    public PcapPacketArrayList readOfflineFiles() throws IllegalAccessException {
        final StringBuilder errbuf = new StringBuilder();
        Pcap pcap = Pcap.openOffline(FileAddress, errbuf);

        if (pcap == null) {
            throw new IllegalAccessException(errbuf.toString());
        }

        PcapPacketHandler<PcapPacketArrayList> jpacketHandler = new PcapPacketHandler<PcapPacketArrayList>() {
            public void nextPacket(PcapPacket packet, PcapPacketArrayList PaketsList) {
                PaketsList.add(packet);
            }
        };

        try {
            PcapPacketArrayList packets = new PcapPacketArrayList();
            pcap.loop(-1,jpacketHandler,packets);
            return packets;
        } finally {
            pcap.close();
        }
    }
}
