package ravensproject.Utils;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;
import ravensproject.RavensProblem;
import ravensproject.models.Image.Coordinate;
import ravensproject.models.Image.IdentifiedObject;
//import ravensproject.models.Image.RavensObject;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Given a RavensFigure image, extract shapes from PNG images and create
 * verbal description of the RavensFigure
 *
 * Created by guoliangwang on 7/18/15.
 */

public class ImageIdentifier {
    private RavensFigure ravensFigure;
    private ArrayList<RavensObject> ravensObjects;
    private RavensProblem ravensProblem;
    private String problemSetName; //e.g. Basic Problems B
    private String figureName;
    private BufferedImage bufferedImage;

    public ImageIdentifier() {}


    /**
     * convert the PNG image to RavensFigure object
     * @return
     */
    public RavensFigure convertImageToRF() {

        RavensProblem problem = this.ravensProblem;
        String figureName = this.figureName;
        String problemSetName = this.problemSetName;
        BufferedImage inputImage = this.bufferedImage;

        this.ravensFigure = new RavensFigure(figureName, problem.getName(), problemSetName);
        this.ravensObjects = new ArrayList<>();
        int numObject = 0;

//        int topRightX = inputImage.getWidth();
//        int topRightY = inputImage.getHeight();
//        int bottomLeftX = inputImage.getMinX();
//        int bottomLeftY = inputImage.getMinY();

        Coordinate bottomRightPoint = new Coordinate(inputImage.getWidth(), inputImage.getHeight());
        Coordinate topLeftPoint = new Coordinate(inputImage.getMinX(), inputImage.getMinY());

        //this pixcel Matrix uses 1 for black and - for white pixels (binary colors)
        int[][]pixelMatrix = new int[bottomRightPoint.getY() - topLeftPoint.getY()][bottomRightPoint.getX() - topLeftPoint.getX()];

        //fill the pixelMatrix, from top to bottom, left to right (upperLeft corner has (0, 0) coordinate)
        for (int y = topLeftPoint.getY(); y < bottomRightPoint.getY(); y++) {
            for (int x = topLeftPoint.getX(); x < bottomRightPoint.getX(); x++) {
                int rgbValue = inputImage.getRGB(x, y);
                int red = (rgbValue) & 0xFF;
                int green = (rgbValue>>8) & 0xFF;
                int blue = (rgbValue>>16) & 0xFF;
                int currColor = (rgbValue != -1) ? 1 : 0; //1 for black, 0 for white
                // int currColor = (red == 0 && green == 0 && blue == 0) ? 1 : 0; //1 for black, 0 for white
                pixelMatrix[y][x] = currColor;
            }
        }
        
        //copied below
        for(int y = topLeftPoint.getY(); y < bottomRightPoint.getY(); y++)  {
            for(int x = topLeftPoint.getX(); x < bottomRightPoint.getX(); x++)  {
                int leftOf = 0;
                int topLeftOf = 0;
                int topLeftOfPlus1 = 0;
                int topLeftOfPlus2 = 0;
                int topLeftOfPlus3 = 0;
                int topOf = 0;
                int topRightOf = 0;
                int topRightOfPlus1 = 0;
                int topRightOfPlus2 = 0;
                int topRightOfPlus3 = 0;

                try {
                    leftOf = pixelMatrix[y][x-1];
                    topLeftOf = pixelMatrix[y-1][x-1];
                    topLeftOfPlus1 = pixelMatrix[y-1][x-2];
                    topLeftOfPlus2 = pixelMatrix[y-1][x-3];
                    topLeftOfPlus3 = pixelMatrix[y-1][x-7];
                    topOf = pixelMatrix[y-1][x];
                    topRightOf = pixelMatrix[y-1][x+1];
                    topRightOfPlus1 = pixelMatrix[y-1][x+2];
                    topRightOfPlus2 = pixelMatrix[y-1][x+3];
                    topRightOfPlus3 = pixelMatrix[y-1][x+7];
                }catch(ArrayIndexOutOfBoundsException e) {

                }
                int current = pixelMatrix[y][x];

                //check to see if we ran into the top of a new shape
                if(current == 1 && (leftOf == 0 && topLeftOf == 0 && topOf == 0 && topRightOf == 0 &&
                        topRightOfPlus1 == 0 && topLeftOfPlus1 == 0  &&  topRightOfPlus2 == 0 && topLeftOfPlus2 == 0  &&
                        topRightOfPlus3 == 0 && topLeftOfPlus3 == 0)) {

                    //Double check there is nothing to the left
                    boolean foundaOne = false;
                    for(int toLeft = x-1; toLeft > 0; toLeft--) {
                        int leftOfCheck = pixelMatrix[y][toLeft];
                        if(leftOfCheck ==  1) {
                            foundaOne = true;
                            break;
                        }
                    }
                    if(!foundaOne)  {
                        //New shape has been found - create an object to represent it
                        numObject++;
                        RavensObject ravenObj = new RavensObject(String.valueOf(numObject));


                        //Start to figure out what this shape is by seeing how far right it goes
                        int topRight = x;
                        int topLeft = x;
                        while(pixelMatrix[y][topRight] == 1) {
                            topRight++;
                        }

                        //Now that we know how wide the top is, shoot down the middle to find the height
                        int height = 0;
                        int objectTop = y;
                        int middle = topLeft + ((topRight - topLeft) / 2);


                        //Next we need to find the height, so start following the outline of the shape
                        //until we run into the line that goes down the center of the shape.
                        int[] cords =  new int[2];
                        int[] last = new int[2];
                        //X Coordinate
                        cords[0] = topRight;

                        //Y Coordinate
                        cords[1] = objectTop;
                        last = cords;
                        int curveCount = 0;
                        ArrayList<Coordinate> edges = new ArrayList<Coordinate>();
                        while(true) {
                            int[] tmp = findNextInOutline(cords, pixelMatrix, last);
                            last = cords;
                            cords = tmp;
                            edges.add(new Coordinate(cords[0], cords[1]));
                            if(cords[0] != last[0] && cords[1] != last[1]) {
                                curveCount++;
                            }
                            if(cords[0] == middle && Math.abs(cords[1] - objectTop) > 5)  {
                                //found our way to the bottom middle
                                break;
                            }else if(cords[0] == -99 && cords[1] == -99)  {
                                //found our way into a pickle....
                                cords = last;
                                cords[0] = cords[0] + 1;
                                cords[1] = cords[1] + 1;
                                break;
                            }

                        }

                        height = cords[1] - objectTop;

                        //From the height we know where the bottom is, so find out how wide the bottom is
                        int bottomLeft = middle;
                        int bottomRight = middle;

                        //See how far right we can go from the bottom middle point
                        while(pixelMatrix[objectTop + (height)][bottomRight] == 1) {
                            bottomRight++;
                        }

                        //See how far left we can go from the bottom middle point
                        while(pixelMatrix[objectTop + (height)][bottomLeft] == 1) {
                            bottomLeft--;
                        }

                        //From the height we know where the bottom is, so find out how wide the middle is
                        int middleRight = middle;
                        int centerY = objectTop + (height/2);

                        //See how far right we can go from the bottom middle point
                        for(Coordinate pair : edges)  {
                            if(pair.getY() == centerY && middleRight < pair.getX()) {
                                middleRight = pair.getX();
                            }
                        }

                        int middleLeft = middle - (middleRight - middle);

                        //try to figure out if the object is filled or not
                        int fillCheck = 1;
                        boolean isFilled = false;
                        while(pixelMatrix[objectTop+fillCheck][middle] == 1) {
                            fillCheck++;
                        }
                        isFilled = fillCheck > 5;

                        //See how likely this object is to have followed a curved path
                        boolean followsCurve = curveCount > 10;

                        int topWidth = topRight - topLeft;
                        int bottomWidth = bottomRight - bottomLeft;
                        int middleWidth = middleRight - middleLeft;

                        IdentifiedObject object = new IdentifiedObject(new Coordinate(topLeft, objectTop), new Coordinate(topRight, objectTop),
                                new Coordinate(middleLeft, (objectTop + (height/2))), new Coordinate(middleRight, (objectTop + (height/2))),
                                new Coordinate(bottomLeft, (objectTop + height)), new Coordinate(bottomRight, (objectTop + height)), topWidth,
                                middleWidth, bottomWidth, height, followsCurve, isFilled);


                        this.ravensObjects.add(populateObject(ravenObj, object));
                        //Mark the edges so that we can recognize them
                        for(Coordinate pair : edges) {
                            try {
                                pixelMatrix[pair.getY()][pair.getX()] = numObject;
                            } catch(ArrayIndexOutOfBoundsException e) {

                            }
                        }
                    }
                }
            }
        }
        
        
        //copied above

        return this.ravensFigure;
    }

    public RavensFigure getRavensFigure() {
        return ravensFigure;
    }

    public void setRavensFigure(RavensFigure ravensFigure) {
        this.ravensFigure = ravensFigure;
    }

    public ArrayList<RavensObject> getRavensObjects() {
        return ravensObjects;
    }

    public void setRavensObjects(ArrayList<RavensObject> ravensObjects) {
        this.ravensObjects = ravensObjects;
    }

    public RavensProblem getRavensProblem() {
        return ravensProblem;
    }

    public void setRavensProblem(RavensProblem ravensProblem) {
        this.ravensProblem = ravensProblem;
    }

    public String getProblemSetName() {
        return problemSetName;
    }

    public void setProblemSetName(String problemSetName) {
        this.problemSetName = problemSetName;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public String getFigureName() {
        return figureName;
    }

    public void setFigureName(String figureName) {
        this.figureName = figureName;
    }

    /**********************************************************************************
     * Find the next pixel in the outline of the shape.  Assumes that we are moving
     * from top-center to bottom-center
     * @param cords - int[], where x-Cord is 0 and y-Cord is 1
     * @param map - int[][], representation of the image's pixels (0=white, 1=black)
     *
     * @return int[] - where x-Cord is 0, y-Cord is 1
     ***********************************************************************************/
    private int[] findNextInOutline(int[] cords, int[][] map, int[] last) {
        int[] toReturn = new int[2];
        int x =  cords[0];
        int y = cords[1];
        //Start looking for the next pixel in the outline, give preference to going 
        //right and down since we are starting at the top and working to the bottom going right

        try {
            //look to the right
            if(map[y][x+1] == 1 && (last[0] != (x+1))) {
                toReturn[0] = x+1;
                toReturn[1] = y;

                //look down and to the right
            }else if(map[y+1][x+1] == 1  && (last[0] != (x+1) && last[1] != (y+1))) {
                toReturn[0] = x+1;
                toReturn[1] = y+1;

                //Look down
            }else if(map[y+1][x] == 1  && (last[1] != (y+1))) {
                toReturn[0] = x;
                toReturn[1] = y+1;

                //look down and to the left
            }else if(map[y+1][x-1] == 1  && (last[0] != (x-1) && last[1] != (y+1))) {
                toReturn[0] = x-1;
                toReturn[1] = y+1;

                //look to the left  -- Should this be removed??
            }else if(map[y][x-1] == 1  && (last[0] != (x-1) && last[1] != (y))) {
                toReturn[0] = x-1;
                toReturn[1] = y;

                //look ?
            }else {
                //Ok.... there isn't a clear path forward when only looking one pixel away, stretch it out....
                //Find the farthest right 1 that is cloest to the last x in the next row and call it good;
                boolean oneFound = false;
                int bestX = 0;
                for(int i = 0; i < map[y+1].length; i++) {
                    if(map[y+1][i] == 1)  {
                        if(i > bestX && ((Math.abs(bestX - i) <= Math.abs(bestX - x)))) {
                            bestX = i;
                            oneFound = true;
                        }
                    }
                }


                //No 1 exists in the next row, so look in the same row, but move left
                if(!oneFound)  {
                    for(int i = 0; i < map[y].length; i++) {
                        if(map[y][i] == 1)  {
                            if(i > bestX && ((Math.abs(bestX - i) <= Math.abs(bestX - x)))) {
                                bestX = i;
                            }
                        }
                    }
                    toReturn[1] = y;
                    toReturn[0] = bestX-1;
                }else {
                    toReturn[1] = y+1;
                    toReturn[0] = bestX;
                }

            }
        } catch(ArrayIndexOutOfBoundsException e) {
            //System.out.println("Index Out Of Bounds Finding Next 1");
            toReturn[0] = -99;
            toReturn[1] = -99;
        }
        return toReturn;
    }

    /********************************************************************************
     * Create a RavensObject from the infromation gathered to map out an object
     * The only attributes supported at this point are fill, size and shape
     * This method depends on pass by reference.
     *
     * @param ravenObj - RavensObject to be populated with gathered knowledge
     * @param object - CoordinateObject which contains the information known.
     *******************************************************************************/
    private RavensObject populateObject(RavensObject ravenObj, IdentifiedObject object) {
        HashMap<String, String> attributes = new HashMap<>();
        //ArrayList<ROAttributeValuePair> attrs = new ArrayList<>();

        ravenObj.getAttributes().put("shape", object.getShape().toString());
        ravenObj.getAttributes().put("fill", object.isFilled ? "yes" : "no");
        //lacking angle changes!!
        //Set the size
        if(object.getHeight() >= 140)  {
            ravenObj.getAttributes().put("size", "large");
        }else if(object.getHeight() >= 90  && object.getHeight() < 140)  {
            ravenObj.getAttributes().put("size", "medium");
        }else if(object.getHeight() < 90)  {
            ravenObj.getAttributes().put("size", "small");
        }
        return ravenObj;
    }


}