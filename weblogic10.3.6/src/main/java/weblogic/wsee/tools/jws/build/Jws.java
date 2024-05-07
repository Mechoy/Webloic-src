package weblogic.wsee.tools.jws.build;

import com.bea.util.jam.JClass;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.ToolsUtil;
import weblogic.wsee.tools.jws.decl.port.HttpPort;
import weblogic.wsee.tools.jws.decl.port.HttpsPort;
import weblogic.wsee.tools.jws.decl.port.JmsPort;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.cow.CowReader;

public class Jws implements JwsInfo {
   private String fileName = null;
   private List<PortDecl> ports = new ArrayList();
   private JClass jClass = null;
   private File cowFile = null;
   private CowReader cowReader = null;
   private File srcDir = null;
   private WebServiceType webServiceType;
   private boolean isGenerateWsdl;
   private boolean isGenerateDescriptors;

   public Jws() {
      this.webServiceType = WebServiceType.JAXRPC;
      this.isGenerateWsdl = false;
      this.isGenerateDescriptors = false;
   }

   public void srcdir(File var1) {
      this.srcDir = var1;
   }

   public boolean hasSrcDir() {
      return this.srcDir != null;
   }

   public File getAbsoluteFile() {
      return (new File(this.srcDir, this.fileName)).getAbsoluteFile();
   }

   public void setFile(String var1) {
      if (StringUtil.isEmpty(var1)) {
         throw new IllegalArgumentException("file must not be null.");
      } else if (!var1.endsWith(".java")) {
         throw new IllegalArgumentException("file must be a java source file.");
      } else {
         this.fileName = ToolsUtil.normalize(var1);
      }
   }

   public String getFile() {
      return this.fileName;
   }

   public Iterator<PortDecl> getPorts() {
      return this.ports.iterator();
   }

   public void setJClass(JClass var1) {
      this.jClass = var1;
   }

   public JClass getJClass() {
      return this.jClass;
   }

   public HttpPort createWLHttpTransport() {
      HttpPort var1 = new HttpPort();
      this.ports.add(var1);
      return var1;
   }

   public HttpsPort createWLHttpsTransport() {
      HttpsPort var1 = new HttpsPort();
      this.ports.add(var1);
      return var1;
   }

   public CowReader getCowReader() {
      if (this.cowFile == null) {
         return null;
      } else {
         if (this.cowReader == null) {
            this.cowReader = CowReader.Factory.newInstance(this.cowFile);
         }

         return this.cowReader;
      }
   }

   public File getCowFile() {
      return this.cowFile;
   }

   public void setCompiledWsdl(File var1) {
      this.cowFile = var1;
   }

   public JmsPort createWLJmsTransport() {
      JmsPort var1 = new JmsPort();
      this.ports.add(var1);
      return var1;
   }

   public WebServiceType getType() {
      return this.webServiceType;
   }

   public void setType(String var1) {
      this.webServiceType = WebServiceType.valueOf(var1);
   }

   public boolean isGenerateWsdl() {
      return this.isGenerateWsdl;
   }

   public void setGenerateWsdl(boolean var1) {
      this.isGenerateWsdl = var1;
   }

   public boolean isGenerateDescriptors() {
      return this.isGenerateDescriptors;
   }

   public void setGenerateDescriptors(boolean var1) {
      this.isGenerateDescriptors = var1;
   }
}
