package FileTransferClient;

import java.io.File;

public class ClientCMD {
	public static int PKGSIZE = 1024;

	public static void main(String[] args) {
		// �����ľ���eclipse��ʹ�� ������� ���ʱȥ��
		args = new String[3];
		args[1] = "129.204.152.91";
		args[2] = "9527";
		args[0] = "��������磨��7�棩-лϣ��.pdf";

		if (args.length != 3) {
			System.out.println("���÷�ʽӦΪ:\n"//
					+ "args[0]:�ļ�·��\n"//
					+ "args[1]:��������ַ\n"//
					+ "args[2]:�������˿�");
			return;
		}
		String FilePath = args[0];
		String ServerIP = args[1];
		int ServerPort;
		try {
			ServerPort = Integer.parseInt(args[2]);
			if (ServerPort < 0 || ServerPort > 65535) {
				System.out.println("�˿ں�ֻ��Ϊ0~65535֮���������");
				return;
			}
		} catch (NumberFormatException e) {
			System.out.println("�˿ں�ֻ��Ϊ0~65535֮���������");
			return;
		}

		File file = new File(FilePath);
		if (!file.exists()) {
			System.out.println("�ļ������ڣ�");
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
