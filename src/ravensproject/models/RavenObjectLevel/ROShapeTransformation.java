package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensObject;

/**
 * Created by guoliangwang on 6/27/15.
 *
 * Model for Shape Transformation at the Raven's Object level
 *
 * @param fromShape String (e.g. square)
 * @param toShape String (e.g. circle)
 *
 */

public class ROShapeTransformation {
    private String fromShape;
    private String toShape;
    private RavensObject ravensObject1;
    private RavensObject ravensObject2;

    public ROShapeTransformation() {}

    public ROShapeTransformation(RavensObject ravensObject1, RavensObject ravensObject2) {
        this.ravensObject1 = ravensObject1;
        this.ravensObject2 = ravensObject2;
        if (this.ravensObject1.getAttributes().containsKey("shape") &&
                this.ravensObject2.getAttributes().containsKey("shape")) {
            this.fromShape = this.ravensObject1.getAttributes().get("shape");
            this.toShape = this.ravensObject2.getAttributes().get("shape");
        }
    }

    public ROShapeTransformation(String fromShape, String toShape) {
        this.fromShape = fromShape;
        this.toShape = toShape;
    }

    @Override
    public boolean equals(Object o) {
        //null shapes should return false - not equal
        if (this.fromShape == null || this.toShape == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof ROShapeTransformation)) {
            return false;
        }

        //Cast Object o to ShapeTransformation
        ROShapeTransformation that = (ROShapeTransformation) o;

        return this.fromShape.equalsIgnoreCase(that.fromShape) &&
                this.toShape.equalsIgnoreCase(that.toShape);
    }


    //getters below

    public String getFromShape() {
        return fromShape;
    }

    public String getToShape() {
        return toShape;
    }

    public RavensObject getRavensObject1() {
        return ravensObject1;
    }

    public RavensObject getRavensObject2() {
        return ravensObject2;
    }

    //setters below
    public void setFromShape(String fromShape) {
        this.fromShape = fromShape;
    }

    public void setToShape(String toShape) {
        this.toShape = toShape;
    }

    public void setRavensObject1(RavensObject ravensObject1) {
        this.ravensObject1 = ravensObject1;
    }

    public void setRavensObject2(RavensObject ravensObject2) {
        this.ravensObject2 = ravensObject2;
    }
}