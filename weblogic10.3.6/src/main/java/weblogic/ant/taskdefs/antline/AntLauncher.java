package weblogic.ant.taskdefs.antline;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Main;
import weblogic.utils.Executable;
import weblogic.utils.StringUtils;
import weblogic.xml.stream.ElementFactory;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;
import weblogic.xml.stream.XMLStreamException;

public class AntLauncher {
   private static String XML_TASK_NAME = "antline_task";
   private static String XML_PROJECT_NAME = "antline_project";
   private static String XML_TARGET_NAME = "antline_target";
   private static final int ERR_BUFFER_SIZE = 2048;
   private Collection antOpts;
   private Class task;
   private String command;
   private boolean externalProcess = false;

   public AntLauncher(Class var1, Collection var2) {
      this.antOpts = var2;
      this.task = var1;
      if (AntTool.debug) {
         System.out.println("task = " + var1.getName());
      }

   }

   public void launch() throws BuildException, IOException {
      File var1 = this.generateBuildXml();
      if (this.externalProcess) {
         launchExternal(var1);
      } else {
         launchInternal(var1);
      }

   }

   private static void launchInternal(File var0) throws BuildException, IOException {
      ArrayList var1 = new ArrayList();
      boolean var2 = false;
      var1.add("-f");
      var1.add(var0.getCanonicalPath());
      var1.add("-logger");
      var1.add("weblogic.ant.taskdefs.antline.ToolLogger");
      if (AntTool.debug) {
         var1.add("-verbose");
      } else {
         var1.add("-quiet");
      }

      try {
         Main.start((String[])((String[])var1.toArray(new String[0])), (Properties)null, (ClassLoader)null);
      } finally {
         if (!AntTool.debug) {
            var0.delete();
         }

      }

   }

   private static void launchExternal(File var0) throws BuildException, IOException {
      ArrayList var1 = new ArrayList();
      boolean var2 = false;
      var1.add("java");
      var1.add("org.apache.tools.ant.Main");
      var1.add("-f");
      var1.add(var0.getCanonicalPath());
      var1.add("-logger");
      var1.add("weblogic.ant.taskdefs.antline.ToolLogger");
      if (AntTool.debug) {
         var1.add("-verbose");
      } else {
         var1.add("-quiet");
      }

      CharArrayWriter var3 = new CharArrayWriter(2048);
      CharArrayWriter var4 = new CharArrayWriter(2048);
      Executable var5 = new Executable();
      var5.setUseCharWriter(var4, var3);
      boolean var6 = var5.exec((String[])((String[])var1.toArray(new String[0])));
      if (!AntTool.debug) {
         var0.delete();
      }

      if (AntTool.debug) {
         System.out.println(var4.toString());
      }

      if (!var6) {
         throw new BuildException("Command failed. Error output was:\n" + var3.toString());
      }
   }

   private File generateBuildXml() throws IOException {
      String var1 = System.getProperty("java.io.tmpdir");
      if (var1 == null) {
         var1 = ".";
      }

      File var2 = new File(var1);
      var2.mkdirs();
      File var3 = File.createTempFile("antline", ".xml", var2);
      if (AntTool.debug) {
         System.out.println("tmpfile = " + var3);
      } else {
         var3.deleteOnExit();
      }

      XMLOutputStreamFactory var4 = XMLOutputStreamFactory.newInstance();
      XMLOutputStream var5 = var4.newOutputStream(new FileOutputStream(var3));
      var5.add(ElementFactory.createStartElement("project"));
      var5.add(ElementFactory.createAttribute("name", XML_PROJECT_NAME));
      var5.add(ElementFactory.createAttribute("default", XML_TARGET_NAME));
      var5.add(ElementFactory.createStartElement("taskdef"));
      var5.add(ElementFactory.createAttribute("name", XML_TASK_NAME));
      var5.add(ElementFactory.createAttribute("classname", this.task.getName()));
      var5.add(ElementFactory.createEndElement("taskdef"));
      var5.add(ElementFactory.createStartElement("target"));
      var5.add(ElementFactory.createAttribute("name", XML_TARGET_NAME));
      ParamNode var6 = this.buildParamTree(this.antOpts);
      var5 = this.emitXML(var6, var5);
      var5.add(ElementFactory.createEndElement("target"));
      var5.add(ElementFactory.createEndElement("project"));
      var5.flush();
      return var3;
   }

   private XMLOutputStream emitXML(ParamNode var1, XMLOutputStream var2) throws XMLStreamException {
      String var3 = var1.getName();
      var2.add(ElementFactory.createStartElement(var3));
      Collection var4 = var1.getAttributeChildren();
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         ParamNode var6 = (ParamNode)var5.next();
         AntOpt var7 = var6.getParam();
         if (var7.getValue() != null) {
            var2.add(ElementFactory.createAttribute(var7.getAntAttrName(), var7.getValue()));
         }
      }

      Collection var8 = var1.getElementChildren();
      Iterator var9 = var8.iterator();

      while(var9.hasNext()) {
         this.emitXML((ParamNode)var9.next(), var2);
      }

      var2.add(ElementFactory.createEndElement(var3));
      return var2;
   }

   private ParamNode buildParamTree(Collection var1) {
      ParamNode var2 = new ParamNode(XML_TASK_NAME);
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         AntOpt var4 = (AntOpt)var3.next();
         if (var4.getValue() != null) {
            this.addNode(var2, var4);
         }
      }

      return var2;
   }

   private void addNode(ParamNode var1, AntOpt var2) {
      String[] var3 = StringUtils.splitCompletely(var2.getAntElementPath(), "/");
      ParamNode var4 = var1;

      ParamNode var6;
      for(int var5 = 0; var5 < var3.length; ++var5) {
         var6 = var4.getChildByName(var3[var5]);
         if (var6 == null) {
            var6 = new ParamNode(var3[var5]);
            var4.addChild(var6);
         }

         var4 = var6;
      }

      String var7 = "@" + var2.getAntAttrName();
      if (var7 != null) {
         var6 = var4.getChildByName(var7);
         if (var6 == null) {
            var6 = new ParamNode(var7);
            var4.addChild(var6);
         }

         var4 = var6;
      }

      var4.setParam(var2);
   }

   public void setExternalProcess(boolean var1) {
      this.externalProcess = var1;
   }

   public boolean getExternalProcess() {
      return this.externalProcess;
   }

   private static class ParamNode {
      private String name;
      private AntOpt value;
      private List children = new ArrayList();

      public ParamNode(String var1) {
         this.name = var1;
         if (AntTool.debug) {
            System.out.println("ParamNode[" + System.identityHashCode(this) + "] = " + this.name);
         }

      }

      public String getName() {
         return this.name;
      }

      public AntOpt getParam() {
         return this.value;
      }

      public void setParam(AntOpt var1) {
         this.value = var1;
      }

      public void addChild(ParamNode var1) {
         if (AntTool.debug) {
            System.out.println("ParamNode[" + System.identityHashCode(this) + "].addChild(" + System.identityHashCode(var1));
         }

         this.children.add(var1);
      }

      public ParamNode getChildByName(String var1) {
         Iterator var2 = this.children.iterator();

         ParamNode var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (ParamNode)var2.next();
         } while(!var3.getName().equals(var1));

         return var3;
      }

      public Collection getAttributeChildren() {
         ArrayList var1 = new ArrayList();
         Iterator var2 = this.children.iterator();

         while(var2.hasNext()) {
            ParamNode var3 = (ParamNode)var2.next();
            if (var3.getName().startsWith("@")) {
               var1.add(var3);
            }
         }

         return var1;
      }

      public Collection getElementChildren() {
         ArrayList var1 = new ArrayList();
         Iterator var2 = this.children.iterator();

         while(var2.hasNext()) {
            ParamNode var3 = (ParamNode)var2.next();
            if (!var3.getName().startsWith("@")) {
               var1.add(var3);
            }
         }

         return var1;
      }
   }
}
