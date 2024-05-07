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

public class ImageSummaryBeanImpl extends AbstractDescriptorBean implements ImageSummaryBean, Serializable {
   private FailedImageSourceBean[] _FailedImageSource;
   private boolean _ImageCaptureCancelled;
   private String _ImageCreationDate;
   private long _ImageCreationElapsedTime;
   private String _ImageFileName;
   private String _MuxerClass;
   private String _RequestStackTrace;
   private String _RequesterThreadName;
   private String _RequesterUserId;
   private String _ServerName;
   private String _ServerReleaseInfo;
   private SuccessfulImageSourceBean[] _SuccessfulImageSources;
   private SystemPropertyBean[] _SystemProperties;
   private static SchemaHelper2 _schemaHelper;

   public ImageSummaryBeanImpl() {
      this._initializeRootBean(this.getDescriptor());
      this._initializeProperty(-1);
   }

   public ImageSummaryBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeRootBean(this.getDescriptor());
      this._initializeProperty(-1);
   }

   public String getImageCreationDate() {
      return this._ImageCreationDate;
   }

   public boolean isImageCreationDateSet() {
      return this._isSet(0);
   }

   public void setImageCreationDate(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ImageCreationDate;
      this._ImageCreationDate = var1;
      this._postSet(0, var2, var1);
   }

   public void setImageFileName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ImageFileName;
      this._ImageFileName = var1;
      this._postSet(1, var2, var1);
   }

   public String getImageFileName() {
      return this._ImageFileName;
   }

   public boolean isImageFileNameSet() {
      return this._isSet(1);
   }

   public void setImageCreationElapsedTime(long var1) {
      long var3 = this._ImageCreationElapsedTime;
      this._ImageCreationElapsedTime = var1;
      this._postSet(2, var3, var1);
   }

   public long getImageCreationElapsedTime() {
      return this._ImageCreationElapsedTime;
   }

   public boolean isImageCreationElapsedTimeSet() {
      return this._isSet(2);
   }

   public void setImageCaptureCancelled(boolean var1) {
      boolean var2 = this._ImageCaptureCancelled;
      this._ImageCaptureCancelled = var1;
      this._postSet(3, var2, var1);
   }

   public boolean isImageCaptureCancelled() {
      return this._ImageCaptureCancelled;
   }

   public boolean isImageCaptureCancelledSet() {
      return this._isSet(3);
   }

   public void setServerReleaseInfo(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ServerReleaseInfo;
      this._ServerReleaseInfo = var1;
      this._postSet(4, var2, var1);
   }

   public String getServerReleaseInfo() {
      return this._ServerReleaseInfo;
   }

   public boolean isServerReleaseInfoSet() {
      return this._isSet(4);
   }

   public void setServerName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ServerName;
      this._ServerName = var1;
      this._postSet(5, var2, var1);
   }

   public String getServerName() {
      return this._ServerName;
   }

   public boolean isServerNameSet() {
      return this._isSet(5);
   }

   public void setMuxerClass(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MuxerClass;
      this._MuxerClass = var1;
      this._postSet(6, var2, var1);
   }

   public String getMuxerClass() {
      return this._MuxerClass;
   }

   public boolean isMuxerClassSet() {
      return this._isSet(6);
   }

   public void addSystemProperty(SystemPropertyBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         SystemPropertyBean[] var2;
         if (this._isSet(7)) {
            var2 = (SystemPropertyBean[])((SystemPropertyBean[])this._getHelper()._extendArray(this.getSystemProperties(), SystemPropertyBean.class, var1));
         } else {
            var2 = new SystemPropertyBean[]{var1};
         }

         try {
            this.setSystemProperties(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SystemPropertyBean[] getSystemProperties() {
      return this._SystemProperties;
   }

   public boolean isSystemPropertiesSet() {
      return this._isSet(7);
   }

   public void removeSystemProperty(SystemPropertyBean var1) {
      SystemPropertyBean[] var2 = this.getSystemProperties();
      SystemPropertyBean[] var3 = (SystemPropertyBean[])((SystemPropertyBean[])this._getHelper()._removeElement(var2, SystemPropertyBean.class, var1));
      if (var3.length != var2.length) {
         this._preDestroy((AbstractDescriptorBean)var1);

         try {
            this._getReferenceManager().unregisterBean((AbstractDescriptorBean)var1);
            this.setSystemProperties(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setSystemProperties(SystemPropertyBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SystemPropertyBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 7)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SystemPropertyBean[] var5 = this._SystemProperties;
      this._SystemProperties = (SystemPropertyBean[])var4;
      this._postSet(7, var5, var4);
   }

   public SystemPropertyBean createSystemProperty() {
      SystemPropertyBeanImpl var1 = new SystemPropertyBeanImpl(this, -1);

      try {
         this.addSystemProperty(var1);
         return var1;
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public SuccessfulImageSourceBean createSuccessfulImageSource() {
      SuccessfulImageSourceBeanImpl var1 = new SuccessfulImageSourceBeanImpl(this, -1);

      try {
         this.addSuccessfulImageSource(var1);
         return var1;
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void addSuccessfulImageSource(SuccessfulImageSourceBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 8)) {
         SuccessfulImageSourceBean[] var2;
         if (this._isSet(8)) {
            var2 = (SuccessfulImageSourceBean[])((SuccessfulImageSourceBean[])this._getHelper()._extendArray(this.getSuccessfulImageSources(), SuccessfulImageSourceBean.class, var1));
         } else {
            var2 = new SuccessfulImageSourceBean[]{var1};
         }

         try {
            this.setSuccessfulImageSources(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SuccessfulImageSourceBean[] getSuccessfulImageSources() {
      return this._SuccessfulImageSources;
   }

   public boolean isSuccessfulImageSourcesSet() {
      return this._isSet(8);
   }

   public void removeSuccessfulImageSource(SuccessfulImageSourceBean var1) {
      SuccessfulImageSourceBean[] var2 = this.getSuccessfulImageSources();
      SuccessfulImageSourceBean[] var3 = (SuccessfulImageSourceBean[])((SuccessfulImageSourceBean[])this._getHelper()._removeElement(var2, SuccessfulImageSourceBean.class, var1));
      if (var3.length != var2.length) {
         this._preDestroy((AbstractDescriptorBean)var1);

         try {
            this._getReferenceManager().unregisterBean((AbstractDescriptorBean)var1);
            this.setSuccessfulImageSources(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setSuccessfulImageSources(SuccessfulImageSourceBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SuccessfulImageSourceBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 8)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SuccessfulImageSourceBean[] var5 = this._SuccessfulImageSources;
      this._SuccessfulImageSources = (SuccessfulImageSourceBean[])var4;
      this._postSet(8, var5, var4);
   }

   public FailedImageSourceBean createFailedImageSource() {
      FailedImageSourceBeanImpl var1 = new FailedImageSourceBeanImpl(this, -1);

      try {
         this.addFailedImageSource(var1);
         return var1;
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void addFailedImageSource(FailedImageSourceBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 9)) {
         FailedImageSourceBean[] var2;
         if (this._isSet(9)) {
            var2 = (FailedImageSourceBean[])((FailedImageSourceBean[])this._getHelper()._extendArray(this.getFailedImageSource(), FailedImageSourceBean.class, var1));
         } else {
            var2 = new FailedImageSourceBean[]{var1};
         }

         try {
            this.setFailedImageSource(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public FailedImageSourceBean[] getFailedImageSource() {
      return this._FailedImageSource;
   }

   public boolean isFailedImageSourceSet() {
      return this._isSet(9);
   }

   public void removeFailedImageSource(FailedImageSourceBean var1) {
      FailedImageSourceBean[] var2 = this.getFailedImageSource();
      FailedImageSourceBean[] var3 = (FailedImageSourceBean[])((FailedImageSourceBean[])this._getHelper()._removeElement(var2, FailedImageSourceBean.class, var1));
      if (var3.length != var2.length) {
         this._preDestroy((AbstractDescriptorBean)var1);

         try {
            this._getReferenceManager().unregisterBean((AbstractDescriptorBean)var1);
            this.setFailedImageSource(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setFailedImageSource(FailedImageSourceBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new FailedImageSourceBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 9)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      FailedImageSourceBean[] var5 = this._FailedImageSource;
      this._FailedImageSource = (FailedImageSourceBean[])var4;
      this._postSet(9, var5, var4);
   }

   public void setRequesterThreadName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RequesterThreadName;
      this._RequesterThreadName = var1;
      this._postSet(10, var2, var1);
   }

   public String getRequesterThreadName() {
      return this._RequesterThreadName;
   }

   public boolean isRequesterThreadNameSet() {
      return this._isSet(10);
   }

   public void setRequesterUserId(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RequesterUserId;
      this._RequesterUserId = var1;
      this._postSet(11, var2, var1);
   }

   public String getRequesterUserId() {
      return this._RequesterUserId;
   }

   public boolean isRequesterUserIdSet() {
      return this._isSet(11);
   }

   public void setRequestStackTrace(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RequestStackTrace;
      this._RequestStackTrace = var1;
      this._postSet(12, var2, var1);
   }

   public String getRequestStackTrace() {
      return this._RequestStackTrace;
   }

   public boolean isRequestStackTraceSet() {
      return this._isSet(12);
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
               this._FailedImageSource = new FailedImageSourceBean[0];
               if (var2) {
                  break;
               }
            case 0:
               this._ImageCreationDate = null;
               if (var2) {
                  break;
               }
            case 2:
               this._ImageCreationElapsedTime = 0L;
               if (var2) {
                  break;
               }
            case 1:
               this._ImageFileName = null;
               if (var2) {
                  break;
               }
            case 6:
               this._MuxerClass = null;
               if (var2) {
                  break;
               }
            case 12:
               this._RequestStackTrace = null;
               if (var2) {
                  break;
               }
            case 10:
               this._RequesterThreadName = null;
               if (var2) {
                  break;
               }
            case 11:
               this._RequesterUserId = null;
               if (var2) {
                  break;
               }
            case 5:
               this._ServerName = null;
               if (var2) {
                  break;
               }
            case 4:
               this._ServerReleaseInfo = null;
               if (var2) {
                  break;
               }
            case 8:
               this._SuccessfulImageSources = new SuccessfulImageSourceBean[0];
               if (var2) {
                  break;
               }
            case 7:
               this._SystemProperties = new SystemPropertyBean[0];
               if (var2) {
                  break;
               }
            case 3:
               this._ImageCaptureCancelled = false;
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
            case 11:
               if (var1.equals("muxer-class")) {
                  return 6;
               }

               if (var1.equals("server-name")) {
                  return 5;
               }
            case 12:
            case 13:
            case 14:
            case 16:
            case 18:
            case 20:
            case 22:
            case 24:
            case 25:
            case 26:
            default:
               break;
            case 15:
               if (var1.equals("image-file-name")) {
                  return 1;
               }

               if (var1.equals("system-property")) {
                  return 7;
               }
               break;
            case 17:
               if (var1.equals("requester-user-id")) {
                  return 11;
               }
               break;
            case 19:
               if (var1.equals("failed-image-source")) {
                  return 9;
               }

               if (var1.equals("image-creation-date")) {
                  return 0;
               }

               if (var1.equals("request-stack-trace")) {
                  return 12;
               }

               if (var1.equals("server-release-info")) {
                  return 4;
               }
               break;
            case 21:
               if (var1.equals("requester-thread-name")) {
                  return 10;
               }
               break;
            case 23:
               if (var1.equals("successful-image-source")) {
                  return 8;
               }

               if (var1.equals("image-capture-cancelled")) {
                  return 3;
               }
               break;
            case 27:
               if (var1.equals("image-creation-elapsed-time")) {
                  return 2;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 7:
               return new SystemPropertyBeanImpl.SchemaHelper2();
            case 8:
               return new SuccessfulImageSourceBeanImpl.SchemaHelper2();
            case 9:
               return new FailedImageSourceBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getRootElementName() {
         return "image-summary";
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 0:
               return "image-creation-date";
            case 1:
               return "image-file-name";
            case 2:
               return "image-creation-elapsed-time";
            case 3:
               return "image-capture-cancelled";
            case 4:
               return "server-release-info";
            case 5:
               return "server-name";
            case 6:
               return "muxer-class";
            case 7:
               return "system-property";
            case 8:
               return "successful-image-source";
            case 9:
               return "failed-image-source";
            case 10:
               return "requester-thread-name";
            case 11:
               return "requester-user-id";
            case 12:
               return "request-stack-trace";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
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
            case 7:
               return true;
            case 8:
               return true;
            case 9:
               return true;
            default:
               return super.isBean(var1);
         }
      }
   }

   protected static class Helper extends AbstractDescriptorBeanHelper {
      private ImageSummaryBeanImpl bean;

      protected Helper(ImageSummaryBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "ImageCreationDate";
            case 1:
               return "ImageFileName";
            case 2:
               return "ImageCreationElapsedTime";
            case 3:
               return "ImageCaptureCancelled";
            case 4:
               return "ServerReleaseInfo";
            case 5:
               return "ServerName";
            case 6:
               return "MuxerClass";
            case 7:
               return "SystemProperties";
            case 8:
               return "SuccessfulImageSources";
            case 9:
               return "FailedImageSource";
            case 10:
               return "RequesterThreadName";
            case 11:
               return "RequesterUserId";
            case 12:
               return "RequestStackTrace";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("FailedImageSource")) {
            return 9;
         } else if (var1.equals("ImageCreationDate")) {
            return 0;
         } else if (var1.equals("ImageCreationElapsedTime")) {
            return 2;
         } else if (var1.equals("ImageFileName")) {
            return 1;
         } else if (var1.equals("MuxerClass")) {
            return 6;
         } else if (var1.equals("RequestStackTrace")) {
            return 12;
         } else if (var1.equals("RequesterThreadName")) {
            return 10;
         } else if (var1.equals("RequesterUserId")) {
            return 11;
         } else if (var1.equals("ServerName")) {
            return 5;
         } else if (var1.equals("ServerReleaseInfo")) {
            return 4;
         } else if (var1.equals("SuccessfulImageSources")) {
            return 8;
         } else if (var1.equals("SystemProperties")) {
            return 7;
         } else {
            return var1.equals("ImageCaptureCancelled") ? 3 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getFailedImageSource()));
         var1.add(new ArrayIterator(this.bean.getSuccessfulImageSources()));
         var1.add(new ArrayIterator(this.bean.getSystemProperties()));
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

            int var7;
            for(var7 = 0; var7 < this.bean.getFailedImageSource().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getFailedImageSource()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isImageCreationDateSet()) {
               var2.append("ImageCreationDate");
               var2.append(String.valueOf(this.bean.getImageCreationDate()));
            }

            if (this.bean.isImageCreationElapsedTimeSet()) {
               var2.append("ImageCreationElapsedTime");
               var2.append(String.valueOf(this.bean.getImageCreationElapsedTime()));
            }

            if (this.bean.isImageFileNameSet()) {
               var2.append("ImageFileName");
               var2.append(String.valueOf(this.bean.getImageFileName()));
            }

            if (this.bean.isMuxerClassSet()) {
               var2.append("MuxerClass");
               var2.append(String.valueOf(this.bean.getMuxerClass()));
            }

            if (this.bean.isRequestStackTraceSet()) {
               var2.append("RequestStackTrace");
               var2.append(String.valueOf(this.bean.getRequestStackTrace()));
            }

            if (this.bean.isRequesterThreadNameSet()) {
               var2.append("RequesterThreadName");
               var2.append(String.valueOf(this.bean.getRequesterThreadName()));
            }

            if (this.bean.isRequesterUserIdSet()) {
               var2.append("RequesterUserId");
               var2.append(String.valueOf(this.bean.getRequesterUserId()));
            }

            if (this.bean.isServerNameSet()) {
               var2.append("ServerName");
               var2.append(String.valueOf(this.bean.getServerName()));
            }

            if (this.bean.isServerReleaseInfoSet()) {
               var2.append("ServerReleaseInfo");
               var2.append(String.valueOf(this.bean.getServerReleaseInfo()));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSuccessfulImageSources().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSuccessfulImageSources()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSystemProperties().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSystemProperties()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isImageCaptureCancelledSet()) {
               var2.append("ImageCaptureCancelled");
               var2.append(String.valueOf(this.bean.isImageCaptureCancelled()));
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
            ImageSummaryBeanImpl var2 = (ImageSummaryBeanImpl)var1;
            this.computeChildDiff("FailedImageSource", this.bean.getFailedImageSource(), var2.getFailedImageSource(), false);
            this.computeDiff("ImageCreationDate", this.bean.getImageCreationDate(), var2.getImageCreationDate(), false);
            this.computeDiff("ImageCreationElapsedTime", this.bean.getImageCreationElapsedTime(), var2.getImageCreationElapsedTime(), false);
            this.computeDiff("ImageFileName", this.bean.getImageFileName(), var2.getImageFileName(), false);
            this.computeDiff("MuxerClass", this.bean.getMuxerClass(), var2.getMuxerClass(), false);
            this.computeDiff("RequestStackTrace", this.bean.getRequestStackTrace(), var2.getRequestStackTrace(), false);
            this.computeDiff("RequesterThreadName", this.bean.getRequesterThreadName(), var2.getRequesterThreadName(), false);
            this.computeDiff("RequesterUserId", this.bean.getRequesterUserId(), var2.getRequesterUserId(), false);
            this.computeDiff("ServerName", this.bean.getServerName(), var2.getServerName(), false);
            this.computeDiff("ServerReleaseInfo", this.bean.getServerReleaseInfo(), var2.getServerReleaseInfo(), false);
            this.computeChildDiff("SuccessfulImageSources", this.bean.getSuccessfulImageSources(), var2.getSuccessfulImageSources(), false);
            this.computeChildDiff("SystemProperties", this.bean.getSystemProperties(), var2.getSystemProperties(), false);
            this.computeDiff("ImageCaptureCancelled", this.bean.isImageCaptureCancelled(), var2.isImageCaptureCancelled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ImageSummaryBeanImpl var3 = (ImageSummaryBeanImpl)var1.getSourceBean();
            ImageSummaryBeanImpl var4 = (ImageSummaryBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("FailedImageSource")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addFailedImageSource((FailedImageSourceBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeFailedImageSource((FailedImageSourceBean)var2.getRemovedObject());
                  }

                  if (var3.getFailedImageSource() == null || var3.getFailedImageSource().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  }
               } else if (var5.equals("ImageCreationDate")) {
                  var3.setImageCreationDate(var4.getImageCreationDate());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 0);
               } else if (var5.equals("ImageCreationElapsedTime")) {
                  var3.setImageCreationElapsedTime(var4.getImageCreationElapsedTime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("ImageFileName")) {
                  var3.setImageFileName(var4.getImageFileName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 1);
               } else if (var5.equals("MuxerClass")) {
                  var3.setMuxerClass(var4.getMuxerClass());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 6);
               } else if (var5.equals("RequestStackTrace")) {
                  var3.setRequestStackTrace(var4.getRequestStackTrace());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("RequesterThreadName")) {
                  var3.setRequesterThreadName(var4.getRequesterThreadName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("RequesterUserId")) {
                  var3.setRequesterUserId(var4.getRequesterUserId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("ServerName")) {
                  var3.setServerName(var4.getServerName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 5);
               } else if (var5.equals("ServerReleaseInfo")) {
                  var3.setServerReleaseInfo(var4.getServerReleaseInfo());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 4);
               } else if (var5.equals("SuccessfulImageSources")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addSuccessfulImageSource((SuccessfulImageSourceBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeSuccessfulImageSource((SuccessfulImageSourceBean)var2.getRemovedObject());
                  }

                  if (var3.getSuccessfulImageSources() == null || var3.getSuccessfulImageSources().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  }
               } else if (var5.equals("SystemProperties")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addSystemProperty((SystemPropertyBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeSystemProperty((SystemPropertyBean)var2.getRemovedObject());
                  }

                  if (var3.getSystemProperties() == null || var3.getSystemProperties().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  }
               } else if (var5.equals("ImageCaptureCancelled")) {
                  var3.setImageCaptureCancelled(var4.isImageCaptureCancelled());
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
            ImageSummaryBeanImpl var5 = (ImageSummaryBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            int var8;
            if ((var3 == null || !var3.contains("FailedImageSource")) && this.bean.isFailedImageSourceSet() && !var5._isSet(9)) {
               FailedImageSourceBean[] var6 = this.bean.getFailedImageSource();
               FailedImageSourceBean[] var7 = new FailedImageSourceBean[var6.length];

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (FailedImageSourceBean)((FailedImageSourceBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setFailedImageSource(var7);
            }

            if ((var3 == null || !var3.contains("ImageCreationDate")) && this.bean.isImageCreationDateSet()) {
               var5.setImageCreationDate(this.bean.getImageCreationDate());
            }

            if ((var3 == null || !var3.contains("ImageCreationElapsedTime")) && this.bean.isImageCreationElapsedTimeSet()) {
               var5.setImageCreationElapsedTime(this.bean.getImageCreationElapsedTime());
            }

            if ((var3 == null || !var3.contains("ImageFileName")) && this.bean.isImageFileNameSet()) {
               var5.setImageFileName(this.bean.getImageFileName());
            }

            if ((var3 == null || !var3.contains("MuxerClass")) && this.bean.isMuxerClassSet()) {
               var5.setMuxerClass(this.bean.getMuxerClass());
            }

            if ((var3 == null || !var3.contains("RequestStackTrace")) && this.bean.isRequestStackTraceSet()) {
               var5.setRequestStackTrace(this.bean.getRequestStackTrace());
            }

            if ((var3 == null || !var3.contains("RequesterThreadName")) && this.bean.isRequesterThreadNameSet()) {
               var5.setRequesterThreadName(this.bean.getRequesterThreadName());
            }

            if ((var3 == null || !var3.contains("RequesterUserId")) && this.bean.isRequesterUserIdSet()) {
               var5.setRequesterUserId(this.bean.getRequesterUserId());
            }

            if ((var3 == null || !var3.contains("ServerName")) && this.bean.isServerNameSet()) {
               var5.setServerName(this.bean.getServerName());
            }

            if ((var3 == null || !var3.contains("ServerReleaseInfo")) && this.bean.isServerReleaseInfoSet()) {
               var5.setServerReleaseInfo(this.bean.getServerReleaseInfo());
            }

            if ((var3 == null || !var3.contains("SuccessfulImageSources")) && this.bean.isSuccessfulImageSourcesSet() && !var5._isSet(8)) {
               SuccessfulImageSourceBean[] var11 = this.bean.getSuccessfulImageSources();
               SuccessfulImageSourceBean[] var13 = new SuccessfulImageSourceBean[var11.length];

               for(var8 = 0; var8 < var13.length; ++var8) {
                  var13[var8] = (SuccessfulImageSourceBean)((SuccessfulImageSourceBean)this.createCopy((AbstractDescriptorBean)var11[var8], var2));
               }

               var5.setSuccessfulImageSources(var13);
            }

            if ((var3 == null || !var3.contains("SystemProperties")) && this.bean.isSystemPropertiesSet() && !var5._isSet(7)) {
               SystemPropertyBean[] var12 = this.bean.getSystemProperties();
               SystemPropertyBean[] var14 = new SystemPropertyBean[var12.length];

               for(var8 = 0; var8 < var14.length; ++var8) {
                  var14[var8] = (SystemPropertyBean)((SystemPropertyBean)this.createCopy((AbstractDescriptorBean)var12[var8], var2));
               }

               var5.setSystemProperties(var14);
            }

            if ((var3 == null || !var3.contains("ImageCaptureCancelled")) && this.bean.isImageCaptureCancelledSet()) {
               var5.setImageCaptureCancelled(this.bean.isImageCaptureCancelled());
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
         this.inferSubTree(this.bean.getFailedImageSource(), var1, var2);
         this.inferSubTree(this.bean.getSuccessfulImageSources(), var1, var2);
         this.inferSubTree(this.bean.getSystemProperties(), var1, var2);
      }
   }
}
