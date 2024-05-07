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

public class PersistentStoreMBeanImpl extends DeploymentMBeanImpl implements PersistentStoreMBean, Serializable {
   private String _LogicalName;
   private TargetMBean[] _Targets;
   private String _XAResourceName;
   private static SchemaHelper2 _schemaHelper;

   public PersistentStoreMBeanImpl() {
      this._initializeProperty(-1);
   }

   public PersistentStoreMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public TargetMBean[] getTargets() {
      return this._Targets;
   }

   public String getTargetsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargets());
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
                        PersistentStoreMBeanImpl.this.addTarget((TargetMBean)var1);
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

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var4 = var1 == null ? new TargetMBeanImpl[0] : var1;
      var1 = (TargetMBean[])((TargetMBean[])this._getHelper()._cleanAndValidateArray(var4, TargetMBean.class));
      JMSLegalHelper.validateSingleServerTargets(var1);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return PersistentStoreMBeanImpl.this.getTargets();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      TargetMBean[] var5 = this._Targets;
      this._Targets = var1;
      this._postSet(7, var5, var1);
   }

   public boolean addTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         TargetMBean[] var2;
         if (this._isSet(7)) {
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

   public String getLogicalName() {
      return this._LogicalName;
   }

   public boolean isLogicalNameSet() {
      return this._isSet(9);
   }

   public void setLogicalName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._LogicalName;
      this._LogicalName = var1;
      this._postSet(9, var2, var1);
   }

   public String getXAResourceName() {
      return this._XAResourceName;
   }

   public boolean isXAResourceNameSet() {
      return this._isSet(10);
   }

   public void setXAResourceName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._XAResourceName;
      this._XAResourceName = var1;
      this._postSet(10, var2, var1);
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
         var1 = 9;
      }

      try {
         switch (var1) {
            case 9:
               this._LogicalName = null;
               if (var2) {
                  break;
               }
            case 7:
               this._Targets = new TargetMBean[0];
               if (var2) {
                  break;
               }
            case 10:
               this._XAResourceName = null;
               if (var2) {
                  break;
               }
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
      return "PersistentStore";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("LogicalName")) {
         var3 = this._LogicalName;
         this._LogicalName = (String)var2;
         this._postSet(9, var3, this._LogicalName);
      } else if (var1.equals("Targets")) {
         TargetMBean[] var4 = this._Targets;
         this._Targets = (TargetMBean[])((TargetMBean[])var2);
         this._postSet(7, var4, this._Targets);
      } else if (var1.equals("XAResourceName")) {
         var3 = this._XAResourceName;
         this._XAResourceName = (String)var2;
         this._postSet(10, var3, this._XAResourceName);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("LogicalName")) {
         return this._LogicalName;
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else {
         return var1.equals("XAResourceName") ? this._XAResourceName : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 6:
               if (var1.equals("target")) {
                  return 7;
               }
               break;
            case 12:
               if (var1.equals("logical-name")) {
                  return 9;
               }
               break;
            case 16:
               if (var1.equals("xa-resource-name")) {
                  return 10;
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
               return "target";
            case 8:
            default:
               return super.getElementName(var1);
            case 9:
               return "logical-name";
            case 10:
               return "xa-resource-name";
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private PersistentStoreMBeanImpl bean;

      protected Helper(PersistentStoreMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "Targets";
            case 8:
            default:
               return super.getPropertyName(var1);
            case 9:
               return "LogicalName";
            case 10:
               return "XAResourceName";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("LogicalName")) {
            return 9;
         } else if (var1.equals("Targets")) {
            return 7;
         } else {
            return var1.equals("XAResourceName") ? 10 : super.getPropertyIndex(var1);
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
            if (this.bean.isLogicalNameSet()) {
               var2.append("LogicalName");
               var2.append(String.valueOf(this.bean.getLogicalName()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
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
            PersistentStoreMBeanImpl var2 = (PersistentStoreMBeanImpl)var1;
            this.computeDiff("LogicalName", this.bean.getLogicalName(), var2.getLogicalName(), true);
            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
            this.computeDiff("XAResourceName", this.bean.getXAResourceName(), var2.getXAResourceName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            PersistentStoreMBeanImpl var3 = (PersistentStoreMBeanImpl)var1.getSourceBean();
            PersistentStoreMBeanImpl var4 = (PersistentStoreMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("LogicalName")) {
                  var3.setLogicalName(var4.getLogicalName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("Targets")) {
                  var3.setTargetsAsString(var4.getTargetsAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("XAResourceName")) {
                  var3.setXAResourceName(var4.getXAResourceName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
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
            PersistentStoreMBeanImpl var5 = (PersistentStoreMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("LogicalName")) && this.bean.isLogicalNameSet()) {
               var5.setLogicalName(this.bean.getLogicalName());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 7);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
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
         this.inferSubTree(this.bean.getTargets(), var1, var2);
      }
   }
}
