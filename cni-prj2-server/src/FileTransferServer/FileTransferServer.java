package FileTransferServer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.concurrent.Task;

//服务器Socket类
public class FileTransferServer extends ServerSocket {
	private static final int ServerPort = 9527;// 端口号
	RECVTaskPool taskpool;// 这玩意循环接收用

	public FileTransferServer() throws IOException {
		super(ServerPort);
		taskpool = new RECVTaskPool(this);
	}

	// 启动服务器
	public void RunServer() throws Exception {
		new Thread(taskpool).start();
	}

	// 关闭服务器
	public void StopServer() {
		taskpool.cancel();
		if (!this.isClosed())
			try {
				this.close();
			} catch (IOException e) {
			}
	}
}

//一次接收的任务(本应该用Thread，这里为了UI使用了JavaFX中的Task，下同)
class RECVTask extends Task<Void> {
	private Socket socket;// 套接字
	private DataInputStream InputStream;// 从数据流输入
	private FileOutputStream OutputStream;// 对文件输出

	public RECVTask(Socket socket) {
		this.socket = socket;
	}

	@Override
	public Void call() {
		try {
			// 初始化流
			InputStream = new DataInputStream(socket.getInputStream());
			String fileName = InputStream.readUTF();
			File file = new File(fileName);
			OutputStream = new FileOutputStream(file);

			// 开始接收文件
			byte[] bytes = new byte[1024];
			int length = 0;
			while ((length = InputStream.read(bytes, 0, bytes.length)) != -1) {
				OutputStream.write(bytes, 0, length);
				OutputStream.flush();
			}

			// 文件接收成功
			Server.LogOutputArea.appendText("文件接收成功 文件名：" + fileName + "\n");
//			System.out.println("文件接收成功 文件名：" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流和套接字
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
	private FileTransferServer server;//服务器套接字

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
				//在每次关闭服务器套接字时一定会catch一次异常
				//(因为server.accept()被中断)
			}
		}
		return null;
	}
}
