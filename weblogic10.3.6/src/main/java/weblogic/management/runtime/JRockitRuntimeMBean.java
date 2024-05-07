package weblogic.management.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import weblogic.management.utils.NotFoundException;

public interface JRockitRuntimeMBean extends JVMRuntimeMBean {
   long getTotalPhysicalMemory();

   long getFreePhysicalMemory();

   long getUsedPhysicalMemory();

   long getMaxHeapSize();

   long getTotalHeap();

   long getFreeHeap();

   long getUsedHeap();

   long getTotalNurserySize();

   String getGcAlgorithm();

   long getTotalGarbageCollectionCount();

   long getLastGCStart() throws NotFoundException;

   long getLastGCEnd() throws NotFoundException;

   long getTotalGarbageCollectionTime() throws NotFoundException;

   boolean isGCHandlesCompaction() throws NotFoundException;

   boolean isConcurrent() throws NotFoundException;

   boolean isGenerational() throws NotFoundException;

   boolean isIncremental() throws NotFoundException;

   boolean isParallel() throws NotFoundException;

   void setPauseTimeTarget(long var1) throws NotFoundException;

   long getPauseTimeTarget() throws NotFoundException;

   /** @deprecated */
   boolean isMethodTimingEnabled(Method var1) throws NotFoundException;

   /** @deprecated */
   long getMethodTiming(Method var1) throws NotFoundException;

   /** @deprecated */
   boolean isMethodInvocationCountEnabled(Method var1) throws NotFoundException;

   /** @deprecated */
   long getMethodInvocationCount(Method var1) throws NotFoundException;

   /** @deprecated */
   boolean isConstructorTimingEnabled(Constructor var1) throws NotFoundException;

   /** @deprecated */
   long getConstructorTiming(Constructor var1) throws NotFoundException;

   /** @deprecated */
   boolean isConstructorInvocationCountEnabled(Constructor var1) throws NotFoundException;

   /** @deprecated */
   long getConstructorInvocationCount(Constructor var1) throws NotFoundException;

   /** @deprecated */
   boolean isExceptionCountEnabled(Class var1) throws ClassCastException;

   /** @deprecated */
   long getExceptionCount(Class var1);

   int getTotalNumberOfThreads();

   int getNumberOfDaemonThreads();

   Collection getThreadSnapShots();

   String getJVMDescription();

   String getVendor();

   String getVersion();

   String getName();

   int getNumberOfProcessors();

   double getAllProcessorsAverageLoad();

   double getJvmProcessorLoad();

   List getCPUs() throws NotFoundException;

   Collection getHardwareComponents() throws NotFoundException;

   Collection getNICs() throws NotFoundException;
}
