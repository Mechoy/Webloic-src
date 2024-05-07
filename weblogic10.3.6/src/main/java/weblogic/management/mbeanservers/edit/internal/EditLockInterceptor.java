package weblogic.management.mbeanservers.edit.internal;

import java.io.IOException;
import java.security.AccessController;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.NoAccessRuntimeException;
import weblogic.management.jmx.mbeanserver.WLSMBeanServer;
import weblogic.management.jmx.mbeanserver.WLSMBeanServerInterceptorBase;
import weblogic.management.jmx.modelmbean.WLSModelMBean;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class EditLockInterceptor extends WLSMBeanServerInterceptorBase {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugJMXEdit");
   private WLSMBeanServer wlsMBeanServer;
   private EditAccess editAccess;

   public EditLockInterceptor(WLSMBeanServer var1) {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.editAccess = ManagementServiceRestricted.getEditAccess(var2);
      this.wlsMBeanServer = var1;
   }

   public ObjectInstance createMBean(String var1, ObjectName var2) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
      this.checkEditLock();
      ObjectInstance var3 = super.createMBean(var1, var2);
      return var3;
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
      this.checkEditLock();
      ObjectInstance var4 = super.createMBean(var1, var2, var3);
      return var4;
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, Object[] var3, String[] var4) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
      this.checkEditLock();
      ObjectInstance var5 = super.createMBean(var1, var2, var3, var4);
      return var5;
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3, Object[] var4, String[] var5) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
      this.checkEditLock();
      ObjectInstance var6 = super.createMBean(var1, var2, var3, var4, var5);
      return var6;
   }

   public void setAttribute(ObjectName var1, Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
      this.checkEditLock();
      super.setAttribute(var1, var2);
   }

   public AttributeList setAttributes(ObjectName var1, AttributeList var2) throws InstanceNotFoundException, ReflectionException, IOException {
      this.checkEditLock();
      AttributeList var3 = super.setAttributes(var1, var2);
      return var3;
   }

   public Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
      if (this.editAccess.isEditor()) {
         return super.invoke(var1, var2, var3, var4);
      } else {
         boolean var6 = false;
         Object var7 = this.wlsMBeanServer.lookupObject(var1);
         if (var7 != null && var7 instanceof WLSModelMBean) {
            WLSModelMBean var8 = (WLSModelMBean)var7;
            String var9 = var8.getRole(var2, var3, var4);
            if (var9 != null) {
               if (!var9.equals("factory") && !var9.equals("collection")) {
                  if (var9.equals("operation")) {
                     String var10 = var8.getImpact(var2, var3, var4);
                     if (var10 != null && !var10.equals("info")) {
                        var6 = true;
                     }
                  }
               } else {
                  var6 = true;
               }
            }
         }

         if (var6) {
            this.checkEditLock();
         }

         return super.invoke(var1, var2, var3, var4);
      }
   }

   private void checkEditLock() {
      if (!this.editAccess.isEditor()) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Caller does not own the edit lock.");
         }

         throw new NoAccessRuntimeException("Operation can not be performed as caller has not started an edit session");
      }
   }
}
