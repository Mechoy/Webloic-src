package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class PathServiceMBeanImpl extends DeploymentMBeanImpl implements PathServiceMBean, Serializable {
   private PersistentStoreMBean _PersistentStore;
   private static SchemaHelper2 _schemaHelper;

   public PathServiceMBeanImpl() {
      this._initializeProperty(-1);
   }

   public PathServiceMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public PersistentStoreMBean getPersistentStore() {
      return this._PersistentStore;
   }

   public String getPersistentStoreAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getPersistentStore();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isPersistentStoreSet() {
      return this._isSet(9);
   }

   public void setPersistentStoreAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, PersistentStoreMBean.class, new ReferenceManager.Resolver(this, 9) {
            public void resolveReference(Object var1) {
               try {
                  PathServiceMBeanImpl.this.setPersistentStore((PersistentStoreMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         PersistentStoreMBean var2 = this._PersistentStore;
         this._initializeProperty(9);
         this._postSet(9, var2, this._PersistentStore);
      }

   }

   public void setPersistentStore(PersistentStoreMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 9, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return PathServiceMBeanImpl.this.getPersistentStore();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      PersistentStoreMBean var3 = this._PersistentStore;
      this._PersistentStore = var1;
      this._postSet(9, var3, var1);
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
               this._PersistentStore = null;
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
      return "PathService";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("PersistentStore")) {
         PersistentStoreMBean var3 = this._PersistentStore;
         this._PersistentStore = (PersistentStoreMBean)var2;
         this._postSet(9, var3, this._PersistentStore);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      return var1.equals("PersistentStore") ? this._PersistentStore : super.getValue(var1);
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 16:
               if (var1.equals("persistent-store")) {
                  return 9;
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
            case 9:
               return "persistent-store";
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
      private PathServiceMBeanImpl bean;

      protected Helper(PathServiceMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 9:
               return "PersistentStore";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         return var1.equals("PersistentStore") ? 9 : super.getPropertyIndex(var1);
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
            if (this.bean.isPersistentStoreSet()) {
               var2.append("PersistentStore");
               var2.append(String.valueOf(this.bean.getPersistentStore()));
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
            PathServiceMBeanImpl var2 = (PathServiceMBeanImpl)var1;
            this.computeDiff("PersistentStore", this.bean.getPersistentStore(), var2.getPersistentStore(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            PathServiceMBeanImpl var3 = (PathServiceMBeanImpl)var1.getSourceBean();
            PathServiceMBeanImpl var4 = (PathServiceMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("PersistentStore")) {
                  var3.setPersistentStoreAsString(var4.getPersistentStoreAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
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
            PathServiceMBeanImpl var5 = (PathServiceMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("PersistentStore")) && this.bean.isPersistentStoreSet()) {
               var5._unSet(var5, 9);
               var5.setPersistentStoreAsString(this.bean.getPersistentStoreAsString());
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
         this.inferSubTree(this.bean.getPersistentStore(), var1, var2);
      }
   }
}
