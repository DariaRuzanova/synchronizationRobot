import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static final int threadNumbers = 1000;

    public static void main(String[] args) {

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadNumbers; i++) {
            Thread thread = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int percent = freqR(route);
                synchronized (sizeToFreq) {
                    int count = sizeToFreq.getOrDefault(percent, 1);
                    count++;
                    sizeToFreq.put(percent, count);
                }
            });
            threads.add(thread);
            thread.start();
        }
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Optional<Map.Entry<Integer, Integer>> maxNumberOfReps = sizeToFreq.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", maxNumberOfReps.get().getKey(),
                maxNumberOfReps.get().getValue());
        System.out.println("Другие размеры:");
        List<Map.Entry<Integer, Integer>> ordered = sizeToFreq.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .toList();
        for (Map.Entry<Integer, Integer> entry2 : ordered) {
            if (!Objects.equals(entry2.getKey(), maxNumberOfReps.get().getKey())) {
                System.out.printf("- %d (%d раз)\n", entry2.getKey(), entry2.getValue());
            }
        }


    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int freqR(String route) {
        return (int) route.chars().filter(x -> x == 'R').count();
    }
}
