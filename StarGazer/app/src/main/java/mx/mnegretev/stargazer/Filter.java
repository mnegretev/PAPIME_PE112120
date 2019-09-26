package mx.mnegretev.stargazer;

public class Filter {
    private final double[] filterInput = new double[5];
    private final double[] filterOutput = new double[5];
    private final double[] movingAverage = new double[10];
    private double average = 0;
    private double lastValue = 0;

    public Filter()
    {
        for(int i=0; i < 5; i++) {
            filterInput[i] = 0;
            filterOutput[i] = 0;
        }
        for(int i=0; i < movingAverage.length; i++)
            movingAverage[i] =0;

    }

    public double Filter(double inputSignal)
    {
        /*
        if(Math.abs(inputSignal - lastValue) > 0.002)
        {
            lastValue = inputSignal;
            return filterOutput[4];
        }
        lastValue = inputSignal;
        */
        filterInput[0] = filterInput[1];
        filterInput[1] = filterInput[2];
        filterInput[2] = filterInput[3];
        filterInput[3] = filterInput[4];
        filterInput[4] = inputSignal;
        filterOutput[0] = filterOutput[1];
        filterOutput[1] = filterOutput[2];
        filterOutput[2] = filterOutput[3];
        filterOutput[3] = filterOutput[4];
    /*
        //Constants for Wn = 0.01
        filterOutput[4] = 0.058451424277128*filterInput[4] + 0.233805697108513*filterInput[3] +
                0.350708545662770*filterInput[2] + 0.233805697108513*filterInput[1] +
                0.058451424277128*filterInput[0];
        filterOutput[4] *= 0.000001;
        filterOutput[4] -= -3.917907865391990*filterOutput[3] + 5.757076379118073*filterOutput[2] -
                3.760349507694534*filterOutput[1] + 0.921181929191239*filterOutput[0];
        */
        //Constants for wn = 0.06
        filterOutput[4] = 0.623869835484794*filterInput[4] + 2.49547934193918*filterInput[3] +
                3.74321901290876*filterInput[2] + 2.49547934193918*filterInput[1] +
                0.623869835484794*filterInput[0];
        filterOutput[4] *= 0.0001;
        filterOutput[4] -= -3.507786207390782*filterOutput[3] + 4.640902412686707*filterOutput[2] -
                2.742652821120372*filterOutput[1] + 0.610534807561224*filterOutput[0];

        return filterOutput[4];
    }

    public double MovingAverage(double inputSignal)
    {
        /*
        if(Math.abs(inputSignal - lastValue) > 0.002)
        {
            lastValue = inputSignal;
            return average;
        }
        lastValue = inputSignal;
*/
        average = 0;
        for(int i=0; i < movingAverage.length-1; i++) {
            movingAverage[i] = movingAverage[i + 1];
            average += movingAverage[i+1];
        }
        movingAverage[movingAverage.length-1] = inputSignal;
        average += inputSignal;
        average /= movingAverage.length;
        return average;
    }
}
