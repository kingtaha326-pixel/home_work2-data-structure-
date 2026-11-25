import java.util.Arrays;
import java.util.Scanner;

public class SortingManualBenchmark {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int totalSize = 100_000;
        int batchSize = 7;

        int[] originalArray = new int[totalSize];

        System.out.println("--- نظام إدخال البيانات اليدوي ---");
        System.out.println("أدخل أول 7 عناصر:");

        // إدخال أول 7 عناصر يدوياً
        for (int i = 0; i < batchSize; i++) {
            System.out.print("أدخل العنصر رقم " + (i + 1) + ": ");
            originalArray[i] = scanner.nextInt();
        }

        // توليد بقية العناصر تلقائياً
        for (int i = batchSize; i < totalSize; i++) {
            originalArray[i] = (int)(Math.random() * 1000);
        }

        System.out.println("\nجاري تنفيذ الخوارزميات الست...");

        System.out.println("\n--------------------------------------------------------------------------");

        System.out.printf("%-15s | %-15s | %-15s | %-15s | %-15s%n",
                "Algorithm",
                "Random",
                "Reverse",
                "Partial",
                "Sorted");

        System.out.println("--------------------------------------------------------------------------");

        runBatchBenchmark("Selection Sort", originalArray, batchSize, 1);
        runBatchBenchmark("Insertion Sort", originalArray, batchSize, 2);
        runBatchBenchmark("Bubble Sort", originalArray, batchSize, 3);
        runBatchBenchmark("Merge Sort", originalArray, batchSize, 4);
        runBatchBenchmark("Quick Sort", originalArray, batchSize, 5);
        runBatchBenchmark("Radix Sort", originalArray, batchSize, 6);

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("تم الانتهاء بنجاح.");

        scanner.close();
    }

    // قياس زمن الخوارزميات
    public static void runBatchBenchmark(
            String name, int[] original,
            int batchSize, int algoType) {

        long[] totalTimes = new long[4];

        for (int state = 1; state <= 4; state++) {

            int[] testArray = prepareTestCase(original, state);

            long totalStateTimeNano = 0;

            for (int i = 0; i < testArray.length; i += batchSize) {

                int currentBatchSize =
                        Math.min(batchSize, testArray.length - i);

                int[] batch = new int[currentBatchSize];

                System.arraycopy(
                        testArray, i, batch, 0, currentBatchSize);

                long startTime = System.nanoTime();

                executeSort(batch, algoType);

                long endTime = System.nanoTime();

                totalStateTimeNano += (endTime - startTime);
            }

            totalTimes[state - 1] =
                    totalStateTimeNano / 1000;
        }

        System.out.printf(
                "%-15s | %-15d | %-15d | %-15d | %-15d%n",
                name,
                totalTimes[0],
                totalTimes[1],
                totalTimes[2],
                totalTimes[3]);
    }

    // تجهيز الحالات الأربع
    public static int[] prepareTestCase(int[] original, int type) {

        int[] arr = Arrays.copyOf(original, original.length);

        if (type == 1) {
            return arr;
        }

        else if (type == 2) {
            Arrays.sort(arr);
            reverse(arr);
        }

        else if (type == 3) {

            Arrays.sort(arr);

            int swaps = (int)(arr.length * 0.20);

            for (int i = 0; i < swaps; i++) {

                int idx1 = (int)(Math.random() * arr.length);
                int idx2 = (int)(Math.random() * arr.length);

                int temp = arr[idx1];
                arr[idx1] = arr[idx2];
                arr[idx2] = temp;
            }
        }

        else if (type == 4) {
            Arrays.sort(arr);
        }

        return arr;
    }

    // عكس المصفوفة
    public static void reverse(int[] arr) {

        int left = 0;
        int right = arr.length - 1;

        while (left < right) {

            int temp = arr[left];

            arr[left] = arr[right];
            arr[right] = temp;

            left++;
            right--;
        }
    }

    // اختيار الخوارزمية
    public static void executeSort(int[] arr, int algoType) {

        switch (algoType) {

            case 1:
                selectionSort(arr);
                break;

            case 2:
                insertionSort(arr);
                break;

            case 3:
                bubbleSort(arr);
                break;

            case 4:
                mergeSort(arr, 0, arr.length - 1);
                break;

            case 5:
                quickSort(arr, 0, arr.length - 1);
                break;

            case 6:
                radixSort(arr);
                break;
        }
    }

    // 1. Selection Sort
    public static void selectionSort(int[] arr) {

        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {

            int minIdx = i;

            for (int j = i + 1; j < n; j++) {

                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }

            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    // 2. Insertion Sort
    public static void insertionSort(int[] arr) {

        int n = arr.length;

        for (int i = 1; i < n; i++) {

            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {

                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }
    }

    // 3. Bubble Sort
    public static void bubbleSort(int[] arr) {

        int n = arr.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {

            swapped = false;

            for (int j = 0; j < n - i - 1; j++) {

                if (arr[j] > arr[j + 1]) {

                    int temp = arr[j];

                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;

                    swapped = true;
                }
            }

            if (!swapped) break;
        }
    }

    // 4. Merge Sort
    public static void mergeSort(int[] arr, int l, int r) {

        if (l < r) {

            int m = l + (r - l) / 2;

            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);

            merge(arr, l, m, r);
        }
    }

    public static void merge(int[] arr, int l, int m, int r) {

        int n1 = m - l + 1;
        int n2 = r - m;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++)
            L[i] = arr[l + i];

        for (int j = 0; j < n2; j++)
            R[j] = arr[m + 1 + j];

        int i = 0;
        int j = 0;
        int k = l;

        while (i < n1 && j < n2) {

            if (L[i] <= R[j])
                arr[k++] = L[i++];

            else
                arr[k++] = R[j++];
        }

        while (i < n1)
            arr[k++] = L[i++];

        while (j < n2)
            arr[k++] = R[j++];
    }

    // 5. Quick Sort
    public static void quickSort(int[] arr, int low, int high) {

        if (low < high) {

            int pi = partition(arr, low, high);

            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    public static int partition(int[] arr, int low, int high) {

        int pivot = arr[high];

        int i = low - 1;

        for (int j = low; j < high; j++) {

            if (arr[j] <= pivot) {

                i++;

                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];

        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    // 6. Radix Sort
    // يدعم الأعداد الموجبة والسالبة
    public static void radixSort(int[] arr) {

        int n = arr.length;

        if (n <= 1) return;

        int[] output = new int[n];

        // معالجة 4 بايتات لكل عدد
        for (int shift = 0; shift < 32; shift += 8) {

            int[] count = new int[256];

            // حساب تكرار القيم
            for (int i = 0; i < n; i++) {

                int key = arr[i] ^ Integer.MIN_VALUE;

                int digit = (key >>> shift) & 255;

                count[digit]++;
            }

            // حساب المجموع التراكمي
            for (int i = 1; i < 256; i++) {

                count[i] += count[i - 1];
            }

            // ترتيب العناصر
            for (int i = n - 1; i >= 0; i--) {

                int key = arr[i] ^ Integer.MIN_VALUE;

                int digit = (key >>> shift) & 255;

                output[--count[digit]] = arr[i];
            }

            // نسخ النتيجة
            System.arraycopy(output, 0, arr, 0, n);
        }
    }
}
