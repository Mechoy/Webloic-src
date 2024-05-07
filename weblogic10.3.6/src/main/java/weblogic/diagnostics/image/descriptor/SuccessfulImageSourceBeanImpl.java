package weblogic.diagnostics.image.descriptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class SuccessfulImageSourceBeanImpl extends AbstractDescriptorBean implements SuccessfulImageSourceBean, Serializable {
   private long _ImageCaptureElapsedTime;
   private String _ImageSource;
   private static SchemaHelper2 _schemaHelper;

   public SuccessfulImageSourceBeanImpl() {
      this._initializeProperty(-1);
   }

   public SuccessfulImageSourceBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getImageSource() {
      return this._ImageSource;
   }

   public boolean isImageSourceSet() {
      return this._isSet(0);
   }

   public void setImageSource(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ImageSource;
      this._ImageSource = var1;
      this._postSet(0, var2, var1);
   }

   public void setImageCaptureElapsedTime(long var1) {
      long var3 = this._ImageCaptureElapsedTime;
      this._ImageCaptureElapsedTime = var1;
      this._postSet(1, var3, var1);
   }

   public long getImageCaptureElapsedTime() {
      return this._ImageCaptureElapsedTime;
   }

   public boolean isImageCaptureElapsedTimeSet() {
      return this._isSet(1);
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
         var1 = 1;
      }

      try {
         switch (var1) {
            case 1:
               this._ImageCaptureElapsedTime = 0L;
               if (var2) {
                  break;
               }
            case 0:
               this._ImageSource = null;
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
            case 12:
               if (var1.equals("image-source")) {
                  return 0;
               }
               break;
            case 26:
               if (var1.equals("image-capture-elapsed-time")) {
                  return 1;
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
            case 0:
               return "image-source";
            case 1:
               return "image-capture-elapsed-time";
            default:
               return super.getElementName(var1);
         }
      }
   }

   protected static class Helper extends AbstractDescriptorBeanHelper {
      private SuccessfulImageSourceBeanImpl bean;

      protected Helper(SuccessfulImageSourceBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "ImageSource";
            case 1:
               return "ImageCaptureElapsedTime";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ImageCaptureElapsedTime")) {
            return 1;
         } else {
            return var1.equals("ImageSource") ? 0 : super.getPropertyIndex(var1);
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
            if (this.bean.isImageCaptureElapsedTimeSet()) {
               var2.append("ImageCaptureElapsedTime");
               var2.append(String.valueOf(this.bean.getImageCaptureElapsedTime()));
            }

            if (this.bean.isImageSourceSet()) {
               var2.append("ImageSource");
               var2.append(String.valueOf(this.bean.getImageSource()));
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
            SuccessfulImageSourceBeanImpl var2 = (SuccessfulImageSourceBeanImpl)var1;
            this.computeDiff("ImageCaptureElapsedTime", this.bean.getImageCaptureElapsedTime(), var2.getImageCaptureElapsedTime(), false);
            this.computeDiff("ImageSource", this.bean.getImageSource(), var2.getImageSource(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SuccessfulImageSourceBeanImpl var3 = (SuccessfulImageSourceBeanImpl)var1.getSourceBean();
            SuccessfulImageSourceBeanImpl var4 = (SuccessfulImageSourceBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ImageCaptureElapsedTime")) {
                  var3.setImageCaptureElapsedTime(var4.getImageCaptureElapsedTime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 1);
               } else if (var5.equals("ImageSource")) {
                  var3.setImageSource(var4.getImageSource());
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
            SuccessfulImageSourceBeanImpl var5 = (SuccessfulImageSourceBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ImageCaptureElapsedTime")) && this.bean.isImageCaptureElapsedTimeSet()) {
               var5.setImageCaptureElapsedTime(this.bean.getImageCaptureElapsedTime());
            }

            if ((var3 == null || !var3.contains("ImageSource")) && this.bean.isImageSourceSet()) {
               var5.setImageSource(this.bean.getImageSource());
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
