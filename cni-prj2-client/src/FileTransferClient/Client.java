package FileTransferClient;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Client extends Application {
	private Stage stage;

	public static int PKGSIZE = 1024;
	private TextField ServerIPInput;// 服务器IP
	private TextField ServerPortInput;// 服务器端口
	private TextField FilePathInput;// 文件路径
	private Button UploadBtn;// 上传按钮
	public static ProgressBar FileUploadProgressBar;// 进度条

	public static void main(String[] args) throws IOException {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MainWin.fxml"));
			init(root);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("客户端");
			primaryStage.show();
			// 固定宽高
			primaryStage.setMaxHeight(primaryStage.getHeight());
			primaryStage.setMinHeight(primaryStage.getHeight());
			primaryStage.setMaxWidth(primaryStage.getWidth());
			primaryStage.setMinWidth(primaryStage.getWidth());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(Parent root) {
		// 初始化控件
		ServerIPInput = (TextField) root.lookup("#ServerIPInput");
		ServerPortInput = (TextField) root.lookup("#ServerPortInput");
		FilePathInput = (TextField) root.lookup("#FilePathInput");
		UploadBtn = (Button) root.lookup("#UploadBtn");
		FileUploadProgressBar = (ProgressBar) root.lookup("#FileUploadProgressBar");

		UploadBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				SendFileBtn();
			}
		});
	}

	private FileTransferClient ConnServer() {
		try {
			int ServerPort = -1;
			try {
				ServerPort = Integer.parseInt(this.ServerPortInput.getText());
			} catch (NumberFormatException e) {
				SummonAlertDialog("输入错误", "端口必须为0~65535间的整数！");
				return null;
			}
			if (ServerPort < 0 || ServerPort > 65535) {
				SummonAlertDialog("输入错误", "端口必须为0~65535间的整数！");
				return null;
			}
			try {
				return new FileTransferClient(ServerIPInput.getText(), ServerPort);
			} catch (ConnectException e) {
				SummonAlertDialog("连接错误", "连接服务器失败！");
				return null;
			} catch (UnknownHostException e) {
				SummonAlertDialog("连接错误", "地址格式错误！");
				return null;
			}
		} catch (Exception e) {
		}
		return null;
	}

	private void SendFileBtn() {
		File file = new File(FilePathInput.getText());
		if (!file.exists()) {
			SummonAlertDialog("读取错误", "文件不存在！");
			return;
		}
		try {
			FileTransferClient client = ConnServer();
			if (client != null)
				client.SendFile(FilePathInput.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void SummonAlertDialog(String Header, String Msg) {
		Alert _alert = new Alert(Alert.AlertType.WARNING);
		_alert.setTitle("错误");
		_alert.setHeaderText(Header);
		_alert.setContentText(Msg);
		_alert.initOwner(stage);
		_alert.show();
	}
}