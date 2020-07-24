package com.littlelollipop.lbox.valve;

/**
 * Created by sai on 17/11/29.
 */

public class ManifoldValve {

    Outfall   outfall;
    int       valveSize;
    boolean[] openedValve;
    Object[]  data   = new Object[10];
    boolean   opened = false;

    public ManifoldValve(Outfall outfall, int valveSize) {
        this.outfall = outfall;
        this.valveSize = valveSize;
        openedValve = new boolean[valveSize];
        for (int i = 0; i < valveSize; i++) {
            openedValve[i] = false;
        }
    }

    public void setData(Object dataIn, int dataNumber) {
        data[dataNumber] = dataIn;
    }

    public void setData(long dataIn, int dataNumber) {
        data[dataNumber] = Long.valueOf(dataIn);
    }

    public void setData(int dataIn, int dataNumber) {
        data[dataNumber] = Integer.valueOf(dataIn);
    }

    public void setData(boolean dataIn, int dataNumber) {
        data[dataNumber] = Boolean.valueOf(dataIn);
    }

    public void setData(String dataIn, int dataNumber) {
        data[dataNumber] = dataIn;
    }

    public Object getData_object(int dataNumber) {
        return data[dataNumber];
    }

    public long getData_long(int dataNumber) {
        return ((Long) data[dataNumber]).longValue();
    }

    public long getData_int(int dataNumber) {
        return ((Integer) data[dataNumber]).intValue();
    }

    public boolean getData_boolean(int dataNumber) {
        return ((Boolean) data[dataNumber]).booleanValue();
    }

    public String getData_String(int dataNumber) {
        return (String) data[dataNumber];
    }

    public void openValve(int valveNumber) {

        openedValve[valveNumber] = true;

        for (int i = 0; i < valveSize; i++) {
            if (!openedValve[i])
                return;
        }

        opened = true;
        outfall.discharge(this);
    }

    public void openValveOnce(int i) {

        if (opened) {
            return;
        }
        openValve(i);
    }

    public interface Outfall {
        void discharge(ManifoldValve mManifoldValve);
    }

}
