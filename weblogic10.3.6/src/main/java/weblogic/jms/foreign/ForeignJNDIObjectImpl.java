package weblogic.jms.foreign;

import java.util.HashMap;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.application.ModuleException;
import weblogic.deployment.jms.ForeignOpaqueReference;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.ForeignConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.ForeignDestinationBean;
import weblogic.j2ee.descriptor.wl.ForeignJNDIObjectBean;
import weblogic.j2ee.descriptor.wl.ForeignServerBean;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSException;
import weblogic.management.ManagementException;
import weblogic.management.utils.GenericBeanListener;

public class ForeignJNDIObjectImpl {
   private ForeignServerBean parent;
   private ForeignJNDIObjectBean foreignJNDIObjectBean;
   private boolean bound;
   private String localJNDIName;
   private String remoteJNDIName;
   private String username;
   private String password;
   private byte[] encryptedPassword;
   private boolean isDestination;
   private transient GenericBeanListener foreignJNDIObjectBeanListener;
   private static final HashMap foreignDestinationBeanSignatures = new HashMap();
   private static final HashMap foreignConnectionFactoryBeanSignatures = new HashMap();

   private ForeignJNDIObjectImpl(ForeignServerBean var1, ForeignJNDIObjectBean var2, boolean var3, HashMap var4) throws JMSException {
      this.parent = var1;
      this.foreignJNDIObjectBean = var2;
      this.isDestination = var3;
      DescriptorBean var5 = (DescriptorBean)this.foreignJNDIObjectBean;
      this.foreignJNDIObjectBeanListener = new GenericBeanListener(var5, this, var4, false);

      try {
         this.foreignJNDIObjectBeanListener.initialize();
      } catch (ManagementException var7) {
         throw new JMSException(var7);
      }
   }

   public ForeignJNDIObjectImpl(ForeignServerBean var1, ForeignDestinationBean var2) throws JMSException {
      this(var1, var2, true, foreignDestinationBeanSignatures);
   }

   public ForeignJNDIObjectImpl(ForeignServerBean var1, ForeignConnectionFactoryBean var2) throws JMSException {
      this(var1, var2, false, foreignConnectionFactoryBeanSignatures);
   }

   public void close() {
      this.unregisterBeanUpdateListeners();
   }

   synchronized void bind(boolean var1) throws JMSException {
      this.registerBeanUpdateListeners();
      this.doBind(this.foreignJNDIObjectBean.getLocalJNDIName(), var1);
   }

   void validateJNDI() throws ModuleException {
      String var1 = this.foreignJNDIObjectBean.getLocalJNDIName();
      Context var3 = JMSService.getContext();

      Object var2;
      try {
         var2 = var3.lookup(var1);
      } catch (NamingException var5) {
         return;
      }

      throw new ModuleException("The foreign object of name \"" + this.foreignJNDIObjectBean.getName() + "\" cannot bind to JNDI name \"" + var1 + "\" because another object is already bound there of type " + (var2 == null ? "null" : var2.getClass().getName()));
   }

   private void doBind(String var1, boolean var2) throws JMSException {
      if (var1 != null && var1.length() != 0) {
         try {
            Object var3 = this.getJNDIReference();
            Context var4 = JMSService.getContext(false);
            if (var2) {
               if (JMSDebug.JMSConfig.isDebugEnabled()) {
                  JMSDebug.JMSConfig.debug("Replacing binding of MBean " + this.foreignJNDIObjectBean.getName() + " bound to JNDI name " + var1);
               }

               var4.rebind(var1, var3);
            } else {
               if (JMSDebug.JMSConfig.isDebugEnabled()) {
                  JMSDebug.JMSConfig.debug("Binding MBean " + this.foreignJNDIObjectBean.getName() + " to JNDI name " + var1);
               }

               var4.bind(var1, var3);
            }

            this.bound = true;
         } catch (NamingException var5) {
            throw new JMSException(JMSExceptionLogger.logErrorInJNDIBindLoggable(var1), var5);
         }
      }
   }

   synchronized void unbind() {
      this.unregisterBeanUpdateListeners();
      this.doUnbind(this.foreignJNDIObjectBean.getLocalJNDIName());
   }

   private void doUnbind(String var1) {
      if (this.bound && var1 != null && var1.length() != 0) {
         try {
            Context var2 = JMSService.getContext(false);

            try {
               if (JMSDebug.JMSConfig.isDebugEnabled()) {
                  JMSDebug.JMSConfig.debug("Un-binding remote JMS object from " + var1);
               }

               var2.unbind(var1);
               this.bound = false;
            } finally {
               var2.close();
            }
         } catch (NamingException var8) {
            JMSLogger.logErrorInJNDIUnbind(var1, var8.toString());
         }

      }
   }

   private Object getJNDIReference() {
      return new ForeignOpaqueReference(this.parent, this.foreignJNDIObjectBean);
   }

   public String getLocalJNDIName() {
      return this.localJNDIName;
   }

   public void setLocalJNDIName(String var1) {
      this.localJNDIName = var1;
   }

   public String getRemoteJNDIName() {
      return this.remoteJNDIName;
   }

   public void setRemoteJNDIName(String var1) {
      this.remoteJNDIName = var1;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String var1) {
      this.username = var1;
   }

   public byte[] getPasswordEncrypted() {
      return this.encryptedPassword;
   }

   public void setPasswordEncrypted(byte[] var1) {
      this.encryptedPassword = var1;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public String getName() {
      return this.foreignJNDIObjectBean.getName();
   }

   public void setName(String var1) {
      throw new AssertionError("Name setter only here to satisfy interface: " + var1);
   }

   public String getNotes() {
      return this.foreignJNDIObjectBean.getNotes();
   }

   public void setNotes(String var1) {
      throw new AssertionError("Notes setter only here to satisfy interface: " + var1);
   }

   private void registerBeanUpdateListeners() {
      if (this.foreignJNDIObjectBeanListener != null) {
         this.foreignJNDIObjectBeanListener.open();
      } else {
         DescriptorBean var1 = (DescriptorBean)this.foreignJNDIObjectBean;
         HashMap var2 = this.isDestination ? foreignDestinationBeanSignatures : foreignConnectionFactoryBeanSignatures;
         this.foreignJNDIObjectBeanListener = new GenericBeanListener(var1, this, var2);
      }

   }

   private void unregisterBeanUpdateListeners() {
      if (this.foreignJNDIObjectBeanListener != null) {
         this.foreignJNDIObjectBeanListener.close();
         this.foreignJNDIObjectBeanListener = null;
      }

   }

   static {
      foreignDestinationBeanSignatures.put("LocalJNDIName", String.class);
      foreignDestinationBeanSignatures.put("RemoteJNDIName", String.class);
      foreignConnectionFactoryBeanSignatures.put("LocalJNDIName", String.class);
      foreignConnectionFactoryBeanSignatures.put("RemoteJNDIName", String.class);
      foreignConnectionFactoryBeanSignatures.put("Username", String.class);
      foreignConnectionFactoryBeanSignatures.put("Password", String.class);
      foreignConnectionFactoryBeanSignatures.put("PasswordEncrypted", byte[].class);
   }
}
