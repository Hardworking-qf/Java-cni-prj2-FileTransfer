package FileTransferClient;
import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
    	try {  
            @SuppressWarnings("resource")
			FileTransferClient client = new FileTransferClient();
            client.SendFile("1.txt");
        } catch (Exception e) {  
            System.out.println("ÎÄ¼þ´«ÊäÊ§°Ü");
        }  
    }
}