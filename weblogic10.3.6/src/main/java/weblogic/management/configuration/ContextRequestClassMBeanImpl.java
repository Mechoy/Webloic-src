package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
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

public class ContextRequestClassMBeanImpl extends DeploymentMBeanImpl implements ContextRequestClassMBean, Serializable {
   private ContextCaseMBean[] _ContextCases;
   private static SchemaHelper2 _schemaHelper;

   public ContextRequestClassMBeanImpl() {
      this._initializeProperty(-1);
   }

   public ContextRequestClassMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void addContextCase(ContextCaseMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 9)) {
         ContextCaseMBean[] var2;
         if (this._isSet(9)) {
            var2 = (ContextCaseMBean[])((ContextCaseMBean[])this._getHelper()._extendArray(this.getContextCases(), ContextCaseMBean.class, var1));
         } else {
            var2 = new ContextCaseMBean[]{var1};
         }

         try {
            this.setContextCases(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ContextCaseMBean[] getContextCases() {
      return this._ContextCases;
   }

   public boolean isContextCasesSet() {
      return this._isSet(9);
   }

   public void removeContextCase(ContextCaseMBean var1) {
      this.destroyContextCase(var1);
   }

   public void setContextCases(ContextCaseMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ContextCaseMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 9)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      ContextCaseMBean[] var5 = this._ContextCases;
      this._ContextCases = (ContextCaseMBean[])var4;
      this._postSet(9, var5, var4);
   }

   public ContextCaseMBean createContextCase(String var1) {
      ContextCaseMBeanImpl var2 = new ContextCaseMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addContextCase(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyContextCase(ContextCaseMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 9);
         ContextCaseMBean[] var2 = this.getContextCases();
         ContextCaseMBean[] var3 = (ContextCaseMBean[])((ContextCaseMBean[])this._getHelper()._removeElement(var2, ContextCaseMBean.class, var1));
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
               this.setContextCases(var3);
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
               this._ContextCases = new ContextCaseMBean[0];
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
      return "ContextRequestClass";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("ContextCases")) {
         ContextCaseMBean[] var3 = this._ContextCases;
         this._ContextCases = (ContextCaseMBean[])((ContextCaseMBean[])var2);
         this._postSet(9, var3, this._ContextCases);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      return var1.equals("ContextCases") ? this._ContextCases : super.getValue(var1);
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 12:
               if (var1.equals("context-case")) {
                  return 9;
               }
            default:
               return super.getPropertyIndex(var1);
         }
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 9:
               return new ContextCaseMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 9:
               return "context-case";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
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

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private ContextRequestClassMBeanImpl bean;

      protected Helper(ContextRequestClassMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 9:
               return "ContextCases";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         return var1.equals("ContextCases") ? 9 : super.getPropertyIndex(var1);
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getContextCases()));
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

            for(int var7 = 0; var7 < this.bean.getContextCases().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getContextCases()[var7]);
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
            ContextRequestClassMBeanImpl var2 = (ContextRequestClassMBeanImpl)var1;
            this.computeChildDiff("ContextCases", this.bean.getContextCases(), var2.getContextCases(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ContextRequestClassMBeanImpl var3 = (ContextRequestClassMBeanImpl)var1.getSourceBean();
            ContextRequestClassMBeanImpl var4 = (ContextRequestClassMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ContextCases")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addContextCase((ContextCaseMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeContextCase((ContextCaseMBean)var2.getRemovedObject());
                  }

                  if (var3.getContextCases() == null || var3.getContextCases().length == 0) {
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
            ContextRequestClassMBeanImpl var5 = (ContextRequestClassMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ContextCases")) && this.bean.isContextCasesSet() && !var5._isSet(9)) {
               ContextCaseMBean[] var6 = this.bean.getContextCases();
               ContextCaseMBean[] var7 = new ContextCaseMBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (ContextCaseMBean)((ContextCaseMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setContextCases(var7);
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
         this.inferSubTree(this.bean.getContextCases(), var1, var2);
      }
   }
}
