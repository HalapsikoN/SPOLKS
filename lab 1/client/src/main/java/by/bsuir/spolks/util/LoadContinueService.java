package by.bsuir.spolks.util;

import by.bsuir.spolks.entity.StoppedDownloadFileMemory;
import by.bsuir.spolks.service.TCPConnectionService;
import by.bsuir.spolks.service.UDPConnectionService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class LoadContinueService {

    private static final String FILE_PATH = "client\\src\\main\\resources\\files\\";
    private static final Integer PACKAGE_SIZE = 1024;
    private static final Long TIME_TO_WAIT_MILLIS = 20000L;

    private static final StoppedDownloadFileMemory memory = StoppedDownloadFileMemory.getInstance();

    public static void checkUnfinishedCommand(DataInputStream fromServer, DataOutputStream toServer) throws IOException {

        String message = fromServer.readUTF();
        System.out.println("Message from server: '" + message + "'");
        String[] messageInfo = message.split(" ");

        switch (messageInfo[0]) {
            case "DOWNLOAD":
                continueDownload(fromServer, toServer, messageInfo[1], Integer.parseInt(messageInfo[2]));
                break;
            case "UPLOAD":
                continueUpload(fromServer, toServer, messageInfo[1], Integer.parseInt(messageInfo[2]));
                break;
            default:
                break;
        }
    }

    public static void checkUnfinishedCommand(DatagramSocket socket, InetAddress address, int port) throws IOException {

        String message = UDPConnectionService.getMessage(socket);
        System.out.println("Message from server: '" + message + "'");
        String[] messageInfo = message.split(" ");

        switch (messageInfo[0]) {
            case "DOWNLOAD":
                continueDownload(socket, address, port, messageInfo[1], Integer.parseInt(messageInfo[2]));
                break;
            case "UPLOAD":
                continueUpload(socket, address, port, messageInfo[1], Integer.parseInt(messageInfo[2]));
                break;
            default:
                break;
        }
    }

    private static void continueDownload(DataInputStream fromServer, DataOutputStream toServer, String fileName, int sendBytes) throws IOException {

        if (memory.isTCPSaved() && memory.getFileName().equals(fileName) && memory.getDownloadedBytes() == sendBytes) {
            TCPConnectionService.doTCPDownload(fromServer, toServer, fileName, memory.getFileByteArray(), sendBytes);
        }
    }

    private static void continueUpload(DataInputStream fromServer, DataOutputStream toServer, String fileName, int sendBytes) throws IOException {

        try {
            TCPConnectionService.doTCPUpload(fromServer, toServer, fileName, sendBytes);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private static void continueDownload(DatagramSocket socket, InetAddress address, int port, String fileName, int sendBytes) throws IOException {

        if (memory.isUDPSaved() && memory.getFileName().equals(fileName) && memory.getDownloadedBytes() == sendBytes) {
            UDPConnectionService.doUDPDownload(socket, address, port, fileName, memory.getFileByteArray(), sendBytes);
        }
    }

    private static void continueUpload(DatagramSocket socket, InetAddress address, int port, String fileName, int sendBytes) throws IOException {

        try {
            UDPConnectionService.doUDPUpload(socket, address, port, fileName, sendBytes);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
