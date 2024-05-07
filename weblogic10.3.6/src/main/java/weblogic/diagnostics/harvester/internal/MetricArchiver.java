package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.harvester.HarvestCallback;
import com.bea.adaptive.harvester.WatchedValues;
import com.bea.adaptive.harvester.WatchedValuesImpl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.application.ApplicationContext;
import weblogic.descriptor.DescriptorDiff;
import weblogic.diagnostics.accessor.DiagnosticAccessRuntime;
import weblogic.diagnostics.accessor.DiagnosticDataAccessRuntime;
import weblogic.diagnostics.archive.DataWriter;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.descriptor.WLDFHarvestedTypeBean;
import weblogic.diagnostics.descriptor.WLDFHarvesterBean;
import weblogic.diagnostics.descriptor.WLDFResourceBean;
import weblogic.diagnostics.harvester.HarvesterDataSample;
import weblogic.diagnostics.harvester.HarvesterException;
import weblogic.diagnostics.harvester.I18NConstants;
import weblogic.diagnostics.harvester.I18NSupport;
import weblogic.diagnostics.harvester.InstanceNameNormalizer;
import weblogic.diagnostics.harvester.LogSupport;
import weblogic.diagnostics.harvester.WLDFHarvester;
import weblogic.diagnostics.harvester.WLDFHarvesterLauncher;
import weblogic.diagnostics.harvester.WLDFHarvesterManager;
import weblogic.diagnostics.harvester.WLDFHarvesterUtils;
import weblogic.diagnostics.harvester.WLDFToHarvester;
import weblogic.diagnostics.i18n.DiagnosticsHarvesterLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.image.ImageManager;
import weblogic.diagnostics.lifecycle.DiagnosticComponentLifecycleException;
import weblogic.diagnostics.module.WLDFModule;
import weblogic.diagnostics.module.WLDFModuleException;
import weblogic.diagnostics.utils.DateUtils;
import weblogic.diagnostics.watch.WatchManager;
import weblogic.kernel.Kernel;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.t3.srvr.T3Srvr;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.PlatformConstants;
import weblogic.utils.time.Timer;
import weblogic.work.WorkManagerFactory;

public final class MetricArchiver implements TimerListener, WLDFToHarvester, I18NConstants {
   private static final String WATCHED_VALUES_NAME = "WLDFArchivedMetrics";
   private static final MetricArchiver archiver = new MetricArchiver();
   private ImageManager imageManager;
   private HarvesterRuntimeMBeanImpl harvesterRuntime;
   private boolean enabled = true;
   private static WLDFHarvester harvester;
   private static WLDFHarvesterLauncher launcher;
   private WatchedValues watchedValues = null;
   private int wvid = -1;
   private static boolean initializationComplete;
   private long samplePeriodNanos = 0L;
   private long samplePeriodFromConfigMillis = 0L;
   private boolean deployed = false;
   private boolean currentSnapshotAnOutlier = false;
   private long lastTotalTimeNanos = 0L;
   private long minimumSampleIntervalNanos;
   private int totalSamplingCycles = 0;
   private int currentDataSampleCount = 0;
   private long currentSnapshotStartTimeMillis = -1L;
   private long currentSnapshotElapsedTimeNanos = 0L;
   private long minimumSamplingTimeNanos = Long.MAX_VALUE;
   private long maximumSamplingTimeNanos = Long.MIN_VALUE;
   private long totalSamplingTimeNanos = 0L;
   private float averageSamplingTimeNanos = 0.0F;
   DataWriter archive = null;
   private HarvesterSnapshot inProgressSnapshot;
   private HarvesterSnapshot currentSnapshot;
   private Set<String> unharvestableTypes = null;
   private HashMap<String, HarvesterSpec> harvestSpecs = null;
   private long startTimeNanos = 0L;
   private long startTimeMillis = 0L;
   private long totalConfiguredDataSampleCount = 0L;
   private long totalImplicitDataSampleCount = 0L;
   private long outlierCount = 0L;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final Timer CLOCK = Timer.createTimer();
   private static final DebugLogger DBG = DebugSupport.getDebugLogger();
   private static final DebugLogger DBG_DATA = DebugSupport.getLowLevelDebugLogger();
   private static final float STAT_OUTLIER_MULT_FACTOR = 3.0F;
   private static final boolean TEST_NO_ARCHIVE = Boolean.getBoolean("weblogic.diagnostics.harvester.DBG_NoArchive");
   long previousExpired = 0L;
   private static final boolean TEST_TRACE_INTERVALS = Boolean.getBoolean("weblogic.diagnostics.harvester.DBG_TraceHarvestIntervals");
   private static final boolean TEST_TRACE_STATS = Boolean.getBoolean("weblogic.diagnostics.harvester.DBG_TraceHarvesterStatistics");
   private static final boolean TEST_REMOVE_OUTLIER_DETECTION = Boolean.getBoolean("weblogic.diagnostics.harvester.DBG_RemoveOutlierDetection");
   private static final Long TEST_HARVEST_DELAY_MILLIS = Long.getLong("weblogic.diagnostics.harvester.DBG_HarvesterDelay");
   int cycleCount = 0;
   private static final Integer TEST_HARVEST_DELAY_AT_CYCLE = Integer.getInteger("weblogic.diagnostics.harvester.DBG_HarvesterDelayAt");
   private static final boolean DBG_USE_LONG_DATA_FORM = Boolean.getBoolean("weblogic.diagnostics.harvester.DBG_UseLongDataForm");
   public static final String HARV_IMAGE_SOURCE_NAME = "HarvesterImageSource";
   private static final long MINIMUM_SAMPLE_INTERVAL_AS_PERCENT_OF_SAMPLE_PERIOD = 50L;
   public static final String HVST_TIMER_MANAGER = "HarvesterTimerManager";
   private static final long NANOS_PER_MILLI = 1000000L;
   private static final long TIME_CONVERSION;
   WLDFResourceBean rootBean = null;
   private WatchManager watchManager;
   private TimerManagerFactory timerManagerFactory;
   private TimerManager timerManager;
   private weblogic.timers.Timer harvestTimer;
   private int enabledTypeCount;
   private static boolean adminServer;
   private static boolean checkedServerType;

   synchronized WLDFHarvester getHarvester() {
      if (initializationComplete) {
         this.initWLDFHarvester();
      }

      return harvester;
   }

   public void finalizeActivation() {
      synchronized(this) {
         initializationComplete = true;
      }

      this.addWatchedValues();

      try {
         WatchManager var1 = WatchManager.getInstance();
         if (var1 != null) {
            var1.activateConfiguration();
         }
      } catch (ManagementException var3) {
         DiagnosticsHarvesterLogger.logErrorActivatingWatchConfiguration(var3);
      }

      this.initWLDFHarvester();
   }

   private synchronized void initWLDFHarvester() {
      if (initializationComplete) {
         if (harvester == null) {
            if (DBG.isDebugEnabled()) {
               DBG.debug("WLDFHarvester singleton being lazily initialized...");
            }

            if (launcher == null) {
               launcher = WLDFHarvesterManager.getInstance();
               launcher.prepare();
               launcher.activate();
            }

            harvester = launcher.getHarvesterSingleton();
         }
      } else if (DBG.isDebugEnabled()) {
         DBG.debug("Harvester service has not yet signaled that the activation is finalized");
      }

   }

   public boolean isActivated() {
      return initializationComplete;
   }

   private MetricArchiver() {
   }

   static long getNanoWallClockTime() {
      return CLOCK.timestamp() + TIME_CONVERSION;
   }

   void init(ApplicationContext var1, WLDFResourceBean var2) {
      this.rootBean = var2;
   }

   void prepare() {
   }

   synchronized void activate() {
      if (!this.deployed) {
         try {
            if (DBG.isDebugEnabled()) {
               DBG.debug("Harvester is being activated.");
            }

            this.watchManager = WatchManager.getInstance();
            this.imageManager = ImageManager.getInstance();
            this.imageManager.registerImageSource("HarvesterImageSource", new HarvesterImageSource());
            this.timerManagerFactory = TimerManagerFactory.getTimerManagerFactory();
            this.timerManager = this.timerManagerFactory.getTimerManager("HarvesterTimerManager", WorkManagerFactory.getInstance().getSystem());
            this.loadConfig(this.rootBean);
            this.deployed = true;
            if (DBG.isDebugEnabled()) {
               DBG.debug("Harvester activation is completed.");
            }
         } catch (Exception var2) {
            LogSupport.logUnexpectedException(I18NSupport.formatter().getConfigLoadingProblemMessage(), var2);
         }

      }
   }

   private void addWatchedValues() {
      try {
         if (this.rootBean != null && this.rootBean.getHarvester().isEnabled()) {
            if (this.enabledTypeCount == 0) {
               DiagnosticsLogger.logDisablingHarvesterDueToLackOfActiveConfig();
               this.disableHarvester();
            } else {
               this.wvid = this.addWatchedValues(this.watchedValues);
               this.enableHarvester();
            }
         }

      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   synchronized void deactivate() {
      if (DBG.isDebugEnabled()) {
         DBG.debug("Harvester is being deactivated.");
      }

      this.disableHarvester();
      this.harvestSpecs = null;
      this.watchManager = null;
      if (launcher != null && harvester != null) {
         this.deleteArchiverWatchedValues();
      }

      try {
         this.imageManager.unregisterImageSource("HarvesterImageSource");
         this.imageManager = null;
      } catch (Exception var2) {
         LogSupport.logUnexpectedException("Problem unregistering Harvester image source.", var2);
      }

      this.inProgressSnapshot = null;
      this.currentSnapshot = null;
      this.deployed = false;
      this.unharvestableTypes = null;
      if (DBG.isDebugEnabled()) {
         DBG.debug("Harvester is now inactive.");
      }

   }

   private void deleteArchiverWatchedValues() {
      if (this.wvid > -1) {
         try {
            this.deleteWatchedValues(this.watchedValues);
         } catch (HarvesterException var2) {
            DiagnosticsLogger.logErrorDeleteingWatchedValues(this.watchedValues.getName(), var2);
         }

         this.watchedValues = null;
         this.wvid = -1;
      }

   }

   void unprepare() {
   }

   void destroy() {
   }

   void prepareUpdate(WLDFResourceBean var1, DescriptorDiff var2) throws WLDFModuleException {
   }

   synchronized void activateUpdate(WLDFResourceBean var1, DescriptorDiff var2) throws WLDFModuleException {
      try {
         this.reloadConfig(var1);
      } catch (Exception var4) {
         LogSupport.logUnexpectedException("Problem consuming modified configuration.", var4);
      }

   }

   void rollbackUpdate(WLDFResourceBean var1, DescriptorDiff var2) {
   }

   public void timerExpired(weblogic.timers.Timer var1) {
      if (this.deployed && this.enabled) {
         if (DBG.isDebugEnabled()) {
            DBG.debug("Timer expired for harvester with period = " + var1.getPeriod() + " at " + DateUtils.nanoDateToString(System.currentTimeMillis() * 1000000L, true));
            DBG.debug("TIMER OBJECT  " + var1);
         }

         synchronized(WLDFModule.UPDATE_SYNC) {
            if (DBG.isDebugEnabled()) {
               DBG.debug("Entered sync block at " + DateUtils.nanoDateToString(System.currentTimeMillis() * 1000000L, true));
            }

            long var3 = 0L;
            long var5 = getNanoWallClockTime();
            long var7 = System.currentTimeMillis();
            if (TEST_TRACE_INTERVALS) {
               var3 = getNanoWallClockTime();
               System.out.print(" HTO: " + (var3 - this.previousExpired));
            }

            long var9 = var5 - this.startTimeNanos;
            if (var9 < this.minimumSampleIntervalNanos) {
               DiagnosticsLogger.logInsufficientTimeBetweenHarvesterCycles(var9 / 1000000L);
               if (TEST_TRACE_INTERVALS) {
                  System.out.print("*");
               }

            } else {
               this.startTimeNanos = var5;
               this.startTimeMillis = var7;
               this.execute(var5);
               if (this.watchManager != null) {
                  this.watchManager.evaluateHarvesterRules();
               }

               if (this.harvesterRuntime != null) {
                  long var11 = this.currentSnapshot != null ? this.currentSnapshot.getSnapshotStartTimeMillis() : this.startTimeMillis;
                  this.harvesterRuntime.harvestCycleOccurred(var11);
               }

               this.previousExpired = var3;
            }
         }
      } else {
         if (DBG.isDebugEnabled()) {
            DBG.debug("A pending harvester cycle is being skipped because the harvester has been disabled.");
         }

      }
   }

   public int addWatchedValues(WatchedValues var1) {
      int var2 = -1;
      WLDFHarvester var3 = this.getHarvester();
      if (var3 == null) {
         if (DBG.isDebugEnabled()) {
            DBG.debug("addWatchedValues(): harvester not yet initialized, returning");
         }

         return var2;
      } else {
         try {
            if (DBG.isDebugEnabled()) {
               DBG.debug("Harvester is being providing the list of watched data.");
            }

            var2 = var3.addWatchedValues(var1.getName(), var1, (HarvestCallback)null);
         } catch (Exception var5) {
            throw new RuntimeException(var5);
         }

         if (var1.getAllMetricValues().size() > 0) {
            if (DBG.isDebugEnabled()) {
               DBG.debug("Harvester watched data has been setup.");
            }

            if (!this.enabled) {
               if (DBG.isDebugEnabled()) {
                  DBG.debug("Harvester is being enabled implicitly with an internal of " + this.samplePeriodFromConfigMillis + " milliseconds.");
               }

               this.enableHarvester();
            } else if (DBG.isDebugEnabled()) {
               String var4 = this.dumpAsString("  ");
               DBG.debug(PlatformConstants.EOL + var4);
            }
         }

         return var2;
      }
   }

   public Collection<WatchedValues.Validation> validateWatchedValues(WatchedValues var1) throws HarvesterException.HarvestingNotEnabled {
      if (harvester != null) {
         return harvester.validateWatchedValues(var1);
      } else {
         throw new HarvesterException.HarvestingNotEnabled();
      }
   }

   int getStatus() {
      return this.enabled && this.deployed ? 1 : 2;
   }

   boolean isEnabled() {
      switch (this.getStatus()) {
         case 1:
            return true;
         default:
            return false;
      }
   }

   void initialize() {
      if (Kernel.isServer()) {
         try {
            this.harvesterRuntime = HarvesterRuntimeMBeanImpl.getInstance(this);
         } catch (ManagementException var2) {
            throw new RuntimeException(var2);
         }
      }

   }

   void enable() throws DiagnosticComponentLifecycleException {
   }

   void disable() throws DiagnosticComponentLifecycleException {
      if (this.harvestTimer != null) {
         this.harvestTimer.cancel();
      }

      this.harvestTimer = null;
   }

   public static MetricArchiver getInstance() {
      return archiver;
   }

   HarvesterSnapshot getCurrentSnapshot() {
      return this.currentSnapshot;
   }

   long getSamplePeriod() throws HarvesterException.HarvestingNotEnabled {
      return this.samplePeriodFromConfigMillis;
   }

   int getTotalSamplingCycles() {
      return this.totalSamplingCycles;
   }

   long getTotalConfiguredDataSampleCount() {
      return this.totalConfiguredDataSampleCount;
   }

   int getCurrentConfiguredDataSampleCount() throws HarvesterException.HarvestingNotEnabled {
      return this.currentDataSampleCount;
   }

   long getTotalImplicitDataSampleCount() {
      return this.totalImplicitDataSampleCount;
   }

   long getCurrentImplicitDataSampleCount() throws HarvesterException.HarvestingNotEnabled {
      if (this.enabled && this.deployed) {
         return this.watchedValues == null ? 0L : this.watchedValues.getMostRecentValuesCount();
      } else {
         throw new HarvesterException.HarvestingNotEnabled();
      }
   }

   public long getTotalSamplingTimeOutlierCount() {
      return this.outlierCount;
   }

   public boolean isCurrentSampleTimeAnOutlier() {
      return this.currentSnapshotAnOutlier;
   }

   public float getOutlierDetectionFactor() {
      return 3.0F;
   }

   long getCurrentSnapshotStartTime() throws HarvesterException.HarvestingNotEnabled {
      return this.currentSnapshotStartTimeMillis * 1000000L;
   }

   long getCurrentSnapshotElapsedTime() throws HarvesterException.HarvestingNotEnabled {
      return this.currentSnapshotElapsedTimeNanos;
   }

   long getMinimumSamplingTime() {
      return this.minimumSamplingTimeNanos;
   }

   long getMaximumSamplingTime() {
      return this.maximumSamplingTimeNanos;
   }

   long getTotalSamplingTime() {
      return this.totalSamplingTimeNanos;
   }

   long getAverageSamplingTime() {
      return (long)Math.round(this.averageSamplingTimeNanos);
   }

   synchronized String[] getCurrentlyHarvestedAttributes(String var1) throws HarvesterException.MissingConfigurationType {
      String[] var2 = new String[0];
      WLDFHarvester var3 = this.getHarvester();
      if (var3 != null) {
         this.checkConfiguredType(var1);
         List var4 = var3.getHarvestedAttributes(this.wvid, var1, (String)null);
         if (var4 != null && var4.size() > 0) {
            var2 = (String[])var4.toArray(new String[var4.size()]);
         }
      }

      return var2;
   }

   public String[] getConfiguredNamespaces() {
      String[] var1 = new String[0];
      WLDFHarvester var2 = this.getHarvester();
      if (var2 != null) {
         var1 = var2.getSupportedNamespaces();
      }

      return var1;
   }

   public String getDefaultNamespace() {
      String var1 = null;
      WLDFHarvester var2 = this.getHarvester();
      if (var2 != null) {
         var1 = var2.getDefaultNamespace();
      }

      return var1;
   }

   private void checkConfiguredType(String var1) throws HarvesterException.MissingConfigurationType {
      HarvesterSpec var2 = null;
      if (this.harvestSpecs != null) {
         synchronized(this.harvestSpecs) {
            var2 = (HarvesterSpec)this.harvestSpecs.get(var1);
         }
      }

      if (var2 == null) {
         throw new HarvesterException.MissingConfigurationType(var1);
      }
   }

   String[] getKnownHarvestableTypes() {
      return this.getKnownHarvestableTypes((String)null);
   }

   String[] getKnownHarvestableTypes(String var1) {
      String[] var2 = new String[0];
      WLDFHarvester var3 = this.getHarvester();
      if (var3 != null) {
         try {
            String[][] var4 = var3.getKnownHarvestableTypes(var1, (String)null);
            if (var4 != null) {
               var2 = new String[var4.length];

               for(int var5 = 0; var5 < var4.length; ++var5) {
                  var2[var5] = var4[var5][0];
               }
            }
         } catch (IOException var6) {
            throw new RuntimeException(var6);
         }
      }

      return var2;
   }

   String[] getKnownHarvestableInstances(String var1) throws HarvesterException.AmbiguousTypeName, HarvesterException.HarvestableTypesNotFoundException, HarvesterException.TypeNotHarvestable {
      return this.getKnownHarvestableInstances((String)null, var1);
   }

   synchronized String[] getKnownHarvestableInstances(String var1, String var2) throws HarvesterException.AmbiguousTypeName, HarvesterException.HarvestableTypesNotFoundException, HarvesterException.TypeNotHarvestable {
      String[] var3 = new String[0];
      WLDFHarvester var4 = this.getHarvester();
      if (var4 != null) {
         try {
            List var5 = var4.getKnownHarvestableInstances(var1, var2, (String)null);
            var3 = (String[])var5.toArray(new String[var5.size()]);
         } catch (IOException var6) {
            throw new RuntimeException(var6);
         } catch (RuntimeException var7) {
            throw new HarvesterException.HarvestableTypesNotFoundException(new String[]{var2}, var7);
         }

         if (var3 == null) {
            throw new HarvesterException.HarvestableTypesNotFoundException(new String[]{var2});
         }
      }

      return var3;
   }

   synchronized String[] getCurrentlyHarvestedInstances(String var1) throws HarvesterException.MissingConfigurationType {
      String[] var2 = new String[0];
      WLDFHarvester var3 = this.getHarvester();
      if (var3 != null) {
         this.checkConfiguredType(var1);

         try {
            List var4 = var3.getHarvestedInstances(this.wvid, var1, (String)null);
            var2 = new String[var4.size()];
            var4.toArray(var2);
         } catch (RuntimeException var5) {
            throw new HarvesterException.MissingConfigurationType(var1, var5);
         }
      }

      return var2;
   }

   synchronized String[][] getHarvestableAttributes(String var1) throws IOException, HarvesterException.AmbiguousTypeName, HarvesterException.HarvestableTypesNotFoundException, HarvesterException.TypeNotHarvestable {
      String[][] var2 = new String[0][];
      WLDFHarvester var3 = this.getHarvester();
      if (var3 != null) {
         String[][] var4 = var3.getHarvestableAttributes(var1, (String)null);
         if (var4 != null) {
            ArrayList var5 = new ArrayList();
            String[][] var6 = var4;
            int var7 = var4.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String[] var9 = var6[var8];
               String var10 = var9[1];
               if (HarvesterSnapshot.isSupportedValueType(var10)) {
                  var5.add(var9);
               }
            }

            if (var5.size() > 0) {
               var2 = (String[][])var5.toArray(var2);
            }
         }
      }

      return var2;
   }

   public synchronized String getHarvestableType(String var1) throws HarvesterException.HarvestableInstancesNotFoundException, HarvesterException.AmbiguousInstanceName {
      String var2 = null;
      WLDFHarvester var3 = this.getHarvester();
      if (var3 != null) {
         String var4 = var1;

         try {
            var4 = (new ObjectName(var1)).getCanonicalName();
         } catch (MalformedObjectNameException var6) {
         } catch (NullPointerException var7) {
         }

         var2 = WLDFHarvesterUtils.getTypeForInstance(var1);
         if (var2 == null) {
            var2 = var3.getTypeForInstance(var4);
         }
      }

      return var2;
   }

   private synchronized void execute(long var1) {
      if (T3Srvr.getT3Srvr().getRunState() != 2) {
         if (DBG.isDebugEnabled()) {
            DBG.debug("Scheduled harvest cycle postponed util server is started.");
         }

      } else {
         if (DBG.isDebugEnabled()) {
            DBG.debug("Executing harvesting process.");
         }

         if (harvester != null) {
            harvester.harvest((Map)null);
         }

         long var3 = System.currentTimeMillis();
         this.inProgressSnapshot = new HarvesterSnapshot(var3);
         if (this.watchedValues == null) {
            if (DBG.isDebugEnabled()) {
               DBG.debug("Watched values for MetricArchiver not set, skipping harvest cycle");
            }

            DiagnosticsLogger.logHarvesterIsDisabled();
         } else {
            List var5 = this.watchedValues.getAllMetricValues();
            this.inProgressSnapshot.setDataSamples(var5);
            long var6 = var5 != null ? (long)var5.size() : 0L;
            long var8 = this.watchedValues != null ? this.watchedValues.getMostRecentValuesCount() : 0L;
            this.totalConfiguredDataSampleCount += var6;
            this.totalImplicitDataSampleCount += var8;
            long var10 = var8 + var6;
            if (var10 == 0L) {
               if (DBG.isDebugEnabled()) {
                  DBG.debug("No data was harvested");
               }

            } else {
               if (var6 > 0L) {
                  DBG.debug("Harvested " + var6 + " data samples from configured specifications.");
               }

               if (var8 > 0L) {
                  DBG.debug("Harvested " + var8 + " implicit data samples.");
               }

               long var12 = 0L;
               long var14 = 0L;
               long var16 = 0L;
               boolean var18;
               long var26;
               if (TEST_HARVEST_DELAY_MILLIS != null) {
                  var18 = true;
                  if (TEST_HARVEST_DELAY_AT_CYCLE != null) {
                     int var19 = TEST_HARVEST_DELAY_AT_CYCLE;
                     ++this.cycleCount;
                     if (this.cycleCount != var19) {
                        var18 = false;
                     }
                  }

                  if (var18) {
                     var26 = TEST_HARVEST_DELAY_MILLIS;

                     try {
                        Thread.sleep(var26);
                     } catch (Exception var24) {
                     }
                  }
               }

               if (DBG_DATA.isDebugEnabled()) {
                  String var25;
                  if (var6 > 0L) {
                     var25 = PlatformConstants.EOL + PlatformConstants.EOL + "*************************************************" + PlatformConstants.EOL + "CONFIGURED DATA SAMPLES (set in the last cycle):" + PlatformConstants.EOL + "*************************************************" + PlatformConstants.EOL + PlatformConstants.EOL;
                     var26 = this.inProgressSnapshot.getSnapshotStartTimeMillis();
                     var25 = var25 + "Timestamp of the most recent collection: " + DateUtils.nanoDateToString(var26 * 1000000L, true) + PlatformConstants.EOL + PlatformConstants.EOL;

                     String var23;
                     for(Iterator var21 = this.inProgressSnapshot.getHarvesterDataSamples().iterator(); var21.hasNext(); var25 = var25 + var23 + PlatformConstants.EOL) {
                        HarvesterDataSample var22 = (HarvesterDataSample)var21.next();
                        var23 = DBG_USE_LONG_DATA_FORM ? var22.toStringLong() : var22.toStringShort();
                     }

                     DBG_DATA.debug(var25);
                  }

                  if (var8 > 0L) {
                     var25 = this.watchedValues.dump("", true, false, !DBG_USE_LONG_DATA_FORM);
                     DBG_DATA.debug(var25);
                  }
               }

               if (DBG.isDebugEnabled()) {
                  var12 = getNanoWallClockTime() - var1;
               }

               if (!TEST_NO_ARCHIVE) {
                  long var27 = 0L;
                  if (DBG.isDebugEnabled()) {
                     var27 = getNanoWallClockTime();
                  }

                  this.archive();
                  if (DBG.isDebugEnabled()) {
                     var14 = getNanoWallClockTime() - var27;
                  }
               }

               var16 = getNanoWallClockTime() - var1;
               if (DBG.isDebugEnabled()) {
                  DBG.debug("Nanosecond timings for this harvest cycle:   1) harvest: " + var12 + "   2) archive: " + var14 + "   3) total: " + var16);
               }

               var18 = (float)var16 > (float)this.lastTotalTimeNanos * 3.0F;
               if (TEST_REMOVE_OUTLIER_DETECTION) {
                  var18 = false;
               }

               if (TEST_TRACE_STATS) {
                  System.out.print("HT: " + var16);
                  System.out.println("" + (var18 ? "*" : ""));
               }

               this.inProgressSnapshot.setSnapshotElapsedTimeNanos(var16);
               this.currentSnapshot = this.inProgressSnapshot;
               this.inProgressSnapshot = null;
               if (!var18) {
                  if (!this.currentSnapshotAnOutlier) {
                     this.updateHarvesterRuntime(var16, 0L);
                  } else {
                     if (DBG.isDebugEnabled()) {
                        DBG.debug("The last harvest cycle was removed from the Harvester statistics as an outlier.  To make up for this we are adding the current values twice.");
                     }

                     this.updateHarvesterRuntime(var16, var16);
                  }
               } else {
                  ++this.outlierCount;
                  if (!this.currentSnapshotAnOutlier) {
                     if (DBG.isDebugEnabled()) {
                        DBG.debug("This harvest cycle took significantly longer than expected.  It is being removed from the harvester statistics as an outlier");
                     }
                  } else {
                     if (DBG.isDebugEnabled()) {
                        DBG.debug("This harvest cycle took significantly longer than expected.  It is being removed from the harvester statistics as an outlier.  In addition, the last cycle was removed for the same reasons.  Because this value is larger we are promoting it to outlier status and adding in the previous cycle values twice.");
                     }

                     this.updateHarvesterRuntime(this.lastTotalTimeNanos, this.lastTotalTimeNanos);
                  }
               }

               this.lastTotalTimeNanos = var16;
               this.currentSnapshotAnOutlier = var18;
            }
         }
      }
   }

   private DataWriter getArchive() {
      if (this.archive == null) {
         try {
            DiagnosticAccessRuntime var1 = DiagnosticAccessRuntime.getInstance();
            DiagnosticDataAccessRuntime var2 = (DiagnosticDataAccessRuntime)var1.lookupWLDFDataAccessRuntime("HarvestedDataArchive");
            this.archive = (DataWriter)var2.getDiagnosticDataAccessService();
         } catch (Exception var3) {
            LogSupport.logUnexpectedException("Could not find harvester archive", var3);
         }
      }

      return this.archive;
   }

   private void archive() {
      DataWriter var1 = this.getArchive();
      if (var1 != null) {
         Collection var2 = this.inProgressSnapshot.getHarvesterDataSamples();
         if (DBG.isDebugEnabled()) {
            DBG.debug("Archiving " + var2.size() + " samples...");
         }

         if (var2.size() == 0) {
            return;
         }

         try {
            var1.writeData(var2);
         } catch (IOException var4) {
            DiagnosticsLogger.logErrorHarvesting(var4);
         }
      }

   }

   private void updateHarvesterRuntime(long var1, long var3) {
      ++this.totalSamplingCycles;
      this.currentSnapshotStartTimeMillis = this.currentSnapshot.getSnapshotStartTimeMillis();
      this.currentSnapshotElapsedTimeNanos = var1;
      this.currentDataSampleCount = this.currentSnapshot.getDataSampleCount();
      this.totalSamplingTimeNanos += this.currentSnapshotElapsedTimeNanos;
      this.averageSamplingTimeNanos = (float)this.totalSamplingTimeNanos / (float)this.totalSamplingCycles;
      this.minimumSamplingTimeNanos = Math.min(this.minimumSamplingTimeNanos, this.currentSnapshotElapsedTimeNanos);
      this.maximumSamplingTimeNanos = Math.max(this.maximumSamplingTimeNanos, this.currentSnapshotElapsedTimeNanos);
      if (TEST_TRACE_STATS) {
         System.out.println("HS:              " + this.currentSnapshotElapsedTimeNanos + " " + this.totalSamplingTimeNanos + " " + this.averageSamplingTimeNanos);
      }

      if (var3 != 0L) {
         ++this.totalSamplingCycles;
         this.totalSamplingTimeNanos += var3;
         this.averageSamplingTimeNanos = (float)this.totalSamplingTimeNanos / (float)this.totalSamplingCycles;
         if (TEST_TRACE_STATS) {
            System.out.println("HS2:             " + var3 + " " + this.totalSamplingTimeNanos + " " + this.averageSamplingTimeNanos);
         }
      }

   }

   public WatchedValues createWatchedValues(String var1) {
      WatchedValuesImpl var2 = new WatchedValuesImpl(var1, this.getSamplePeriodSeconds());
      var2.setShared(true);
      var2.setId(-1);
      return var2;
   }

   public void deleteWatchedValues(WatchedValues var1) throws HarvesterException {
      if (harvester == null) {
         throw new HarvesterException.HarvestingNotEnabled();
      } else {
         try {
            if (var1 != null) {
               if (var1.getId() >= 0) {
                  harvester.deleteWatchedValues(var1);
                  var1.setId(-1);
               } else if (DBG.isDebugEnabled()) {
                  DBG.debug("deleteWatchedValues(): nothing to do, watched values list id is invalid: " + var1.getId());
               }
            } else if (DBG.isDebugEnabled()) {
               DBG.debug("deleteWatchedValues(): nothing to do, watched values list is null");
            }
         } catch (Exception var4) {
            String var3 = var1 == null ? "Unknown" : var1.getName();
            DiagnosticsLogger.logErrorDeleteingWatchedValues(var3, var4);
         }

      }
   }

   private void addUnharvestableType(String var1) {
      DiagnosticsLogger.logTypeRemoval(var1);
   }

   private void loadConfigInternal(WLDFResourceBean var1) {
      WLDFHarvesterBean var2 = var1.getHarvester();
      this.resetState(var2);
      if (!var2.isEnabled()) {
         DiagnosticsLogger.logHarvesterIsDisabled();
         this.disableHarvester();
      } else {
         WLDFHarvestedTypeBean[] var3 = var2.getHarvestedTypes();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            WLDFHarvestedTypeBean var5 = var3[var4];
            String var6 = var5.getName();
            String var7 = var5.getNamespace();
            if (!isAdminServer() && var7.equals("DomainRuntime")) {
               DiagnosticsHarvesterLogger.logUnservicableHarvestedTypeNamespaceError(var6, var7);
            } else {
               HarvesterSpec var8 = (HarvesterSpec)this.harvestSpecs.get(var6);
               boolean var9 = var5.isEnabled();
               if (DBG.isDebugEnabled()) {
                  DBG.debug("Harvester configuration for type: " + var6 + " is currently " + (var9 ? "enabled" : "disabled"));
               }

               String[] var10 = var5.getHarvestedAttributes();
               if (var10 != null && var10.length == 0) {
                  var10 = null;
               }

               String[] var11 = var5.getHarvestedInstances();
               if (var11 != null && var11.length == 0) {
                  var11 = null;
               }

               if (var8 == null) {
                  try {
                     var10 = WLDFHarvesterUtils.normalizeAttributeSpecs(var6, var10);
                     var8 = new HarvesterSpec(var6, var10, var11, var9);
                     this.registerWatchedValues(var7, var6, var11, var10, var9);
                     this.harvestSpecs.put(var6, var8);
                  } catch (HarvesterException.HarvestingNotEnabled var13) {
                     throw new RuntimeException(var13);
                  } catch (HarvesterException.NullName var14) {
                     this.addUnharvestableType(var6);
                  }
               }

               if (!var9) {
                  DiagnosticsLogger.logHarvesterTypeIsDisabled(var6);
               } else {
                  ++this.enabledTypeCount;
               }
            }
         }

         this.addWatchedValues();
      }
   }

   private void resetState(WLDFHarvesterBean var1) {
      this.deleteArchiverWatchedValues();
      this.watchedValues = this.createWatchedValues("WLDFArchivedMetrics");
      this.harvestSpecs = new HashMap();
      this.unharvestableTypes = new HashSet();
      this.samplePeriodFromConfigMillis = var1.getSamplePeriod();
      this.enabledTypeCount = 0;
   }

   private void registerWatchedValues(String var1, String var2, String[] var3, String[] var4, boolean var5) throws HarvesterException.HarvestingNotEnabled {
      if (var3 == null) {
         this.registerWatchedValues(var1, var2, (String)null, false, var4, var5);
      } else {
         String[] var6 = var3;
         int var7 = var3.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            InstanceNameNormalizer var10 = new InstanceNameNormalizer(var9);

            try {
               this.registerWatchedValues(var1, var2, var10.translateHarvesterSpec(), var10.isRegexPattern(), var4, var5);
            } catch (Exception var12) {
               DiagnosticsHarvesterLogger.logInstanceNameInvalid(var9);
            }
         }
      }

   }

   private void registerWatchedValues(String var1, String var2, String var3, boolean var4, String[] var5, boolean var6) throws HarvesterException.HarvestingNotEnabled {
      if (var5 == null) {
         this.watchedValues.addMetric(var1, var2, var3, (String)null, false, var4, false, var6);
      } else {
         String[] var7 = var5;
         int var8 = var5.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String var10 = var7[var9];
            this.watchedValues.addMetric(var1, var2, var3, var10, false, false, false, var6);
         }
      }

   }

   private int getSamplePeriodSeconds() {
      return (int)this.samplePeriodFromConfigMillis / 1000;
   }

   private void loadConfig(WLDFResourceBean var1) {
      DiagnosticsLogger.logConfigLoading();
      this.loadConfigInternal(var1);
   }

   private void reloadConfig(WLDFResourceBean var1) {
      DiagnosticsLogger.logConfigReloading();
      if (!this.deployed) {
         throw new AssertionError(LogSupport.getGenericHarvesterProblemText("Attempt to load a new configuration into an undeployed Harvester."));
      } else {
         this.loadConfigInternal(var1);
      }
   }

   private void enableHarvester() {
      long var1 = this.samplePeriodFromConfigMillis * 1000000L;
      if (!this.enabled || var1 != this.samplePeriodNanos) {
         if (this.harvestTimer != null) {
            this.harvestTimer.cancel();
         }

         this.samplePeriodNanos = var1;
         this.minimumSampleIntervalNanos = (long)Math.round((float)this.samplePeriodNanos / 100.0F * 50.0F);
         this.harvestTimer = this.scheduleTimer(this, this.samplePeriodFromConfigMillis);
         DiagnosticsLogger.logHarvestTimerInitiated(this.samplePeriodFromConfigMillis);
      }

      this.enabled = true;
      if (DBG.isDebugEnabled()) {
         String var3 = this.dumpAsString("  ");
         DBG.debug(PlatformConstants.EOL + var3);
      }

      this.postEnabledStatusMessage();
   }

   private void disableHarvester() {
      if (this.harvestTimer != null) {
         this.harvestTimer.cancel();
      }

      this.harvestTimer = null;
      this.harvestSpecs = null;
      this.enabled = false;
      this.postEnabledStatusMessage();
   }

   private void postEnabledStatusMessage() {
      String var1 = this.enabled ? ACTIVE_I18N : INACTIVE_I18N;
      DiagnosticsLogger.logHarvestState(var1);
   }

   private weblogic.timers.Timer scheduleTimer(final MetricArchiver var1, final long var2) {
      weblogic.timers.Timer var4 = null;

      try {
         var4 = (weblogic.timers.Timer)SecurityServiceManager.runAs(KERNEL_ID, SecurityServiceManager.getCurrentSubject(KERNEL_ID), new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               MetricArchiver.this.harvestTimer = MetricArchiver.this.timerManager.scheduleAtFixedRate(var1, 0L, var2);
               return MetricArchiver.this.harvestTimer;
            }
         });
         return var4;
      } catch (RuntimeException var6) {
         throw var6;
      } catch (PrivilegedActionException var7) {
         throw new RuntimeException(var7);
      }
   }

   String dumpAsString(String var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      PrintStream var3 = new PrintStream(var2);
      this.dump(var3, var1);
      String var4 = var2.toString();
      return var4;
   }

   void dump(PrintStream var1, String var2) {
      var1.println("");
      var1.println(var2 + "Active Harvester Configuration:");
      var1.println(var2 + "  enabled: " + this.enabled);
      var1.println(var2 + "  samplePeriod: " + Math.round((float)this.samplePeriodNanos / 1000000.0F));
      var1.println(var2 + "  unharvestableTypes: " + this.unharvestableTypes);
      var1.println(var2 + "  Current Configured Harvester Specifications:");
      if (this.watchedValues != null) {
         this.watchedValues.dump("\t", true, true, false);
      }

   }

   public static boolean isAdminServer() {
      if (!checkedServerType) {
         RuntimeAccess var0 = ManagementService.getRuntimeAccess(KERNEL_ID);
         adminServer = var0.isAdminServer();
         checkedServerType = true;
         if (adminServer && DBG.isDebugEnabled()) {
            DBG.debug("adminServer is " + adminServer);
         }
      }

      return adminServer;
   }

   static {
      TIME_CONVERSION = System.currentTimeMillis() * 1000000L - CLOCK.timestamp();
   }
}
