package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.harvester.HarvestCallback;
import com.bea.adaptive.harvester.Harvester;
import com.bea.adaptive.harvester.WatchedValues;
import com.bea.adaptive.harvester.WatchedValuesImpl;
import com.bea.adaptive.mbean.typing.MBeanCategorizer;
import com.bea.adaptive.mbean.typing.MBeanCategorizerPlugins;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import javax.management.JMException;
import javax.management.MBeanServer;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.harvester.HarvesterConstants;
import weblogic.diagnostics.harvester.HarvesterException;
import weblogic.diagnostics.harvester.HarvesterRuntimeException;
import weblogic.diagnostics.harvester.InvalidHarvesterNamespaceException;
import weblogic.diagnostics.harvester.LogSupport;
import weblogic.diagnostics.harvester.WLDFHarvester;
import weblogic.diagnostics.harvester.WLDFHarvesterUtils;
import weblogic.diagnostics.i18n.DiagnosticsHarvesterLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.i18n.DiagnosticsTextHarvesterTextFormatter;
import weblogic.diagnostics.utils.SecurityHelper;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class WLDFHarvesterImpl implements WLDFHarvester, HarvesterConstants {
   public static final String WLDFHARVESTER_NAME = "WLDFHarvester";
   private final DebugLogger debugLogger = DebugSupport.getDebugLogger();
   private static final WLDFHarvesterImpl self = new WLDFHarvesterImpl("WLDFHarvester");
   private static final DebugLogger DBG = DebugLogger.getDebugLogger("DebugDiagnosticsHarvester");
   private String name;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private DelegateHarvesterManager harvesterManager = DelegateHarvesterManagerImpl.createDelegateHarvesterManager();
   private ConcurrentHashMap<Integer, WatchedValuesControl> wvidToDelegatesMap = new ConcurrentHashMap();
   private int nextWVID;
   private int defaultSamplePeriod = 300;
   private boolean attributeTrackingEnabled;
   private boolean adminServer;
   private boolean attributeValidationEnabled;

   public static WLDFHarvesterImpl getInstance() {
      SecurityHelper.checkForAdminRole();
      return self;
   }

   WLDFHarvesterImpl(String var1) {
      this.name = var1;
   }

   public int addWatchedValues(String var1, WatchedValues var2, HarvestCallback var3) throws IOException, JMException {
      SecurityHelper.checkForAdminRole();
      int var4 = -1;
      Collection var5 = this.mapWatchedValues(var2);
      if (var5 == null) {
         return -1;
      } else {
         ArrayList var6 = null;
         Iterator var7 = this.harvesterManager.activeOnlyIterator();

         while(var7.hasNext()) {
            Harvester var8 = (Harvester)var7.next();
            ArrayList var9 = this.findVidsForHarvester(var8, var5);
            if (var9 != null) {
               if (var4 < 0) {
                  var4 = this.computeNextWVID();
               }

               if (DBG.isDebugEnabled()) {
                  DBG.debug("Found " + var9.size() + " metrics claimed by harvester " + var8.getName());
                  DBG.debug("VIDS claimed: " + var9.toString());
               }

               int var10 = var8.addWatchedValues(var1, var2, var3);
               if (var6 == null) {
                  var6 = new ArrayList(this.harvesterManager.getConfiguredHarvestersCount());
               }

               WatchedValuesDelegateMap var11 = new WatchedValuesDelegateMap(var8, var9, var10, var2);
               var6.add(var11);
            }
         }

         if (var4 > -1) {
            this.wvidToDelegatesMap.put(var4, new WatchedValuesControl(var2, var3, var6));
            var2.setId(var4);
         }

         if (var5.size() > 0) {
            WLDFHarvesterUtils.processValidationResults(var2.getName(), this.extractValidations(var5));
         }

         if (this.debugLogger.isDebugEnabled()) {
            this.debugLogger.debug("addWatchedValues(): " + this.wvidToDelegatesMap.size() + " watched values are now active");
         }

         return var4;
      }
   }

   public Collection<WatchedValues.Validation> validateWatchedValues(WatchedValues var1) {
      Collection var2 = this.mapWatchedValues(var1);
      ArrayList var3 = new ArrayList(var2.size());
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         var3.add(((ValidationMap)var4.next()).vote);
      }

      return var3;
   }

   public int deleteMetrics(int var1, Collection<WatchedValues.Values> var2) {
      int var3 = 0;
      List var4 = this.getMapListForParentWVID(var1);

      WatchedValuesDelegateMap var6;
      Collection var7;
      for(Iterator var5 = var4.iterator(); var5.hasNext(); var3 += var6.getDelegateHarvester().deleteMetrics(var1, var7)) {
         var6 = (WatchedValuesDelegateMap)var5.next();
         var7 = var6.findMatchingValuesSlots(var2);
      }

      return var3;
   }

   public int deleteWatchedValues(int[] var1) {
      int var2 = 0;
      int[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int var6 = var3[var5];
         List var7 = this.getMapListForParentWVID(var6);
         Iterator var8 = var7.iterator();

         while(var8.hasNext()) {
            WatchedValuesDelegateMap var9 = (WatchedValuesDelegateMap)var8.next();
            if (var9.getDelegateHarvester().deleteWatchedValues(new int[]{var9.getDelegateWVID()}) > 0) {
               ++var2;
            }
         }

         var7.clear();
         this.wvidToDelegatesMap.remove(var6);
      }

      if (this.debugLogger.isDebugEnabled()) {
         this.debugLogger.debug("deleteWatchedValues(): " + this.wvidToDelegatesMap.size() + " watched values are now active");
      }

      return var2;
   }

   public int disableMetrics(int var1, Integer[] var2) {
      int var3 = 0;
      WatchedValuesControl var4 = (WatchedValuesControl)this.wvidToDelegatesMap.get(var1);
      WatchedValuesDelegateMap var6;
      if (var4 != null) {
         for(Iterator var5 = var4.getMapList().iterator(); var5.hasNext(); var3 += var6.disableMetrics(var2)) {
            var6 = (WatchedValuesDelegateMap)var5.next();
         }
      }

      return var3;
   }

   public int enableMetrics(int var1, Integer[] var2) {
      int var3 = 0;
      WatchedValuesControl var4 = (WatchedValuesControl)this.wvidToDelegatesMap.get(var1);
      WatchedValuesDelegateMap var6;
      if (var4 != null) {
         for(Iterator var5 = var4.getMapList().iterator(); var5.hasNext(); var3 += var6.enableMetrics(var2)) {
            var6 = (WatchedValuesDelegateMap)var5.next();
         }
      }

      return var3;
   }

   public void extendWatchedValues(int var1, WatchedValues var2) {
      SecurityHelper.checkForAdminRole();
      WatchedValuesControl var3 = (WatchedValuesControl)this.wvidToDelegatesMap.get(var1);
      if (var3 == null) {
         throw new HarvesterRuntimeException(DiagnosticsTextHarvesterTextFormatter.getInstance().getWatchedValuesIdNotFoundText(var1));
      } else {
         Collection var4 = this.mapWatchedValues(var2);
         if (var4 != null) {
            Iterator var5 = this.harvesterManager.activeOnlyIterator();

            while(var5.hasNext()) {
               Harvester var6 = (Harvester)var5.next();
               ArrayList var7 = this.findVidsForHarvester(var6, var4);
               if (var7 != null) {
                  WatchedValuesDelegateMap var8 = var3.findDelegateMap(var6);
                  if (var8 != null) {
                     var8.extendWatchedValues(var2, var7);
                  } else {
                     ArrayList var9 = var3.extendDelegateMap(var6, var2, var7);
                     if (DBG.isDebugEnabled()) {
                        DBG.debug("extendWatchedValues: Found " + var9.size() + " metrics claimed by harvester " + var6.getName());
                        DBG.debug("extendWatchedValues: VIDS claimed: " + var9.toString());
                     }
                  }
               }
            }
         }

      }
   }

   public String[][] getHarvestableAttributes(String var1, String var2) throws IOException {
      ArrayList var3 = new ArrayList();
      Iterator var4 = this.harvesterManager.activatingIterator();

      while(var4.hasNext()) {
         Harvester var5 = (Harvester)var4.next();
         int var6 = var5.isTypeHandled(var1);
         if (var6 == 2) {
            return var5.getHarvestableAttributes(var1, var2);
         }

         if (var6 == 1) {
            var3.add(var5);
         }
      }

      String[][] var7 = (String[][])null;
      if (var3.size() > 0) {
         Iterator var8 = var3.iterator();

         while(var8.hasNext()) {
            var7 = ((Harvester)var8.next()).getHarvestableAttributes(var1, var2);
            if (var7 != null) {
               break;
            }
         }
      }

      return var7;
   }

   public List<String> getHarvestedAttributes(int var1, String var2, String var3) {
      List var4 = null;
      WatchedValuesControl var5 = (WatchedValuesControl)this.wvidToDelegatesMap.get(var1);
      if (var5 != null) {
         List var6 = var5.getMapList();
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            WatchedValuesDelegateMap var8 = (WatchedValuesDelegateMap)var7.next();
            List var9 = var8.getDelegateHarvester().getHarvestedAttributes(var8.getDelegateWVID(), var2, var3);
            if (var9 != null && var9.size() > 0) {
               var4 = var9;
               break;
            }
         }
      }

      return var4;
   }

   public List<String> getHarvestedInstances(int var1, String var2, String var3) {
      List var4 = null;
      WatchedValuesControl var5 = (WatchedValuesControl)this.wvidToDelegatesMap.get(var1);
      if (var5 != null) {
         List var6 = var5.getMapList();
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            WatchedValuesDelegateMap var8 = (WatchedValuesDelegateMap)var7.next();
            List var9 = var8.getDelegateHarvester().getHarvestedInstances(var8.getDelegateWVID(), var2, var3);
            if (var9 != null && var9.size() > 0) {
               var4 = var9;
               break;
            }
         }
      }

      return var4;
   }

   public List<String> getHarvestedTypes(int var1, String var2) {
      ArrayList var3 = new ArrayList();
      WatchedValuesControl var4 = (WatchedValuesControl)this.wvidToDelegatesMap.get(var1);
      if (var4 != null) {
         Iterator var5 = var4.getMapList().iterator();

         while(var5.hasNext()) {
            WatchedValuesDelegateMap var6 = (WatchedValuesDelegateMap)var5.next();
            List var7 = var6.getHarvestedTypes(var2);
            var3.addAll(var7);
         }
      }

      return var3;
   }

   public List<String> getKnownHarvestableInstances(String var1, String var2) throws IOException {
      return this.getKnownHarvestableInstances((String)null, var1, var2);
   }

   public List<String> getKnownHarvestableInstances(String var1, String var2, String var3) throws IOException {
      Pattern var4 = var1 == null ? null : Pattern.compile(var1);
      ArrayList var5 = new ArrayList();
      Iterator var6 = this.harvesterManager.activatingIterator();

      while(var6.hasNext()) {
         Harvester var7 = (Harvester)var6.next();
         boolean var8 = var4 == null || var4.matcher(var7.getNamespace()).matches();
         if (var8) {
            int var9 = var7.isTypeHandled(var2);
            if (var9 == 2) {
               return var7.getKnownHarvestableInstances(var2, var3);
            }

            if (var9 == 1) {
               var5.add(var7);
            }
         }
      }

      List var10 = null;
      Iterator var11 = var5.iterator();
      if (var11.hasNext()) {
         Harvester var12 = (Harvester)var11.next();
         var10 = var12.getKnownHarvestableInstances(var2, var3);
      }

      return var10;
   }

   public String[][] getKnownHarvestableTypes(String var1) throws IOException {
      return this.getKnownHarvestableTypes((String)null, var1);
   }

   public String[][] getKnownHarvestableTypes(String var1, String var2) throws IOException {
      String[][] var3 = (String[][])null;
      Map var4 = this.getKnownTypesMap(var1, var2);
      if (var4.size() > 0) {
         var3 = new String[var4.size()][2];
         int var5 = 0;

         for(Iterator var6 = var4.values().iterator(); var6.hasNext(); var3[var5++] = (String[])var6.next()) {
         }
      }

      return var3;
   }

   public String getName() {
      return this.name;
   }

   public WatchedValues.Values[] getPendingMetrics(int var1) {
      ArrayList var2 = new ArrayList();
      WatchedValuesControl var3 = (WatchedValuesControl)this.wvidToDelegatesMap.get(var1);
      if (var3 != null) {
         Iterator var4 = var3.getMapList().iterator();

         while(var4.hasNext()) {
            WatchedValuesDelegateMap var5 = (WatchedValuesDelegateMap)var4.next();
            List var6 = var5.getPendingMetrics();
            var2.addAll(var6);
         }
      }

      return (WatchedValues.Values[])var2.toArray(new WatchedValues.Values[0]);
   }

   public List<String> getUnharvestableAttributes(int var1, String var2, String var3) {
      ArrayList var4 = new ArrayList();
      WatchedValuesControl var5 = (WatchedValuesControl)this.wvidToDelegatesMap.get(var1);
      if (var5 != null) {
         Iterator var6 = var5.getMapList().iterator();

         while(var6.hasNext()) {
            WatchedValuesDelegateMap var7 = (WatchedValuesDelegateMap)var6.next();
            List var8 = var7.getUnharvestableAttributes(var2, var3);
            var4.addAll(var8);
         }
      }

      return var4;
   }

   public List<String> getDisabledAttributes(int var1, String var2, String var3) {
      HashSet var4 = new HashSet();
      WatchedValuesControl var5 = (WatchedValuesControl)this.wvidToDelegatesMap.get(var1);
      if (var5 != null) {
         Iterator var6 = var5.getMapList().iterator();

         while(var6.hasNext()) {
            WatchedValuesDelegateMap var7 = (WatchedValuesDelegateMap)var6.next();
            List var8 = var7.getDisabledAttributes(var2, var3);
            var4.addAll(var8);
         }
      }

      ArrayList var9 = new ArrayList(var4);
      return var9;
   }

   public void harvest(Map<Integer, Set<Integer>> var1) {
      Iterator var2;
      if (var1 == null) {
         var2 = this.wvidToDelegatesMap.values().iterator();

         while(var2.hasNext()) {
            WatchedValuesControl var3 = (WatchedValuesControl)var2.next();
            var3.getWatchedValues().setTimeStamp(MetricArchiver.getNanoWallClockTime());
            List var4 = var3.getMapList();
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               WatchedValuesDelegateMap var6 = (WatchedValuesDelegateMap)var5.next();
               var6.harvest((Collection)null);
            }
         }

      } else {
         var2 = var1.keySet().iterator();

         while(true) {
            Integer var9;
            WatchedValuesControl var10;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var9 = (Integer)var2.next();
               var10 = (WatchedValuesControl)this.wvidToDelegatesMap.get(var9);
            } while(var10 == null);

            var10.getWatchedValues().setTimeStamp(MetricArchiver.getNanoWallClockTime());
            Set var11 = (Set)var1.get(var9);
            List var12 = var10.getMapList();
            Iterator var7 = var12.iterator();

            while(var7.hasNext()) {
               WatchedValuesDelegateMap var8 = (WatchedValuesDelegateMap)var7.next();
               var8.harvest(var11);
            }
         }
      }
   }

   public int isTypeHandled(String var1) {
      int var2 = 0;
      ArrayList var3 = new ArrayList();
      Harvester var4 = null;
      Iterator var5 = this.harvesterManager.activatingIterator();

      while(var5.hasNext()) {
         Harvester var6 = (Harvester)var5.next();

         try {
            int var7 = var6.isTypeHandled(var1);
            switch (var7) {
               case -1:
                  if (var2 == 0) {
                     var2 = var7;
                  }
                  break;
               case 0:
               default:
                  String var8 = var6.getName();
                  LogSupport.logUnexpectedProblem("Provider " + var8 + " returned an invalid value from method isTypeHandled (" + var7 + "). Value must be " + 2 + "(yes), " + -1 + "(no), or " + 1 + "(maybe). " + var8 + " is not eligible to handle type " + var1 + ".");
                  break;
               case 1:
                  if (var4 == null) {
                     var2 = var7;
                     var3.add(var6);
                  }
                  break;
               case 2:
                  var2 = var7;
                  if (var4 != null) {
                     throw new HarvesterException.AmbiguousTypeName(var1, var6.getName(), var4.getName());
                  }

                  var3 = new ArrayList(1);
                  var3.add(var6);
                  var4 = var6;
            }
         } catch (Exception var9) {
            LogSupport.logUnexpectedException("Exception thrown in call to harvester plugIn " + var6.getName() + " for type " + var1 + ". This type will be removed from consideration by the plug-in", var9);
         }
      }

      return var2;
   }

   public void deallocate() {
      this.deactivate();
   }

   public void prepare() {
      if (DBG.isDebugEnabled()) {
         DBG.debug("In WLDFHarvesterImpl.prepare");
      }

   }

   public void activate() {
      if (DBG.isDebugEnabled()) {
         DBG.debug("In WLDFHarvesterImpl.activate()");
      }

      try {
         this.initializeHarvesters();
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   public void deactivate() {
      if (DBG.isDebugEnabled()) {
         DBG.debug("In WLDFHarvesterImpl.deactivate()");
      }

      this.harvesterManager.removeAll();
   }

   public void unprepare() {
      if (DBG.isDebugEnabled()) {
         DBG.debug("In WLDFHarvester.unprepare()");
      }

   }

   public WatchedValues createWatchedValues(String var1) {
      WatchedValuesImpl var2 = new WatchedValuesImpl(var1, this.defaultSamplePeriod);
      var2.setShared(true);
      var2.setId(-1);
      return var2;
   }

   public void deleteWatchedValues(WatchedValues var1) {
      if (var1.getId() > -1) {
         int[] var2 = new int[]{var1.getId()};
         this.deleteWatchedValues(var2);
      }

   }

   public boolean isAttributeNameTrackingEnabled() {
      return this.attributeTrackingEnabled;
   }

   public void setAttributeNameTrackingEnabled(boolean var1) {
      this.attributeTrackingEnabled = var1;
      Iterator var2 = this.harvesterManager.iterator();

      while(var2.hasNext()) {
         ((DelegateHarvesterControl)var2.next()).setAttributeNameTrackingEnabled(this.attributeTrackingEnabled);
      }

   }

   public boolean isAttributeValidationEnabled() {
      return this.attributeValidationEnabled;
   }

   public void setAttributeValidationEnabled(boolean var1) {
      this.attributeValidationEnabled = var1;
      Iterator var2 = this.harvesterManager.iterator();

      while(var2.hasNext()) {
         ((DelegateHarvesterControl)var2.next()).setAttributeValidationEnabled(this.attributeTrackingEnabled);
      }

   }

   public String getTypeForInstance(String var1) {
      String var2 = null;
      Iterator var3 = this.harvesterManager.activatingIterator();

      while(var3.hasNext()) {
         var2 = ((Harvester)var3.next()).getTypeForInstance(var1);
         if (var2 != null) {
            break;
         }
      }

      return var2;
   }

   public void validateNamespace(String var1) throws InvalidHarvesterNamespaceException {
      boolean var2 = false;
      if (var1 != null) {
         String[] var3 = this.getSupportedNamespaces();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var6.equals(var1)) {
               var2 = true;
               break;
            }
         }

         if (!var2) {
            throw new InvalidHarvesterNamespaceException(DiagnosticsTextHarvesterTextFormatter.getInstance().getInvalidHarvesterNamespaceText(var1));
         }

         if (var1.equals("DomainRuntime") && !this.adminServer) {
            throw new InvalidHarvesterNamespaceException(DiagnosticsTextHarvesterTextFormatter.getInstance().getDomainRuntimeNamespaceWarningText(var1));
         }
      }

   }

   public String[] getSupportedNamespaces() {
      HashSet var1 = new HashSet(this.harvesterManager.getConfiguredHarvestersCount());
      Iterator var2 = this.harvesterManager.iterator();

      while(var2.hasNext()) {
         DelegateHarvesterControl var3 = (DelegateHarvesterControl)var2.next();
         var1.add(var3.getNamespace());
      }

      return (String[])var1.toArray(new String[var1.size()]);
   }

   public String getNamespace() {
      return "weblogic";
   }

   public String getDefaultNamespace() {
      String var1 = "ServerRuntime";
      Iterator var2 = this.harvesterManager.iterator();

      while(var2.hasNext()) {
         DelegateHarvesterControl var3 = (DelegateHarvesterControl)var2.next();
         if (var3.isDefaultDelegate()) {
            var1 = var3.getNamespace();
         }
      }

      return var1;
   }

   public void resolveAllMetrics(int[] var1) {
      int[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var2[var4];
         this.resolveMetrics(var5, (Set)null);
      }

   }

   public void resolveMetrics(int var1, Set<Integer> var2) {
      WatchedValuesControl var3 = (WatchedValuesControl)this.wvidToDelegatesMap.get(var1);
      if (var3 == null) {
         throw new HarvesterRuntimeException(DiagnosticsTextHarvesterTextFormatter.getInstance().getWatchedValuesIdNotFoundText(var1));
      } else {
         var3.resolveMetrics(var2);
      }
   }

   public boolean removeAttributesWithProblems() {
      return true;
   }

   public void setRemoveAttributesWithProblems(boolean var1) {
   }

   private void initializeHarvesters() throws IOException, JMException {
      this.harvesterManager.addDelegateHarvester(new BeanTreeHarvesterControlImpl("BeanTreeHarvester", "ServerRuntime", DelegateHarvesterControl.ActivationPolicy.ON_METADATA_REQUEST, true));
      MBeanCategorizer.Impl var1 = new MBeanCategorizer.Impl(new MBeanCategorizer.Plugin[]{new MBeanCategorizerPlugins.NonWLSPlugin(), new ServerRuntimePlugin()});
      MBeanServer var2 = ManagementService.getRuntimeMBeanServer(KERNEL_ID);
      if (var2 != null) {
         this.addHarvesterForMBeanServer(var1, "ServerRuntimeHarvester", "ServerRuntime", var2, DelegateHarvesterControl.ActivationPolicy.ON_METADATA_REQUEST);
      } else {
         DiagnosticsHarvesterLogger.logServerRuntimeMBeanServerNotAvailable();
      }

      MBeanServer var3 = null;
      RuntimeAccess var4 = ManagementService.getRuntimeAccess(KERNEL_ID);
      this.adminServer = var4.isAdminServer();
      if (this.adminServer) {
         var3 = ManagementService.getDomainRuntimeMBeanServer(KERNEL_ID);
         if (var3 == null) {
            DiagnosticsLogger.logDomainRuntimeHarvesterUnavailable();
         } else {
            MBeanCategorizer.Impl var5 = new MBeanCategorizer.Impl(new MBeanCategorizer.Plugin[]{new ServerRuntimePlugin(), new MBeanCategorizerPlugins.NonWLSPlugin()});
            this.addHarvesterForMBeanServer(var5, "DomainRuntimeHarvester", "DomainRuntime", var3, DelegateHarvesterControl.ActivationPolicy.ON_METADATA_REQUEST);
         }
      }

   }

   private int computeNextWVID() {
      return this.nextWVID++;
   }

   private ArrayList<Integer> findVidsForHarvester(Harvester var1, Collection<ValidationMap> var2) {
      ArrayList var3 = null;
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         ValidationMap var5 = (ValidationMap)var4.next();
         if (var5.harvester == var1) {
            if (var3 == null) {
               var3 = new ArrayList();
            }

            var3.add(var5.vote.getMetric().getVID());
         }
      }

      return var3;
   }

   private Map<String, String[]> getKnownTypesMap(String var1, String var2) throws IOException {
      Pattern var3 = var1 == null ? null : Pattern.compile(var1);
      HashMap var4 = new HashMap();
      Iterator var5 = this.harvesterManager.activatingIterator(DelegateHarvesterControl.ActivationPolicy.ON_METADATA_REQUEST);

      while(true) {
         Harvester var6;
         do {
            if (!var5.hasNext()) {
               return var4;
            }

            var6 = (Harvester)var5.next();
         } while(var3 != null && !var3.matcher(var6.getNamespace()).matches());

         String[][] var7 = var6.getKnownHarvestableTypes(var2);

         for(int var8 = 0; var8 < var7.length; ++var8) {
            var4.put(var7[var8][0], var7[var8]);
         }
      }
   }

   private Collection<WatchedValues.Validation> extractValidations(Collection<ValidationMap> var1) {
      ArrayList var2 = null;
      if (var1 != null) {
         var2 = new ArrayList(var1.size());
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            ValidationMap var4 = (ValidationMap)var3.next();
            var2.add(var4.vote);
         }
      }

      return var2;
   }

   private List<WatchedValuesDelegateMap> getMapListForParentWVID(int var1) throws RuntimeException {
      WatchedValuesControl var2 = (WatchedValuesControl)this.wvidToDelegatesMap.get(var1);
      if (var2 == null) {
         throw new RuntimeException("Unable to map watched values identifier: " + var1);
      } else {
         return var2.getMapList();
      }
   }

   private void addHarvesterForMBeanServer(MBeanCategorizer.Impl var1, String var2, String var3, MBeanServer var4, DelegateHarvesterControl.ActivationPolicy var5) throws IOException, JMException {
      this.harvesterManager.addDelegateHarvester(new JMXHarvesterControlImpl(new JMXHarvesterConfig(var2, var3, var1, true, var4), var5));
   }

   private Collection<ValidationMap> mapWatchedValues(WatchedValues var1) {
      int var2 = 0;

      for(Iterator var3 = var1.getAllMetricValues().iterator(); var3.hasNext(); ++var2) {
         var3.next();
      }

      HashMap var4 = new HashMap(var2);
      if (var2 > 0) {
         int var5 = var2;
         Iterator var6 = this.harvesterManager.activatingIterator(DelegateHarvesterControl.ActivationPolicy.EXPLICIT);

         while(true) {
            while(true) {
               Harvester var7;
               Collection var8;
               do {
                  if (!var6.hasNext() || var5 <= 0) {
                     return var4.values();
                  }

                  var7 = (Harvester)var6.next();
                  var8 = var7.validateWatchedValues(var1);
               } while(var8 == null);

               if (var8.size() != var2) {
                  throw new RuntimeException("Child validation result length did number of metrics in WatchedValues!");
               }

               Iterator var9;
               WatchedValues.Validation var10;
               if (var4.size() == 0) {
                  var4 = new HashMap(var8.size());

                  for(var9 = var8.iterator(); var9.hasNext(); var4.put(var10.getMetric().getVID(), new ValidationMap(var7, var10))) {
                     var10 = (WatchedValues.Validation)var9.next();
                     if (var10.getStatus() == 2) {
                        --var5;
                     }
                  }
               } else {
                  var9 = var8.iterator();

                  while(var9.hasNext()) {
                     var10 = (WatchedValues.Validation)var9.next();
                     int var11 = var10.getMetric().getVID();
                     ValidationMap var12 = (ValidationMap)var4.get(var11);
                     WatchedValues.Validation var13 = var12.vote;
                     if (var10.getStatus() > var13.getStatus()) {
                        if (var10.getStatus() == 2) {
                           --var5;
                        }

                        if (DBG.isDebugEnabled()) {
                           DBG.debug("Harvester " + var7.getName() + " has outbid harvester " + var12.harvester.getName() + " for metric " + var12.vote.getMetric().toString());
                        }

                        var4.put(var11, new ValidationMap(var7, var10));
                     }
                  }
               }
            }
         }
      } else if (DBG.isDebugEnabled()) {
         DBG.debug("mapWatchedValues(): No values in watched values list");
      }

      return var4.values();
   }

   public void oneShotHarvest(WatchedValues var1) {
      Iterator var2 = this.harvesterManager.activeOnlyIterator();

      while(var2.hasNext()) {
         ((Harvester)var2.next()).oneShotHarvest(var1);
      }

   }

   private static class ValidationMap {
      WatchedValues.Validation vote;
      Harvester harvester;

      public ValidationMap(Harvester var1, WatchedValues.Validation var2) {
         this.harvester = var1;
         this.vote = var2;
      }
   }
}
