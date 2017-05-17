package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.util.PcapPacketArrayList;
import sample.Filereading.PcapFile;
import java.io.File;
import java.util.ArrayList;

/*
*
* Alex Lilley, Alex Mori, Ethan Balzer
*
* */

public class GetTable {
    public static TableView gettable(File file) throws IllegalAccessException {
        int i = 0;
        PcapFile myfile = new PcapFile(file.toString());
        PcapPacketArrayList mypackets = myfile.readOfflineFiles();

        TableView<Entries> table = new TableView<Entries>();

        TableColumn<Entries, Number> framnenum = new TableColumn<>("Frame #");
        TableColumn<Entries, Number> sourceip = new TableColumn<>("Source");
        TableColumn<Entries, Number> destinatonip = new TableColumn<>("Destination");
        TableColumn<Entries, Number> time = new TableColumn<>("Time (Microseconds)");
        TableColumn<Entries, Number> size = new TableColumn<>("Size(Bytes)");

        framnenum.setCellValueFactory(celldata->celldata.getValue().getFramenum());
        sourceip.setCellValueFactory(celldata->celldata.getValue().getIpsrc());
        destinatonip.setCellValueFactory(celldata->celldata.getValue().getIpdst());
        time.setCellValueFactory(celldata->celldata.getValue().getTime());
        size.setCellValueFactory(celldata->celldata.getValue().getSize());

        ArrayList<Long> timeDifs = new ArrayList<>(FindTimeDiff.findTimeDif(mypackets));
        ArrayList<ArrayList> address = new ArrayList<>(getIP(mypackets));
        ArrayList<Integer> sizes = new ArrayList<>(getsize(mypackets));
        ObservableList<Entries> inputdata = FXCollections.observableArrayList();
        ArrayList<Integer> srcs = address.get(0);
        ArrayList<Integer> dsts = address.get(1);

        for(Long in:timeDifs){
            Entries sample = new Entries(srcs.get(i),dsts.get(i) ,in,i+1,sizes.get(i));
            inputdata.add(sample);
            i+=1;
        }

        table.getColumns().addAll(framnenum,time,sourceip,destinatonip,size);
        table.setItems(inputdata);
        return table;
    }

    public static ArrayList<ArrayList> getIP(PcapPacketArrayList list){
        ArrayList listlist = new ArrayList();
        ArrayList<Integer>ipsrc=new ArrayList<>();
        ArrayList<Integer>ipdst = new ArrayList<>();

        Tcp tcp = new Tcp();
        Udp udp = new Udp();
        Arp arp = new Arp();

        for (PcapPacket s : list){
            if (s.hasHeader(tcp)){
                ipdst.add(s.getHeader(tcp).destination());
                ipsrc.add(s.getHeader(tcp).source());
            }else {
                if (s.hasHeader(udp)){
                    ipdst.add(s.getHeader(udp).destination());
                    ipsrc.add(s.getHeader(udp).source());
                } else{
                    if(s.hasHeader(arp)){
                        ipdst.add(0);
                        ipsrc.add(0);
                    }else{
                        ipdst.add(0);
                        ipsrc.add(0);}}}
        }

        listlist.add(ipdst);
        listlist.add(ipsrc);

        return listlist;

    }

    public static ArrayList<Integer> getsize(PcapPacketArrayList list){
        ArrayList<Integer> listlist= new ArrayList();

        for (PcapPacket s : list){
            listlist.add(s.size());
        }

        return listlist;
    }
}
