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

public class WebserviceSecurityConfigurationMBeanImpl extends ConfigurationMBeanImpl implements WebserviceSecurityConfigurationMBean, Serializable {
   private String _ClassName;
   private ConfigurationPropertyMBean[] _ConfigurationProperties;
   private String _TokenType;
   private static SchemaHelper2 _schemaHelper;

   public WebserviceSecurityConfigurationMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WebserviceSecurityConfigurationMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getClassName() {
      return this._ClassName;
   }

   public boolean isClassNameSet() {
      return this._isSet(7);
   }

   public void setClassName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ClassName;
      this._ClassName = var1;
      this._postSet(7, var2, var1);
   }

   public void setTokenType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TokenType;
      this._TokenType = var1;
      this._postSet(8, var2, var1);
   }

   public String getTokenType() {
      return this._TokenType;
   }

   public boolean isTokenTypeSet() {
      return this._isSet(8);
   }

   public void addConfigurationProperty(ConfigurationPropertyMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 9)) {
         ConfigurationPropertyMBean[] var2;
         if (this._isSet(9)) {
            var2 = (ConfigurationPropertyMBean[])((ConfigurationPropertyMBean[])this._getHelper()._extendArray(this.getConfigurationProperties(), ConfigurationPropertyMBean.class, var1));
         } else {
            var2 = new ConfigurationPropertyMBean[]{var1};
         }

         try {
            this.setConfigurationProperties(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ConfigurationPropertyMBean[] getConfigurationProperties() {
      return this._ConfigurationProperties;
   }

   public boolean isConfigurationPropertiesSet() {
      return this._isSet(9);
   }

   public void removeConfigurationProperty(ConfigurationPropertyMBean var1) {
      this.destroyConfigurationProperty(var1);
   }

   public void setConfigurationProperties(ConfigurationPropertyMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ConfigurationPropertyMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 9)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      ConfigurationPropertyMBean[] var5 = this._ConfigurationProperties;
      this._ConfigurationProperties = (ConfigurationPropertyMBean[])var4;
      this._postSet(9, var5, var4);
   }

   public ConfigurationPropertyMBean lookupConfigurationProperty(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ConfigurationProperties).iterator();

      ConfigurationPropertyMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ConfigurationPropertyMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public ConfigurationPropertyMBean createConfigurationProperty(String var1) {
      ConfigurationPropertyMBeanImpl var2 = new ConfigurationPropertyMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addConfigurationProperty(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyConfigurationProperty(ConfigurationPropertyMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 9);
         ConfigurationPropertyMBean[] var2 = this.getConfigurationProperties();
         ConfigurationPropertyMBean[] var3 = (ConfigurationPropertyMBean[])((ConfigurationPropertyMBean[])this._getHelper()._removeElement(var2, ConfigurationPropertyMBean.class, var1));
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
               this.setConfigurationProperties(var3);
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

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("ClassName", this.isClassNameSet());
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("TokenType", this.isTokenTypeSet());
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
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._ClassName = null;
               if (var2) {
                  break;
               }
            case 9:
               this._ConfigurationProperties = new ConfigurationPropertyMBean[0];
               if (var2) {
                  break;
               }
            case 8:
               this._TokenType = null;
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
      return "WebserviceSecurityConfiguration";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("ClassName")) {
         var3 = this._ClassName;
         this._ClassName = (String)var2;
         this._postSet(7, var3, this._ClassName);
      } else if (var1.equals("ConfigurationProperties")) {
         ConfigurationPropertyMBean[] var4 = this._ConfigurationProperties;
         this._ConfigurationProperties = (ConfigurationPropertyMBean[])((ConfigurationPropertyMBean[])var2);
         this._postSet(9, var4, this._ConfigurationProperties);
      } else if (var1.equals("TokenType")) {
         var3 = this._TokenType;
         this._TokenType = (String)var2;
         this._postSet(8, var3, this._TokenType);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ClassName")) {
         return this._ClassName;
      } else if (var1.equals("ConfigurationProperties")) {
         return this._ConfigurationProperties;
      } else {
         return var1.equals("TokenType") ? this._TokenType : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 10:
               if (var1.equals("class-name")) {
                  return 7;
               }

               if (var1.equals("token-type")) {
                  return 8;
               }
               break;
            case 22:
               if (var1.equals("configuration-property")) {
                  return 9;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 9:
               return new ConfigurationPropertyMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "class-name";
            case 8:
               return "token-type";
            case 9:
               return "configuration-property";
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
            case 9:
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private WebserviceSecurityConfigurationMBeanImpl bean;

      protected Helper(WebserviceSecurityConfigurationMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "ClassName";
            case 8:
               return "TokenType";
            case 9:
               return "ConfigurationProperties";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ClassName")) {
            return 7;
         } else if (var1.equals("ConfigurationProperties")) {
            return 9;
         } else {
            return var1.equals("TokenType") ? 8 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getConfigurationProperties()));
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
            if (this.bean.isClassNameSet()) {
               var2.append("ClassName");
               var2.append(String.valueOf(this.bean.getClassName()));
            }

            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getConfigurationProperties().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getConfigurationProperties()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isTokenTypeSet()) {
               var2.append("TokenType");
               var2.append(String.valueOf(this.bean.getTokenType()));
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
            WebserviceSecurityConfigurationMBeanImpl var2 = (WebserviceSecurityConfigurationMBeanImpl)var1;
            this.computeDiff("ClassName", this.bean.getClassName(), var2.getClassName(), true);
            this.computeChildDiff("ConfigurationProperties", this.bean.getConfigurationProperties(), var2.getConfigurationProperties(), true);
            this.computeDiff("TokenType", this.bean.getTokenType(), var2.getTokenType(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebserviceSecurityConfigurationMBeanImpl var3 = (WebserviceSecurityConfigurationMBeanImpl)var1.getSourceBean();
            WebserviceSecurityConfigurationMBeanImpl var4 = (WebserviceSecurityConfigurationMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ClassName")) {
                  var3.setClassName(var4.getClassName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("ConfigurationProperties")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addConfigurationProperty((ConfigurationPropertyMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeConfigurationProperty((ConfigurationPropertyMBean)var2.getRemovedObject());
                  }

                  if (var3.getConfigurationProperties() == null || var3.getConfigurationProperties().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  }
               } else if (var5.equals("TokenType")) {
                  var3.setTokenType(var4.getTokenType());
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
            WebserviceSecurityConfigurationMBeanImpl var5 = (WebserviceSecurityConfigurationMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ClassName")) && this.bean.isClassNameSet()) {
               var5.setClassName(this.bean.getClassName());
            }

            if ((var3 == null || !var3.contains("ConfigurationProperties")) && this.bean.isConfigurationPropertiesSet() && !var5._isSet(9)) {
               ConfigurationPropertyMBean[] var6 = this.bean.getConfigurationProperties();
               ConfigurationPropertyMBean[] var7 = new ConfigurationPropertyMBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (ConfigurationPropertyMBean)((ConfigurationPropertyMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setConfigurationProperties(var7);
            }

            if ((var3 == null || !var3.contains("TokenType")) && this.bean.isTokenTypeSet()) {
               var5.setTokenType(this.bean.getTokenType());
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
         this.inferSubTree(this.bean.getConfigurationProperties(), var1, var2);
      }
   }
}
