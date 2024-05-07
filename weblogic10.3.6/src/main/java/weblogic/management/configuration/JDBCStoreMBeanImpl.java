package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class JDBCStoreMBeanImpl extends GenericJDBCStoreMBeanImpl implements JDBCStoreMBean, Serializable {
   private JDBCConnectionPoolMBean _ConnectionPool;
   private JDBCSystemResourceMBean _DataSource;
   private int _DeletesPerBatchMaximum;
   private int _DeletesPerStatementMaximum;
   private int _DeploymentOrder;
   private int _InsertsPerBatchMaximum;
   private String _LogicalName;
   private TargetMBean[] _Targets;
   private int _ThreeStepThreshold;
   private int _WorkerCount;
   private int _WorkerPreferredBatchSize;
   private String _XAResourceName;
   private static SchemaHelper2 _schemaHelper;

   public JDBCStoreMBeanImpl() {
      this._initializeProperty(-1);
   }

   public JDBCStoreMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public JDBCSystemResourceMBean getDataSource() {
      return this._DataSource;
   }

   public String getDataSourceAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getDataSource();
      return var1 == null ? null : var1._getKey().toString();
   }

   public TargetMBean[] getTargets() {
      return this._Targets;
   }

   public String getTargetsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargets());
   }

   public boolean isDataSourceSet() {
      return this._isSet(13);
   }

   public boolean isTargetsSet() {
      return this._isSet(9);
   }

   public void setDataSourceAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JDBCSystemResourceMBean.class, new ReferenceManager.Resolver(this, 13) {
            public void resolveReference(Object var1) {
               try {
                  JDBCStoreMBeanImpl.this.setDataSource((JDBCSystemResourceMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JDBCSystemResourceMBean var2 = this._DataSource;
         this._initializeProperty(13);
         this._postSet(13, var2, this._DataSource);
      }

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
               this._getReferenceManager().registerUnresolvedReference(var5, TargetMBean.class, new ReferenceManager.Resolver(this, 9) {
                  public void resolveReference(Object var1) {
                     try {
                        JDBCStoreMBeanImpl.this.addTarget((TargetMBean)var1);
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
         this._initializeProperty(9);
         this._postSet(9, var2, this._Targets);
      }
   }

   public void setDataSource(JDBCSystemResourceMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 13, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return JDBCStoreMBeanImpl.this.getDataSource();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      JDBCSystemResourceMBean var3 = this._DataSource;
      this._DataSource = var1;
      this._postSet(13, var3, var1);
   }

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var4 = var1 == null ? new TargetMBeanImpl[0] : var1;
      var1 = (TargetMBean[])((TargetMBean[])this._getHelper()._cleanAndValidateArray(var4, TargetMBean.class));
      JMSLegalHelper.validateSingleServerTargets(var1);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 9, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return JDBCStoreMBeanImpl.this.getTargets();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      TargetMBean[] var5 = this._Targets;
      this._Targets = var1;
      this._postSet(9, var5, var1);
   }

   public boolean addTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 9)) {
         TargetMBean[] var2;
         if (this._isSet(9)) {
            var2 = (TargetMBean[])((TargetMBean[])this._getHelper()._extendArray(this.getTargets(), TargetMBean.class, var1));
         } else {
            var2 = new TargetMBean[]{var1};
         }

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

   public JDBCConnectionPoolMBean getConnectionPool() {
      return this._ConnectionPool;
   }

   public String getConnectionPoolAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getConnectionPool();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isConnectionPoolSet() {
      return this._isSet(14);
   }

   public void setConnectionPoolAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JDBCConnectionPoolMBean.class, new ReferenceManager.Resolver(this, 14) {
            public void resolveReference(Object var1) {
               try {
                  JDBCStoreMBeanImpl.this.setConnectionPool((JDBCConnectionPoolMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JDBCConnectionPoolMBean var2 = this._ConnectionPool;
         this._initializeProperty(14);
         this._postSet(14, var2, this._ConnectionPool);
      }

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

   public void setConnectionPool(JDBCConnectionPoolMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 14, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return JDBCStoreMBeanImpl.this.getConnectionPool();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      JDBCConnectionPoolMBean var3 = this._ConnectionPool;
      this._ConnectionPool = var1;
      this._postSet(14, var3, var1);
   }

   public int getDeletesPerBatchMaximum() {
      return this._DeletesPerBatchMaximum;
   }

   public int getDeploymentOrder() {
      return this._DeploymentOrder;
   }

   public String getLogicalName() {
      return this._LogicalName;
   }

   public boolean isDeletesPerBatchMaximumSet() {
      return this._isSet(15);
   }

   public boolean isDeploymentOrderSet() {
      return this._isSet(10);
   }

   public boolean isLogicalNameSet() {
      return this._isSet(11);
   }

   public void setDeletesPerBatchMaximum(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DeletesPerBatchMaximum", (long)var1, 1L, 100L);
      int var2 = this._DeletesPerBatchMaximum;
      this._DeletesPerBatchMaximum = var1;
      this._postSet(15, var2, var1);
   }

   public void setDeploymentOrder(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DeploymentOrder", (long)var1, 0L, 2147483647L);
      int var2 = this._DeploymentOrder;
      this._DeploymentOrder = var1;
      this._postSet(10, var2, var1);
   }

   public void setLogicalName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._LogicalName;
      this._LogicalName = var1;
      this._postSet(11, var2, var1);
   }

   public int getInsertsPerBatchMaximum() {
      return this._InsertsPerBatchMaximum;
   }

   public String getXAResourceName() {
      return this._XAResourceName;
   }

   public boolean isInsertsPerBatchMaximumSet() {
      return this._isSet(16);
   }

   public boolean isXAResourceNameSet() {
      return this._isSet(12);
   }

   public void setInsertsPerBatchMaximum(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("InsertsPerBatchMaximum", (long)var1, 1L, 100L);
      int var2 = this._InsertsPerBatchMaximum;
      this._InsertsPerBatchMaximum = var1;
      this._postSet(16, var2, var1);
   }

   public void setXAResourceName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._XAResourceName;
      this._XAResourceName = var1;
      this._postSet(12, var2, var1);
   }

   public int getDeletesPerStatementMaximum() {
      return this._DeletesPerStatementMaximum;
   }

   public boolean isDeletesPerStatementMaximumSet() {
      return this._isSet(17);
   }

   public void setDeletesPerStatementMaximum(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DeletesPerStatementMaximum", (long)var1, 1L, 100L);
      int var2 = this._DeletesPerStatementMaximum;
      this._DeletesPerStatementMaximum = var1;
      this._postSet(17, var2, var1);
   }

   public int getWorkerCount() {
      return this._WorkerCount;
   }

   public boolean isWorkerCountSet() {
      return this._isSet(18);
   }

   public void setWorkerCount(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("WorkerCount", (long)var1, 1L, 1000L);
      int var2 = this._WorkerCount;
      this._WorkerCount = var1;
      this._postSet(18, var2, var1);
   }

   public int getWorkerPreferredBatchSize() {
      return this._WorkerPreferredBatchSize;
   }

   public boolean isWorkerPreferredBatchSizeSet() {
      return this._isSet(19);
   }

   public void setWorkerPreferredBatchSize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("WorkerPreferredBatchSize", (long)var1, 1L, 2147483647L);
      int var2 = this._WorkerPreferredBatchSize;
      this._WorkerPreferredBatchSize = var1;
      this._postSet(19, var2, var1);
   }

   public int getThreeStepThreshold() {
      return this._ThreeStepThreshold;
   }

   public boolean isThreeStepThresholdSet() {
      return this._isSet(20);
   }

   public void setThreeStepThreshold(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ThreeStepThreshold", (long)var1, 4000L, 2147483647L);
      int var2 = this._ThreeStepThreshold;
      this._ThreeStepThreshold = var1;
      this._postSet(20, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
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
         var1 = 14;
      }

      try {
         switch (var1) {
            case 14:
               this._ConnectionPool = null;
               if (var2) {
                  break;
               }
            case 13:
               this._DataSource = null;
               if (var2) {
                  break;
               }
            case 15:
               this._DeletesPerBatchMaximum = 20;
               if (var2) {
                  break;
               }
            case 17:
               this._DeletesPerStatementMaximum = 20;
               if (var2) {
                  break;
               }
            case 10:
               this._DeploymentOrder = 1000;
               if (var2) {
                  break;
               }
            case 16:
               this._InsertsPerBatchMaximum = 20;
               if (var2) {
                  break;
               }
            case 11:
               this._LogicalName = null;
               if (var2) {
                  break;
               }
            case 9:
               this._Targets = new TargetMBean[0];
               if (var2) {
                  break;
               }
            case 20:
               this._ThreeStepThreshold = 200000;
               if (var2) {
                  break;
               }
            case 18:
               this._WorkerCount = 1;
               if (var2) {
                  break;
               }
            case 19:
               this._WorkerPreferredBatchSize = 10;
               if (var2) {
                  break;
               }
            case 12:
               this._XAResourceName = null;
               if (var2) {
                  break;
               }
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
      return "JDBCStore";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("ConnectionPool")) {
         JDBCConnectionPoolMBean var7 = this._ConnectionPool;
         this._ConnectionPool = (JDBCConnectionPoolMBean)var2;
         this._postSet(14, var7, this._ConnectionPool);
      } else if (var1.equals("DataSource")) {
         JDBCSystemResourceMBean var6 = this._DataSource;
         this._DataSource = (JDBCSystemResourceMBean)var2;
         this._postSet(13, var6, this._DataSource);
      } else {
         int var4;
         if (var1.equals("DeletesPerBatchMaximum")) {
            var4 = this._DeletesPerBatchMaximum;
            this._DeletesPerBatchMaximum = (Integer)var2;
            this._postSet(15, var4, this._DeletesPerBatchMaximum);
         } else if (var1.equals("DeletesPerStatementMaximum")) {
            var4 = this._DeletesPerStatementMaximum;
            this._DeletesPerStatementMaximum = (Integer)var2;
            this._postSet(17, var4, this._DeletesPerStatementMaximum);
         } else if (var1.equals("DeploymentOrder")) {
            var4 = this._DeploymentOrder;
            this._DeploymentOrder = (Integer)var2;
            this._postSet(10, var4, this._DeploymentOrder);
         } else if (var1.equals("InsertsPerBatchMaximum")) {
            var4 = this._InsertsPerBatchMaximum;
            this._InsertsPerBatchMaximum = (Integer)var2;
            this._postSet(16, var4, this._InsertsPerBatchMaximum);
         } else {
            String var3;
            if (var1.equals("LogicalName")) {
               var3 = this._LogicalName;
               this._LogicalName = (String)var2;
               this._postSet(11, var3, this._LogicalName);
            } else if (var1.equals("Targets")) {
               TargetMBean[] var5 = this._Targets;
               this._Targets = (TargetMBean[])((TargetMBean[])var2);
               this._postSet(9, var5, this._Targets);
            } else if (var1.equals("ThreeStepThreshold")) {
               var4 = this._ThreeStepThreshold;
               this._ThreeStepThreshold = (Integer)var2;
               this._postSet(20, var4, this._ThreeStepThreshold);
            } else if (var1.equals("WorkerCount")) {
               var4 = this._WorkerCount;
               this._WorkerCount = (Integer)var2;
               this._postSet(18, var4, this._WorkerCount);
            } else if (var1.equals("WorkerPreferredBatchSize")) {
               var4 = this._WorkerPreferredBatchSize;
               this._WorkerPreferredBatchSize = (Integer)var2;
               this._postSet(19, var4, this._WorkerPreferredBatchSize);
            } else if (var1.equals("XAResourceName")) {
               var3 = this._XAResourceName;
               this._XAResourceName = (String)var2;
               this._postSet(12, var3, this._XAResourceName);
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ConnectionPool")) {
         return this._ConnectionPool;
      } else if (var1.equals("DataSource")) {
         return this._DataSource;
      } else if (var1.equals("DeletesPerBatchMaximum")) {
         return new Integer(this._DeletesPerBatchMaximum);
      } else if (var1.equals("DeletesPerStatementMaximum")) {
         return new Integer(this._DeletesPerStatementMaximum);
      } else if (var1.equals("DeploymentOrder")) {
         return new Integer(this._DeploymentOrder);
      } else if (var1.equals("InsertsPerBatchMaximum")) {
         return new Integer(this._InsertsPerBatchMaximum);
      } else if (var1.equals("LogicalName")) {
         return this._LogicalName;
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else if (var1.equals("ThreeStepThreshold")) {
         return new Integer(this._ThreeStepThreshold);
      } else if (var1.equals("WorkerCount")) {
         return new Integer(this._WorkerCount);
      } else if (var1.equals("WorkerPreferredBatchSize")) {
         return new Integer(this._WorkerPreferredBatchSize);
      } else {
         return var1.equals("XAResourceName") ? this._XAResourceName : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends GenericJDBCStoreMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 6:
               if (var1.equals("target")) {
                  return 9;
               }
            case 7:
            case 8:
            case 9:
            case 10:
            case 13:
            case 14:
            case 17:
            case 18:
            case 19:
            case 21:
            case 22:
            case 23:
            case 24:
            case 26:
            case 28:
            default:
               break;
            case 11:
               if (var1.equals("data-source")) {
                  return 13;
               }
               break;
            case 12:
               if (var1.equals("logical-name")) {
                  return 11;
               }

               if (var1.equals("worker-count")) {
                  return 18;
               }
               break;
            case 15:
               if (var1.equals("connection-pool")) {
                  return 14;
               }
               break;
            case 16:
               if (var1.equals("deployment-order")) {
                  return 10;
               }

               if (var1.equals("xa-resource-name")) {
                  return 12;
               }
               break;
            case 20:
               if (var1.equals("three-step-threshold")) {
                  return 20;
               }
               break;
            case 25:
               if (var1.equals("deletes-per-batch-maximum")) {
                  return 15;
               }

               if (var1.equals("inserts-per-batch-maximum")) {
                  return 16;
               }
               break;
            case 27:
               if (var1.equals("worker-preferred-batch-size")) {
                  return 19;
               }
               break;
            case 29:
               if (var1.equals("deletes-per-statement-maximum")) {
                  return 17;
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
            case 9:
               return "target";
            case 10:
               return "deployment-order";
            case 11:
               return "logical-name";
            case 12:
               return "xa-resource-name";
            case 13:
               return "data-source";
            case 14:
               return "connection-pool";
            case 15:
               return "deletes-per-batch-maximum";
            case 16:
               return "inserts-per-batch-maximum";
            case 17:
               return "deletes-per-statement-maximum";
            case 18:
               return "worker-count";
            case 19:
               return "worker-preferred-batch-size";
            case 20:
               return "three-step-threshold";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 9:
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends GenericJDBCStoreMBeanImpl.Helper {
      private JDBCStoreMBeanImpl bean;

      protected Helper(JDBCStoreMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 9:
               return "Targets";
            case 10:
               return "DeploymentOrder";
            case 11:
               return "LogicalName";
            case 12:
               return "XAResourceName";
            case 13:
               return "DataSource";
            case 14:
               return "ConnectionPool";
            case 15:
               return "DeletesPerBatchMaximum";
            case 16:
               return "InsertsPerBatchMaximum";
            case 17:
               return "DeletesPerStatementMaximum";
            case 18:
               return "WorkerCount";
            case 19:
               return "WorkerPreferredBatchSize";
            case 20:
               return "ThreeStepThreshold";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ConnectionPool")) {
            return 14;
         } else if (var1.equals("DataSource")) {
            return 13;
         } else if (var1.equals("DeletesPerBatchMaximum")) {
            return 15;
         } else if (var1.equals("DeletesPerStatementMaximum")) {
            return 17;
         } else if (var1.equals("DeploymentOrder")) {
            return 10;
         } else if (var1.equals("InsertsPerBatchMaximum")) {
            return 16;
         } else if (var1.equals("LogicalName")) {
            return 11;
         } else if (var1.equals("Targets")) {
            return 9;
         } else if (var1.equals("ThreeStepThreshold")) {
            return 20;
         } else if (var1.equals("WorkerCount")) {
            return 18;
         } else if (var1.equals("WorkerPreferredBatchSize")) {
            return 19;
         } else {
            return var1.equals("XAResourceName") ? 12 : super.getPropertyIndex(var1);
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
            if (this.bean.isConnectionPoolSet()) {
               var2.append("ConnectionPool");
               var2.append(String.valueOf(this.bean.getConnectionPool()));
            }

            if (this.bean.isDataSourceSet()) {
               var2.append("DataSource");
               var2.append(String.valueOf(this.bean.getDataSource()));
            }

            if (this.bean.isDeletesPerBatchMaximumSet()) {
               var2.append("DeletesPerBatchMaximum");
               var2.append(String.valueOf(this.bean.getDeletesPerBatchMaximum()));
            }

            if (this.bean.isDeletesPerStatementMaximumSet()) {
               var2.append("DeletesPerStatementMaximum");
               var2.append(String.valueOf(this.bean.getDeletesPerStatementMaximum()));
            }

            if (this.bean.isDeploymentOrderSet()) {
               var2.append("DeploymentOrder");
               var2.append(String.valueOf(this.bean.getDeploymentOrder()));
            }

            if (this.bean.isInsertsPerBatchMaximumSet()) {
               var2.append("InsertsPerBatchMaximum");
               var2.append(String.valueOf(this.bean.getInsertsPerBatchMaximum()));
            }

            if (this.bean.isLogicalNameSet()) {
               var2.append("LogicalName");
               var2.append(String.valueOf(this.bean.getLogicalName()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            if (this.bean.isThreeStepThresholdSet()) {
               var2.append("ThreeStepThreshold");
               var2.append(String.valueOf(this.bean.getThreeStepThreshold()));
            }

            if (this.bean.isWorkerCountSet()) {
               var2.append("WorkerCount");
               var2.append(String.valueOf(this.bean.getWorkerCount()));
            }

            if (this.bean.isWorkerPreferredBatchSizeSet()) {
               var2.append("WorkerPreferredBatchSize");
               var2.append(String.valueOf(this.bean.getWorkerPreferredBatchSize()));
            }

            if (this.bean.isXAResourceNameSet()) {
               var2.append("XAResourceName");
               var2.append(String.valueOf(this.bean.getXAResourceName()));
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
            JDBCStoreMBeanImpl var2 = (JDBCStoreMBeanImpl)var1;
            this.computeDiff("ConnectionPool", this.bean.getConnectionPool(), var2.getConnectionPool(), false);
            this.computeDiff("DataSource", this.bean.getDataSource(), var2.getDataSource(), false);
            this.computeDiff("DeletesPerBatchMaximum", this.bean.getDeletesPerBatchMaximum(), var2.getDeletesPerBatchMaximum(), false);
            this.computeDiff("DeletesPerStatementMaximum", this.bean.getDeletesPerStatementMaximum(), var2.getDeletesPerStatementMaximum(), false);
            this.computeDiff("DeploymentOrder", this.bean.getDeploymentOrder(), var2.getDeploymentOrder(), true);
            this.computeDiff("InsertsPerBatchMaximum", this.bean.getInsertsPerBatchMaximum(), var2.getInsertsPerBatchMaximum(), false);
            this.computeDiff("LogicalName", this.bean.getLogicalName(), var2.getLogicalName(), true);
            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
            this.computeDiff("ThreeStepThreshold", this.bean.getThreeStepThreshold(), var2.getThreeStepThreshold(), false);
            this.computeDiff("WorkerCount", this.bean.getWorkerCount(), var2.getWorkerCount(), false);
            this.computeDiff("WorkerPreferredBatchSize", this.bean.getWorkerPreferredBatchSize(), var2.getWorkerPreferredBatchSize(), false);
            this.computeDiff("XAResourceName", this.bean.getXAResourceName(), var2.getXAResourceName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JDBCStoreMBeanImpl var3 = (JDBCStoreMBeanImpl)var1.getSourceBean();
            JDBCStoreMBeanImpl var4 = (JDBCStoreMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ConnectionPool")) {
                  var3.setConnectionPoolAsString(var4.getConnectionPoolAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("DataSource")) {
                  var3.setDataSourceAsString(var4.getDataSourceAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("DeletesPerBatchMaximum")) {
                  var3.setDeletesPerBatchMaximum(var4.getDeletesPerBatchMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("DeletesPerStatementMaximum")) {
                  var3.setDeletesPerStatementMaximum(var4.getDeletesPerStatementMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("DeploymentOrder")) {
                  var3.setDeploymentOrder(var4.getDeploymentOrder());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("InsertsPerBatchMaximum")) {
                  var3.setInsertsPerBatchMaximum(var4.getInsertsPerBatchMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("LogicalName")) {
                  var3.setLogicalName(var4.getLogicalName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("Targets")) {
                  var3.setTargetsAsString(var4.getTargetsAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("ThreeStepThreshold")) {
                  var3.setThreeStepThreshold(var4.getThreeStepThreshold());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("WorkerCount")) {
                  var3.setWorkerCount(var4.getWorkerCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("WorkerPreferredBatchSize")) {
                  var3.setWorkerPreferredBatchSize(var4.getWorkerPreferredBatchSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("XAResourceName")) {
                  var3.setXAResourceName(var4.getXAResourceName());
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
            JDBCStoreMBeanImpl var5 = (JDBCStoreMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ConnectionPool")) && this.bean.isConnectionPoolSet()) {
               var5._unSet(var5, 14);
               var5.setConnectionPoolAsString(this.bean.getConnectionPoolAsString());
            }

            if ((var3 == null || !var3.contains("DataSource")) && this.bean.isDataSourceSet()) {
               var5._unSet(var5, 13);
               var5.setDataSourceAsString(this.bean.getDataSourceAsString());
            }

            if ((var3 == null || !var3.contains("DeletesPerBatchMaximum")) && this.bean.isDeletesPerBatchMaximumSet()) {
               var5.setDeletesPerBatchMaximum(this.bean.getDeletesPerBatchMaximum());
            }

            if ((var3 == null || !var3.contains("DeletesPerStatementMaximum")) && this.bean.isDeletesPerStatementMaximumSet()) {
               var5.setDeletesPerStatementMaximum(this.bean.getDeletesPerStatementMaximum());
            }

            if ((var3 == null || !var3.contains("DeploymentOrder")) && this.bean.isDeploymentOrderSet()) {
               var5.setDeploymentOrder(this.bean.getDeploymentOrder());
            }

            if ((var3 == null || !var3.contains("InsertsPerBatchMaximum")) && this.bean.isInsertsPerBatchMaximumSet()) {
               var5.setInsertsPerBatchMaximum(this.bean.getInsertsPerBatchMaximum());
            }

            if ((var3 == null || !var3.contains("LogicalName")) && this.bean.isLogicalNameSet()) {
               var5.setLogicalName(this.bean.getLogicalName());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 9);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
            }

            if ((var3 == null || !var3.contains("ThreeStepThreshold")) && this.bean.isThreeStepThresholdSet()) {
               var5.setThreeStepThreshold(this.bean.getThreeStepThreshold());
            }

            if ((var3 == null || !var3.contains("WorkerCount")) && this.bean.isWorkerCountSet()) {
               var5.setWorkerCount(this.bean.getWorkerCount());
            }

            if ((var3 == null || !var3.contains("WorkerPreferredBatchSize")) && this.bean.isWorkerPreferredBatchSizeSet()) {
               var5.setWorkerPreferredBatchSize(this.bean.getWorkerPreferredBatchSize());
            }

            if ((var3 == null || !var3.contains("XAResourceName")) && this.bean.isXAResourceNameSet()) {
               var5.setXAResourceName(this.bean.getXAResourceName());
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
         this.inferSubTree(this.bean.getConnectionPool(), var1, var2);
         this.inferSubTree(this.bean.getDataSource(), var1, var2);
         this.inferSubTree(this.bean.getTargets(), var1, var2);
      }
   }
}
