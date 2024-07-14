package id.go.bpjskesehatan.inspired.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.multipart.MultipartFile;

public class FTPUploader {
	
	public static boolean uploadFile(FTPClient ftpClient, String pathFile, String namaFile, MultipartFile input) throws IOException {
		boolean upload = false;
	    
	    boolean existed = checkDirectoryExistsFtp(ftpClient, pathFile);
	        if(existed) {
	          ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	          upload = ftpClient.storeFile(pathFile +"/"+ namaFile, input.getInputStream());
	          FTPUploader.showServerReply(ftpClient);
	        }
	    return upload;
	  	}
	  
	public void deleteFile(FTPClient ftpClient, String pathFile) {
	    try {
	      ftpClient.deleteFile(pathFile);
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
	    
	public static boolean checkDirectoryExistsFtp(FTPClient ftpClient, String dirPath) throws IOException {
	      boolean res = false;
	      ftpClient.changeWorkingDirectory(dirPath);
	      int returnCode = ftpClient.getReplyCode();
	        if (returnCode == 550) {
	          res = ftpClient.makeDirectory(dirPath);
	        } else {
	          ftpClient.changeWorkingDirectory("/");
	          res = true;
	        }
	      return res;
	 }
	    
	public static boolean checkFileExistsFtp(FTPClient ftpClient, String filePath) throws IOException {
	        InputStream inputStream = ftpClient.retrieveFileStream(filePath);
	        int returnCode = ftpClient.getReplyCode();
	        if (inputStream == null || returnCode == 550) {
	            return false;
	        }
	        return true;
	    }
	    
	public static boolean makeDirectories(FTPClient ftpClient, String dirPath) throws IOException {
        String[] pathElements = dirPath.split("/");
        if (pathElements != null && pathElements.length > 0) {
            for (String singleDir : pathElements) {
                boolean existed = ftpClient.changeWorkingDirectory(singleDir);
                if (!existed) {
                    boolean created = ftpClient.makeDirectory(singleDir);
                    if (created) {
                        System.out.println("CREATED directory: " + singleDir);
                        ftpClient.changeWorkingDirectory(singleDir);
                    } else {
                        System.out.println("COULD NOT create directory: " + singleDir);
                        return false;
                    }
                }
            }
        }
        return true;
     }
	
	public static void disconnect(FTPClient ftpClient){
	    if (ftpClient.isConnected()) {
	      try {
	        ftpClient.logout();
	        ftpClient.disconnect();
	      } catch (IOException f) {
	      }
	    }
	 }
	  
	public static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
	 }
}