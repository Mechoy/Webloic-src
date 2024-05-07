package weblogic.diagnostics.snmp.agent.monfox;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Map;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import monfox.toolkit.snmp.SnmpOid;
import monfox.toolkit.snmp.SnmpValue;
import monfox.toolkit.snmp.SnmpValueException;
import monfox.toolkit.snmp.agent.ext.table.SnmpMibTableAdaptor;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.MD5;
import weblogic.diagnostics.snmp.i18n.SNMPLogger;
import weblogic.logging.Loggable;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.ArrayUtils;

public class MBeanInstanceTableRow extends SnmpMibTableAdaptor.Row {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugSNMPAgent");
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String LOCATION_KEY = "Location";
   private static final int SIZE_LIMIT = 512;
   private static String domainName;
   private MBeanServerConnection mbeanServerConnection;
   private ObjectName objectName;
   private Map columnsMetadata;
   private String index;
   private String location;

   public MBeanInstanceTableRow(MBeanServerConnection var1, ObjectName var2, Map var3) {
      this.mbeanServerConnection = var1;
      this.objectName = var2;
      this.columnsMetadata = var3;
      this.index = computeIndex(this.objectName.toString());
      this.location = var2.getKeyProperty("Location");
   }

   public boolean isAvailableForContextName(String var1) {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Context name = " + var1);
      }

      if (var1 != null && var1.length() > 0) {
         if (var1.equals(getDomainName())) {
            return true;
         }

         if (this.location != null && this.location.length() > 0) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Location = " + this.location + " for " + this.objectName);
            }

            return this.location.equals(var1);
         }
      }

      return true;
   }

   public SnmpValue getValue(SnmpOid var1) throws SnmpValueException {
      String var2 = var1.getOidInfo().getName();
      final String var3 = (String)this.columnsMetadata.get(var2);
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Getting attribute " + var3 + " for SNMP Column " + var2);
      }

      if (var3.equals("Index")) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Index = " + this.index + " for " + var1);
         }

         return SnmpValue.getInstance(var1, this.index);
      } else if (var3.equals("ObjectName")) {
         return SnmpValue.getInstance(var1, this.objectName.toString());
      } else {
         Object var4 = null;

         try {
            var4 = SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  return MBeanInstanceTableRow.this.mbeanServerConnection.getAttribute(MBeanInstanceTableRow.this.objectName, var3);
               }
            });
         } catch (PrivilegedActionException var6) {
            throw new SnmpValueException("Error getting MBean attribute value");
         }

         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Got attribute " + var3 + " value " + var4);
         }

         if (var4 == null) {
            var4 = "";
         } else if (var4 instanceof Object[]) {
            var4 = toString((Object[])((Object[])var4));
         }

         String var5 = var4.toString();
         if (var5.length() > 512) {
            var5 = var5.substring(0, 512);
         }

         return SnmpValue.getInstance(var1, var5);
      }
   }

   private static String toString(Object[] var0) {
      ArrayList var1 = new ArrayList();
      ArrayUtils.addAll(var1, var0);
      return var1.toString();
   }

   public void setValue(SnmpOid var1, SnmpValue var2) throws SnmpValueException {
      Loggable var3 = SNMPLogger.logCannotModifyMBeanAttributesLoggable();
      String var4 = var3.getMessage();
      throw new SnmpValueException(var4);
   }

   static String computeIndex(String var0) {
      MD5 var1 = new MD5();
      byte[] var2 = new byte[16];
      var1.update(var0.toString().getBytes());
      var1.md5final(var2);
      return "0x" + MD5.dumpBytes(var2);
   }

   String getIndex() {
      return this.index;
   }

   static String getDomainName() {
      if (domainName == null) {
         domainName = ManagementService.getRuntimeAccess(KERNEL_ID).getDomainName();
      }

      return domainName;
   }

   ObjectName getObjectName() {
      return this.objectName;
   }

   String getAttributeName(String var1) {
      String var2 = (String)this.columnsMetadata.get(var1);
      return var2;
   }
}
