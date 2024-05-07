package weblogic.diagnostics.descriptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.LegalChecks;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.PlatformConstants;
import weblogic.utils.collections.CombinedIterator;

public class WLDFImageNotificationBeanImpl extends WLDFNotificationBeanImpl implements WLDFImageNotificationBean, Serializable {
   private String _ImageDirectory;
   private int _ImageLockout;
   private static SchemaHelper2 _schemaHelper;

   public WLDFImageNotificationBeanImpl() {
      this._initializeProperty(-1);
   }

   public WLDFImageNotificationBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getImageDirectory() {
      if (!this._isSet(2)) {
         try {
            return "logs" + PlatformConstants.FILE_SEP + "diagnostic_images";
         } catch (NullPointerException var2) {
         }
      }

      return this._ImageDirectory;
   }

   public boolean isImageDirectorySet() {
      return this._isSet(2);
   }

   public void setImageDirectory(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ImageDirectory;
      this._ImageDirectory = var1;
      this._postSet(2, var2, var1);
   }

   public int getImageLockout() {
      return this._ImageLockout;
   }

   public boolean isImageLockoutSet() {
      return this._isSet(3);
   }

   public void setImageLockout(int var1) {
      LegalChecks.checkMin("ImageLockout", var1, 0);
      int var2 = this._ImageLockout;
      this._ImageLockout = var1;
      this._postSet(3, var2, var1);
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
         var1 = 2;
      }

      try {
         switch (var1) {
            case 2:
               this._ImageDirectory = null;
               if (var2) {
                  break;
               }
            case 3:
               this._ImageLockout = 0;
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
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics/1.0/weblogic-diagnostics.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public static class SchemaHelper2 extends WLDFNotificationBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 13:
               if (var1.equals("image-lockout")) {
                  return 3;
               }
               break;
            case 15:
               if (var1.equals("image-directory")) {
                  return 2;
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
            case 2:
               return "image-directory";
            case 3:
               return "image-lockout";
            default:
               return super.getElementName(var1);
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends WLDFNotificationBeanImpl.Helper {
      private WLDFImageNotificationBeanImpl bean;

      protected Helper(WLDFImageNotificationBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "ImageDirectory";
            case 3:
               return "ImageLockout";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ImageDirectory")) {
            return 2;
         } else {
            return var1.equals("ImageLockout") ? 3 : super.getPropertyIndex(var1);
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
            if (this.bean.isImageDirectorySet()) {
               var2.append("ImageDirectory");
               var2.append(String.valueOf(this.bean.getImageDirectory()));
            }

            if (this.bean.isImageLockoutSet()) {
               var2.append("ImageLockout");
               var2.append(String.valueOf(this.bean.getImageLockout()));
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
            WLDFImageNotificationBeanImpl var2 = (WLDFImageNotificationBeanImpl)var1;
            this.computeDiff("ImageDirectory", this.bean.getImageDirectory(), var2.getImageDirectory(), true);
            this.computeDiff("ImageLockout", this.bean.getImageLockout(), var2.getImageLockout(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WLDFImageNotificationBeanImpl var3 = (WLDFImageNotificationBeanImpl)var1.getSourceBean();
            WLDFImageNotificationBeanImpl var4 = (WLDFImageNotificationBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ImageDirectory")) {
                  var3.setImageDirectory(var4.getImageDirectory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("ImageLockout")) {
                  var3.setImageLockout(var4.getImageLockout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
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
            WLDFImageNotificationBeanImpl var5 = (WLDFImageNotificationBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ImageDirectory")) && this.bean.isImageDirectorySet()) {
               var5.setImageDirectory(this.bean.getImageDirectory());
            }

            if ((var3 == null || !var3.contains("ImageLockout")) && this.bean.isImageLockoutSet()) {
               var5.setImageLockout(this.bean.getImageLockout());
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
