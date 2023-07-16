package fr.dams4k.cpsdisplay.gui.buttons;

public class ModSliderMainPoint {
    private float value;
    private float range;

    public ModSliderMainPoint(float value, float range) {
        this.value = value;
        this.range = range;
    }

    public void setValue(float value) {
        this.value = value;
    }
    public float getValue() {
        return value;
    }

    public void setRange(float range) {
        this.range = range;
    }
    public float getRange() {
        return range;
    }

    public boolean isNear(float v) {
        return v < this.value + this.range && v > this.value - this.range;
    }
}
