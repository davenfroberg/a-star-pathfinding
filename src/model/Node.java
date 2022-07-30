package model;

public class Node {

    private int gCost, hCost;
    private int position;
    private boolean wall;
    private boolean target;
    private int xPos, yPos;
    private boolean open;
    private boolean closed;
    private Node parent;
    private boolean path;

    public Node(int position, boolean wall, boolean target, int xPos, int yPos) {
        this.position = position;
        this.wall = wall;
        this.target = target;
        this.xPos = xPos;
        this.yPos = yPos;
        gCost = 0;
        hCost = 0;
        open = false;
        closed = false;
        path = false;
        parent = null;
    }

    public boolean isPath() {
        return path;
    }

    public void setPath(boolean path) {
        this.path = path;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public boolean isTarget() {
        return target;
    }

    public int getgCost() {
        return gCost;
    }

    public void setgCost(int gCost) {
        this.gCost = gCost;
    }

    public int gethCost() {
        return hCost;
    }

    public void sethCost(int hCost) {
        this.hCost = hCost;
    }

    public int getfCost() {
        return (hCost + gCost);
    }

    public int getPosition() {
        return position;
    }

    public boolean isWall() {
        return wall;
    }
}
