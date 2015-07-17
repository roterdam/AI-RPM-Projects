package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Created by guoliangwang on 6/27/15.
 *
 * @author guoliangwang
 *
 * Model for Raven's Object Transformation
 *
 * Given two Raven's Object, find out their transformations
 *
 * Comparing order: from left to right (e.g. Figure A compared to Figure B)
 * or from top to bottom (e.g. Figure A compared to Figure C if 2x2 or
 * Figure A compared to Figure D if 3x3
 *
 * ??? on a second thought, I am not sure what I should do to better use this model ???!!
 * ??? or should I make it a Utility or Factory tool ???!!!
 *
 */
public class ROTransformation implements Comparator<ROTransformation> {

    private RavensObject ravensObject1;
    private RavensObject ravensObject2;

    private ROShapeTransformation roShapeTransformation;
    private ROSizeChange roSizeChange;
    private ROAngleChange roAngleChange;
    private ROFillChange roFillChange;
    private ROAlignment roAlignment;


    public ROTransformation() {}
    public ROTransformation(RavensObject ravensObject1, RavensObject ravensObject2) {
        this.ravensObject1 = ravensObject1;
        this.ravensObject2 = ravensObject2;
        this.roShapeTransformation = new ROShapeTransformation(ravensObject1, ravensObject2);
        this.roSizeChange = new ROSizeChange(ravensObject1, ravensObject2);
        this.roAngleChange = new ROAngleChange(ravensObject1, ravensObject2);
        this.roFillChange = new ROFillChange(ravensObject1, ravensObject2);
        this.roAlignment = new ROAlignment(ravensObject1, ravensObject2);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ROTransformation)) {
            return false;
        }

        ROTransformation that = (ROTransformation) o;
        return this.roAngleChange.equals(that.roAngleChange) &&
                this.roShapeTransformation.equals(that.roShapeTransformation) &&
                this.roSizeChange.equals(that.roSizeChange) &&
                this.roFillChange.equals(that.roSizeChange) &&
                this.roAlignment.equals(that.roAlignment);
    }

    /**
     * get all transformations (what is changed) between the two RavensObjects
     * @return List<ROTransformationInterface>
     */
    public List<ROTransformationInterface> getChangedTransformations() {
        List<ROTransformationInterface> whatsChanged = new ArrayList<>();

        if (this.roAngleChange.getAngleDiff() != 0) {
            whatsChanged.add(this.roAngleChange);
        }
        if (this.roShapeTransformation.isShapeChanged()) {
            whatsChanged.add(this.roShapeTransformation);
        }
        if (this.roSizeChange.sizeDifference() != 0) {
            whatsChanged.add(this.roSizeChange);
        }
        if (this.roFillChange.isFillChanged()) {
            whatsChanged.add(this.roFillChange);
        }
        if (this.roAlignment.isDifferent()) {
            whatsChanged.add(this.roAngleChange);
        }

        return whatsChanged;
    }


    /**
     * List of unchanged transformations between the two objects
     * @return
     */
    public List<ROTransformationInterface> getUnchangedTransformations() {
        List<ROTransformationInterface> unchanged = new ArrayList<>();
        if (this.roAngleChange.getAngleDiff() == 0) {
            unchanged.add(this.roAngleChange);
        }
        if (!this.roShapeTransformation.isShapeChanged()) {
            unchanged.add(this.roShapeTransformation);
        }
        if (this.roSizeChange.sizeDifference() == 0) {
            unchanged.add(this.roSizeChange);
        }
        if (this.roFillChange.isFillChanged()) {
            unchanged.add(this.roFillChange);
        }
        if (!this.roAlignment.isDifferent()) {
            unchanged.add(this.roAlignment);
        }

        return unchanged;
    }


    @Override
    public int compare(ROTransformation o1, ROTransformation o2) {

        if (o1.roSizeChange.sizeDifference() < o2.roSizeChange.sizeDifference()) {
            return -1;
        }
        if (o1.roSizeChange.sizeDifference() > o2.roSizeChange.sizeDifference()) {
            return 1;
        }
        if (o1.roAngleChange.getAngleDiff() < o2.roAngleChange.getAngleDiff()) {
            return -1;
        }
        if (o1.roAngleChange.getAngleDiff() > o2.roAngleChange.getAngleDiff()) {
            return 1;
        }
        if (o1.roAlignment.isDifferent() == false && o2.roAlignment.isDifferent() == true) {
            return 1;
        }
        if (o1.roAlignment.isDifferent() == true && o2.roAlignment.isDifferent() == false) {
            return -1;
        }

        return 0;
    }

    @Override
    public int hashCode() {
        return (this.ravensObject1.getName() +
                this.ravensObject2.getName()).hashCode();
    }

    public List<ROTransformationInterface> compareAnotherROTransformations(ROTransformation anotherROTransformation) {
        List<ROTransformationInterface> differences = new ArrayList<>();
        if (!this.equals(anotherROTransformation)) {
            if (!this.roAngleChange.equals(anotherROTransformation.roAngleChange)) {
                differences.add(this.roAngleChange);
            }
        }

        return differences;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.roAngleChange.getAttributeKeyName() + " : " + this.roAngleChange.getAngleDiff());
        sb.append(this.roSizeChange.getAttributeKeyName() + " : " + this.roSizeChange.sizeDifference());
        sb.append(this.roShapeTransformation.getAttributeKeyName() + " : " + this.roShapeTransformation.toString());
        sb.append(this.roAngleChange.getAttributeKeyName() + " : " + this.roAngleChange.getAngleDiff());
        sb.append(this.roAlignment.getAttributeKeyName() + " : " + this.roAlignment.isDifferent());
        return sb.toString();
    }


    //getters
    public RavensObject getRavensObject1() {
        return ravensObject1;
    }

    public RavensObject getRavensObject2() {
        return ravensObject2;
    }

    public ROShapeTransformation getRoShapeTransformation() {
        return roShapeTransformation;
    }

    public ROSizeChange getRoSizeChange() {
        return roSizeChange;
    }

    public ROAngleChange getRoAngleChange() {
        return roAngleChange;
    }

    public ROFillChange getRoFillChange() {
        return roFillChange;
    }

    public ROAlignment getRoAlignment() {return roAlignment;}
}
