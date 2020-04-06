package FileTransferClient;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

class FileTransferClient extends Socket {
	private static final String ServerIP = "localhost";// "129.204.152.91";//���Ǳ��ˣ�3170������Ѷ��
	private static final int ServerPort = 9527;
	private Socket ClientSocket;
	private FileInputStream InputStream;
	private DataOutputStream OutputStream;

	public FileTransferClient() throws Exception {
		super(ServerIP, ServerPort);
		this.ClientSocket = this;
		System.out.println("�ɹ����ӷ��������˿ںţ�" + this.getLocalPort());
	}

	public void SendFile(String path) throws Exception {
		try {
			File file = new File(path);
			if (file.exists()) {
				InputStream = new FileInputStream(file);
				OutputStream = new DataOutputStream(ClientSocket.getOutputStream());
				OutputStream.writeUTF(file.getName());
				OutputStream.flush();
				System.out.println("��ʼ�����ļ�");
				byte[] bytes = new byte[1024];
				int length = 0;
				while ((length = InputStream.read(bytes, 0, bytes.length)) != -1) {
					OutputStream.write(bytes, 0, length);
					OutputStream.flush();
				}
				System.out.println();
				System.out.println("�ļ�����ɹ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (InputStream != null)
				InputStream.close();
			if (OutputStream != null)
				OutputStream.close();
			ClientSocket.close();
		}
	}
}
