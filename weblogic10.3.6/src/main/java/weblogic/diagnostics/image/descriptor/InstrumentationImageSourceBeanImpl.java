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

public class InstrumentationImageSourceBeanImpl extends AbstractDescriptorBean implements InstrumentationImageSourceBean, Serializable {
   private InstrumentationEventBean[] _InstrumentationEvents;
   private static SchemaHelper2 _schemaHelper;

   public InstrumentationImageSourceBeanImpl() {
      this._initializeRootBean(this.getDescriptor());
      this._initializeProperty(-1);
   }

   public InstrumentationImageSourceBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeRootBean(this.getDescriptor());
      this._initializeProperty(-1);
   }

   public void addInstrumentationEvent(InstrumentationEventBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 0)) {
         InstrumentationEventBean[] var2;
         if (this._isSet(0)) {
            var2 = (InstrumentationEventBean[])((InstrumentationEventBean[])this._getHelper()._extendArray(this.getInstrumentationEvents(), InstrumentationEventBean.class, var1));
         } else {
            var2 = new InstrumentationEventBean[]{var1};
         }

         try {
            this.setInstrumentationEvents(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public InstrumentationEventBean[] getInstrumentationEvents() {
      return this._InstrumentationEvents;
   }

   public boolean isInstrumentationEventsSet() {
      return this._isSet(0);
   }

   public void removeInstrumentationEvent(InstrumentationEventBean var1) {
      InstrumentationEventBean[] var2 = this.getInstrumentationEvents();
      InstrumentationEventBean[] var3 = (InstrumentationEventBean[])((InstrumentationEventBean[])this._getHelper()._removeElement(var2, InstrumentationEventBean.class, var1));
      if (var3.length != var2.length) {
         this._preDestroy((AbstractDescriptorBean)var1);

         try {
            this._getReferenceManager().unregisterBean((AbstractDescriptorBean)var1);
            this.setInstrumentationEvents(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setInstrumentationEvents(InstrumentationEventBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new InstrumentationEventBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 0)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      InstrumentationEventBean[] var5 = this._InstrumentationEvents;
      this._InstrumentationEvents = (InstrumentationEventBean[])var4;
      this._postSet(0, var5, var4);
   }

   public InstrumentationEventBean createInstrumentationEvent() {
      InstrumentationEventBeanImpl var1 = new InstrumentationEventBeanImpl(this, -1);

      try {
         this.addInstrumentationEvent(var1);
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
               this._InstrumentationEvents = new InstrumentationEventBean[0];
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
            case 21:
               if (var1.equals("instrumentation-event")) {
                  return 0;
               }
            default:
               return super.getPropertyIndex(var1);
         }
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 0:
               return new InstrumentationEventBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getRootElementName() {
         return "instrumentation-image-source";
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 0:
               return "instrumentation-event";
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
      private InstrumentationImageSourceBeanImpl bean;

      protected Helper(InstrumentationImageSourceBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "InstrumentationEvents";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         return var1.equals("InstrumentationEvents") ? 0 : super.getPropertyIndex(var1);
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getInstrumentationEvents()));
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

            for(int var7 = 0; var7 < this.bean.getInstrumentationEvents().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getInstrumentationEvents()[var7]);
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
            InstrumentationImageSourceBeanImpl var2 = (InstrumentationImageSourceBeanImpl)var1;
            this.computeChildDiff("InstrumentationEvents", this.bean.getInstrumentationEvents(), var2.getInstrumentationEvents(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            InstrumentationImageSourceBeanImpl var3 = (InstrumentationImageSourceBeanImpl)var1.getSourceBean();
            InstrumentationImageSourceBeanImpl var4 = (InstrumentationImageSourceBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("InstrumentationEvents")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addInstrumentationEvent((InstrumentationEventBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeInstrumentationEvent((InstrumentationEventBean)var2.getRemovedObject());
                  }

                  if (var3.getInstrumentationEvents() == null || var3.getInstrumentationEvents().length == 0) {
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
            InstrumentationImageSourceBeanImpl var5 = (InstrumentationImageSourceBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("InstrumentationEvents")) && this.bean.isInstrumentationEventsSet() && !var5._isSet(0)) {
               InstrumentationEventBean[] var6 = this.bean.getInstrumentationEvents();
               InstrumentationEventBean[] var7 = new InstrumentationEventBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (InstrumentationEventBean)((InstrumentationEventBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setInstrumentationEvents(var7);
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
         this.inferSubTree(this.bean.getInstrumentationEvents(), var1, var2);
      }
   }
}
