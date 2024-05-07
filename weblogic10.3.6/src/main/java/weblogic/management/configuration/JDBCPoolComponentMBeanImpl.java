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
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JDBCPoolComponent;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class JDBCPoolComponentMBeanImpl extends ComponentMBeanImpl implements JDBCPoolComponentMBean, Serializable {
   private TargetMBean[] _ActivatedTargets;
   private ApplicationMBean _Application;
   private int _CacheSize;
   private int _CapacityIncrement;
   private boolean _CheckOnCreateEnabled;
   private boolean _CheckOnReleaseEnabled;
   private boolean _CheckOnReserveEnabled;
   private int _ConnectionCreationRetryFrequencySeconds;
   private int _ConnectionReserveTimeoutSeconds;
   private int _HighestNumUnavailable;
   private int _HighestNumWaiters;
   private int _InactiveConnectionTimeoutSeconds;
   private int _InitialCapacity;
   private boolean _LoggingEnabled;
   private int _MaxCapacity;
   private int _MaxIdleTime;
   private String _Name;
   private boolean _ProfilingEnabled;
   private int _ShrinkFrequencySeconds;
   private boolean _ShrinkingEnabled;
   private TargetMBean[] _Targets;
   private int _TestFrequencySeconds;
   private JDBCPoolComponent _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JDBCPoolComponentMBeanImpl() {
      try {
         this._customizer = new JDBCPoolComponent(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JDBCPoolComponentMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JDBCPoolComponent(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public ApplicationMBean getApplication() {
      return this._customizer.getApplication();
   }

   public int getInitialCapacity() throws ManagementException {
      return this._InitialCapacity;
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

   public TargetMBean[] getTargets() {
      return this._customizer.getTargets();
   }

   public String getTargetsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargets());
   }

   public boolean isApplicationSet() {
      return this._isSet(9);
   }

   public boolean isInitialCapacitySet() {
      return this._isSet(12);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isTargetsSet() {
      return this._isSet(7);
   }

   public void setTargetsAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._Targets);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, TargetMBean.class, new ReferenceManager.Resolver(this, 7) {
                  public void resolveReference(Object var1) {
                     try {
                        JDBCPoolComponentMBeanImpl.this.addTarget((TargetMBean)var1);
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
               TargetMBean[] var6 = this._Targets;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  TargetMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeTarget(var9);
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
         TargetMBean[] var2 = this._Targets;
         this._initializeProperty(7);
         this._postSet(7, var2, this._Targets);
      }
   }

   public void setApplication(ApplicationMBean var1) throws InvalidAttributeValueException {
      this._customizer.setApplication(var1);
   }

   public void setInitialCapacity(int var1) throws ManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("InitialCapacity", (long)var1, 0L, 2147483647L);
      this._InitialCapacity = var1;
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

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var4 = var1 == null ? new TargetMBeanImpl[0] : var1;
      var1 = (TargetMBean[])((TargetMBean[])this._getHelper()._cleanAndValidateArray(var4, TargetMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return JDBCPoolComponentMBeanImpl.this.getTargets();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      TargetMBean[] var5 = this.getTargets();
      this._customizer.setTargets(var1);
      this._postSet(7, var5, var1);
   }

   public boolean addTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         TargetMBean[] var2 = (TargetMBean[])((TargetMBean[])this._getHelper()._extendArray(this.getTargets(), TargetMBean.class, var1));

         try {
            this.setTargets(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public int getMaxCapacity() throws ManagementException {
      return this._MaxCapacity;
   }

   public boolean isMaxCapacitySet() {
      return this._isSet(13);
   }

   public boolean removeTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      TargetMBean[] var2 = this.getTargets();
      TargetMBean[] var3 = (TargetMBean[])((TargetMBean[])this._getHelper()._removeElement(var2, TargetMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setTargets(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
            } else if (var5 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public void setMaxCapacity(int var1) throws ManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxCapacity", (long)var1, 1L, 2147483647L);
      this._MaxCapacity = var1;
   }

   public TargetMBean[] getActivatedTargets() {
      return this._customizer.getActivatedTargets();
   }

   public int getCapacityIncrement() throws ManagementException {
      return this._CapacityIncrement;
   }

   public boolean isActivatedTargetsSet() {
      return this._isSet(11);
   }

   public boolean isCapacityIncrementSet() {
      return this._isSet(14);
   }

   public void addActivatedTarget(TargetMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 11)) {
         TargetMBean[] var2 = (TargetMBean[])((TargetMBean[])this._getHelper()._extendArray(this.getActivatedTargets(), TargetMBean.class, var1));

         try {
            this.setActivatedTargets(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public void setCapacityIncrement(int var1) throws ManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CapacityIncrement", (long)var1, 1L, 2147483647L);
      this._CapacityIncrement = var1;
   }

   public int getHighestNumWaiters() throws ManagementException {
      return this._HighestNumWaiters;
   }

   public boolean isHighestNumWaitersSet() {
      return this._isSet(15);
   }

   public void removeActivatedTarget(TargetMBean var1) {
      TargetMBean[] var2 = this.getActivatedTargets();
      TargetMBean[] var3 = (TargetMBean[])((TargetMBean[])this._getHelper()._removeElement(var2, TargetMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setActivatedTargets(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setActivatedTargets(TargetMBean[] var1) {
      Object var2 = var1 == null ? new TargetMBeanImpl[0] : var1;
      this._ActivatedTargets = (TargetMBean[])var2;
   }

   public void setHighestNumWaiters(int var1) throws ManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("HighestNumWaiters", (long)var1, 0L, 2147483647L);
      this._HighestNumWaiters = var1;
   }

   public boolean activated(TargetMBean var1) {
      return this._customizer.activated(var1);
   }

   public int getHighestNumUnavailable() throws ManagementException {
      return this._HighestNumUnavailable;
   }

   public boolean isHighestNumUnavailableSet() {
      return this._isSet(16);
   }

   public void refreshDDsIfNeeded(String[] var1) {
      this._customizer.refreshDDsIfNeeded(var1);
   }

   public void setHighestNumUnavailable(int var1) throws ManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("HighestNumUnavailable", (long)var1, 0L, 2147483647L);
      this._HighestNumUnavailable = var1;
   }

   public int getInactiveConnectionTimeoutSeconds() throws ManagementException {
      return this._InactiveConnectionTimeoutSeconds;
   }

   public boolean isInactiveConnectionTimeoutSecondsSet() {
      return this._isSet(17);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setInactiveConnectionTimeoutSeconds(int var1) throws ManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("InactiveConnectionTimeoutSeconds", (long)var1, 0L, 2147483647L);
      this._InactiveConnectionTimeoutSeconds = var1;
   }

   public int getConnectionCreationRetryFrequencySeconds() throws ManagementException {
      return this._ConnectionCreationRetryFrequencySeconds;
   }

   public boolean isConnectionCreationRetryFrequencySecondsSet() {
      return this._isSet(18);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setConnectionCreationRetryFrequencySeconds(int var1) throws ManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ConnectionCreationRetryFrequencySeconds", (long)var1, 600L, 2147483647L);
      this._ConnectionCreationRetryFrequencySeconds = var1;
   }

   public int getConnectionReserveTimeoutSeconds() throws ManagementException {
      return this._ConnectionReserveTimeoutSeconds;
   }

   public boolean isConnectionReserveTimeoutSecondsSet() {
      return this._isSet(19);
   }

   public void setConnectionReserveTimeoutSeconds(int var1) throws ManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ConnectionReserveTimeoutSeconds", (long)var1, -1L, 2147483647L);
      this._ConnectionReserveTimeoutSeconds = var1;
   }

   public int getShrinkFrequencySeconds() throws ManagementException {
      return this._ShrinkFrequencySeconds;
   }

   public boolean isShrinkFrequencySecondsSet() {
      return this._isSet(20);
   }

   public void setShrinkFrequencySeconds(int var1) throws ManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ShrinkFrequencySeconds", (long)var1, 0L, 2147483647L);
      this._ShrinkFrequencySeconds = var1;
   }

   public int getTestFrequencySeconds() throws ManagementException {
      return this._TestFrequencySeconds;
   }

   public boolean isTestFrequencySecondsSet() {
      return this._isSet(21);
   }

   public void setTestFrequencySeconds(int var1) throws ManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("TestFrequencySeconds", (long)var1, 0L, 2147483647L);
      this._TestFrequencySeconds = var1;
   }

   public int getCacheSize() throws ManagementException {
      return this._CacheSize;
   }

   public boolean isCacheSizeSet() {
      return this._isSet(22);
   }

   public void setCacheSize(int var1) throws ManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CacheSize", (long)var1, 0L, 1024L);
      this._CacheSize = var1;
   }

   public boolean isShrinkingEnabled() throws ManagementException {
      return this._ShrinkingEnabled;
   }

   public boolean isShrinkingEnabledSet() {
      return this._isSet(23);
   }

   public void setShrinkingEnabled(boolean var1) throws ManagementException {
      this._ShrinkingEnabled = var1;
   }

   public boolean isCheckOnReserveEnabled() throws ManagementException {
      return this._CheckOnReserveEnabled;
   }

   public boolean isCheckOnReserveEnabledSet() {
      return this._isSet(24);
   }

   public void setCheckOnReserveEnabled(boolean var1) throws ManagementException {
      this._CheckOnReserveEnabled = var1;
   }

   public boolean isCheckOnReleaseEnabled() throws ManagementException {
      return this._CheckOnReleaseEnabled;
   }

   public boolean isCheckOnReleaseEnabledSet() {
      return this._isSet(25);
   }

   public void setCheckOnReleaseEnabled(boolean var1) throws ManagementException {
      this._CheckOnReleaseEnabled = var1;
   }

   public boolean isCheckOnCreateEnabled() throws ManagementException {
      return this._CheckOnCreateEnabled;
   }

   public boolean isCheckOnCreateEnabledSet() {
      return this._isSet(26);
   }

   public void setCheckOnCreateEnabled(boolean var1) throws ManagementException {
      this._CheckOnCreateEnabled = var1;
   }

   public int getMaxIdleTime() throws ManagementException {
      return this._MaxIdleTime;
   }

   public boolean isMaxIdleTimeSet() {
      return this._isSet(27);
   }

   public void setMaxIdleTime(int var1) throws ManagementException {
      this._MaxIdleTime = var1;
   }

   public boolean isProfilingEnabled() throws ManagementException {
      return this._ProfilingEnabled;
   }

   public boolean isProfilingEnabledSet() {
      return this._isSet(28);
   }

   public void setProfilingEnabled(boolean var1) throws ManagementException {
      this._ProfilingEnabled = var1;
   }

   public boolean isLoggingEnabled() throws ManagementException {
      return this._LoggingEnabled;
   }

   public boolean isLoggingEnabledSet() {
      return this._isSet(29);
   }

   public void setLoggingEnabled(boolean var1) throws ManagementException {
      this._LoggingEnabled = var1;
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
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
         var1 = 11;
      }

      try {
         switch (var1) {
            case 11:
               this._ActivatedTargets = new TargetMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._customizer.setApplication((ApplicationMBean)null);
               if (var2) {
                  break;
               }
            case 22:
               this._CacheSize = 10;
               if (var2) {
                  break;
               }
            case 14:
               this._CapacityIncrement = 1;
               if (var2) {
                  break;
               }
            case 18:
               this._ConnectionCreationRetryFrequencySeconds = 3600;
               if (var2) {
                  break;
               }
            case 19:
               this._ConnectionReserveTimeoutSeconds = 10;
               if (var2) {
                  break;
               }
            case 16:
               this._HighestNumUnavailable = 0;
               if (var2) {
                  break;
               }
            case 15:
               this._HighestNumWaiters = Integer.MAX_VALUE;
               if (var2) {
                  break;
               }
            case 17:
               this._InactiveConnectionTimeoutSeconds = 0;
               if (var2) {
                  break;
               }
            case 12:
               this._InitialCapacity = 1;
               if (var2) {
                  break;
               }
            case 13:
               this._MaxCapacity = 15;
               if (var2) {
                  break;
               }
            case 27:
               this._MaxIdleTime = -1;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 20:
               this._ShrinkFrequencySeconds = 900;
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setTargets(new TargetMBean[0]);
               if (var2) {
                  break;
               }
            case 21:
               this._TestFrequencySeconds = 0;
               if (var2) {
                  break;
               }
            case 26:
               this._CheckOnCreateEnabled = false;
               if (var2) {
                  break;
               }
            case 25:
               this._CheckOnReleaseEnabled = false;
               if (var2) {
                  break;
               }
            case 24:
               this._CheckOnReserveEnabled = false;
               if (var2) {
                  break;
               }
            case 29:
               this._LoggingEnabled = false;
               if (var2) {
                  break;
               }
            case 28:
               this._ProfilingEnabled = false;
               if (var2) {
                  break;
               }
            case 23:
               this._ShrinkingEnabled = true;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            case 10:
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
      return "JDBCPoolComponent";
   }

   public void putValue(String var1, Object var2) {
      TargetMBean[] var5;
      if (var1.equals("ActivatedTargets")) {
         var5 = this._ActivatedTargets;
         this._ActivatedTargets = (TargetMBean[])((TargetMBean[])var2);
         this._postSet(11, var5, this._ActivatedTargets);
      } else if (var1.equals("Application")) {
         ApplicationMBean var8 = this._Application;
         this._Application = (ApplicationMBean)var2;
         this._postSet(9, var8, this._Application);
      } else {
         int var4;
         if (var1.equals("CacheSize")) {
            var4 = this._CacheSize;
            this._CacheSize = (Integer)var2;
            this._postSet(22, var4, this._CacheSize);
         } else if (var1.equals("CapacityIncrement")) {
            var4 = this._CapacityIncrement;
            this._CapacityIncrement = (Integer)var2;
            this._postSet(14, var4, this._CapacityIncrement);
         } else {
            boolean var6;
            if (var1.equals("CheckOnCreateEnabled")) {
               var6 = this._CheckOnCreateEnabled;
               this._CheckOnCreateEnabled = (Boolean)var2;
               this._postSet(26, var6, this._CheckOnCreateEnabled);
            } else if (var1.equals("CheckOnReleaseEnabled")) {
               var6 = this._CheckOnReleaseEnabled;
               this._CheckOnReleaseEnabled = (Boolean)var2;
               this._postSet(25, var6, this._CheckOnReleaseEnabled);
            } else if (var1.equals("CheckOnReserveEnabled")) {
               var6 = this._CheckOnReserveEnabled;
               this._CheckOnReserveEnabled = (Boolean)var2;
               this._postSet(24, var6, this._CheckOnReserveEnabled);
            } else if (var1.equals("ConnectionCreationRetryFrequencySeconds")) {
               var4 = this._ConnectionCreationRetryFrequencySeconds;
               this._ConnectionCreationRetryFrequencySeconds = (Integer)var2;
               this._postSet(18, var4, this._ConnectionCreationRetryFrequencySeconds);
            } else if (var1.equals("ConnectionReserveTimeoutSeconds")) {
               var4 = this._ConnectionReserveTimeoutSeconds;
               this._ConnectionReserveTimeoutSeconds = (Integer)var2;
               this._postSet(19, var4, this._ConnectionReserveTimeoutSeconds);
            } else if (var1.equals("HighestNumUnavailable")) {
               var4 = this._HighestNumUnavailable;
               this._HighestNumUnavailable = (Integer)var2;
               this._postSet(16, var4, this._HighestNumUnavailable);
            } else if (var1.equals("HighestNumWaiters")) {
               var4 = this._HighestNumWaiters;
               this._HighestNumWaiters = (Integer)var2;
               this._postSet(15, var4, this._HighestNumWaiters);
            } else if (var1.equals("InactiveConnectionTimeoutSeconds")) {
               var4 = this._InactiveConnectionTimeoutSeconds;
               this._InactiveConnectionTimeoutSeconds = (Integer)var2;
               this._postSet(17, var4, this._InactiveConnectionTimeoutSeconds);
            } else if (var1.equals("InitialCapacity")) {
               var4 = this._InitialCapacity;
               this._InitialCapacity = (Integer)var2;
               this._postSet(12, var4, this._InitialCapacity);
            } else if (var1.equals("LoggingEnabled")) {
               var6 = this._LoggingEnabled;
               this._LoggingEnabled = (Boolean)var2;
               this._postSet(29, var6, this._LoggingEnabled);
            } else if (var1.equals("MaxCapacity")) {
               var4 = this._MaxCapacity;
               this._MaxCapacity = (Integer)var2;
               this._postSet(13, var4, this._MaxCapacity);
            } else if (var1.equals("MaxIdleTime")) {
               var4 = this._MaxIdleTime;
               this._MaxIdleTime = (Integer)var2;
               this._postSet(27, var4, this._MaxIdleTime);
            } else if (var1.equals("Name")) {
               String var7 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var7, this._Name);
            } else if (var1.equals("ProfilingEnabled")) {
               var6 = this._ProfilingEnabled;
               this._ProfilingEnabled = (Boolean)var2;
               this._postSet(28, var6, this._ProfilingEnabled);
            } else if (var1.equals("ShrinkFrequencySeconds")) {
               var4 = this._ShrinkFrequencySeconds;
               this._ShrinkFrequencySeconds = (Integer)var2;
               this._postSet(20, var4, this._ShrinkFrequencySeconds);
            } else if (var1.equals("ShrinkingEnabled")) {
               var6 = this._ShrinkingEnabled;
               this._ShrinkingEnabled = (Boolean)var2;
               this._postSet(23, var6, this._ShrinkingEnabled);
            } else if (var1.equals("Targets")) {
               var5 = this._Targets;
               this._Targets = (TargetMBean[])((TargetMBean[])var2);
               this._postSet(7, var5, this._Targets);
            } else if (var1.equals("TestFrequencySeconds")) {
               var4 = this._TestFrequencySeconds;
               this._TestFrequencySeconds = (Integer)var2;
               this._postSet(21, var4, this._TestFrequencySeconds);
            } else if (var1.equals("customizer")) {
               JDBCPoolComponent var3 = this._customizer;
               this._customizer = (JDBCPoolComponent)var2;
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ActivatedTargets")) {
         return this._ActivatedTargets;
      } else if (var1.equals("Application")) {
         return this._Application;
      } else if (var1.equals("CacheSize")) {
         return new Integer(this._CacheSize);
      } else if (var1.equals("CapacityIncrement")) {
         return new Integer(this._CapacityIncrement);
      } else if (var1.equals("CheckOnCreateEnabled")) {
         return new Boolean(this._CheckOnCreateEnabled);
      } else if (var1.equals("CheckOnReleaseEnabled")) {
         return new Boolean(this._CheckOnReleaseEnabled);
      } else if (var1.equals("CheckOnReserveEnabled")) {
         return new Boolean(this._CheckOnReserveEnabled);
      } else if (var1.equals("ConnectionCreationRetryFrequencySeconds")) {
         return new Integer(this._ConnectionCreationRetryFrequencySeconds);
      } else if (var1.equals("ConnectionReserveTimeoutSeconds")) {
         return new Integer(this._ConnectionReserveTimeoutSeconds);
      } else if (var1.equals("HighestNumUnavailable")) {
         return new Integer(this._HighestNumUnavailable);
      } else if (var1.equals("HighestNumWaiters")) {
         return new Integer(this._HighestNumWaiters);
      } else if (var1.equals("InactiveConnectionTimeoutSeconds")) {
         return new Integer(this._InactiveConnectionTimeoutSeconds);
      } else if (var1.equals("InitialCapacity")) {
         return new Integer(this._InitialCapacity);
      } else if (var1.equals("LoggingEnabled")) {
         return new Boolean(this._LoggingEnabled);
      } else if (var1.equals("MaxCapacity")) {
         return new Integer(this._MaxCapacity);
      } else if (var1.equals("MaxIdleTime")) {
         return new Integer(this._MaxIdleTime);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("ProfilingEnabled")) {
         return new Boolean(this._ProfilingEnabled);
      } else if (var1.equals("ShrinkFrequencySeconds")) {
         return new Integer(this._ShrinkFrequencySeconds);
      } else if (var1.equals("ShrinkingEnabled")) {
         return new Boolean(this._ShrinkingEnabled);
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else if (var1.equals("TestFrequencySeconds")) {
         return new Integer(this._TestFrequencySeconds);
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ComponentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 7:
            case 8:
            case 9:
            case 14:
            case 20:
            case 21:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            default:
               break;
            case 6:
               if (var1.equals("target")) {
                  return 7;
               }
               break;
            case 10:
               if (var1.equals("cache-size")) {
                  return 22;
               }
               break;
            case 11:
               if (var1.equals("application")) {
                  return 9;
               }
               break;
            case 12:
               if (var1.equals("max-capacity")) {
                  return 13;
               }
               break;
            case 13:
               if (var1.equals("max-idle-time")) {
                  return 27;
               }
               break;
            case 15:
               if (var1.equals("logging-enabled")) {
                  return 29;
               }
               break;
            case 16:
               if (var1.equals("activated-target")) {
                  return 11;
               }

               if (var1.equals("initial-capacity")) {
                  return 12;
               }
               break;
            case 17:
               if (var1.equals("profiling-enabled")) {
                  return 28;
               }

               if (var1.equals("shrinking-enabled")) {
                  return 23;
               }
               break;
            case 18:
               if (var1.equals("capacity-increment")) {
                  return 14;
               }
               break;
            case 19:
               if (var1.equals("highest-num-waiters")) {
                  return 15;
               }
               break;
            case 22:
               if (var1.equals("test-frequency-seconds")) {
                  return 21;
               }
               break;
            case 23:
               if (var1.equals("highest-num-unavailable")) {
                  return 16;
               }

               if (var1.equals("check-on-create-enabled")) {
                  return 26;
               }
               break;
            case 24:
               if (var1.equals("shrink-frequency-seconds")) {
                  return 20;
               }

               if (var1.equals("check-on-release-enabled")) {
                  return 25;
               }

               if (var1.equals("check-on-reserve-enabled")) {
                  return 24;
               }
               break;
            case 34:
               if (var1.equals("connection-reserve-timeout-seconds")) {
                  return 19;
               }
               break;
            case 35:
               if (var1.equals("inactive-connection-timeout-seconds")) {
                  return 17;
               }
               break;
            case 43:
               if (var1.equals("connection-creation-retry-frequency-seconds")) {
                  return 18;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
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
            case 8:
            case 10:
            default:
               return super.getElementName(var1);
            case 7:
               return "target";
            case 9:
               return "application";
            case 11:
               return "activated-target";
            case 12:
               return "initial-capacity";
            case 13:
               return "max-capacity";
            case 14:
               return "capacity-increment";
            case 15:
               return "highest-num-waiters";
            case 16:
               return "highest-num-unavailable";
            case 17:
               return "inactive-connection-timeout-seconds";
            case 18:
               return "connection-creation-retry-frequency-seconds";
            case 19:
               return "connection-reserve-timeout-seconds";
            case 20:
               return "shrink-frequency-seconds";
            case 21:
               return "test-frequency-seconds";
            case 22:
               return "cache-size";
            case 23:
               return "shrinking-enabled";
            case 24:
               return "check-on-reserve-enabled";
            case 25:
               return "check-on-release-enabled";
            case 26:
               return "check-on-create-enabled";
            case 27:
               return "max-idle-time";
            case 28:
               return "profiling-enabled";
            case 29:
               return "logging-enabled";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 11:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
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

   protected static class Helper extends ComponentMBeanImpl.Helper {
      private JDBCPoolComponentMBeanImpl bean;

      protected Helper(JDBCPoolComponentMBeanImpl var1) {
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
            case 8:
            case 10:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Targets";
            case 9:
               return "Application";
            case 11:
               return "ActivatedTargets";
            case 12:
               return "InitialCapacity";
            case 13:
               return "MaxCapacity";
            case 14:
               return "CapacityIncrement";
            case 15:
               return "HighestNumWaiters";
            case 16:
               return "HighestNumUnavailable";
            case 17:
               return "InactiveConnectionTimeoutSeconds";
            case 18:
               return "ConnectionCreationRetryFrequencySeconds";
            case 19:
               return "ConnectionReserveTimeoutSeconds";
            case 20:
               return "ShrinkFrequencySeconds";
            case 21:
               return "TestFrequencySeconds";
            case 22:
               return "CacheSize";
            case 23:
               return "ShrinkingEnabled";
            case 24:
               return "CheckOnReserveEnabled";
            case 25:
               return "CheckOnReleaseEnabled";
            case 26:
               return "CheckOnCreateEnabled";
            case 27:
               return "MaxIdleTime";
            case 28:
               return "ProfilingEnabled";
            case 29:
               return "LoggingEnabled";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ActivatedTargets")) {
            return 11;
         } else if (var1.equals("Application")) {
            return 9;
         } else if (var1.equals("CacheSize")) {
            return 22;
         } else if (var1.equals("CapacityIncrement")) {
            return 14;
         } else if (var1.equals("ConnectionCreationRetryFrequencySeconds")) {
            return 18;
         } else if (var1.equals("ConnectionReserveTimeoutSeconds")) {
            return 19;
         } else if (var1.equals("HighestNumUnavailable")) {
            return 16;
         } else if (var1.equals("HighestNumWaiters")) {
            return 15;
         } else if (var1.equals("InactiveConnectionTimeoutSeconds")) {
            return 17;
         } else if (var1.equals("InitialCapacity")) {
            return 12;
         } else if (var1.equals("MaxCapacity")) {
            return 13;
         } else if (var1.equals("MaxIdleTime")) {
            return 27;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("ShrinkFrequencySeconds")) {
            return 20;
         } else if (var1.equals("Targets")) {
            return 7;
         } else if (var1.equals("TestFrequencySeconds")) {
            return 21;
         } else if (var1.equals("CheckOnCreateEnabled")) {
            return 26;
         } else if (var1.equals("CheckOnReleaseEnabled")) {
            return 25;
         } else if (var1.equals("CheckOnReserveEnabled")) {
            return 24;
         } else if (var1.equals("LoggingEnabled")) {
            return 29;
         } else if (var1.equals("ProfilingEnabled")) {
            return 28;
         } else {
            return var1.equals("ShrinkingEnabled") ? 23 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
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
            if (this.bean.isActivatedTargetsSet()) {
               var2.append("ActivatedTargets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getActivatedTargets())));
            }

            if (this.bean.isApplicationSet()) {
               var2.append("Application");
               var2.append(String.valueOf(this.bean.getApplication()));
            }

            if (this.bean.isCacheSizeSet()) {
               var2.append("CacheSize");
               var2.append(String.valueOf(this.bean.getCacheSize()));
            }

            if (this.bean.isCapacityIncrementSet()) {
               var2.append("CapacityIncrement");
               var2.append(String.valueOf(this.bean.getCapacityIncrement()));
            }

            if (this.bean.isConnectionCreationRetryFrequencySecondsSet()) {
               var2.append("ConnectionCreationRetryFrequencySeconds");
               var2.append(String.valueOf(this.bean.getConnectionCreationRetryFrequencySeconds()));
            }

            if (this.bean.isConnectionReserveTimeoutSecondsSet()) {
               var2.append("ConnectionReserveTimeoutSeconds");
               var2.append(String.valueOf(this.bean.getConnectionReserveTimeoutSeconds()));
            }

            if (this.bean.isHighestNumUnavailableSet()) {
               var2.append("HighestNumUnavailable");
               var2.append(String.valueOf(this.bean.getHighestNumUnavailable()));
            }

            if (this.bean.isHighestNumWaitersSet()) {
               var2.append("HighestNumWaiters");
               var2.append(String.valueOf(this.bean.getHighestNumWaiters()));
            }

            if (this.bean.isInactiveConnectionTimeoutSecondsSet()) {
               var2.append("InactiveConnectionTimeoutSeconds");
               var2.append(String.valueOf(this.bean.getInactiveConnectionTimeoutSeconds()));
            }

            if (this.bean.isInitialCapacitySet()) {
               var2.append("InitialCapacity");
               var2.append(String.valueOf(this.bean.getInitialCapacity()));
            }

            if (this.bean.isMaxCapacitySet()) {
               var2.append("MaxCapacity");
               var2.append(String.valueOf(this.bean.getMaxCapacity()));
            }

            if (this.bean.isMaxIdleTimeSet()) {
               var2.append("MaxIdleTime");
               var2.append(String.valueOf(this.bean.getMaxIdleTime()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isShrinkFrequencySecondsSet()) {
               var2.append("ShrinkFrequencySeconds");
               var2.append(String.valueOf(this.bean.getShrinkFrequencySeconds()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            if (this.bean.isTestFrequencySecondsSet()) {
               var2.append("TestFrequencySeconds");
               var2.append(String.valueOf(this.bean.getTestFrequencySeconds()));
            }

            if (this.bean.isCheckOnCreateEnabledSet()) {
               var2.append("CheckOnCreateEnabled");
               var2.append(String.valueOf(this.bean.isCheckOnCreateEnabled()));
            }

            if (this.bean.isCheckOnReleaseEnabledSet()) {
               var2.append("CheckOnReleaseEnabled");
               var2.append(String.valueOf(this.bean.isCheckOnReleaseEnabled()));
            }

            if (this.bean.isCheckOnReserveEnabledSet()) {
               var2.append("CheckOnReserveEnabled");
               var2.append(String.valueOf(this.bean.isCheckOnReserveEnabled()));
            }

            if (this.bean.isLoggingEnabledSet()) {
               var2.append("LoggingEnabled");
               var2.append(String.valueOf(this.bean.isLoggingEnabled()));
            }

            if (this.bean.isProfilingEnabledSet()) {
               var2.append("ProfilingEnabled");
               var2.append(String.valueOf(this.bean.isProfilingEnabled()));
            }

            if (this.bean.isShrinkingEnabledSet()) {
               var2.append("ShrinkingEnabled");
               var2.append(String.valueOf(this.bean.isShrinkingEnabled()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            JDBCPoolComponentMBeanImpl var2 = (JDBCPoolComponentMBeanImpl)var1;
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JDBCPoolComponentMBeanImpl var3 = (JDBCPoolComponentMBeanImpl)var1.getSourceBean();
            JDBCPoolComponentMBeanImpl var4 = (JDBCPoolComponentMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("ActivatedTargets") && !var5.equals("Application") && !var5.equals("CacheSize") && !var5.equals("CapacityIncrement") && !var5.equals("ConnectionCreationRetryFrequencySeconds") && !var5.equals("ConnectionReserveTimeoutSeconds") && !var5.equals("HighestNumUnavailable") && !var5.equals("HighestNumWaiters") && !var5.equals("InactiveConnectionTimeoutSeconds") && !var5.equals("InitialCapacity") && !var5.equals("MaxCapacity") && !var5.equals("MaxIdleTime")) {
                  if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (!var5.equals("ShrinkFrequencySeconds")) {
                     if (var5.equals("Targets")) {
                        var3.setTargetsAsString(var4.getTargetsAsString());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                     } else if (!var5.equals("TestFrequencySeconds") && !var5.equals("CheckOnCreateEnabled") && !var5.equals("CheckOnReleaseEnabled") && !var5.equals("CheckOnReserveEnabled") && !var5.equals("LoggingEnabled") && !var5.equals("ProfilingEnabled") && !var5.equals("ShrinkingEnabled")) {
                        super.applyPropertyUpdate(var1, var2);
                     }
                  }
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
            JDBCPoolComponentMBeanImpl var5 = (JDBCPoolComponentMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 7);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
            }

            return var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getActivatedTargets(), var1, var2);
         this.inferSubTree(this.bean.getApplication(), var1, var2);
         this.inferSubTree(this.bean.getTargets(), var1, var2);
      }
   }
}
