package weblogic.management.mbeanservers.edit.internal;

import java.io.IOException;
import java.security.AccessController;
import java.util.Date;
import java.util.HashMap;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementLogger;
import weblogic.management.WebLogicMBean;
import weblogic.management.jmx.mbeanserver.WLSMBeanServerInterceptorBase;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.mbeanservers.edit.RecordingManagerMBean;
import weblogic.management.mbeanservers.internal.RecordingManager;
import weblogic.management.provider.ManagementService;
import weblogic.management.scripting.WLSTPathUtil;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.StringUtils;

public class RecordingInterceptor extends WLSMBeanServerInterceptorBase {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugJMXEdit");
   private static HashMap cmOperToWLSTMap = null;
   private static final AuthenticatedSubject kernelIdentity;
   private HashMap pathCache = new HashMap();
   private String domainName = null;

   public RecordingInterceptor() {
      this.domainName = ManagementService.getRuntimeAccess(kernelIdentity).getDomainName();
   }

   public void setAttribute(ObjectName var1, Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
      try {
         if (RecordingManager.getInstance().isRecording()) {
            String var3 = var2.getName();
            Object var4 = var2.getValue();
            String var5 = this.lookupPath(var1);
            if (var5 != null) {
               this.write("cd('" + var5 + "')", true);
               String var6;
               if (var4 instanceof String && this.isEncrypted(var1, var3)) {
                  var6 = var3 + "_" + (new Date()).getTime();
                  this.encrypt(var3, var6, (String)var4);
               } else {
                  var6 = this.objectToString(var4);
                  if (var6.startsWith("jarray")) {
                     this.write("set('" + var3 + "'," + var6 + ")");
                  } else {
                     this.write("cmo.set" + var3 + "(" + var6 + ")");
                  }
               }
            }
         }
      } catch (IOException var7) {
         ManagementLogger.logSetAttributeRecordingIOException(var1, var2.getName(), var7);
      }

      super.setAttribute(var1, var2);
   }

   public Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
      try {
         if (RecordingManager.getInstance().isRecording() && !var2.startsWith("lookup") && !var2.equals("isSet")) {
            String var5;
            if (ConfigurationManagerMBean.OBJECT_NAME.equals(var1.toString())) {
               var5 = (String)cmOperToWLSTMap.get(var2);
               if (var5 != null) {
                  if (var2.equals("removeReferencesToBean")) {
                     String var6 = this.getParameterList(var3);
                     var5 = var5 + "(" + var6 + ")";
                  }

                  if (var2.equals("startEdit") && RecordingManager.getInstance().isVerbose()) {
                     AuthenticatedSubject var9 = SecurityServiceManager.getCurrentSubject(kernelIdentity);
                     String var7 = "# User " + SubjectUtils.getUsername(var9) + " starts a new edit session at " + (new Date()).toString();
                     var5 = var7 + "\n" + var5;
                  }

                  this.write(var5, true);
               }
            } else if (!RecordingManagerMBean.OBJECT_NAME.equals(var1.toString())) {
               var5 = this.lookupPath(var1);
               if (var5 != null) {
                  this.write("cd('" + var5 + "')", true);
                  this.write("cmo." + var2 + "(" + this.getParameterList(var3) + ")");
               }
            }
         }
      } catch (IOException var8) {
         ManagementLogger.logInvokeRecordingIOException(var1, var2, var8);
      }

      return super.invoke(var1, var2, var3, var4);
   }

   private String objectToString(Object var1) {
      if (var1 == null) {
         return "None";
      } else {
         String var7;
         if (var1 instanceof String) {
            var7 = StringUtils.escapeString((String)var1);
            return "'" + var7 + "'";
         } else if (var1 instanceof ObjectName) {
            var7 = this.lookupPath((ObjectName)var1);
            return "getMBean('" + var7 + "')";
         } else if (var1 instanceof String[]) {
            String[] var6 = (String[])((String[])var1);
            return this.arrayToString(var6, "String");
         } else if (var1 instanceof ObjectName[]) {
            ObjectName[] var5 = (ObjectName[])((ObjectName[])var1);
            return this.arrayToString(var5, "ObjectName");
         } else if (!(var1 instanceof WebLogicMBean[])) {
            return var1.toString();
         } else {
            WebLogicMBean[] var2 = (WebLogicMBean[])((WebLogicMBean[])var1);
            ObjectName[] var3 = new ObjectName[var2.length];

            for(int var4 = 0; var4 < var2.length; ++var4) {
               var3[var4] = var2[var4].getObjectName();
            }

            return this.arrayToString(var3, "ObjectName");
         }
      }
   }

   private String arrayToString(Object[] var1, String var2) {
      String var3 = "";

      for(int var4 = 0; var4 < var1.length; ++var4) {
         if (var4 != 0) {
            var3 = var3 + ", ";
         }

         var3 = var3 + var2 + "('" + var1[var4] + "')";
      }

      return "jarray.array([" + var3 + "], " + var2 + ")";
   }

   private String getParameterList(Object[] var1) {
      if (var1 != null && var1.length != 0) {
         StringBuffer var2 = new StringBuffer();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var3 != 0) {
               var2.append(", ");
            }

            var2.append(this.objectToString(var1[var3]));
         }

         return var2.toString();
      } else {
         return "";
      }
   }

   private String lookupPath(ObjectName var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("ObjectName can not be null.");
      } else {
         String var2 = (String)this.pathCache.get(var1);
         if (var2 != null) {
            return var2;
         } else {
            try {
               var2 = WLSTPathUtil.lookupPath(this, this.domainName, var1);
            } catch (Exception var4) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Recording error: can not get WLST path for " + var1.getCanonicalName(), var4);
               }
            }

            if (var2 == null) {
               return null;
            } else {
               if (var2.startsWith("Domains")) {
                  var2 = "/";
               } else {
                  var2 = "/" + var2;
               }

               this.pathCache.put(var1, var2);
               return var2;
            }
         }
      }
   }

   private boolean isEncrypted(ObjectName var1, String var2) {
      try {
         MBeanInfo var3 = this.getMBeanInfo(var1);
         if (var3 != null && var3 instanceof ModelMBeanInfo) {
            ModelMBeanAttributeInfo var4 = ((ModelMBeanInfo)var3).getAttribute(var2);
            if (var4 != null) {
               Object var5 = var4.getDescriptor().getFieldValue("com.bea.encrypted");
               if (var5 != null) {
                  return (Boolean)var5;
               }
            }
         }
      } catch (Exception var6) {
      }

      return false;
   }

   private void write(String var1) throws IOException {
      this.write(var1, false);
   }

   private void write(String var1, boolean var2) throws IOException {
      RecordingManager.getInstance().write(var1, var2, true);
   }

   private void encrypt(String var1, String var2, String var3) throws IOException {
      RecordingManager.getInstance().encrypt(var1, var2, var3);
   }

   static {
      cmOperToWLSTMap = new HashMap();
      cmOperToWLSTMap.put("startEdit", "startEdit()");
      cmOperToWLSTMap.put("stopEdit", "stopEdit('y')");
      cmOperToWLSTMap.put("cancelEdit", "cancelEdit('y')");
      cmOperToWLSTMap.put("undo", "undo(defaultAnswer='y')");
      cmOperToWLSTMap.put("undoUnactivatedChanges", "undo(defaultAnswer='y', unactivatedChanges='true')");
      cmOperToWLSTMap.put("activate", "activate()");
      cmOperToWLSTMap.put("removeReferencesToBean", "editService.getConfigurationManager().removeReferencesToBean");
      kernelIdentity = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }
}
