package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensObject;

import java.util.ArrayList;
import java.util.List;

/**
 * for generating a composite key for describing the spatial relationship between a
 * RavensObject and the associated RavensObjects
 *
 * e.g. 'RavensObject A' "above" RavensObject B
 */
public class ROSpatialDescCompositeKey implements Comparable<ROSpatialDescCompositeKey> {
    private RavensObject ravensObject;
    private String spatialDesc;
    private String ravensObjectName;

    public ROSpatialDescCompositeKey() {}

    public ROSpatialDescCompositeKey(RavensObject ravensObject, String spatialDesc) {
        this.ravensObject = ravensObject;
        this.spatialDesc = spatialDesc;
    }

    public ROSpatialDescCompositeKey(String ravensObjectName, String spatialDesc) {
        this.ravensObjectName = ravensObjectName;
        this.spatialDesc = spatialDesc;
    }


    @Override
    public int compareTo(ROSpatialDescCompositeKey that) {
        return this.ravensObject.getName().compareTo(that.ravensObject.getName());
    }

    /**
     * Compares the spatialDesc for equality regardless of RavensObject's name
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ROSpatialDescCompositeKey)) {
            return false;
        }

        ROSpatialDescCompositeKey that = (ROSpatialDescCompositeKey) o;
        return that.spatialDesc.equalsIgnoreCase(this.spatialDesc);

        //&& that.ravensObject.getName().equals(this.ravensObject.getName());
    }

    @Override
    public int hashCode() {
        return (this.ravensObject.getName() + this.spatialDesc).hashCode();
    }

    public RavensObject getRavensObject() {
        return ravensObject;
    }

    public String getSpatialDesc() {
        return spatialDesc;
    }


    public static void main(String[] args) {
        ROSpatialDescCompositeKey key1 = new ROSpatialDescCompositeKey("object1", "above");
        ROSpatialDescCompositeKey key2 = new ROSpatialDescCompositeKey("object2", "above");
        ROSpatialDescCompositeKey key3 = new ROSpatialDescCompositeKey("object3", "below");

        List<ROSpatialDescCompositeKey> keys = new ArrayList<>();
        keys.add(key1);
        keys.add(key2);
        keys.add(key3);

        if (keys.contains(key3)) {
            System.out.println("keys contains key3");
        }

        System.out.println(key1.equals(key2));
    }
}