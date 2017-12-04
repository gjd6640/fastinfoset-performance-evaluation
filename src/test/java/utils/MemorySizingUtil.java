package utils;

public class MemorySizingUtil {
  private static Long lastJVMTotalMemorySizeInMB = null;
  final static int mb = 1024*1024;
  
  private static final int IGNORE_THIS_MANY_MB_OF_GROWTH = 50; // We're only interested in substantial heap resizes I think

  public static void checkTotalJVMMemorySizeForGrowth() {
    Runtime runtime = Runtime.getRuntime();
    long currentJVMTotalMemorySizeInMB = runtime.totalMemory()/mb;
    if (lastJVMTotalMemorySizeInMB == null) {
        throw new RuntimeException("You have to call MemorySizingUtil.init() at least once before you call this method.");
    } else if (currentJVMTotalMemorySizeInMB > lastJVMTotalMemorySizeInMB + IGNORE_THIS_MANY_MB_OF_GROWTH) {
      System.out.println("!! Notice: Total memory usage by the jvm has increased significantly! Consider increasing your -Xms JVM parameter to prevent heap resizing activity from influencing these measurements. [" + lastJVMTotalMemorySizeInMB + "mb --> " + currentJVMTotalMemorySizeInMB + "mb]");
    }
    lastJVMTotalMemorySizeInMB = currentJVMTotalMemorySizeInMB;
  }

  public static void init() {
      lastJVMTotalMemorySizeInMB = Runtime.getRuntime().totalMemory()/mb;
  }
  
  public static void printMemoryInfo() {
    Runtime runtime = Runtime.getRuntime();

    System.out.println("##### Heap utilization statistics [MB] #####");

    //Print used memory
    System.out.println("Used Memory:"
           + (runtime.totalMemory() - runtime.freeMemory())/mb + "mb");

    //Print free memory
    System.out.println("Free Memory:"
        + runtime.freeMemory()/mb + "mb");

    //Print total available memory
    System.out.println("Total Memory:" + runtime.totalMemory()/mb + "mb");

    //Print Maximum available memory
    System.out.println("Max Memory:" + runtime.maxMemory()/mb + "mb");
  }

}