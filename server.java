package project1;

import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) throws Exception {

        //initiate server port number
        int port_number = 1103;
        ServerSocket socket = new ServerSocket(port_number);
        System.out.println("========================================");
        System.out.println("\nThe server has been started at Port Number: " + port_number);
        System.out.println("========================================");
        while (true) {
            Socket serverSocket = socket.accept();
            DataInputStream inputStream = new DataInputStream(serverSocket.getInputStream());
            String inputData = inputStream.readUTF();

            /* ==============================================
                get         - to fetch file from the server
                upload      - to save file in the server
                ftpclient   - to check if the port number is correct
            ============================================== */       

            if (inputData.startsWith("get")) {
                String fileName = inputData.split(" ")[1];
                File file = new File(fileName);

                //Returns file if exists
                //else returns error code
                if (file.exists() && !file.isDirectory()) {
                    
                    //Inform the client that the file is ready to be sent
                    DataOutputStream outputStream = new DataOutputStream(serverSocket.getOutputStream());
                    outputStream.writeUTF("FILE_OK " + file.length());

                    //byteArr is an array with a length of 1024, 
                    //in each pass, the code reads 1024 bytes from the inputFile
                    FileInputStream inputFile = new FileInputStream(file);
                    byte[] byteArr = new byte[1024];
                    int inputBytes;

                    while ((inputBytes = inputFile.read(byteArr)) > 0) {
                        outputStream.write(byteArr, 0, inputBytes);
                    }

                    inputFile.close();

                } else {
                    //if file not found
                    DataOutputStream outputStream = new DataOutputStream(serverSocket.getOutputStream());
                    outputStream.writeUTF("GET ERROR");
                }

            }
            
            else if (inputData.startsWith("upload")) {

                //Inform the client that the file is ready to be uploaded
                String fileName = inputData.split(" ")[1];
                DataOutputStream outputStream = new DataOutputStream(serverSocket.getOutputStream());
                outputStream.writeUTF("UPLOAD OK");

                //in each pass, the code writes 1024 bytes from the outputFile
                FileOutputStream outputFile = new FileOutputStream(new File("new"+fileName));
                byte[] byteArr = new byte[1024];
                int inputBytes;
                while ((inputBytes = inputStream.read(byteArr)) > 0) {
                    outputFile.write(byteArr, 0, inputBytes);
                }

                outputFile.close();
                
            } 
            
            else if (inputData.startsWith("ftpclient")){

                //Check if the port is correct else throw error
                String portNumber = inputData.split(" ")[1];
                DataOutputStream outputStream = new DataOutputStream(serverSocket.getOutputStream());

                if(!portNumber.equals(String.valueOf(port_number))) {
                    outputStream.writeUTF("INCORRECT PORT");
                } 
                else{
                    outputStream.writeUTF("SERVER OK");
                    System.out.println("\n\nListening to client..");
                }    

            }

            serverSocket.close();

        }
    }
}