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
import weblogic.j2ee.descriptor.wl.validators.WseeConfigBeanValidator;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class WebServicePersistenceMBeanImpl extends ConfigurationMBeanImpl implements WebServicePersistenceMBean, Serializable {
   private String _DefaultLogicalStoreName;
   private WebServiceLogicalStoreMBean[] _WebServiceLogicalStores;
   private WebServicePhysicalStoreMBean[] _WebServicePhysicalStores;
   private static SchemaHelper2 _schemaHelper;

   public WebServicePersistenceMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WebServicePersistenceMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getDefaultLogicalStoreName() {
      return this._DefaultLogicalStoreName;
   }

   public boolean isDefaultLogicalStoreNameSet() {
      return this._isSet(7);
   }

   public void setDefaultLogicalStoreName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      WseeConfigBeanValidator.validateDefaultLogicalStoreName(var1);
      String var2 = this._DefaultLogicalStoreName;
      this._DefaultLogicalStoreName = var1;
      this._postSet(7, var2, var1);
   }

   public WebServiceLogicalStoreMBean createWebServiceLogicalStore(String var1) {
      WebServiceLogicalStoreMBeanImpl var2 = new WebServiceLogicalStoreMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWebServiceLogicalStore(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWebServiceLogicalStore(WebServiceLogicalStoreMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 8);
         WebServiceLogicalStoreMBean[] var2 = this.getWebServiceLogicalStores();
         WebServiceLogicalStoreMBean[] var3 = (WebServiceLogicalStoreMBean[])((WebServiceLogicalStoreMBean[])this._getHelper()._removeElement(var2, WebServiceLogicalStoreMBean.class, var1));
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
               this.setWebServiceLogicalStores(var3);
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

   public void addWebServiceLogicalStore(WebServiceLogicalStoreMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 8)) {
         WebServiceLogicalStoreMBean[] var2;
         if (this._isSet(8)) {
            var2 = (WebServiceLogicalStoreMBean[])((WebServiceLogicalStoreMBean[])this._getHelper()._extendArray(this.getWebServiceLogicalStores(), WebServiceLogicalStoreMBean.class, var1));
         } else {
            var2 = new WebServiceLogicalStoreMBean[]{var1};
         }

         try {
            this.setWebServiceLogicalStores(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WebServiceLogicalStoreMBean[] getWebServiceLogicalStores() {
      return this._WebServiceLogicalStores;
   }

   public boolean isWebServiceLogicalStoresSet() {
      return this._isSet(8);
   }

   public void removeWebServiceLogicalStore(WebServiceLogicalStoreMBean var1) {
      this.destroyWebServiceLogicalStore(var1);
   }

   public void setWebServiceLogicalStores(WebServiceLogicalStoreMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WebServiceLogicalStoreMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 8)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WebServiceLogicalStoreMBean[] var5 = this._WebServiceLogicalStores;
      this._WebServiceLogicalStores = (WebServiceLogicalStoreMBean[])var4;
      this._postSet(8, var5, var4);
   }

   public WebServiceLogicalStoreMBean lookupWebServiceLogicalStore(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WebServiceLogicalStores).iterator();

      WebServiceLogicalStoreMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WebServiceLogicalStoreMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public WebServicePhysicalStoreMBean createWebServicePhysicalStore(String var1) {
      WebServicePhysicalStoreMBeanImpl var2 = new WebServicePhysicalStoreMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWebServicePhysicalStore(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWebServicePhysicalStore(WebServicePhysicalStoreMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 9);
         WebServicePhysicalStoreMBean[] var2 = this.getWebServicePhysicalStores();
         WebServicePhysicalStoreMBean[] var3 = (WebServicePhysicalStoreMBean[])((WebServicePhysicalStoreMBean[])this._getHelper()._removeElement(var2, WebServicePhysicalStoreMBean.class, var1));
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
               this.setWebServicePhysicalStores(var3);
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

   public void addWebServicePhysicalStore(WebServicePhysicalStoreMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 9)) {
         WebServicePhysicalStoreMBean[] var2;
         if (this._isSet(9)) {
            var2 = (WebServicePhysicalStoreMBean[])((WebServicePhysicalStoreMBean[])this._getHelper()._extendArray(this.getWebServicePhysicalStores(), WebServicePhysicalStoreMBean.class, var1));
         } else {
            var2 = new WebServicePhysicalStoreMBean[]{var1};
         }

         try {
            this.setWebServicePhysicalStores(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WebServicePhysicalStoreMBean[] getWebServicePhysicalStores() {
      return this._WebServicePhysicalStores;
   }

   public boolean isWebServicePhysicalStoresSet() {
      return this._isSet(9);
   }

   public void removeWebServicePhysicalStore(WebServicePhysicalStoreMBean var1) {
      this.destroyWebServicePhysicalStore(var1);
   }

   public void setWebServicePhysicalStores(WebServicePhysicalStoreMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WebServicePhysicalStoreMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 9)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WebServicePhysicalStoreMBean[] var5 = this._WebServicePhysicalStores;
      this._WebServicePhysicalStores = (WebServicePhysicalStoreMBean[])var4;
      this._postSet(9, var5, var4);
   }

   public WebServicePhysicalStoreMBean lookupWebServicePhysicalStore(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WebServicePhysicalStores).iterator();

      WebServicePhysicalStoreMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WebServicePhysicalStoreMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
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
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._DefaultLogicalStoreName = "WseeStore";
               if (var2) {
                  break;
               }
            case 8:
               this._WebServiceLogicalStores = new WebServiceLogicalStoreMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._WebServicePhysicalStores = new WebServicePhysicalStoreMBean[0];
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
      return "WebServicePersistence";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("DefaultLogicalStoreName")) {
         String var5 = this._DefaultLogicalStoreName;
         this._DefaultLogicalStoreName = (String)var2;
         this._postSet(7, var5, this._DefaultLogicalStoreName);
      } else if (var1.equals("WebServiceLogicalStores")) {
         WebServiceLogicalStoreMBean[] var4 = this._WebServiceLogicalStores;
         this._WebServiceLogicalStores = (WebServiceLogicalStoreMBean[])((WebServiceLogicalStoreMBean[])var2);
         this._postSet(8, var4, this._WebServiceLogicalStores);
      } else if (var1.equals("WebServicePhysicalStores")) {
         WebServicePhysicalStoreMBean[] var3 = this._WebServicePhysicalStores;
         this._WebServicePhysicalStores = (WebServicePhysicalStoreMBean[])((WebServicePhysicalStoreMBean[])var2);
         this._postSet(9, var3, this._WebServicePhysicalStores);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("DefaultLogicalStoreName")) {
         return this._DefaultLogicalStoreName;
      } else if (var1.equals("WebServiceLogicalStores")) {
         return this._WebServiceLogicalStores;
      } else {
         return var1.equals("WebServicePhysicalStores") ? this._WebServicePhysicalStores : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 25:
               if (var1.equals("web-service-logical-store")) {
                  return 8;
               }
               break;
            case 26:
               if (var1.equals("default-logical-store-name")) {
                  return 7;
               }

               if (var1.equals("web-service-physical-store")) {
                  return 9;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 8:
               return new WebServiceLogicalStoreMBeanImpl.SchemaHelper2();
            case 9:
               return new WebServicePhysicalStoreMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "default-logical-store-name";
            case 8:
               return "web-service-logical-store";
            case 9:
               return "web-service-physical-store";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 8:
               return true;
            case 9:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 8:
               return true;
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
      private WebServicePersistenceMBeanImpl bean;

      protected Helper(WebServicePersistenceMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "DefaultLogicalStoreName";
            case 8:
               return "WebServiceLogicalStores";
            case 9:
               return "WebServicePhysicalStores";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DefaultLogicalStoreName")) {
            return 7;
         } else if (var1.equals("WebServiceLogicalStores")) {
            return 8;
         } else {
            return var1.equals("WebServicePhysicalStores") ? 9 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getWebServiceLogicalStores()));
         var1.add(new ArrayIterator(this.bean.getWebServicePhysicalStores()));
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
            if (this.bean.isDefaultLogicalStoreNameSet()) {
               var2.append("DefaultLogicalStoreName");
               var2.append(String.valueOf(this.bean.getDefaultLogicalStoreName()));
            }

            var5 = 0L;

            int var7;
            for(var7 = 0; var7 < this.bean.getWebServiceLogicalStores().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWebServiceLogicalStores()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWebServicePhysicalStores().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWebServicePhysicalStores()[var7]);
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
            WebServicePersistenceMBeanImpl var2 = (WebServicePersistenceMBeanImpl)var1;
            this.computeDiff("DefaultLogicalStoreName", this.bean.getDefaultLogicalStoreName(), var2.getDefaultLogicalStoreName(), true);
            this.computeChildDiff("WebServiceLogicalStores", this.bean.getWebServiceLogicalStores(), var2.getWebServiceLogicalStores(), true);
            this.computeChildDiff("WebServicePhysicalStores", this.bean.getWebServicePhysicalStores(), var2.getWebServicePhysicalStores(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebServicePersistenceMBeanImpl var3 = (WebServicePersistenceMBeanImpl)var1.getSourceBean();
            WebServicePersistenceMBeanImpl var4 = (WebServicePersistenceMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DefaultLogicalStoreName")) {
                  var3.setDefaultLogicalStoreName(var4.getDefaultLogicalStoreName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("WebServiceLogicalStores")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addWebServiceLogicalStore((WebServiceLogicalStoreMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeWebServiceLogicalStore((WebServiceLogicalStoreMBean)var2.getRemovedObject());
                  }

                  if (var3.getWebServiceLogicalStores() == null || var3.getWebServiceLogicalStores().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  }
               } else if (var5.equals("WebServicePhysicalStores")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addWebServicePhysicalStore((WebServicePhysicalStoreMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeWebServicePhysicalStore((WebServicePhysicalStoreMBean)var2.getRemovedObject());
                  }

                  if (var3.getWebServicePhysicalStores() == null || var3.getWebServicePhysicalStores().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
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
            WebServicePersistenceMBeanImpl var5 = (WebServicePersistenceMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DefaultLogicalStoreName")) && this.bean.isDefaultLogicalStoreNameSet()) {
               var5.setDefaultLogicalStoreName(this.bean.getDefaultLogicalStoreName());
            }

            int var8;
            if ((var3 == null || !var3.contains("WebServiceLogicalStores")) && this.bean.isWebServiceLogicalStoresSet() && !var5._isSet(8)) {
               WebServiceLogicalStoreMBean[] var6 = this.bean.getWebServiceLogicalStores();
               WebServiceLogicalStoreMBean[] var7 = new WebServiceLogicalStoreMBean[var6.length];

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (WebServiceLogicalStoreMBean)((WebServiceLogicalStoreMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setWebServiceLogicalStores(var7);
            }

            if ((var3 == null || !var3.contains("WebServicePhysicalStores")) && this.bean.isWebServicePhysicalStoresSet() && !var5._isSet(9)) {
               WebServicePhysicalStoreMBean[] var11 = this.bean.getWebServicePhysicalStores();
               WebServicePhysicalStoreMBean[] var12 = new WebServicePhysicalStoreMBean[var11.length];

               for(var8 = 0; var8 < var12.length; ++var8) {
                  var12[var8] = (WebServicePhysicalStoreMBean)((WebServicePhysicalStoreMBean)this.createCopy((AbstractDescriptorBean)var11[var8], var2));
               }

               var5.setWebServicePhysicalStores(var12);
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
         this.inferSubTree(this.bean.getWebServiceLogicalStores(), var1, var2);
         this.inferSubTree(this.bean.getWebServicePhysicalStores(), var1, var2);
      }
   }
}
