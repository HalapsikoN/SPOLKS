package by.bsuir.spolks.service;

import by.bsuir.spolks.command.tcp.CommandNameTCP;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TCPConnectionService {

    private static final TCPConnectionService instance = new TCPConnectionService();

    private static MemoryService memoryService = MemoryService.getInstance();

    private static final String FILE_PATH = "server\\src\\main\\resources\\files\\";
    private static final Integer PACKAGE_SIZE = 1024;
    private static final Integer SLEEP_TIME_IN_MILLIS = 1000;
    private static final Integer TIME_TO_WAIT_IN_MILLIS = 10000;

    public static TCPConnectionService getInstance() {
        return instance;
    }

    public static void doTCPDownload(String clientId, DataInputStream fromClient, DataOutputStream toClient, String fileName, int startByte) throws IOException, InterruptedException {

        File file = new File(FILE_PATH + fileName);

        if (!fileName.trim().isEmpty() && file.exists()) {
            byte[] fileByteArray = new byte[(int) file.length()];
            toClient.writeUTF("1 " + fileByteArray.length);

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                bis.read(fileByteArray, 0, fileByteArray.length);
            }

            int current = doTCPDownloadFromByteArray(fromClient, toClient, fileByteArray, startByte);

            if (fileByteArray.length != current) {
                memoryService.saveDownloadCommandInfo(clientId, CommandNameTCP.DOWNLOAD, fileName, current, fileByteArray);
            }

        } else {
            toClient.writeUTF("0");
        }
    }

    public static void doTCPUpload(String clientId, DataInputStream fromClient, DataOutputStream toClient, String filename, byte[] memoryArray, int startByte) throws IOException {

        String[] answer = fromClient.readUTF().split(" ", 2);
        if (answer[0].equalsIgnoreCase("1")) {

            int fileSize = Integer.parseInt(answer[1]);
            byte[] fileByteArray = memoryArray == null ? new byte[fileSize] : memoryArray;

            int current = doTCPUploadToByteArray(fromClient, toClient, fileByteArray, startByte);

            if (current == fileSize) {
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(FILE_PATH + filename)))) {
                    bos.write(fileByteArray, 0, fileSize);
                    bos.flush();
                }

                toClient.writeUTF("The file has been gotten fully");
            } else {
                memoryService.saveDownloadCommandInfo(clientId, CommandNameTCP.UPLOAD, filename, current, fileByteArray);
                toClient.writeUTF("The file has not been gotten fully");
            }
        } else {
            toClient.writeUTF("There is no such file on client");
        }
    }

    private static int doTCPDownloadFromByteArray(DataInputStream fromClient, DataOutputStream toClient, byte[] fileByteArray, int startByte) throws InterruptedException, IOException {

        int current = startByte;

        while (fileByteArray.length != current) {
            Thread.sleep(SLEEP_TIME_IN_MILLIS);

            int bytesToSend = Math.min(fileByteArray.length - current, PACKAGE_SIZE);
            toClient.write(fileByteArray, current, bytesToSend);
            toClient.flush();

            if (!waitForBytes(fromClient)) {
                break;
            }
            fromClient.skipBytes(1);
            current += bytesToSend;
            System.out.println(current);
        }

        return current;
    }

    private static int doTCPUploadToByteArray(DataInputStream fromClient, DataOutputStream toClient, byte[] fileByteArray, int startByte) throws IOException {

        int current = startByte;

        while (fileByteArray.length != current) {

            if (!waitForBytes(fromClient)) {
                break;
            }

            int bytesToGet = Math.min(PACKAGE_SIZE, fileByteArray.length - current);
            current += fromClient.read(fileByteArray, current, bytesToGet);

            toClient.write(1);
        }

        return current;
    }

    private static boolean waitForBytes(DataInputStream fromClient) throws IOException {
        long startTime = System.currentTimeMillis();
        long endTime = startTime;
        while (fromClient.available() <= 0) {
            endTime = System.currentTimeMillis();
            if (endTime - startTime >= TIME_TO_WAIT_IN_MILLIS) {
                System.out.println("OUT OF TIME WAITING (" + Thread.currentThread().getId() + ")");
                return false;
            }
        }
        return true;
    }
}
