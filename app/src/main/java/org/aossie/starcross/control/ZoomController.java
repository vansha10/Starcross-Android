package org.aossie.starcross.control;

public class ZoomController extends AbstractController {
    private static final float MAX_ZOOM_OUT = 90.0f;
    private static final float MAX_ZOOM_IN = 10.0f;

    private void setFieldOfView(float zoomDegrees) {
        if (!enabled) {
            return;
        }
        model.setFieldOfView(zoomDegrees);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    void zoomBy(float ratio) {
        float zoomDegrees = model.getFieldOfView();
        zoomDegrees = Math.min(zoomDegrees * ratio, MAX_ZOOM_OUT);
        zoomDegrees = Math.max(zoomDegrees, MAX_ZOOM_IN);
        setFieldOfView(zoomDegrees);
    }
}