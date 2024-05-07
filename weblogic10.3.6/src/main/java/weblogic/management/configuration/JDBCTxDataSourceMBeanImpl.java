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
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JDBCTxDataSource;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class JDBCTxDataSourceMBeanImpl extends DeploymentMBeanImpl implements JDBCTxDataSourceMBean, Serializable {
   private boolean _EnableTwoPhaseCommit;
   private JDBCSystemResourceMBean _JDBCSystemResource;
   private String _JNDIName;
   private String _JNDINameSeparator;
   private String _Name;
   private String _PoolName;
   private boolean _RowPrefetchEnabled;
   private int _RowPrefetchSize;
   private int _StreamChunkSize;
   private TargetMBean[] _Targets;
   private JDBCTxDataSource _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JDBCTxDataSourceMBeanImpl() {
      try {
         this._customizer = new JDBCTxDataSource(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JDBCTxDataSourceMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JDBCTxDataSource(this);
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
                        JDBCTxDataSourceMBeanImpl.this.addTarget((TargetMBean)var1);
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
                  JDBCTxDataSourceMBeanImpl.this.setJDBCSystemResource((JDBCSystemResourceMBean)var1);
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
                  return JDBCTxDataSourceMBeanImpl.this.getTargets();
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

   public String getJNDIName() {
      return this._customizer.getJNDIName();
   }

   public boolean isJNDINameSet() {
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

   public void setJNDIName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getJNDIName();
      this._customizer.setJNDIName(var1);
      this._postSet(10, var2, var1);
   }

   public String getJNDINameSeparator() {
      return this._customizer.getJNDINameSeparator();
   }

   public boolean isJNDINameSeparatorSet() {
      return this._isSet(11);
   }

   public void setJNDINameSeparator(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("JNDINameSeparator", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("JNDINameSeparator", var1);
      String var2 = this.getJNDINameSeparator();
      this._customizer.setJNDINameSeparator(var1);
      this._postSet(11, var2, var1);
   }

   public String getPoolName() {
      return this._customizer.getPoolName();
   }

   public boolean isPoolNameSet() {
      return this._isSet(12);
   }

   public void setPoolName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getPoolName();
      this._customizer.setPoolName(var1);
      this._postSet(12, var2, var1);
   }

   public boolean getEnableTwoPhaseCommit() {
      return this._customizer.getEnableTwoPhaseCommit();
   }

   public boolean isEnableTwoPhaseCommitSet() {
      return this._isSet(13);
   }

   public void setEnableTwoPhaseCommit(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getEnableTwoPhaseCommit();
      this._customizer.setEnableTwoPhaseCommit(var1);
      this._postSet(13, var2, var1);
   }

   public boolean isRowPrefetchEnabled() {
      return this._customizer.isRowPrefetchEnabled();
   }

   public boolean isRowPrefetchEnabledSet() {
      return this._isSet(14);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setRowPrefetchEnabled(boolean var1) {
      boolean var2 = this.isRowPrefetchEnabled();

      try {
         this._customizer.setRowPrefetchEnabled(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(14, var2, var1);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setRowPrefetchSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RowPrefetchSize", (long)var1, 2L, 65536L);
      int var2 = this.getRowPrefetchSize();

      try {
         this._customizer.setRowPrefetchSize(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(15, var2, var1);
   }

   public int getRowPrefetchSize() {
      return this._customizer.getRowPrefetchSize();
   }

   public boolean isRowPrefetchSizeSet() {
      return this._isSet(15);
   }

   public void setStreamChunkSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("StreamChunkSize", (long)var1, 1L, 65536L);
      int var2 = this.getStreamChunkSize();

      try {
         this._customizer.setStreamChunkSize(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(16, var2, var1);
   }

   public int getStreamChunkSize() {
      return this._customizer.getStreamChunkSize();
   }

   public boolean isStreamChunkSizeSet() {
      return this._isSet(16);
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
         var1 = 13;
      }

      try {
         switch (var1) {
            case 13:
               this._customizer.setEnableTwoPhaseCommit(false);
               if (var2) {
                  break;
               }
            case 9:
               this._customizer.setJDBCSystemResource((JDBCSystemResourceMBean)null);
               if (var2) {
                  break;
               }
            case 10:
               this._customizer.setJNDIName((String)null);
               if (var2) {
                  break;
               }
            case 11:
               this._customizer.setJNDINameSeparator("JNDINameSeparator");
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 12:
               this._customizer.setPoolName((String)null);
               if (var2) {
                  break;
               }
            case 15:
               this._customizer.setRowPrefetchSize(48);
               if (var2) {
                  break;
               }
            case 16:
               this._customizer.setStreamChunkSize(256);
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setTargets(new TargetMBean[0]);
               if (var2) {
                  break;
               }
            case 14:
               this._customizer.setRowPrefetchEnabled(false);
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
      return "JDBCTxDataSource";
   }

   public void putValue(String var1, Object var2) {
      boolean var6;
      if (var1.equals("EnableTwoPhaseCommit")) {
         var6 = this._EnableTwoPhaseCommit;
         this._EnableTwoPhaseCommit = (Boolean)var2;
         this._postSet(13, var6, this._EnableTwoPhaseCommit);
      } else if (var1.equals("JDBCSystemResource")) {
         JDBCSystemResourceMBean var8 = this._JDBCSystemResource;
         this._JDBCSystemResource = (JDBCSystemResourceMBean)var2;
         this._postSet(9, var8, this._JDBCSystemResource);
      } else {
         String var7;
         if (var1.equals("JNDIName")) {
            var7 = this._JNDIName;
            this._JNDIName = (String)var2;
            this._postSet(10, var7, this._JNDIName);
         } else if (var1.equals("JNDINameSeparator")) {
            var7 = this._JNDINameSeparator;
            this._JNDINameSeparator = (String)var2;
            this._postSet(11, var7, this._JNDINameSeparator);
         } else if (var1.equals("Name")) {
            var7 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var7, this._Name);
         } else if (var1.equals("PoolName")) {
            var7 = this._PoolName;
            this._PoolName = (String)var2;
            this._postSet(12, var7, this._PoolName);
         } else if (var1.equals("RowPrefetchEnabled")) {
            var6 = this._RowPrefetchEnabled;
            this._RowPrefetchEnabled = (Boolean)var2;
            this._postSet(14, var6, this._RowPrefetchEnabled);
         } else {
            int var5;
            if (var1.equals("RowPrefetchSize")) {
               var5 = this._RowPrefetchSize;
               this._RowPrefetchSize = (Integer)var2;
               this._postSet(15, var5, this._RowPrefetchSize);
            } else if (var1.equals("StreamChunkSize")) {
               var5 = this._StreamChunkSize;
               this._StreamChunkSize = (Integer)var2;
               this._postSet(16, var5, this._StreamChunkSize);
            } else if (var1.equals("Targets")) {
               TargetMBean[] var4 = this._Targets;
               this._Targets = (TargetMBean[])((TargetMBean[])var2);
               this._postSet(7, var4, this._Targets);
            } else if (var1.equals("customizer")) {
               JDBCTxDataSource var3 = this._customizer;
               this._customizer = (JDBCTxDataSource)var2;
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("EnableTwoPhaseCommit")) {
         return new Boolean(this._EnableTwoPhaseCommit);
      } else if (var1.equals("JDBCSystemResource")) {
         return this._JDBCSystemResource;
      } else if (var1.equals("JNDIName")) {
         return this._JNDIName;
      } else if (var1.equals("JNDINameSeparator")) {
         return this._JNDINameSeparator;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("PoolName")) {
         return this._PoolName;
      } else if (var1.equals("RowPrefetchEnabled")) {
         return new Boolean(this._RowPrefetchEnabled);
      } else if (var1.equals("RowPrefetchSize")) {
         return new Integer(this._RowPrefetchSize);
      } else if (var1.equals("StreamChunkSize")) {
         return new Integer(this._StreamChunkSize);
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("JNDINameSeparator", "JNDINameSeparator");
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("The default value for the property  is zero-length. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-zero-length value on @default annotation. Refer annotation legalZeroLength on property JNDINameSeparator in JDBCTxDataSourceMBean" + var2.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("JNDINameSeparator", "JNDINameSeparator");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property JNDINameSeparator in JDBCTxDataSourceMBean" + var1.getMessage());
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
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 21:
            case 22:
            default:
               break;
            case 6:
               if (var1.equals("target")) {
                  return 7;
               }
               break;
            case 9:
               if (var1.equals("jndi-name")) {
                  return 10;
               }

               if (var1.equals("pool-name")) {
                  return 12;
               }
               break;
            case 17:
               if (var1.equals("row-prefetch-size")) {
                  return 15;
               }

               if (var1.equals("stream-chunk-size")) {
                  return 16;
               }
               break;
            case 19:
               if (var1.equals("jndi-name-separator")) {
                  return 11;
               }
               break;
            case 20:
               if (var1.equals("jdbc-system-resource")) {
                  return 9;
               }

               if (var1.equals("row-prefetch-enabled")) {
                  return 14;
               }
               break;
            case 23:
               if (var1.equals("enable-two-phase-commit")) {
                  return 13;
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
               return "jndi-name";
            case 11:
               return "jndi-name-separator";
            case 12:
               return "pool-name";
            case 13:
               return "enable-two-phase-commit";
            case 14:
               return "row-prefetch-enabled";
            case 15:
               return "row-prefetch-size";
            case 16:
               return "stream-chunk-size";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
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
      private JDBCTxDataSourceMBeanImpl bean;

      protected Helper(JDBCTxDataSourceMBeanImpl var1) {
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
               return "JNDIName";
            case 11:
               return "JNDINameSeparator";
            case 12:
               return "PoolName";
            case 13:
               return "EnableTwoPhaseCommit";
            case 14:
               return "RowPrefetchEnabled";
            case 15:
               return "RowPrefetchSize";
            case 16:
               return "StreamChunkSize";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("EnableTwoPhaseCommit")) {
            return 13;
         } else if (var1.equals("JDBCSystemResource")) {
            return 9;
         } else if (var1.equals("JNDIName")) {
            return 10;
         } else if (var1.equals("JNDINameSeparator")) {
            return 11;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("PoolName")) {
            return 12;
         } else if (var1.equals("RowPrefetchSize")) {
            return 15;
         } else if (var1.equals("StreamChunkSize")) {
            return 16;
         } else if (var1.equals("Targets")) {
            return 7;
         } else {
            return var1.equals("RowPrefetchEnabled") ? 14 : super.getPropertyIndex(var1);
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
            if (this.bean.isEnableTwoPhaseCommitSet()) {
               var2.append("EnableTwoPhaseCommit");
               var2.append(String.valueOf(this.bean.getEnableTwoPhaseCommit()));
            }

            if (this.bean.isJDBCSystemResourceSet()) {
               var2.append("JDBCSystemResource");
               var2.append(String.valueOf(this.bean.getJDBCSystemResource()));
            }

            if (this.bean.isJNDINameSet()) {
               var2.append("JNDIName");
               var2.append(String.valueOf(this.bean.getJNDIName()));
            }

            if (this.bean.isJNDINameSeparatorSet()) {
               var2.append("JNDINameSeparator");
               var2.append(String.valueOf(this.bean.getJNDINameSeparator()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPoolNameSet()) {
               var2.append("PoolName");
               var2.append(String.valueOf(this.bean.getPoolName()));
            }

            if (this.bean.isRowPrefetchSizeSet()) {
               var2.append("RowPrefetchSize");
               var2.append(String.valueOf(this.bean.getRowPrefetchSize()));
            }

            if (this.bean.isStreamChunkSizeSet()) {
               var2.append("StreamChunkSize");
               var2.append(String.valueOf(this.bean.getStreamChunkSize()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            if (this.bean.isRowPrefetchEnabledSet()) {
               var2.append("RowPrefetchEnabled");
               var2.append(String.valueOf(this.bean.isRowPrefetchEnabled()));
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
            JDBCTxDataSourceMBeanImpl var2 = (JDBCTxDataSourceMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("EnableTwoPhaseCommit", this.bean.getEnableTwoPhaseCommit(), var2.getEnableTwoPhaseCommit(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JDBCSystemResource", this.bean.getJDBCSystemResource(), var2.getJDBCSystemResource(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JNDIName", this.bean.getJNDIName(), var2.getJNDIName(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JNDINameSeparator", this.bean.getJNDINameSeparator(), var2.getJNDINameSeparator(), false);
            }

            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("PoolName", this.bean.getPoolName(), var2.getPoolName(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("RowPrefetchSize", this.bean.getRowPrefetchSize(), var2.getRowPrefetchSize(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StreamChunkSize", this.bean.getStreamChunkSize(), var2.getStreamChunkSize(), false);
            }

            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("RowPrefetchEnabled", this.bean.isRowPrefetchEnabled(), var2.isRowPrefetchEnabled(), false);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JDBCTxDataSourceMBeanImpl var3 = (JDBCTxDataSourceMBeanImpl)var1.getSourceBean();
            JDBCTxDataSourceMBeanImpl var4 = (JDBCTxDataSourceMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("EnableTwoPhaseCommit")) {
                  var3.setEnableTwoPhaseCommit(var4.getEnableTwoPhaseCommit());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("JDBCSystemResource")) {
                  var3.setJDBCSystemResourceAsString(var4.getJDBCSystemResourceAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("JNDIName")) {
                  var3.setJNDIName(var4.getJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("JNDINameSeparator")) {
                  var3.setJNDINameSeparator(var4.getJNDINameSeparator());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("PoolName")) {
                  var3.setPoolName(var4.getPoolName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("RowPrefetchSize")) {
                  var3.setRowPrefetchSize(var4.getRowPrefetchSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("StreamChunkSize")) {
                  var3.setStreamChunkSize(var4.getStreamChunkSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("Targets")) {
                  var3.setTargetsAsString(var4.getTargetsAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("RowPrefetchEnabled")) {
                  var3.setRowPrefetchEnabled(var4.isRowPrefetchEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
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
            JDBCTxDataSourceMBeanImpl var5 = (JDBCTxDataSourceMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("EnableTwoPhaseCommit")) && this.bean.isEnableTwoPhaseCommitSet()) {
               var5.setEnableTwoPhaseCommit(this.bean.getEnableTwoPhaseCommit());
            }

            if (var2 && (var3 == null || !var3.contains("JDBCSystemResource")) && this.bean.isJDBCSystemResourceSet()) {
               var5._unSet(var5, 9);
               var5.setJDBCSystemResourceAsString(this.bean.getJDBCSystemResourceAsString());
            }

            if (var2 && (var3 == null || !var3.contains("JNDIName")) && this.bean.isJNDINameSet()) {
               var5.setJNDIName(this.bean.getJNDIName());
            }

            if (var2 && (var3 == null || !var3.contains("JNDINameSeparator")) && this.bean.isJNDINameSeparatorSet()) {
               var5.setJNDINameSeparator(this.bean.getJNDINameSeparator());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if (var2 && (var3 == null || !var3.contains("PoolName")) && this.bean.isPoolNameSet()) {
               var5.setPoolName(this.bean.getPoolName());
            }

            if (var2 && (var3 == null || !var3.contains("RowPrefetchSize")) && this.bean.isRowPrefetchSizeSet()) {
               var5.setRowPrefetchSize(this.bean.getRowPrefetchSize());
            }

            if (var2 && (var3 == null || !var3.contains("StreamChunkSize")) && this.bean.isStreamChunkSizeSet()) {
               var5.setStreamChunkSize(this.bean.getStreamChunkSize());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 7);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
            }

            if (var2 && (var3 == null || !var3.contains("RowPrefetchEnabled")) && this.bean.isRowPrefetchEnabledSet()) {
               var5.setRowPrefetchEnabled(this.bean.isRowPrefetchEnabled());
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
         this.inferSubTree(this.bean.getTargets(), var1, var2);
      }
   }
}
