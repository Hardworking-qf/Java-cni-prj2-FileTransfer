package FileTransferClient;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

class FileTransferClient extends Socket {
	@SuppressWarnings("unused")
	private static String ServerIP;

	@SuppressWarnings("unused")
	private static int ServerPort;

//	private Socket ClientSocket;
//	private FileInputStream InputStream;
//	private DataOutputStream OutputStream;

	@SuppressWarnings("static-access")
	public FileTransferClient(String ServerIP, int ServerPort) throws Exception {
		super(ServerIP, ServerPort);
		this.ServerIP = ServerIP;
		this.ServerPort = ServerPort;
//		this.ClientSocket = this;
//		System.out.println("�ɹ����ӷ��������˿ںţ�" + this.getLocalPort());
	}

	public void SendFile(String path) throws IOException {
		ClientGUI.UploadBtn.setDisable(true);
		ClientGUI.UploadBtn.setText("�ϴ���");
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
				long size = ClientGUI.filesize;
				long sentsize = 0;
				long StartTime = System.currentTimeMillis();

				InputStream = new FileInputStream(file);
				OutputStream = new DataOutputStream(socket.getOutputStream());
				OutputStream.writeUTF(file.getName());
				OutputStream.flush();
//				System.out.println("��ʼ�����ļ�");
				byte[] bytes = new byte[ClientGUI.PKGSIZE];
				int length = 0;
				while ((length = InputStream.read(bytes, 0, bytes.length)) != -1) {
					OutputStream.write(bytes, 0, length);
					OutputStream.flush();
					sentsize += bytes.length;
					ClientGUI.FileUploadProgressBar.setProgress(sentsize / (double) size);
				}
//				System.out.println();
//				System.out.println("�ļ�����ɹ�");
				ClientGUI.UploadBtn.setDisable(false);
				Platform.runLater(new Runnable() {
				    @Override
				    public void run() {
						ClientGUI.UploadBtn.setText("�ϴ�");
				    }
				});
				ClientGUI.FileUploadProgressBar.setProgress(0);
//				System.out.println("ƽ���ٶȣ�"
//						+ (double) sentsize / 1024 / 1024 / (System.currentTimeMillis() - StartTime) * 1000 + "MB/s");
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Alert _alert = new Alert(Alert.AlertType.INFORMATION);
						_alert.setTitle("�������");
						_alert.setHeaderText("�ļ��ϴ����");
						_alert.setContentText("ƽ���ٶȣ�"
								+ (double) ClientGUI.filesize / 1024 / 1024 / (System.currentTimeMillis() - StartTime) * 1000 + "MB/s");
						_alert.show();
					}
				});
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
