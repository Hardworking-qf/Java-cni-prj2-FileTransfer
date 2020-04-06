package FileTransferClient;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;

class FileTransferClient extends Socket {
	@SuppressWarnings("unused")
	private static String ServerIP;

	@SuppressWarnings("unused")
	private static int ServerPort;

	// private Socket ClientSocket;
	// private FileInputStream InputStream;
	// private DataOutputStream OutputStream;

	@SuppressWarnings("static-access")
	public FileTransferClient(String ServerIP, int ServerPort) throws Exception {
		super(ServerIP, ServerPort);
		this.ServerIP = ServerIP;
		this.ServerPort = ServerPort;
		// this.ClientSocket = this;
		System.out.println("成功连接服务器，端口号：" + this.getLocalPort());
	}

	public void SendFile(String path) throws IOException {
		new Thread(new UploadFileTask(this, path)).start();
	}
}

class UploadFileTask extends Task<Void> {
	private Socket socket;
	private String path;

	UploadFileTask(Socket socket, String path) {
		this.socket = socket;
		this.path = path;
	}

	@Override
	protected Void call() throws Exception {
		FileInputStream InputStream = null;
		DataOutputStream OutputStream = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				long size = file.length();
				long sentsize = 0;
				InputStream = new FileInputStream(file);
				OutputStream = new DataOutputStream(socket.getOutputStream());
				OutputStream.writeUTF(file.getName());
				OutputStream.flush();
				System.out.println("开始传输文件");
				byte[] bytes = new byte[Client.PKGSIZE];
				int length = 0;
				while ((length = InputStream.read(bytes, 0, bytes.length)) != -1) {
					OutputStream.write(bytes, 0, length);
					OutputStream.flush();
					sentsize += bytes.length;
					Client.FileUploadProgressBar.setProgress(sentsize / (double)size);
				}
				System.out.println();
				System.out.println("文件传输成功");
				Client.FileUploadProgressBar.setProgress(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (InputStream != null)
				InputStream.close();
			if (OutputStream != null)
				OutputStream.close();
			socket.close();
		}
		return null;
	}
}
