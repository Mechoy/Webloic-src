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
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.commo.AbstractCommoConfigurationBean;
import weblogic.utils.collections.CombinedIterator;

public class CredentialCacheMBeanImpl extends AbstractCommoConfigurationBean implements CredentialCacheMBean, Serializable {
   private int _CredentialCacheTTL;
   private boolean _CredentialCachingEnabled;
   private int _CredentialsCacheSize;
   private static SchemaHelper2 _schemaHelper;

   public CredentialCacheMBeanImpl() {
      this._initializeProperty(-1);
   }

   public CredentialCacheMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public boolean isCredentialCachingEnabled() {
      return this._CredentialCachingEnabled;
   }

   public boolean isCredentialCachingEnabledSet() {
      return this._isSet(2);
   }

   public void setCredentialCachingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._CredentialCachingEnabled;
      this._CredentialCachingEnabled = var1;
      this._postSet(2, var2, var1);
   }

   public int getCredentialsCacheSize() {
      return this._CredentialsCacheSize;
   }

   public boolean isCredentialsCacheSizeSet() {
      return this._isSet(3);
   }

   public void setCredentialsCacheSize(int var1) throws InvalidAttributeValueException {
      int var2 = this._CredentialsCacheSize;
      this._CredentialsCacheSize = var1;
      this._postSet(3, var2, var1);
   }

   public int getCredentialCacheTTL() {
      return this._CredentialCacheTTL;
   }

   public boolean isCredentialCacheTTLSet() {
      return this._isSet(4);
   }

   public void setCredentialCacheTTL(int var1) throws InvalidAttributeValueException {
      int var2 = this._CredentialCacheTTL;
      this._CredentialCacheTTL = var1;
      this._postSet(4, var2, var1);
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
         var1 = 4;
      }

      try {
         switch (var1) {
            case 4:
               this._CredentialCacheTTL = 600;
               if (var2) {
                  break;
               }
            case 3:
               this._CredentialsCacheSize = 100;
               if (var2) {
                  break;
               }
            case 2:
               this._CredentialCachingEnabled = false;
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
      return "weblogic.management.security.credentials.CredentialCacheMBean";
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 19:
               if (var1.equals("credential-cachettl")) {
                  return 4;
               }
               break;
            case 22:
               if (var1.equals("credentials-cache-size")) {
                  return 3;
               }
               break;
            case 26:
               if (var1.equals("credential-caching-enabled")) {
                  return 2;
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
               return "credential-caching-enabled";
            case 3:
               return "credentials-cache-size";
            case 4:
               return "credential-cachettl";
            default:
               return super.getElementName(var1);
         }
      }
   }

   protected static class Helper extends AbstractCommoConfigurationBean.Helper {
      private CredentialCacheMBeanImpl bean;

      protected Helper(CredentialCacheMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "CredentialCachingEnabled";
            case 3:
               return "CredentialsCacheSize";
            case 4:
               return "CredentialCacheTTL";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CredentialCacheTTL")) {
            return 4;
         } else if (var1.equals("CredentialsCacheSize")) {
            return 3;
         } else {
            return var1.equals("CredentialCachingEnabled") ? 2 : super.getPropertyIndex(var1);
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
            if (this.bean.isCredentialCacheTTLSet()) {
               var2.append("CredentialCacheTTL");
               var2.append(String.valueOf(this.bean.getCredentialCacheTTL()));
            }

            if (this.bean.isCredentialsCacheSizeSet()) {
               var2.append("CredentialsCacheSize");
               var2.append(String.valueOf(this.bean.getCredentialsCacheSize()));
            }

            if (this.bean.isCredentialCachingEnabledSet()) {
               var2.append("CredentialCachingEnabled");
               var2.append(String.valueOf(this.bean.isCredentialCachingEnabled()));
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
            CredentialCacheMBeanImpl var2 = (CredentialCacheMBeanImpl)var1;
            this.computeDiff("CredentialCacheTTL", this.bean.getCredentialCacheTTL(), var2.getCredentialCacheTTL(), false);
            this.computeDiff("CredentialsCacheSize", this.bean.getCredentialsCacheSize(), var2.getCredentialsCacheSize(), false);
            this.computeDiff("CredentialCachingEnabled", this.bean.isCredentialCachingEnabled(), var2.isCredentialCachingEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            CredentialCacheMBeanImpl var3 = (CredentialCacheMBeanImpl)var1.getSourceBean();
            CredentialCacheMBeanImpl var4 = (CredentialCacheMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CredentialCacheTTL")) {
                  var3.setCredentialCacheTTL(var4.getCredentialCacheTTL());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 4);
               } else if (var5.equals("CredentialsCacheSize")) {
                  var3.setCredentialsCacheSize(var4.getCredentialsCacheSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (var5.equals("CredentialCachingEnabled")) {
                  var3.setCredentialCachingEnabled(var4.isCredentialCachingEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
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
            CredentialCacheMBeanImpl var5 = (CredentialCacheMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CredentialCacheTTL")) && this.bean.isCredentialCacheTTLSet()) {
               var5.setCredentialCacheTTL(this.bean.getCredentialCacheTTL());
            }

            if ((var3 == null || !var3.contains("CredentialsCacheSize")) && this.bean.isCredentialsCacheSizeSet()) {
               var5.setCredentialsCacheSize(this.bean.getCredentialsCacheSize());
            }

            if ((var3 == null || !var3.contains("CredentialCachingEnabled")) && this.bean.isCredentialCachingEnabledSet()) {
               var5.setCredentialCachingEnabled(this.bean.isCredentialCachingEnabled());
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
