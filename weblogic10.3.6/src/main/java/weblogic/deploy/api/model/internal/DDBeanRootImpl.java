package weblogic.deploy.api.model.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.shared.ModuleType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.model.WebLogicDDBeanRoot;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.deploy.api.spi.config.DescriptorParser;
import weblogic.deploy.api.spi.config.DescriptorSupport;
import weblogic.deploy.api.spi.config.DescriptorSupportManager;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;

public class DDBeanRootImpl extends DDBeanImpl implements WebLogicDDBeanRoot {
   private static final boolean debug = Debug.isDebug("model");
   private Document doc = null;
   private DescriptorParser dom = null;
   private String altdd;
   private ModuleType moduleType;
   private boolean initialized = false;
   private DescriptorBean beanTree = null;
   private boolean schemaBased;

   public DDBeanRootImpl(String var1, WebLogicDeployableObject var2, ModuleType var3) {
      super(var2);
      this.altdd = var1;
      this.moduleType = var3;
      this.setRoot(this);
   }

   public DDBeanRootImpl(String var1, WebLogicDeployableObject var2, ModuleType var3, DescriptorBean var4, boolean var5) {
      super(var2);
      this.altdd = var1;
      this.moduleType = var3;
      this.beanTree = var4;
      this.schemaBased = var5;
      this.setRoot(this);
      if (debug && var4 != null) {
         Debug.say("Descriptor bean for " + var3 + " is not null");
      }

      this.initialize();
      this.initialized = true;
   }

   public synchronized void setupDDBeanRoot(InputStream var1) throws DDBeanCreateException {
      if (!this.initialized) {
         try {
            ModuleType var2 = this.getModuleType();
            DescriptorSupport[] var7 = DescriptorSupportManager.getForModuleType(var2);
            DescriptorSupport var4 = null;
            if (var7.length >= 1) {
               var4 = var7[0];
               if (var7.length > 1) {
                  var7 = DescriptorSupportManager.getForBaseURI(this.altdd);
                  if (var7.length > 0) {
                     var4 = var7[0];
                  }
               }

               if (var1 != null) {
                  if (debug) {
                     Debug.say("+++++++++++ PARSING " + var2.toString());
                  }

                  this.dom = DescriptorParser.getDescriptorParser(var1, (DeploymentPlanBean)null, var4);
                  this.doc = this.dom.getDocument();
               }

               Document var5 = this.doc;
               this.initDD(var5, "/", (DDBean)null);
               this.initialized = true;
            } else {
               throw new DDBeanCreateException(SPIDeployerLogger.unknownDD(this.altdd, this.dObject.getUri()));
            }
         } catch (IOException var6) {
            DDBeanCreateException var3 = new DDBeanCreateException(var6.getMessage());
            var3.initCause(var6);
            throw var3;
         }
      }
   }

   public String getFilename() {
      return this.dObject.getFileName(this);
   }

   public String getDDBeanRootVersion() {
      return this.getDocumentVersion();
   }

   public ModuleType getType() {
      return this.moduleType;
   }

   public DeployableObject getDeployableObject() {
      return this.dObject;
   }

   /** @deprecated */
   public String getModuleDTDVersion() {
      return this.isSchemaBased() ? null : this.getDDBeanRootVersion();
   }

   public boolean hasDBean() {
      return this.beanTree != null;
   }

   public DescriptorBean getDescriptorBean() throws IOException {
      return this.beanTree;
   }

   public String getDocumentVersion() {
      return this.dom == null ? null : this.dom.getDocumentVersion();
   }

   public boolean isSchemaBased() {
      return this.schemaBased;
   }

   public Document getDocument() {
      return this.doc;
   }

   public DescriptorParser getDescriptorParser() {
      return this.dom;
   }

   public String getAltdd() {
      return this.altdd;
   }

   public ModuleType getModuleType() {
      return this.moduleType;
   }

   private void dumpNode(Node var1) {
      switch (var1.getNodeType()) {
         case 1:
            Debug.say("element: " + this.getNodeData(var1));
            break;
         case 2:
            Debug.say("attr: " + this.getNodeData(var1));
            break;
         case 3:
            Debug.say("text: " + this.getNodeData(var1));
            break;
         case 4:
            Debug.say("cdata: " + this.getNodeData(var1));
            break;
         case 5:
            Debug.say("entity ref: " + this.getNodeData(var1));
            break;
         case 6:
            Debug.say("entity: " + this.getNodeData(var1));
            break;
         case 7:
            Debug.say("pi: " + this.getNodeData(var1));
            break;
         case 8:
            Debug.say("comment: " + this.getNodeData(var1));
            break;
         case 9:
            Debug.say("doc: " + this.getNodeData(var1));
            break;
         case 10:
            Debug.say("doctype: " + this.getNodeData(var1));
            break;
         case 11:
            Debug.say("docfrag: " + this.getNodeData(var1));
            break;
         case 12:
            Debug.say("notation: " + this.getNodeData(var1));
      }

   }

   private String getNodeData(Node var1) {
      return var1.getNodeName() + " value: " + var1.getNodeValue();
   }

   public boolean isInitialized() {
      return this.initialized;
   }

   private void initialize() {
      ByteArrayOutputStream var1 = null;
      ByteArrayInputStream var2 = null;
      this.xpath = "/";
      if (this.beanTree != null) {
         try {
            if (this.beanTree instanceof WeblogicExtensionBean) {
               DescriptorSupportManager.registerWebLogicExtensions((WeblogicExtensionBean)this.beanTree, this.altdd);
            }

            if (this.moduleType != null) {
               var1 = new ByteArrayOutputStream();
               (new EditableDescriptorManager()).writeDescriptorAsXML(this.beanTree.getDescriptor(), var1);
               var2 = new ByteArrayInputStream(var1.toByteArray());
               this.setupDDBeanRoot(var2);
            }
         } catch (IOException var15) {
            if (debug) {
               Debug.say("Marshalling of Descriptor Bean failed. XML view will not be available for this bean  " + var15);
            }
         } catch (DDBeanCreateException var16) {
            if (debug) {
               Debug.say("Creation of child DD Beans failed " + var16);
            }
         } finally {
            try {
               if (var1 != null) {
                  var1.close();
               }

               if (var2 != null) {
                  var2.close();
               }
            } catch (IOException var14) {
            }

         }
      }

   }
}
