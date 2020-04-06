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

public class Server extends Application {

	private Label ServerStatusShowLabel;// 显示服务器状态
	private Button ServerStatusCtrlBtn;// 服务器开关
	public static TextArea LogOutputArea;// 日志输出
	boolean isServerOn = false;// 记录服务器是否开启
	FileTransferServer server;// 服务器

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
			primaryStage.setTitle("服务端");
			primaryStage.show();
			// 固定宽高
			primaryStage.setMaxHeight(primaryStage.getHeight());
			primaryStage.setMinHeight(primaryStage.getHeight());
			primaryStage.setMaxWidth(primaryStage.getWidth());
			primaryStage.setMinWidth(primaryStage.getWidth());
			primaryStage.setOnCloseRequest(e -> {
				server.StopServer();
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(Parent root) {
		// 初始化控件
		this.ServerStatusShowLabel = (Label) root.lookup("#ServerStatusShowLabel");
		this.ServerStatusCtrlBtn = (Button) root.lookup("#ServerStatusCtrlBtn");
		Server.LogOutputArea = (TextArea) root.lookup("#LogOutputArea");

		this.ServerStatusCtrlBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if (isServerOn)
					BtnStopServer();
				else
					BtnRunServer();
			}
		});

	}

	// 开服
	private void BtnRunServer() {
		try {
			Server.LogOutputArea.appendText("试图开启服务器中……\n");
			server = new FileTransferServer();
			server.RunServer();
			this.ServerStatusShowLabel.setText("服务器状态：开启");
			this.ServerStatusCtrlBtn.setText("关闭");
			Server.LogOutputArea.appendText("服务器成功开启\n");
			this.isServerOn = true;
		} catch (Exception e) {
			this.ServerStatusShowLabel.setText("服务器状态：关闭");
			this.ServerStatusCtrlBtn.setText("开启");
			Server.LogOutputArea.appendText("服务器开启失败，原因：" + e.getMessage() + "\n");
			this.isServerOn = false;
		}
	}

	// 关服
	private void BtnStopServer() {
		Server.LogOutputArea.appendText("试图关闭服务器中……\n");
		server.StopServer();
		this.ServerStatusShowLabel.setText("服务器状态：关闭");
		this.ServerStatusCtrlBtn.setText("开启");
		Server.LogOutputArea.appendText("服务器成功关闭\n");
		isServerOn = false;
	}
}