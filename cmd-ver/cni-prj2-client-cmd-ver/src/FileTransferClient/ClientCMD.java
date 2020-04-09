package FileTransferClient;

import java.io.File;

public class ClientCMD {
	public static int PKGSIZE = 1024;

	public static void main(String[] args) {
		// 以下四句在eclipse中使用 方便调试 打包时去掉
		args = new String[3];
		args[1] = "129.204.152.91";
		args[2] = "9527";
		args[0] = "计算机网络（第7版）-谢希仁.pdf";

		if (args.length != 3) {
			System.out.println("调用方式应为:\n"//
					+ "args[0]:文件路径\n"//
					+ "args[1]:服务器地址\n"//
					+ "args[2]:服务器端口");
			return;
		}
		String FilePath = args[0];
		String ServerIP = args[1];
		int ServerPort;
		try {
			ServerPort = Integer.parseInt(args[2]);
			if (ServerPort < 0 || ServerPort > 65535) {
				System.out.println("端口号只能为0~65535之间的整数！");
				return;
			}
		} catch (NumberFormatException e) {
			System.out.println("端口号只能为0~65535之间的整数！");
			return;
		}

		File file = new File(FilePath);
		if (!file.exists()) {
			System.out.println("文件不存在！");
			return;
		}
		try {
			FileTransferClient client = new FileTransferClient(ServerIP, ServerPort);
			if (client != null)
				client.SendFile(FilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
