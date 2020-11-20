package by.bsuir.spolks.service;

import by.bsuir.spolks.command.tcp.CommandNameTCP;
import by.bsuir.spolks.command.udp.CommandNameUDP;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UDPConnectionService {

    private static byte[] buffer=new byte[1024*64];
    private static MemoryService memoryService = MemoryService.getInstance();

    private static final String FILE_PATH = "server\\src\\main\\resources\\files\\";
    private static final Integer PACKAGE_SIZE = 1024;
    private static final Integer SLEEP_TIME_IN_MILLIS = 1000;
    private static final Integer TIME_TO_WAIT_IN_MILLIS = 10000;

    public static DatagramPacket getPacket(DatagramSocket socket) throws IOException {

        DatagramPacket packet=new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return packet;
    }

    public static String getMessage(DatagramSocket socket) throws IOException {

        DatagramPacket packet=new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }

    public static void sendMessage(DatagramSocket socket, InetAddress inetAddress, int port, String message) throws IOException {

        DatagramPacket packet=new DatagramPacket(message.getBytes(), message.length(), inetAddress, port);
        socket.send(packet);
    }

    public static void doUDPDownload(String clientId, DatagramSocket socket, InetAddress address, int port, String fileName, int startByte) throws IOException, InterruptedException {

        File file = new File(FILE_PATH + fileName);

        if (!fileName.trim().isEmpty() && file.exists()) {
            byte[] fileByteArray = new byte[(int) file.length()];
            sendMessage(socket, address, port, "1 "+fileByteArray.length);

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                bis.read(fileByteArray, 0, fileByteArray.length);
            }

            int current = doUDPDownloadFromByteArray(socket, address, port, fileByteArray, startByte);

            if (fileByteArray.length != current) {
                memoryService.saveDownloadCommandInfo(clientId, CommandNameUDP.DOWNLOAD, fileName, current, fileByteArray);
            }

        } else {
            sendMessage(socket, address, port, "0");
        }
    }

    private static int doUDPDownloadFromByteArray(DatagramSocket socket, InetAddress address, int port, byte[] fileByteArray, int startByte) throws InterruptedException, IOException {

        int current = startByte;

        socket.setSoTimeout(TIME_TO_WAIT_IN_MILLIS);

        while (fileByteArray.length != current) {
            Thread.sleep(SLEEP_TIME_IN_MILLIS);

            int bytesToSend = Math.min(fileByteArray.length - current, PACKAGE_SIZE);
            DatagramPacket packet=new DatagramPacket(fileByteArray, current, bytesToSend, address, port);
            socket.send(packet);

            try {
                socket.receive(packet);
            }catch (SocketTimeoutException e){
                break;
            }
            current += bytesToSend;
            System.out.println(current);
        }

        return current;
    }

    public static void doUDPUpload(String clientId, DatagramSocket socket, InetAddress address, int port, String filename, byte[] memoryArray, int startByte) throws IOException {

        String[] answer = getMessage(socket).split(" ", 2);
        if (answer[0].equalsIgnoreCase("1")) {

            int fileSize = Integer.parseInt(answer[1]);
            byte[] fileByteArray = memoryArray == null ? new byte[fileSize] : memoryArray;

            int current = doUDPUploadToByteArray(socket, address, port, fileByteArray, startByte);

            if (current == fileSize) {
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(FILE_PATH + filename)))) {
                    bos.write(fileByteArray, 0, fileSize);
                    bos.flush();
                }

                sendMessage(socket, address, port, "The file has been gotten fully");
            } else {
                memoryService.saveDownloadCommandInfo(clientId, CommandNameUDP.UPLOAD, filename, current, fileByteArray);
                sendMessage(socket, address, port, "The file has not been gotten fully");
            }
        } else {
            sendMessage(socket, address, port, "There is no such file on client");
        }
    }

    private static int doUDPUploadToByteArray(DatagramSocket socket, InetAddress address, int port, byte[] fileByteArray, int startByte) throws IOException {

        int current = startByte;

        socket.setSoTimeout(TIME_TO_WAIT_IN_MILLIS);

        while (fileByteArray.length != current) {

            DatagramPacket packet=new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(packet);
            }catch (SocketTimeoutException e){
                break;
            }

            byte[] data = packet.getData();
            int length = packet.getLength();
            setArrayToAnotherArray(fileByteArray, data, current, length);
            current+=length;

            sendMessage(socket, address, port, "1");
        }

        return current;
    }

    private static void setArrayToAnotherArray(byte[] array, byte[] insertArray, int offset, int length){

        for(int i=0; i<length; ++i){
            array[offset+i]=insertArray[i];
        }
    }
}
