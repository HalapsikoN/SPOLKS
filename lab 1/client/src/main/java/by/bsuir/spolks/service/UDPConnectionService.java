package by.bsuir.spolks.service;

import by.bsuir.spolks.command.tcp.CommandNameTCP;
import by.bsuir.spolks.command.udp.CommandNameUDP;
import by.bsuir.spolks.entity.StoppedDownloadFileMemory;
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
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UDPConnectionService {

    private static StoppedDownloadFileMemory memory = StoppedDownloadFileMemory.getInstance();

    private static final String FILE_PATH = "client\\src\\main\\resources\\files\\";
    private static final int TIME_TO_WAIT_IN_MILLIS=10000;
    private static final int SLEEP_TIME_IN_MILLIS=1000;
    private static final int PACKAGE_SIZE=1024;
    private static byte[] buffer=new byte[1024*64];

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

    public static void doUDPDownload(DatagramSocket socket, InetAddress inetAddress, int port, String fileName, byte[] memoryArray, int startByte) throws IOException {

        String[] answer = getMessage(socket).split(" ", 2);
        if (answer[0].equalsIgnoreCase("1")) {

            int fileSize = Integer.parseInt(answer[1]);
            byte[] fileByteArray = memoryArray == null ? new byte[fileSize] : memoryArray;


            long startTime = System.currentTimeMillis();
            int current = doUDPDownloadToByteArray(socket, inetAddress, port, fileByteArray, startByte);
            long endTime = System.currentTimeMillis();

            if (current == fileSize) {
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(FILE_PATH + fileName)))) {
                    bos.write(fileByteArray, 0, fileSize);
                    bos.flush();
                }
                System.out.println("The file has been gotten fully. Received (" + fileSize + ") bytes");
                long resultTimeInSeconds = TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.MILLISECONDS);
                System.out.println("Total time: " + resultTimeInSeconds + " seconds");
                System.out.println("Bandwidth: " + (double) fileSize / resultTimeInSeconds + " Bit/s");
                StoppedDownloadFileMemory.getInstance().setSaved(false);
            } else {
                memory.saveDownloadCommandInfo(CommandNameUDP.DOWNLOAD, fileName, current, fileByteArray);
                System.out.println("The file has not been gotten fully(" + current + "/" + fileSize + ")");
                System.out.println(memory);
            }
        } else {
            System.out.println("There is no such file on server");
            StoppedDownloadFileMemory.getInstance().setSaved(false);
        }
    }

    private static int doUDPDownloadToByteArray(DatagramSocket socket, InetAddress inetAddress, int port, byte[] fileByteArray, int startByte) throws IOException {

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

            System.out.println("Downloaded: " + (int) ((double) current / fileByteArray.length * 100) + "%");

            sendMessage(socket, inetAddress, port, "1");
        }

        return current;
    }

    public static void doUDPUpload(DatagramSocket socket, InetAddress inetAddress, int port, String fileName, int startByte) throws IOException, InterruptedException {

        File file = new File(FILE_PATH + fileName);

        if (!fileName.trim().isEmpty() && file.exists()) {
            byte[] fileByteArray = new byte[(int) file.length()];
            sendMessage(socket, inetAddress, port, "1 "+fileByteArray.length);

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                bis.read(fileByteArray, 0, fileByteArray.length);
            }

            long startTime = System.currentTimeMillis();
            int current = doUDPUploadToByteArray(socket, inetAddress, port, fileByteArray, startByte);
            long endTime = System.currentTimeMillis();

            if (current != fileByteArray.length) {
                System.out.println("The file has not been send fully");
            } else {
                System.out.println("The file has been send fully. Received (" + fileByteArray.length + ") bytes");
                long resultTimeInSeconds = TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.MILLISECONDS);
                System.out.println("Total time: " + resultTimeInSeconds + " seconds");
                System.out.println("Bandwidth: " + (double) fileByteArray.length / resultTimeInSeconds + " Bit/s");
                StoppedDownloadFileMemory.getInstance().setSaved(false);
            }
        } else {
            sendMessage(socket, inetAddress, port, "0");
        }

        String answer = getMessage(socket);
        System.out.println("The file status on server: " + answer);
    }

    private static int doUDPUploadToByteArray(DatagramSocket socket, InetAddress inetAddress, int port, byte[] fileByteArray, int startByte) throws InterruptedException, IOException {
        int current = startByte;

        socket.setSoTimeout(TIME_TO_WAIT_IN_MILLIS);

        while (fileByteArray.length != current) {
            Thread.sleep(SLEEP_TIME_IN_MILLIS);

            int bytesToSend = Math.min(fileByteArray.length - current, PACKAGE_SIZE);
            DatagramPacket packet=new DatagramPacket(fileByteArray, current, bytesToSend, inetAddress, port);
            socket.send(packet);

            try {
                socket.receive(packet);
            }catch (SocketTimeoutException e){
                break;
            }
            current += bytesToSend;
            System.out.println("Send: " + (int) ((double) current / fileByteArray.length * 100) + "%");
        }

        return current;
    }

    private static void setArrayToAnotherArray(byte[] array, byte[] insertArray, int offset, int length){

        for(int i=0; i<length; ++i){
            array[offset+i]=insertArray[i];
        }
    }
}
