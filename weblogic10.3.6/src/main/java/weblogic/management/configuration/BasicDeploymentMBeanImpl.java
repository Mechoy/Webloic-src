package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class BasicDeploymentMBeanImpl extends TargetInfoMBeanImpl implements BasicDeploymentMBean, Serializable {
   private int _DeploymentOrder;
   private String _DeploymentPrincipalName;
   private String _SourcePath;
   private SubDeploymentMBean[] _SubDeployments;
   private static SchemaHelper2 _schemaHelper;

   public BasicDeploymentMBeanImpl() {
      this._initializeProperty(-1);
   }

   public BasicDeploymentMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getSourcePath() {
      return this._SourcePath;
   }

   public boolean isSourcePathSet() {
      return this._isSet(10);
   }

   public void setSourcePath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SourcePath;
      this._SourcePath = var1;
      this._postSet(10, var2, var1);
   }

   public void addSubDeployment(SubDeploymentMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 11)) {
         SubDeploymentMBean[] var2;
         if (this._isSet(11)) {
            var2 = (SubDeploymentMBean[])((SubDeploymentMBean[])this._getHelper()._extendArray(this.getSubDeployments(), SubDeploymentMBean.class, var1));
         } else {
            var2 = new SubDeploymentMBean[]{var1};
         }

         try {
            this.setSubDeployments(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SubDeploymentMBean[] getSubDeployments() {
      return this._SubDeployments;
   }

   public boolean isSubDeploymentsSet() {
      return this._isSet(11);
   }

   public void removeSubDeployment(SubDeploymentMBean var1) {
      this.destroySubDeployment(var1);
   }

   public void setSubDeployments(SubDeploymentMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SubDeploymentMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 11)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SubDeploymentMBean[] var5 = this._SubDeployments;
      this._SubDeployments = (SubDeploymentMBean[])var4;
      this._postSet(11, var5, var4);
   }

   public SubDeploymentMBean createSubDeployment(String var1) {
      SubDeploymentMBeanImpl var2 = new SubDeploymentMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSubDeployment(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public SubDeploymentMBean lookupSubDeployment(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SubDeployments).iterator();

      SubDeploymentMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (SubDeploymentMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void destroySubDeployment(SubDeploymentMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 11);
         SubDeploymentMBean[] var2 = this.getSubDeployments();
         SubDeploymentMBean[] var3 = (SubDeploymentMBean[])((SubDeploymentMBean[])this._getHelper()._removeElement(var2, SubDeploymentMBean.class, var1));
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
               this.setSubDeployments(var3);
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

   public int getDeploymentOrder() {
      return this._DeploymentOrder;
   }

   public boolean isDeploymentOrderSet() {
      return this._isSet(12);
   }

   public void setDeploymentOrder(int var1) {
      int var2 = this._DeploymentOrder;
      this._DeploymentOrder = var1;
      this._postSet(12, var2, var1);
   }

   public String getDeploymentPrincipalName() {
      return this._DeploymentPrincipalName;
   }

   public boolean isDeploymentPrincipalNameSet() {
      return this._isSet(13);
   }

   public void setDeploymentPrincipalName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      SecurityLegalHelper.validatePrincipalName(var1);
      String var2 = this._DeploymentPrincipalName;
      this._DeploymentPrincipalName = var1;
      this._postSet(13, var2, var1);
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
         var1 = 12;
      }

      try {
         switch (var1) {
            case 12:
               this._DeploymentOrder = 100;
               if (var2) {
                  break;
               }
            case 13:
               this._DeploymentPrincipalName = null;
               if (var2) {
                  break;
               }
            case 10:
               this._SourcePath = null;
               if (var2) {
                  break;
               }
            case 11:
               this._SubDeployments = new SubDeploymentMBean[0];
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
      return "BasicDeployment";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("DeploymentOrder")) {
         int var5 = this._DeploymentOrder;
         this._DeploymentOrder = (Integer)var2;
         this._postSet(12, var5, this._DeploymentOrder);
      } else {
         String var4;
         if (var1.equals("DeploymentPrincipalName")) {
            var4 = this._DeploymentPrincipalName;
            this._DeploymentPrincipalName = (String)var2;
            this._postSet(13, var4, this._DeploymentPrincipalName);
         } else if (var1.equals("SourcePath")) {
            var4 = this._SourcePath;
            this._SourcePath = (String)var2;
            this._postSet(10, var4, this._SourcePath);
         } else if (var1.equals("SubDeployments")) {
            SubDeploymentMBean[] var3 = this._SubDeployments;
            this._SubDeployments = (SubDeploymentMBean[])((SubDeploymentMBean[])var2);
            this._postSet(11, var3, this._SubDeployments);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("DeploymentOrder")) {
         return new Integer(this._DeploymentOrder);
      } else if (var1.equals("DeploymentPrincipalName")) {
         return this._DeploymentPrincipalName;
      } else if (var1.equals("SourcePath")) {
         return this._SourcePath;
      } else {
         return var1.equals("SubDeployments") ? this._SubDeployments : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends TargetInfoMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 11:
               if (var1.equals("source-path")) {
                  return 10;
               }
               break;
            case 14:
               if (var1.equals("sub-deployment")) {
                  return 11;
               }
               break;
            case 16:
               if (var1.equals("deployment-order")) {
                  return 12;
               }
               break;
            case 25:
               if (var1.equals("deployment-principal-name")) {
                  return 13;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 11:
               return new SubDeploymentMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 10:
               return "source-path";
            case 11:
               return "sub-deployment";
            case 12:
               return "deployment-order";
            case 13:
               return "deployment-principal-name";
            default:
               return super.getElementName(var1);
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
            case 11:
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends TargetInfoMBeanImpl.Helper {
      private BasicDeploymentMBeanImpl bean;

      protected Helper(BasicDeploymentMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 10:
               return "SourcePath";
            case 11:
               return "SubDeployments";
            case 12:
               return "DeploymentOrder";
            case 13:
               return "DeploymentPrincipalName";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DeploymentOrder")) {
            return 12;
         } else if (var1.equals("DeploymentPrincipalName")) {
            return 13;
         } else if (var1.equals("SourcePath")) {
            return 10;
         } else {
            return var1.equals("SubDeployments") ? 11 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getSubDeployments()));
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
            if (this.bean.isDeploymentOrderSet()) {
               var2.append("DeploymentOrder");
               var2.append(String.valueOf(this.bean.getDeploymentOrder()));
            }

            if (this.bean.isDeploymentPrincipalNameSet()) {
               var2.append("DeploymentPrincipalName");
               var2.append(String.valueOf(this.bean.getDeploymentPrincipalName()));
            }

            if (this.bean.isSourcePathSet()) {
               var2.append("SourcePath");
               var2.append(String.valueOf(this.bean.getSourcePath()));
            }

            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getSubDeployments().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSubDeployments()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
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
            BasicDeploymentMBeanImpl var2 = (BasicDeploymentMBeanImpl)var1;
            this.computeDiff("DeploymentOrder", this.bean.getDeploymentOrder(), var2.getDeploymentOrder(), false);
            this.computeDiff("DeploymentPrincipalName", this.bean.getDeploymentPrincipalName(), var2.getDeploymentPrincipalName(), false);
            this.computeDiff("SourcePath", this.bean.getSourcePath(), var2.getSourcePath(), false);
            this.computeChildDiff("SubDeployments", this.bean.getSubDeployments(), var2.getSubDeployments(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            BasicDeploymentMBeanImpl var3 = (BasicDeploymentMBeanImpl)var1.getSourceBean();
            BasicDeploymentMBeanImpl var4 = (BasicDeploymentMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DeploymentOrder")) {
                  var3.setDeploymentOrder(var4.getDeploymentOrder());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("DeploymentPrincipalName")) {
                  var3.setDeploymentPrincipalName(var4.getDeploymentPrincipalName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("SourcePath")) {
                  var3.setSourcePath(var4.getSourcePath());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("SubDeployments")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addSubDeployment((SubDeploymentMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeSubDeployment((SubDeploymentMBean)var2.getRemovedObject());
                  }

                  if (var3.getSubDeployments() == null || var3.getSubDeployments().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  }
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
            BasicDeploymentMBeanImpl var5 = (BasicDeploymentMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DeploymentOrder")) && this.bean.isDeploymentOrderSet()) {
               var5.setDeploymentOrder(this.bean.getDeploymentOrder());
            }

            if ((var3 == null || !var3.contains("DeploymentPrincipalName")) && this.bean.isDeploymentPrincipalNameSet()) {
               var5.setDeploymentPrincipalName(this.bean.getDeploymentPrincipalName());
            }

            if ((var3 == null || !var3.contains("SourcePath")) && this.bean.isSourcePathSet()) {
               var5.setSourcePath(this.bean.getSourcePath());
            }

            if ((var3 == null || !var3.contains("SubDeployments")) && this.bean.isSubDeploymentsSet() && !var5._isSet(11)) {
               SubDeploymentMBean[] var6 = this.bean.getSubDeployments();
               SubDeploymentMBean[] var7 = new SubDeploymentMBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (SubDeploymentMBean)((SubDeploymentMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setSubDeployments(var7);
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
         this.inferSubTree(this.bean.getSubDeployments(), var1, var2);
      }
   }
}
