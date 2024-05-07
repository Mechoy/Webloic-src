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

public class TransactionLogJDBCStoreMBeanImpl extends JDBCStoreMBeanImpl implements TransactionLogJDBCStoreMBean, Serializable {
   private boolean _Enabled;
   private int _MaxRetrySecondsBeforeTLOGFail;
   private int _MaxRetrySecondsBeforeTXException;
   private String _PrefixName;
   private int _RetryIntervalSeconds;
   private TargetMBean[] _Targets;
   private static SchemaHelper2 _schemaHelper;

   public TransactionLogJDBCStoreMBeanImpl() {
      this._initializeProperty(-1);
   }

   public TransactionLogJDBCStoreMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getPrefixName() {
      if (!this._isSet(7)) {
         try {
            return "TLOG_" + ((ServerMBean)this.getParent()).getName() + "_";
         } catch (NullPointerException var2) {
         }
      }

      return this._PrefixName;
   }

   public TargetMBean[] getTargets() {
      return this._Targets;
   }

   public String getTargetsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargets());
   }

   public boolean isPrefixNameSet() {
      return this._isSet(7);
   }

   public boolean isTargetsSet() {
      return this._isSet(9);
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
                        TransactionLogJDBCStoreMBeanImpl.this.addTarget((TargetMBean)var1);
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

   public void setPrefixName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      JMSLegalHelper.validateJDBCPrefix(var1);
      String var2 = this._PrefixName;
      this._PrefixName = var1;
      this._postSet(7, var2, var1);
   }

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var4 = var1 == null ? new TargetMBeanImpl[0] : var1;
      var1 = (TargetMBean[])((TargetMBean[])this._getHelper()._cleanAndValidateArray(var4, TargetMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 9, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return TransactionLogJDBCStoreMBeanImpl.this.getTargets();
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

   public boolean isEnabled() {
      return this._Enabled;
   }

   public boolean isEnabledSet() {
      return this._isSet(21);
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

   public void setEnabled(boolean var1) {
      boolean var2 = this._Enabled;
      this._Enabled = var1;
      this._postSet(21, var2, var1);
   }

   public int getMaxRetrySecondsBeforeTLOGFail() {
      return this._MaxRetrySecondsBeforeTLOGFail;
   }

   public boolean isMaxRetrySecondsBeforeTLOGFailSet() {
      return this._isSet(22);
   }

   public void setMaxRetrySecondsBeforeTLOGFail(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxRetrySecondsBeforeTLOGFail", (long)var1, 0L, 2147483647L);
      int var2 = this._MaxRetrySecondsBeforeTLOGFail;
      this._MaxRetrySecondsBeforeTLOGFail = var1;
      this._postSet(22, var2, var1);
   }

   public int getMaxRetrySecondsBeforeTXException() {
      return this._MaxRetrySecondsBeforeTXException;
   }

   public boolean isMaxRetrySecondsBeforeTXExceptionSet() {
      return this._isSet(23);
   }

   public void setMaxRetrySecondsBeforeTXException(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxRetrySecondsBeforeTXException", (long)var1, 0L, 300L);
      int var2 = this._MaxRetrySecondsBeforeTXException;
      this._MaxRetrySecondsBeforeTXException = var1;
      this._postSet(23, var2, var1);
   }

   public int getRetryIntervalSeconds() {
      return this._RetryIntervalSeconds;
   }

   public boolean isRetryIntervalSecondsSet() {
      return this._isSet(24);
   }

   public void setRetryIntervalSeconds(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RetryIntervalSeconds", (long)var1, 1L, 60L);
      int var2 = this._RetryIntervalSeconds;
      this._RetryIntervalSeconds = var1;
      this._postSet(24, var2, var1);
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
         var1 = 22;
      }

      try {
         switch (var1) {
            case 22:
               this._MaxRetrySecondsBeforeTLOGFail = 300;
               if (var2) {
                  break;
               }
            case 23:
               this._MaxRetrySecondsBeforeTXException = 60;
               if (var2) {
                  break;
               }
            case 7:
               this._PrefixName = null;
               if (var2) {
                  break;
               }
            case 24:
               this._RetryIntervalSeconds = 5;
               if (var2) {
                  break;
               }
            case 9:
               this._Targets = new TargetMBean[0];
               if (var2) {
                  break;
               }
            case 21:
               this._Enabled = false;
               if (var2) {
                  break;
               }
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
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
      return "TransactionLogJDBCStore";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("Enabled")) {
         boolean var6 = this._Enabled;
         this._Enabled = (Boolean)var2;
         this._postSet(21, var6, this._Enabled);
      } else {
         int var4;
         if (var1.equals("MaxRetrySecondsBeforeTLOGFail")) {
            var4 = this._MaxRetrySecondsBeforeTLOGFail;
            this._MaxRetrySecondsBeforeTLOGFail = (Integer)var2;
            this._postSet(22, var4, this._MaxRetrySecondsBeforeTLOGFail);
         } else if (var1.equals("MaxRetrySecondsBeforeTXException")) {
            var4 = this._MaxRetrySecondsBeforeTXException;
            this._MaxRetrySecondsBeforeTXException = (Integer)var2;
            this._postSet(23, var4, this._MaxRetrySecondsBeforeTXException);
         } else if (var1.equals("PrefixName")) {
            String var5 = this._PrefixName;
            this._PrefixName = (String)var2;
            this._postSet(7, var5, this._PrefixName);
         } else if (var1.equals("RetryIntervalSeconds")) {
            var4 = this._RetryIntervalSeconds;
            this._RetryIntervalSeconds = (Integer)var2;
            this._postSet(24, var4, this._RetryIntervalSeconds);
         } else if (var1.equals("Targets")) {
            TargetMBean[] var3 = this._Targets;
            this._Targets = (TargetMBean[])((TargetMBean[])var2);
            this._postSet(9, var3, this._Targets);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Enabled")) {
         return new Boolean(this._Enabled);
      } else if (var1.equals("MaxRetrySecondsBeforeTLOGFail")) {
         return new Integer(this._MaxRetrySecondsBeforeTLOGFail);
      } else if (var1.equals("MaxRetrySecondsBeforeTXException")) {
         return new Integer(this._MaxRetrySecondsBeforeTXException);
      } else if (var1.equals("PrefixName")) {
         return this._PrefixName;
      } else if (var1.equals("RetryIntervalSeconds")) {
         return new Integer(this._RetryIntervalSeconds);
      } else {
         return var1.equals("Targets") ? this._Targets : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends JDBCStoreMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 6:
               if (var1.equals("target")) {
                  return 9;
               }
               break;
            case 7:
               if (var1.equals("enabled")) {
                  return 21;
               }
               break;
            case 11:
               if (var1.equals("prefix-name")) {
                  return 7;
               }
               break;
            case 22:
               if (var1.equals("retry-interval-seconds")) {
                  return 24;
               }
               break;
            case 33:
               if (var1.equals("max-retry-seconds-beforetlog-fail")) {
                  return 22;
               }
               break;
            case 36:
               if (var1.equals("max-retry-seconds-beforetx-exception")) {
                  return 23;
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
            case 7:
               return "prefix-name";
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            default:
               return super.getElementName(var1);
            case 9:
               return "target";
            case 21:
               return "enabled";
            case 22:
               return "max-retry-seconds-beforetlog-fail";
            case 23:
               return "max-retry-seconds-beforetx-exception";
            case 24:
               return "retry-interval-seconds";
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

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 22:
               return true;
            case 23:
               return true;
            case 24:
               return true;
            default:
               return super.isConfigurable(var1);
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

   protected static class Helper extends JDBCStoreMBeanImpl.Helper {
      private TransactionLogJDBCStoreMBeanImpl bean;

      protected Helper(TransactionLogJDBCStoreMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "PrefixName";
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            default:
               return super.getPropertyName(var1);
            case 9:
               return "Targets";
            case 21:
               return "Enabled";
            case 22:
               return "MaxRetrySecondsBeforeTLOGFail";
            case 23:
               return "MaxRetrySecondsBeforeTXException";
            case 24:
               return "RetryIntervalSeconds";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("MaxRetrySecondsBeforeTLOGFail")) {
            return 22;
         } else if (var1.equals("MaxRetrySecondsBeforeTXException")) {
            return 23;
         } else if (var1.equals("PrefixName")) {
            return 7;
         } else if (var1.equals("RetryIntervalSeconds")) {
            return 24;
         } else if (var1.equals("Targets")) {
            return 9;
         } else {
            return var1.equals("Enabled") ? 21 : super.getPropertyIndex(var1);
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
            if (this.bean.isMaxRetrySecondsBeforeTLOGFailSet()) {
               var2.append("MaxRetrySecondsBeforeTLOGFail");
               var2.append(String.valueOf(this.bean.getMaxRetrySecondsBeforeTLOGFail()));
            }

            if (this.bean.isMaxRetrySecondsBeforeTXExceptionSet()) {
               var2.append("MaxRetrySecondsBeforeTXException");
               var2.append(String.valueOf(this.bean.getMaxRetrySecondsBeforeTXException()));
            }

            if (this.bean.isPrefixNameSet()) {
               var2.append("PrefixName");
               var2.append(String.valueOf(this.bean.getPrefixName()));
            }

            if (this.bean.isRetryIntervalSecondsSet()) {
               var2.append("RetryIntervalSeconds");
               var2.append(String.valueOf(this.bean.getRetryIntervalSeconds()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            if (this.bean.isEnabledSet()) {
               var2.append("Enabled");
               var2.append(String.valueOf(this.bean.isEnabled()));
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
            TransactionLogJDBCStoreMBeanImpl var2 = (TransactionLogJDBCStoreMBeanImpl)var1;
            this.computeDiff("MaxRetrySecondsBeforeTLOGFail", this.bean.getMaxRetrySecondsBeforeTLOGFail(), var2.getMaxRetrySecondsBeforeTLOGFail(), true);
            this.computeDiff("MaxRetrySecondsBeforeTXException", this.bean.getMaxRetrySecondsBeforeTXException(), var2.getMaxRetrySecondsBeforeTXException(), true);
            this.computeDiff("PrefixName", this.bean.getPrefixName(), var2.getPrefixName(), true);
            this.computeDiff("RetryIntervalSeconds", this.bean.getRetryIntervalSeconds(), var2.getRetryIntervalSeconds(), true);
            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), false);
            this.computeDiff("Enabled", this.bean.isEnabled(), var2.isEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            TransactionLogJDBCStoreMBeanImpl var3 = (TransactionLogJDBCStoreMBeanImpl)var1.getSourceBean();
            TransactionLogJDBCStoreMBeanImpl var4 = (TransactionLogJDBCStoreMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("MaxRetrySecondsBeforeTLOGFail")) {
                  var3.setMaxRetrySecondsBeforeTLOGFail(var4.getMaxRetrySecondsBeforeTLOGFail());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("MaxRetrySecondsBeforeTXException")) {
                  var3.setMaxRetrySecondsBeforeTXException(var4.getMaxRetrySecondsBeforeTXException());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("PrefixName")) {
                  var3.setPrefixName(var4.getPrefixName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("RetryIntervalSeconds")) {
                  var3.setRetryIntervalSeconds(var4.getRetryIntervalSeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("Targets")) {
                  var3.setTargetsAsString(var4.getTargetsAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("Enabled")) {
                  var3.setEnabled(var4.isEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
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
            TransactionLogJDBCStoreMBeanImpl var5 = (TransactionLogJDBCStoreMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("MaxRetrySecondsBeforeTLOGFail")) && this.bean.isMaxRetrySecondsBeforeTLOGFailSet()) {
               var5.setMaxRetrySecondsBeforeTLOGFail(this.bean.getMaxRetrySecondsBeforeTLOGFail());
            }

            if ((var3 == null || !var3.contains("MaxRetrySecondsBeforeTXException")) && this.bean.isMaxRetrySecondsBeforeTXExceptionSet()) {
               var5.setMaxRetrySecondsBeforeTXException(this.bean.getMaxRetrySecondsBeforeTXException());
            }

            if ((var3 == null || !var3.contains("PrefixName")) && this.bean.isPrefixNameSet()) {
               var5.setPrefixName(this.bean.getPrefixName());
            }

            if ((var3 == null || !var3.contains("RetryIntervalSeconds")) && this.bean.isRetryIntervalSecondsSet()) {
               var5.setRetryIntervalSeconds(this.bean.getRetryIntervalSeconds());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 9);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
            }

            if ((var3 == null || !var3.contains("Enabled")) && this.bean.isEnabledSet()) {
               var5.setEnabled(this.bean.isEnabled());
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
         this.inferSubTree(this.bean.getTargets(), var1, var2);
      }
   }
}
