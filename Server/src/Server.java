public class Server {
	public static void main(String[] args) {
		try {
			FileTransferServer server = new FileTransferServer(); // ���������
			server.RunServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}