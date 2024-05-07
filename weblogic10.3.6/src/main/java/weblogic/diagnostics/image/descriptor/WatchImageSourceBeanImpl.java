package weblogic.diagnostics.image.descriptor;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
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
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class WatchImageSourceBeanImpl extends AbstractDescriptorBean implements WatchImageSourceBean, Serializable {
   private WatchAlarmStateBean[] _WatchAlarmStates;
   private static SchemaHelper2 _schemaHelper;

   public WatchImageSourceBeanImpl() {
      this._initializeRootBean(this.getDescriptor());
      this._initializeProperty(-1);
   }

   public WatchImageSourceBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeRootBean(this.getDescriptor());
      this._initializeProperty(-1);
   }

   public void addWatchAlarmState(WatchAlarmStateBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 0)) {
         WatchAlarmStateBean[] var2;
         if (this._isSet(0)) {
            var2 = (WatchAlarmStateBean[])((WatchAlarmStateBean[])this._getHelper()._extendArray(this.getWatchAlarmStates(), WatchAlarmStateBean.class, var1));
         } else {
            var2 = new WatchAlarmStateBean[]{var1};
         }

         try {
            this.setWatchAlarmStates(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WatchAlarmStateBean[] getWatchAlarmStates() {
      return this._WatchAlarmStates;
   }

   public boolean isWatchAlarmStatesSet() {
      return this._isSet(0);
   }

   public void removeWatchAlarmState(WatchAlarmStateBean var1) {
      WatchAlarmStateBean[] var2 = this.getWatchAlarmStates();
      WatchAlarmStateBean[] var3 = (WatchAlarmStateBean[])((WatchAlarmStateBean[])this._getHelper()._removeElement(var2, WatchAlarmStateBean.class, var1));
      if (var3.length != var2.length) {
         this._preDestroy((AbstractDescriptorBean)var1);

         try {
            this._getReferenceManager().unregisterBean((AbstractDescriptorBean)var1);
            this.setWatchAlarmStates(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setWatchAlarmStates(WatchAlarmStateBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WatchAlarmStateBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 0)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      WatchAlarmStateBean[] var5 = this._WatchAlarmStates;
      this._WatchAlarmStates = (WatchAlarmStateBean[])var4;
      this._postSet(0, var5, var4);
   }

   public WatchAlarmStateBean createWatchAlarmState() {
      WatchAlarmStateBeanImpl var1 = new WatchAlarmStateBeanImpl(this, -1);

      try {
         this.addWatchAlarmState(var1);
         return var1;
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
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
         var1 = 0;
      }

      try {
         switch (var1) {
            case 0:
               this._WatchAlarmStates = new WatchAlarmStateBean[0];
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
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics-image/1.0/weblogic-diagnostics-image.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics-image";
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
            case 17:
               if (var1.equals("watch-alarm-state")) {
                  return 0;
               }
            default:
               return super.getPropertyIndex(var1);
         }
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 0:
               return new WatchAlarmStateBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getRootElementName() {
         return "watch-image-source";
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 0:
               return "watch-alarm-state";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 0:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 0:
               return true;
            default:
               return super.isBean(var1);
         }
      }
   }

   protected static class Helper extends AbstractDescriptorBeanHelper {
      private WatchImageSourceBeanImpl bean;

      protected Helper(WatchImageSourceBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "WatchAlarmStates";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         return var1.equals("WatchAlarmStates") ? 0 : super.getPropertyIndex(var1);
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getWatchAlarmStates()));
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

            for(int var7 = 0; var7 < this.bean.getWatchAlarmStates().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWatchAlarmStates()[var7]);
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
            WatchImageSourceBeanImpl var2 = (WatchImageSourceBeanImpl)var1;
            this.computeChildDiff("WatchAlarmStates", this.bean.getWatchAlarmStates(), var2.getWatchAlarmStates(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WatchImageSourceBeanImpl var3 = (WatchImageSourceBeanImpl)var1.getSourceBean();
            WatchImageSourceBeanImpl var4 = (WatchImageSourceBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("WatchAlarmStates")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addWatchAlarmState((WatchAlarmStateBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeWatchAlarmState((WatchAlarmStateBean)var2.getRemovedObject());
                  }

                  if (var3.getWatchAlarmStates() == null || var3.getWatchAlarmStates().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 0);
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
            WatchImageSourceBeanImpl var5 = (WatchImageSourceBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("WatchAlarmStates")) && this.bean.isWatchAlarmStatesSet() && !var5._isSet(0)) {
               WatchAlarmStateBean[] var6 = this.bean.getWatchAlarmStates();
               WatchAlarmStateBean[] var7 = new WatchAlarmStateBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (WatchAlarmStateBean)((WatchAlarmStateBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setWatchAlarmStates(var7);
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
         this.inferSubTree(this.bean.getWatchAlarmStates(), var1, var2);
      }
   }
}
