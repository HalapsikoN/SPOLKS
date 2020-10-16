package by.bsuir.spolks.util;

import by.bsuir.spolks.command.CommandName;
import by.bsuir.spolks.entity.StoppedCommandMemory;
import by.bsuir.spolks.service.Connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadContinueService {

    private static final String FILE_PATH = "client\\src\\main\\resources\\files\\";
    private static final Integer PACKAGE_SIZE = 1024;
    private static final Long TIME_TO_WAIT_MILLIS = 20000L;

    private static final StoppedCommandMemory memory = StoppedCommandMemory.getInstance();

    public static void checkUnfinishedCommand(DataInputStream fromServer, DataOutputStream toServer) throws IOException {

        if (memory.isSaved()) {
            switch (memory.getCommandName()) {
                case DOWNLOAD:
                    continueDownload(fromServer, toServer);
                    break;
                case UPLOAD_CONTINUE:
                    continueUpload(fromServer, toServer);
                    break;
                default:
                    break;
            }
        }
    }

    private static void continueDownload(DataInputStream fromServer, DataOutputStream toServer) throws IOException {

        toServer.writeUTF(CommandName.DOWNLOAD_CONTINUE.name() + " " + memory.getFileName()+ " "+memory.getDownloadedBytes());

        Connection.doTCPDownload(fromServer, toServer, memory.getFileName(), memory.getFileByteArray(), memory.getDownloadedBytes());
    }

    private static void continueUpload(DataInputStream fromServer, DataOutputStream toServer) throws IOException {

        toServer.writeUTF(CommandName.UPLOAD_CONTINUE.name() + " " + memory.getFileName() + " " + memory.getDownloadedBytes());

        try {
            Connection.doTCPUpload(fromServer, toServer, memory.getFileName(), memory.getDownloadedBytes());
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
//        File file = new File(FILE_PATH + memory.getFileName());
//        if (file.exists()) {
//            byte[] fileByteArray = memory.getFileByteArray();
//            toServer.writeUTF("1 " + fileByteArray.length);
//
//            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
//                bis.read(fileByteArray, 0, fileByteArray.length);
//
//                int current = memory.getDownloadedBytes();
//                while (fileByteArray.length != current) {
//                    toServer.write(fileByteArray, current, Math.min(fileByteArray.length - current, PACKAGE_SIZE));
//                    toServer.flush();
//                    long startTime = System.currentTimeMillis();
//                    long endTime = System.currentTimeMillis();
//
//                    while (fromServer.available() <= 0) {
//                        endTime = System.currentTimeMillis();
//                        if (endTime - startTime >= TIME_TO_WAIT_MILLIS) {
//                            break;
//                        }
//                    }
//
//                    if (endTime - startTime >= TIME_TO_WAIT_MILLIS) {
//                        break;
//                    }
//
//                    fromServer.skipBytes(1);
//                    current += Math.min(fileByteArray.length - current, PACKAGE_SIZE);
//                }
//
//                if (current != fileByteArray.length) {
//                    StoppedCommandMemory memory = StoppedCommandMemory.getInstance();
//                    memory.saveDownloadCommandInfo(CommandName.UPLOAD_CONTINUE, memory.getFileName(), current, fileByteArray);
//                    System.out.println("The file has not been send fully");
//                }
//            }
//        } else {
//            toServer.writeUTF("0");
//        }
//
//        String answer = fromServer.readUTF();
//        System.out.println("The file status on server: " + answer);
    }
}
