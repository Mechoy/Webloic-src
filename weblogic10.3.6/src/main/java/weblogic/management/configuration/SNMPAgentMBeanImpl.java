package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.SNMPAgent;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class SNMPAgentMBeanImpl extends ConfigurationMBeanImpl implements SNMPAgentMBean, Serializable {
   private String _AuthenticationProtocol;
   private boolean _CommunityBasedAccessEnabled;
   private String _CommunityPrefix;
   private int _DebugLevel;
   private boolean _Enabled;
   private boolean _InformEnabled;
   private int _InformRetryInterval;
   private long _LocalizedKeyCacheInvalidationInterval;
   private int _MasterAgentXPort;
   private int _MaxInformRetryCount;
   private int _MibDataRefreshInterval;
   private String _Name;
   private String _PrivacyProtocol;
   private boolean _SNMPAccessForUserMBeansEnabled;
   private SNMPAttributeChangeMBean[] _SNMPAttributeChanges;
   private SNMPCounterMonitorMBean[] _SNMPCounterMonitors;
   private String _SNMPEngineId;
   private SNMPGaugeMonitorMBean[] _SNMPGaugeMonitors;
   private SNMPLogFilterMBean[] _SNMPLogFilters;
   private int _SNMPPort;
   private SNMPProxyMBean[] _SNMPProxies;
   private SNMPStringMonitorMBean[] _SNMPStringMonitors;
   private SNMPTrapDestinationMBean[] _SNMPTrapDestinations;
   private int _SNMPTrapVersion;
   private boolean _SendAutomaticTrapsEnabled;
   private int _ServerStatusCheckIntervalFactor;
   private SNMPTrapDestinationMBean[] _TargetedTrapDestinations;
   private String _UserDefinedMib;
   private SNMPAgent _customizer;
   private static SchemaHelper2 _schemaHelper;

   public SNMPAgentMBeanImpl() {
      try {
         this._customizer = new SNMPAgent(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public SNMPAgentMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new SNMPAgent(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getName() {
      if (!this._isSet(2)) {
         try {
            return ((ConfigurationMBean)this.getParent()).getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getName();
   }

   public boolean isEnabled() {
      return this._Enabled;
   }

   public boolean isEnabledSet() {
      return this._isSet(7);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setEnabled(boolean var1) {
      boolean var2 = this._Enabled;
      this._Enabled = var1;
      this._postSet(7, var2, var1);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public boolean isSendAutomaticTrapsEnabled() {
      return this._SendAutomaticTrapsEnabled;
   }

   public boolean isSendAutomaticTrapsEnabledSet() {
      return this._isSet(8);
   }

   public void setSendAutomaticTrapsEnabled(boolean var1) {
      boolean var2 = this._SendAutomaticTrapsEnabled;
      this._SendAutomaticTrapsEnabled = var1;
      this._postSet(8, var2, var1);
   }

   public int getSNMPPort() {
      return this._SNMPPort;
   }

   public boolean isSNMPPortSet() {
      return this._isSet(9);
   }

   public void setSNMPPort(int var1) throws InvalidAttributeValueException, ConfigurationException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("SNMPPort", (long)var1, 1L, 65535L);
      int var2 = this._SNMPPort;
      this._SNMPPort = var1;
      this._postSet(9, var2, var1);
   }

   public int getSNMPTrapVersion() {
      return this._SNMPTrapVersion;
   }

   public boolean isSNMPTrapVersionSet() {
      return this._isSet(10);
   }

   public void setSNMPTrapVersion(int var1) {
      int[] var2 = new int[]{1, 2, 3};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("SNMPTrapVersion", var1, var2);
      int var3 = this._SNMPTrapVersion;
      this._SNMPTrapVersion = var1;
      this._postSet(10, var3, var1);
   }

   public int getMibDataRefreshInterval() {
      return this._MibDataRefreshInterval;
   }

   public boolean isMibDataRefreshIntervalSet() {
      return this._isSet(11);
   }

   public void setMibDataRefreshInterval(int var1) throws InvalidAttributeValueException, ConfigurationException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MibDataRefreshInterval", (long)var1, 30L, 65535L);
      int var2 = this._MibDataRefreshInterval;
      this._MibDataRefreshInterval = var1;
      this._postSet(11, var2, var1);
   }

   public int getServerStatusCheckIntervalFactor() {
      return this._ServerStatusCheckIntervalFactor;
   }

   public boolean isServerStatusCheckIntervalFactorSet() {
      return this._isSet(12);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setServerStatusCheckIntervalFactor(int var1) throws InvalidAttributeValueException, ConfigurationException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ServerStatusCheckIntervalFactor", (long)var1, 1L, 65535L);
      int var2 = this._ServerStatusCheckIntervalFactor;
      this._ServerStatusCheckIntervalFactor = var1;
      this._postSet(12, var2, var1);
   }

   public String getCommunityPrefix() {
      return this._CommunityPrefix;
   }

   public boolean isCommunityPrefixSet() {
      return this._isSet(13);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setCommunityPrefix(String var1) throws InvalidAttributeValueException, ConfigurationException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("CommunityPrefix", var1);
      String var2 = this._CommunityPrefix;
      this._CommunityPrefix = var1;
      this._postSet(13, var2, var1);
   }

   public String getUserDefinedMib() {
      return this._UserDefinedMib;
   }

   public boolean isUserDefinedMibSet() {
      return this._isSet(14);
   }

   public void setUserDefinedMib(String var1) {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("UserDefinedMib", var1);
      String var2 = this._UserDefinedMib;
      this._UserDefinedMib = var1;
      this._postSet(14, var2, var1);
   }

   public int getDebugLevel() {
      return this._DebugLevel;
   }

   public boolean isDebugLevelSet() {
      return this._isSet(15);
   }

   public void setDebugLevel(int var1) throws InvalidAttributeValueException, ConfigurationException {
      int[] var2 = new int[]{0, 1, 2, 3};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DebugLevel", var1, var2);
      int var3 = this._DebugLevel;
      this._DebugLevel = var1;
      this._postSet(15, var3, var1);
   }

   public SNMPTrapDestinationMBean[] getTargetedTrapDestinations() {
      return this._TargetedTrapDestinations;
   }

   public String getTargetedTrapDestinationsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargetedTrapDestinations());
   }

   public boolean isTargetedTrapDestinationsSet() {
      return this._isSet(16);
   }

   public void setTargetedTrapDestinationsAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._TargetedTrapDestinations);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, SNMPTrapDestinationMBean.class, new ReferenceManager.Resolver(this, 16) {
                  public void resolveReference(Object var1) {
                     try {
                        SNMPAgentMBeanImpl.this.addTargetedTrapDestination((SNMPTrapDestinationMBean)var1);
                     } catch (RuntimeException var3) {
                        throw var3;
                     } catch (Exception var4) {
                        throw new AssertionError("Impossible exception: " + var4);
                     }
                  }
               });
            }
         }

         Iterator var14 = var3.iterator();

         while(true) {
            while(var14.hasNext()) {
               var5 = (String)var14.next();
               SNMPTrapDestinationMBean[] var6 = this._TargetedTrapDestinations;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  SNMPTrapDestinationMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeTargetedTrapDestination(var9);
                        break;
                     } catch (RuntimeException var11) {
                        throw var11;
                     } catch (Exception var12) {
                        throw new AssertionError("Impossible exception: " + var12);
                     }
                  }
               }
            }

            return;
         }
      } else {
         SNMPTrapDestinationMBean[] var2 = this._TargetedTrapDestinations;
         this._initializeProperty(16);
         this._postSet(16, var2, this._TargetedTrapDestinations);
      }
   }

   public void setTargetedTrapDestinations(SNMPTrapDestinationMBean[] var1) throws InvalidAttributeValueException, ConfigurationException {
      Object var3 = var1 == null ? new SNMPTrapDestinationMBeanImpl[0] : var1;
      SNMPTrapDestinationMBean[] var2 = this._TargetedTrapDestinations;
      this._TargetedTrapDestinations = (SNMPTrapDestinationMBean[])var3;
      this._postSet(16, var2, var3);
   }

   public boolean addTargetedTrapDestination(SNMPTrapDestinationMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 16)) {
         SNMPTrapDestinationMBean[] var2;
         if (this._isSet(16)) {
            var2 = (SNMPTrapDestinationMBean[])((SNMPTrapDestinationMBean[])this._getHelper()._extendArray(this.getTargetedTrapDestinations(), SNMPTrapDestinationMBean.class, var1));
         } else {
            var2 = new SNMPTrapDestinationMBean[]{var1};
         }

         try {
            this.setTargetedTrapDestinations(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof ConfigurationException) {
               throw (ConfigurationException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeTargetedTrapDestination(SNMPTrapDestinationMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      SNMPTrapDestinationMBean[] var2 = this.getTargetedTrapDestinations();
      SNMPTrapDestinationMBean[] var3 = (SNMPTrapDestinationMBean[])((SNMPTrapDestinationMBean[])this._getHelper()._removeElement(var2, SNMPTrapDestinationMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setTargetedTrapDestinations(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
            } else if (var5 instanceof ConfigurationException) {
               throw (ConfigurationException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public SNMPTrapDestinationMBean createSNMPTrapDestination(String var1) {
      SNMPTrapDestinationMBeanImpl var2 = new SNMPTrapDestinationMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPTrapDestination(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public SNMPTrapDestinationMBean createSNMPTrapDestination(String var1, SNMPTrapDestinationMBean var2) {
      return this._customizer.createSNMPTrapDestination(var1, var2);
   }

   public void destroySNMPTrapDestination(SNMPTrapDestinationMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 17);
         SNMPTrapDestinationMBean[] var2 = this.getSNMPTrapDestinations();
         SNMPTrapDestinationMBean[] var3 = (SNMPTrapDestinationMBean[])((SNMPTrapDestinationMBean[])this._getHelper()._removeElement(var2, SNMPTrapDestinationMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setSNMPTrapDestinations(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public SNMPTrapDestinationMBean lookupSNMPTrapDestination(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SNMPTrapDestinations).iterator();

      SNMPTrapDestinationMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (SNMPTrapDestinationMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public SNMPTrapDestinationMBean[] getSNMPTrapDestinations() {
      return this._SNMPTrapDestinations;
   }

   public boolean isSNMPTrapDestinationsSet() {
      return this._isSet(17);
   }

   public void removeSNMPTrapDestination(SNMPTrapDestinationMBean var1) {
      this.destroySNMPTrapDestination(var1);
   }

   public void setSNMPTrapDestinations(SNMPTrapDestinationMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SNMPTrapDestinationMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 17)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      SNMPTrapDestinationMBean[] var5 = this._SNMPTrapDestinations;
      this._SNMPTrapDestinations = (SNMPTrapDestinationMBean[])var4;
      this._postSet(17, var5, var4);
   }

   public boolean addSNMPTrapDestination(SNMPTrapDestinationMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 17)) {
         SNMPTrapDestinationMBean[] var2;
         if (this._isSet(17)) {
            var2 = (SNMPTrapDestinationMBean[])((SNMPTrapDestinationMBean[])this._getHelper()._extendArray(this.getSNMPTrapDestinations(), SNMPTrapDestinationMBean.class, var1));
         } else {
            var2 = new SNMPTrapDestinationMBean[]{var1};
         }

         try {
            this.setSNMPTrapDestinations(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof ConfigurationException) {
               throw (ConfigurationException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public SNMPProxyMBean[] getSNMPProxies() {
      return this._SNMPProxies;
   }

   public boolean isSNMPProxiesSet() {
      return this._isSet(18);
   }

   public void setSNMPProxies(SNMPProxyMBean[] var1) throws InvalidAttributeValueException, ConfigurationException {
      Object var4 = var1 == null ? new SNMPProxyMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 18)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SNMPProxyMBean[] var5 = this._SNMPProxies;
      this._SNMPProxies = (SNMPProxyMBean[])var4;
      this._postSet(18, var5, var4);
   }

   public boolean addSNMPProxy(SNMPProxyMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 18)) {
         SNMPProxyMBean[] var2;
         if (this._isSet(18)) {
            var2 = (SNMPProxyMBean[])((SNMPProxyMBean[])this._getHelper()._extendArray(this.getSNMPProxies(), SNMPProxyMBean.class, var1));
         } else {
            var2 = new SNMPProxyMBean[]{var1};
         }

         try {
            this.setSNMPProxies(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof ConfigurationException) {
               throw (ConfigurationException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeSNMPProxy(SNMPProxyMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this.destroySNMPProxy(var1);
      return true;
   }

   public SNMPProxyMBean createSNMPProxy(String var1) {
      SNMPProxyMBeanImpl var2 = new SNMPProxyMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPProxy(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public SNMPProxyMBean createSNMPProxy(String var1, SNMPProxyMBean var2) {
      return this._customizer.createSNMPProxy(var1, var2);
   }

   public void destroySNMPProxy(SNMPProxyMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 18);
         SNMPProxyMBean[] var2 = this.getSNMPProxies();
         SNMPProxyMBean[] var3 = (SNMPProxyMBean[])((SNMPProxyMBean[])this._getHelper()._removeElement(var2, SNMPProxyMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setSNMPProxies(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public SNMPProxyMBean lookupSNMPProxy(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SNMPProxies).iterator();

      SNMPProxyMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (SNMPProxyMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public SNMPGaugeMonitorMBean[] getSNMPGaugeMonitors() {
      return this._SNMPGaugeMonitors;
   }

   public boolean isSNMPGaugeMonitorsSet() {
      return this._isSet(19);
   }

   public void setSNMPGaugeMonitors(SNMPGaugeMonitorMBean[] var1) throws InvalidAttributeValueException, ConfigurationException {
      Object var4 = var1 == null ? new SNMPGaugeMonitorMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 19)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SNMPGaugeMonitorMBean[] var5 = this._SNMPGaugeMonitors;
      this._SNMPGaugeMonitors = (SNMPGaugeMonitorMBean[])var4;
      this._postSet(19, var5, var4);
   }

   public boolean addSNMPGaugeMonitor(SNMPGaugeMonitorMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 19)) {
         SNMPGaugeMonitorMBean[] var2;
         if (this._isSet(19)) {
            var2 = (SNMPGaugeMonitorMBean[])((SNMPGaugeMonitorMBean[])this._getHelper()._extendArray(this.getSNMPGaugeMonitors(), SNMPGaugeMonitorMBean.class, var1));
         } else {
            var2 = new SNMPGaugeMonitorMBean[]{var1};
         }

         try {
            this.setSNMPGaugeMonitors(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof ConfigurationException) {
               throw (ConfigurationException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeSNMPGaugeMonitor(SNMPGaugeMonitorMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this.destroySNMPGaugeMonitor(var1);
      return true;
   }

   public SNMPGaugeMonitorMBean createSNMPGaugeMonitor(String var1) {
      SNMPGaugeMonitorMBeanImpl var2 = new SNMPGaugeMonitorMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPGaugeMonitor(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public SNMPGaugeMonitorMBean createSNMPGaugeMonitor(String var1, SNMPGaugeMonitorMBean var2) {
      return this._customizer.createSNMPGaugeMonitor(var1, var2);
   }

   public void destroySNMPGaugeMonitor(SNMPGaugeMonitorMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 19);
         SNMPGaugeMonitorMBean[] var2 = this.getSNMPGaugeMonitors();
         SNMPGaugeMonitorMBean[] var3 = (SNMPGaugeMonitorMBean[])((SNMPGaugeMonitorMBean[])this._getHelper()._removeElement(var2, SNMPGaugeMonitorMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setSNMPGaugeMonitors(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public SNMPGaugeMonitorMBean lookupSNMPGaugeMonitor(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SNMPGaugeMonitors).iterator();

      SNMPGaugeMonitorMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (SNMPGaugeMonitorMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public SNMPStringMonitorMBean[] getSNMPStringMonitors() {
      return this._SNMPStringMonitors;
   }

   public boolean isSNMPStringMonitorsSet() {
      return this._isSet(20);
   }

   public void setSNMPStringMonitors(SNMPStringMonitorMBean[] var1) throws InvalidAttributeValueException, ConfigurationException {
      Object var4 = var1 == null ? new SNMPStringMonitorMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 20)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SNMPStringMonitorMBean[] var5 = this._SNMPStringMonitors;
      this._SNMPStringMonitors = (SNMPStringMonitorMBean[])var4;
      this._postSet(20, var5, var4);
   }

   public boolean addSNMPStringMonitor(SNMPStringMonitorMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 20)) {
         SNMPStringMonitorMBean[] var2;
         if (this._isSet(20)) {
            var2 = (SNMPStringMonitorMBean[])((SNMPStringMonitorMBean[])this._getHelper()._extendArray(this.getSNMPStringMonitors(), SNMPStringMonitorMBean.class, var1));
         } else {
            var2 = new SNMPStringMonitorMBean[]{var1};
         }

         try {
            this.setSNMPStringMonitors(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof ConfigurationException) {
               throw (ConfigurationException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeSNMPStringMonitor(SNMPStringMonitorMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this.destroySNMPStringMonitor(var1);
      return true;
   }

   public SNMPStringMonitorMBean createSNMPStringMonitor(String var1) {
      SNMPStringMonitorMBeanImpl var2 = new SNMPStringMonitorMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPStringMonitor(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public SNMPStringMonitorMBean createSNMPStringMonitor(String var1, SNMPStringMonitorMBean var2) {
      return this._customizer.createSNMPStringMonitor(var1, var2);
   }

   public void destroySNMPStringMonitor(SNMPStringMonitorMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 20);
         SNMPStringMonitorMBean[] var2 = this.getSNMPStringMonitors();
         SNMPStringMonitorMBean[] var3 = (SNMPStringMonitorMBean[])((SNMPStringMonitorMBean[])this._getHelper()._removeElement(var2, SNMPStringMonitorMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setSNMPStringMonitors(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public SNMPStringMonitorMBean lookupSNMPStringMonitor(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SNMPStringMonitors).iterator();

      SNMPStringMonitorMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (SNMPStringMonitorMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public SNMPCounterMonitorMBean[] getSNMPCounterMonitors() {
      return this._SNMPCounterMonitors;
   }

   public boolean isSNMPCounterMonitorsSet() {
      return this._isSet(21);
   }

   public void setSNMPCounterMonitors(SNMPCounterMonitorMBean[] var1) throws InvalidAttributeValueException, ConfigurationException {
      Object var4 = var1 == null ? new SNMPCounterMonitorMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 21)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SNMPCounterMonitorMBean[] var5 = this._SNMPCounterMonitors;
      this._SNMPCounterMonitors = (SNMPCounterMonitorMBean[])var4;
      this._postSet(21, var5, var4);
   }

   public boolean addSNMPCounterMonitor(SNMPCounterMonitorMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 21)) {
         SNMPCounterMonitorMBean[] var2;
         if (this._isSet(21)) {
            var2 = (SNMPCounterMonitorMBean[])((SNMPCounterMonitorMBean[])this._getHelper()._extendArray(this.getSNMPCounterMonitors(), SNMPCounterMonitorMBean.class, var1));
         } else {
            var2 = new SNMPCounterMonitorMBean[]{var1};
         }

         try {
            this.setSNMPCounterMonitors(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof ConfigurationException) {
               throw (ConfigurationException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeSNMPCounterMonitor(SNMPCounterMonitorMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this.destroySNMPCounterMonitor(var1);
      return true;
   }

   public SNMPCounterMonitorMBean createSNMPCounterMonitor(String var1) {
      SNMPCounterMonitorMBeanImpl var2 = new SNMPCounterMonitorMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPCounterMonitor(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public SNMPCounterMonitorMBean createSNMPCounterMonitor(String var1, SNMPCounterMonitorMBean var2) {
      return this._customizer.createSNMPCounterMonitor(var1, var2);
   }

   public void destroySNMPCounterMonitor(SNMPCounterMonitorMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 21);
         SNMPCounterMonitorMBean[] var2 = this.getSNMPCounterMonitors();
         SNMPCounterMonitorMBean[] var3 = (SNMPCounterMonitorMBean[])((SNMPCounterMonitorMBean[])this._getHelper()._removeElement(var2, SNMPCounterMonitorMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setSNMPCounterMonitors(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public SNMPCounterMonitorMBean lookupSNMPCounterMonitor(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SNMPCounterMonitors).iterator();

      SNMPCounterMonitorMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (SNMPCounterMonitorMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public SNMPLogFilterMBean[] getSNMPLogFilters() {
      return this._SNMPLogFilters;
   }

   public boolean isSNMPLogFiltersSet() {
      return this._isSet(22);
   }

   public void setSNMPLogFilters(SNMPLogFilterMBean[] var1) throws InvalidAttributeValueException, ConfigurationException {
      Object var4 = var1 == null ? new SNMPLogFilterMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 22)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SNMPLogFilterMBean[] var5 = this._SNMPLogFilters;
      this._SNMPLogFilters = (SNMPLogFilterMBean[])var4;
      this._postSet(22, var5, var4);
   }

   public boolean addSNMPLogFilter(SNMPLogFilterMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 22)) {
         SNMPLogFilterMBean[] var2;
         if (this._isSet(22)) {
            var2 = (SNMPLogFilterMBean[])((SNMPLogFilterMBean[])this._getHelper()._extendArray(this.getSNMPLogFilters(), SNMPLogFilterMBean.class, var1));
         } else {
            var2 = new SNMPLogFilterMBean[]{var1};
         }

         try {
            this.setSNMPLogFilters(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof ConfigurationException) {
               throw (ConfigurationException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeSNMPLogFilter(SNMPLogFilterMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this.destroySNMPLogFilter(var1);
      return true;
   }

   public SNMPLogFilterMBean createSNMPLogFilter(String var1) {
      SNMPLogFilterMBeanImpl var2 = new SNMPLogFilterMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPLogFilter(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public SNMPLogFilterMBean createSNMPLogFilter(String var1, SNMPLogFilterMBean var2) {
      return this._customizer.createSNMPLogFilter(var1, var2);
   }

   public void destroySNMPLogFilter(SNMPLogFilterMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 22);
         SNMPLogFilterMBean[] var2 = this.getSNMPLogFilters();
         SNMPLogFilterMBean[] var3 = (SNMPLogFilterMBean[])((SNMPLogFilterMBean[])this._getHelper()._removeElement(var2, SNMPLogFilterMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setSNMPLogFilters(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public SNMPLogFilterMBean lookupSNMPLogFilter(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SNMPLogFilters).iterator();

      SNMPLogFilterMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (SNMPLogFilterMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public SNMPAttributeChangeMBean[] getSNMPAttributeChanges() {
      return this._SNMPAttributeChanges;
   }

   public boolean isSNMPAttributeChangesSet() {
      return this._isSet(23);
   }

   public void setSNMPAttributeChanges(SNMPAttributeChangeMBean[] var1) throws InvalidAttributeValueException, ConfigurationException {
      Object var4 = var1 == null ? new SNMPAttributeChangeMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 23)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SNMPAttributeChangeMBean[] var5 = this._SNMPAttributeChanges;
      this._SNMPAttributeChanges = (SNMPAttributeChangeMBean[])var4;
      this._postSet(23, var5, var4);
   }

   public boolean addSNMPAttributeChange(SNMPAttributeChangeMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 23)) {
         SNMPAttributeChangeMBean[] var2;
         if (this._isSet(23)) {
            var2 = (SNMPAttributeChangeMBean[])((SNMPAttributeChangeMBean[])this._getHelper()._extendArray(this.getSNMPAttributeChanges(), SNMPAttributeChangeMBean.class, var1));
         } else {
            var2 = new SNMPAttributeChangeMBean[]{var1};
         }

         try {
            this.setSNMPAttributeChanges(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof ConfigurationException) {
               throw (ConfigurationException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeSNMPAttributeChange(SNMPAttributeChangeMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this.destroySNMPAttributeChange(var1);
      return true;
   }

   public SNMPAttributeChangeMBean createSNMPAttributeChange(String var1) {
      SNMPAttributeChangeMBeanImpl var2 = new SNMPAttributeChangeMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPAttributeChange(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public SNMPAttributeChangeMBean createSNMPAttributeChange(String var1, SNMPAttributeChangeMBean var2) {
      return this._customizer.createSNMPAttributeChange(var1, var2);
   }

   public void destroySNMPAttributeChange(SNMPAttributeChangeMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 23);
         SNMPAttributeChangeMBean[] var2 = this.getSNMPAttributeChanges();
         SNMPAttributeChangeMBean[] var3 = (SNMPAttributeChangeMBean[])((SNMPAttributeChangeMBean[])this._getHelper()._removeElement(var2, SNMPAttributeChangeMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setSNMPAttributeChanges(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public SNMPAttributeChangeMBean lookupSNMPAttributeChange(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SNMPAttributeChanges).iterator();

      SNMPAttributeChangeMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (SNMPAttributeChangeMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public boolean isCommunityBasedAccessEnabled() {
      return this._CommunityBasedAccessEnabled;
   }

   public boolean isCommunityBasedAccessEnabledSet() {
      return this._isSet(24);
   }

   public void setCommunityBasedAccessEnabled(boolean var1) {
      boolean var2 = this._CommunityBasedAccessEnabled;
      this._CommunityBasedAccessEnabled = var1;
      this._postSet(24, var2, var1);
   }

   public String getSNMPEngineId() {
      if (!this._isSet(25)) {
         try {
            return this.getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._SNMPEngineId;
   }

   public boolean isSNMPEngineIdSet() {
      return this._isSet(25);
   }

   public void setSNMPEngineId(String var1) {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("SNMPEngineId", var1);
      String var2 = this._SNMPEngineId;
      this._SNMPEngineId = var1;
      this._postSet(25, var2, var1);
   }

   public String getAuthenticationProtocol() {
      if (!this._isSet(26)) {
         return this._isProductionModeEnabled() ? "MD5" : "noAuth";
      } else {
         return this._AuthenticationProtocol;
      }
   }

   public boolean isAuthenticationProtocolSet() {
      return this._isSet(26);
   }

   public void setAuthenticationProtocol(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"noAuth", "MD5", "SHA"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("AuthenticationProtocol", var1, var2);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("AuthenticationProtocol", var1);
      String var3 = this._AuthenticationProtocol;
      this._AuthenticationProtocol = var1;
      this._postSet(26, var3, var1);
   }

   public String getPrivacyProtocol() {
      return this._PrivacyProtocol;
   }

   public boolean isPrivacyProtocolSet() {
      return this._isSet(27);
   }

   public void setPrivacyProtocol(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"noPriv", "DES", "AES_128"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("PrivacyProtocol", var1, var2);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("PrivacyProtocol", var1);
      String var3 = this._PrivacyProtocol;
      this._PrivacyProtocol = var1;
      this._postSet(27, var3, var1);
   }

   public int getInformRetryInterval() {
      return this._InformRetryInterval;
   }

   public boolean isInformRetryIntervalSet() {
      return this._isSet(28);
   }

   public void setInformRetryInterval(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("InformRetryInterval", (long)var1, 3000L, 30000L);
      int var2 = this._InformRetryInterval;
      this._InformRetryInterval = var1;
      this._postSet(28, var2, var1);
   }

   public int getMaxInformRetryCount() {
      return this._MaxInformRetryCount;
   }

   public boolean isMaxInformRetryCountSet() {
      return this._isSet(29);
   }

   public void setMaxInformRetryCount(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxInformRetryCount", (long)var1, 1L, 3L);
      int var2 = this._MaxInformRetryCount;
      this._MaxInformRetryCount = var1;
      this._postSet(29, var2, var1);
   }

   public long getLocalizedKeyCacheInvalidationInterval() {
      return this._LocalizedKeyCacheInvalidationInterval;
   }

   public boolean isLocalizedKeyCacheInvalidationIntervalSet() {
      return this._isSet(30);
   }

   public void setLocalizedKeyCacheInvalidationInterval(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMax("LocalizedKeyCacheInvalidationInterval", var1, 86400000L);
      long var3 = this._LocalizedKeyCacheInvalidationInterval;
      this._LocalizedKeyCacheInvalidationInterval = var1;
      this._postSet(30, var3, var1);
   }

   public boolean isSNMPAccessForUserMBeansEnabled() {
      return this._SNMPAccessForUserMBeansEnabled;
   }

   public boolean isSNMPAccessForUserMBeansEnabledSet() {
      return this._isSet(31);
   }

   public void setSNMPAccessForUserMBeansEnabled(boolean var1) {
      boolean var2 = this._SNMPAccessForUserMBeansEnabled;
      this._SNMPAccessForUserMBeansEnabled = var1;
      this._postSet(31, var2, var1);
   }

   public boolean isInformEnabled() {
      return this._InformEnabled;
   }

   public boolean isInformEnabledSet() {
      return this._isSet(32);
   }

   public void setInformEnabled(boolean var1) {
      boolean var2 = this._InformEnabled;
      this._InformEnabled = var1;
      this._postSet(32, var2, var1);
   }

   public int getMasterAgentXPort() {
      return this._MasterAgentXPort;
   }

   public boolean isMasterAgentXPortSet() {
      return this._isSet(33);
   }

   public void setMasterAgentXPort(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MasterAgentXPort", (long)var1, 1L, 65535L);
      int var2 = this._MasterAgentXPort;
      this._MasterAgentXPort = var1;
      this._postSet(33, var2, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      SNMPValidator.validateSNMPAgent(this);
   }

   public boolean _hasKey() {
      return true;
   }

   public boolean _isPropertyAKey(Munger.ReaderEventInfo var1) {
      String var2 = var1.getElementName();
      switch (var2.length()) {
         case 4:
            if (var2.equals("name")) {
               return var1.compareXpaths(this._getPropertyXpath("name"));
            }

            return super._isPropertyAKey(var1);
         default:
            return super._isPropertyAKey(var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 26;
      }

      try {
         switch (var1) {
            case 26:
               this._AuthenticationProtocol = "noAuth";
               if (var2) {
                  break;
               }
            case 13:
               this._CommunityPrefix = "public";
               if (var2) {
                  break;
               }
            case 15:
               this._DebugLevel = 0;
               if (var2) {
                  break;
               }
            case 28:
               this._InformRetryInterval = 10000;
               if (var2) {
                  break;
               }
            case 30:
               this._LocalizedKeyCacheInvalidationInterval = 3600000L;
               if (var2) {
                  break;
               }
            case 33:
               this._MasterAgentXPort = 705;
               if (var2) {
                  break;
               }
            case 29:
               this._MaxInformRetryCount = 1;
               if (var2) {
                  break;
               }
            case 11:
               this._MibDataRefreshInterval = 120;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 27:
               this._PrivacyProtocol = "noPriv";
               if (var2) {
                  break;
               }
            case 23:
               this._SNMPAttributeChanges = new SNMPAttributeChangeMBean[0];
               if (var2) {
                  break;
               }
            case 21:
               this._SNMPCounterMonitors = new SNMPCounterMonitorMBean[0];
               if (var2) {
                  break;
               }
            case 25:
               this._SNMPEngineId = null;
               if (var2) {
                  break;
               }
            case 19:
               this._SNMPGaugeMonitors = new SNMPGaugeMonitorMBean[0];
               if (var2) {
                  break;
               }
            case 22:
               this._SNMPLogFilters = new SNMPLogFilterMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._SNMPPort = 161;
               if (var2) {
                  break;
               }
            case 18:
               this._SNMPProxies = new SNMPProxyMBean[0];
               if (var2) {
                  break;
               }
            case 20:
               this._SNMPStringMonitors = new SNMPStringMonitorMBean[0];
               if (var2) {
                  break;
               }
            case 17:
               this._SNMPTrapDestinations = new SNMPTrapDestinationMBean[0];
               if (var2) {
                  break;
               }
            case 10:
               this._SNMPTrapVersion = 1;
               if (var2) {
                  break;
               }
            case 12:
               this._ServerStatusCheckIntervalFactor = 1;
               if (var2) {
                  break;
               }
            case 16:
               this._TargetedTrapDestinations = new SNMPTrapDestinationMBean[0];
               if (var2) {
                  break;
               }
            case 14:
               this._UserDefinedMib = "na";
               if (var2) {
                  break;
               }
            case 24:
               this._CommunityBasedAccessEnabled = true;
               if (var2) {
                  break;
               }
            case 7:
               this._Enabled = false;
               if (var2) {
                  break;
               }
            case 32:
               this._InformEnabled = false;
               if (var2) {
                  break;
               }
            case 31:
               this._SNMPAccessForUserMBeansEnabled = false;
               if (var2) {
                  break;
               }
            case 8:
               this._SendAutomaticTrapsEnabled = true;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/domain.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/domain";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String getType() {
      return "SNMPAgent";
   }

   public void putValue(String var1, Object var2) {
      String var5;
      if (var1.equals("AuthenticationProtocol")) {
         var5 = this._AuthenticationProtocol;
         this._AuthenticationProtocol = (String)var2;
         this._postSet(26, var5, this._AuthenticationProtocol);
      } else {
         boolean var8;
         if (var1.equals("CommunityBasedAccessEnabled")) {
            var8 = this._CommunityBasedAccessEnabled;
            this._CommunityBasedAccessEnabled = (Boolean)var2;
            this._postSet(24, var8, this._CommunityBasedAccessEnabled);
         } else if (var1.equals("CommunityPrefix")) {
            var5 = this._CommunityPrefix;
            this._CommunityPrefix = (String)var2;
            this._postSet(13, var5, this._CommunityPrefix);
         } else {
            int var7;
            if (var1.equals("DebugLevel")) {
               var7 = this._DebugLevel;
               this._DebugLevel = (Integer)var2;
               this._postSet(15, var7, this._DebugLevel);
            } else if (var1.equals("Enabled")) {
               var8 = this._Enabled;
               this._Enabled = (Boolean)var2;
               this._postSet(7, var8, this._Enabled);
            } else if (var1.equals("InformEnabled")) {
               var8 = this._InformEnabled;
               this._InformEnabled = (Boolean)var2;
               this._postSet(32, var8, this._InformEnabled);
            } else if (var1.equals("InformRetryInterval")) {
               var7 = this._InformRetryInterval;
               this._InformRetryInterval = (Integer)var2;
               this._postSet(28, var7, this._InformRetryInterval);
            } else if (var1.equals("LocalizedKeyCacheInvalidationInterval")) {
               long var15 = this._LocalizedKeyCacheInvalidationInterval;
               this._LocalizedKeyCacheInvalidationInterval = (Long)var2;
               this._postSet(30, var15, this._LocalizedKeyCacheInvalidationInterval);
            } else if (var1.equals("MasterAgentXPort")) {
               var7 = this._MasterAgentXPort;
               this._MasterAgentXPort = (Integer)var2;
               this._postSet(33, var7, this._MasterAgentXPort);
            } else if (var1.equals("MaxInformRetryCount")) {
               var7 = this._MaxInformRetryCount;
               this._MaxInformRetryCount = (Integer)var2;
               this._postSet(29, var7, this._MaxInformRetryCount);
            } else if (var1.equals("MibDataRefreshInterval")) {
               var7 = this._MibDataRefreshInterval;
               this._MibDataRefreshInterval = (Integer)var2;
               this._postSet(11, var7, this._MibDataRefreshInterval);
            } else if (var1.equals("Name")) {
               var5 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var5, this._Name);
            } else if (var1.equals("PrivacyProtocol")) {
               var5 = this._PrivacyProtocol;
               this._PrivacyProtocol = (String)var2;
               this._postSet(27, var5, this._PrivacyProtocol);
            } else if (var1.equals("SNMPAccessForUserMBeansEnabled")) {
               var8 = this._SNMPAccessForUserMBeansEnabled;
               this._SNMPAccessForUserMBeansEnabled = (Boolean)var2;
               this._postSet(31, var8, this._SNMPAccessForUserMBeansEnabled);
            } else if (var1.equals("SNMPAttributeChanges")) {
               SNMPAttributeChangeMBean[] var14 = this._SNMPAttributeChanges;
               this._SNMPAttributeChanges = (SNMPAttributeChangeMBean[])((SNMPAttributeChangeMBean[])var2);
               this._postSet(23, var14, this._SNMPAttributeChanges);
            } else if (var1.equals("SNMPCounterMonitors")) {
               SNMPCounterMonitorMBean[] var13 = this._SNMPCounterMonitors;
               this._SNMPCounterMonitors = (SNMPCounterMonitorMBean[])((SNMPCounterMonitorMBean[])var2);
               this._postSet(21, var13, this._SNMPCounterMonitors);
            } else if (var1.equals("SNMPEngineId")) {
               var5 = this._SNMPEngineId;
               this._SNMPEngineId = (String)var2;
               this._postSet(25, var5, this._SNMPEngineId);
            } else if (var1.equals("SNMPGaugeMonitors")) {
               SNMPGaugeMonitorMBean[] var12 = this._SNMPGaugeMonitors;
               this._SNMPGaugeMonitors = (SNMPGaugeMonitorMBean[])((SNMPGaugeMonitorMBean[])var2);
               this._postSet(19, var12, this._SNMPGaugeMonitors);
            } else if (var1.equals("SNMPLogFilters")) {
               SNMPLogFilterMBean[] var11 = this._SNMPLogFilters;
               this._SNMPLogFilters = (SNMPLogFilterMBean[])((SNMPLogFilterMBean[])var2);
               this._postSet(22, var11, this._SNMPLogFilters);
            } else if (var1.equals("SNMPPort")) {
               var7 = this._SNMPPort;
               this._SNMPPort = (Integer)var2;
               this._postSet(9, var7, this._SNMPPort);
            } else if (var1.equals("SNMPProxies")) {
               SNMPProxyMBean[] var10 = this._SNMPProxies;
               this._SNMPProxies = (SNMPProxyMBean[])((SNMPProxyMBean[])var2);
               this._postSet(18, var10, this._SNMPProxies);
            } else if (var1.equals("SNMPStringMonitors")) {
               SNMPStringMonitorMBean[] var9 = this._SNMPStringMonitors;
               this._SNMPStringMonitors = (SNMPStringMonitorMBean[])((SNMPStringMonitorMBean[])var2);
               this._postSet(20, var9, this._SNMPStringMonitors);
            } else {
               SNMPTrapDestinationMBean[] var6;
               if (var1.equals("SNMPTrapDestinations")) {
                  var6 = this._SNMPTrapDestinations;
                  this._SNMPTrapDestinations = (SNMPTrapDestinationMBean[])((SNMPTrapDestinationMBean[])var2);
                  this._postSet(17, var6, this._SNMPTrapDestinations);
               } else if (var1.equals("SNMPTrapVersion")) {
                  var7 = this._SNMPTrapVersion;
                  this._SNMPTrapVersion = (Integer)var2;
                  this._postSet(10, var7, this._SNMPTrapVersion);
               } else if (var1.equals("SendAutomaticTrapsEnabled")) {
                  var8 = this._SendAutomaticTrapsEnabled;
                  this._SendAutomaticTrapsEnabled = (Boolean)var2;
                  this._postSet(8, var8, this._SendAutomaticTrapsEnabled);
               } else if (var1.equals("ServerStatusCheckIntervalFactor")) {
                  var7 = this._ServerStatusCheckIntervalFactor;
                  this._ServerStatusCheckIntervalFactor = (Integer)var2;
                  this._postSet(12, var7, this._ServerStatusCheckIntervalFactor);
               } else if (var1.equals("TargetedTrapDestinations")) {
                  var6 = this._TargetedTrapDestinations;
                  this._TargetedTrapDestinations = (SNMPTrapDestinationMBean[])((SNMPTrapDestinationMBean[])var2);
                  this._postSet(16, var6, this._TargetedTrapDestinations);
               } else if (var1.equals("UserDefinedMib")) {
                  var5 = this._UserDefinedMib;
                  this._UserDefinedMib = (String)var2;
                  this._postSet(14, var5, this._UserDefinedMib);
               } else if (var1.equals("customizer")) {
                  SNMPAgent var3 = this._customizer;
                  this._customizer = (SNMPAgent)var2;
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AuthenticationProtocol")) {
         return this._AuthenticationProtocol;
      } else if (var1.equals("CommunityBasedAccessEnabled")) {
         return new Boolean(this._CommunityBasedAccessEnabled);
      } else if (var1.equals("CommunityPrefix")) {
         return this._CommunityPrefix;
      } else if (var1.equals("DebugLevel")) {
         return new Integer(this._DebugLevel);
      } else if (var1.equals("Enabled")) {
         return new Boolean(this._Enabled);
      } else if (var1.equals("InformEnabled")) {
         return new Boolean(this._InformEnabled);
      } else if (var1.equals("InformRetryInterval")) {
         return new Integer(this._InformRetryInterval);
      } else if (var1.equals("LocalizedKeyCacheInvalidationInterval")) {
         return new Long(this._LocalizedKeyCacheInvalidationInterval);
      } else if (var1.equals("MasterAgentXPort")) {
         return new Integer(this._MasterAgentXPort);
      } else if (var1.equals("MaxInformRetryCount")) {
         return new Integer(this._MaxInformRetryCount);
      } else if (var1.equals("MibDataRefreshInterval")) {
         return new Integer(this._MibDataRefreshInterval);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("PrivacyProtocol")) {
         return this._PrivacyProtocol;
      } else if (var1.equals("SNMPAccessForUserMBeansEnabled")) {
         return new Boolean(this._SNMPAccessForUserMBeansEnabled);
      } else if (var1.equals("SNMPAttributeChanges")) {
         return this._SNMPAttributeChanges;
      } else if (var1.equals("SNMPCounterMonitors")) {
         return this._SNMPCounterMonitors;
      } else if (var1.equals("SNMPEngineId")) {
         return this._SNMPEngineId;
      } else if (var1.equals("SNMPGaugeMonitors")) {
         return this._SNMPGaugeMonitors;
      } else if (var1.equals("SNMPLogFilters")) {
         return this._SNMPLogFilters;
      } else if (var1.equals("SNMPPort")) {
         return new Integer(this._SNMPPort);
      } else if (var1.equals("SNMPProxies")) {
         return this._SNMPProxies;
      } else if (var1.equals("SNMPStringMonitors")) {
         return this._SNMPStringMonitors;
      } else if (var1.equals("SNMPTrapDestinations")) {
         return this._SNMPTrapDestinations;
      } else if (var1.equals("SNMPTrapVersion")) {
         return new Integer(this._SNMPTrapVersion);
      } else if (var1.equals("SendAutomaticTrapsEnabled")) {
         return new Boolean(this._SendAutomaticTrapsEnabled);
      } else if (var1.equals("ServerStatusCheckIntervalFactor")) {
         return new Integer(this._ServerStatusCheckIntervalFactor);
      } else if (var1.equals("TargetedTrapDestinations")) {
         return this._TargetedTrapDestinations;
      } else if (var1.equals("UserDefinedMib")) {
         return this._UserDefinedMib;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("AuthenticationProtocol", "noAuth");
      } catch (IllegalArgumentException var4) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property AuthenticationProtocol in SNMPAgentMBean" + var4.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("CommunityPrefix", "public");
      } catch (IllegalArgumentException var3) {
         throw new DescriptorValidateException("The default value for the property  is zero-length. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-zero-length value on @default annotation. Refer annotation legalZeroLength on property CommunityPrefix in SNMPAgentMBean" + var3.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("PrivacyProtocol", "noPriv");
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property PrivacyProtocol in SNMPAgentMBean" + var2.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("UserDefinedMib", "na");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is zero-length. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-zero-length value on @default annotation. Refer annotation legalZeroLength on property UserDefinedMib in SNMPAgentMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 8:
            case 12:
            case 13:
            case 24:
            case 26:
            case 27:
            case 29:
            case 31:
            case 32:
            case 33:
            case 34:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            default:
               break;
            case 7:
               if (var1.equals("enabled")) {
                  return 7;
               }
               break;
            case 9:
               if (var1.equals("snmp-port")) {
                  return 9;
               }
               break;
            case 10:
               if (var1.equals("snmp-proxy")) {
                  return 18;
               }
               break;
            case 11:
               if (var1.equals("debug-level")) {
                  return 15;
               }
               break;
            case 14:
               if (var1.equals("snmp-engine-id")) {
                  return 25;
               }

               if (var1.equals("inform-enabled")) {
                  return 32;
               }
               break;
            case 15:
               if (var1.equals("snmp-log-filter")) {
                  return 22;
               }
               break;
            case 16:
               if (var1.equals("community-prefix")) {
                  return 13;
               }

               if (var1.equals("privacy-protocol")) {
                  return 27;
               }

               if (var1.equals("user-defined-mib")) {
                  return 14;
               }
               break;
            case 17:
               if (var1.equals("snmp-trap-version")) {
                  return 10;
               }
               break;
            case 18:
               if (var1.equals("master-agentx-port")) {
                  return 33;
               }

               if (var1.equals("snmp-gauge-monitor")) {
                  return 19;
               }
               break;
            case 19:
               if (var1.equals("snmp-string-monitor")) {
                  return 20;
               }
               break;
            case 20:
               if (var1.equals("snmp-counter-monitor")) {
                  return 21;
               }
               break;
            case 21:
               if (var1.equals("inform-retry-interval")) {
                  return 28;
               }

               if (var1.equals("snmp-attribute-change")) {
                  return 23;
               }

               if (var1.equals("snmp-trap-destination")) {
                  return 17;
               }
               break;
            case 22:
               if (var1.equals("max-inform-retry-count")) {
                  return 29;
               }
               break;
            case 23:
               if (var1.equals("authentication-protocol")) {
                  return 26;
               }
               break;
            case 25:
               if (var1.equals("mib-data-refresh-interval")) {
                  return 11;
               }

               if (var1.equals("targeted-trap-destination")) {
                  return 16;
               }
               break;
            case 28:
               if (var1.equals("send-automatic-traps-enabled")) {
                  return 8;
               }
               break;
            case 30:
               if (var1.equals("community-based-access-enabled")) {
                  return 24;
               }
               break;
            case 35:
               if (var1.equals("server-status-check-interval-factor")) {
                  return 12;
               }

               if (var1.equals("snmp-access-for-userm-beans-enabled")) {
                  return 31;
               }
               break;
            case 41:
               if (var1.equals("localized-key-cache-invalidation-interval")) {
                  return 30;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 17:
               return new SNMPTrapDestinationMBeanImpl.SchemaHelper2();
            case 18:
               return new SNMPProxyMBeanImpl.SchemaHelper2();
            case 19:
               return new SNMPGaugeMonitorMBeanImpl.SchemaHelper2();
            case 20:
               return new SNMPStringMonitorMBeanImpl.SchemaHelper2();
            case 21:
               return new SNMPCounterMonitorMBeanImpl.SchemaHelper2();
            case 22:
               return new SNMPLogFilterMBeanImpl.SchemaHelper2();
            case 23:
               return new SNMPAttributeChangeMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "enabled";
            case 8:
               return "send-automatic-traps-enabled";
            case 9:
               return "snmp-port";
            case 10:
               return "snmp-trap-version";
            case 11:
               return "mib-data-refresh-interval";
            case 12:
               return "server-status-check-interval-factor";
            case 13:
               return "community-prefix";
            case 14:
               return "user-defined-mib";
            case 15:
               return "debug-level";
            case 16:
               return "targeted-trap-destination";
            case 17:
               return "snmp-trap-destination";
            case 18:
               return "snmp-proxy";
            case 19:
               return "snmp-gauge-monitor";
            case 20:
               return "snmp-string-monitor";
            case 21:
               return "snmp-counter-monitor";
            case 22:
               return "snmp-log-filter";
            case 23:
               return "snmp-attribute-change";
            case 24:
               return "community-based-access-enabled";
            case 25:
               return "snmp-engine-id";
            case 26:
               return "authentication-protocol";
            case 27:
               return "privacy-protocol";
            case 28:
               return "inform-retry-interval";
            case 29:
               return "max-inform-retry-count";
            case 30:
               return "localized-key-cache-invalidation-interval";
            case 31:
               return "snmp-access-for-userm-beans-enabled";
            case 32:
               return "inform-enabled";
            case 33:
               return "master-agentx-port";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 16:
               return true;
            case 17:
               return true;
            case 18:
               return true;
            case 19:
               return true;
            case 20:
               return true;
            case 21:
               return true;
            case 22:
               return true;
            case 23:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 17:
               return true;
            case 18:
               return true;
            case 19:
               return true;
            case 20:
               return true;
            case 21:
               return true;
            case 22:
               return true;
            case 23:
               return true;
            default:
               return super.isBean(var1);
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 2:
               return true;
            default:
               return super.isKey(var1);
         }
      }

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private SNMPAgentMBeanImpl bean;

      protected Helper(SNMPAgentMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Enabled";
            case 8:
               return "SendAutomaticTrapsEnabled";
            case 9:
               return "SNMPPort";
            case 10:
               return "SNMPTrapVersion";
            case 11:
               return "MibDataRefreshInterval";
            case 12:
               return "ServerStatusCheckIntervalFactor";
            case 13:
               return "CommunityPrefix";
            case 14:
               return "UserDefinedMib";
            case 15:
               return "DebugLevel";
            case 16:
               return "TargetedTrapDestinations";
            case 17:
               return "SNMPTrapDestinations";
            case 18:
               return "SNMPProxies";
            case 19:
               return "SNMPGaugeMonitors";
            case 20:
               return "SNMPStringMonitors";
            case 21:
               return "SNMPCounterMonitors";
            case 22:
               return "SNMPLogFilters";
            case 23:
               return "SNMPAttributeChanges";
            case 24:
               return "CommunityBasedAccessEnabled";
            case 25:
               return "SNMPEngineId";
            case 26:
               return "AuthenticationProtocol";
            case 27:
               return "PrivacyProtocol";
            case 28:
               return "InformRetryInterval";
            case 29:
               return "MaxInformRetryCount";
            case 30:
               return "LocalizedKeyCacheInvalidationInterval";
            case 31:
               return "SNMPAccessForUserMBeansEnabled";
            case 32:
               return "InformEnabled";
            case 33:
               return "MasterAgentXPort";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AuthenticationProtocol")) {
            return 26;
         } else if (var1.equals("CommunityPrefix")) {
            return 13;
         } else if (var1.equals("DebugLevel")) {
            return 15;
         } else if (var1.equals("InformRetryInterval")) {
            return 28;
         } else if (var1.equals("LocalizedKeyCacheInvalidationInterval")) {
            return 30;
         } else if (var1.equals("MasterAgentXPort")) {
            return 33;
         } else if (var1.equals("MaxInformRetryCount")) {
            return 29;
         } else if (var1.equals("MibDataRefreshInterval")) {
            return 11;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("PrivacyProtocol")) {
            return 27;
         } else if (var1.equals("SNMPAttributeChanges")) {
            return 23;
         } else if (var1.equals("SNMPCounterMonitors")) {
            return 21;
         } else if (var1.equals("SNMPEngineId")) {
            return 25;
         } else if (var1.equals("SNMPGaugeMonitors")) {
            return 19;
         } else if (var1.equals("SNMPLogFilters")) {
            return 22;
         } else if (var1.equals("SNMPPort")) {
            return 9;
         } else if (var1.equals("SNMPProxies")) {
            return 18;
         } else if (var1.equals("SNMPStringMonitors")) {
            return 20;
         } else if (var1.equals("SNMPTrapDestinations")) {
            return 17;
         } else if (var1.equals("SNMPTrapVersion")) {
            return 10;
         } else if (var1.equals("ServerStatusCheckIntervalFactor")) {
            return 12;
         } else if (var1.equals("TargetedTrapDestinations")) {
            return 16;
         } else if (var1.equals("UserDefinedMib")) {
            return 14;
         } else if (var1.equals("CommunityBasedAccessEnabled")) {
            return 24;
         } else if (var1.equals("Enabled")) {
            return 7;
         } else if (var1.equals("InformEnabled")) {
            return 32;
         } else if (var1.equals("SNMPAccessForUserMBeansEnabled")) {
            return 31;
         } else {
            return var1.equals("SendAutomaticTrapsEnabled") ? 8 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getSNMPAttributeChanges()));
         var1.add(new ArrayIterator(this.bean.getSNMPCounterMonitors()));
         var1.add(new ArrayIterator(this.bean.getSNMPGaugeMonitors()));
         var1.add(new ArrayIterator(this.bean.getSNMPLogFilters()));
         var1.add(new ArrayIterator(this.bean.getSNMPProxies()));
         var1.add(new ArrayIterator(this.bean.getSNMPStringMonitors()));
         var1.add(new ArrayIterator(this.bean.getSNMPTrapDestinations()));
         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            if (this.bean.isAuthenticationProtocolSet()) {
               var2.append("AuthenticationProtocol");
               var2.append(String.valueOf(this.bean.getAuthenticationProtocol()));
            }

            if (this.bean.isCommunityPrefixSet()) {
               var2.append("CommunityPrefix");
               var2.append(String.valueOf(this.bean.getCommunityPrefix()));
            }

            if (this.bean.isDebugLevelSet()) {
               var2.append("DebugLevel");
               var2.append(String.valueOf(this.bean.getDebugLevel()));
            }

            if (this.bean.isInformRetryIntervalSet()) {
               var2.append("InformRetryInterval");
               var2.append(String.valueOf(this.bean.getInformRetryInterval()));
            }

            if (this.bean.isLocalizedKeyCacheInvalidationIntervalSet()) {
               var2.append("LocalizedKeyCacheInvalidationInterval");
               var2.append(String.valueOf(this.bean.getLocalizedKeyCacheInvalidationInterval()));
            }

            if (this.bean.isMasterAgentXPortSet()) {
               var2.append("MasterAgentXPort");
               var2.append(String.valueOf(this.bean.getMasterAgentXPort()));
            }

            if (this.bean.isMaxInformRetryCountSet()) {
               var2.append("MaxInformRetryCount");
               var2.append(String.valueOf(this.bean.getMaxInformRetryCount()));
            }

            if (this.bean.isMibDataRefreshIntervalSet()) {
               var2.append("MibDataRefreshInterval");
               var2.append(String.valueOf(this.bean.getMibDataRefreshInterval()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPrivacyProtocolSet()) {
               var2.append("PrivacyProtocol");
               var2.append(String.valueOf(this.bean.getPrivacyProtocol()));
            }

            var5 = 0L;

            int var7;
            for(var7 = 0; var7 < this.bean.getSNMPAttributeChanges().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPAttributeChanges()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPCounterMonitors().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPCounterMonitors()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isSNMPEngineIdSet()) {
               var2.append("SNMPEngineId");
               var2.append(String.valueOf(this.bean.getSNMPEngineId()));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPGaugeMonitors().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPGaugeMonitors()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPLogFilters().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPLogFilters()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isSNMPPortSet()) {
               var2.append("SNMPPort");
               var2.append(String.valueOf(this.bean.getSNMPPort()));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPProxies().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPProxies()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPStringMonitors().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPStringMonitors()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPTrapDestinations().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPTrapDestinations()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isSNMPTrapVersionSet()) {
               var2.append("SNMPTrapVersion");
               var2.append(String.valueOf(this.bean.getSNMPTrapVersion()));
            }

            if (this.bean.isServerStatusCheckIntervalFactorSet()) {
               var2.append("ServerStatusCheckIntervalFactor");
               var2.append(String.valueOf(this.bean.getServerStatusCheckIntervalFactor()));
            }

            if (this.bean.isTargetedTrapDestinationsSet()) {
               var2.append("TargetedTrapDestinations");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargetedTrapDestinations())));
            }

            if (this.bean.isUserDefinedMibSet()) {
               var2.append("UserDefinedMib");
               var2.append(String.valueOf(this.bean.getUserDefinedMib()));
            }

            if (this.bean.isCommunityBasedAccessEnabledSet()) {
               var2.append("CommunityBasedAccessEnabled");
               var2.append(String.valueOf(this.bean.isCommunityBasedAccessEnabled()));
            }

            if (this.bean.isEnabledSet()) {
               var2.append("Enabled");
               var2.append(String.valueOf(this.bean.isEnabled()));
            }

            if (this.bean.isInformEnabledSet()) {
               var2.append("InformEnabled");
               var2.append(String.valueOf(this.bean.isInformEnabled()));
            }

            if (this.bean.isSNMPAccessForUserMBeansEnabledSet()) {
               var2.append("SNMPAccessForUserMBeansEnabled");
               var2.append(String.valueOf(this.bean.isSNMPAccessForUserMBeansEnabled()));
            }

            if (this.bean.isSendAutomaticTrapsEnabledSet()) {
               var2.append("SendAutomaticTrapsEnabled");
               var2.append(String.valueOf(this.bean.isSendAutomaticTrapsEnabled()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            SNMPAgentMBeanImpl var2 = (SNMPAgentMBeanImpl)var1;
            this.computeDiff("AuthenticationProtocol", this.bean.getAuthenticationProtocol(), var2.getAuthenticationProtocol(), true);
            this.computeDiff("CommunityPrefix", this.bean.getCommunityPrefix(), var2.getCommunityPrefix(), true);
            this.computeDiff("DebugLevel", this.bean.getDebugLevel(), var2.getDebugLevel(), true);
            this.computeDiff("InformRetryInterval", this.bean.getInformRetryInterval(), var2.getInformRetryInterval(), true);
            this.computeDiff("LocalizedKeyCacheInvalidationInterval", this.bean.getLocalizedKeyCacheInvalidationInterval(), var2.getLocalizedKeyCacheInvalidationInterval(), true);
            this.computeDiff("MasterAgentXPort", this.bean.getMasterAgentXPort(), var2.getMasterAgentXPort(), true);
            this.computeDiff("MaxInformRetryCount", this.bean.getMaxInformRetryCount(), var2.getMaxInformRetryCount(), true);
            this.computeDiff("MibDataRefreshInterval", this.bean.getMibDataRefreshInterval(), var2.getMibDataRefreshInterval(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("PrivacyProtocol", this.bean.getPrivacyProtocol(), var2.getPrivacyProtocol(), true);
            this.computeChildDiff("SNMPAttributeChanges", this.bean.getSNMPAttributeChanges(), var2.getSNMPAttributeChanges(), true);
            this.computeChildDiff("SNMPCounterMonitors", this.bean.getSNMPCounterMonitors(), var2.getSNMPCounterMonitors(), true);
            this.computeDiff("SNMPEngineId", this.bean.getSNMPEngineId(), var2.getSNMPEngineId(), true);
            this.computeChildDiff("SNMPGaugeMonitors", this.bean.getSNMPGaugeMonitors(), var2.getSNMPGaugeMonitors(), true);
            this.computeChildDiff("SNMPLogFilters", this.bean.getSNMPLogFilters(), var2.getSNMPLogFilters(), true);
            this.computeDiff("SNMPPort", this.bean.getSNMPPort(), var2.getSNMPPort(), true);
            this.computeChildDiff("SNMPProxies", this.bean.getSNMPProxies(), var2.getSNMPProxies(), true);
            this.computeChildDiff("SNMPStringMonitors", this.bean.getSNMPStringMonitors(), var2.getSNMPStringMonitors(), true);
            this.computeChildDiff("SNMPTrapDestinations", this.bean.getSNMPTrapDestinations(), var2.getSNMPTrapDestinations(), true);
            this.computeDiff("SNMPTrapVersion", this.bean.getSNMPTrapVersion(), var2.getSNMPTrapVersion(), true);
            this.computeDiff("ServerStatusCheckIntervalFactor", this.bean.getServerStatusCheckIntervalFactor(), var2.getServerStatusCheckIntervalFactor(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("TargetedTrapDestinations", this.bean.getTargetedTrapDestinations(), var2.getTargetedTrapDestinations(), true);
            }

            this.computeDiff("UserDefinedMib", this.bean.getUserDefinedMib(), var2.getUserDefinedMib(), true);
            this.computeDiff("CommunityBasedAccessEnabled", this.bean.isCommunityBasedAccessEnabled(), var2.isCommunityBasedAccessEnabled(), true);
            this.computeDiff("Enabled", this.bean.isEnabled(), var2.isEnabled(), true);
            this.computeDiff("InformEnabled", this.bean.isInformEnabled(), var2.isInformEnabled(), true);
            this.computeDiff("SNMPAccessForUserMBeansEnabled", this.bean.isSNMPAccessForUserMBeansEnabled(), var2.isSNMPAccessForUserMBeansEnabled(), true);
            this.computeDiff("SendAutomaticTrapsEnabled", this.bean.isSendAutomaticTrapsEnabled(), var2.isSendAutomaticTrapsEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SNMPAgentMBeanImpl var3 = (SNMPAgentMBeanImpl)var1.getSourceBean();
            SNMPAgentMBeanImpl var4 = (SNMPAgentMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AuthenticationProtocol")) {
                  var3.setAuthenticationProtocol(var4.getAuthenticationProtocol());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
               } else if (var5.equals("CommunityPrefix")) {
                  var3.setCommunityPrefix(var4.getCommunityPrefix());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("DebugLevel")) {
                  var3.setDebugLevel(var4.getDebugLevel());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("InformRetryInterval")) {
                  var3.setInformRetryInterval(var4.getInformRetryInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 28);
               } else if (var5.equals("LocalizedKeyCacheInvalidationInterval")) {
                  var3.setLocalizedKeyCacheInvalidationInterval(var4.getLocalizedKeyCacheInvalidationInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 30);
               } else if (var5.equals("MasterAgentXPort")) {
                  var3.setMasterAgentXPort(var4.getMasterAgentXPort());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 33);
               } else if (var5.equals("MaxInformRetryCount")) {
                  var3.setMaxInformRetryCount(var4.getMaxInformRetryCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 29);
               } else if (var5.equals("MibDataRefreshInterval")) {
                  var3.setMibDataRefreshInterval(var4.getMibDataRefreshInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("PrivacyProtocol")) {
                  var3.setPrivacyProtocol(var4.getPrivacyProtocol());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
               } else if (var5.equals("SNMPAttributeChanges")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addSNMPAttributeChange((SNMPAttributeChangeMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeSNMPAttributeChange((SNMPAttributeChangeMBean)var2.getRemovedObject());
                  }

                  if (var3.getSNMPAttributeChanges() == null || var3.getSNMPAttributeChanges().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                  }
               } else if (var5.equals("SNMPCounterMonitors")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addSNMPCounterMonitor((SNMPCounterMonitorMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeSNMPCounterMonitor((SNMPCounterMonitorMBean)var2.getRemovedObject());
                  }

                  if (var3.getSNMPCounterMonitors() == null || var3.getSNMPCounterMonitors().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                  }
               } else if (var5.equals("SNMPEngineId")) {
                  var3.setSNMPEngineId(var4.getSNMPEngineId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("SNMPGaugeMonitors")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addSNMPGaugeMonitor((SNMPGaugeMonitorMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeSNMPGaugeMonitor((SNMPGaugeMonitorMBean)var2.getRemovedObject());
                  }

                  if (var3.getSNMPGaugeMonitors() == null || var3.getSNMPGaugeMonitors().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                  }
               } else if (var5.equals("SNMPLogFilters")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addSNMPLogFilter((SNMPLogFilterMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeSNMPLogFilter((SNMPLogFilterMBean)var2.getRemovedObject());
                  }

                  if (var3.getSNMPLogFilters() == null || var3.getSNMPLogFilters().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                  }
               } else if (var5.equals("SNMPPort")) {
                  var3.setSNMPPort(var4.getSNMPPort());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("SNMPProxies")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addSNMPProxy((SNMPProxyMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeSNMPProxy((SNMPProxyMBean)var2.getRemovedObject());
                  }

                  if (var3.getSNMPProxies() == null || var3.getSNMPProxies().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                  }
               } else if (var5.equals("SNMPStringMonitors")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addSNMPStringMonitor((SNMPStringMonitorMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeSNMPStringMonitor((SNMPStringMonitorMBean)var2.getRemovedObject());
                  }

                  if (var3.getSNMPStringMonitors() == null || var3.getSNMPStringMonitors().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                  }
               } else if (var5.equals("SNMPTrapDestinations")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addSNMPTrapDestination((SNMPTrapDestinationMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeSNMPTrapDestination((SNMPTrapDestinationMBean)var2.getRemovedObject());
                  }

                  if (var3.getSNMPTrapDestinations() == null || var3.getSNMPTrapDestinations().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                  }
               } else if (var5.equals("SNMPTrapVersion")) {
                  var3.setSNMPTrapVersion(var4.getSNMPTrapVersion());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("ServerStatusCheckIntervalFactor")) {
                  var3.setServerStatusCheckIntervalFactor(var4.getServerStatusCheckIntervalFactor());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("TargetedTrapDestinations")) {
                  var3.setTargetedTrapDestinationsAsString(var4.getTargetedTrapDestinationsAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("UserDefinedMib")) {
                  var3.setUserDefinedMib(var4.getUserDefinedMib());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("CommunityBasedAccessEnabled")) {
                  var3.setCommunityBasedAccessEnabled(var4.isCommunityBasedAccessEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("Enabled")) {
                  var3.setEnabled(var4.isEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("InformEnabled")) {
                  var3.setInformEnabled(var4.isInformEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 32);
               } else if (var5.equals("SNMPAccessForUserMBeansEnabled")) {
                  var3.setSNMPAccessForUserMBeansEnabled(var4.isSNMPAccessForUserMBeansEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 31);
               } else if (var5.equals("SendAutomaticTrapsEnabled")) {
                  var3.setSendAutomaticTrapsEnabled(var4.isSendAutomaticTrapsEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else {
                  super.applyPropertyUpdate(var1, var2);
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            SNMPAgentMBeanImpl var5 = (SNMPAgentMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AuthenticationProtocol")) && this.bean.isAuthenticationProtocolSet()) {
               var5.setAuthenticationProtocol(this.bean.getAuthenticationProtocol());
            }

            if ((var3 == null || !var3.contains("CommunityPrefix")) && this.bean.isCommunityPrefixSet()) {
               var5.setCommunityPrefix(this.bean.getCommunityPrefix());
            }

            if ((var3 == null || !var3.contains("DebugLevel")) && this.bean.isDebugLevelSet()) {
               var5.setDebugLevel(this.bean.getDebugLevel());
            }

            if ((var3 == null || !var3.contains("InformRetryInterval")) && this.bean.isInformRetryIntervalSet()) {
               var5.setInformRetryInterval(this.bean.getInformRetryInterval());
            }

            if ((var3 == null || !var3.contains("LocalizedKeyCacheInvalidationInterval")) && this.bean.isLocalizedKeyCacheInvalidationIntervalSet()) {
               var5.setLocalizedKeyCacheInvalidationInterval(this.bean.getLocalizedKeyCacheInvalidationInterval());
            }

            if ((var3 == null || !var3.contains("MasterAgentXPort")) && this.bean.isMasterAgentXPortSet()) {
               var5.setMasterAgentXPort(this.bean.getMasterAgentXPort());
            }

            if ((var3 == null || !var3.contains("MaxInformRetryCount")) && this.bean.isMaxInformRetryCountSet()) {
               var5.setMaxInformRetryCount(this.bean.getMaxInformRetryCount());
            }

            if ((var3 == null || !var3.contains("MibDataRefreshInterval")) && this.bean.isMibDataRefreshIntervalSet()) {
               var5.setMibDataRefreshInterval(this.bean.getMibDataRefreshInterval());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("PrivacyProtocol")) && this.bean.isPrivacyProtocolSet()) {
               var5.setPrivacyProtocol(this.bean.getPrivacyProtocol());
            }

            int var8;
            if ((var3 == null || !var3.contains("SNMPAttributeChanges")) && this.bean.isSNMPAttributeChangesSet() && !var5._isSet(23)) {
               SNMPAttributeChangeMBean[] var6 = this.bean.getSNMPAttributeChanges();
               SNMPAttributeChangeMBean[] var7 = new SNMPAttributeChangeMBean[var6.length];

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (SNMPAttributeChangeMBean)((SNMPAttributeChangeMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setSNMPAttributeChanges(var7);
            }

            if ((var3 == null || !var3.contains("SNMPCounterMonitors")) && this.bean.isSNMPCounterMonitorsSet() && !var5._isSet(21)) {
               SNMPCounterMonitorMBean[] var11 = this.bean.getSNMPCounterMonitors();
               SNMPCounterMonitorMBean[] var14 = new SNMPCounterMonitorMBean[var11.length];

               for(var8 = 0; var8 < var14.length; ++var8) {
                  var14[var8] = (SNMPCounterMonitorMBean)((SNMPCounterMonitorMBean)this.createCopy((AbstractDescriptorBean)var11[var8], var2));
               }

               var5.setSNMPCounterMonitors(var14);
            }

            if ((var3 == null || !var3.contains("SNMPEngineId")) && this.bean.isSNMPEngineIdSet()) {
               var5.setSNMPEngineId(this.bean.getSNMPEngineId());
            }

            if ((var3 == null || !var3.contains("SNMPGaugeMonitors")) && this.bean.isSNMPGaugeMonitorsSet() && !var5._isSet(19)) {
               SNMPGaugeMonitorMBean[] var12 = this.bean.getSNMPGaugeMonitors();
               SNMPGaugeMonitorMBean[] var16 = new SNMPGaugeMonitorMBean[var12.length];

               for(var8 = 0; var8 < var16.length; ++var8) {
                  var16[var8] = (SNMPGaugeMonitorMBean)((SNMPGaugeMonitorMBean)this.createCopy((AbstractDescriptorBean)var12[var8], var2));
               }

               var5.setSNMPGaugeMonitors(var16);
            }

            if ((var3 == null || !var3.contains("SNMPLogFilters")) && this.bean.isSNMPLogFiltersSet() && !var5._isSet(22)) {
               SNMPLogFilterMBean[] var13 = this.bean.getSNMPLogFilters();
               SNMPLogFilterMBean[] var18 = new SNMPLogFilterMBean[var13.length];

               for(var8 = 0; var8 < var18.length; ++var8) {
                  var18[var8] = (SNMPLogFilterMBean)((SNMPLogFilterMBean)this.createCopy((AbstractDescriptorBean)var13[var8], var2));
               }

               var5.setSNMPLogFilters(var18);
            }

            if ((var3 == null || !var3.contains("SNMPPort")) && this.bean.isSNMPPortSet()) {
               var5.setSNMPPort(this.bean.getSNMPPort());
            }

            if ((var3 == null || !var3.contains("SNMPProxies")) && this.bean.isSNMPProxiesSet() && !var5._isSet(18)) {
               SNMPProxyMBean[] var15 = this.bean.getSNMPProxies();
               SNMPProxyMBean[] var20 = new SNMPProxyMBean[var15.length];

               for(var8 = 0; var8 < var20.length; ++var8) {
                  var20[var8] = (SNMPProxyMBean)((SNMPProxyMBean)this.createCopy((AbstractDescriptorBean)var15[var8], var2));
               }

               var5.setSNMPProxies(var20);
            }

            if ((var3 == null || !var3.contains("SNMPStringMonitors")) && this.bean.isSNMPStringMonitorsSet() && !var5._isSet(20)) {
               SNMPStringMonitorMBean[] var17 = this.bean.getSNMPStringMonitors();
               SNMPStringMonitorMBean[] var21 = new SNMPStringMonitorMBean[var17.length];

               for(var8 = 0; var8 < var21.length; ++var8) {
                  var21[var8] = (SNMPStringMonitorMBean)((SNMPStringMonitorMBean)this.createCopy((AbstractDescriptorBean)var17[var8], var2));
               }

               var5.setSNMPStringMonitors(var21);
            }

            if ((var3 == null || !var3.contains("SNMPTrapDestinations")) && this.bean.isSNMPTrapDestinationsSet() && !var5._isSet(17)) {
               SNMPTrapDestinationMBean[] var19 = this.bean.getSNMPTrapDestinations();
               SNMPTrapDestinationMBean[] var22 = new SNMPTrapDestinationMBean[var19.length];

               for(var8 = 0; var8 < var22.length; ++var8) {
                  var22[var8] = (SNMPTrapDestinationMBean)((SNMPTrapDestinationMBean)this.createCopy((AbstractDescriptorBean)var19[var8], var2));
               }

               var5.setSNMPTrapDestinations(var22);
            }

            if ((var3 == null || !var3.contains("SNMPTrapVersion")) && this.bean.isSNMPTrapVersionSet()) {
               var5.setSNMPTrapVersion(this.bean.getSNMPTrapVersion());
            }

            if ((var3 == null || !var3.contains("ServerStatusCheckIntervalFactor")) && this.bean.isServerStatusCheckIntervalFactorSet()) {
               var5.setServerStatusCheckIntervalFactor(this.bean.getServerStatusCheckIntervalFactor());
            }

            if (var2 && (var3 == null || !var3.contains("TargetedTrapDestinations")) && this.bean.isTargetedTrapDestinationsSet()) {
               var5._unSet(var5, 16);
               var5.setTargetedTrapDestinationsAsString(this.bean.getTargetedTrapDestinationsAsString());
            }

            if ((var3 == null || !var3.contains("UserDefinedMib")) && this.bean.isUserDefinedMibSet()) {
               var5.setUserDefinedMib(this.bean.getUserDefinedMib());
            }

            if ((var3 == null || !var3.contains("CommunityBasedAccessEnabled")) && this.bean.isCommunityBasedAccessEnabledSet()) {
               var5.setCommunityBasedAccessEnabled(this.bean.isCommunityBasedAccessEnabled());
            }

            if ((var3 == null || !var3.contains("Enabled")) && this.bean.isEnabledSet()) {
               var5.setEnabled(this.bean.isEnabled());
            }

            if ((var3 == null || !var3.contains("InformEnabled")) && this.bean.isInformEnabledSet()) {
               var5.setInformEnabled(this.bean.isInformEnabled());
            }

            if ((var3 == null || !var3.contains("SNMPAccessForUserMBeansEnabled")) && this.bean.isSNMPAccessForUserMBeansEnabledSet()) {
               var5.setSNMPAccessForUserMBeansEnabled(this.bean.isSNMPAccessForUserMBeansEnabled());
            }

            if ((var3 == null || !var3.contains("SendAutomaticTrapsEnabled")) && this.bean.isSendAutomaticTrapsEnabledSet()) {
               var5.setSendAutomaticTrapsEnabled(this.bean.isSendAutomaticTrapsEnabled());
            }

            return var5;
         } catch (RuntimeException var9) {
            throw var9;
         } catch (Exception var10) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var10);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getSNMPAttributeChanges(), var1, var2);
         this.inferSubTree(this.bean.getSNMPCounterMonitors(), var1, var2);
         this.inferSubTree(this.bean.getSNMPGaugeMonitors(), var1, var2);
         this.inferSubTree(this.bean.getSNMPLogFilters(), var1, var2);
         this.inferSubTree(this.bean.getSNMPProxies(), var1, var2);
         this.inferSubTree(this.bean.getSNMPStringMonitors(), var1, var2);
         this.inferSubTree(this.bean.getSNMPTrapDestinations(), var1, var2);
         this.inferSubTree(this.bean.getTargetedTrapDestinations(), var1, var2);
      }
   }
}
