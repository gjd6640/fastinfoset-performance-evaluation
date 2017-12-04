package utils;


    import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Map;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;

    public class GCInfoCollector {
        private static boolean listenerHasBeenInstalled = false;
        //GCInfoCollector.installCollector();
        
        public static void installCollector() {
            // http://www.programcreek.com/java-api-examples/index.php?class=javax.management.MBeanServerConnection&method=addNotificationListener
            // https://docs.oracle.com/javase/8/docs/jre/api/management/extension/com/sun/management/GarbageCollectionNotificationInfo.html#GARBAGE_COLLECTION_NOTIFICATION
            for (GarbageCollectorMXBean gcMbean : ManagementFactory.getGarbageCollectorMXBeans()) {
                try {
                    ManagementFactory.getPlatformMBeanServer().
                            addNotificationListener(gcMbean.getObjectName(), dataCollectingListener, null,null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            listenerHasBeenInstalled = true;
        }

        public static void resetCounters() {
          countOfMinorGCs = 0;
          countOfMajorGCs = 0;
          totalMillisecondsInMinorGCs = 0;
          totalMBReclaimedInMinorGCs = 0;
          totalMillisecondsInMajorGCs = 0;
          totalMBReclaimedInMajorGCs = 0;
        }

        public static void printCollectedStats() {
            if (listenerHasBeenInstalled == false) throw new IllegalStateException("You've never installed the collector. Try calling 'installCollector()' first. ");
            String indentPadding = new String(new char[30]).replace('\0', ' ');
            if (countOfMajorGCs == 0) {
                System.out.print("\n" +indentPadding+ "No major GCs. ");
            } else {
                System.out.println("\n" +indentPadding+ "Total seconds in " + countOfMajorGCs + " major GCs: " + Math.round(totalMillisecondsInMajorGCs / 10.0) / 100.0 + " (" + totalMillisecondsInMajorGCs + "ms)"
                        + "\n" +indentPadding+ "Total GB reclaimed in major GCs: " + Math.round(totalMBReclaimedInMajorGCs/1000.0*10)/10.0 + "\n");
                
            }
            if (countOfMinorGCs == 0) {
                System.out.println("No minor GCs.");
            } else {
                System.out.println(
                        "Total seconds in " + countOfMinorGCs + " minor GCs: " + Math.round(totalMillisecondsInMinorGCs / 10.0) / 100.0 + " (" + totalMillisecondsInMinorGCs + "ms)"
                        + "\n" +indentPadding+ "Total GB reclaimed in minor GCs: " + Math.round(totalMBReclaimedInMinorGCs/1000.0*10)/10.0);
            }

        }
        
        private static long countOfMinorGCs = 0;
        private static long countOfMajorGCs = 0;
        private static long totalMillisecondsInMinorGCs = 0;
        private static long totalMBReclaimedInMinorGCs = 0;
        private static long totalMillisecondsInMajorGCs = 0;
        private static long totalMBReclaimedInMajorGCs = 0;
        
        private static NotificationListener dataCollectingListener = new NotificationListener() {
            @Override
            public void handleNotification(Notification notification, Object handback) {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    // https://docs.oracle.com/javase/8/docs/jre/api/management/extension/com/sun/management/GarbageCollectionNotificationInfo.html
                    CompositeData cd = (CompositeData) notification.getUserData();
                    GarbageCollectionNotificationInfo gcNotificationInfo = GarbageCollectionNotificationInfo.from(cd);
                    GcInfo gcInfo = gcNotificationInfo.getGcInfo();

                    long memReclaimedInMB = sumOfMemoryInUseInMb(gcInfo.getMemoryUsageBeforeGc()) - sumOfMemoryInUseInMb(gcInfo.getMemoryUsageAfterGc());
                    
                    if (gcNotificationInfo.getGcAction().contains("minor GC")) {
                        totalMillisecondsInMinorGCs += gcInfo.getDuration();
                        totalMBReclaimedInMinorGCs += memReclaimedInMB;
                        countOfMinorGCs++;
                    } else if (gcNotificationInfo.getGcAction().contains("major GC")) {
                        totalMillisecondsInMajorGCs += gcInfo.getDuration();
                        totalMBReclaimedInMajorGCs += memReclaimedInMB;
                        countOfMajorGCs++;
                    }
                }
            }
        };
/*
        private static NotificationListener logWritingListener = new NotificationListener() {
            @Override
            public void handleNotification(Notification notification, Object handback) {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    // https://docs.oracle.com/javase/8/docs/jre/api/management/extension/com/sun/management/GarbageCollectionNotificationInfo.html
                    CompositeData cd = (CompositeData) notification.getUserData();
                    GarbageCollectionNotificationInfo gcNotificationInfo = GarbageCollectionNotificationInfo.from(cd);
                    GcInfo gcInfo = gcNotificationInfo.getGcInfo();
                    System.out.println("GarbageCollection: "+
                            gcNotificationInfo.getGcAction() + " " +
                            gcNotificationInfo.getGcName() +
                            " duration: " + gcInfo.getDuration() + "ms" +
                            " used: " + sumOfMemoryInUseInMb(gcInfo.getMemoryUsageBeforeGc()) + "MB" +
                            " -> " + sumOfMemoryInUseInMb(gcInfo.getMemoryUsageAfterGc()) + "MB");
                }
            }
        };
*/
        private static long sumOfMemoryInUseInMb(Map<String, MemoryUsage> memUsages) {
            long sum = 0;
            for (MemoryUsage memoryUsage : memUsages.values()) {
                sum += memoryUsage.getUsed();
            }
            return sum / (1024 * 1024);
        }
        
        public static long getTotalMillisecondsInMinorGCs() {
            return totalMillisecondsInMinorGCs;
        }

        public static long getTotalMBReclaimedInMinorGCs() {
            return totalMBReclaimedInMinorGCs;
        }

        public static long getTotalMillisecondsInMajorGCs() {
            return totalMillisecondsInMajorGCs;
        }

        public static long getTotalMBReclaimedInMajorGCs() {
            return totalMBReclaimedInMajorGCs;
        }

    }
