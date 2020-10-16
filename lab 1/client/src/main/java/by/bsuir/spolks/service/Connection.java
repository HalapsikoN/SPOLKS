package by.bsuir.spolks.service;

import by.bsuir.spolks.command.CommandName;
import by.bsuir.spolks.entity.DownloadStatus;
import by.bsuir.spolks.entity.StoppedCommandMemory;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Connection {

    private static final Connection instance = new Connection();

    private static final String FILE_PATH = "client\\src\\main\\resources\\files\\";
    private static final Integer PACKAGE_SIZE = 1024;
    private static final Integer TIME_TO_WAIT_IN_MILLIS = 10000;
    private static final Integer SLEEP_TIME_IN_MILLIS = 1000;

    public static Connection getInstance() {
        return instance;
    }

    public static void doTCPDownload(DataInputStream fromServer, DataOutputStream toServer, String fileName, byte[] memoryArray, int startByte) throws IOException {

        String[] answer = fromServer.readUTF().split(" ", 2);
        if (answer[0].equalsIgnoreCase("1")) {

            int fileSize = Integer.parseInt(answer[1]);
            byte[] fileByteArray = memoryArray == null ? new byte[fileSize] : memoryArray;


            long startTime = System.currentTimeMillis();
            int current = doTCPDownloadToByteArray(fromServer, toServer, fileByteArray, startByte);
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
                StoppedCommandMemory.getInstance().setSaved(false);
            } else {
                StoppedCommandMemory memory = StoppedCommandMemory.getInstance();
                memory.saveDownloadCommandInfo(CommandName.DOWNLOAD, fileName, current, fileByteArray);
                System.out.println("The file has not been gotten fully(" + current + "/" + fileSize + ")");
                System.out.println(memory);
            }
        } else {
            System.out.println("There is no such file on server");
            StoppedCommandMemory.getInstance().setSaved(false);
        }
    }

    public static void doTCPUpload(DataInputStream fromServer, DataOutputStream toServer, String fileName, int startByte) throws IOException, InterruptedException {

        File file = new File(FILE_PATH + fileName);

        if (!fileName.trim().isEmpty() && file.exists()) {
            byte[] fileByteArray = new byte[(int) file.length()];
            toServer.writeUTF("1 " + fileByteArray.length);

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                bis.read(fileByteArray, 0, fileByteArray.length);
            }

            long startTime = System.currentTimeMillis();
            int current = doTCPUploadToByteArray(fromServer, toServer, fileByteArray, startByte);
            long endTime = System.currentTimeMillis();

            if (current != fileByteArray.length) {
                StoppedCommandMemory memory = StoppedCommandMemory.getInstance();
                memory.saveDownloadCommandInfo(CommandName.UPLOAD_CONTINUE, fileName, current, fileByteArray);
                System.out.println("The file has not been send fully");
            } else {
                System.out.println("The file has been send fully. Received (" + fileByteArray.length + ") bytes");
                long resultTimeInSeconds = TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.MILLISECONDS);
                System.out.println("Total time: " + resultTimeInSeconds + " seconds");
                System.out.println("Bandwidth: " + (double) fileByteArray.length / resultTimeInSeconds + " Bit/s");
                StoppedCommandMemory.getInstance().setSaved(false);
            }
        } else {
            toServer.writeUTF("0");
        }

        String answer = fromServer.readUTF();
        System.out.println("The file status on server: " + answer);
    }


    private static int doTCPDownloadToByteArray(DataInputStream fromServer, DataOutputStream toServer, byte[] fileByteArray, int startByte) throws IOException {

        int current = startByte;

        while (fileByteArray.length != current) {

            if (!waitForBytes(fromServer)) {
                break;
            }

            int bytesToGet = Math.min(PACKAGE_SIZE, fileByteArray.length - current);
            current += fromServer.read(fileByteArray, current, bytesToGet);

            System.out.println("Downloaded: " + (int) ((double) current / fileByteArray.length * 100) + "%");

            toServer.write(1);
        }

        return current;
    }

    private static int doTCPUploadToByteArray(DataInputStream fromServer, DataOutputStream toServer, byte[] fileByteArray, int startByte) throws InterruptedException, IOException {
        int current = startByte;

        while (fileByteArray.length != current) {
            Thread.sleep(SLEEP_TIME_IN_MILLIS);

            int bytesToSend = Math.min(fileByteArray.length - current, PACKAGE_SIZE);
            toServer.write(fileByteArray, current, bytesToSend);
            toServer.flush();

            if (!waitForBytes(fromServer)) {
                break;
            }
            fromServer.skipBytes(1);
            current += bytesToSend;
            System.out.println("Send: " + (int) ((double) current / fileByteArray.length * 100) + "%");
        }

        return current;
    }

    private static boolean waitForBytes(DataInputStream fromServer) throws IOException {
        long startTime = System.currentTimeMillis();
        long emdTime = startTime;
        while (fromServer.available() <= 0) {
            emdTime = System.currentTimeMillis();
            if (emdTime - startTime >= TIME_TO_WAIT_IN_MILLIS) {
                return false;
            }
        }
        return true;
    }
}
