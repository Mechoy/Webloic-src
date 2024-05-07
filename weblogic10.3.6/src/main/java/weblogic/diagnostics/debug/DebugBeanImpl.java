package weblogic.diagnostics.debug;

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
import weblogic.descriptor.beangen.LegalChecks;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class DebugBeanImpl extends AbstractDescriptorBean implements DebugBean, Serializable {
   private DebugScopeBean[] _DebugScopes;
   private String _Name;
   private static SchemaHelper2 _schemaHelper;

   public DebugBeanImpl() {
      this._initializeProperty(-1);
   }

   public DebugBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getName() {
      return this._Name;
   }

   public boolean isNameSet() {
      return this._isSet(0);
   }

   public void setName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      LegalChecks.checkNonEmptyString("Name", var1);
      LegalChecks.checkNonNull("Name", var1);
      String var2 = this._Name;
      this._Name = var1;
      this._postSet(0, var2, var1);
   }

   public void addDebugScope(DebugScopeBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 1)) {
         DebugScopeBean[] var2;
         if (this._isSet(1)) {
            var2 = (DebugScopeBean[])((DebugScopeBean[])this._getHelper()._extendArray(this.getDebugScopes(), DebugScopeBean.class, var1));
         } else {
            var2 = new DebugScopeBean[]{var1};
         }

         try {
            this.setDebugScopes(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public DebugScopeBean[] getDebugScopes() {
      return this._DebugScopes;
   }

   public boolean isDebugScopesSet() {
      return this._isSet(1);
   }

   public void removeDebugScope(DebugScopeBean var1) {
      this.destroyDebugScope(var1);
   }

   public void setDebugScopes(DebugScopeBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new DebugScopeBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 1)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      DebugScopeBean[] var5 = this._DebugScopes;
      this._DebugScopes = (DebugScopeBean[])var4;
      this._postSet(1, var5, var4);
   }

   public DebugScopeBean createDebugScope(String var1) {
      DebugScopeBeanImpl var2 = new DebugScopeBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addDebugScope(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyDebugScope(DebugScopeBean var1) {
      try {
         this._checkIsPotentialChild(var1, 1);
         DebugScopeBean[] var2 = this.getDebugScopes();
         DebugScopeBean[] var3 = (DebugScopeBean[])((DebugScopeBean[])this._getHelper()._removeElement(var2, DebugScopeBean.class, var1));
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
               this.setDebugScopes(var3);
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

   public DebugScopeBean lookupDebugScope(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._DebugScopes).iterator();

      DebugScopeBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (DebugScopeBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      LegalChecks.checkIsSet("Name", this.isNameSet());
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
         var1 = 1;
      }

      try {
         switch (var1) {
            case 1:
               this._DebugScopes = new DebugScopeBean[0];
               if (var2) {
                  break;
               }
            case 0:
               this._Name = null;
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

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics-debug";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 0;
               }
               break;
            case 11:
               if (var1.equals("debug-scope")) {
                  return 1;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 1:
               return new DebugScopeBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 0:
               return "name";
            case 1:
               return "debug-scope";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 1:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 1:
               return true;
            default:
               return super.isBean(var1);
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 0:
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

   protected static class Helper extends AbstractDescriptorBeanHelper {
      private DebugBeanImpl bean;

      protected Helper(DebugBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "Name";
            case 1:
               return "DebugScopes";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DebugScopes")) {
            return 1;
         } else {
            return var1.equals("Name") ? 0 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getDebugScopes()));
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
            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getDebugScopes().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getDebugScopes()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
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
            DebugBeanImpl var2 = (DebugBeanImpl)var1;
            this.computeChildDiff("DebugScopes", this.bean.getDebugScopes(), var2.getDebugScopes(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            DebugBeanImpl var3 = (DebugBeanImpl)var1.getSourceBean();
            DebugBeanImpl var4 = (DebugBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DebugScopes")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addDebugScope((DebugScopeBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeDebugScope((DebugScopeBean)var2.getRemovedObject());
                  }

                  if (var3.getDebugScopes() == null || var3.getDebugScopes().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 1);
                  }
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 0);
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
            DebugBeanImpl var5 = (DebugBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DebugScopes")) && this.bean.isDebugScopesSet() && !var5._isSet(1)) {
               DebugScopeBean[] var6 = this.bean.getDebugScopes();
               DebugScopeBean[] var7 = new DebugScopeBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (DebugScopeBean)((DebugScopeBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setDebugScopes(var7);
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
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
         this.inferSubTree(this.bean.getDebugScopes(), var1, var2);
      }
   }
}
