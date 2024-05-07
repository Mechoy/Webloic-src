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

public class DebugMBeanImpl extends ConfigurationMBeanImpl implements DebugMBean, Serializable {
   private DebugScopeMBean[] _DebugScopes;
   private static SchemaHelper2 _schemaHelper;

   public DebugMBeanImpl() {
      this._initializeProperty(-1);
   }

   public DebugMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void addDebugScope(DebugScopeMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         DebugScopeMBean[] var2;
         if (this._isSet(7)) {
            var2 = (DebugScopeMBean[])((DebugScopeMBean[])this._getHelper()._extendArray(this.getDebugScopes(), DebugScopeMBean.class, var1));
         } else {
            var2 = new DebugScopeMBean[]{var1};
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

   public DebugScopeMBean[] getDebugScopes() {
      return this._DebugScopes;
   }

   public boolean isDebugScopesSet() {
      return this._isSet(7);
   }

   public void removeDebugScope(DebugScopeMBean var1) {
      this.destroyDebugScope(var1);
   }

   public void setDebugScopes(DebugScopeMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new DebugScopeMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 7)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      DebugScopeMBean[] var5 = this._DebugScopes;
      this._DebugScopes = (DebugScopeMBean[])var4;
      this._postSet(7, var5, var4);
   }

   public DebugScopeMBean createDebugScope(String var1) {
      DebugScopeMBeanImpl var2 = new DebugScopeMBeanImpl(this, -1);

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

   public void destroyDebugScope(DebugScopeMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 7);
         DebugScopeMBean[] var2 = this.getDebugScopes();
         DebugScopeMBean[] var3 = (DebugScopeMBean[])((DebugScopeMBean[])this._getHelper()._removeElement(var2, DebugScopeMBean.class, var1));
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

   public DebugScopeMBean lookupDebugScope(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._DebugScopes).iterator();

      DebugScopeMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (DebugScopeMBeanImpl)var2.next();
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
               this._DebugScopes = new DebugScopeMBean[0];
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
      return "Debug";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("DebugScopes")) {
         DebugScopeMBean[] var3 = this._DebugScopes;
         this._DebugScopes = (DebugScopeMBean[])((DebugScopeMBean[])var2);
         this._postSet(7, var3, this._DebugScopes);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      return var1.equals("DebugScopes") ? this._DebugScopes : super.getValue(var1);
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 11:
               if (var1.equals("debug-scope")) {
                  return 7;
               }
            default:
               return super.getPropertyIndex(var1);
         }
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 7:
               return new DebugScopeMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "debug-scope";
            default:
               return super.getElementName(var1);
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
            case 7:
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
      private DebugMBeanImpl bean;

      protected Helper(DebugMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "DebugScopes";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         return var1.equals("DebugScopes") ? 7 : super.getPropertyIndex(var1);
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

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            DebugMBeanImpl var2 = (DebugMBeanImpl)var1;
            this.computeChildDiff("DebugScopes", this.bean.getDebugScopes(), var2.getDebugScopes(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            DebugMBeanImpl var3 = (DebugMBeanImpl)var1.getSourceBean();
            DebugMBeanImpl var4 = (DebugMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DebugScopes")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addDebugScope((DebugScopeMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeDebugScope((DebugScopeMBean)var2.getRemovedObject());
                  }

                  if (var3.getDebugScopes() == null || var3.getDebugScopes().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
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
            DebugMBeanImpl var5 = (DebugMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DebugScopes")) && this.bean.isDebugScopesSet() && !var5._isSet(7)) {
               DebugScopeMBean[] var6 = this.bean.getDebugScopes();
               DebugScopeMBean[] var7 = new DebugScopeMBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (DebugScopeMBean)((DebugScopeMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setDebugScopes(var7);
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
