import sim.TradingSim;

public class Main {


    public static void main(String[] args) {
        try {
            TradingSim.runSim();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
