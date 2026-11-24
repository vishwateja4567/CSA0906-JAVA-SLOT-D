
class ScannerStation extends Thread {

    int station;
    int scripts;

    ScannerStation(int station, int scripts) {
        this.station = station;
        this.scripts = scripts;
    }

    public void run() {
        try {
            System.out.println("Station " + station + " started");

            Thread.sleep(500);

            System.out.println("Station " + station +
                               " scanned " + scripts + " scripts");

        } catch (InterruptedException e) {
            System.out.println("Station interrupted");
        }
    }
}

class RunnableStation implements Runnable {

    int station;
    int scripts;

    RunnableStation(int station, int scripts) {
        this.station = station;
        this.scripts = scripts;
    }

    public void run() {
        try {
            System.out.println("Runnable station " +
                               station + " started");

            Thread.sleep(500);

            System.out.println("Runnable station " +
                               station + " scanned " +
                               scripts + " scripts");

        } catch (InterruptedException e) {
            System.out.println("Station interrupted");
        }
    }
}

public class ScannerStationDemo {

    public static void main(String[] args)
            throws InterruptedException {

        ScannerStation[] stations = new ScannerStation[3];

        int total = 0;

        for (int i = 0; i < 3; i++) {
            stations[i] = new ScannerStation(i + 1, 5);
            stations[i].start();
        }

        RunnableStation task = new RunnableStation(4, 5);
        Thread fourth = new Thread(task);
        fourth.start();

        for (ScannerStation s : stations) {
            s.join();
            total += 5;
        }

        fourth.join();
        total += 5;

        System.out.println("Station 1 alive: " +
                           stations[0].isAlive());

        System.out.println("Total scripts scanned: " + total);
    }
}
