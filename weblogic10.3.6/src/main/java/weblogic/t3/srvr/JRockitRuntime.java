package weblogic.t3.srvr;

import com.bea.jvm.CPU;
import com.bea.jvm.JVM;
import com.bea.jvm.JVMFactory;
import com.bea.jvm.MemorySystem;
import com.bea.jvm.NotAvailableException;
import com.bea.jvm.ProfilingSystem;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JRockitRuntimeMBean;
import weblogic.management.utils.NotFoundException;

public class JRockitRuntime extends JVMRuntime implements JRockitRuntimeMBean {
   private JVM theJVM = null;

   public JRockitRuntime() throws ManagementException {
      this.theJVM = JVMFactory.getJVM();
   }

   public double getAllProcessorsAverageLoad() {
      List var1 = this.theJVM.getMachine().getCPUs();
      if (var1.size() < 1) {
         return 0.0;
      } else {
         Iterator var2 = var1.iterator();

         double var3;
         CPU var5;
         for(var3 = 0.0; var2.hasNext(); var3 += var5.getLoad()) {
            var5 = (CPU)var2.next();
         }

         return var3 / (double)var1.size();
      }
   }

   public int getNumberOfProcessors() {
      return this.theJVM.getMachine().getCPUs().size();
   }

   public double getJvmProcessorLoad() {
      return this.theJVM.getJVMLoad();
   }

   public String getJVMDescription() {
      return this.theJVM.getDescription();
   }

   public String getVendor() {
      return this.theJVM.getVendor();
   }

   public String getVersion() {
      return this.theJVM.getVersion();
   }

   public int getNumberOfDaemonThreads() {
      return this.theJVM.getThreadSystem().getDaemonThreadCount();
   }

   public int getTotalNumberOfThreads() {
      return this.theJVM.getThreadSystem().getTotalThreadCount();
   }

   public Collection getThreadSnapShots() {
      return this.theJVM.getThreadSystem().getThreadSnapshots();
   }

   public String getThreadStackDump() {
      return this.theJVM.getThreadSystem().getThreadStackDump();
   }

   public long getUptime() {
      return System.currentTimeMillis() - this.theJVM.getStartTime();
   }

   public long getFreeHeap() {
      MemorySystem var1 = this.theJVM.getMemorySystem();
      return var1.getTotalHeapSize() - var1.getUsedHeapSize();
   }

   public long getFreePhysicalMemory() {
      return this.theJVM.getMachine().getPhysicalMemory().getTotalMemory() - this.theJVM.getMachine().getPhysicalMemory().getUsedMemory();
   }

   public long getTotalHeap() {
      return this.theJVM.getMemorySystem().getTotalHeapSize();
   }

   public long getTotalPhysicalMemory() {
      return this.theJVM.getMachine().getPhysicalMemory().getTotalMemory();
   }

   public long getTotalNurserySize() {
      try {
         return this.theJVM.getMemorySystem().getGarbageCollector().getNurserySize();
      } catch (NotAvailableException var2) {
         return 0L;
      }
   }

   public long getUsedHeap() {
      return this.theJVM.getMemorySystem().getUsedHeapSize();
   }

   public long getUsedPhysicalMemory() {
      return this.theJVM.getMachine().getPhysicalMemory().getUsedMemory();
   }

   public long getMaxHeapSize() {
      return this.theJVM.getMemorySystem().getMaxHeapSize();
   }

   public List getCPUs() throws NotFoundException {
      try {
         return this.theJVM.getMachine().getCPUs();
      } catch (NotAvailableException var2) {
         throw new NotFoundException(var2.getMessage());
      }
   }

   public Collection getHardwareComponents() throws NotFoundException {
      try {
         return this.theJVM.getMachine().getHardwareComponents();
      } catch (NotAvailableException var2) {
         throw new NotFoundException(var2.getMessage());
      }
   }

   public Collection getNICs() throws NotFoundException {
      try {
         return this.theJVM.getMachine().getNICs();
      } catch (NotAvailableException var2) {
         throw new NotFoundException(var2.getMessage());
      }
   }

   public boolean isExceptionCountEnabled(Class var1) throws ClassCastException {
      return this.theJVM.getProfilingSystem().isExceptionCountEnabled(var1);
   }

   public String getGcAlgorithm() {
      return this.theJVM.getMemorySystem().getGarbageCollector().getDescription();
   }

   public long getTotalGarbageCollectionCount() {
      return this.theJVM.getMemorySystem().getGarbageCollector().getTotalGarbageCollectionCount();
   }

   public long getLastGCEnd() throws NotFoundException {
      try {
         return this.theJVM.getMemorySystem().getGarbageCollector().getLastGCEnd();
      } catch (NotAvailableException var2) {
         throw new NotFoundException(var2.getMessage());
      }
   }

   public long getLastGCStart() throws NotFoundException {
      try {
         return this.theJVM.getMemorySystem().getGarbageCollector().getLastGCStart();
      } catch (NotAvailableException var2) {
         throw new NotFoundException(var2.getMessage());
      }
   }

   public long getTotalGarbageCollectionTime() throws NotFoundException {
      try {
         return this.theJVM.getMemorySystem().getGarbageCollector().getTotalGarbageCollectionTime();
      } catch (NotAvailableException var2) {
         throw new NotFoundException(var2.getMessage());
      }
   }

   public boolean isGCHandlesCompaction() throws NotFoundException {
      try {
         return this.theJVM.getMemorySystem().getGarbageCollector().hasCompaction();
      } catch (NotAvailableException var2) {
         throw new NotFoundException(var2.getMessage());
      }
   }

   public boolean isConcurrent() throws NotFoundException {
      try {
         return this.theJVM.getMemorySystem().getGarbageCollector().isConcurrent();
      } catch (NotAvailableException var2) {
         throw new NotFoundException(var2.getMessage());
      }
   }

   public boolean isGenerational() throws NotFoundException {
      try {
         return this.theJVM.getMemorySystem().getGarbageCollector().isGenerational();
      } catch (NotAvailableException var2) {
         throw new NotFoundException(var2.getMessage());
      }
   }

   public boolean isIncremental() throws NotFoundException {
      try {
         return this.theJVM.getMemorySystem().getGarbageCollector().isIncremental();
      } catch (NotAvailableException var2) {
         throw new NotFoundException(var2.getMessage());
      }
   }

   public boolean isParallel() throws NotFoundException {
      try {
         return this.theJVM.getMemorySystem().getGarbageCollector().isParallel();
      } catch (NotAvailableException var2) {
         throw new NotFoundException(var2.getMessage());
      }
   }

   public void setPauseTimeTarget(long var1) throws NotFoundException {
      try {
         this.theJVM.getMemorySystem().getGarbageCollector().setPauseTimeTarget(var1);
      } catch (NotAvailableException var4) {
         throw new NotFoundException(var4.getMessage());
      }
   }

   public long getPauseTimeTarget() throws NotFoundException {
      try {
         return this.theJVM.getMemorySystem().getGarbageCollector().getPauseTimeTarget();
      } catch (NotAvailableException var2) {
         throw new NotFoundException(var2.getMessage());
      }
   }

   public long getMethodTiming(Method var1) throws NotFoundException {
      try {
         return this.theJVM.getProfilingSystem().getTiming(var1);
      } catch (Throwable var3) {
         throw new NotFoundException(var3.getMessage());
      }
   }

   public long getConstructorTiming(Constructor var1) {
      return this.theJVM.getProfilingSystem().getTiming(var1);
   }

   public boolean isMethodInvocationCountEnabled(Method var1) throws NotFoundException {
      try {
         return this.theJVM.getProfilingSystem().isInvocationCountEnabled(var1);
      } catch (Throwable var3) {
         throw new NotFoundException(var3.getMessage());
      }
   }

   public boolean isConstructorInvocationCountEnabled(Constructor var1) {
      return this.theJVM.getProfilingSystem().isInvocationCountEnabled(var1);
   }

   public boolean isMethodTimingEnabled(Method var1) throws NotFoundException {
      try {
         return this.theJVM.getProfilingSystem().isTimingEnabled(var1);
      } catch (Throwable var3) {
         throw new NotFoundException(var3.getMessage());
      }
   }

   public boolean isConstructorTimingEnabled(Constructor var1) {
      return this.theJVM.getProfilingSystem().isTimingEnabled(var1);
   }

   public long getExceptionCount(Class var1) throws ClassCastException {
      return this.theJVM.getProfilingSystem().getExceptionCount(var1);
   }

   public long getMethodInvocationCount(Method var1) throws NotFoundException {
      try {
         ProfilingSystem var2 = this.theJVM.getProfilingSystem();
         return var2.getInvocationCount(var1);
      } catch (Throwable var3) {
         throw new NotFoundException(var3.getMessage());
      }
   }

   public long getConstructorInvocationCount(Constructor var1) throws NotFoundException {
      try {
         ProfilingSystem var2 = this.theJVM.getProfilingSystem();
         return var2.getInvocationCount(var1);
      } catch (Throwable var3) {
         throw new NotFoundException(var3.getMessage());
      }
   }
}
