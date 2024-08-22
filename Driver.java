import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Driver extends Application {

	private BorderPane pane;
	private ScrollPane sc;
	private StackPane b;
	private Button start;
	private Button getFileButton;
	private HBox steps;
	private Button next;
	private Memory m;
	private LinkedList<Object> memory;
	private static int jobsize = 0;
	LinkedList<PCB> ready;
	LinkedList<PCB> job;
	LinkedList<PCB> finish;
	File readyfile;
	File jobfile;

	VBox jobQueue;
	VBox finishQueue;

	@Override
	public void start(Stage stage) throws Exception {
		creat_buttons();
		pane = new BorderPane();
		pane.setStyle("-fx-background-color: #FFF5EE;");
		ready = new LinkedList<>();
		job = new LinkedList<>();

		TextField readyPath = new TextField();
		readyPath.setPromptText("Enter ready Q file path");

		TextField jobPath = new TextField();
		jobPath.setPromptText("Enter ready Q file path");

		// Create a Button to trigger file retrieval
		

		// Create a Label to display file details or errors
		Label fileDetailsLabel = new Label();

		// Set up the button action
		getFileButton.setOnAction(e -> {
			// Get the file path from the TextField
			String readyPathString = readyPath.getText();
			String jobPathString = jobPath.getText();

			// Create a File object
			File file = new File(readyPathString);
			File file1 = new File(jobPathString);
			// Check if the file exists
			if (file.exists() && !file.isDirectory() && file1.exists() && !file1.isDirectory()) {
				// Display file details
				getfile(readyPathString, jobPathString);
			} else {
				// Display error message
				fileDetailsLabel.setText("File not found or is a directory.");
			}
		});

		// Create a layout and add controls
		Label l1 = new Label("Ready Q File Path:  ");
		l1.setFont(Font.font("Verdana", FontWeight.MEDIUM, FontPosture.ITALIC, 18));

		Label l2 = new Label("Job Q File Path:  ");
		l2.setFont(Font.font("Verdana", FontWeight.MEDIUM, FontPosture.ITALIC, 18));

		VBox layout = new VBox(10, new HBox(l1, readyPath), new HBox(l2, jobPath), getFileButton, fileDetailsLabel);
		layout.setPadding(new Insets(400));
		layout.setAlignment(Pos.CENTER);
		pane.setCenter(layout);

		stage.setMaximized(true);

		Scene scene = new Scene(pane, 600, 600);
		stage.setScene(scene);
		stage.show();

	}

	private void getfile(String p1, String p2) {
		try (BufferedReader br = new BufferedReader(new FileReader(p1))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(" ");
				if (parts.length == 3) {
					int processId = Integer.parseInt(parts[0]);
					int size = Integer.parseInt(parts[1]);
					int timeInMemory = Integer.parseInt(parts[2]);
					PCB pcb = new PCB(processId, size, timeInMemory);
					ready.add(pcb);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		}

		try (BufferedReader br = new BufferedReader(new FileReader(p2))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(" ");
				if (parts.length == 3) {
					int processId = Integer.parseInt(parts[0]);
					int size = Integer.parseInt(parts[1]);
					int timeInMemory = Integer.parseInt(parts[2]);
					PCB pcb = new PCB(processId, size, timeInMemory);
					job.add(pcb);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		}
		jobsize = job.size();

		VBox v1 = create_Q(ready);
		VBox v2 = create_Q(job);
		m = new Memory(ready, job);
		memory = m.getMemory();
		// Using a light color close to sugar-white

		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);

		// Set the gap between rows and columns
		gridPane.setHgap(300); // Horizontal gap
		gridPane.setVgap(10); // Vertical gap

		Label label1 = new Label("Ready Queue");
		Label label2 = new Label("Job Queue");
		label1.setFont(new Font("Arial", 30));
		label2.setFont(new Font("Arial", 30));

		gridPane.add(label1, 0, 0); // Column 0, Row 0
		gridPane.add(v1, 0, 1); // Column 0, Row 1
		gridPane.add(label2, 1, 0); // Column 1, Row 0
		gridPane.add(v2, 1, 1);// Column 1, Row 1
		gridPane.setPadding(new Insets(60, 60, 60, 60));
		gridPane.setStyle("-fx-background-color: #FFF5EE;");

		sc = new ScrollPane(gridPane);
		gridPane.setAlignment(Pos.CENTER);
		pane.setCenter(sc);

		start.setAlignment(Pos.CENTER);
		start.setOnAction(e -> startProcess());

		b = new StackPane(start);
		b.setPadding(new Insets(50, 50, 50, 50));
		b.setStyle("-fx-background-color: #FFF5EE;");
		pane.setRight(b);
	}

	public VBox create_memory(LinkedList<Object> memory, Memory m) {
		VBox table = new VBox();
		table.setPadding(new Insets(60, 60, 60, 60));

		Label l = new Label("ATT= " + m.getOverAllTime());
		l.setPadding(new Insets(10, 10, 10, 10));
		l.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 18));
		table.getChildren().add(l);

		int os_size = m.getOS_size();
		Block os = new Block("os", "0", "" + os_size, "" + os_size, "");
		table.getChildren().add(os);

		for (int i = 0; i < memory.size(); i++) {
			if (memory.get(i) instanceof PCB) {
				PCB p = ((PCB) (memory.get(i)));
				Block process = new Block("process", String.valueOf(p.getBaseRegister()),
						String.valueOf(p.getProcessId()), String.valueOf(p.getSize()),
						String.valueOf(p.getLimitRegister()));
				table.getChildren().add(process);
			} else {
				Hole h = ((Hole) (memory.get(i)));
				Block hole = new Block("hole", String.valueOf(h.getBaseReg()), "", String.valueOf(h.getSize()),
						String.valueOf(h.getLimitReg()));
				table.getChildren().add(hole);
			}
		}
		return table;

	}

	public VBox create_Q(LinkedList<PCB> job) {
		VBox table = new VBox();

		for (int i = 0; i < job.size(); i++) {
			if (job.get(i) instanceof PCB) {
				PCB p = ((PCB) (job.get(i)));
				Block process = new Block("process", String.valueOf(p.getBaseRegister()),
						String.valueOf(p.getProcessId()), String.valueOf(p.getSize()),
						String.valueOf(p.getLimitRegister()));
				table.getChildren().add(process);
			}
		}
		return table;

	}

	public VBox create_Q_to_process_bage(LinkedList<PCB> job, Label l) {

		VBox table = new VBox();
		table.setPadding(new Insets(10, 10, 10, 10));
		l.setPadding(new Insets(10, 10, 10, 10));
		l.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 18));
		table.getChildren().add(l);

		for (int i = 0; i < job.size(); i++) {
			if (job.get(i) instanceof PCB) {
				PCB p = ((PCB) (job.get(i)));
				Block process = new Block("process", String.valueOf(0), String.valueOf(p.getProcessId()),
						String.valueOf(p.getSize()), String.valueOf(0));
				table.getChildren().add(process);
			}
		}
		ScrollPane sc = new ScrollPane(table);
		sc.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		sc.setFitToWidth(true); // Ensure the content width fits the ScrollPane width
		sc.setFitToHeight(true);
		VBox x = new VBox(sc);
		table.setStyle("-fx-background-color: #adb5bd;");
		return x;

	}

	public void startProcess() {
		pane.getChildren().removeAll(b, sc);
		steps = new HBox();
		steps.setSpacing(70);
		// steps.setPadding(new Insets(10,60,60,60));
		steps.setStyle("-fx-background-color: #8d99ae;");
		steps.setAlignment(Pos.CENTER);

		next.setOnAction(e -> nextStep());
		StackPane nextbtn = new StackPane(next);
		nextbtn.setPadding(new Insets(10, 10, 10, 10));
		nextbtn.setStyle("-fx-background-color: #adb5bd;");

		VBox first = create_memory(memory, m);
		steps.getChildren().add(first);
		// steps.getChildren().add(next);

		StackPane stackPane = new StackPane(steps);
		stackPane.setAlignment(Pos.CENTER);
		ScrollPane proc = new ScrollPane(stackPane);
		proc.setFitToWidth(true); // Ensure the content width fits the ScrollPane width
		proc.setFitToHeight(true);
		// stackPane.setPadding(new Insets(40,40,40,40));
		jobQueue = create_Q_to_process_bage(job, new Label("Job Queue"));
		pane.setLeft(jobQueue);

//		VBox main= new VBox();
//		main.getChildren().addAll(proc, next);
		pane.setTop(nextbtn);
		pane.setCenter(proc);
		pane.getCenter().setStyle("-fx-background-color: #8d99ae;");

	}

	private void nextStep() {
		if (jobsize > 0) {
			m.creatHole();
			margeNext();
			boolean x = m.AllocatNextProcess();
			margeNext();
			VBox first = create_memory(memory, m);
			steps.getChildren().clear();
			steps.getChildren().add(first);

			pane.setLeft(null);
			jobQueue = create_Q_to_process_bage(m.getJobs(), new Label("Job Queue"));
			pane.setLeft(jobQueue);

			finishQueue = create_Q_to_process_bage(m.getFinish(), new Label("Finish Queue"));
			pane.setRight(finishQueue);
			if (x)
				jobsize--;

		} else {
			BtDeleteStyle(next);
		}
		System.out.println(memory.toString());
		compaction();
	}

	private void margeNext() {
		for (int i = 0, j = 1; j < memory.size(); i++, j++) {
			if ((memory.get(i) instanceof Hole) && (memory.get(j) instanceof Hole)) {
				Hole second = (Hole) memory.remove(j);
				Hole first = (Hole) memory.remove(i);

				Hole newHole = new Hole(first.getBaseReg(), second.getLimitReg(), first.getSize() + second.getSize());
				memory.add(i, newHole);
			}
		}

	}

	private void compaction() {
		int count = 0;
		for (int i = 0; i < memory.size(); i++) {
			if (memory.get(i) instanceof Hole)
				count++;
		}
		int holeSize = 0;
		if (count > 3) {
			for (int i = 0; i < memory.size(); i++) {
				if (memory.get(i) instanceof Hole)
					holeSize += ((Hole) (memory.remove(i))).getSize();
			}

			Hole newHole = new Hole(((PCB) (memory.getLast())).getLimitRegister(), 2500, holeSize);
			memory.add(newHole);
		}
	}

	private void creat_buttons() {

		start = new Button("Start Processing");
		start.setAlignment(Pos.CENTER);

		start.setStyle(
				"-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 34pt; -fx-padding: 15px 30px;");

		start.setOnMouseEntered(e -> {
			ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), start); // bt1 effects
			scaleTransition.setToX(1.2);
			scaleTransition.setToY(1.2);
			scaleTransition.play();
		});
		start.setOnMouseExited(e -> {
			ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), start);// bt2 effects
			scaleTransition.setToX(1.0);
			scaleTransition.setToY(1.0);
			scaleTransition.play();
		});

		next = new Button("Next Step");
		next.setPadding(new Insets(10, 10, 10, 10));

		next.setStyle(
				"-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 34pt; -fx-padding: 3px 7px;");

		next.setOnMouseEntered(e -> {
			ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), next); // bt1 effects
			scaleTransition.setToX(1.2);
			scaleTransition.setToY(1.2);
			scaleTransition.play();
		});
		next.setOnMouseExited(e -> {
			ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), next);// bt2 effects
			scaleTransition.setToX(1.0);
			scaleTransition.setToY(1.0);
			scaleTransition.play();
		});
		
		 getFileButton = new Button("Get Files");
		 getFileButton.setPadding(new Insets(10, 10, 10, 10));

		 getFileButton.setStyle(
				"-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 34pt; -fx-padding: 5px 10px;");

		 getFileButton.setOnMouseEntered(e -> {
			ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), getFileButton); // bt1 effects
			scaleTransition.setToX(1.2);
			scaleTransition.setToY(1.2);
			scaleTransition.play();
		});
		 getFileButton.setOnMouseExited(e -> {
			ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), getFileButton);// bt2 effects
			scaleTransition.setToX(1.0);
			scaleTransition.setToY(1.0);
			scaleTransition.play();
		});
	}

	public void BtDeleteStyle(Button bt) {
		bt.setStyle(
				"-fx-background-color: #808080; -fx-text-fill: white; -fx-font-size: 20pt; -fx-padding: 18px 40px;");
		bt.setOnMouseEntered(null);

		bt.setOnMouseExited(null);
	}

	public static void main(String[] args) {

		launch();

	}
}
