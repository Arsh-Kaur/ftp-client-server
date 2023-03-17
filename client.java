package project1;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class client {
	
	public static Integer portNumber;
	
    public static void main(String[] args) throws Exception {
        
        System.out.println("========================================");
        System.out.println("To connect to a server at port, enter 'ftpclient <port>' ");
        
        while(true) {
            System.out.println("\nEnter the command for server: ");
            System.out.println("1. ftpclient <port number>\n2. get <filename>\n3. upload <filename>\n4. exit\n");
            Scanner sc = new Scanner(System.in);
            String inputCommand = sc.nextLine();
            if(inputCommand.equals("exit")) break;

            String[] command = inputCommand.split(" ");
            
            //For any command not in our scope
            if(command.length != 2) {
                System.out.println("\nPlease enter a valid input command.");
                continue;
            }
            
            String commandVal = command[0];
            String fileOrPort = command[1];

            /* ==============================================
                get         - to fetch file from the server
                upload      - to upload file in the server
                ftpclient   - to connect to server port
            ============================================== */

            if(commandVal.equals("ftpclient")) portNumber = Integer.valueOf(fileOrPort);
            if(portNumber == null){
                System.out.println("\n\nPlease enter the command with correct port number again.");
                continue;
            }

            try {

                //Create connection
                Socket clientSocket = new Socket("localhost", portNumber);
                DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                

                if(commandVal.equals("ftpclient")) {
                    //Send connection request to server
                    outputStream.writeUTF("ftpclient " + String.valueOf(portNumber));
                    String response = inputStream.readUTF();

                    //Check for server responses
                    if(response.equals("SERVER OK")) 
                        System.out.println("\nConnected to server on port " + portNumber);
                    else if(response.equals("INCORRECT PORT")) 
                        System.out.println("\nInvalid port no " + portNumber + "\nPlease enter a valid port number");
                    else 
                        System.out.println("\nError connecting to server");
                    continue;
                } 

                else if (commandVal.equals("get")) {
                    outputStream.writeUTF("get " + fileOrPort);
                    String response = inputStream.readUTF();

                    //If the server is ready to send the file 
                    if (response.startsWith("FILE_OK")) {
                        long file_size = Long.parseLong(response.split(" ")[1]);
 
                        //in each pass, the code reads 1024 bytes to the outputFile
                        FileOutputStream outputFile = new FileOutputStream("new"+fileOrPort);
                        byte[] byteArray = new byte[1024];
                        int inputBytes;
                        while (file_size > 0 && (inputBytes = inputStream.read(byteArray, 0, (int) Math.min(byteArray.length, file_size))) != -1) {
                            outputFile.write(byteArray, 0, inputBytes);
                            file_size -= inputBytes;
                        }
                        outputFile.close();
                        System.out.println("\nThe file has been downloaded successfully!");
                    } 
                    else if(response.equals("GET ERROR")) {
                        System.out.println("\nThe server was unable to locate the file " + fileOrPort);
                    }
                    else {
                        System.out.println("\nUnable to get file "+ fileOrPort);
                    }

                } 

                else if (commandVal.equals("upload")) {
                    File file = new File(fileOrPort);

                    //Send request to upload file onlyif file exists
                    //else returns error code
                    if (file.exists() && !file.isDirectory()) {
                        outputStream.writeUTF("upload " + fileOrPort);
                        String response = inputStream.readUTF();

                        if (response.equals("UPLOAD OK")) {
                                FileInputStream inputFile = new FileInputStream(file);
                                byte[] byteArr = new byte[1024];
                                int inputBytes;
                                while ((inputBytes = inputFile.read(byteArr)) > 0) {
                                    outputStream.write(byteArr, 0, inputBytes);
                                }
                                inputFile.close();
                                System.out.println("\nThe file has been uploaded successfully!");
                        }
                    }
                    else {
                        outputStream.writeUTF("file_not_found");
                        System.out.println("\nThe file could not be located.");
                    }
                } 
                
                //If no valid command given
                else {
                    System.out.println("\nPlease enter a valid input command.");
                    continue;
                }

                clientSocket.close();
            }
            //If client sends a request but the server has stopped or connection lost in between 
            catch(ConnectException ce) {
                System.out.println("\nUnable to connect to server " + ce.getMessage() );
                continue;
            }	
            catch(Exception e) {
                System.out.println("\n\nException " + e.getMessage() + " occured during program run.");
                break;
            }
        }
    }
}