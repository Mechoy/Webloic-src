package weblogic.management.j2ee.internal;

import java.util.HashMap;
import java.util.Map;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.diagnostics.debug.DebugLogger;

final class NotificationHandler {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJ2EEManagement");
   private final Map j2eeobjectNameToImpl = new HashMap(3);
   private final String j2eeType;
   private final String wlsType;
   private final ObjectName wlsObjectName;
   private final String domain;
   private boolean registerThisObject = true;

   public NotificationHandler(ObjectName var1, String var2) {
      this.wlsObjectName = var1;
      this.wlsType = this.wlsObjectName.getKeyProperty("Type");
      this.j2eeType = Types.getJ2EETypeForWLSType(this.wlsType);
      this.domain = var2;
      this.processWLSObjectName();
   }

   private ObjectName getJ2EEObjectName() {
      String var1 = this.wlsObjectName.getKeyProperty("Name");
      StringBuffer var2 = new StringBuffer(this.domain + ":" + "name" + "=" + var1 + ",");
      StringBuffer var3 = new StringBuffer(this.domain + ":" + "Name" + "=" + var1 + ",");
      String[] var4 = JMOTypesHelper.getParentsForType(this.j2eeType);
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = var4[var5];
            String var7 = Types.getWLSTypeForJ2EEType(var6);
            String var8 = this.wlsObjectName.getKeyProperty(var7);
            var3.append(var7 + "=" + var8 + ",");
            var2.append(var6 + "=" + var8 + ",");
         }
      }

      var2.append("j2eeType=" + this.j2eeType);
      var3.append("Type=" + this.wlsType);
      ObjectName var9 = this.getJ2EEObjectName(var2);
      return var9;
   }

   private ObjectName getJ2EEObjectName(StringBuffer var1) {
      try {
         return new ObjectName(var1.toString());
      } catch (MalformedObjectNameException var3) {
         throw new AssertionError("Malformed ObjectName" + var3);
      }
   }

   private ObjectName getJ2EEObjectNameForModule(boolean var1) {
      String var2 = this.wlsObjectName.getKeyProperty("Name");
      StringBuffer var3 = new StringBuffer(this.domain + ":" + "name" + "=" + var2 + ",");
      String var4 = var1 ? this.wlsObjectName.getKeyProperty("ApplicationRuntime") : null;
      var3.append("J2EEApplication=" + var4 + ",");
      var3.append("J2EEServer=" + this.wlsObjectName.getKeyProperty("ServerRuntime") + ",");
      var3.append("j2eeType=" + this.j2eeType);
      return this.getJ2EEObjectName(var3);
   }

   private void processWLSObjectName() {
      // $FF: Couldn't be decompiled
   }

   private void setValues(ObjectName var1, Object var2) {
      if (debug.isDebugEnabled()) {
         debug.debug("setValues: objectName = " + var1 + " value = " + var2);
      }

      this.j2eeobjectNameToImpl.put(var1, var2);
   }

   private void processJ2EEDomainBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new J2EEDomainMBeanImpl(var1.getCanonicalName()));
   }

   private void processJ2EEServerBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new J2EEServerMBeanImpl(var1.getCanonicalName()));
      ObjectName var2 = this.makeObjectNameForType("JNDIResource");
      this.setValues(var2, new JNDIResourceMBeanImpl(var2.getCanonicalName()));
      ObjectName var3 = this.makeObjectNameForType("RMI_IIOPResource");
      this.setValues(var3, new RMI_IIOPResourceMBeanImpl(var3.getCanonicalName()));
   }

   private void processJ2EEApplicationBeanObject() {
      ApplicationInfo var1 = new ApplicationInfo(this.wlsObjectName, 1);
      if (!var1.isEar()) {
         this.registerThisObject = false;
      } else {
         ObjectName var2 = this.getJ2EEObjectName();
         this.setValues(var2, new J2EEApplicationMBeanImpl(var2.getCanonicalName(), this.getJ2EEServer(), var1));
      }
   }

   private void processJ2EEAppClientModuleBeanObject() {
      ApplicationInfo var1 = new ApplicationInfo(this.wlsObjectName, 5);
      ObjectName var2 = this.getJ2EEObjectName();
      this.setValues(var2, new AppClientModuleMBeanImpl(var2.getCanonicalName(), this.getJ2EEServer(), this.getJ2EEJVM(), var1));
   }

   private void processJ2EEEJBModuleBeanObject() {
      ApplicationInfo var1 = new ApplicationInfo(this.wlsObjectName, 3);
      ObjectName var2 = this.getJ2EEObjectNameForModule(var1.isParentEar());
      this.setValues(var2, new EJBModuleMBeanImpl(var2.getCanonicalName(), this.getJ2EEServer(), this.getJ2EEJVM(), var1));
   }

   private void processJ2EEWebModuleBeanObject() {
      ApplicationInfo var1 = new ApplicationInfo(this.wlsObjectName, 2);
      ObjectName var2 = this.getJ2EEObjectNameForModule(var1.isParentEar());
      this.setValues(var2, new WebModuleMBeanImpl(var2.getCanonicalName(), this.getJ2EEServer(), this.getJ2EEJVM(), var1));
   }

   private void processJ2EEResourceAdaptorModuleBeanObject() {
      ApplicationInfo var1 = new ApplicationInfo(this.wlsObjectName, 4);
      ObjectName var2 = this.getJ2EEObjectNameForModule(var1.isParentEar());
      this.setValues(var2, new ResourceAdapterModuleMBeanImpl(var2.getCanonicalName(), this.getJ2EEServer(), this.getJ2EEJVM(), var1));
      ObjectName var3 = this.getResourceAdapterObjectName(var2);
      ObjectName var4 = this.getJCAResourceObjectName(var2);
      this.setValues(var3, new ResourceAdapterMBeanImpl(var3.getCanonicalName(), var4.getCanonicalName()));
      this.setValues(var4, new JCAResourceMBeanImpl(var4.getCanonicalName(), this.wlsObjectName));
   }

   private void processJ2EEEntityBeanBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new EntityBeanMBeanImpl(var1.getCanonicalName()));
   }

   private void processJ2EEMessageDrivenBeanBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new MessageDrivenBeanMBeanImpl(var1.getCanonicalName()));
   }

   private void processJ2EEStatelessBeanBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new StatelessSessionBeanMBeanImpl(var1.getCanonicalName()));
   }

   private void processJ2EEStatefulBeanBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new StatefulSessionBeanMBeanImpl(var1.getCanonicalName()));
   }

   private void processJ2EEServletBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new ServletMBeanImpl(var1.getCanonicalName()));
   }

   private void processJ2EEJCAConFactoryBeanObject() {
      String var1 = this.wlsObjectName.getKeyProperty("ConnectorComponentRuntime");
      ObjectName var2 = this.getJ2EEConnectionFactoryObjectName(var1);
      ObjectName var3 = this.getJCAManagedConnectionFactoryObjectName(var2);
      this.setValues(var2, new JCAConnectionFactoryMBeanImpl(var2.getCanonicalName(), var3.getCanonicalName()));
      this.setValues(var3, new JCAManagedConnectionFactoryMBeanImpl(var3.getCanonicalName()));
   }

   private void processJ2EEJDBCDataSourceBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      ObjectName var2 = this.getObjectNameWithServerAsParent("JDBCDriver");
      this.setValues(var1, new JDBCDataSourceMBeanImpl(var1.getCanonicalName(), var2.getCanonicalName()));
      this.setValues(var2, new JDBCDriverMBeanImpl(var2.getCanonicalName()));
   }

   private void processJ2EEJDBCDriverBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new JDBCDriverMBeanImpl(var1.getCanonicalName()));
   }

   private void processJ2EEJDBCResourceBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new JDBCResourceMBeanImpl(var1.getCanonicalName(), this.wlsObjectName.getKeyProperty("ServerRuntime")));
   }

   private void processJ2EEJMSResourceBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new JMSResourceMBeanImpl(var1.getCanonicalName()));
   }

   private void processJ2EEJavaMailResourceBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new JavaMailResourceMBeanImpl(var1.getCanonicalName()));
   }

   private void processJ2EEJNDIResourceBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new JNDIResourceMBeanImpl(var1.getCanonicalName()));
   }

   private void processJ2EERMI_IIOP_ResourceBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new RMI_IIOPResourceMBeanImpl(var1.getCanonicalName()));
   }

   private void processJ2EEURLResourceBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new URLResourceMBeanImpl(var1.getCanonicalName()));
   }

   private void processJ2EEJTAResourceBeanObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new JTAResourceMBeanImpl(var1.getCanonicalName()));
   }

   private void processJ2EEJVMObject() {
      ObjectName var1 = this.getJ2EEObjectName();
      this.setValues(var1, new JVMMBeanImpl(var1.getCanonicalName(), this.wlsObjectName));
   }

   private String getJ2EEServer() {
      String var1 = this.wlsObjectName.getKeyProperty("ServerRuntime");
      return this.domain + ":" + "name" + "=" + var1 + "," + "j2eeType" + "=" + "J2EEServer";
   }

   private String getJ2EEJVM() {
      String var1 = this.wlsObjectName.getKeyProperty("ServerRuntime");
      return this.domain + ":" + "name" + "=" + var1 + "," + "j2eeType" + "=" + "JVM" + "," + "J2EEServer" + "=" + var1;
   }

   private ObjectName getResourceAdapterObjectName(ObjectName var1) {
      String var2 = var1.getKeyProperty("name");
      StringBuffer var3 = new StringBuffer(this.domain + ":" + "J2EEServer" + "=" + var1.getKeyProperty("J2EEServer") + "," + "J2EEApplication" + "=" + var1.getKeyProperty("J2EEApplication") + "," + "name=" + var2 + "," + "ResourceAdapterModule" + "=" + var2 + "," + "j2eeType" + "=" + "ResourceAdapter");
      return this.getJ2EEObjectName(var3);
   }

   private ObjectName getJCAResourceObjectName(ObjectName var1) {
      String var2 = var1.getKeyProperty("name");
      StringBuffer var3 = new StringBuffer(this.domain + ":" + "J2EEServer" + "=" + var1.getKeyProperty("J2EEServer") + "," + "ResourceAdapter" + "=" + var2 + "," + "name=" + var2 + "," + "j2eeType" + "=" + "JCAResource");
      return this.getJ2EEObjectName(var3);
   }

   private ObjectName getJCAManagedConnectionFactoryObjectName(ObjectName var1) {
      StringBuffer var2 = new StringBuffer(this.domain + ":" + "J2EEServer" + "=" + var1.getKeyProperty("J2EEServer") + "," + "name=" + var1.getKeyProperty("name") + "," + "j2eeType" + "=" + "JCAManagedConnectionFactory");
      return this.getJ2EEObjectName(var2);
   }

   private ObjectName getJ2EEConnectionFactoryObjectName(String var1) {
      StringBuffer var2 = new StringBuffer(this.domain + ":" + "J2EEServer" + "=" + this.wlsObjectName.getKeyProperty("ServerRuntime") + "," + "JCAResource" + "=" + var1 + "," + "ResourceAdapterModule" + "=" + var1 + "," + "name=" + this.wlsObjectName.getKeyProperty("Name") + "," + "j2eeType" + "=" + "JCAConnectionFactory");
      return this.getJ2EEObjectName(var2);
   }

   private ObjectName getObjectNameWithServerAsParent(String var1) {
      String var2 = this.wlsObjectName.getKeyProperty("Name");
      String var3 = this.wlsObjectName.getKeyProperty("ServerRuntime");
      StringBuffer var4 = new StringBuffer(this.domain + ":" + "name=" + var2 + "," + "J2EEServer" + "=" + var3 + "," + "j2eeType" + "=" + var1);
      return this.getJ2EEObjectName(var4);
   }

   private ObjectName makeObjectNameForType(String var1) {
      String var2 = this.wlsObjectName.getKeyProperty("Name");
      StringBuffer var3 = new StringBuffer(this.domain + ":" + "name=" + var2 + "," + "J2EEServer" + "=" + var2 + "," + "j2eeType" + "=" + var1);
      return this.getJ2EEObjectName(var3);
   }

   boolean registerThisObject() {
      return this.registerThisObject;
   }

   Map getJ2EEObjectNameToImplMap() {
      return this.j2eeobjectNameToImpl;
   }

   ObjectName getWLSObjectName() {
      return this.wlsObjectName;
   }

   private boolean validApp() {
      boolean var1 = false;

      try {
         Object var2 = null;
         String var3 = (String)MBeanServerConnectionProvider.getDomainMBeanServerConnection().getAttribute(this.wlsObjectName, "ApplicationName");
         String var4 = this.wlsObjectName.getDomain() + ":Name=" + var3 + ",Type=AppDeployment";

         try {
            MBeanServerConnection var5 = MBeanServerConnectionProvider.getEditMBeanServerConnection();
            var5.getObjectInstance(new ObjectName(var4));
            var1 = true;
         } catch (InstanceNotFoundException var7) {
            if (InternalAppDataCacheService.isInternalApp(var3)) {
               return true;
            }

            MBeanServerConnection var6 = MBeanServerConnectionProvider.geRuntimeMBeanServerConnection();
            var6.getObjectInstance(new ObjectName(var4));
            var1 = true;
         }

         return var1;
      } catch (Throwable var8) {
         if (debug.isDebugEnabled()) {
            debug.debug("validApp got exception ", var8);
         }

         this.registerThisObject = false;
         return var1;
      }
   }
}
