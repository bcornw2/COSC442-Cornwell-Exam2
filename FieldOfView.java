package game;

public class FieldOfView {
	private World world;
	private int depth;
	
	private boolean[][] visible;
	public boolean isVisible(int x, int y, int z){
		return z == depth && x >= 0 && y >= 0 && x < visible.length && y < visible[0].length && visible[x][y];
	}
	
	private Tile[][][] tiles;
	public Tile tile(int x, int y, int z){
		return tiles[x][y][z];
	}
	
	public FieldOfView(World world){
		this.world = world;
		this.visible = new boolean[world.width()][world.height()];
		this.tiles = new Tile[world.width()][world.height()][world.depth()];
		
		for (int x = 0; x < world.width(); x++){
			for (int y = 0; y < world.height(); y++){
				for (int z = 0; z < world.depth(); z++){
					tiles[x][y][z] = Tile.UNKNOWN;
				}
			}
		}
	}
	
	public void update(int wx, int wy, int wz, int r){
		depth = wz;
		visible = new boolean[world.width()][world.height()];
		
		worldHeightLength(wx, wy, wz, r);
	}

	private void worldHeightLength(int wx, int wy, int wz, int r) {
		widthHeightNested(wx, wy, wz, r);
	}

	private void widthHeightNested(int wx, int wy, int wz, int r) {
		for (int x = -r; x < r; x++){
			ifNewLinePoint(wx, wy, wz, r, x);
		}
	}

	private void ifNewLinePoint(int wx, int wy, int wz, int r, int x) {
		for (int y = -r; y < r; y++){
			if (ifStats(r, x, y)) {
				continue;
			}
			
			if (ifWidthLength(wx, wy, x, y)) {
				continue;
			}
			
			newLinePoint(wx, wy, wz, x, y);
		}
	}

	private boolean ifStats(int r, int x, int y) {
		return x*x + y*y > r*r;
	}

	private boolean ifWidthLength(int wx, int wy, int x, int y) {
		return wx + x < 0 || wx + x >= world.width() || wy + y < 0 || wy + y >= world.height();
	}

	private void newLinePoint(int wx, int wy, int wz, int x, int y) {
		for (Point p : new Line(wx, wy, wx + x, wy + y)){
			Tile tile = world.tile(p.x, p.y, wz);
			visible[p.x][p.y] = true;
			tiles[p.x][p.y][wz] = tile; 
			
			if (!tile.isGround())
				break;
		}
	}
}
