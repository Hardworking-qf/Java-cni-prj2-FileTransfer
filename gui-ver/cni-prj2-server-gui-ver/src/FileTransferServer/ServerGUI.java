package FileTransferServer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ServerGUI extends Application {
	private Label ServerStatusShowLabel;// ��ʾ������״̬
	private Button ServerStatusCtrlBtn;// ����������
	public static TextArea LogOutputArea;// ��־���
	boolean isServerOn = false;// ��¼�������Ƿ���
	FileTransferServer server;// ������
	public static int PKGSIZE=1024;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MainWin.fxml"));
			init(root);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("�����");
			primaryStage.show();
			// �̶����
			primaryStage.setMaxHeight(primaryStage.getHeight());
			primaryStage.setMinHeight(primaryStage.getHeight());
			primaryStage.setMaxWidth(primaryStage.getWidth());
			primaryStage.setMinWidth(primaryStage.getWidth());
			primaryStage.setOnCloseRequest(e -> {
				try {
					server.StopServer();
				} catch (Exception ex) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(Parent root) {
		// ��ʼ���ؼ�
		this.ServerStatusShowLabel = (Label) root.lookup("#ServerStatusShowLabel");
		this.ServerStatusCtrlBtn = (Button) root.lookup("#ServerStatusCtrlBtn");
		ServerGUI.LogOutputArea = (TextArea) root.lookup("#LogOutputArea");

		this.ServerStatusCtrlBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if (isServerOn)
					BtnStopServer();
				else
					BtnRunServer();
			}
		});

	}

	// ����
	private void BtnRunServer() {
		try {
			ServerGUI.LogOutputArea.clear();
			ServerGUI.LogOutputArea.appendText("��ͼ�����������С���\n");
			server = new FileTransferServer();
			server.RunServer();
			this.ServerStatusShowLabel.setText("������״̬������");
			this.ServerStatusCtrlBtn.setText("�رշ�����");
			ServerGUI.LogOutputArea.appendText("�������ɹ�����\n");
			this.isServerOn = true;
		} catch (Exception e) {
			this.ServerStatusShowLabel.setText("������״̬���ر�");
			this.ServerStatusCtrlBtn.setText("����������");
			ServerGUI.LogOutputArea.appendText("����������ʧ�ܣ�ԭ��" + e.getMessage() + "\n");
			this.isServerOn = false;
		}
	}

	// �ط�
	private void BtnStopServer() {
		ServerGUI.LogOutputArea.appendText("��ͼ�رշ������С���\n");
		server.StopServer();
		this.ServerStatusShowLabel.setText("������״̬���ر�");
		this.ServerStatusCtrlBtn.setText("����������");
		ServerGUI.LogOutputArea.appendText("�������ɹ��ر�\n");
		isServerOn = false;
	}
}