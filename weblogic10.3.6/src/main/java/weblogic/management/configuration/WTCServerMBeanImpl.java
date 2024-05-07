package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.WTCServer;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class WTCServerMBeanImpl extends DeploymentMBeanImpl implements WTCServerMBean, Serializable {
   private WTCExportMBean[] _Exports;
   private WTCImportMBean[] _Imports;
   private WTCLocalTuxDomMBean[] _LocalTuxDoms;
   private String _Name;
   private WTCPasswordMBean[] _Passwords;
   private WTCRemoteTuxDomMBean[] _RemoteTuxDoms;
   private WTCResourcesMBean _Resource;
   private WTCResourcesMBean _Resources;
   private WTCExportMBean[] _WTCExports;
   private WTCImportMBean[] _WTCImports;
   private WTCLocalTuxDomMBean[] _WTCLocalTuxDoms;
   private WTCPasswordMBean[] _WTCPasswords;
   private WTCRemoteTuxDomMBean[] _WTCRemoteTuxDoms;
   private WTCResourcesMBean _WTCResources;
   private WTCtBridgeGlobalMBean _WTCtBridgeGlobal;
   private WTCtBridgeRedirectMBean[] _WTCtBridgeRedirects;
   private WTCServer _customizer;
   private WTCtBridgeGlobalMBean _tBridgeGlobal;
   private WTCtBridgeRedirectMBean[] _tBridgeRedirects;
   private static SchemaHelper2 _schemaHelper;

   public WTCServerMBeanImpl() {
      try {
         this._customizer = new WTCServer(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public WTCServerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new WTCServer(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public void addWTCLocalTuxDom(WTCLocalTuxDomMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 9)) {
         WTCLocalTuxDomMBean[] var2;
         if (this._isSet(9)) {
            var2 = (WTCLocalTuxDomMBean[])((WTCLocalTuxDomMBean[])this._getHelper()._extendArray(this.getWTCLocalTuxDoms(), WTCLocalTuxDomMBean.class, var1));
         } else {
            var2 = new WTCLocalTuxDomMBean[]{var1};
         }

         try {
            this.setWTCLocalTuxDoms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

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

   public WTCLocalTuxDomMBean[] getWTCLocalTuxDoms() {
      return this._WTCLocalTuxDoms;
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isWTCLocalTuxDomsSet() {
      return this._isSet(9);
   }

   public void removeWTCLocalTuxDom(WTCLocalTuxDomMBean var1) {
      this.destroyWTCLocalTuxDom(var1);
   }

   public void setWTCLocalTuxDoms(WTCLocalTuxDomMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WTCLocalTuxDomMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 9)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      WTCLocalTuxDomMBean[] var5 = this._WTCLocalTuxDoms;
      this._WTCLocalTuxDoms = (WTCLocalTuxDomMBean[])var4;
      this._postSet(9, var5, var4);
   }

   public WTCLocalTuxDomMBean createWTCLocalTuxDom(String var1) {
      WTCLocalTuxDomMBeanImpl var2 = new WTCLocalTuxDomMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWTCLocalTuxDom(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
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

   public void destroyWTCLocalTuxDom(WTCLocalTuxDomMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 9);
         WTCLocalTuxDomMBean[] var2 = this.getWTCLocalTuxDoms();
         WTCLocalTuxDomMBean[] var3 = (WTCLocalTuxDomMBean[])((WTCLocalTuxDomMBean[])this._getHelper()._removeElement(var2, WTCLocalTuxDomMBean.class, var1));
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
               this.setWTCLocalTuxDoms(var3);
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

   public WTCLocalTuxDomMBean lookupWTCLocalTuxDom(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WTCLocalTuxDoms).iterator();

      WTCLocalTuxDomMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WTCLocalTuxDomMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addLocalTuxDom(WTCLocalTuxDomMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 10)) {
         WTCLocalTuxDomMBean[] var2 = (WTCLocalTuxDomMBean[])((WTCLocalTuxDomMBean[])this._getHelper()._extendArray(this.getLocalTuxDoms(), WTCLocalTuxDomMBean.class, var1));

         try {
            this.setLocalTuxDoms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WTCLocalTuxDomMBean[] getLocalTuxDoms() {
      return this._customizer.getLocalTuxDoms();
   }

   public boolean isLocalTuxDomsSet() {
      return this._isSet(10);
   }

   public void removeLocalTuxDom(WTCLocalTuxDomMBean var1) {
      WTCLocalTuxDomMBean[] var2 = this.getLocalTuxDoms();
      WTCLocalTuxDomMBean[] var3 = (WTCLocalTuxDomMBean[])((WTCLocalTuxDomMBean[])this._getHelper()._removeElement(var2, WTCLocalTuxDomMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setLocalTuxDoms(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setLocalTuxDoms(WTCLocalTuxDomMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new WTCLocalTuxDomMBeanImpl[0] : var1;
      this._LocalTuxDoms = (WTCLocalTuxDomMBean[])var2;
   }

   public void addWTCRemoteTuxDom(WTCRemoteTuxDomMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 11)) {
         WTCRemoteTuxDomMBean[] var2;
         if (this._isSet(11)) {
            var2 = (WTCRemoteTuxDomMBean[])((WTCRemoteTuxDomMBean[])this._getHelper()._extendArray(this.getWTCRemoteTuxDoms(), WTCRemoteTuxDomMBean.class, var1));
         } else {
            var2 = new WTCRemoteTuxDomMBean[]{var1};
         }

         try {
            this.setWTCRemoteTuxDoms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WTCRemoteTuxDomMBean[] getWTCRemoteTuxDoms() {
      return this._WTCRemoteTuxDoms;
   }

   public boolean isWTCRemoteTuxDomsSet() {
      return this._isSet(11);
   }

   public void removeWTCRemoteTuxDom(WTCRemoteTuxDomMBean var1) {
      this.destroyWTCRemoteTuxDom(var1);
   }

   public void setWTCRemoteTuxDoms(WTCRemoteTuxDomMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WTCRemoteTuxDomMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 11)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      WTCRemoteTuxDomMBean[] var5 = this._WTCRemoteTuxDoms;
      this._WTCRemoteTuxDoms = (WTCRemoteTuxDomMBean[])var4;
      this._postSet(11, var5, var4);
   }

   public WTCRemoteTuxDomMBean createWTCRemoteTuxDom(String var1) {
      WTCRemoteTuxDomMBeanImpl var2 = new WTCRemoteTuxDomMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWTCRemoteTuxDom(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWTCRemoteTuxDom(WTCRemoteTuxDomMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 11);
         WTCRemoteTuxDomMBean[] var2 = this.getWTCRemoteTuxDoms();
         WTCRemoteTuxDomMBean[] var3 = (WTCRemoteTuxDomMBean[])((WTCRemoteTuxDomMBean[])this._getHelper()._removeElement(var2, WTCRemoteTuxDomMBean.class, var1));
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
               this.setWTCRemoteTuxDoms(var3);
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

   public WTCRemoteTuxDomMBean lookupWTCRemoteTuxDom(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WTCRemoteTuxDoms).iterator();

      WTCRemoteTuxDomMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WTCRemoteTuxDomMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addRemoteTuxDom(WTCRemoteTuxDomMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 12)) {
         WTCRemoteTuxDomMBean[] var2 = (WTCRemoteTuxDomMBean[])((WTCRemoteTuxDomMBean[])this._getHelper()._extendArray(this.getRemoteTuxDoms(), WTCRemoteTuxDomMBean.class, var1));

         try {
            this.setRemoteTuxDoms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WTCRemoteTuxDomMBean[] getRemoteTuxDoms() {
      return this._customizer.getRemoteTuxDoms();
   }

   public boolean isRemoteTuxDomsSet() {
      return this._isSet(12);
   }

   public void removeRemoteTuxDom(WTCRemoteTuxDomMBean var1) {
      WTCRemoteTuxDomMBean[] var2 = this.getRemoteTuxDoms();
      WTCRemoteTuxDomMBean[] var3 = (WTCRemoteTuxDomMBean[])((WTCRemoteTuxDomMBean[])this._getHelper()._removeElement(var2, WTCRemoteTuxDomMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setRemoteTuxDoms(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setRemoteTuxDoms(WTCRemoteTuxDomMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new WTCRemoteTuxDomMBeanImpl[0] : var1;
      this._RemoteTuxDoms = (WTCRemoteTuxDomMBean[])var2;
   }

   public void addWTCExport(WTCExportMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 13)) {
         WTCExportMBean[] var2;
         if (this._isSet(13)) {
            var2 = (WTCExportMBean[])((WTCExportMBean[])this._getHelper()._extendArray(this.getWTCExports(), WTCExportMBean.class, var1));
         } else {
            var2 = new WTCExportMBean[]{var1};
         }

         try {
            this.setWTCExports(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WTCExportMBean[] getWTCExports() {
      return this._WTCExports;
   }

   public boolean isWTCExportsSet() {
      return this._isSet(13);
   }

   public void removeWTCExport(WTCExportMBean var1) {
      this.destroyWTCExport(var1);
   }

   public void setWTCExports(WTCExportMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WTCExportMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 13)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      WTCExportMBean[] var5 = this._WTCExports;
      this._WTCExports = (WTCExportMBean[])var4;
      this._postSet(13, var5, var4);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public WTCExportMBean createWTCExport(String var1) {
      WTCExportMBeanImpl var2 = new WTCExportMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWTCExport(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void destroyWTCExport(WTCExportMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 13);
         WTCExportMBean[] var2 = this.getWTCExports();
         WTCExportMBean[] var3 = (WTCExportMBean[])((WTCExportMBean[])this._getHelper()._removeElement(var2, WTCExportMBean.class, var1));
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
               this.setWTCExports(var3);
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

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public WTCExportMBean lookupWTCExport(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WTCExports).iterator();

      WTCExportMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WTCExportMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addExport(WTCExportMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 14)) {
         WTCExportMBean[] var2 = (WTCExportMBean[])((WTCExportMBean[])this._getHelper()._extendArray(this.getExports(), WTCExportMBean.class, var1));

         try {
            this.setExports(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WTCExportMBean[] getExports() {
      return this._customizer.getExports();
   }

   public boolean isExportsSet() {
      return this._isSet(14);
   }

   public void removeExport(WTCExportMBean var1) {
      WTCExportMBean[] var2 = this.getExports();
      WTCExportMBean[] var3 = (WTCExportMBean[])((WTCExportMBean[])this._getHelper()._removeElement(var2, WTCExportMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setExports(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setExports(WTCExportMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new WTCExportMBeanImpl[0] : var1;
      this._Exports = (WTCExportMBean[])var2;
   }

   public void addWTCImport(WTCImportMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 15)) {
         WTCImportMBean[] var2;
         if (this._isSet(15)) {
            var2 = (WTCImportMBean[])((WTCImportMBean[])this._getHelper()._extendArray(this.getWTCImports(), WTCImportMBean.class, var1));
         } else {
            var2 = new WTCImportMBean[]{var1};
         }

         try {
            this.setWTCImports(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WTCImportMBean[] getWTCImports() {
      return this._WTCImports;
   }

   public boolean isWTCImportsSet() {
      return this._isSet(15);
   }

   public void removeWTCImport(WTCImportMBean var1) {
      this.destroyWTCImport(var1);
   }

   public void setWTCImports(WTCImportMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WTCImportMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 15)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      WTCImportMBean[] var5 = this._WTCImports;
      this._WTCImports = (WTCImportMBean[])var4;
      this._postSet(15, var5, var4);
   }

   public WTCImportMBean createWTCImport(String var1) {
      WTCImportMBeanImpl var2 = new WTCImportMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWTCImport(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWTCImport(WTCImportMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 15);
         WTCImportMBean[] var2 = this.getWTCImports();
         WTCImportMBean[] var3 = (WTCImportMBean[])((WTCImportMBean[])this._getHelper()._removeElement(var2, WTCImportMBean.class, var1));
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
               this.setWTCImports(var3);
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

   public WTCImportMBean lookupWTCImport(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WTCImports).iterator();

      WTCImportMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WTCImportMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addImport(WTCImportMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 16)) {
         WTCImportMBean[] var2 = (WTCImportMBean[])((WTCImportMBean[])this._getHelper()._extendArray(this.getImports(), WTCImportMBean.class, var1));

         try {
            this.setImports(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WTCImportMBean[] getImports() {
      return this._customizer.getImports();
   }

   public boolean isImportsSet() {
      return this._isSet(16);
   }

   public void removeImport(WTCImportMBean var1) {
      WTCImportMBean[] var2 = this.getImports();
      WTCImportMBean[] var3 = (WTCImportMBean[])((WTCImportMBean[])this._getHelper()._removeElement(var2, WTCImportMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setImports(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setImports(WTCImportMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new WTCImportMBeanImpl[0] : var1;
      this._Imports = (WTCImportMBean[])var2;
   }

   public void addWTCPassword(WTCPasswordMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 17)) {
         WTCPasswordMBean[] var2;
         if (this._isSet(17)) {
            var2 = (WTCPasswordMBean[])((WTCPasswordMBean[])this._getHelper()._extendArray(this.getWTCPasswords(), WTCPasswordMBean.class, var1));
         } else {
            var2 = new WTCPasswordMBean[]{var1};
         }

         try {
            this.setWTCPasswords(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WTCPasswordMBean[] getWTCPasswords() {
      return this._WTCPasswords;
   }

   public boolean isWTCPasswordsSet() {
      return this._isSet(17);
   }

   public void removeWTCPassword(WTCPasswordMBean var1) {
      this.destroyWTCPassword(var1);
   }

   public void setWTCPasswords(WTCPasswordMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WTCPasswordMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 17)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      WTCPasswordMBean[] var5 = this._WTCPasswords;
      this._WTCPasswords = (WTCPasswordMBean[])var4;
      this._postSet(17, var5, var4);
   }

   public WTCPasswordMBean createWTCPassword(String var1) {
      WTCPasswordMBeanImpl var2 = new WTCPasswordMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWTCPassword(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWTCPassword(WTCPasswordMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 17);
         WTCPasswordMBean[] var2 = this.getWTCPasswords();
         WTCPasswordMBean[] var3 = (WTCPasswordMBean[])((WTCPasswordMBean[])this._getHelper()._removeElement(var2, WTCPasswordMBean.class, var1));
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
               this.setWTCPasswords(var3);
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

   public WTCPasswordMBean lookupWTCPassword(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WTCPasswords).iterator();

      WTCPasswordMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WTCPasswordMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addPassword(WTCPasswordMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 18)) {
         WTCPasswordMBean[] var2 = (WTCPasswordMBean[])((WTCPasswordMBean[])this._getHelper()._extendArray(this.getPasswords(), WTCPasswordMBean.class, var1));

         try {
            this.setPasswords(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WTCPasswordMBean[] getPasswords() {
      return this._customizer.getPasswords();
   }

   public boolean isPasswordsSet() {
      return this._isSet(18);
   }

   public void removePassword(WTCPasswordMBean var1) {
      WTCPasswordMBean[] var2 = this.getPasswords();
      WTCPasswordMBean[] var3 = (WTCPasswordMBean[])((WTCPasswordMBean[])this._getHelper()._removeElement(var2, WTCPasswordMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setPasswords(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setPasswords(WTCPasswordMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new WTCPasswordMBeanImpl[0] : var1;
      this._Passwords = (WTCPasswordMBean[])var2;
   }

   public WTCResourcesMBean getWTCResources() {
      return this._WTCResources;
   }

   public boolean isWTCResourcesSet() {
      return this._isSet(19);
   }

   public void setWTCResources(WTCResourcesMBean var1) throws InvalidAttributeValueException {
      if (var1 != null && this.getWTCResources() != null && var1 != this.getWTCResources()) {
         throw new BeanAlreadyExistsException(this.getWTCResources() + " has already been created");
      } else {
         if (var1 != null) {
            AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
            if (this._setParent(var2, this, 19)) {
               this._getReferenceManager().registerBean(var2, false);
               this._postCreate(var2);
            }
         }

         WTCResourcesMBean var3 = this._WTCResources;
         this._WTCResources = var1;
         this._postSet(19, var3, var1);
      }
   }

   public WTCResourcesMBean getResources() {
      return this._customizer.getResources();
   }

   public boolean isResourcesSet() {
      return this._isSet(20);
   }

   public void setResources(WTCResourcesMBean var1) throws InvalidAttributeValueException {
      this._Resources = var1;
   }

   public WTCResourcesMBean createWTCResources(String var1) throws InstanceAlreadyExistsException {
      WTCResourcesMBeanImpl var2 = new WTCResourcesMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.setWTCResources(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof InstanceAlreadyExistsException) {
            throw (InstanceAlreadyExistsException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWTCResources(WTCResourcesMBean var1) {
      try {
         AbstractDescriptorBean var2 = (AbstractDescriptorBean)this._WTCResources;
         if (var2 != null) {
            List var3 = this._getReferenceManager().getResolvedReferences(var2);
            if (var3 != null && var3.size() > 0) {
               throw new BeanRemoveRejectedException(var2, var3);
            } else {
               this._getReferenceManager().unregisterBean(var2);
               this._markDestroyed(var2);
               this.setWTCResources((WTCResourcesMBean)null);
               this._unSet(19);
            }
         }
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public WTCtBridgeGlobalMBean getWTCtBridgeGlobal() {
      return this._WTCtBridgeGlobal;
   }

   public boolean isWTCtBridgeGlobalSet() {
      return this._isSet(21);
   }

   public void setWTCtBridgeGlobal(WTCtBridgeGlobalMBean var1) throws InvalidAttributeValueException {
      if (var1 != null && this.getWTCtBridgeGlobal() != null && var1 != this.getWTCtBridgeGlobal()) {
         throw new BeanAlreadyExistsException(this.getWTCtBridgeGlobal() + " has already been created");
      } else {
         if (var1 != null) {
            AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
            if (this._setParent(var2, this, 21)) {
               this._getReferenceManager().registerBean(var2, false);
               this._postCreate(var2);
            }
         }

         WTCtBridgeGlobalMBean var3 = this._WTCtBridgeGlobal;
         this._WTCtBridgeGlobal = var1;
         this._postSet(21, var3, var1);
      }
   }

   public WTCtBridgeGlobalMBean createWTCtBridgeGlobal() throws InstanceAlreadyExistsException {
      WTCtBridgeGlobalMBeanImpl var1 = new WTCtBridgeGlobalMBeanImpl(this, -1);

      try {
         this.setWTCtBridgeGlobal(var1);
         return var1;
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else if (var3 instanceof InstanceAlreadyExistsException) {
            throw (InstanceAlreadyExistsException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void destroyWTCtBridgeGlobal() {
      try {
         AbstractDescriptorBean var1 = (AbstractDescriptorBean)this._WTCtBridgeGlobal;
         if (var1 != null) {
            List var2 = this._getReferenceManager().getResolvedReferences(var1);
            if (var2 != null && var2.size() > 0) {
               throw new BeanRemoveRejectedException(var1, var2);
            } else {
               this._getReferenceManager().unregisterBean(var1);
               this._markDestroyed(var1);
               this.setWTCtBridgeGlobal((WTCtBridgeGlobalMBean)null);
               this._unSet(21);
            }
         }
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public WTCResourcesMBean getResource() {
      return this._Resource;
   }

   public boolean isResourceSet() {
      return this._isSet(22);
   }

   public void setResource(WTCResourcesMBean var1) throws InvalidAttributeValueException {
      this._Resource = var1;
   }

   public WTCtBridgeGlobalMBean gettBridgeGlobal() {
      return this._tBridgeGlobal;
   }

   public boolean istBridgeGlobalSet() {
      return this._isSet(23);
   }

   public void settBridgeGlobal(WTCtBridgeGlobalMBean var1) throws InvalidAttributeValueException {
      this._tBridgeGlobal = var1;
   }

   public void addtBridgeRedirect(WTCtBridgeRedirectMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 24)) {
         WTCtBridgeRedirectMBean[] var2 = (WTCtBridgeRedirectMBean[])((WTCtBridgeRedirectMBean[])this._getHelper()._extendArray(this.gettBridgeRedirects(), WTCtBridgeRedirectMBean.class, var1));

         try {
            this.settBridgeRedirects(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WTCtBridgeRedirectMBean[] gettBridgeRedirects() {
      return this._customizer.gettBridgeRedirects();
   }

   public boolean istBridgeRedirectsSet() {
      return this._isSet(24);
   }

   public void removetBridgeRedirect(WTCtBridgeRedirectMBean var1) {
      WTCtBridgeRedirectMBean[] var2 = this.gettBridgeRedirects();
      WTCtBridgeRedirectMBean[] var3 = (WTCtBridgeRedirectMBean[])((WTCtBridgeRedirectMBean[])this._getHelper()._removeElement(var2, WTCtBridgeRedirectMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.settBridgeRedirects(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void settBridgeRedirects(WTCtBridgeRedirectMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new WTCtBridgeRedirectMBeanImpl[0] : var1;
      this._tBridgeRedirects = (WTCtBridgeRedirectMBean[])var2;
   }

   public void addWTCtBridgeRedirect(WTCtBridgeRedirectMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 25)) {
         WTCtBridgeRedirectMBean[] var2;
         if (this._isSet(25)) {
            var2 = (WTCtBridgeRedirectMBean[])((WTCtBridgeRedirectMBean[])this._getHelper()._extendArray(this.getWTCtBridgeRedirects(), WTCtBridgeRedirectMBean.class, var1));
         } else {
            var2 = new WTCtBridgeRedirectMBean[]{var1};
         }

         try {
            this.setWTCtBridgeRedirects(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WTCtBridgeRedirectMBean[] getWTCtBridgeRedirects() {
      return this._WTCtBridgeRedirects;
   }

   public boolean isWTCtBridgeRedirectsSet() {
      return this._isSet(25);
   }

   public void removeWTCtBridgeRedirect(WTCtBridgeRedirectMBean var1) {
      this.destroyWTCtBridgeRedirect(var1);
   }

   public void setWTCtBridgeRedirects(WTCtBridgeRedirectMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WTCtBridgeRedirectMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 25)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      WTCtBridgeRedirectMBean[] var5 = this._WTCtBridgeRedirects;
      this._WTCtBridgeRedirects = (WTCtBridgeRedirectMBean[])var4;
      this._postSet(25, var5, var4);
   }

   public WTCtBridgeRedirectMBean createWTCtBridgeRedirect(String var1) {
      WTCtBridgeRedirectMBeanImpl var2 = new WTCtBridgeRedirectMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWTCtBridgeRedirect(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWTCtBridgeRedirect(WTCtBridgeRedirectMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 25);
         WTCtBridgeRedirectMBean[] var2 = this.getWTCtBridgeRedirects();
         WTCtBridgeRedirectMBean[] var3 = (WTCtBridgeRedirectMBean[])((WTCtBridgeRedirectMBean[])this._getHelper()._removeElement(var2, WTCtBridgeRedirectMBean.class, var1));
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
               this.setWTCtBridgeRedirects(var3);
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

   public WTCtBridgeRedirectMBean lookupWTCtBridgeRedirect(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WTCtBridgeRedirects).iterator();

      WTCtBridgeRedirectMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WTCtBridgeRedirectMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
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
         var1 = 14;
      }

      try {
         switch (var1) {
            case 14:
               this._Exports = new WTCExportMBean[0];
               if (var2) {
                  break;
               }
            case 16:
               this._Imports = new WTCImportMBean[0];
               if (var2) {
                  break;
               }
            case 10:
               this._LocalTuxDoms = new WTCLocalTuxDomMBean[0];
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 18:
               this._Passwords = new WTCPasswordMBean[0];
               if (var2) {
                  break;
               }
            case 12:
               this._RemoteTuxDoms = new WTCRemoteTuxDomMBean[0];
               if (var2) {
                  break;
               }
            case 22:
               this._Resource = null;
               if (var2) {
                  break;
               }
            case 20:
               this._Resources = null;
               if (var2) {
                  break;
               }
            case 13:
               this._WTCExports = new WTCExportMBean[0];
               if (var2) {
                  break;
               }
            case 15:
               this._WTCImports = new WTCImportMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._WTCLocalTuxDoms = new WTCLocalTuxDomMBean[0];
               if (var2) {
                  break;
               }
            case 17:
               this._WTCPasswords = new WTCPasswordMBean[0];
               if (var2) {
                  break;
               }
            case 11:
               this._WTCRemoteTuxDoms = new WTCRemoteTuxDomMBean[0];
               if (var2) {
                  break;
               }
            case 19:
               this._WTCResources = null;
               if (var2) {
                  break;
               }
            case 21:
               this._WTCtBridgeGlobal = null;
               if (var2) {
                  break;
               }
            case 25:
               this._WTCtBridgeRedirects = new WTCtBridgeRedirectMBean[0];
               if (var2) {
                  break;
               }
            case 23:
               this._tBridgeGlobal = null;
               if (var2) {
                  break;
               }
            case 24:
               this._tBridgeRedirects = new WTCtBridgeRedirectMBean[0];
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
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
      return "WTCServer";
   }

   public void putValue(String var1, Object var2) {
      WTCExportMBean[] var11;
      if (var1.equals("Exports")) {
         var11 = this._Exports;
         this._Exports = (WTCExportMBean[])((WTCExportMBean[])var2);
         this._postSet(14, var11, this._Exports);
      } else {
         WTCImportMBean[] var10;
         if (var1.equals("Imports")) {
            var10 = this._Imports;
            this._Imports = (WTCImportMBean[])((WTCImportMBean[])var2);
            this._postSet(16, var10, this._Imports);
         } else {
            WTCLocalTuxDomMBean[] var9;
            if (var1.equals("LocalTuxDoms")) {
               var9 = this._LocalTuxDoms;
               this._LocalTuxDoms = (WTCLocalTuxDomMBean[])((WTCLocalTuxDomMBean[])var2);
               this._postSet(10, var9, this._LocalTuxDoms);
            } else if (var1.equals("Name")) {
               String var12 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var12, this._Name);
            } else {
               WTCPasswordMBean[] var8;
               if (var1.equals("Passwords")) {
                  var8 = this._Passwords;
                  this._Passwords = (WTCPasswordMBean[])((WTCPasswordMBean[])var2);
                  this._postSet(18, var8, this._Passwords);
               } else {
                  WTCRemoteTuxDomMBean[] var7;
                  if (var1.equals("RemoteTuxDoms")) {
                     var7 = this._RemoteTuxDoms;
                     this._RemoteTuxDoms = (WTCRemoteTuxDomMBean[])((WTCRemoteTuxDomMBean[])var2);
                     this._postSet(12, var7, this._RemoteTuxDoms);
                  } else {
                     WTCResourcesMBean var6;
                     if (var1.equals("Resource")) {
                        var6 = this._Resource;
                        this._Resource = (WTCResourcesMBean)var2;
                        this._postSet(22, var6, this._Resource);
                     } else if (var1.equals("Resources")) {
                        var6 = this._Resources;
                        this._Resources = (WTCResourcesMBean)var2;
                        this._postSet(20, var6, this._Resources);
                     } else if (var1.equals("WTCExports")) {
                        var11 = this._WTCExports;
                        this._WTCExports = (WTCExportMBean[])((WTCExportMBean[])var2);
                        this._postSet(13, var11, this._WTCExports);
                     } else if (var1.equals("WTCImports")) {
                        var10 = this._WTCImports;
                        this._WTCImports = (WTCImportMBean[])((WTCImportMBean[])var2);
                        this._postSet(15, var10, this._WTCImports);
                     } else if (var1.equals("WTCLocalTuxDoms")) {
                        var9 = this._WTCLocalTuxDoms;
                        this._WTCLocalTuxDoms = (WTCLocalTuxDomMBean[])((WTCLocalTuxDomMBean[])var2);
                        this._postSet(9, var9, this._WTCLocalTuxDoms);
                     } else if (var1.equals("WTCPasswords")) {
                        var8 = this._WTCPasswords;
                        this._WTCPasswords = (WTCPasswordMBean[])((WTCPasswordMBean[])var2);
                        this._postSet(17, var8, this._WTCPasswords);
                     } else if (var1.equals("WTCRemoteTuxDoms")) {
                        var7 = this._WTCRemoteTuxDoms;
                        this._WTCRemoteTuxDoms = (WTCRemoteTuxDomMBean[])((WTCRemoteTuxDomMBean[])var2);
                        this._postSet(11, var7, this._WTCRemoteTuxDoms);
                     } else if (var1.equals("WTCResources")) {
                        var6 = this._WTCResources;
                        this._WTCResources = (WTCResourcesMBean)var2;
                        this._postSet(19, var6, this._WTCResources);
                     } else {
                        WTCtBridgeGlobalMBean var4;
                        if (var1.equals("WTCtBridgeGlobal")) {
                           var4 = this._WTCtBridgeGlobal;
                           this._WTCtBridgeGlobal = (WTCtBridgeGlobalMBean)var2;
                           this._postSet(21, var4, this._WTCtBridgeGlobal);
                        } else {
                           WTCtBridgeRedirectMBean[] var3;
                           if (var1.equals("WTCtBridgeRedirects")) {
                              var3 = this._WTCtBridgeRedirects;
                              this._WTCtBridgeRedirects = (WTCtBridgeRedirectMBean[])((WTCtBridgeRedirectMBean[])var2);
                              this._postSet(25, var3, this._WTCtBridgeRedirects);
                           } else if (var1.equals("customizer")) {
                              WTCServer var5 = this._customizer;
                              this._customizer = (WTCServer)var2;
                           } else if (var1.equals("tBridgeGlobal")) {
                              var4 = this._tBridgeGlobal;
                              this._tBridgeGlobal = (WTCtBridgeGlobalMBean)var2;
                              this._postSet(23, var4, this._tBridgeGlobal);
                           } else if (var1.equals("tBridgeRedirects")) {
                              var3 = this._tBridgeRedirects;
                              this._tBridgeRedirects = (WTCtBridgeRedirectMBean[])((WTCtBridgeRedirectMBean[])var2);
                              this._postSet(24, var3, this._tBridgeRedirects);
                           } else {
                              super.putValue(var1, var2);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Exports")) {
         return this._Exports;
      } else if (var1.equals("Imports")) {
         return this._Imports;
      } else if (var1.equals("LocalTuxDoms")) {
         return this._LocalTuxDoms;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Passwords")) {
         return this._Passwords;
      } else if (var1.equals("RemoteTuxDoms")) {
         return this._RemoteTuxDoms;
      } else if (var1.equals("Resource")) {
         return this._Resource;
      } else if (var1.equals("Resources")) {
         return this._Resources;
      } else if (var1.equals("WTCExports")) {
         return this._WTCExports;
      } else if (var1.equals("WTCImports")) {
         return this._WTCImports;
      } else if (var1.equals("WTCLocalTuxDoms")) {
         return this._WTCLocalTuxDoms;
      } else if (var1.equals("WTCPasswords")) {
         return this._WTCPasswords;
      } else if (var1.equals("WTCRemoteTuxDoms")) {
         return this._WTCRemoteTuxDoms;
      } else if (var1.equals("WTCResources")) {
         return this._WTCResources;
      } else if (var1.equals("WTCtBridgeGlobal")) {
         return this._WTCtBridgeGlobal;
      } else if (var1.equals("WTCtBridgeRedirects")) {
         return this._WTCtBridgeRedirects;
      } else if (var1.equals("customizer")) {
         return this._customizer;
      } else if (var1.equals("tBridgeGlobal")) {
         return this._tBridgeGlobal;
      } else {
         return var1.equals("tBridgeRedirects") ? this._tBridgeRedirects : super.getValue(var1);
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
            case 16:
            case 19:
            default:
               break;
            case 6:
               if (var1.equals("export")) {
                  return 14;
               }

               if (var1.equals("import")) {
                  return 16;
               }
               break;
            case 8:
               if (var1.equals("password")) {
                  return 18;
               }

               if (var1.equals("resource")) {
                  return 22;
               }
               break;
            case 9:
               if (var1.equals("resources")) {
                  return 20;
               }
               break;
            case 10:
               if (var1.equals("wtc-export")) {
                  return 13;
               }

               if (var1.equals("wtc-import")) {
                  return 15;
               }
               break;
            case 12:
               if (var1.equals("wtc-password")) {
                  return 17;
               }
               break;
            case 13:
               if (var1.equals("local-tux-dom")) {
                  return 10;
               }

               if (var1.equals("wtc-resources")) {
                  return 19;
               }
               break;
            case 14:
               if (var1.equals("remote-tux-dom")) {
                  return 12;
               }
               break;
            case 15:
               if (var1.equals("t-bridge-global")) {
                  return 23;
               }
               break;
            case 17:
               if (var1.equals("wtc-local-tux-dom")) {
                  return 9;
               }

               if (var1.equals("t-bridge-redirect")) {
                  return 24;
               }
               break;
            case 18:
               if (var1.equals("wtc-remote-tux-dom")) {
                  return 11;
               }

               if (var1.equals("wtc-tbridge-global")) {
                  return 21;
               }
               break;
            case 20:
               if (var1.equals("wtc-tbridge-redirect")) {
                  return 25;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 9:
               return new WTCLocalTuxDomMBeanImpl.SchemaHelper2();
            case 10:
            case 12:
            case 14:
            case 16:
            case 18:
            case 20:
            case 22:
            case 23:
            case 24:
            default:
               return super.getSchemaHelper(var1);
            case 11:
               return new WTCRemoteTuxDomMBeanImpl.SchemaHelper2();
            case 13:
               return new WTCExportMBeanImpl.SchemaHelper2();
            case 15:
               return new WTCImportMBeanImpl.SchemaHelper2();
            case 17:
               return new WTCPasswordMBeanImpl.SchemaHelper2();
            case 19:
               return new WTCResourcesMBeanImpl.SchemaHelper2();
            case 21:
               return new WTCtBridgeGlobalMBeanImpl.SchemaHelper2();
            case 25:
               return new WTCtBridgeRedirectMBeanImpl.SchemaHelper2();
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
            case 7:
            case 8:
            default:
               return super.getElementName(var1);
            case 9:
               return "wtc-local-tux-dom";
            case 10:
               return "local-tux-dom";
            case 11:
               return "wtc-remote-tux-dom";
            case 12:
               return "remote-tux-dom";
            case 13:
               return "wtc-export";
            case 14:
               return "export";
            case 15:
               return "wtc-import";
            case 16:
               return "import";
            case 17:
               return "wtc-password";
            case 18:
               return "password";
            case 19:
               return "wtc-resources";
            case 20:
               return "resources";
            case 21:
               return "wtc-tbridge-global";
            case 22:
               return "resource";
            case 23:
               return "t-bridge-global";
            case 24:
               return "t-bridge-redirect";
            case 25:
               return "wtc-tbridge-redirect";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            default:
               return super.isArray(var1);
            case 9:
               return true;
            case 10:
               return true;
            case 11:
               return true;
            case 12:
               return true;
            case 13:
               return true;
            case 14:
               return true;
            case 15:
               return true;
            case 16:
               return true;
            case 17:
               return true;
            case 18:
               return true;
            case 24:
               return true;
            case 25:
               return true;
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 9:
               return true;
            case 10:
            case 12:
            case 14:
            case 16:
            case 18:
            case 20:
            case 22:
            case 23:
            case 24:
            default:
               return super.isBean(var1);
            case 11:
               return true;
            case 13:
               return true;
            case 15:
               return true;
            case 17:
               return true;
            case 19:
               return true;
            case 21:
               return true;
            case 25:
               return true;
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
      private WTCServerMBeanImpl bean;

      protected Helper(WTCServerMBeanImpl var1) {
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
            case 7:
            case 8:
            default:
               return super.getPropertyName(var1);
            case 9:
               return "WTCLocalTuxDoms";
            case 10:
               return "LocalTuxDoms";
            case 11:
               return "WTCRemoteTuxDoms";
            case 12:
               return "RemoteTuxDoms";
            case 13:
               return "WTCExports";
            case 14:
               return "Exports";
            case 15:
               return "WTCImports";
            case 16:
               return "Imports";
            case 17:
               return "WTCPasswords";
            case 18:
               return "Passwords";
            case 19:
               return "WTCResources";
            case 20:
               return "Resources";
            case 21:
               return "WTCtBridgeGlobal";
            case 22:
               return "Resource";
            case 23:
               return "tBridgeGlobal";
            case 24:
               return "tBridgeRedirects";
            case 25:
               return "WTCtBridgeRedirects";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Exports")) {
            return 14;
         } else if (var1.equals("Imports")) {
            return 16;
         } else if (var1.equals("LocalTuxDoms")) {
            return 10;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Passwords")) {
            return 18;
         } else if (var1.equals("RemoteTuxDoms")) {
            return 12;
         } else if (var1.equals("Resource")) {
            return 22;
         } else if (var1.equals("Resources")) {
            return 20;
         } else if (var1.equals("WTCExports")) {
            return 13;
         } else if (var1.equals("WTCImports")) {
            return 15;
         } else if (var1.equals("WTCLocalTuxDoms")) {
            return 9;
         } else if (var1.equals("WTCPasswords")) {
            return 17;
         } else if (var1.equals("WTCRemoteTuxDoms")) {
            return 11;
         } else if (var1.equals("WTCResources")) {
            return 19;
         } else if (var1.equals("WTCtBridgeGlobal")) {
            return 21;
         } else if (var1.equals("WTCtBridgeRedirects")) {
            return 25;
         } else if (var1.equals("tBridgeGlobal")) {
            return 23;
         } else {
            return var1.equals("tBridgeRedirects") ? 24 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getWTCExports()));
         var1.add(new ArrayIterator(this.bean.getWTCImports()));
         var1.add(new ArrayIterator(this.bean.getWTCLocalTuxDoms()));
         var1.add(new ArrayIterator(this.bean.getWTCPasswords()));
         var1.add(new ArrayIterator(this.bean.getWTCRemoteTuxDoms()));
         if (this.bean.getWTCResources() != null) {
            var1.add(new ArrayIterator(new WTCResourcesMBean[]{this.bean.getWTCResources()}));
         }

         if (this.bean.getWTCtBridgeGlobal() != null) {
            var1.add(new ArrayIterator(new WTCtBridgeGlobalMBean[]{this.bean.getWTCtBridgeGlobal()}));
         }

         var1.add(new ArrayIterator(this.bean.getWTCtBridgeRedirects()));
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
            if (this.bean.isExportsSet()) {
               var2.append("Exports");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getExports())));
            }

            if (this.bean.isImportsSet()) {
               var2.append("Imports");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getImports())));
            }

            if (this.bean.isLocalTuxDomsSet()) {
               var2.append("LocalTuxDoms");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getLocalTuxDoms())));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPasswordsSet()) {
               var2.append("Passwords");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPasswords())));
            }

            if (this.bean.isRemoteTuxDomsSet()) {
               var2.append("RemoteTuxDoms");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getRemoteTuxDoms())));
            }

            if (this.bean.isResourceSet()) {
               var2.append("Resource");
               var2.append(String.valueOf(this.bean.getResource()));
            }

            if (this.bean.isResourcesSet()) {
               var2.append("Resources");
               var2.append(String.valueOf(this.bean.getResources()));
            }

            var5 = 0L;

            int var7;
            for(var7 = 0; var7 < this.bean.getWTCExports().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWTCExports()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWTCImports().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWTCImports()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWTCLocalTuxDoms().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWTCLocalTuxDoms()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWTCPasswords().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWTCPasswords()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWTCRemoteTuxDoms().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWTCRemoteTuxDoms()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getWTCResources());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getWTCtBridgeGlobal());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWTCtBridgeRedirects().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWTCtBridgeRedirects()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.istBridgeGlobalSet()) {
               var2.append("tBridgeGlobal");
               var2.append(String.valueOf(this.bean.gettBridgeGlobal()));
            }

            if (this.bean.istBridgeRedirectsSet()) {
               var2.append("tBridgeRedirects");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.gettBridgeRedirects())));
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
            WTCServerMBeanImpl var2 = (WTCServerMBeanImpl)var1;
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeChildDiff("WTCExports", this.bean.getWTCExports(), var2.getWTCExports(), true);
            this.computeChildDiff("WTCImports", this.bean.getWTCImports(), var2.getWTCImports(), true);
            this.computeChildDiff("WTCLocalTuxDoms", this.bean.getWTCLocalTuxDoms(), var2.getWTCLocalTuxDoms(), false);
            this.computeChildDiff("WTCPasswords", this.bean.getWTCPasswords(), var2.getWTCPasswords(), false);
            this.computeChildDiff("WTCRemoteTuxDoms", this.bean.getWTCRemoteTuxDoms(), var2.getWTCRemoteTuxDoms(), false);
            this.computeChildDiff("WTCResources", this.bean.getWTCResources(), var2.getWTCResources(), false);
            this.computeChildDiff("WTCtBridgeGlobal", this.bean.getWTCtBridgeGlobal(), var2.getWTCtBridgeGlobal(), false);
            this.computeChildDiff("WTCtBridgeRedirects", this.bean.getWTCtBridgeRedirects(), var2.getWTCtBridgeRedirects(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WTCServerMBeanImpl var3 = (WTCServerMBeanImpl)var1.getSourceBean();
            WTCServerMBeanImpl var4 = (WTCServerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("Exports") && !var5.equals("Imports") && !var5.equals("LocalTuxDoms")) {
                  if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (!var5.equals("Passwords") && !var5.equals("RemoteTuxDoms") && !var5.equals("Resource") && !var5.equals("Resources")) {
                     if (var5.equals("WTCExports")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addWTCExport((WTCExportMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeWTCExport((WTCExportMBean)var2.getRemovedObject());
                        }

                        if (var3.getWTCExports() == null || var3.getWTCExports().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                        }
                     } else if (var5.equals("WTCImports")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addWTCImport((WTCImportMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeWTCImport((WTCImportMBean)var2.getRemovedObject());
                        }

                        if (var3.getWTCImports() == null || var3.getWTCImports().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                        }
                     } else if (var5.equals("WTCLocalTuxDoms")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addWTCLocalTuxDom((WTCLocalTuxDomMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeWTCLocalTuxDom((WTCLocalTuxDomMBean)var2.getRemovedObject());
                        }

                        if (var3.getWTCLocalTuxDoms() == null || var3.getWTCLocalTuxDoms().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                        }
                     } else if (var5.equals("WTCPasswords")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addWTCPassword((WTCPasswordMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeWTCPassword((WTCPasswordMBean)var2.getRemovedObject());
                        }

                        if (var3.getWTCPasswords() == null || var3.getWTCPasswords().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                        }
                     } else if (var5.equals("WTCRemoteTuxDoms")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addWTCRemoteTuxDom((WTCRemoteTuxDomMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeWTCRemoteTuxDom((WTCRemoteTuxDomMBean)var2.getRemovedObject());
                        }

                        if (var3.getWTCRemoteTuxDoms() == null || var3.getWTCRemoteTuxDoms().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                        }
                     } else if (var5.equals("WTCResources")) {
                        if (var6 == 2) {
                           var3.setWTCResources((WTCResourcesMBean)this.createCopy((AbstractDescriptorBean)var4.getWTCResources()));
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3._destroySingleton("WTCResources", var3.getWTCResources());
                        }

                        var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                     } else if (var5.equals("WTCtBridgeGlobal")) {
                        if (var6 == 2) {
                           var3.setWTCtBridgeGlobal((WTCtBridgeGlobalMBean)this.createCopy((AbstractDescriptorBean)var4.getWTCtBridgeGlobal()));
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3._destroySingleton("WTCtBridgeGlobal", var3.getWTCtBridgeGlobal());
                        }

                        var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                     } else if (var5.equals("WTCtBridgeRedirects")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addWTCtBridgeRedirect((WTCtBridgeRedirectMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeWTCtBridgeRedirect((WTCtBridgeRedirectMBean)var2.getRemovedObject());
                        }

                        if (var3.getWTCtBridgeRedirects() == null || var3.getWTCtBridgeRedirects().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 25);
                        }
                     } else if (!var5.equals("tBridgeGlobal") && !var5.equals("tBridgeRedirects")) {
                        super.applyPropertyUpdate(var1, var2);
                     }
                  }
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
            WTCServerMBeanImpl var5 = (WTCServerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            int var8;
            if ((var3 == null || !var3.contains("WTCExports")) && this.bean.isWTCExportsSet() && !var5._isSet(13)) {
               WTCExportMBean[] var6 = this.bean.getWTCExports();
               WTCExportMBean[] var7 = new WTCExportMBean[var6.length];

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (WTCExportMBean)((WTCExportMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setWTCExports(var7);
            }

            if ((var3 == null || !var3.contains("WTCImports")) && this.bean.isWTCImportsSet() && !var5._isSet(15)) {
               WTCImportMBean[] var12 = this.bean.getWTCImports();
               WTCImportMBean[] var15 = new WTCImportMBean[var12.length];

               for(var8 = 0; var8 < var15.length; ++var8) {
                  var15[var8] = (WTCImportMBean)((WTCImportMBean)this.createCopy((AbstractDescriptorBean)var12[var8], var2));
               }

               var5.setWTCImports(var15);
            }

            if ((var3 == null || !var3.contains("WTCLocalTuxDoms")) && this.bean.isWTCLocalTuxDomsSet() && !var5._isSet(9)) {
               WTCLocalTuxDomMBean[] var13 = this.bean.getWTCLocalTuxDoms();
               WTCLocalTuxDomMBean[] var17 = new WTCLocalTuxDomMBean[var13.length];

               for(var8 = 0; var8 < var17.length; ++var8) {
                  var17[var8] = (WTCLocalTuxDomMBean)((WTCLocalTuxDomMBean)this.createCopy((AbstractDescriptorBean)var13[var8], var2));
               }

               var5.setWTCLocalTuxDoms(var17);
            }

            if ((var3 == null || !var3.contains("WTCPasswords")) && this.bean.isWTCPasswordsSet() && !var5._isSet(17)) {
               WTCPasswordMBean[] var14 = this.bean.getWTCPasswords();
               WTCPasswordMBean[] var19 = new WTCPasswordMBean[var14.length];

               for(var8 = 0; var8 < var19.length; ++var8) {
                  var19[var8] = (WTCPasswordMBean)((WTCPasswordMBean)this.createCopy((AbstractDescriptorBean)var14[var8], var2));
               }

               var5.setWTCPasswords(var19);
            }

            if ((var3 == null || !var3.contains("WTCRemoteTuxDoms")) && this.bean.isWTCRemoteTuxDomsSet() && !var5._isSet(11)) {
               WTCRemoteTuxDomMBean[] var16 = this.bean.getWTCRemoteTuxDoms();
               WTCRemoteTuxDomMBean[] var20 = new WTCRemoteTuxDomMBean[var16.length];

               for(var8 = 0; var8 < var20.length; ++var8) {
                  var20[var8] = (WTCRemoteTuxDomMBean)((WTCRemoteTuxDomMBean)this.createCopy((AbstractDescriptorBean)var16[var8], var2));
               }

               var5.setWTCRemoteTuxDoms(var20);
            }

            if ((var3 == null || !var3.contains("WTCResources")) && this.bean.isWTCResourcesSet() && !var5._isSet(19)) {
               WTCResourcesMBean var4 = this.bean.getWTCResources();
               var5.setWTCResources((WTCResourcesMBean)null);
               var5.setWTCResources(var4 == null ? null : (WTCResourcesMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("WTCtBridgeGlobal")) && this.bean.isWTCtBridgeGlobalSet() && !var5._isSet(21)) {
               WTCtBridgeGlobalMBean var11 = this.bean.getWTCtBridgeGlobal();
               var5.setWTCtBridgeGlobal((WTCtBridgeGlobalMBean)null);
               var5.setWTCtBridgeGlobal(var11 == null ? null : (WTCtBridgeGlobalMBean)this.createCopy((AbstractDescriptorBean)var11, var2));
            }

            if ((var3 == null || !var3.contains("WTCtBridgeRedirects")) && this.bean.isWTCtBridgeRedirectsSet() && !var5._isSet(25)) {
               WTCtBridgeRedirectMBean[] var18 = this.bean.getWTCtBridgeRedirects();
               WTCtBridgeRedirectMBean[] var21 = new WTCtBridgeRedirectMBean[var18.length];

               for(var8 = 0; var8 < var21.length; ++var8) {
                  var21[var8] = (WTCtBridgeRedirectMBean)((WTCtBridgeRedirectMBean)this.createCopy((AbstractDescriptorBean)var18[var8], var2));
               }

               var5.setWTCtBridgeRedirects(var21);
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
         this.inferSubTree(this.bean.getExports(), var1, var2);
         this.inferSubTree(this.bean.getImports(), var1, var2);
         this.inferSubTree(this.bean.getLocalTuxDoms(), var1, var2);
         this.inferSubTree(this.bean.getPasswords(), var1, var2);
         this.inferSubTree(this.bean.getRemoteTuxDoms(), var1, var2);
         this.inferSubTree(this.bean.getResource(), var1, var2);
         this.inferSubTree(this.bean.getResources(), var1, var2);
         this.inferSubTree(this.bean.getWTCExports(), var1, var2);
         this.inferSubTree(this.bean.getWTCImports(), var1, var2);
         this.inferSubTree(this.bean.getWTCLocalTuxDoms(), var1, var2);
         this.inferSubTree(this.bean.getWTCPasswords(), var1, var2);
         this.inferSubTree(this.bean.getWTCRemoteTuxDoms(), var1, var2);
         this.inferSubTree(this.bean.getWTCResources(), var1, var2);
         this.inferSubTree(this.bean.getWTCtBridgeGlobal(), var1, var2);
         this.inferSubTree(this.bean.getWTCtBridgeRedirects(), var1, var2);
         this.inferSubTree(this.bean.gettBridgeGlobal(), var1, var2);
         this.inferSubTree(this.bean.gettBridgeRedirects(), var1, var2);
      }
   }
}
