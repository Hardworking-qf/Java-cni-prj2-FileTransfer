package FileTransferServer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.concurrent.Task;

//������Socket��
class FileTransferServer extends ServerSocket {
	private static final int ServerPort = 9527;// �˿ں�
	RECVTaskPool taskpool;// ѭ�����ý���

	public FileTransferServer() throws IOException {
		super(ServerPort);
		taskpool = new RECVTaskPool(this);
	}

	// ����������
	public void RunServer() throws Exception {
		new Thread(taskpool).start();
	}

	// �رշ�����
	public void StopServer() {
		taskpool.cancel();
		if (!this.isClosed())
			try {
				this.close();
			} catch (IOException e) {
				// ��������ʲôBUG ����Ҫ�� ΢Ц������� ����BUG����÷����������BUG catch������ ��������
			}
	}
}

//һ�ν��յ�����(��Ӧ����Thread������Ϊ��UIʹ����JavaFX�е�Task����ͬ)
class RECVTask extends Task<Void> {
	private Socket socket;// �׽���
	private DataInputStream InputStream;// ������������
	private FileOutputStream OutputStream;// ���ļ����

	public RECVTask(Socket socket) {
		this.socket = socket;
	}

	@Override
	public Void call() {
		try {
			// ��ʼ����
			InputStream = new DataInputStream(socket.getInputStream());
			String fileName = InputStream.readUTF();
			File file = new File(fileName);
			OutputStream = new FileOutputStream(file);

			// ��ʼ�����ļ�
			byte[] bytes = new byte[ServerGUI.PKGSIZE];
			int length = 0;
			while ((length = InputStream.read(bytes, 0, bytes.length)) != -1) {
				OutputStream.write(bytes, 0, length);
				OutputStream.flush();
			}

			// �ļ����ճɹ�
			ServerGUI.LogOutputArea.appendText("�ļ����ճɹ� �ļ�����" + fileName + "\n");
//			System.out.println("�ļ����ճɹ� �ļ�����" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// �ر������׽���
				if (OutputStream != null)
					OutputStream.close();
				if (InputStream != null)
					InputStream.close();
				socket.close();
			} catch (Exception e) {
			}
		}
		return null;
	}
}

class RECVTaskPool extends Task<Void> {
	private FileTransferServer server;// �������׽���

	public RECVTaskPool(FileTransferServer server) {
		this.server = server;
	}

	@Override
	public Void call() {
		while (!this.isCancelled()) {
			try {
				Socket socket;
				socket = server.accept();
				new RECVTask(socket).call();
			} catch (IOException e) {
				// ��ÿ�ιرշ������׽���ʱһ����catchһ���쳣
				// (��Ϊserver.accept()���ж�)
			}
		}
		return null;
	}
}