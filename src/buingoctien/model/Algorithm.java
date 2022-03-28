package buingoctien.model;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Algorithm {
	private int row;
	private int col;
	private int notBarrier = 0;
	private int[][] matrix;

	public Algorithm(int row, int col) {
		this.row = row;
		this.col = col;
		createMatrix();
		showMatrix();

	}

	// đọc ma trận từ file input
	private void readFile() {
		File fileInput = new File(getClass().getResource("/input").getFile());
		try {
			Scanner scan = new Scanner(fileInput);
			matrix = new int[row][col];
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					matrix[i][j] = scan.nextInt();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// show matrix
	public void showMatrix() {
		for (int i = 1; i < row - 1; i++) {
			for (int j = 1; j < col - 1; j++) {
				System.out.printf("%3d", matrix[i][j]);
			}
			System.out.println();
		}
	}

	// kiểm tra với dòng x, từ cột y1 đến y2. Nếu đúng thì trả về true, sai trả về
	// false
	private boolean checkLineX(int y1, int y2, int x) {
		System.out.println("check line x");
		// tìm điểm có cột min và max
		int min = Math.min(y1, y2);
		int max = Math.max(y1, y2);
		for (int y = min + 1; y < max; y++) {
			if (matrix[x][y] > notBarrier) {
				// Nếu thấy rào cản (tức là giữa 2 điểm xét còn có 1 điểm khác thì k thỏa mãn)
				System.out.println("Not ok: " + x + "" + y);
				return false;
			}
			System.out.println("ok: " + x + "" + y);
		}
		// thỏa mãn
		return true;
	}

	// kiểm tra với cột y, từ dòng x1 đến x2
	private boolean checkLineY(int x1, int x2, int y) {
		System.out.println("check line y");
		int min = Math.min(x1, x2);
		int max = Math.max(x1, x2);
		for (int x = min + 1; x < max; x++) {
			if (matrix[x][y] > notBarrier) {
				System.out.println("Not ok: " + x + "" + y);
				return false;
			}
			System.out.println("ok: " + x + "" + y);
		}
		return true;
	}

	// Kiểm tra trong hình chữ nhật theo dòng, nếu đúng thì trả về cột, sai thì trả
	// về -1
	private int checkRectX(Point p1, Point p2) {
		System.out.println("check rect x");
		// Tìm điểm có y min và y max
		Point pMinY = p1, pMaxY = p2;
		if (p1.y > p2.y) {
			pMinY = p2;
			pMaxY = p1;
		}
		for (int y = pMinY.y; y <= pMaxY.y; y++) {
			if (y > pMinY.y && matrix[pMinY.x][y] > notBarrier) {
				return -1;
			}
			if ((matrix[pMaxY.x][y] == notBarrier || y == pMaxY.y) && checkLineY(pMinY.x, pMaxY.x, y)
					&& checkLineX(y, pMaxY.y, pMaxY.x)) {

				System.out.println("Rect x");
				System.out.println("(" + pMinY.x + "," + pMinY.y + ") -> (" + pMinY.x + "," + y + ") -> (" + pMaxY.x
						+ "," + y + ") -> (" + pMaxY.x + "," + pMaxY.y + ")");
				return y;
			}
		}
		return -1;
	}

	// Kiểm tra trong hình chữ nhật theo cột, nếu đúng thì trả về dòng, sai thì trả
	// về -1
	private int checkRectY(Point p1, Point p2) {
		System.out.println("check rect y");
		Point pMinX = p1, pMaxX = p2;
		if (p1.x > p2.x) {
			pMinX = p2;
			pMaxX = p1;
		}
		for (int x = pMinX.x; x <= pMaxX.x; x++) {
			if (x > pMinX.x && matrix[x][pMinX.y] > notBarrier) {
				return -1;
			}
			if ((matrix[x][pMaxX.y] == notBarrier || x == pMaxX.x) && checkLineX(pMinX.y, pMaxX.y, x)
					&& checkLineY(x, pMaxX.x, pMaxX.y)) {

				System.out.println("Rect y");
				System.out.println("(" + pMinX.x + "," + pMinX.y + ") -> (" + x + "," + pMinX.y + ") -> (" + x + ","
						+ pMaxX.y + ") -> (" + pMaxX.x + "," + pMaxX.y + ")");
				return x;
			}
		}
		return -1;
	}

	/*
	 * Kiểm tra mở rộng theo dòng p1 và p2 là điểm muốn kiểm tra type: 1 là kiểm tra
	 * với cột tăng, -1 là kiểm tra với cột giảm
	 * 
	 */
	private int checkMoreLineX(Point p1, Point p2, int type) {
		System.out.println("check extend x");
		Point pMinY = p1, pMaxY = p2;
		if (p1.y > p2.y) {
			pMinY = p2;
			pMaxY = p1;
		}
		int y = pMaxY.y + type;
		int row = pMinY.x;
		int colFinish = pMaxY.y;
		if (type == -1) {
			colFinish = pMinY.y;
			y = pMinY.y + type;
			row = pMaxY.x;
			System.out.println("colFinish = " + colFinish);
		}

		if ((matrix[row][colFinish] == notBarrier || pMinY.y == pMaxY.y) && checkLineX(pMinY.y, pMaxY.y, row)) {
			while (matrix[pMinY.x][y] == notBarrier && matrix[pMaxY.x][y] == notBarrier) {
				if (checkLineY(pMinY.x, pMaxY.x, y)) {

					System.out.println("TH X " + type);
					System.out.println("(" + pMinY.x + "," + pMinY.y + ") -> (" + pMinY.x + "," + y + ") -> (" + pMaxY.x
							+ "," + y + ") -> (" + pMaxY.x + "," + pMaxY.y + ")");
					return y;
				}
				y += type;
			}
		}
		return -1;
	}

	private int checkMoreLineY(Point p1, Point p2, int type) {
		System.out.println("check extend y");
		Point pMinX = p1, pMaxX = p2;
		if (p1.x > p2.x) {
			pMinX = p2;
			pMaxX = p1;
		}
		int x = pMaxX.x + type;
		int col = pMinX.y;
		int rowFinish = pMaxX.x;
		if (type == -1) {
			rowFinish = pMinX.x;
			x = pMinX.x + type;
			col = pMaxX.y;
		}
		if ((matrix[rowFinish][col] == notBarrier || pMinX.x == pMaxX.x) && checkLineY(pMinX.x, pMaxX.x, col)) {
			while (matrix[x][pMinX.y] == notBarrier && matrix[x][pMaxX.y] == notBarrier) {
				if (checkLineX(pMinX.y, pMaxX.y, x)) {
					System.out.println("TH Y " + type);
					System.out.println("(" + pMinX.x + "," + pMinX.y + ") -> (" + x + "," + pMinX.y + ") -> (" + x + ","
							+ pMaxX.y + ") -> (" + pMaxX.x + "," + pMaxX.y + ")");
					return x;
				}
				x += type;
			}
		}
		return -1;
	}

	public MyLine checkTwoPoint(Point p1, Point p2) {
		if (!p1.equals(p2) && matrix[p1.x][p1.y] == matrix[p2.x][p2.y]) {
			// Kiểm tra 2 điểm cùng dòng
			if (p1.x == p2.x) {
				System.out.println("line x");
				if (checkLineX(p1.y, p2.y, p1.x)) {
					return new MyLine(p1, p2);
				}
			}
			// Kiểm tra 2 điểm cùng cột
			if (p1.y == p2.y) {
				System.out.println("line y");
				if (checkLineY(p1.x, p2.x, p1.y)) {
					System.out.println("ok line y");
					return new MyLine(p1, p2);
				}
			}

			int t = -1; // t là cột tìm hoặc hàng tìm

			// Kiểm tra hình chữ nhật theo chiều ngang
			if ((t = checkRectX(p1, p2)) != -1) {
				System.out.println("rect x");
				return new MyLine(new Point(p1.x, t), new Point(p2.x, t));
			}

			// Kiểm tra hình chữ nhật theo chiều dọc
			if ((t = checkRectY(p1, p2)) != -1) {
				System.out.println("rect y");
				return new MyLine(new Point(t, p1.y), new Point(t, p2.y));
			}
			// Kiểm tra mở rộng về phía bên phải
			if ((t = checkMoreLineX(p1, p2, 1)) != -1) {
				System.out.println("more right");
				return new MyLine(new Point(p1.x, t), new Point(p2.x, t));
			}
			// Kiểm tra mở rộng về bên trái
			if ((t = checkMoreLineX(p1, p2, -1)) != -1) {
				System.out.println("more left");
				return new MyLine(new Point(p1.x, t), new Point(p2.x, t));
			}
			// Kiểm tra mở rộng lên phía trên
			if ((t = checkMoreLineY(p1, p2, 1)) != -1) {
				System.out.println("more down");
				return new MyLine(new Point(t, p1.y), new Point(t, p2.y));
			}
			// Kiểm tra mở rộng xuống phía dưới
			if ((t = checkMoreLineY(p1, p2, -1)) != -1) {
				System.out.println("more up");
				return new MyLine(new Point(t, p1.y), new Point(t, p2.y));
			}
		}
		return null;
	}

	private void createMatrix() {

		matrix = new int[row][col];
		for (int i = 0; i < col; i++) {
			matrix[0][i] = matrix[row - 1][i] = 0;
		}
		for (int i = 0; i < row; i++) {
			matrix[i][0] = matrix[i][col - 1] = 0;
		}

		Random rand = new Random();
		int imgNumber = 37;
		int maxDouble = imgNumber / 4;
		int imgArr[] = new int[imgNumber + 1];
		ArrayList<Point> listPoint = new ArrayList<Point>();
		for (int i = 1; i < row - 1; i++) {
			for (int j = 1; j < col - 1; j++) {
				listPoint.add(new Point(i, j));
			}
		}
		int i = 0;
		do {
			int imgIndex = rand.nextInt(imgNumber) + 1; // chỉ số của img, bắt đầu từ 1
			if (imgArr[imgIndex] < maxDouble) {
				imgArr[imgIndex] += 2;
				for (int j = 0; j < 2; j++) {
					try {
						int size = listPoint.size();
						int pointIndex = rand.nextInt(size);
						matrix[listPoint.get(pointIndex).x][listPoint.get(pointIndex).y] = imgIndex;
						listPoint.remove(pointIndex);
					} catch (Exception e) {

					}
				}
				i++;
			}
		} while (i < row * col / 2);

	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}
}
