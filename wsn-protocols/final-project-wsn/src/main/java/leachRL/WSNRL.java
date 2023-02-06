package leachRL;


public class WSNRL {
    // Field Dimensions in meters
    public double xm = 100;
    public double ym = 100;
    public int x = 0; //added for better display results of the plot
    public int y = 0; //added for better display results of the plot
    // Number of Nodes in the field
    public int n = 100;
    public int dead_nodes = 0;
    //Coordinates of the Sink (location is predetermined in this simulation)
    public double sinkx = 50;
    public double sinky = 50;
    // Energy Values
    // Initial Energy of a Node (in Joules)
    public double eo = 2; // units in Joules
    // Energy required to run circuity (both for transmitter and receiver)
    public double Eelec = 50 * Math.pow(10, (-9)); // units in Joules/bit
    public double ETx = 50 * Math.pow(10, (-9)); // units in Joules/bit

    public double ERx = 50 * Math.pow(10, (-9)); // units in Joules/bit
    //Transmit Amplifier Types
    public double Eamp = 100 * Math.pow(10, (-12)); // units in Joules/bit/m^2 (amount of energy spent by the amplifier to transmit the bits)
    // Data Aggregation Energy
    public double EDA = 5 * Math.pow(10, (-9)); // % units in Joules/bit
    //Size of data package %
    public int k = 4000; //units in bits
    // Suggested percentage of cluster head
    public double p = 0.05;// a 5 percent of the total amount of nodes used in the network is proposed to give good results
    // Number of Clusters
    public double No = p * n;
    public int operating_nodes = n;
    public int r = 40;

    @Override
    public String toString() {
        return "WSN{" +

                ", eo=" + eo +
                ", Eelec=" + Eelec +
                ", ETx=" + ETx +
                ", ERx=" + ERx +
                ", Eamp=" + Eamp +
                ", EDA=" + EDA +

                ", operating_nodes=" + operating_nodes +
                '}';
    }
}
