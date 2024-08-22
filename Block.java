import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Block extends HBox {

	public Block(String type, String baseR, String id, String size, String limitR) {
		int sizememo = Integer.parseInt(size);
		Label base = new Label();
		Label lim = new Label();
		Label pr_id = new Label();
		Label sizeLabel = new Label();

		base.setText(baseR);
		base.setFont(Font.font("Verdana", FontWeight.MEDIUM, FontPosture.ITALIC, 18));

		lim.setText(limitR);
		lim.setFont(Font.font("Verdana", FontWeight.MEDIUM, FontPosture.ITALIC, 18));

		pr_id.setFont(Font.font("Verdana", FontWeight.MEDIUM, 18));

		sizeLabel.setText("Size: " + size);
		sizeLabel.setFont(Font.font("Verdana", FontWeight.MEDIUM, 18));


		VBox block = new VBox();
		block.setMaxWidth(270);
		block.setMinWidth(270);

		if (sizememo <= 50) { // to change each block size based on size
			block.setMaxHeight(50);
			block.setMinHeight(50);
		} else if (sizememo > 50 && sizememo <= 150) {
			block.setMaxHeight(60);
			block.setMinHeight(60);
		}else if (sizememo > 150 && sizememo <= 250) {
			block.setMaxHeight(70);
			block.setMinHeight(70);
		}else if (sizememo > 250 && sizememo <= 400) {
			block.setMaxHeight(80);
			block.setMinHeight(80);
		}else if (sizememo > 400 && sizememo <= 600) {
			block.setMaxHeight(85);
			block.setMinHeight(85);
		}else {
			block.setMaxHeight(sizememo/7);
			block.setMinHeight(sizememo/7);
		}

		block.getChildren().addAll(pr_id, sizeLabel);
		if (type.equals("os")) { // to change block color based in type (OS , Process, Hole)
			pr_id.setText("OS");
			block.setStyle("-fx-background-color: #808080; -fx-border-color: black; -fx-border-width: 3;");
		} else if (type.equals("process")) {
			pr_id.setText("P" + id);
			block.setStyle("-fx-background-color: #ADD8E6; -fx-border-color: black; -fx-border-width: 3;");
		} else {
			pr_id.setText("Hole");
			block.setStyle("-fx-background-color: #D3D3D3; -fx-border-color: black; -fx-border-width: 3;");

		}
		lim.setAlignment(Pos.BOTTOM_LEFT);

		if (baseR.equals("0"))
			getChildren().addAll(block);
		else
			getChildren().addAll(block, base);

	}
}
