package maze;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;


/**
 * ��һ������ǽ�ڵ�����ʼ��
 * ��ѡһ��ϸ����������Ϊ�Թ���һ���֡�����Ԫ���ǽ��ӵ�ǽ�б��С�
 * ��Ȼ�б�����ǽ�ڣ�
 * ���б���ѡ��һ�����ǽ�����ǽ�ڷָ���������Ԫ����ֻ��һ�������ʣ���
 * ��ǽ����Ϊͨ������δ���ʵĵ�Ԫ���Ϊ�Թ���һ���֡�
 * ����Ԫ�������ǽ��ӵ�ǽ�б��С�
 * ���б���ɾ��ǽ.
 */
public class SquareMaze extends BorderPane{
	AStar aStar;
	Dfs dfs;
	Bfs bfs;
	Prim prim;
	Traversing traversing;
	/**
	 * ����װ�Թ���ͼ�е�ÿ��������� ʹ��������Ϊ�˸�����Ķ�λ
	 */
	private SquareNode[][] squareNodes;
	
	// 
	/**
	 * �Թ��Ŀ��
	 * ����������Թ�ʱ֪����ӵķ������
	 */
	private int width;
	/**
	 * �Թ��ĳ���
	 * ����������Թ�ʱ֪����ӵķ������
	 */
	private int height;
	
	
	/**
	 * Ѱ·��������
	 */
	private int startX = 1;
	/**
	 * Ѱ·���������
	 */
	private int startY = 1;
	/**
	 * Ѱ·�յ������
	 */
	private int endX = 23;
	/**
	 * Ѱ·�յ�������
	 */
	private int endY = 23;
	
	/**
	 * �����ٶ� Խ��ĵ�ͼ��Ҫ���ص��ٶȾ�ҪԽ�� ���Խ��ٶȴ����ɱ��� ������ĵ�ͼ�ı��ı��ٶȱ��� �Ӷ��ӿ�����ٶ�
	 */
	private int GenerationSpeed = 5;
	private int FindWaySpeed = 100;
	private int SingleCellSpeed = 3000;
	
	/**
	 * ͼƬ��ʾ�Ŀ��
	 */
	private double imageWidth = 20;
	/**
	 * ͼƬ��ʾ�ĸ߶�
	 */
	private double imageHeight = 20;
	
	/**
	 * ������ʾ���� ��ʾ��ʾ�����ķ�����±� ��TimeLine��������ʾ�ڼ�index�᲻������ index��ֵ��������Ҫ���ж����ķ�����±�index
	 */
	private int index = 0;
	
	/**
	 * ����װ�Թ��������� ����Ҫ�γ�������Թ�����ͼƬ�Ĵ�С��һ����Ҫһ��һ�з���
	 */
	private FlowPane mazepane = new FlowPane();
	
	/**
	 * UI���
	 */
	private ToggleButton btnGenerateMaze = new ToggleButton("Generate a maze");
	private ToggleButton btnFindWay = new ToggleButton("Find a way");
	private ToggleButton btnShortestWay = new ToggleButton("Shortest way");
	private ToggleButton btnFootprintClear = new ToggleButton("Footprint Clear");
	private ToggleButton btnMazefile = new ToggleButton("Import Maze file");
	private FileChooser fileChooser = new FileChooser();
	private ToggleButton btnStep = new ToggleButton("Step Mode");
	
	private ToggleGroup btnGroup = new ToggleGroup();
	
	
	/**
	 * �Թ���Сѡ���� ��"25��25","51��51"���ִ�С
	 */
	private String[] SizeTitle = {"25��25","51��51"};
	/**
	 * �Թ���Сѡ���
	 */
	private ComboBox<String> cboSizeChoose = new ComboBox<>();
	ObservableList<String> sizeTitleItems;
	
	/**
	 * Ѱ·ģʽѡ���� ��dfs bfs A* ����Ѱ·��ʽ
	 */
	private String[] findWayModelTitle = {"dfs","bfs","A*"};
	/**
	 * Ѱ·ģʽѡ���
	 */
	private ComboBox<String> cboModelChoose = new ComboBox<>();
	ObservableList<String> modelChooseItems;
	
	/**
	 * ѡ���Ѱ·ģʽ
	 */
	FindWayable findWay;
	
	/**
	 * ������¼ԭ��·��ͼƬ
	 */
	private ArrayList<Image> originalRoadImage = new ArrayList<>();
	
	/**
	 * �洢�Թ��Ľⷨ����
	 */
	private Stack<SquareNode> solutionProcess;
	
	/**
	 * �洢�Թ��Ľⷨ
	 */
	private Stack<SquareNode> solution;
	
	/**
	 * �ж�����Ƿ�ѡ��
	 */
	private boolean isStartChoose = true;
	
	/**
	 * �ж��յ��Ƿ�ѡ��
	 */
	private boolean isEndChoose = true;
	
	/**
	 * ������������ʱ��¼ԭ����ͼƬ
	 */
	String originalStartImage;
	
	/**
	 * ���յ��������ʱ��¼ԭ����ͼƬ
	 */
	String originalEndImage;
	
	public SquareMaze() {
		this(25,25,1,1,23,23);
	}
	
	public SquareMaze(int width, int height, int startX, int startY, int endX, int endY){
		this.width = width;
		this.height = height;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.squareNodes = new SquareNode[height][width];
		addWall(); // ����ΪͼƬ��С
		addUIComponents();   // ���UI��� ���� Ѱ· �����Թ� ���� �Ȱ�ť
	}
	
	
	/**
	 * ���ݵ�ͼ�Ĵ�С��ǽ������뵽�����
	 */
	private void addWall() {
		mazepane.getChildren().clear();
		mazepane.setPrefSize(510, 510);
		mazepane.setAlignment(Pos.CENTER);
		mazepane.setOrientation(Orientation.VERTICAL);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				/**
				 * ����ǽ��ͼƬ(ǽ��ͼƬ��������ɫ�ͻҰ�ɫ)
				 */
				String[] imagefilename;
				if(j == width-1) {
					// �������һ��ʱΪ�˱������ͼ����Ҫ������ͼƬ
					imageHeight = imageWidth + imageWidth/2;
					String[] filename = {"file:Wallup.png", "file:greenWallup.png"};
					imagefilename = filename;
				}
				else {
					imageHeight = imageWidth;
					String[] filename = {"file:Wallleft.png", "file:WallGreen.png"};
					imagefilename = filename;
				}
				
				int index = (int)(Math.random()*imagefilename.length);
				// �������һ��ͼƬ(��ɫ������ɫ)
				Image image = new Image(imagefilename[index]);
				ImageView imageView = new ImageView(image);
				imageView.setFitHeight(imageHeight);
				imageView.setFitWidth(imageWidth);
				imageHeight = imageWidth;
				mazepane.getChildren().add(imageView);
				
				/**
				 * ����յ��ѡ���ж� �����������ѡ���Ҽ�ȡ��  �յ�������Ҽ�ѡ�����ȡ��
				 */
				imageView.setOnMouseClicked(e -> {
					if(e.getButton() == MouseButton.PRIMARY && judgeRoadImage(imageView) == true && isStartChoose == false) {
						originalStartImage = imageView.getImage().impl_getUrl();
						Image originalStartImageTemp = imageView.getImage();
						
						if(imageView.getFitHeight() == imageView.getFitWidth()) {
							imageView.setImage(new Image("file:Start.png"));
						}
						else {
							imageView.setImage(new Image("file:Starthalf.png"));
						}
						
						updateOriginalRoadImage(originalStartImageTemp, imageView.getImage());
		
						int n = mazepane.getChildren().indexOf(imageView);
						startY = n%width;
						startX = n/width;
						isStartChoose = true;
					}
					else if(e.getButton() == MouseButton.SECONDARY && judgeStartImage(imageView) == true){
						Image tempimage = new Image(originalStartImage);
						updateOriginalRoadImage(imageView.getImage(), tempimage);
						
						imageView.setImage(tempimage);
						isStartChoose = false;
					}
					else if(e.getButton() == MouseButton.SECONDARY && judgeRoadImage(imageView) == true && isEndChoose == false) {
						originalEndImage = imageView.getImage().impl_getUrl();
						Image originalStartImageTemp = imageView.getImage();
						
						if(imageView.getFitHeight() == imageView.getFitWidth()) {
							imageView.setImage(new Image("file:End.png"));
						}
						else {
							imageView.setImage(new Image("file:Endhalf.png"));
						}
						
						updateOriginalRoadImage(originalStartImageTemp, imageView.getImage());
						
						int n = mazepane.getChildren().indexOf(imageView);
						endY = n%width;
						endX = n/width;
						isEndChoose = true;
					}
					else if(e.getButton() == MouseButton.PRIMARY && judgeEndImage(imageView) == true) {
						Image tempimage = new Image(originalEndImage);
						updateOriginalRoadImage(imageView.getImage(), tempimage);
						
						imageView.setImage(tempimage);
						isEndChoose = false;
					}
				});
				
				/**
				 * ��������ͼƬ�ı�͸����
				 */
				imageView.setOnMouseEntered(e -> {
					imageView.setOpacity(0.5);
				});
				
				/**
				 * ������Ƴ�ͼƬ��ԭ͸����
				 */
				imageView.setOnMouseExited(e -> {
					imageView.setOpacity(1);
				});
				
				/**
				 * ����ǽʱ�����ɶ���
				 */
				FadeTransition ft = new FadeTransition(Duration.millis(3000), imageView);
				ft.setFromValue(0.2);
				ft.setToValue(1);
				ft.setCycleCount(3);
				ft.setAutoReverse(true);
				ft.play();
			}
		}
		
		// ��mazepane����Ϊ���Ĳ�����
		super.setCenter(mazepane);
		BorderPane.setAlignment(mazepane, Pos.CENTER);
	}
	
	
	/**
	 * ��������յ�λ��
	 * @param originalImage ԭʼͼƬ
	 * @param updateImage ��Ҫ���µ�ͼƬ
	 */
	private void updateOriginalRoadImage(Image originalImage, Image updateImage) {
		originalRoadImage.set(originalRoadImage.indexOf(originalImage), updateImage);
	}

	
	/**
	 * �ж�ͼƬ�Ƿ�����·����ͼƬ
	 * @param imageView ͼƬ��imageView
	 * @return Boolean���� ��·��ͼƬ�򷵻�true �����򷵻�false
	 */
	private boolean judgeRoadImage(ImageView imageView) {
		String[] imagefilename = {"file:WayNomal.png", "file:Waymidlight.png", "file:Waymiddeep.png", "file:Waylight.png",
				"file:WayUp.png", "file:Wayhalf.png", "file:Wayhalf2.png"};
		
		for (String string : imagefilename) {
			if(string.equals(imageView.getImage().impl_getUrl()) == true) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * �ж�ͼƬ�Ƿ���������ͼƬ
	 * @param imageView ͼƬ��imageView
	 * @return Boolean���� �����ͼƬ�򷵻�true �����򷵻�false
	 */
	private boolean judgeStartImage(ImageView imageView) {
		String[] imagefilename = {"file:Start.png", "file:Starthalf.png"};
		for (String string : imagefilename) {
			if(imageView.getImage().impl_getUrl().equals(string) == true) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * �ж�ͼƬ�Ƿ������յ��ͼƬ
	 * @param imageView ͼƬ��imageView
	 * @return Boolean���� ���յ�ͼƬ�򷵻�true �����򷵻�false
	 */
	private boolean judgeEndImage(ImageView imageView) {
		String[] imagefilename = {"file:End.png", "file:Endhalf.png"};
		for (String string : imagefilename) {
			if(imageView.getImage().impl_getUrl().equals(string) == true) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * ��UI������뵽�����
	 */
	private void addUIComponents() {
		VBox vBox = new VBox(10);
		vBox.setPadding(new Insets(8,8,8,8));
		vBox.setAlignment(Pos.CENTER);
		super.setRight(vBox);
		BorderPane.setAlignment(vBox, Pos.CENTER);
		
		/**
		 * �Զ������Թ���UI���
		 */
		btnGenerateMaze.setToggleGroup(btnGroup);
		btnGenerateMaze.setDisable(false);
		btnGenerateMaze.setOnAction(e -> {
			originalRoadImage.clear();
			cerateMaze();
		});
		
		
		/**
		 * �ֶ������Թ���UI���
		 */
		btnMazefile.setOnAction(e -> {
			File file = fileChooser.showOpenDialog(null);
			String MazeStr = new String("");
			if(file != null) {
				try(
					Scanner input = new Scanner(file);
				){
					while(input.hasNext()) {
						MazeStr += input.next();			
					}
				} catch (FileNotFoundException e1) {
					System.out.println(e1.getMessage());
				}
				
				if(MazeStr.length() == width*height) {
		            for (int i = 0; i < squareNodes.length; i++) {
		        		for (int j = 0; j < squareNodes[0].length; j++) {
		        			squareNodes[i][j] = new SquareNode();
		        			squareNodes[i][j].setX(j);
		    				squareNodes[i][j].setY(i);
		    				if(MazeStr.charAt(i*width+j) == '1') {
		    					squareNodes[i][j].setRoad(false);
		    				}
		    				else {
		    					squareNodes[i][j].setRoad(true);
		    				}
		    			}
		        	}
					RoadAnimation();
				}
			}
			
		});
		
		
		/**
		 * Ѱ·UI������������·����
		 */
		btnFindWay.setToggleGroup(btnGroup);
		btnFindWay.setDisable(true);
		btnFindWay.setOnAction(e -> {
			if(isStartChoose == true && isEndChoose == true) {
				restoreMaze();
				CheckModelChoose();
				WayAnimation(findWay, 2);
			}
		});
		
		
		/**
		 * Ѱ·UI����������·����
		 */
		btnShortestWay.setToggleGroup(btnGroup);
		btnShortestWay.setDisable(true);
		btnShortestWay.setOnAction(e -> {
			if(isStartChoose == true && isEndChoose == true) {
				findWay = new Traversing(startX, startY, endX, endY, squareNodes);
				WayAnimation(findWay, 3);
			}
		});
		
		
		/**
		 * ����㼣
		 */
		btnFootprintClear.setToggleGroup(btnGroup);
		btnFootprintClear.setDisable(true);
		btnFootprintClear.setOnAction(e -> {
			if(isStartChoose == true && isEndChoose == true) {
				restoreMaze();
			}
		});
		
		
		/**
		 * ����Ѱ·
		 */
		btnStep.setToggleGroup(btnGroup);
		btnStep.setDisable(true);
		btnStep.setOnAction(e -> {
			btnFindWay.setDisable(true);
			btnFootprintClear.setDisable(true);
			btnShortestWay.setDisable(true);
			btnGenerateMaze.setDisable(true);
			btnMazefile.setDisable(true);
			if(isStartChoose == true && isEndChoose == true && btnStep.getText().equals("Step Mode")) {
				restoreMaze();
				CheckModelChoose();
				btnStep.setText("Step");
				index = 0;
			}
			else if(btnStep.getText().equals("Step")) {
				solutionProcess = findWay.getFindWayProcessResult();
				 
		        if(index < solutionProcess.size()) {
		        	if(index > 0 && index < solutionProcess.size()-1 && (solutionProcess.get(index).getX() != startX || solutionProcess.get(index).getY() != startY)) {
		        		int imageViewIndex = solutionProcess.get(index).getX()*width + solutionProcess.get(index).getY();
		        		if(((ImageView)(mazepane.getChildren().get(imageViewIndex))).getFitHeight() == ((ImageView)(mazepane.getChildren().get(imageViewIndex))).getFitWidth())
		        			((ImageView)(mazepane.getChildren().get(imageViewIndex))).setImage(new Image("file:head.png"));
		        		else
		        			((ImageView)(mazepane.getChildren().get(imageViewIndex))).setImage(new Image("file:headhalf.png"));
		        			 
		        	}
		        	if(index > 1 && (solutionProcess.get(index-1).getX() != startX || solutionProcess.get(index-1).getY() != startY)) {
		        		int imageViewIndex = solutionProcess.get(index-1).getX()*width + solutionProcess.get(index-1).getY();
		        		if(((ImageView)(mazepane.getChildren().get(imageViewIndex))).getFitHeight() == ((ImageView)(mazepane.getChildren().get(imageViewIndex))).getFitWidth())
		        			((ImageView)(mazepane.getChildren().get(imageViewIndex))).setImage(new Image("file:histroy.png"));
		        		else
		        			((ImageView)(mazepane.getChildren().get(imageViewIndex))).setImage(new Image("file:histroyhalf.png"));
		        	}
		        	 
		            index++;
		        }
		        else
		            index = 0;
		        if(index == 0) {
		        	btnStep.setText("Step Mode");
		        	btnStep.setDisable(true);
		        	btnFindWay.setDisable(false);
		        	btnFootprintClear.setDisable(false);
		        	btnShortestWay.setDisable(false);
		        	btnStep.setDisable(false);
		        	btnGenerateMaze.setDisable(false);
		        	btnMazefile.setDisable(false);
		        }
			}
		});
		
		
		/**
		 * �Թ���Сѡ���
		 */
		BorderPane sizeChoosePane = new BorderPane();
		Label sizeChooseLab = new Label("Choose Size");
		BorderPane.setAlignment(sizeChooseLab, Pos.CENTER);
		sizeChoosePane.setTop(sizeChooseLab);
		sizeChoosePane.setCenter(cboSizeChoose);

		cboSizeChoose.setValue("25��25");
		sizeTitleItems = FXCollections.observableArrayList(SizeTitle);
		cboSizeChoose.getItems().addAll(sizeTitleItems);
		cboSizeChoose.setPrefWidth(90);	
		cboSizeChoose.setOnAction(e -> {
			CheckSizeChoose();
		});
		
		
		/**
		 * Ѱ·ģʽѡ���
		 */
		BorderPane modelChoosePane = new BorderPane();
		Label modelChooseLab = new Label("Choose Model");
		BorderPane.setAlignment(modelChooseLab, Pos.CENTER);
		modelChoosePane.setTop(modelChooseLab);
		modelChoosePane.setCenter(cboModelChoose);
		
		cboModelChoose.setValue("dfs");
		modelChooseItems = FXCollections.observableArrayList(findWayModelTitle);
		cboModelChoose.getItems().addAll(modelChooseItems);
		cboModelChoose.setPrefWidth(90);
		cboModelChoose.setOnAction(e -> {
			
		});
		
		
		/**
		 * ���齨����vBox��
		 */
		vBox.getChildren().addAll(btnGenerateMaze, btnMazefile, modelChoosePane, btnFindWay, btnShortestWay, btnFootprintClear, btnStep, sizeChoosePane);
	}
	
	
	/**
	 * ��ԭ��ǰ�Թ�
	 */
	private void restoreMaze() {
		int k = 0;
		for (int i = 1; i < squareNodes.length-1; i++) {
			for (int j = 1; j < squareNodes.length-1; j++) {
				if(squareNodes[j][i].isRoad() == true) {
					((ImageView)mazepane.getChildren().get(i*width + j)).setImage(originalRoadImage.get(k++));
					squareNodes[j][i].setStep(false);
				}
			}
		}
	}
	
	
	/**
	 * ����Թ���С��ѡ�� ѡ�������������Ը�ֵ
	 */
	private void CheckSizeChoose() {
		int index = sizeTitleItems.indexOf(cboSizeChoose.getValue()); // 0 1 2 3 �ֱ������Թ���СΪ 25 51 101 201
		switch (index) {
		case 0:
			startX = 1;
			startY = 1;
			endX = 23;
			endY = 23;
			width = 25;	
			height = 25;
			GenerationSpeed = 5;
			FindWaySpeed = 100;
			SingleCellSpeed = 3000;
			this.squareNodes = new SquareNode[height][width];
			imageHeight = 20;
			imageWidth = 20;
			addWall();
			break;
		case 1:
			startX = 1;
			startY = 1;
			endX = 49;
			endY = 49;
			width = 51;	
			height = 51;
			GenerationSpeed = 3;
			FindWaySpeed = 50;
			SingleCellSpeed = 3000;
			this.squareNodes = new SquareNode[height][width];
			index = 0;
			imageHeight = 10;
			imageWidth = 10;
			addWall();
			break;
		default:
			break;
		}
		
		// ��ť������
		btnGenerateMaze.setDisable(false);
		btnFindWay.setDisable(true);
		btnShortestWay.setDisable(true);
		btnFootprintClear.setDisable(true);
		btnStep.setDisable(true);
	}
	
	
	/**
	 * ���Ѱ·ѡ���ģʽ
	 */
	private void CheckModelChoose() {
		int index = modelChooseItems.indexOf(cboModelChoose.getValue());
		switch (index) {
		case 0:
			clear();
			findWay = new Dfs(startX, startY, endX, endY, squareNodes);
			break;
		case 1:
			findWay = new Bfs(startX, startY, endX, endY, squareNodes);
			break;
		case 2:
			findWay = new AStar(startX, startY, endX, endY, squareNodes);
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * SquareNode���ͽ���ж�step������
	 */
	private void clear() {
		for (int i = 1; i < squareNodes.length-1; i++) {
			for (int j = 1; j < squareNodes.length-1; j++) {
				if(squareNodes[j][i].isRoad() == true) {
					squareNodes[j][i].setStep(false);
				}
			}
		}
	}
	
	
	/**
	 * �����Թ�
	 */
	public void cerateMaze(){
		cerateNet();
		// ����һ�������Թ���ʵ��
		Prim prim = new Prim(startX, startY, squareNodes);
		prim.RemoveWall(prim.getStartX(), prim.getStartY());
		
		RoadAnimation();
	}
	
	
	/**
	 * �γ�����������һ��һ����ϸ��
	 */
	private void cerateNet(){
		for (int i = 0; i < squareNodes.length; i++) {
			for (int j = 0; j < squareNodes[0].length; j++) {
				squareNodes[i][j] = new SquareNode();
				squareNodes[i][j].setX(j);
				squareNodes[i][j].setY(i);
				if((i * j)%2 == 0){
					squareNodes[i][j].setRoad(false); // ����Ϊǽ
				}
				else{
					squareNodes[i][j].setRoad(true);  // ����Ϊ·
				}
			}
		}	
	}
	
	
	/**
	 * ��Prim�㷨���Թ�������ɺ�ÿ��������·����ǽ���Ѿ���¼��
	 * Roadanimation�����������Ѿ���¼�õ����ݽ��ж�������
	 * ͬ����������SequentialTransition���ж������Ⱥ���ʾ �������ź�������
	 */
	private void RoadAnimation() {
		btnFindWay.setDisable(true);
		btnFootprintClear.setDisable(true);
		btnShortestWay.setDisable(true);
		btnStep.setDisable(true);
		btnGenerateMaze.setDisable(true);
		btnMazefile.setDisable(true);
		Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(GenerationSpeed), e -> {
			if(index < mazepane.getChildren().size()) {
				FadeTransition ft = new FadeTransition(Duration.millis(SingleCellSpeed), (ImageView)(mazepane.getChildren().get(index)));
				ft.setFromValue(1);
				ft.setToValue(0);
				ft.setCycleCount(1);
				ft.setAutoReverse(true);
				ft.play();
				index++;
			}
			else
				index = 0;
		}));
		timeline1.setCycleCount(mazepane.getChildren().size()+1);
		
		Timeline timeline2 = new Timeline(new KeyFrame(Duration.millis(GenerationSpeed), e -> {
			if(index < mazepane.getChildren().size()) {
				chooseImage((ImageView)(mazepane.getChildren().get(index)), index);
				 
				FadeTransition ft = new FadeTransition(Duration.millis(SingleCellSpeed), (ImageView)(mazepane.getChildren().get(index)));
				ft.setFromValue(0);
				ft.setToValue(1);
				ft.setCycleCount(1);
				ft.setAutoReverse(true);
				ft.play();
				index++;
			}
			else {
				index = 0;
				btnFindWay.setDisable(false);
				btnFootprintClear.setDisable(false);
				btnShortestWay.setDisable(false);
				btnStep.setDisable(false);
				btnGenerateMaze.setDisable(false);
				btnMazefile.setDisable(false);
			}
			 
		}));
		 
		timeline2.setCycleCount(mazepane.getChildren().size()+1);
		SequentialTransition sequentialTransition = new SequentialTransition(timeline1, timeline2);
		sequentialTransition.play();
	}
	
	
	/**
	 * ѡ��ǰ�ڵ�Ӧ����ʾ��ͼƬ
	 * @param imageView ��Ҫ�ı�ͼƬ�Ľ��
	 * @param index �ڵ�������е��±�
	 */
	public void chooseImage(ImageView imageView, int index) {
		if(squareNodes[index%width][index/width].isRoad() == false) {
			if(index%width + 1 < squareNodes.length && squareNodes[index%width+1][index/width].isRoad() == true) {
				String[] filename = {"file:Wallup.png", "file:greenWallup.png"};
				int randomIndex = (int)(Math.random()*filename.length);
				Image image = new Image(filename[randomIndex]);
				imageView.setImage(image);
				imageView.setFitHeight(imageHeight + imageWidth/2);
				imageView.setFitWidth(imageWidth);
			}
			else if(index%width + 1 >= squareNodes.length) {
				String[] filename = {"file:greenWallup.png", "file:Wallup.png"};
				int randomIndex = (int)(Math.random()*filename.length);
				Image image = new Image(filename[randomIndex]);
				imageView.setImage(image);
				imageView.setFitHeight(imageHeight + imageWidth/2);
				imageView.setFitWidth(imageWidth);
			}
			else {
				String[] filename = {"file:Wallleft.png", "file:WallGreen.png"};
				int randomIndex = (int)(Math.random()*filename.length);
				Image image = new Image(filename[randomIndex]);
				imageView.setImage(image);
				imageView.setFitHeight(imageHeight);
				imageView.setFitWidth(imageWidth);
			}
		}
		else {
			// ��ʾ·����ͼƬ
			Image image;
			if(squareNodes[index%width+1][index/width].isRoad() == false && squareNodes[index%width-1][index/width].isRoad() == false) {
				image = new Image("file:Wayhalf2.png");
				imageView.setImage(image);
				imageView.setFitHeight(imageHeight/2);
				imageView.setFitWidth(imageWidth);
			}
			else if(squareNodes[index%width+1][index/width].isRoad() == false && squareNodes[index%width-1][index/width].isRoad() == true) {
				image = new Image("file:Wayhalf.png");
				imageView.setImage(image);
				imageView.setFitHeight(imageHeight/2);
				imageView.setFitWidth(imageWidth);
			}
			else if(squareNodes[index%width+1][index/width].isRoad() == true && squareNodes[index%width-1][index/width].isRoad() == false) {
				image = new Image("file:WayUp.png");
				imageView.setImage(image);
				imageView.setFitHeight(imageHeight);
				imageView.setFitWidth(imageWidth);
			}
			else {
				String[] filename = {"file:WayNomal.png", "file:Waymidlight.png", "file:Waymiddeep.png", "file:Waylight.png"};
				// �������ͼƬ�±�
				int randomIndex = (int)(Math.random()*filename.length);
				image = new Image(filename[randomIndex]);
				imageView.setImage(image);
				imageView.setFitHeight(imageHeight);
				imageView.setFitWidth(imageWidth);
			}
			
			originalRoadImage.add(image);
			
			if(index/width == startX && index%width == startY){
				// �洢���ԭ����ͼƬ��ַ
				originalStartImage = image.impl_getUrl();
				Image startImage;
				// �������ʱ����������ͼƬ ��֮�������ͼƬ
				if(imageView.getFitHeight() == imageView.getFitWidth()) {
					startImage = new Image("file:Start.png");					
				}
				else {
					startImage = new Image("file:Starthalf.png");
				}
				imageView.setImage(startImage);
				originalRoadImage.remove(image);
				originalRoadImage.add(startImage);
			}
			else if(index/width == endX && index%width == endY) {
				// �洢�յ�ԭ����ͼƬ��ַ
				originalEndImage = image.impl_getUrl();
				
				Image endImage;
				// �������ʱ����������ͼƬ ��֮�������ͼƬ
				if(imageView.getFitHeight() == imageView.getFitWidth()) {
					endImage = new Image("file:End.png");					
				}
				else {
					endImage = new Image("file:Endhalf.png");
				}
				imageView.setImage(endImage);
				originalRoadImage.remove(image);
				originalRoadImage.add(endImage);
			}
		}
		
		imageView.setOpacity(0);
	}


	/**
	 * ��Ѱ·�㷨�Ѿ���·����¼�ú�����Wayanimation����·���Ķ�����ʾ
	 * ͬ����������SequentialTransition���ж������Ⱥ���ʾ �������ź�������
	 * @param findWay Ѱ��·���ķ���
	 * @param type int���� 
	 * ����typeֵΪ0��ʾA*�㷨 
	 * ����typeֵΪ1��ʾ����������� 
	 * ����typeֵΪ2��ʾ����������� 
	 */
	private void WayAnimation(FindWayable findWay, int type) {
		btnFindWay.setDisable(true);
		btnFootprintClear.setDisable(true);
		btnShortestWay.setDisable(true);
		btnStep.setDisable(true);
		btnGenerateMaze.setDisable(true);
		btnMazefile.setDisable(true);
		Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(FindWaySpeed), e -> {
			 solutionProcess = findWay.getFindWayProcessResult();
			 
	         if(index < solutionProcess.size()) {
	        	 if(index > 0 && index < solutionProcess.size()-1 && (solutionProcess.get(index).getX() != startX || solutionProcess.get(index).getY() != startY)) {
	        		 int imageViewIndex = solutionProcess.get(index).getX()*width + solutionProcess.get(index).getY();
	        		 if(((ImageView)(mazepane.getChildren().get(imageViewIndex))).getFitHeight() == ((ImageView)(mazepane.getChildren().get(imageViewIndex))).getFitWidth())
	        			 ((ImageView)(mazepane.getChildren().get(imageViewIndex))).setImage(new Image("file:head.png"));
	        		 else
	        			 ((ImageView)(mazepane.getChildren().get(imageViewIndex))).setImage(new Image("file:headhalf.png"));
	        			 
	        	 }
	        	 if(index > 1 && (solutionProcess.get(index-1).getX() != startX || solutionProcess.get(index-1).getY() != startY)) {
	        		 int imageViewIndex = solutionProcess.get(index-1).getX()*width + solutionProcess.get(index-1).getY();
	        		 if(((ImageView)(mazepane.getChildren().get(imageViewIndex))).getFitHeight() == ((ImageView)(mazepane.getChildren().get(imageViewIndex))).getFitWidth())
	        			 ((ImageView)(mazepane.getChildren().get(imageViewIndex))).setImage(new Image("file:histroy.png"));
	        		 else
	        			 ((ImageView)(mazepane.getChildren().get(imageViewIndex))).setImage(new Image("file:histroyhalf.png"));
	        	 }
	        	 
	             index++;
	         }
	         else {
	        	 index = 0;
	         }
		 }));
		
		 timeline1.setCycleCount(findWay.getFindWayProcessResult().size()+1);
		 
		 Timeline timeline2 = new Timeline(new KeyFrame(Duration.millis(FindWaySpeed), e -> {
			 solution = findWay.getFindWayResult();
			 // ͼƬ�ļ���
			 String[] imagefilename = {"file:TraversingFindResult.png","file:bfsFindResult.png","file:dfsFindResult.png","file:shortestway.png"};
			 String[] imagefilenamehalf = {"file:TraversingFindResulthalf.png","file:bfsFindResulthalf.png","file:dfsFindResulthalf.png","file:shortestwayhalf.png"};
			 Image image = new Image(imagefilename[type]);
			 Image imagehalf = new Image(imagefilenamehalf[type]);
			 
	         if(index < solution.size()-1) {
	        	 if(index > 0 && index < solution.size()-1 && (solution.get(index).getX() != startX || solution.get(index).getY() != startY)) {
	        		 int imageViewIndex = solution.get(index).getX()*width + solution.get(index).getY();
	        		 if(((ImageView)(mazepane.getChildren().get(imageViewIndex))).getFitHeight() == ((ImageView)(mazepane.getChildren().get(imageViewIndex))).getFitWidth())
	        			 ((ImageView)(mazepane.getChildren().get(imageViewIndex))).setImage(image);
	        		 else
	        			 ((ImageView)(mazepane.getChildren().get(imageViewIndex))).setImage(imagehalf);
	        			 
	        	 }
	             index++;
	         }
	         else {
	        	 index = 0;
	             btnFindWay.setDisable(false);
	     		 btnFootprintClear.setDisable(false);
	     		 btnShortestWay.setDisable(false);
	     		 btnStep.setDisable(false);
	     		 btnGenerateMaze.setDisable(false);
	     		 btnMazefile.setDisable(false);
	         }
		 }));
		 
		 timeline2.setCycleCount(findWay.getFindWayResult().size());
		 
		 SequentialTransition sequentialTransition = new SequentialTransition(timeline1, timeline2);
		 sequentialTransition.play();
	}
}