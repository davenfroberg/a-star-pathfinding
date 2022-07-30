package ui;

import model.Node;

public class Pathfinder {

    public int xDim = 30, yDim = 10;
    public Node[][] map = new Node[xDim][yDim];
    public int targPos, startPos;
    public Node[] openList = new Node[xDim * yDim];
    public Node[] closedList = new Node[xDim * yDim];

    public Pathfinder() {
        init();
        int openCounter = 1; //the start node is already in the open list
        int closedCounter = 0;
        boolean pathFound = true;
        Node current = map[0][0];

        while (lowestFCost() != null) {

            current = lowestFCost();
            openList[findInOpen(current)] = null;
            current.setOpen(false);
            closedList[closedCounter++] = current;
            current.setClosed(true);

            if (current.getPosition() == targPos) {
                pathFound = true;
                break;
            }
            //Find all the neighbouring nodes
            Node neighbour;
            for (int y = -1; y < 2; y++)
                for (int x = -1; x < 2; x++) {
                    if (x == 0 && y == 0)
                        continue;
                    if (current.getxPos() + x < 0 || current.getxPos() + x > xDim - 1)
                        continue;
                    if (current.getyPos() + y < 0 || current.getyPos() + y > yDim - 1)
                        continue;
                    neighbour = map[current.getxPos() + x][current.getyPos() + y];
                    int distance = 0;
                    if (x == 0 || y == 0)
                        distance = 10;
                    else
                        distance = 14;

                    if (neighbour != null && !neighbour.isWall() && !neighbour.isClosed())
                        if (!neighbour.isOpen() || neighbour.getgCost() > current.getgCost() + distance) {
                            neighbour.setgCost(current.getgCost() + distance);
                            neighbour.setParent(current);
                            if (!neighbour.isOpen()) {
                                openList[openCounter++] = neighbour;
                                neighbour.setOpen(true);
                            }
                        }
                }
        }
        if (!pathFound)
            System.out.println("There is NO Possible Path");

        else {
            System.out.println("A Path Has Been Found Successfuly!\n");
            setFinalPath(current);
            System.out.println("Length of Shortest Path: " + current.getgCost() + " units");
        }
        displayMap();
    }

    private void init() {
        assignStartAndTarget();
        createMap();
        assignHCost();
        openList[0] = findStart();
        findStart().setOpen(true);
    }

    private void assignStartAndTarget() {
        targPos = (int) (Math.random() * (xDim * yDim)); // position that is, at max, the size of the grid
        do {
            startPos = (int) (Math.random() * (xDim * yDim)); // position that is, at max, the size of the grid
        } while (startPos == targPos); // start pos and target pos can't be the same
    }

    //	Creates the entire map
    public void createMap() {
        int counter = 0;
        for (int y = 0; y < yDim; y++) {
            for (int x = 0; x < xDim; x++) {
                boolean wall = false;
                if (((int) (Math.random() * 100) > 70) && counter != targPos && counter != startPos)
                    wall = true;
                if (counter == targPos)
                    map[x][y] = new Node(counter, wall, true, x, y);
                else
                    map[x][y] = new Node(counter, wall, false, x, y);
                counter++;
            }
        }
    }

    //	Displays the map
    public void displayMap() {
        for (int y = 0; y < yDim; y++) {
            for (int x = 0; x < xDim; x++) {
                if (map[x][y].isTarget())
                    System.out.print("\u25B2");
                else if (map[x][y].getxPos() == findStart().getxPos() && map[x][y].getyPos() == findStart().getyPos())
                    System.out.print("\u263A");
                else if (map[x][y].isPath())
                    System.out.print("X");
                else if (map[x][y].isWall())
                    System.out.print("\u25A1");
                else
                    System.out.print("-");
            }
            System.out.println();
        }
    }

    //	Calculates the hCost for a particular node
    public int findHCost(Node temp) {
        int hCost = 0;
        Node target = findTarget();
        boolean left, right, up, down;
        while (temp.getxPos() != target.getxPos() || temp.getyPos() != target.getyPos()) {
            left = false;
            right = false;
            up = false;
            down = false;
            if (temp.getxPos() > target.getxPos())
                left = true;
            else if (temp.getxPos() < target.getxPos())
                right = true;
            if (temp.getyPos() > target.getyPos())
                up = true;
            else if (temp.getyPos() < target.getyPos())
                down = true;

            if (up)
                if (left) {
                    temp = map[temp.getxPos() - 1][temp.getyPos() - 1];
                    hCost += 14;
                } else if (right) {
                    temp = map[temp.getxPos() + 1][temp.getyPos() - 1];
                    hCost += 14;
                } else {
                    temp = map[temp.getxPos()][temp.getyPos() - 1];
                    hCost += 10;
                }
            else if (down)
                if (left) {
                    temp = map[temp.getxPos() - 1][temp.getyPos() + 1];
                    hCost += 14;
                } else if (right) {
                    temp = map[temp.getxPos() + 1][temp.getyPos() + 1];
                    hCost += 14;
                } else {
                    temp = map[temp.getxPos()][temp.getyPos() + 1];
                    hCost += 10;
                }

            else if (right) {
                temp = map[temp.getxPos() + 1][temp.getyPos()];
                hCost += 10;
            } else if (left) {
                temp = map[temp.getxPos() - 1][temp.getyPos()];
                hCost += 10;
            }
        }
        return hCost;
    }

    //	Assigns the hCost for the entire map
    public void assignHCost() {
        for (int y = 0; y < yDim; y++)
            for (int x = 0; x < xDim; x++)
                map[x][y].sethCost(findHCost(map[x][y]));
    }

    //	Troubleshooting method to display all hCosts on the map
    public void displayHCosts() {
        for (int y = 0; y < yDim; y++) {
            for (int x = 0; x < xDim; x++)
                System.out.print(map[x][y].gethCost() + " \t");
            System.out.println();
        }
    }

    //	Finds the position of a node in the openList
    public int findInOpen(Node look) {
        for (int x = 0; x < openList.length; x++)
            if (openList[x] != null && openList[x].getPosition() == look.getPosition())
                return x;
        return -1;
    }

    //	Finds the starting node of the map
    public Node findStart() {
        for (int y = 0; y < yDim; y++)
            for (int x = 0; x < xDim; x++)
                if (map[x][y].getPosition() == startPos)
                    return map[x][y];

        return map[0][0];
    }

    //	Finds and returns the current node
    public Node findCurrent() {
        for (int y = 0; y < yDim; y++)
            for (int x = 0; x < xDim; x++)
                if (map[x][y].isCurrent())
                    return map[x][y];

        return map[0][0];
    }

    //	Finds and returns the target node
    public Node findTarget() {
        for (int y = 0; y < yDim; y++)
            for (int x = 0; x < xDim; x++)
                if (map[x][y].isTarget())
                    return map[x][y];

        return map[0][0];
    }

    //	Finds the best node in the openList
    public Node lowestFCost() {
        int fCost = 0;
        for (int x = 0; x < openList.length; x++) {
            if (openList[x] != null) {
                fCost = openList[x].getfCost();
                break;
            }
            if (x == openList.length - 1)
                return null;
        }
        Node temp[] = new Node[openList.length];
        int tempCounter = 0;
        // Finds smallest fCost
        for (int x = 1; x < openList.length; x++)
            if (openList[x] != null && openList[x].getfCost() < fCost)
                fCost = openList[x].getfCost();

        // Finds all instances of the smallest fCost
        for (int x = 0; x < openList.length; x++)
            if (openList[x] != null && openList[x].getfCost() == fCost)
                temp[tempCounter++] = openList[x];

        // Finds the first instance of the lowest hCost in the list of smallest fCosts
        int hCost = temp[0].gethCost();
        int tempPosition = 0;
        if (temp[1] != null) {
            for (int x = 1; x < temp.length; x++) {
                if (temp[x] == null)
                    break;
                if (temp[x].gethCost() < hCost) {
                    hCost = temp[x].gethCost();
                    tempPosition = x;
                }
            }
            return temp[tempPosition];
        } else
            return temp[0];
    }

    //	Assigns the path after finding the shortest
    public void setFinalPath(Node target) {
        Node current = target;
        while (true) {
            current = current.getParent();
            if (current.getPosition() != startPos)
                current.setPath(true);
            else
                break;
        }
    }
}
