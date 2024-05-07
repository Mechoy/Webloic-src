package weblogic.j2ee.descriptors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Properties;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.ApplicationDescriptorMBean;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.Encoding;
import weblogic.management.descriptors.TopLevelDescriptorMBean;
import weblogic.management.descriptors.XMLDeclarationMBean;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.descriptors.application.J2EEApplicationDescriptorMBean;
import weblogic.management.descriptors.application.weblogic.WeblogicApplicationMBean;
import weblogic.utils.Debug;
import weblogic.utils.io.XMLWriter;
import weblogic.utils.jars.RandomAccessJarFile;

public final class ApplicationDescriptorMBeanImpl extends XMLElementMBeanDelegate implements ApplicationDescriptorMBean, TopLevelDescriptorMBean {
   private static final long serialVersionUID = 473061217792399747L;
   private String persistentDestination = null;
   private J2EEApplicationDescriptorMBean j2eeAppDescr;
   private WeblogicApplicationMBean weblogicAppDescr;

   public ApplicationDescriptorMBeanImpl(String var1) {
      super(var1);
   }

   public ApplicationDescriptorMBeanImpl() {
   }

   public void setJ2EEApplicationDescriptor(J2EEApplicationDescriptorMBean var1) {
      this.j2eeAppDescr = var1;
   }

   public J2EEApplicationDescriptorMBean getJ2EEApplicationDescriptor() {
      return this.j2eeAppDescr;
   }

   public void unregister() throws ManagementException {
      super.unregister();
      if (this.j2eeAppDescr != null) {
         this.j2eeAppDescr.unregister();
      }

      if (this.weblogicAppDescr != null) {
         this.weblogicAppDescr.unregister();
      }

   }

   public void toXML(XMLWriter var1) {
      if (this.j2eeAppDescr != null) {
         this.j2eeAppDescr.toXML(var1);
      }

   }

   public String toXML(int var1) {
      return this.toXML();
   }

   public String toXML() {
      StringWriter var1 = new StringWriter();
      this.toXML(new XMLWriter(var1));
      return var1.toString();
   }

   public void validate() throws DescriptorValidationException {
   }

   public void usePersistenceDestination(String var1) {
      this.persistentDestination = var1;
   }

   public void persist() throws IOException {
      this.persist((Properties)null);
   }

   public void persist(Properties var1) throws IOException {
      Debug.assertion(this.persistentDestination != null, "must call usePersistenceDestination on " + this + " before calling persist");
      File var2 = new File(this.persistentDestination);
      Object var3 = null;
      RandomAccessJarFile var4 = null;
      File var5;
      String var6;
      XMLWriter var7;
      if (this.j2eeAppDescr != null) {
         if (var2.isDirectory()) {
            var5 = new File(var2, "META-INF/application.xml");
            var3 = new FileOutputStream(var5);
         } else {
            var6 = ".";
            var4 = new RandomAccessJarFile(new File(var6), var2);
            var3 = var4.writeEntry("META-INF/application.xml", true);
         }

         var7 = this.getXMLWriter(this.j2eeAppDescr, (OutputStream)var3);
         this.toXML(var7);
         var7.close();
      }

      if (this.weblogicAppDescr != null) {
         if (var2.isDirectory()) {
            var5 = new File(var2, "META-INF/weblogic-application.xml");
            var3 = new FileOutputStream(var5);
         } else {
            var6 = ".";
            var4 = new RandomAccessJarFile(new File(var6), var2);
            var3 = var4.writeEntry("META-INF/weblogic-application.xml", true);
         }

         var7 = this.getXMLWriter(this.weblogicAppDescr, (OutputStream)var3);
         var7.println(this.weblogicAppDescr.toXML(2));
         var7.close();
      }

      if (var1 != null) {
         if (var2.isDirectory()) {
            var5 = new File(var2.getPath() + File.separator + "META-INF", "_wl_dynamic_change_list.properties");
            var3 = new FileOutputStream(var5);
         } else {
            var3 = var4.writeEntry("META-INF/_wl_dynamic_change_list.properties", true);
         }

         var1.store((OutputStream)var3, "Dynamic DD change list");
         ((OutputStream)var3).close();
      }

      if (var4 != null) {
         var4.close();
      }

   }

   private XMLWriter getXMLWriter(XMLDeclarationMBean var1, OutputStream var2) throws IOException {
      String var3 = var1.getEncoding();
      XMLWriter var4 = null;
      if (var3 != null) {
         String var5 = Encoding.getIANA2JavaMapping(var3);
         var4 = new XMLWriter(var2, var5);
      } else {
         var4 = new XMLWriter(var2);
      }

      return var4;
   }

   public void setWeblogicApplicationDescriptor(WeblogicApplicationMBean var1) {
      this.weblogicAppDescr = var1;
   }

   public WeblogicApplicationMBean getWeblogicApplicationDescriptor() {
      return this.weblogicAppDescr;
   }
}
