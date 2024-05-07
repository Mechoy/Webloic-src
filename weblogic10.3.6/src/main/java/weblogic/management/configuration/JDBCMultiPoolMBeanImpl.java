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
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JDBCMultiPool;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class JDBCMultiPoolMBeanImpl extends DeploymentMBeanImpl implements JDBCMultiPoolMBean, Serializable {
   private String _ACLName;
   private String _AlgorithmType;
   private String _ConnectionPoolFailoverCallbackHandler;
   private boolean _FailoverRequestIfBusy;
   private int _HealthCheckFrequencySeconds;
   private boolean _HighAvail;
   private JDBCSystemResourceMBean _JDBCSystemResource;
   private boolean _LoadBalance;
   private String _Name;
   private JDBCConnectionPoolMBean[] _PoolList;
   private TargetMBean[] _Targets;
   private JDBCMultiPool _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JDBCMultiPoolMBeanImpl() {
      try {
         this._customizer = new JDBCMultiPool(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JDBCMultiPoolMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JDBCMultiPool(this);
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

   public TargetMBean[] getTargets() {
      return this._customizer.getTargets();
   }

   public String getTargetsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargets());
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isTargetsSet() {
      return this._isSet(7);
   }

   public void setJDBCSystemResource(JDBCSystemResourceMBean var1) {
      JDBCSystemResourceMBean var2 = this.getJDBCSystemResource();
      this._customizer.setJDBCSystemResource(var1);
      this._postSet(9, var2, var1);
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
                        JDBCMultiPoolMBeanImpl.this.addTarget((TargetMBean)var1);
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

   public JDBCSystemResourceMBean getJDBCSystemResource() {
      return this._customizer.getJDBCSystemResource();
   }

   public String getJDBCSystemResourceAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getJDBCSystemResource();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isJDBCSystemResourceSet() {
      return this._isSet(9);
   }

   public void setJDBCSystemResourceAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JDBCSystemResourceMBean.class, new ReferenceManager.Resolver(this, 9) {
            public void resolveReference(Object var1) {
               try {
                  JDBCMultiPoolMBeanImpl.this.setJDBCSystemResource((JDBCSystemResourceMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JDBCSystemResourceMBean var2 = this._JDBCSystemResource;
         this._initializeProperty(9);
         this._postSet(9, var2, this._JDBCSystemResource);
      }

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
                  return JDBCMultiPoolMBeanImpl.this.getTargets();
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

   public String getACLName() {
      return this._ACLName;
   }

   public boolean isACLNameSet() {
      return this._isSet(10);
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

   public void setACLName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ACLName;
      this._ACLName = var1;
      this._postSet(10, var2, var1);
   }

   public void addPool(JDBCConnectionPoolMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 11)) {
         JDBCConnectionPoolMBean[] var2 = (JDBCConnectionPoolMBean[])((JDBCConnectionPoolMBean[])this._getHelper()._extendArray(this.getPoolList(), JDBCConnectionPoolMBean.class, var1));

         try {
            this.setPoolList(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JDBCConnectionPoolMBean[] getPoolList() {
      return this._customizer.getPoolList();
   }

   public String getPoolListAsString() {
      return this._getHelper()._serializeKeyList(this.getPoolList());
   }

   public boolean isPoolListSet() {
      return this._isSet(11);
   }

   public void removePool(JDBCConnectionPoolMBean var1) {
      JDBCConnectionPoolMBean[] var2 = this.getPoolList();
      JDBCConnectionPoolMBean[] var3 = (JDBCConnectionPoolMBean[])((JDBCConnectionPoolMBean[])this._getHelper()._removeElement(var2, JDBCConnectionPoolMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setPoolList(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setPoolListAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._PoolList);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, JDBCConnectionPoolMBean.class, new ReferenceManager.Resolver(this, 11) {
                  public void resolveReference(Object var1) {
                     try {
                        JDBCMultiPoolMBeanImpl.this.addPool((JDBCConnectionPoolMBean)var1);
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
               JDBCConnectionPoolMBean[] var6 = this._PoolList;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  JDBCConnectionPoolMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removePool(var9);
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
         JDBCConnectionPoolMBean[] var2 = this._PoolList;
         this._initializeProperty(11);
         this._postSet(11, var2, this._PoolList);
      }
   }

   public void setPoolList(JDBCConnectionPoolMBean[] var1) throws InvalidAttributeValueException {
      Object var3 = var1 == null ? new JDBCConnectionPoolMBeanImpl[0] : var1;
      JDBCConnectionPoolMBean[] var2 = this.getPoolList();
      this._customizer.setPoolList((JDBCConnectionPoolMBean[])var3);
      this._postSet(11, var2, var3);
   }

   public void setLoadBalance(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._LoadBalance;
      this._LoadBalance = var1;
      this._postSet(12, var2, var1);
   }

   public boolean isLoadBalance() {
      return this._LoadBalance;
   }

   public boolean isLoadBalanceSet() {
      return this._isSet(12);
   }

   public void setHighAvail(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._HighAvail;
      this._HighAvail = var1;
      this._postSet(13, var2, var1);
   }

   public boolean isHighAvail() {
      return this._HighAvail;
   }

   public boolean isHighAvailSet() {
      return this._isSet(13);
   }

   public void setAlgorithmType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"High-Availability", "Load-Balancing", "Failover"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("AlgorithmType", var1, var2);
      String var3 = this.getAlgorithmType();

      try {
         this._customizer.setAlgorithmType(var1);
      } catch (InvalidAttributeValueException var5) {
         throw new UndeclaredThrowableException(var5);
      }

      this._postSet(14, var3, var1);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public String getAlgorithmType() {
      return this._customizer.getAlgorithmType();
   }

   public boolean isAlgorithmTypeSet() {
      return this._isSet(14);
   }

   public String getConnectionPoolFailoverCallbackHandler() {
      return this._customizer.getConnectionPoolFailoverCallbackHandler();
   }

   public boolean isConnectionPoolFailoverCallbackHandlerSet() {
      return this._isSet(15);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setConnectionPoolFailoverCallbackHandler(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getConnectionPoolFailoverCallbackHandler();
      this._customizer.setConnectionPoolFailoverCallbackHandler(var1);
      this._postSet(15, var2, var1);
   }

   public void setFailoverRequestIfBusy(boolean var1) {
      boolean var2 = this.getFailoverRequestIfBusy();
      this._customizer.setFailoverRequestIfBusy(var1);
      this._postSet(16, var2, var1);
   }

   public boolean getFailoverRequestIfBusy() {
      return this._customizer.getFailoverRequestIfBusy();
   }

   public boolean isFailoverRequestIfBusySet() {
      return this._isSet(16);
   }

   public void setHealthCheckFrequencySeconds(int var1) {
      int var2 = this.getHealthCheckFrequencySeconds();
      this._customizer.setHealthCheckFrequencySeconds(var1);
      this._postSet(17, var2, var1);
   }

   public int getHealthCheckFrequencySeconds() {
      return this._customizer.getHealthCheckFrequencySeconds();
   }

   public boolean isHealthCheckFrequencySecondsSet() {
      return this._isSet(17);
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
         var1 = 10;
      }

      try {
         switch (var1) {
            case 10:
               this._ACLName = null;
               if (var2) {
                  break;
               }
            case 14:
               this._customizer.setAlgorithmType("High-Availability");
               if (var2) {
                  break;
               }
            case 15:
               this._customizer.setConnectionPoolFailoverCallbackHandler((String)null);
               if (var2) {
                  break;
               }
            case 16:
               this._customizer.setFailoverRequestIfBusy(false);
               if (var2) {
                  break;
               }
            case 17:
               this._customizer.setHealthCheckFrequencySeconds(300);
               if (var2) {
                  break;
               }
            case 9:
               this._customizer.setJDBCSystemResource((JDBCSystemResourceMBean)null);
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 11:
               this._customizer.setPoolList(new JDBCConnectionPoolMBean[0]);
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setTargets(new TargetMBean[0]);
               if (var2) {
                  break;
               }
            case 13:
               this._HighAvail = false;
               if (var2) {
                  break;
               }
            case 12:
               this._LoadBalance = false;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
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
      return "JDBCMultiPool";
   }

   public void putValue(String var1, Object var2) {
      String var6;
      if (var1.equals("ACLName")) {
         var6 = this._ACLName;
         this._ACLName = (String)var2;
         this._postSet(10, var6, this._ACLName);
      } else if (var1.equals("AlgorithmType")) {
         var6 = this._AlgorithmType;
         this._AlgorithmType = (String)var2;
         this._postSet(14, var6, this._AlgorithmType);
      } else if (var1.equals("ConnectionPoolFailoverCallbackHandler")) {
         var6 = this._ConnectionPoolFailoverCallbackHandler;
         this._ConnectionPoolFailoverCallbackHandler = (String)var2;
         this._postSet(15, var6, this._ConnectionPoolFailoverCallbackHandler);
      } else {
         boolean var7;
         if (var1.equals("FailoverRequestIfBusy")) {
            var7 = this._FailoverRequestIfBusy;
            this._FailoverRequestIfBusy = (Boolean)var2;
            this._postSet(16, var7, this._FailoverRequestIfBusy);
         } else if (var1.equals("HealthCheckFrequencySeconds")) {
            int var9 = this._HealthCheckFrequencySeconds;
            this._HealthCheckFrequencySeconds = (Integer)var2;
            this._postSet(17, var9, this._HealthCheckFrequencySeconds);
         } else if (var1.equals("HighAvail")) {
            var7 = this._HighAvail;
            this._HighAvail = (Boolean)var2;
            this._postSet(13, var7, this._HighAvail);
         } else if (var1.equals("JDBCSystemResource")) {
            JDBCSystemResourceMBean var8 = this._JDBCSystemResource;
            this._JDBCSystemResource = (JDBCSystemResourceMBean)var2;
            this._postSet(9, var8, this._JDBCSystemResource);
         } else if (var1.equals("LoadBalance")) {
            var7 = this._LoadBalance;
            this._LoadBalance = (Boolean)var2;
            this._postSet(12, var7, this._LoadBalance);
         } else if (var1.equals("Name")) {
            var6 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var6, this._Name);
         } else if (var1.equals("PoolList")) {
            JDBCConnectionPoolMBean[] var5 = this._PoolList;
            this._PoolList = (JDBCConnectionPoolMBean[])((JDBCConnectionPoolMBean[])var2);
            this._postSet(11, var5, this._PoolList);
         } else if (var1.equals("Targets")) {
            TargetMBean[] var4 = this._Targets;
            this._Targets = (TargetMBean[])((TargetMBean[])var2);
            this._postSet(7, var4, this._Targets);
         } else if (var1.equals("customizer")) {
            JDBCMultiPool var3 = this._customizer;
            this._customizer = (JDBCMultiPool)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ACLName")) {
         return this._ACLName;
      } else if (var1.equals("AlgorithmType")) {
         return this._AlgorithmType;
      } else if (var1.equals("ConnectionPoolFailoverCallbackHandler")) {
         return this._ConnectionPoolFailoverCallbackHandler;
      } else if (var1.equals("FailoverRequestIfBusy")) {
         return new Boolean(this._FailoverRequestIfBusy);
      } else if (var1.equals("HealthCheckFrequencySeconds")) {
         return new Integer(this._HealthCheckFrequencySeconds);
      } else if (var1.equals("HighAvail")) {
         return new Boolean(this._HighAvail);
      } else if (var1.equals("JDBCSystemResource")) {
         return this._JDBCSystemResource;
      } else if (var1.equals("LoadBalance")) {
         return new Boolean(this._LoadBalance);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("PoolList")) {
         return this._PoolList;
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 7:
            case 11:
            case 13:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            default:
               break;
            case 6:
               if (var1.equals("target")) {
                  return 7;
               }
               break;
            case 8:
               if (var1.equals("acl-name")) {
                  return 10;
               }
               break;
            case 9:
               if (var1.equals("pool-list")) {
                  return 11;
               }
               break;
            case 10:
               if (var1.equals("high-avail")) {
                  return 13;
               }
               break;
            case 12:
               if (var1.equals("load-balance")) {
                  return 12;
               }
               break;
            case 14:
               if (var1.equals("algorithm-type")) {
                  return 14;
               }
               break;
            case 20:
               if (var1.equals("jdbc-system-resource")) {
                  return 9;
               }
               break;
            case 24:
               if (var1.equals("failover-request-if-busy")) {
                  return 16;
               }
               break;
            case 30:
               if (var1.equals("health-check-frequency-seconds")) {
                  return 17;
               }
               break;
            case 41:
               if (var1.equals("connection-pool-failover-callback-handler")) {
                  return 15;
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
            default:
               return super.getElementName(var1);
            case 7:
               return "target";
            case 9:
               return "jdbc-system-resource";
            case 10:
               return "acl-name";
            case 11:
               return "pool-list";
            case 12:
               return "load-balance";
            case 13:
               return "high-avail";
            case 14:
               return "algorithm-type";
            case 15:
               return "connection-pool-failover-callback-handler";
            case 16:
               return "failover-request-if-busy";
            case 17:
               return "health-check-frequency-seconds";
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

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private JDBCMultiPoolMBeanImpl bean;

      protected Helper(JDBCMultiPoolMBeanImpl var1) {
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
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Targets";
            case 9:
               return "JDBCSystemResource";
            case 10:
               return "ACLName";
            case 11:
               return "PoolList";
            case 12:
               return "LoadBalance";
            case 13:
               return "HighAvail";
            case 14:
               return "AlgorithmType";
            case 15:
               return "ConnectionPoolFailoverCallbackHandler";
            case 16:
               return "FailoverRequestIfBusy";
            case 17:
               return "HealthCheckFrequencySeconds";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ACLName")) {
            return 10;
         } else if (var1.equals("AlgorithmType")) {
            return 14;
         } else if (var1.equals("ConnectionPoolFailoverCallbackHandler")) {
            return 15;
         } else if (var1.equals("FailoverRequestIfBusy")) {
            return 16;
         } else if (var1.equals("HealthCheckFrequencySeconds")) {
            return 17;
         } else if (var1.equals("JDBCSystemResource")) {
            return 9;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("PoolList")) {
            return 11;
         } else if (var1.equals("Targets")) {
            return 7;
         } else if (var1.equals("HighAvail")) {
            return 13;
         } else {
            return var1.equals("LoadBalance") ? 12 : super.getPropertyIndex(var1);
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
            if (this.bean.isACLNameSet()) {
               var2.append("ACLName");
               var2.append(String.valueOf(this.bean.getACLName()));
            }

            if (this.bean.isAlgorithmTypeSet()) {
               var2.append("AlgorithmType");
               var2.append(String.valueOf(this.bean.getAlgorithmType()));
            }

            if (this.bean.isConnectionPoolFailoverCallbackHandlerSet()) {
               var2.append("ConnectionPoolFailoverCallbackHandler");
               var2.append(String.valueOf(this.bean.getConnectionPoolFailoverCallbackHandler()));
            }

            if (this.bean.isFailoverRequestIfBusySet()) {
               var2.append("FailoverRequestIfBusy");
               var2.append(String.valueOf(this.bean.getFailoverRequestIfBusy()));
            }

            if (this.bean.isHealthCheckFrequencySecondsSet()) {
               var2.append("HealthCheckFrequencySeconds");
               var2.append(String.valueOf(this.bean.getHealthCheckFrequencySeconds()));
            }

            if (this.bean.isJDBCSystemResourceSet()) {
               var2.append("JDBCSystemResource");
               var2.append(String.valueOf(this.bean.getJDBCSystemResource()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPoolListSet()) {
               var2.append("PoolList");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPoolList())));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            if (this.bean.isHighAvailSet()) {
               var2.append("HighAvail");
               var2.append(String.valueOf(this.bean.isHighAvail()));
            }

            if (this.bean.isLoadBalanceSet()) {
               var2.append("LoadBalance");
               var2.append(String.valueOf(this.bean.isLoadBalance()));
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
            JDBCMultiPoolMBeanImpl var2 = (JDBCMultiPoolMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ACLName", this.bean.getACLName(), var2.getACLName(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("AlgorithmType", this.bean.getAlgorithmType(), var2.getAlgorithmType(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ConnectionPoolFailoverCallbackHandler", this.bean.getConnectionPoolFailoverCallbackHandler(), var2.getConnectionPoolFailoverCallbackHandler(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("FailoverRequestIfBusy", this.bean.getFailoverRequestIfBusy(), var2.getFailoverRequestIfBusy(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("HealthCheckFrequencySeconds", this.bean.getHealthCheckFrequencySeconds(), var2.getHealthCheckFrequencySeconds(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JDBCSystemResource", this.bean.getJDBCSystemResource(), var2.getJDBCSystemResource(), false);
            }

            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("PoolList", this.bean.getPoolList(), var2.getPoolList(), false);
            }

            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("HighAvail", this.bean.isHighAvail(), var2.isHighAvail(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LoadBalance", this.bean.isLoadBalance(), var2.isLoadBalance(), false);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JDBCMultiPoolMBeanImpl var3 = (JDBCMultiPoolMBeanImpl)var1.getSourceBean();
            JDBCMultiPoolMBeanImpl var4 = (JDBCMultiPoolMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ACLName")) {
                  var3.setACLName(var4.getACLName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("AlgorithmType")) {
                  var3.setAlgorithmType(var4.getAlgorithmType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("ConnectionPoolFailoverCallbackHandler")) {
                  var3.setConnectionPoolFailoverCallbackHandler(var4.getConnectionPoolFailoverCallbackHandler());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("FailoverRequestIfBusy")) {
                  var3.setFailoverRequestIfBusy(var4.getFailoverRequestIfBusy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("HealthCheckFrequencySeconds")) {
                  var3.setHealthCheckFrequencySeconds(var4.getHealthCheckFrequencySeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("JDBCSystemResource")) {
                  var3.setJDBCSystemResourceAsString(var4.getJDBCSystemResourceAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("PoolList")) {
                  var3.setPoolListAsString(var4.getPoolListAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("Targets")) {
                  var3.setTargetsAsString(var4.getTargetsAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("HighAvail")) {
                  var3.setHighAvail(var4.isHighAvail());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("LoadBalance")) {
                  var3.setLoadBalance(var4.isLoadBalance());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
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
            JDBCMultiPoolMBeanImpl var5 = (JDBCMultiPoolMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("ACLName")) && this.bean.isACLNameSet()) {
               var5.setACLName(this.bean.getACLName());
            }

            if (var2 && (var3 == null || !var3.contains("AlgorithmType")) && this.bean.isAlgorithmTypeSet()) {
               var5.setAlgorithmType(this.bean.getAlgorithmType());
            }

            if (var2 && (var3 == null || !var3.contains("ConnectionPoolFailoverCallbackHandler")) && this.bean.isConnectionPoolFailoverCallbackHandlerSet()) {
               var5.setConnectionPoolFailoverCallbackHandler(this.bean.getConnectionPoolFailoverCallbackHandler());
            }

            if (var2 && (var3 == null || !var3.contains("FailoverRequestIfBusy")) && this.bean.isFailoverRequestIfBusySet()) {
               var5.setFailoverRequestIfBusy(this.bean.getFailoverRequestIfBusy());
            }

            if (var2 && (var3 == null || !var3.contains("HealthCheckFrequencySeconds")) && this.bean.isHealthCheckFrequencySecondsSet()) {
               var5.setHealthCheckFrequencySeconds(this.bean.getHealthCheckFrequencySeconds());
            }

            if (var2 && (var3 == null || !var3.contains("JDBCSystemResource")) && this.bean.isJDBCSystemResourceSet()) {
               var5._unSet(var5, 9);
               var5.setJDBCSystemResourceAsString(this.bean.getJDBCSystemResourceAsString());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if (var2 && (var3 == null || !var3.contains("PoolList")) && this.bean.isPoolListSet()) {
               var5._unSet(var5, 11);
               var5.setPoolListAsString(this.bean.getPoolListAsString());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 7);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
            }

            if (var2 && (var3 == null || !var3.contains("HighAvail")) && this.bean.isHighAvailSet()) {
               var5.setHighAvail(this.bean.isHighAvail());
            }

            if (var2 && (var3 == null || !var3.contains("LoadBalance")) && this.bean.isLoadBalanceSet()) {
               var5.setLoadBalance(this.bean.isLoadBalance());
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
         this.inferSubTree(this.bean.getJDBCSystemResource(), var1, var2);
         this.inferSubTree(this.bean.getPoolList(), var1, var2);
         this.inferSubTree(this.bean.getTargets(), var1, var2);
      }
   }
}
