# ftp-client-server
Client Server Model using File Transfer Protocol

This is an implementation of a simple version of ftp client/server software. It consists of two programs - client and server.
Once the server is started at the computer, it starts listening to the TCP port, which will be given as input.
The client is executed in the same computer and the server's port number is given in the command line.
The client can perform two operations:
* upload file to the server
* download file from the server

In this project, we are allowing all the files to be saved in the same directory, thus a prefix is added to the filename anytime the file is downloaded or uploaded. 
As the file size can be very large, we break them into smaller segments and send each segment at a time.

Instructions:
1)	Unzip the files to local directory
2)	Open the command prompt and go to the local directory where project is unzipped.
3)	Start the server using the following command:<br>
      java server.java <br>
    The server will be started at localhost <br>
    ![image](https://user-images.githubusercontent.com/29150866/225817528-d959f62c-7bee-4a19-ade2-720a729bd69b.png)<br>
4)	Open a new command prompt and navigate to local directory
5)	Start the client using the following command:
      java client.java
      
Commands at client end - 
* ftpclient <portNumber> - The client will be started and connected to server once we run the command.
* get <fileName> - this commands gets the file from the server and downloads to local directory with new name.
* upload <fileName> - this command uploads the file to the server from the local directory with new name     

file in repository - <br>
![image](https://user-images.githubusercontent.com/29150866/225817323-b12a4cad-cb66-48b8-9606-ba81ea4722a2.png) <br>
After upload and download - <br>
![image](https://user-images.githubusercontent.com/29150866/225817359-55738f20-e119-4a9e-a2ad-66e7feca7b32.png)
