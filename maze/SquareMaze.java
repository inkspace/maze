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
 * 从一个充满墙壁的网格开始。
 * 挑选一个细胞，将其标记为迷宫的一部分。将单元格的墙添加到墙列表中。
 * 虽然列表中有墙壁：
 * 从列表中选择一个随机墙。如果墙壁分隔的两个单元格中只有一个被访问，则：
 * 将墙壁作为通道并将未访问的单元标记为迷宫的一部分。
 * 将单元格的相邻墙添加到墙列表中。
 * 从列表中删除墙.
 */
public class SquareMaze extends BorderPane{
	AStar aStar;
	Dfs dfs;
	Bfs bfs;
	Prim prim;
	Traversing traversing;
	/**
	 * 用来装迷宫地图中的每个方块对象 使用数组是为了更方便的定位
	 */
	private SquareNode[][] squareNodes;
	
	// 
	/**
	 * 迷宫的宽度
	 * 方便在添加迷宫时知道添加的方块个数
	 */
	private int width;
	/**
	 * 迷宫的长度
	 * 方便在添加迷宫时知道添加的方块个数
	 */
	private int height;
	
	
	/**
	 * 寻路起点横坐标
	 */
	private int startX = 1;
	/**
	 * 寻路起点纵坐标
	 */
	private int startY = 1;
	/**
	 * 寻路终点横坐标
	 */
	private int endX = 23;
	/**
	 * 寻路终点纵坐标
	 */
	private int endY = 23;
	
	/**
	 * 动画速度 越大的地图需要加载的速度就要越快 所以将速度创建成变量 可以随的地图的变大改变速度变量 从而加快加载速度
	 */
	private int GenerationSpeed = 5;
	private int FindWaySpeed = 100;
	private int SingleCellSpeed = 3000;
	
	/**
	 * 图片显示的宽度
	 */
	private double imageWidth = 20;
	/**
	 * 图片显示的高度
	 */
	private double imageHeight = 20;
	
	/**
	 * 用于显示动画 表示显示动画的方块的下标 在TimeLine动画的显示期间index会不断增加 index的值代表着需要进行动画的方格的下标index
	 */
	private int index = 0;
	
	/**
	 * 用于装迷宫方块的面板 由于要形成立体的迷宫所以图片的大小不一，需要一列一列放置
	 */
	private FlowPane mazepane = new FlowPane();
	
	/**
	 * UI组件
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
	 * 迷宫大小选择栏 有"25×25","51×51"两种大小
	 */
	private String[] SizeTitle = {"25×25","51×51"};
	/**
	 * 迷宫大小选择框
	 */
	private ComboBox<String> cboSizeChoose = new ComboBox<>();
	ObservableList<String> sizeTitleItems;
	
	/**
	 * 寻路模式选择栏 有dfs bfs A* 三种寻路方式
	 */
	private String[] findWayModelTitle = {"dfs","bfs","A*"};
	/**
	 * 寻路模式选择框
	 */
	private ComboBox<String> cboModelChoose = new ComboBox<>();
	ObservableList<String> modelChooseItems;
	
	/**
	 * 选择的寻路模式
	 */
	FindWayable findWay;
	
	/**
	 * 用来记录原本路的图片
	 */
	private ArrayList<Image> originalRoadImage = new ArrayList<>();
	
	/**
	 * 存储迷宫的解法过程
	 */
	private Stack<SquareNode> solutionProcess;
	
	/**
	 * 存储迷宫的解法
	 */
	private Stack<SquareNode> solution;
	
	/**
	 * 判断起点是否被选择
	 */
	private boolean isStartChoose = true;
	
	/**
	 * 判断终点是否被选择
	 */
	private boolean isEndChoose = true;
	
	/**
	 * 对起点进行设置时记录原来的图片
	 */
	String originalStartImage;
	
	/**
	 * 对终点进行设置时记录原来的图片
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
		addWall(); // 参数为图片大小
		addUIComponents();   // 添加UI组件 即是 寻路 生成迷宫 单步 等按钮
	}
	
	
	/**
	 * 根据地图的大小将墙方格加入到面板中
	 */
	private void addWall() {
		mazepane.getChildren().clear();
		mazepane.setPrefSize(510, 510);
		mazepane.setAlignment(Pos.CENTER);
		mazepane.setOrientation(Orientation.VERTICAL);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				/**
				 * 加载墙的图片(墙的图片有两种绿色和灰白色)
				 */
				String[] imagefilename;
				if(j == width-1) {
					// 当到最后一排时为了变成立体图的需要用立体图片
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
				// 随机加入一个图片(白色或者绿色)
				Image image = new Image(imagefilename[index]);
				ImageView imageView = new ImageView(image);
				imageView.setFitHeight(imageHeight);
				imageView.setFitWidth(imageWidth);
				imageHeight = imageWidth;
				mazepane.getChildren().add(imageView);
				
				/**
				 * 起点终点的选择判断 起点是鼠标左键选择右键取消  终点是鼠标右键选择左键取消
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
				 * 当鼠标进入图片改变透明度
				 */
				imageView.setOnMouseEntered(e -> {
					imageView.setOpacity(0.5);
				});
				
				/**
				 * 当鼠标移出图片还原透明度
				 */
				imageView.setOnMouseExited(e -> {
					imageView.setOpacity(1);
				});
				
				/**
				 * 加入墙时的生成动画
				 */
				FadeTransition ft = new FadeTransition(Duration.millis(3000), imageView);
				ft.setFromValue(0.2);
				ft.setToValue(1);
				ft.setCycleCount(3);
				ft.setAutoReverse(true);
				ft.play();
			}
		}
		
		// 将mazepane设置为中心并居中
		super.setCenter(mazepane);
		BorderPane.setAlignment(mazepane, Pos.CENTER);
	}
	
	
	/**
	 * 更新起点终点位置
	 * @param originalImage 原始图片
	 * @param updateImage 需要更新的图片
	 */
	private void updateOriginalRoadImage(Image originalImage, Image updateImage) {
		originalRoadImage.set(originalRoadImage.indexOf(originalImage), updateImage);
	}

	
	/**
	 * 判断图片是否属于路径的图片
	 * @param imageView 图片的imageView
	 * @return Boolean类型 是路径图片则返回true 不是则返回false
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
	 * 判断图片是否属于起点的图片
	 * @param imageView 图片的imageView
	 * @return Boolean类型 是起点图片则返回true 不是则返回false
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
	 * 判断图片是否属于终点的图片
	 * @param imageView 图片的imageView
	 * @return Boolean类型 是终点图片则返回true 不是则返回false
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
	 * 将UI组件加入到面板中
	 */
	private void addUIComponents() {
		VBox vBox = new VBox(10);
		vBox.setPadding(new Insets(8,8,8,8));
		vBox.setAlignment(Pos.CENTER);
		super.setRight(vBox);
		BorderPane.setAlignment(vBox, Pos.CENTER);
		
		/**
		 * 自动生成迷宫的UI组件
		 */
		btnGenerateMaze.setToggleGroup(btnGroup);
		btnGenerateMaze.setDisable(false);
		btnGenerateMaze.setOnAction(e -> {
			originalRoadImage.clear();
			cerateMaze();
		});
		
		
		/**
		 * 手动导入迷宫的UI组件
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
		 * 寻路UI组件（不是最短路径）
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
		 * 寻路UI组件（是最短路径）
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
		 * 清除足迹
		 */
		btnFootprintClear.setToggleGroup(btnGroup);
		btnFootprintClear.setDisable(true);
		btnFootprintClear.setOnAction(e -> {
			if(isStartChoose == true && isEndChoose == true) {
				restoreMaze();
			}
		});
		
		
		/**
		 * 单步寻路
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
		 * 迷宫大小选择框
		 */
		BorderPane sizeChoosePane = new BorderPane();
		Label sizeChooseLab = new Label("Choose Size");
		BorderPane.setAlignment(sizeChooseLab, Pos.CENTER);
		sizeChoosePane.setTop(sizeChooseLab);
		sizeChoosePane.setCenter(cboSizeChoose);

		cboSizeChoose.setValue("25×25");
		sizeTitleItems = FXCollections.observableArrayList(SizeTitle);
		cboSizeChoose.getItems().addAll(sizeTitleItems);
		cboSizeChoose.setPrefWidth(90);	
		cboSizeChoose.setOnAction(e -> {
			CheckSizeChoose();
		});
		
		
		/**
		 * 寻路模式选择框
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
		 * 将组建加入vBox中
		 */
		vBox.getChildren().addAll(btnGenerateMaze, btnMazefile, modelChoosePane, btnFindWay, btnShortestWay, btnFootprintClear, btnStep, sizeChoosePane);
	}
	
	
	/**
	 * 还原当前迷宫
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
	 * 检测迷宫大小的选择 选择过后给各种属性赋值
	 */
	private void CheckSizeChoose() {
		int index = sizeTitleItems.indexOf(cboSizeChoose.getValue()); // 0 1 2 3 分别代表的迷宫大小为 25 51 101 201
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
		
		// 按钮的重置
		btnGenerateMaze.setDisable(false);
		btnFindWay.setDisable(true);
		btnShortestWay.setDisable(true);
		btnFootprintClear.setDisable(true);
		btnStep.setDisable(true);
	}
	
	
	/**
	 * 检测寻路选择的模式
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
	 * SquareNode类型结点中对step的设置
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
	 * 创建迷宫
	 */
	public void cerateMaze(){
		cerateNet();
		// 创建一个生成迷宫的实例
		Prim prim = new Prim(startX, startY, squareNodes);
		prim.RemoveWall(prim.getStartX(), prim.getStartY());
		
		RoadAnimation();
	}
	
	
	/**
	 * 形成网格即是生成一个一个的细胞
	 */
	private void cerateNet(){
		for (int i = 0; i < squareNodes.length; i++) {
			for (int j = 0; j < squareNodes[0].length; j++) {
				squareNodes[i][j] = new SquareNode();
				squareNodes[i][j].setX(j);
				squareNodes[i][j].setY(i);
				if((i * j)%2 == 0){
					squareNodes[i][j].setRoad(false); // 设置为墙
				}
				else{
					squareNodes[i][j].setRoad(true);  // 设置为路
				}
			}
		}	
	}
	
	
	/**
	 * 当Prim算法将迷宫生成完成后每个方格是路还是墙都已经记录好
	 * Roadanimation则是用来将已经记录好的数据进行动画显现
	 * 同样是运用了SequentialTransition进行动画的先后显示 先是消逝后是显现
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
	 * 选择当前节点应该显示的图片
	 * @param imageView 需要改变图片的结点
	 * @param index 节点在面板中的下标
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
			// 表示路径的图片
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
				// 用来随机图片下标
				int randomIndex = (int)(Math.random()*filename.length);
				image = new Image(filename[randomIndex]);
				imageView.setImage(image);
				imageView.setFitHeight(imageHeight);
				imageView.setFitWidth(imageWidth);
			}
			
			originalRoadImage.add(image);
			
			if(index/width == startX && index%width == startY){
				// 存储起点原来的图片地址
				originalStartImage = image.impl_getUrl();
				Image startImage;
				// 长宽相等时载入正方形图片 反之载入半张图片
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
				// 存储终点原来的图片地址
				originalEndImage = image.impl_getUrl();
				
				Image endImage;
				// 长宽相等时载入正方形图片 反之载入半张图片
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
	 * 当寻路算法已经将路径记录好后，则用Wayanimation进行路径的动画显示
	 * 同样是运用了SequentialTransition进行动画的先后显示 先是消逝后是显现
	 * @param findWay 寻找路径的方法
	 * @param type int类型 
	 * 参数type值为0表示A*算法 
	 * 参数type值为1表示广度优先搜索 
	 * 参数type值为2表示深度优先搜索 
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
			 // 图片文件名
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