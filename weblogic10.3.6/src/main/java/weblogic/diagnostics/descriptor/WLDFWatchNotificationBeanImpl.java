package weblogic.diagnostics.descriptor;

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
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.diagnostics.watch.WatchValidator;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class WLDFWatchNotificationBeanImpl extends WLDFBeanImpl implements WLDFWatchNotificationBean, Serializable {
   private boolean _Enabled;
   private WLDFImageNotificationBean[] _ImageNotifications;
   private WLDFJMSNotificationBean[] _JMSNotifications;
   private WLDFJMXNotificationBean[] _JMXNotifications;
   private String _LogWatchSeverity;
   private WLDFNotificationBean[] _Notifications;
   private WLDFSMTPNotificationBean[] _SMTPNotifications;
   private WLDFSNMPNotificationBean[] _SNMPNotifications;
   private String _Severity;
   private WLDFWatchBean[] _Watches;
   private WLDFWatchNotificationCustomizer _customizer;
   private static SchemaHelper2 _schemaHelper;

   public WLDFWatchNotificationBeanImpl() {
      try {
         this._customizer = new WLDFWatchNotificationCustomizer(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public WLDFWatchNotificationBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new WLDFWatchNotificationCustomizer(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public boolean isEnabled() {
      return this._Enabled;
   }

   public boolean isEnabledSet() {
      return this._isSet(1);
   }

   public void setEnabled(boolean var1) {
      boolean var2 = this._Enabled;
      this._Enabled = var1;
      this._postSet(1, var2, var1);
   }

   public String getSeverity() {
      return this._Severity;
   }

   public boolean isSeveritySet() {
      return this._isSet(2);
   }

   public void setSeverity(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Info", "Warning", "Error", "Notice", "Critical", "Alert", "Emergency"};
      var1 = LegalChecks.checkInEnum("Severity", var1, var2);
      String var3 = this._Severity;
      this._Severity = var1;
      this._postSet(2, var3, var1);
   }

   public String getLogWatchSeverity() {
      return this._LogWatchSeverity;
   }

   public boolean isLogWatchSeveritySet() {
      return this._isSet(3);
   }

   public void setLogWatchSeverity(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Info", "Warning", "Error", "Notice", "Critical", "Alert", "Emergency"};
      var1 = LegalChecks.checkInEnum("LogWatchSeverity", var1, var2);
      String var3 = this._LogWatchSeverity;
      this._LogWatchSeverity = var1;
      this._postSet(3, var3, var1);
   }

   public void addWatch(WLDFWatchBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 4)) {
         WLDFWatchBean[] var2;
         if (this._isSet(4)) {
            var2 = (WLDFWatchBean[])((WLDFWatchBean[])this._getHelper()._extendArray(this.getWatches(), WLDFWatchBean.class, var1));
         } else {
            var2 = new WLDFWatchBean[]{var1};
         }

         try {
            this.setWatches(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WLDFWatchBean[] getWatches() {
      return this._Watches;
   }

   public boolean isWatchesSet() {
      return this._isSet(4);
   }

   public void removeWatch(WLDFWatchBean var1) {
      this.destroyWatch(var1);
   }

   public void setWatches(WLDFWatchBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WLDFWatchBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 4)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      WLDFWatchBean[] var5 = this._Watches;
      this._Watches = (WLDFWatchBean[])var4;
      this._postSet(4, var5, var4);
   }

   public WLDFWatchBean createWatch(String var1) {
      WLDFWatchBeanImpl var2 = new WLDFWatchBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWatch(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWatch(WLDFWatchBean var1) {
      try {
         this._checkIsPotentialChild(var1, 4);
         WLDFWatchBean[] var2 = this.getWatches();
         WLDFWatchBean[] var3 = (WLDFWatchBean[])((WLDFWatchBean[])this._getHelper()._removeElement(var2, WLDFWatchBean.class, var1));
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
               this.setWatches(var3);
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

   public void addNotification(WLDFNotificationBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 5)) {
         WLDFNotificationBean[] var2 = (WLDFNotificationBean[])((WLDFNotificationBean[])this._getHelper()._extendArray(this.getNotifications(), WLDFNotificationBean.class, var1));

         try {
            this.setNotifications(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WLDFNotificationBean[] getNotifications() {
      return this._customizer.getNotifications();
   }

   public boolean isNotificationsSet() {
      return this._isSet(5);
   }

   public void removeNotification(WLDFNotificationBean var1) {
      WLDFNotificationBean[] var2 = this.getNotifications();
      WLDFNotificationBean[] var3 = (WLDFNotificationBean[])((WLDFNotificationBean[])this._getHelper()._removeElement(var2, WLDFNotificationBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setNotifications(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setNotifications(WLDFNotificationBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new WLDFNotificationBeanImpl[0] : var1;
      this._Notifications = (WLDFNotificationBean[])var2;
   }

   public WLDFNotificationBean lookupNotification(String var1) {
      return this._customizer.lookupNotification(var1);
   }

   public void addImageNotification(WLDFImageNotificationBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 6)) {
         WLDFImageNotificationBean[] var2;
         if (this._isSet(6)) {
            var2 = (WLDFImageNotificationBean[])((WLDFImageNotificationBean[])this._getHelper()._extendArray(this.getImageNotifications(), WLDFImageNotificationBean.class, var1));
         } else {
            var2 = new WLDFImageNotificationBean[]{var1};
         }

         try {
            this.setImageNotifications(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WLDFImageNotificationBean[] getImageNotifications() {
      return this._ImageNotifications;
   }

   public boolean isImageNotificationsSet() {
      return this._isSet(6);
   }

   public void removeImageNotification(WLDFImageNotificationBean var1) {
      this.destroyImageNotification(var1);
   }

   public void setImageNotifications(WLDFImageNotificationBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WLDFImageNotificationBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 6)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WLDFImageNotificationBean[] var5 = this._ImageNotifications;
      this._ImageNotifications = (WLDFImageNotificationBean[])var4;
      this._postSet(6, var5, var4);
   }

   public WLDFImageNotificationBean createImageNotification(String var1) {
      WLDFImageNotificationBeanImpl var2 = new WLDFImageNotificationBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addImageNotification(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyImageNotification(WLDFImageNotificationBean var1) {
      try {
         this._checkIsPotentialChild(var1, 6);
         WLDFImageNotificationBean[] var2 = this.getImageNotifications();
         WLDFImageNotificationBean[] var3 = (WLDFImageNotificationBean[])((WLDFImageNotificationBean[])this._getHelper()._removeElement(var2, WLDFImageNotificationBean.class, var1));
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
               this.setImageNotifications(var3);
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

   public WLDFImageNotificationBean lookupImageNotification(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ImageNotifications).iterator();

      WLDFImageNotificationBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WLDFImageNotificationBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSNotification(WLDFJMSNotificationBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         WLDFJMSNotificationBean[] var2;
         if (this._isSet(7)) {
            var2 = (WLDFJMSNotificationBean[])((WLDFJMSNotificationBean[])this._getHelper()._extendArray(this.getJMSNotifications(), WLDFJMSNotificationBean.class, var1));
         } else {
            var2 = new WLDFJMSNotificationBean[]{var1};
         }

         try {
            this.setJMSNotifications(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WLDFJMSNotificationBean[] getJMSNotifications() {
      return this._JMSNotifications;
   }

   public boolean isJMSNotificationsSet() {
      return this._isSet(7);
   }

   public void removeJMSNotification(WLDFJMSNotificationBean var1) {
      this.destroyJMSNotification(var1);
   }

   public void setJMSNotifications(WLDFJMSNotificationBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WLDFJMSNotificationBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 7)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WLDFJMSNotificationBean[] var5 = this._JMSNotifications;
      this._JMSNotifications = (WLDFJMSNotificationBean[])var4;
      this._postSet(7, var5, var4);
   }

   public WLDFJMSNotificationBean createJMSNotification(String var1) {
      WLDFJMSNotificationBeanImpl var2 = new WLDFJMSNotificationBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSNotification(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSNotification(WLDFJMSNotificationBean var1) {
      try {
         this._checkIsPotentialChild(var1, 7);
         WLDFJMSNotificationBean[] var2 = this.getJMSNotifications();
         WLDFJMSNotificationBean[] var3 = (WLDFJMSNotificationBean[])((WLDFJMSNotificationBean[])this._getHelper()._removeElement(var2, WLDFJMSNotificationBean.class, var1));
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
               this.setJMSNotifications(var3);
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

   public WLDFJMSNotificationBean lookupJMSNotification(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSNotifications).iterator();

      WLDFJMSNotificationBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WLDFJMSNotificationBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMXNotification(WLDFJMXNotificationBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 8)) {
         WLDFJMXNotificationBean[] var2;
         if (this._isSet(8)) {
            var2 = (WLDFJMXNotificationBean[])((WLDFJMXNotificationBean[])this._getHelper()._extendArray(this.getJMXNotifications(), WLDFJMXNotificationBean.class, var1));
         } else {
            var2 = new WLDFJMXNotificationBean[]{var1};
         }

         try {
            this.setJMXNotifications(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WLDFJMXNotificationBean[] getJMXNotifications() {
      return this._JMXNotifications;
   }

   public boolean isJMXNotificationsSet() {
      return this._isSet(8);
   }

   public void removeJMXNotification(WLDFJMXNotificationBean var1) {
      this.destroyJMXNotification(var1);
   }

   public void setJMXNotifications(WLDFJMXNotificationBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WLDFJMXNotificationBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 8)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WLDFJMXNotificationBean[] var5 = this._JMXNotifications;
      this._JMXNotifications = (WLDFJMXNotificationBean[])var4;
      this._postSet(8, var5, var4);
   }

   public WLDFJMXNotificationBean createJMXNotification(String var1) {
      WLDFJMXNotificationBeanImpl var2 = new WLDFJMXNotificationBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMXNotification(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMXNotification(WLDFJMXNotificationBean var1) {
      try {
         this._checkIsPotentialChild(var1, 8);
         WLDFJMXNotificationBean[] var2 = this.getJMXNotifications();
         WLDFJMXNotificationBean[] var3 = (WLDFJMXNotificationBean[])((WLDFJMXNotificationBean[])this._getHelper()._removeElement(var2, WLDFJMXNotificationBean.class, var1));
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
               this.setJMXNotifications(var3);
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

   public WLDFJMXNotificationBean lookupJMXNotification(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMXNotifications).iterator();

      WLDFJMXNotificationBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WLDFJMXNotificationBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addSMTPNotification(WLDFSMTPNotificationBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 9)) {
         WLDFSMTPNotificationBean[] var2;
         if (this._isSet(9)) {
            var2 = (WLDFSMTPNotificationBean[])((WLDFSMTPNotificationBean[])this._getHelper()._extendArray(this.getSMTPNotifications(), WLDFSMTPNotificationBean.class, var1));
         } else {
            var2 = new WLDFSMTPNotificationBean[]{var1};
         }

         try {
            this.setSMTPNotifications(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WLDFSMTPNotificationBean[] getSMTPNotifications() {
      return this._SMTPNotifications;
   }

   public boolean isSMTPNotificationsSet() {
      return this._isSet(9);
   }

   public void removeSMTPNotification(WLDFSMTPNotificationBean var1) {
      this.destroySMTPNotification(var1);
   }

   public void setSMTPNotifications(WLDFSMTPNotificationBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WLDFSMTPNotificationBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 9)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WLDFSMTPNotificationBean[] var5 = this._SMTPNotifications;
      this._SMTPNotifications = (WLDFSMTPNotificationBean[])var4;
      this._postSet(9, var5, var4);
   }

   public WLDFSMTPNotificationBean createSMTPNotification(String var1) {
      WLDFSMTPNotificationBeanImpl var2 = new WLDFSMTPNotificationBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSMTPNotification(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroySMTPNotification(WLDFSMTPNotificationBean var1) {
      try {
         this._checkIsPotentialChild(var1, 9);
         WLDFSMTPNotificationBean[] var2 = this.getSMTPNotifications();
         WLDFSMTPNotificationBean[] var3 = (WLDFSMTPNotificationBean[])((WLDFSMTPNotificationBean[])this._getHelper()._removeElement(var2, WLDFSMTPNotificationBean.class, var1));
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
               this.setSMTPNotifications(var3);
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

   public WLDFSMTPNotificationBean lookupSMTPNotification(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SMTPNotifications).iterator();

      WLDFSMTPNotificationBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WLDFSMTPNotificationBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addSNMPNotification(WLDFSNMPNotificationBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 10)) {
         WLDFSNMPNotificationBean[] var2;
         if (this._isSet(10)) {
            var2 = (WLDFSNMPNotificationBean[])((WLDFSNMPNotificationBean[])this._getHelper()._extendArray(this.getSNMPNotifications(), WLDFSNMPNotificationBean.class, var1));
         } else {
            var2 = new WLDFSNMPNotificationBean[]{var1};
         }

         try {
            this.setSNMPNotifications(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WLDFSNMPNotificationBean[] getSNMPNotifications() {
      return this._SNMPNotifications;
   }

   public boolean isSNMPNotificationsSet() {
      return this._isSet(10);
   }

   public void removeSNMPNotification(WLDFSNMPNotificationBean var1) {
      this.destroySNMPNotification(var1);
   }

   public void setSNMPNotifications(WLDFSNMPNotificationBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WLDFSNMPNotificationBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 10)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WLDFSNMPNotificationBean[] var5 = this._SNMPNotifications;
      this._SNMPNotifications = (WLDFSNMPNotificationBean[])var4;
      this._postSet(10, var5, var4);
   }

   public WLDFSNMPNotificationBean createSNMPNotification(String var1) {
      WLDFSNMPNotificationBeanImpl var2 = new WLDFSNMPNotificationBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPNotification(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroySNMPNotification(WLDFSNMPNotificationBean var1) {
      try {
         this._checkIsPotentialChild(var1, 10);
         WLDFSNMPNotificationBean[] var2 = this.getSNMPNotifications();
         WLDFSNMPNotificationBean[] var3 = (WLDFSNMPNotificationBean[])((WLDFSNMPNotificationBean[])this._getHelper()._removeElement(var2, WLDFSNMPNotificationBean.class, var1));
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
               this.setSNMPNotifications(var3);
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

   public WLDFSNMPNotificationBean lookupSNMPNotification(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SNMPNotifications).iterator();

      WLDFSNMPNotificationBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WLDFSNMPNotificationBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      WatchValidator.validateWatchNotificationBean(this);
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
         var1 = 6;
      }

      try {
         switch (var1) {
            case 6:
               this._ImageNotifications = new WLDFImageNotificationBean[0];
               if (var2) {
                  break;
               }
            case 7:
               this._JMSNotifications = new WLDFJMSNotificationBean[0];
               if (var2) {
                  break;
               }
            case 8:
               this._JMXNotifications = new WLDFJMXNotificationBean[0];
               if (var2) {
                  break;
               }
            case 3:
               this._LogWatchSeverity = "Warning";
               if (var2) {
                  break;
               }
            case 5:
               this._Notifications = new WLDFNotificationBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._SMTPNotifications = new WLDFSMTPNotificationBean[0];
               if (var2) {
                  break;
               }
            case 10:
               this._SNMPNotifications = new WLDFSNMPNotificationBean[0];
               if (var2) {
                  break;
               }
            case 2:
               this._Severity = "Notice";
               if (var2) {
                  break;
               }
            case 4:
               this._Watches = new WLDFWatchBean[0];
               if (var2) {
                  break;
               }
            case 1:
               this._Enabled = true;
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

   public static class SchemaHelper2 extends WLDFBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 6:
               if (var1.equals("watche")) {
                  return 4;
               }
               break;
            case 7:
               if (var1.equals("enabled")) {
                  return 1;
               }
               break;
            case 8:
               if (var1.equals("severity")) {
                  return 2;
               }
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            default:
               break;
            case 12:
               if (var1.equals("notification")) {
                  return 5;
               }
               break;
            case 16:
               if (var1.equals("jms-notification")) {
                  return 7;
               }

               if (var1.equals("jmx-notification")) {
                  return 8;
               }
               break;
            case 17:
               if (var1.equals("smtp-notification")) {
                  return 9;
               }

               if (var1.equals("snmp-notification")) {
                  return 10;
               }
               break;
            case 18:
               if (var1.equals("image-notification")) {
                  return 6;
               }

               if (var1.equals("log-watch-severity")) {
                  return 3;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 4:
               return new WLDFWatchBeanImpl.SchemaHelper2();
            case 5:
            default:
               return super.getSchemaHelper(var1);
            case 6:
               return new WLDFImageNotificationBeanImpl.SchemaHelper2();
            case 7:
               return new WLDFJMSNotificationBeanImpl.SchemaHelper2();
            case 8:
               return new WLDFJMXNotificationBeanImpl.SchemaHelper2();
            case 9:
               return new WLDFSMTPNotificationBeanImpl.SchemaHelper2();
            case 10:
               return new WLDFSNMPNotificationBeanImpl.SchemaHelper2();
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 1:
               return "enabled";
            case 2:
               return "severity";
            case 3:
               return "log-watch-severity";
            case 4:
               return "watche";
            case 5:
               return "notification";
            case 6:
               return "image-notification";
            case 7:
               return "jms-notification";
            case 8:
               return "jmx-notification";
            case 9:
               return "smtp-notification";
            case 10:
               return "snmp-notification";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 4:
               return true;
            case 5:
               return true;
            case 6:
               return true;
            case 7:
               return true;
            case 8:
               return true;
            case 9:
               return true;
            case 10:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 4:
               return true;
            case 5:
            default:
               return super.isBean(var1);
            case 6:
               return true;
            case 7:
               return true;
            case 8:
               return true;
            case 9:
               return true;
            case 10:
               return true;
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

   protected static class Helper extends WLDFBeanImpl.Helper {
      private WLDFWatchNotificationBeanImpl bean;

      protected Helper(WLDFWatchNotificationBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 1:
               return "Enabled";
            case 2:
               return "Severity";
            case 3:
               return "LogWatchSeverity";
            case 4:
               return "Watches";
            case 5:
               return "Notifications";
            case 6:
               return "ImageNotifications";
            case 7:
               return "JMSNotifications";
            case 8:
               return "JMXNotifications";
            case 9:
               return "SMTPNotifications";
            case 10:
               return "SNMPNotifications";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ImageNotifications")) {
            return 6;
         } else if (var1.equals("JMSNotifications")) {
            return 7;
         } else if (var1.equals("JMXNotifications")) {
            return 8;
         } else if (var1.equals("LogWatchSeverity")) {
            return 3;
         } else if (var1.equals("Notifications")) {
            return 5;
         } else if (var1.equals("SMTPNotifications")) {
            return 9;
         } else if (var1.equals("SNMPNotifications")) {
            return 10;
         } else if (var1.equals("Severity")) {
            return 2;
         } else if (var1.equals("Watches")) {
            return 4;
         } else {
            return var1.equals("Enabled") ? 1 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getImageNotifications()));
         var1.add(new ArrayIterator(this.bean.getJMSNotifications()));
         var1.add(new ArrayIterator(this.bean.getJMXNotifications()));
         var1.add(new ArrayIterator(this.bean.getSMTPNotifications()));
         var1.add(new ArrayIterator(this.bean.getSNMPNotifications()));
         var1.add(new ArrayIterator(this.bean.getWatches()));
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
            for(var7 = 0; var7 < this.bean.getImageNotifications().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getImageNotifications()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSNotifications().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSNotifications()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMXNotifications().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMXNotifications()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isLogWatchSeveritySet()) {
               var2.append("LogWatchSeverity");
               var2.append(String.valueOf(this.bean.getLogWatchSeverity()));
            }

            if (this.bean.isNotificationsSet()) {
               var2.append("Notifications");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getNotifications())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSMTPNotifications().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSMTPNotifications()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPNotifications().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPNotifications()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isSeveritySet()) {
               var2.append("Severity");
               var2.append(String.valueOf(this.bean.getSeverity()));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWatches().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWatches()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isEnabledSet()) {
               var2.append("Enabled");
               var2.append(String.valueOf(this.bean.isEnabled()));
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
            WLDFWatchNotificationBeanImpl var2 = (WLDFWatchNotificationBeanImpl)var1;
            this.computeChildDiff("ImageNotifications", this.bean.getImageNotifications(), var2.getImageNotifications(), true);
            this.computeChildDiff("JMSNotifications", this.bean.getJMSNotifications(), var2.getJMSNotifications(), true);
            this.computeChildDiff("JMXNotifications", this.bean.getJMXNotifications(), var2.getJMXNotifications(), true);
            this.computeDiff("LogWatchSeverity", this.bean.getLogWatchSeverity(), var2.getLogWatchSeverity(), true);
            this.computeChildDiff("SMTPNotifications", this.bean.getSMTPNotifications(), var2.getSMTPNotifications(), true);
            this.computeChildDiff("SNMPNotifications", this.bean.getSNMPNotifications(), var2.getSNMPNotifications(), true);
            this.computeDiff("Severity", this.bean.getSeverity(), var2.getSeverity(), true);
            this.computeChildDiff("Watches", this.bean.getWatches(), var2.getWatches(), true);
            this.computeDiff("Enabled", this.bean.isEnabled(), var2.isEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WLDFWatchNotificationBeanImpl var3 = (WLDFWatchNotificationBeanImpl)var1.getSourceBean();
            WLDFWatchNotificationBeanImpl var4 = (WLDFWatchNotificationBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ImageNotifications")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addImageNotification((WLDFImageNotificationBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeImageNotification((WLDFImageNotificationBean)var2.getRemovedObject());
                  }

                  if (var3.getImageNotifications() == null || var3.getImageNotifications().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 6);
                  }
               } else if (var5.equals("JMSNotifications")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addJMSNotification((WLDFJMSNotificationBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeJMSNotification((WLDFJMSNotificationBean)var2.getRemovedObject());
                  }

                  if (var3.getJMSNotifications() == null || var3.getJMSNotifications().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  }
               } else if (var5.equals("JMXNotifications")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addJMXNotification((WLDFJMXNotificationBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeJMXNotification((WLDFJMXNotificationBean)var2.getRemovedObject());
                  }

                  if (var3.getJMXNotifications() == null || var3.getJMXNotifications().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  }
               } else if (var5.equals("LogWatchSeverity")) {
                  var3.setLogWatchSeverity(var4.getLogWatchSeverity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (!var5.equals("Notifications")) {
                  if (var5.equals("SMTPNotifications")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addSMTPNotification((WLDFSMTPNotificationBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeSMTPNotification((WLDFSMTPNotificationBean)var2.getRemovedObject());
                     }

                     if (var3.getSMTPNotifications() == null || var3.getSMTPNotifications().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                     }
                  } else if (var5.equals("SNMPNotifications")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addSNMPNotification((WLDFSNMPNotificationBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeSNMPNotification((WLDFSNMPNotificationBean)var2.getRemovedObject());
                     }

                     if (var3.getSNMPNotifications() == null || var3.getSNMPNotifications().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                     }
                  } else if (var5.equals("Severity")) {
                     var3.setSeverity(var4.getSeverity());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("Watches")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addWatch((WLDFWatchBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeWatch((WLDFWatchBean)var2.getRemovedObject());
                     }

                     if (var3.getWatches() == null || var3.getWatches().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 4);
                     }
                  } else if (var5.equals("Enabled")) {
                     var3.setEnabled(var4.isEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 1);
                  } else {
                     super.applyPropertyUpdate(var1, var2);
                  }
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
            WLDFWatchNotificationBeanImpl var5 = (WLDFWatchNotificationBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            int var8;
            if ((var3 == null || !var3.contains("ImageNotifications")) && this.bean.isImageNotificationsSet() && !var5._isSet(6)) {
               WLDFImageNotificationBean[] var6 = this.bean.getImageNotifications();
               WLDFImageNotificationBean[] var7 = new WLDFImageNotificationBean[var6.length];

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (WLDFImageNotificationBean)((WLDFImageNotificationBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setImageNotifications(var7);
            }

            if ((var3 == null || !var3.contains("JMSNotifications")) && this.bean.isJMSNotificationsSet() && !var5._isSet(7)) {
               WLDFJMSNotificationBean[] var11 = this.bean.getJMSNotifications();
               WLDFJMSNotificationBean[] var14 = new WLDFJMSNotificationBean[var11.length];

               for(var8 = 0; var8 < var14.length; ++var8) {
                  var14[var8] = (WLDFJMSNotificationBean)((WLDFJMSNotificationBean)this.createCopy((AbstractDescriptorBean)var11[var8], var2));
               }

               var5.setJMSNotifications(var14);
            }

            if ((var3 == null || !var3.contains("JMXNotifications")) && this.bean.isJMXNotificationsSet() && !var5._isSet(8)) {
               WLDFJMXNotificationBean[] var12 = this.bean.getJMXNotifications();
               WLDFJMXNotificationBean[] var16 = new WLDFJMXNotificationBean[var12.length];

               for(var8 = 0; var8 < var16.length; ++var8) {
                  var16[var8] = (WLDFJMXNotificationBean)((WLDFJMXNotificationBean)this.createCopy((AbstractDescriptorBean)var12[var8], var2));
               }

               var5.setJMXNotifications(var16);
            }

            if ((var3 == null || !var3.contains("LogWatchSeverity")) && this.bean.isLogWatchSeveritySet()) {
               var5.setLogWatchSeverity(this.bean.getLogWatchSeverity());
            }

            if ((var3 == null || !var3.contains("SMTPNotifications")) && this.bean.isSMTPNotificationsSet() && !var5._isSet(9)) {
               WLDFSMTPNotificationBean[] var13 = this.bean.getSMTPNotifications();
               WLDFSMTPNotificationBean[] var18 = new WLDFSMTPNotificationBean[var13.length];

               for(var8 = 0; var8 < var18.length; ++var8) {
                  var18[var8] = (WLDFSMTPNotificationBean)((WLDFSMTPNotificationBean)this.createCopy((AbstractDescriptorBean)var13[var8], var2));
               }

               var5.setSMTPNotifications(var18);
            }

            if ((var3 == null || !var3.contains("SNMPNotifications")) && this.bean.isSNMPNotificationsSet() && !var5._isSet(10)) {
               WLDFSNMPNotificationBean[] var15 = this.bean.getSNMPNotifications();
               WLDFSNMPNotificationBean[] var19 = new WLDFSNMPNotificationBean[var15.length];

               for(var8 = 0; var8 < var19.length; ++var8) {
                  var19[var8] = (WLDFSNMPNotificationBean)((WLDFSNMPNotificationBean)this.createCopy((AbstractDescriptorBean)var15[var8], var2));
               }

               var5.setSNMPNotifications(var19);
            }

            if ((var3 == null || !var3.contains("Severity")) && this.bean.isSeveritySet()) {
               var5.setSeverity(this.bean.getSeverity());
            }

            if ((var3 == null || !var3.contains("Watches")) && this.bean.isWatchesSet() && !var5._isSet(4)) {
               WLDFWatchBean[] var17 = this.bean.getWatches();
               WLDFWatchBean[] var20 = new WLDFWatchBean[var17.length];

               for(var8 = 0; var8 < var20.length; ++var8) {
                  var20[var8] = (WLDFWatchBean)((WLDFWatchBean)this.createCopy((AbstractDescriptorBean)var17[var8], var2));
               }

               var5.setWatches(var20);
            }

            if ((var3 == null || !var3.contains("Enabled")) && this.bean.isEnabledSet()) {
               var5.setEnabled(this.bean.isEnabled());
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
         this.inferSubTree(this.bean.getImageNotifications(), var1, var2);
         this.inferSubTree(this.bean.getJMSNotifications(), var1, var2);
         this.inferSubTree(this.bean.getJMXNotifications(), var1, var2);
         this.inferSubTree(this.bean.getNotifications(), var1, var2);
         this.inferSubTree(this.bean.getSMTPNotifications(), var1, var2);
         this.inferSubTree(this.bean.getSNMPNotifications(), var1, var2);
         this.inferSubTree(this.bean.getWatches(), var1, var2);
      }
   }
}
