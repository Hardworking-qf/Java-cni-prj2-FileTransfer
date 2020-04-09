package FileTransferClient;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

class FileTransferClient extends Socket {
	@SuppressWarnings("unused")
	private static String ServerIP;

	@SuppressWarnings("unused")
	private static int ServerPort;

//		private Socket ClientSocket;
//		private FileInputStream InputStream;
//		private DataOutputStream OutputStream;

	@SuppressWarnings("static-access")
	public FileTransferClient(String ServerIP, int ServerPort) throws Exception {
		super(ServerIP, ServerPort);
		this.ServerIP = ServerIP;
		this.ServerPort = ServerPort;
//			this.ClientSocket = this;
		System.out.println("成功连接服务器，端口号：" + this.getLocalPort());
	}

	public void SendFile(String path) throws IOException {
		FileInputStream InputStream = null;
		DataOutputStream OutputStream = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				long size = file.length();
				long sentsize = 0;
				long StartTime = System.currentTimeMillis();

				InputStream = new FileInputStream(file);
				OutputStream = new DataOutputStream(this.getOutputStream());
				OutputStream.writeUTF(file.getName());
				OutputStream.flush();
				System.out.println("开始传输文件");
				System.out.println();
				byte[] bytes = new byte[ClientCMD.PKGSIZE];
				int length = 0;
				while ((length = InputStream.read(bytes, 0, bytes.length)) != -1) {
					OutputStream.write(bytes, 0, length);
					OutputStream.flush();
					//sentsize += bytes.length;
					//printProgressBar((double) sentsize / size);
					//System.out.printf("\t速度：%.5fMB/s",(double) sentsize / 1024 / 1024 / (System.currentTimeMillis() - StartTime) * 1000);
				}
				System.out.println();
				System.out.println("文件传输成功");
				System.out.println("平均速度："
						+ (double) size / 1024 / 1024 / (System.currentTimeMillis() - StartTime) * 1000 + "MB/s");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (InputStream != null)
				InputStream.close();
			if (OutputStream != null)
				OutputStream.close();
			this.close();
		}
	}
	private void printProgressBar(double progress) {
		System.out.print("\r");
		int percent = (int) (progress * 100);
		int permill = (int) (progress * 1000) % 10;
		for (int i = 0; i < percent; ++i)
			System.out.print("=");
		if (percent != 100)
			System.out.print(permill);
		for (int i = percent + 1; i < 99; ++i)
			System.out.print("-");
	}
}