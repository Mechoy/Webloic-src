package weblogic.management.security.credentials;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class DeployableCredentialMapperMBeanImpl extends CredentialMapperMBeanImpl implements DeployableCredentialMapperMBean, Serializable {
   private boolean _CredentialMappingDeploymentEnabled;
   private static SchemaHelper2 _schemaHelper;

   public DeployableCredentialMapperMBeanImpl() {
      this._initializeProperty(-1);
   }

   public DeployableCredentialMapperMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public boolean isCredentialMappingDeploymentEnabled() {
      return this._CredentialMappingDeploymentEnabled;
   }

   public boolean isCredentialMappingDeploymentEnabledSet() {
      return this._isSet(8);
   }

   public void setCredentialMappingDeploymentEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._CredentialMappingDeploymentEnabled;
      this._CredentialMappingDeploymentEnabled = var1;
      this._postSet(8, var2, var1);
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
         var1 = 8;
      }

      try {
         switch (var1) {
            case 8:
               this._CredentialMappingDeploymentEnabled = true;
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
      return "http://xmlns.oracle.com/weblogic/1.0/security.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/security";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String wls_getInterfaceClassName() {
      return "weblogic.management.security.credentials.DeployableCredentialMapperMBean";
   }

   public static class SchemaHelper2 extends CredentialMapperMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 37:
               if (var1.equals("credential-mapping-deployment-enabled")) {
                  return 8;
               }
            default:
               return super.getPropertyIndex(var1);
         }
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 8:
               return "credential-mapping-deployment-enabled";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            default:
               return super.isBean(var1);
         }
      }
   }

   protected static class Helper extends CredentialMapperMBeanImpl.Helper {
      private DeployableCredentialMapperMBeanImpl bean;

      protected Helper(DeployableCredentialMapperMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 8:
               return "CredentialMappingDeploymentEnabled";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         return var1.equals("CredentialMappingDeploymentEnabled") ? 8 : super.getPropertyIndex(var1);
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
            if (this.bean.isCredentialMappingDeploymentEnabledSet()) {
               var2.append("CredentialMappingDeploymentEnabled");
               var2.append(String.valueOf(this.bean.isCredentialMappingDeploymentEnabled()));
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
            DeployableCredentialMapperMBeanImpl var2 = (DeployableCredentialMapperMBeanImpl)var1;
            this.computeDiff("CredentialMappingDeploymentEnabled", this.bean.isCredentialMappingDeploymentEnabled(), var2.isCredentialMappingDeploymentEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            DeployableCredentialMapperMBeanImpl var3 = (DeployableCredentialMapperMBeanImpl)var1.getSourceBean();
            DeployableCredentialMapperMBeanImpl var4 = (DeployableCredentialMapperMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CredentialMappingDeploymentEnabled")) {
                  var3.setCredentialMappingDeploymentEnabled(var4.isCredentialMappingDeploymentEnabled());
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
            DeployableCredentialMapperMBeanImpl var5 = (DeployableCredentialMapperMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CredentialMappingDeploymentEnabled")) && this.bean.isCredentialMappingDeploymentEnabledSet()) {
               var5.setCredentialMappingDeploymentEnabled(this.bean.isCredentialMappingDeploymentEnabled());
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
      }
   }
}
