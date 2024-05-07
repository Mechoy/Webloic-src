package weblogic.marathon.ddinit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.WebBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.management.descriptors.Encoding;
import weblogic.marathon.fs.FS;
import weblogic.marathon.fs.FSUtils;

/** @deprecated */
public class EarInit extends ModuleInit {
   private Descriptor earRoot;
   private ApplicationBean std_dd;
   private Descriptor wlEarRoot;
   private WeblogicApplicationBean wlDD;

   public EarInit(FS var1) {
      super(var1);
   }

   protected void searchForComponents() {
   }

   protected void initDescriptors() {
   }

   private static void printUsage() {
      System.out.println("usage: java weblogic.marathon.ddinit.EarInit <base-directory or jar>");
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length != 1) {
         printUsage();
         System.exit(0);
      }

      String var1 = var0[0];
      FS var2 = FS.mount(new File(var1));
      EarInit var3 = new EarInit(var2);
      var3.setVerbose(true);
      var3.doCmdLineEarInit();
   }

   protected void doCmdLineEarInit() throws IOException {
      this.inform("Loading content");
      String[] var1 = FSUtils.getPaths(this.baseFS, "", "");
      EditableDescriptorManager var2 = new EditableDescriptorManager();
      this.earRoot = var2.createDescriptorRoot(ApplicationBean.class);
      this.std_dd = (ApplicationBean)this.earRoot.getRootBean();
      this.wlEarRoot = var2.createDescriptorRoot(WeblogicApplicationBean.class);
      this.wlDD = (WeblogicApplicationBean)this.wlEarRoot.getRootBean();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3];
         String var5 = var4;
         if (!var4.endsWith("/")) {
            int var6 = var4.lastIndexOf(47);
            if (var6 != -1) {
               var5 = var4.substring(var6 + 1);
            }

            String var7;
            if (var5.endsWith(".war")) {
               this.verbose("Found WAR archive '" + var4 + "'");
               int var20 = var5.lastIndexOf(46);
               var7 = var5.substring(0, var20);
               this.webModule(var4, var7);
            } else {
               ModuleBean var8;
               FS var19;
               if (var5.endsWith(".jar")) {
                  var7 = null;
                  var19 = this.baseFS.mountNested(var4);

                  try {
                     if (var19.exists("META-INF/ejb-jar.xml")) {
                        this.verbose("Found EJB archive '" + var4 + "'");
                        var8 = this.std_dd.createModule();
                        var8.setEjb(var4);
                     } else {
                        this.baseFS.unmountNested(var4);
                     }
                  } finally {
                     var19.close();
                  }
               } else if (var5.endsWith(".rar")) {
                  var7 = null;
                  var19 = this.baseFS.mountNested(var4);

                  try {
                     if (var19.exists("META-INF/ra.xml")) {
                        this.verbose("Found JCA archive '" + var4 + "'");
                        var8 = this.std_dd.createModule();
                        var8.setConnector(var4);
                     }
                  } finally {
                     var19.close();
                  }
               } else if (var5.equals("ejb-jar.xml") && this.parentPath(var4, 1).endsWith("META-INF")) {
                  var7 = this.parentPath(var4, 2);
                  this.verbose("Found exploded EJB module '" + var7 + "'");
                  var8 = this.std_dd.createModule();
                  var8.setEjb(var7);
               } else if (var5.equals("web.xml") && this.parentPath(var4, 1).endsWith("WEB-INF")) {
                  var7 = this.parentPath(var4, 2);
                  this.verbose("Found exploded WAR module '" + var7 + "'");
                  this.webModule(var7, var7);
               } else if (var5.equals("ra.xml") && this.parentPath(var4, 1).endsWith("META-INF")) {
                  var7 = this.parentPath(var4, 2);
                  this.verbose("Found exploded JCA module '" + var7 + "'");
                  var8 = this.std_dd.createModule();
                  var8.setConnector(var7);
               }
            }
         }
      }

      FS var17 = this.getBaseFS();
      ByteArrayOutputStream var18 = new ByteArrayOutputStream();
      this.earRoot.toXML(var18);
      var17.put("META-INF/application.xml", var18.toByteArray());
      var18.reset();
      this.wlEarRoot.toXML(var18);
      var17.put("META-INF/weblogic-application.xml", var18.toByteArray());
      var17.save();
   }

   private void webModule(String var1, String var2) {
      ModuleBean var3 = this.std_dd.createModule();
      WebBean var4 = var3.createWeb();
      var4.setWebUri(var1);
      var4.setContextRoot(var2);
   }

   private String parentPath(String var1, int var2) {
      if (var2 > 0 && !var1.equals("/") && !var1.equals("")) {
         if (var1.startsWith("/")) {
            var1 = var1.substring(1);
         }

         int var3 = var1.lastIndexOf(47);
         if (var3 < 0) {
            return var1;
         } else {
            var1 = var1.substring(0, var3);
            --var2;
            return this.parentPath(var1, var2);
         }
      } else {
         return var1;
      }
   }

   private String mime2java(String var1) {
      String var2 = null;
      if (var1 != null) {
         var2 = Encoding.getIANA2JavaMapping(var1);
      } else {
         var2 = "UTF8";
      }

      return var2;
   }

   private static void ppp(String var0) {
      System.out.println("[EarInit] " + var0);
   }
}
