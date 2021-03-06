package org.aossie.starcross.source;

import android.util.Log;

import org.aossie.starcross.util.Geometry;
import org.aossie.starcross.util.MathUtil;
import org.aossie.starcross.util.MiscUtil;

public class OrbitalElements {
    private static String TAG = MiscUtil.getTag(OrbitalElements.class);
    private final static float EPSILON = 1.0e-6f;

    public final float distance;       // Mean distance (AU)
    public final float eccentricity;   // Eccentricity of orbit
    public final float inclination;    // Inclination of orbit (AngleUtils.RADIANS)
    public final float ascendingNode;  // Longitude of ascending node (AngleUtils.RADIANS)
    public final float perihelion;     // Longitude of perihelion (AngleUtils.RADIANS)
    public final float meanLongitude;  // Mean longitude (AngleUtils.RADIANS)

    public OrbitalElements(float d, float e, float i, float a, float p, float l) {
        this.distance = d;
        this.eccentricity = e;
        this.inclination = i;
        this.ascendingNode = a;
        this.perihelion = p;
        this.meanLongitude = l;
    }

    public float getAnomaly() {
        return calculateTrueAnomaly(meanLongitude - perihelion, eccentricity);
    }

    private static float calculateTrueAnomaly(float m, float e) {
        // initial approximation of eccentric anomaly
        float e0 = m + e * MathUtil.sin(m) * (1.0f + e * MathUtil.cos(m));
        float e1;

        // iterate to improve accuracy
        int counter = 0;
        do {
            e1 = e0;
            e0 = e1 - (e1 - e * MathUtil.sin(e1) - m) / (1.0f - e * MathUtil.cos(e1));
            if (counter++ > 100) {
                Log.d(TAG, "Failed to converge! Exiting.");
                Log.d(TAG, "e1 = " + e1 + ", e0 = " + e0);
                Log.d(TAG, "diff = " + MathUtil.abs(e0 - e1));
                break;
            }
        } while (MathUtil.abs(e0 - e1) > EPSILON);

        // convert eccentric anomaly to true anomaly
        float v =
                2f * MathUtil.atan(MathUtil.sqrt((1 + e) / (1 - e))
                        * MathUtil.tan(0.5f * e0));
        return Geometry.mod2pi(v);
    }
}
