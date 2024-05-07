package weblogic.application.compiler.flow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.model.J2eeApplicationObject;
import javax.enterprise.deploy.shared.ModuleType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import weblogic.application.compiler.CompilerCtx;
import weblogic.deploy.api.model.EditableDeployableObject;
import weblogic.deploy.api.model.WebLogicDeployableObjectFactory;
import weblogic.deploy.api.model.sca.EditableScaApplicationObject;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.sca.descriptor.ComponentBean;
import weblogic.sca.descriptor.CompositeBean;
import weblogic.sca.descriptor.ScaContributionBean;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFile;

public class SCACompositeViewerFlow extends ModuleViewerFlow {
   private static final String SCA_CONTRIBUTION_URI = "META-INF/sca-contribution.xml";

   public SCACompositeViewerFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      VirtualJarFile var1 = this.ctx.getVSource();
      ScaContributionBean var2 = this.parseSCAComposite(var1);
      WebLogicDeployableObjectFactory var3 = this.ctx.getObjectFactory();
      if (var3 != null) {
         ArrayList var5 = new ArrayList();

         EditableScaApplicationObject var4;
         try {
            var4 = var3.createScaApplicationObject();
            CompositeBean[] var6 = var2.getComposites();
            int var7 = var6.length;

            int var8;
            for(var8 = 0; var8 < var7; ++var8) {
               CompositeBean var9 = var6[var8];
               ComponentBean[] var10 = var9.getComponents();
               int var11 = var10.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  ComponentBean var13 = var10[var12];
                  String var14 = var9.getName() + "/" + var13.getName();
                  EditableDeployableObject var15 = var3.createDeployableObject(var14, (String)null, WebLogicModuleType.SCA_JAVA);
                  var5.add(var15);
               }
            }

            J2eeApplicationObject var18 = (J2eeApplicationObject)this.ctx.getDeployableApplication();
            if (var18 != null) {
               DeployableObject[] var20 = var18.getDeployableObjects();
               var8 = var20.length;

               for(int var22 = 0; var22 < var8; ++var22) {
                  DeployableObject var23 = var20[var22];
                  var5.add((EditableDeployableObject)var23);
               }
            }
         } catch (IOException var17) {
            throw new ToolFailureException("Unable to create deployable object", var17);
         }

         try {
            var4.setVirtualJarFile(this.ctx.getVSource());
            var4.setRootBean((DescriptorBean)var2);
            var4.addRootBean("META-INF/sca-contribution.xml", (DescriptorBean)var2, (ModuleType)null);
            this.ctx.setDeployableApplication(var4);
            Iterator var19 = var5.iterator();

            while(var19.hasNext()) {
               EditableDeployableObject var21 = (EditableDeployableObject)var19.next();
               var4.addDeployableObject(var21);
            }
         } catch (Exception var16) {
            throw new ToolFailureException("Unable to create Application Object", var16);
         }
      }

   }

   public void cleanup() {
   }

   private ScaContributionBean parseSCAComposite(VirtualJarFile var1) throws ToolFailureException {
      try {
         SAXParserFactory var2 = SAXParserFactory.newInstance();
         SAXParser var3 = var2.newSAXParser();
         ZipEntry var4 = var1.getEntry("META-INF/sca-contribution.xml");
         if (var4 == null) {
            throw new IllegalArgumentException("no META-INF/sca-contribution.xml");
         } else {
            ContributionHandler var5 = new ContributionHandler();
            var3.parse(var1.getInputStream(var4), var5);
            ScaContributionBean var6 = this.createScaContributionBean();
            Iterator var7 = var1.entries();

            while(var7.hasNext()) {
               ZipEntry var8 = (ZipEntry)var7.next();
               if (var8.getName().matches("META-INF/.*[.]composite")) {
                  CompositeHandler var9 = new CompositeHandler(var6.createComposite());
                  var3.parse(var1.getInputStream(var8), var9);
               }
            }

            Set var15 = var5.getDeployables();
            CompositeBean[] var16 = var6.getComposites();
            int var17 = var16.length;

            for(int var10 = 0; var10 < var17; ++var10) {
               CompositeBean var11 = var16[var10];
               if (!var15.contains(var11.getName())) {
                  var6.destroyComposite(var11);
               }
            }

            return var6;
         }
      } catch (IOException var12) {
         throw new ToolFailureException("Error parsing SCA descriptors", var12);
      } catch (ParserConfigurationException var13) {
         throw new ToolFailureException("Error parsing SCA descriptors", var13);
      } catch (SAXException var14) {
         throw new ToolFailureException("Error parsing SCA descriptors", var14);
      }
   }

   private ScaContributionBean createScaContributionBean() {
      DescriptorManager var1 = new DescriptorManager();
      return (ScaContributionBean)var1.createDescriptorRoot(ScaContributionBean.class).getRootBean();
   }

   private static class CompositeHandler extends DefaultHandler {
      private final CompositeBean composite;
      private ComponentBean current;

      public CompositeHandler(CompositeBean var1) {
         this.composite = var1;
      }

      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (this.current == null) {
            if (var3.equals("component")) {
               this.current = this.composite.createComponent();
               this.current.setName(var4.getValue("name"));
            } else if (var3.equals("composite")) {
               this.composite.setName(var4.getValue("name"));
            }
         } else if (var3.startsWith("implementation.")) {
            this.current.setImplementationType(var3);
         }

      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (var3.equals("component")) {
            this.current = null;
         }

      }
   }

   private static class ContributionHandler extends DefaultHandler {
      HashSet<String> deployables;

      private ContributionHandler() {
         this.deployables = new HashSet();
      }

      public Set<String> getDeployables() {
         return this.deployables;
      }

      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (var3.equals("deployable")) {
            this.deployables.add(var4.getValue("composite"));
         }

      }

      // $FF: synthetic method
      ContributionHandler(Object var1) {
         this();
      }
   }
}
