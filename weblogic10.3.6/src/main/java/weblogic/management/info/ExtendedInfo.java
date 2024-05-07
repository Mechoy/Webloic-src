package weblogic.management.info;

import javax.management.Attribute;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanRegistrationException;
import weblogic.utils.StringUtils;

public class ExtendedInfo extends MBeanInfo {
   static final long serialVersionUID = 1L;
   private static final String SILVERSWORD_RELEASE = "6.1.0.0";
   private boolean cachingDisabled = false;
   private boolean isPersistent = true;
   private boolean isAbstract = false;
   private String legalDelete = null;
   private String legalDeleteResponse = null;
   private String since = "6.1.0.0";
   private boolean isPersistedEvenIfDefaulted = false;
   private int majorVersion = 6;
   private int minorVersion = 1;
   private int servicePack = 0;
   private int rollingPatch = 0;
   private String customizerClass = null;

   public String getCustomizerClass() {
      return this.customizerClass;
   }

   public ExtendedInfo(String var1, String var2, MBeanAttributeInfo[] var3, MBeanConstructorInfo[] var4, MBeanOperationInfo[] var5, MBeanNotificationInfo[] var6, boolean var7, boolean var8, boolean var9, String var10, String var11, String var12, boolean var13, String var14) {
      super(var1, var2, var3, var4, var5, var6);
      this.cachingDisabled = var7;
      this.isPersistent = var8;
      this.isAbstract = var9;
      this.legalDelete = var10;
      this.legalDeleteResponse = var11;
      this.isPersistedEvenIfDefaulted = var13;
      this.customizerClass = var14;
      if (var12 != null) {
         this.since = var12;
         this.createVersionInfo();
      }

   }

   public boolean isCachingDisabled() {
      return this.cachingDisabled;
   }

   public boolean isPersistent() {
      return this.isPersistent;
   }

   public boolean isAbstract() {
      return this.isAbstract;
   }

   public String getLegalDeleteCheck() {
      return this.legalDelete;
   }

   public String getLegalDeleteCheckResponse() {
      return this.legalDeleteResponse;
   }

   public String getSince() {
      return this.since;
   }

   public boolean isPersistedEvenIfDefaulted() {
      return this.isPersistedEvenIfDefaulted;
   }

   public int getMajorVersion() {
      return this.majorVersion;
   }

   public int getMinorVersion() {
      return this.minorVersion;
   }

   public int getServicePack() {
      return this.servicePack;
   }

   public int getRollingPatch() {
      return this.rollingPatch;
   }

   public Object clone() {
      return this;
   }

   public Object copy() {
      return new ExtendedInfo(this.getClassName(), this.getDescription(), this.getAttributes(), this.getConstructors(), this.getOperations(), this.getNotifications(), this.cachingDisabled, this.isPersistent, this.isAbstract, this.legalDelete, this.legalDeleteResponse, this.since, this.isPersistedEvenIfDefaulted, this.customizerClass);
   }

   public void validateAttribute(Object var1, Attribute var2) throws InvalidAttributeValueException {
   }

   public void validateAddOperation(Object var1, String var2, Object var3) throws InvalidAttributeValueException {
   }

   public void validateRemoveOperation(Object var1, String var2, Object var3) throws InvalidAttributeValueException {
   }

   public void validateAttributes(Object var1, Attribute[] var2) throws InvalidAttributeValueException {
   }

   public void validateDeletion(Object var1) throws MBeanRegistrationException {
   }

   private void createVersionInfo() {
      try {
         String[] var1 = StringUtils.splitCompletely(this.since, " .");
         if (var1.length > 0) {
            this.majorVersion = Integer.parseInt(var1[0]);
            if (var1.length > 1) {
               this.minorVersion = Integer.parseInt(var1[1]);
               if (var1.length > 2) {
                  this.servicePack = Integer.parseInt(var1[2]);
                  if (var1.length > 3) {
                     this.rollingPatch = Integer.parseInt(var1[3]);
                  }
               }
            } else {
               this.minorVersion = 0;
            }
         }
      } catch (NumberFormatException var2) {
      }

   }
}
