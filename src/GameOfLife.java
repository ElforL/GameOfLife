public class GameOfLife {
    final int height, width;
    private boolean[][] cells;
    private boolean[][] nextGen;

    public GameOfLife(int height, int width) {
        this.height = height;
        this.width = width;
        cells = new boolean[height][width];
        nextGen = new boolean[height][width];
    }

    public boolean[][] getCells(){ return cells; }

    void tick() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e){}

        for (int i = 0; i < cells.length; i++) {
        for (int j = 0; j < cells[i].length; j++) {
            nextGen[i][j] = nextStateOf(j, i);
        }}

        cells = nextGen;
        nextGen = new boolean[height][width];
    }

    void setCell(boolean state, int x, int y){
        cells[y][x] = state;
    }

    /** 
     * Any live cell with fewer than two live neighbours dies, as if by underpopulation.
     * Any live cell with more than three live neighbours dies, as if by overpopulation.
     * Any live cell with two or three live neighbours lives on to the next generation.
     * Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
     */
    boolean nextStateOf(int x, int y){
        int neighbours = countNeighbours(x,y);

        if(cells[y][x]){
            // underpopulation and overpopulation
            if(neighbours < 2 || neighbours > 3)
                return false;

            // lives on
            else return true;
        }else{
            // reproduction
            if(neighbours == 3) return true;
            
            // Stay dead
            else return false;
        }
    }

    int countNeighbours(int x, int y){
        int count = 0;

        for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            if(i == 0 && j==0) continue;
            try{
                count += cells[y+i][x+j] ? 1 : 0;
            }catch(ArrayIndexOutOfBoundsException e){
                continue;
            }
        }}

        return count;
    }
}
