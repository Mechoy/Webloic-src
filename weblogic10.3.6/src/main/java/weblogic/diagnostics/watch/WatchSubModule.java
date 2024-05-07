package weblogic.diagnostics.watch;

import com.bea.adaptive.harvester.WatchedValues;
import weblogic.application.ApplicationContext;
import weblogic.descriptor.DescriptorDiff;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.descriptor.WLDFImageNotificationBean;
import weblogic.diagnostics.descriptor.WLDFJMSNotificationBean;
import weblogic.diagnostics.descriptor.WLDFJMXNotificationBean;
import weblogic.diagnostics.descriptor.WLDFNotificationBean;
import weblogic.diagnostics.descriptor.WLDFResourceBean;
import weblogic.diagnostics.descriptor.WLDFSMTPNotificationBean;
import weblogic.diagnostics.descriptor.WLDFWatchBean;
import weblogic.diagnostics.descriptor.WLDFWatchNotificationBean;
import weblogic.diagnostics.harvester.HarvesterException;
import weblogic.diagnostics.harvester.HarvesterInternalAccess;
import weblogic.diagnostics.harvester.WLDFToHarvester;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.module.WLDFModuleException;
import weblogic.diagnostics.module.WLDFSubModule;
import weblogic.logging.Severities;

public final class WatchSubModule implements WLDFSubModule {
   private static final String WATCHED_VALUES_NAME = "WLDFWatchedValues";
   private static WatchManager watchManager = null;
   private static final DebugLogger DBG = DebugLogger.getDebugLogger("DebugDiagnosticWatch");
   private WatchConfiguration currentWatchConfiguration;
   WLDFResourceBean rootBean;
   private JMXNotificationProducer jmxNotificationProducer;
   private WLDFToHarvester wldf2Hv;

   private WatchSubModule() {
      try {
         watchManager = WatchManager.getInstance();
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }

      this.rootBean = null;
      this.jmxNotificationProducer = null;
   }

   public static final WLDFSubModule createInstance() {
      return new WatchSubModule();
   }

   WLDFResourceBean getRootBean() {
      return this.rootBean;
   }

   private void identifyNotifications(WLDFWatchNotificationBean var1, WatchConfiguration var2) throws WLDFModuleException {
      this.identifyImageNotifications(var1.getImageNotifications(), var2);
      this.identifyJMSNotifications(var1.getJMSNotifications(), var2);
      this.identifyJMXNotifications(var1.getJMXNotifications(), var2);
      this.identifySMTPNotifications(var1.getSMTPNotifications(), var2);
      this.identifySNMPNotifications(var1.getSNMPNotifications(), var2);
   }

   private void identifyWatches(WLDFWatchBean[] var1, WatchConfiguration var2) throws WLDFModuleException {
      if (var1 != null && var1.length != 0) {
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var1[var4].getName();
            int var6 = WatchUtils.convertRuleTypeToInt(var1[var4].getRuleType());
            int var7 = Severities.severityStringToNum(var1[var4].getSeverity());
            int var8 = WatchUtils.convertAlarmResetTypeToInt(var1[var4].getAlarmType());
            int var9 = var1[var4].getAlarmResetPeriod();

            try {
               WLDFNotificationBean[] var10 = var1[var4].getNotifications();
               WatchNotificationListener[] var11 = null;
               boolean var12 = false;
               if (var10 != null && var10.length > 0) {
                  var11 = new WatchNotificationListener[var10.length];

                  for(int var13 = 0; var13 < var10.length; ++var13) {
                     try {
                        var11[var13] = var2.getNotification(var10[var13].getName());
                     } catch (NotificationNotFoundException var15) {
                        var12 = true;
                        DiagnosticsLogger.logInvalidNotification(var5, var10[var13].getName());
                     }
                  }
               }

               if (!var12) {
                  Watch var18 = new Watch(var5, var6, var1[var4].getRuleExpression(), var7, var8, var9, WatchUtils.getNotificationNames(var1[var4].getNotifications()), var11, var2);
                  if (var1[var4].isEnabled()) {
                     var18.setEnabled();
                  } else {
                     var18.setDisabled();
                  }

                  var2.addWatch(var18);
                  if (DBG.isDebugEnabled()) {
                     DBG.debug("Loaded watch " + var5);
                  }
               }
            } catch (Exception var16) {
               if (DBG.isDebugEnabled()) {
                  DBG.debug("Error creating watch ", var16);
               }

               throw new WLDFModuleException(DiagnosticsLogger.logCreateWatchErrorLoggable(var5, var16).getMessage());
            }
         }

         if (DBG.isDebugEnabled()) {
            DBG.debug("Loaded " + var3 + " watches ");
         }

         if (DBG.isDebugEnabled()) {
            String var17 = this.currentWatchConfiguration.getWatchedValues().dump("", false, true, true);
            DBG.debug(var17);
         }

      } else {
         if (DBG.isDebugEnabled()) {
            DBG.debug("No configured watches");
         }

      }
   }

   private void identifyImageNotifications(WLDFImageNotificationBean[] var1, WatchConfiguration var2) throws WLDFModuleException {
      if (var1 != null && var1.length != 0) {
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WLDFImageNotificationBean var5 = var1[var4];
            String var6 = var1[var4].getName();
            String var7 = var5.getImageDirectory();
            Integer var8 = new Integer(var5.getImageLockout());
            boolean var9 = var5.isEnabled();
            ImageNotificationListener var10 = null;

            try {
               if (var7 != null && var8 != null) {
                  var10 = new ImageNotificationListener(var6, var7, var8);
               } else if (var7 != null) {
                  var10 = new ImageNotificationListener(var6, var7);
               } else if (var8 != null) {
                  var10 = new ImageNotificationListener(var6, (String)null, var8);
               } else {
                  var10 = new ImageNotificationListener(var6);
               }

               if (var9) {
                  var10.setEnabled();
               } else {
                  var10.setDisabled();
               }

               var2.addNotificationListener(var10);
            } catch (Exception var12) {
               if (DBG.isDebugEnabled()) {
                  DBG.debug("Error creating Image notification ", var12);
               }

               throw new WLDFModuleException(var12);
            }
         }

      }
   }

   private void identifyJMSNotifications(WLDFJMSNotificationBean[] var1, WatchConfiguration var2) throws WLDFModuleException {
      if (var1 != null && var1.length != 0) {
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WLDFJMSNotificationBean var5 = var1[var4];
            String var6 = var5.getName();
            String var7 = var5.getDestinationJNDIName();
            String var8 = var5.getConnectionFactoryJNDIName();
            boolean var9 = var1[var4].isEnabled();
            JMSNotificationListener var10 = null;

            try {
               if (var8 != null) {
                  var10 = new JMSNotificationListener(var6, var7, var8);
               } else {
                  var10 = new JMSNotificationListener(var6, var7);
               }

               if (var9) {
                  var10.setEnabled();
               } else {
                  var10.setDisabled();
               }

               var2.addNotificationListener(var10);
            } catch (Exception var12) {
               if (DBG.isDebugEnabled()) {
                  DBG.debug("Error creating JMS Notification ", var12);
               }

               throw new WLDFModuleException(var12);
            }
         }

      }
   }

   private void identifyJMXNotifications(WLDFNotificationBean[] var1, WatchConfiguration var2) throws WLDFModuleException {
      this.jmxNotificationProducer = JMXNotificationProducer.getInstance();
      if (var1 != null && var1.length != 0) {
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WLDFJMXNotificationBean var5 = (WLDFJMXNotificationBean)var1[var4];
            String var6 = var5.getName();
            String var7 = var5.getNotificationType();
            boolean var8 = var5.isEnabled();

            try {
               JMXNotificationListener var9 = new JMXNotificationListener(var6, var7, this.jmxNotificationProducer);
               if (var8) {
                  var9.setEnabled();
               } else {
                  var9.setDisabled();
               }

               var2.addNotificationListener(var9);
            } catch (Exception var10) {
               if (DBG.isDebugEnabled()) {
                  DBG.debug("Error creating JMX Notification ", var10);
               }

               throw new WLDFModuleException(var10);
            }
         }

      }
   }

   private void identifySMTPNotifications(WLDFNotificationBean[] var1, WatchConfiguration var2) throws WLDFModuleException {
      if (var1 != null && var1.length != 0) {
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WLDFSMTPNotificationBean var5 = (WLDFSMTPNotificationBean)var1[var4];
            String var6 = var5.getName();
            String var7 = var5.getMailSessionJNDIName();
            String[] var8 = var5.getRecipients();
            String var9 = var5.getSubject();
            String var10 = var5.getBody();
            boolean var11 = var5.isEnabled();
            SMTPNotificationListener var12 = null;

            try {
               if (var9 == null && var10 == null) {
                  var12 = new SMTPNotificationListener(var6, var7, var8);
               } else {
                  var12 = new SMTPNotificationListener(var6, var7, var8, var9, var10);
               }

               if (var11) {
                  var12.setEnabled();
               } else {
                  var12.setDisabled();
               }

               var2.addNotificationListener(var12);
            } catch (Exception var14) {
               if (DBG.isDebugEnabled()) {
                  DBG.debug("Error creating SMTP Notification ", var14);
               }

               throw new WLDFModuleException(var14);
            }
         }

      }
   }

   private void identifySNMPNotifications(WLDFNotificationBean[] var1, WatchConfiguration var2) throws WLDFModuleException {
      if (var1 != null && var1.length != 0) {
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var1[var4].getName();
            boolean var6 = var1[var4].isEnabled();
            SNMPNotificationListener var7 = null;

            try {
               var7 = new SNMPNotificationListener(var5);
               if (var6) {
                  var7.setEnabled();
               } else {
                  var7.setDisabled();
               }

               var2.addNotificationListener(var7);
            } catch (Exception var9) {
               if (DBG.isDebugEnabled()) {
                  DBG.debug("Error creating SNMP Notification ", var9);
               }

               throw new WLDFModuleException(var9);
            }
         }

      }
   }

   private void initializeFromConfiguration(WLDFResourceBean var1) throws WLDFModuleException {
      this.wldf2Hv = HarvesterInternalAccess.getInstance();
      if (DBG.isDebugEnabled()) {
         DBG.debug("Creating new WatchNotification configuration object for " + var1.getWatchNotification());
      }

      if (this.currentWatchConfiguration != null) {
         DBG.debug("Deleting current watched values set for W&N");

         try {
            this.deleteWatchedValues();
         } catch (HarvesterException var4) {
            throw new WLDFModuleException(var4);
         }
      }

      WatchedValues var2 = this.wldf2Hv.createWatchedValues("WLDFWatchedValues");
      this.currentWatchConfiguration = new WatchConfiguration(var2);
      this.currentWatchConfiguration.initializeLogEventHandlerSeverity(var1.getWatchNotification().getLogWatchSeverity());
      this.currentWatchConfiguration.setWatchNotificationEnabled(var1.getWatchNotification().isEnabled());
      this.identifyNotifications(var1.getWatchNotification(), this.currentWatchConfiguration);
      this.identifyWatches(var1.getWatchNotification().getWatches(), this.currentWatchConfiguration);
      if (DBG.isDebugEnabled()) {
         boolean var3 = this.currentWatchConfiguration.isWatchNotificationEnabled();
         DBG.debug("Current WatchNotification configuration is " + (var3 ? "enabled" : "disabled"));
      }

   }

   private void deleteWatchedValues() throws HarvesterException {
      if (this.currentWatchConfiguration != null) {
         WatchedValues var1 = this.currentWatchConfiguration.getWatchedValues();
         if (var1.getId() > -1) {
            this.wldf2Hv.deleteWatchedValues(var1);
         } else if (DBG.isDebugEnabled()) {
            DBG.debug("WatchSubModule.deleteWatchedValues(): Watched values ID invalid, does not appear to have been registered");
         }
      }

   }

   public void init(ApplicationContext var1, WLDFResourceBean var2) {
      this.rootBean = var2;
   }

   public void prepare() {
      if (DBG.isDebugEnabled()) {
         DBG.debug("In WatchSubModule.prepare");
      }

   }

   public void activate() throws WLDFModuleException {
      if (DBG.isDebugEnabled()) {
         DBG.debug("In WatchSubModule.activate");
      }

      this.initializeFromConfiguration(this.rootBean);
      watchManager.moduleActivated(this.currentWatchConfiguration);
   }

   public void deactivate() {
      if (DBG.isDebugEnabled()) {
         DBG.debug("In WatchSubModule.deactivate");
      }

      try {
         this.deleteWatchedValues();
      } catch (HarvesterException var2) {
         if (DBG.isDebugEnabled()) {
            DBG.debug("Caught unexpected exception deleting watched values", var2);
         }
      }

      watchManager.moduleDeactivated();
   }

   public void unprepare() {
      if (DBG.isDebugEnabled()) {
         DBG.debug("In WatchSubModule.unprepare");
      }

   }

   public void destroy() {
      if (DBG.isDebugEnabled()) {
         DBG.debug("In WatchSubModule.destroy");
      }

   }

   public void prepareUpdate(WLDFResourceBean var1, DescriptorDiff var2) throws WLDFModuleException {
      if (DBG.isDebugEnabled()) {
         DBG.debug("In WatchSubModule.prepareUpdate");
      }

      this.initializeFromConfiguration(var1);
   }

   public void activateUpdate(WLDFResourceBean var1, DescriptorDiff var2) throws WLDFModuleException {
      if (DBG.isDebugEnabled()) {
         DBG.debug("In WatchSubModule.activateUpdate");
      }

      watchManager.moduleActivated(this.currentWatchConfiguration);
   }

   public void rollbackUpdate(WLDFResourceBean var1, DescriptorDiff var2) {
      if (DBG.isDebugEnabled()) {
         DBG.debug("In WatchSubModule.rollbackUpdate");
      }

   }
}
