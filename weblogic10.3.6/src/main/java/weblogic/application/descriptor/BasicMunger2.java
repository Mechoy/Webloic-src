package weblogic.application.descriptor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import weblogic.descriptor.BeanCreationInterceptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorException;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.j2ee.descriptor.wl.VariableAssignmentBean;
import weblogic.j2ee.descriptor.wl.VariableBean;
import weblogic.j2ee.descriptor.wl.VariableDefinitionBean;
import weblogic.logging.Loggable;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public class BasicMunger2 extends DebugStreamReaderDelegate implements BeanCreationInterceptor {
   protected boolean debug;
   protected boolean merge;
   protected boolean dbgValue;
   protected static final XMLInputFactory xiFactory = XMLInputFactory.newInstance();
   private boolean hasDTD;
   private String dtdNamespaceURI;
   protected ReaderEvent2 root;
   protected ReaderEvent2 currentEvent;
   protected Stack helpers;
   protected Stack helperScopedNames;
   private InputStream in;
   private String absolutePath;
   protected String versionInfo;
   protected boolean disableReorder;
   private static final String TRUE_STR = "true";
   private static final String FALSE_STR = "false";
   HashMap valueTable;
   HashMap locationTable;
   HashMap symbolTable;

   public BasicMunger2(InputStream var1, String var2) throws XMLStreamException {
      super(xiFactory.createXMLStreamReader(var1));
      this.debug = Debug.getCategory("weblogic.descriptor.munger").isEnabled();
      this.merge = Debug.getCategory("weblogic.descriptor.merge").isEnabled();
      this.dbgValue = Debug.getCategory("weblogic.descriptor.valuetable").isEnabled();
      this.hasDTD = false;
      this.helpers = new Stack();
      this.helperScopedNames = new Stack();
      this.in = var1;
      this.absolutePath = var2;
      Location var3 = super.getLocation();
      this.root = new ReaderEvent2(7, new MyLocation(var3));
      this.currentEvent = this.root;
   }

   public BasicMunger2(InputStream var1, AbstractDescriptorLoader2 var2) throws XMLStreamException {
      this(var1, var2.getAbsolutePath());
      this.initValueTable(var2.getDeploymentPlan(), var2.getModuleName(), var2.getDocumentURI());
   }

   String getAbsolutePath() {
      return this.absolutePath;
   }

   public ReaderEvent2 getCurrentEvent() {
      return this.currentEvent;
   }

   public String getDtdNamespaceURI() {
      return null;
   }

   public boolean hasDTD() {
      return this.hasDTD;
   }

   public PlaybackReader getPlaybackReader() throws XMLStreamException {
      PlaybackReader var1 = new PlaybackReader(this.root, this.getAbsolutePath());
      if (this.hasDTD()) {
         var1.setDtdNamespaceURI(this.getDtdNamespaceURI());
      }

      return var1;
   }

   public void initDtdText(String var1) {
   }

   public void toXML(PrintStream var1) {
      this.root.toXML(var1);
   }

   public void logError(List var1) {
      if (!var1.isEmpty()) {
         J2EELogger.logDescriptorParseError(StackTraceUtils.throwable2StackTrace(new DescriptorException("VALIDATION PROBLEMS WERE FOUND FOR: " + this.getAbsolutePath(), var1)));
      }
   }

   protected void setNamespace(String var1, String var2, ReaderEvent2 var3) {
      ReaderEventInfo var4 = var3.getReaderEventInfo();
      if (var1 != null) {
         var4.setPrefix(var1);
      }

      var4.setNamespaceCount(1);
      var4.setNamespaceURI(var1, var2);
      Iterator var5 = var3.getChildren().iterator();

      while(var5.hasNext()) {
         ReaderEvent2 var6 = (ReaderEvent2)var5.next();
         ReaderEventInfo var7 = var6.getReaderEventInfo();
         if (var1 != null) {
            var7.setPrefix(var1);
         }

         var7.setNamespaceCount(1);
         var7.setNamespaceURI(var1, var2);
      }

   }

   public PlaybackReader merge(BasicMunger2 var1, DescriptorBean var2) throws XMLStreamException {
      String var3 = this.getPrefix();
      String var4 = this.getNamespaceURI();
      this.root.merge(var1.root, -1, false);
      this.root.orderChildren();
      this.setNamespace(var3, var4, this.root);
      return new PlaybackReader(this.root, this.getAbsolutePath());
   }

   private String checkNewBeanCreationPattern(String var1) {
      int var2 = var1.lastIndexOf("]");
      if (var2 == -1) {
         return var1;
      } else {
         int var3 = var1.lastIndexOf("[");
         int var4 = var1.lastIndexOf("/");
         String var5 = var1.substring(var4 + 1, var1.length());
         String var6 = var1.substring(var3 + 1, var2);
         return var6.indexOf(var5 + "=") != -1 ? var1.substring(0, var4 + 1) : var1;
      }
   }

   public PlaybackReader mergeDescriptorBeanWithPlan(DescriptorBean var1) throws XMLStreamException {
      ArrayList var2 = new ArrayList();
      if (this.valueTable == null) {
         return new PlaybackReader(this.root, this.getAbsolutePath());
      } else {
         Iterator var3 = this.valueTable.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            String var5 = (String)var4.getKey();
            VariableAssignment var6 = (VariableAssignment)var4.getValue();
            String var7 = var6.getName();
            int var8 = var6.getOperation();
            if (var7 != null) {
               String var9 = var5;
               var5 = this.checkNewBeanCreationPattern(var5);
               SchemaHelper var10 = this.root.findRootSchemaHelper();
               ReaderEvent2 var11 = new ReaderEvent2(7, (Location)this.getLocationTable().get(var9));
               ReaderEvent2 var12 = new ReaderEvent2(new StringBuffer(var5), var11, var10, var7, (Location)this.getLocationTable().get(var9));
               var11.getChildren().add(var12);
               var2.add(new ReaderEventOperation(var12, var8));
            }
         }

         SchemaHelper var13 = this.root.findRootSchemaHelper();
         Iterator var14 = var2.iterator();

         while(var14.hasNext()) {
            ReaderEventOperation var15 = (ReaderEventOperation)var14.next();
            ReaderEvent2 var16 = var15.getReaderEvent();
            int var17 = var15.getOperation();
            var16.validate(var13);
            if (this.merge) {
               System.out.println("\nBasicMunger: ReaderEvent to merge into root from plan: ");
               var16.toXML(System.out);
               System.out.println("----------- end ReaderEvent --------");
            }

            this.root.merge(var16.getParent(), var17, true);
            if (this.merge) {
               System.out.println("\nBasicMunger: root ReaderEvent after merge from plan: ");
               this.root.toXML(System.out);
               System.out.println("----------- end ReaderEvent --------");
            }
         }

         this.root.orderChildren();
         return new PlaybackReader(this.root, this.getAbsolutePath());
      }
   }

   private void dump(ReaderEvent2 var1) {
      System.out.println("****** root Element name " + var1.getElementName() + " size of children " + var1.getChildren().size());
      Iterator var2 = var1.getChildren().iterator();

      while(var2.hasNext()) {
         ReaderEvent2 var3 = (ReaderEvent2)var2.next();
         switch (var3.getEventType()) {
            case 1:
               System.out.println("*** child " + var3.getElementName() + " child.getXpath() " + var3.getXpath());
               this.dump(var3);
            case 5:
            case 11:
         }
      }

   }

   public PlaybackReader mergeDescriptorBeanWithPlan(DeploymentPlanBean var1, String var2, String var3) throws XMLStreamException {
      this.initValueTable(var1, var2, var3);
      return this.mergeDescriptorBeanWithPlan((DescriptorBean)null);
   }

   public DescriptorBean beanCreated(DescriptorBean var1, DescriptorBean var2) {
      SchemaHelper var3 = ((AbstractDescriptorBean)var1)._getSchemaHelper2();
      this.currentEvent.setSchemaHelper(var3);
      this.helpers.push(var3);
      this.helperScopedNames.push(this.getLocalName());
      if (this.debug) {
         System.out.println("   set schema helper for: " + this.currentEvent.getSchemaHelper());
      }

      return var1;
   }

   String getHelperScopedNames() {
      return (String)this.helperScopedNames.peek();
   }

   protected SchemaHelper getTopLevelSchemaHelper() {
      return null;
   }

   public void close() throws XMLStreamException {
      super.close();
      if (this.in != null) {
         try {
            this.in.close();
         } catch (Exception var2) {
         }
      }

      this.in = null;
   }

   public Location getLocation() {
      MyLocation var1 = new MyLocation(super.getLocation());
      if (this.debug) {
         System.out.println("->getLocation: " + var1);
      }

      return var1;
   }

   public int next() throws XMLStreamException {
      int var1 = super.next();
      if (this.debug) {
         System.out.println("->next = " + Utils.type2Str(var1, this));
      }

      switch (var1) {
         case 1:
            this._onStartElement();
            break;
         case 2:
            this._onEndElement();
         case 3:
         case 6:
         case 7:
         case 9:
         case 10:
         default:
            break;
         case 4:
            this._onCharacters();
            break;
         case 5:
            this._onComment();
            break;
         case 8:
            this._onEndDocument();
            break;
         case 11:
            this._onDTD();
            var1 = this.next();
      }

      return var1;
   }

   private void _onDTD() {
      this.hasDTD = true;
      this.versionInfo = "DTD";
      this.initDtdText(this.getText());
   }

   private void _onStartElement() {
      ReaderEvent2 var1 = new ReaderEvent2(this.getEventType(), this.getLocalName(), this.currentEvent, super.getLocation());
      String var2 = this.getCharacterEncodingScheme();
      if (var2 != null) {
         var1.getReaderEventInfo().setCharacterEncodingScheme(var2.toUpperCase(Locale.US));
      }

      int var5;
      if (this.getTopLevelSchemaHelper() != null) {
         if (var1.getElementName().equals(this.getTopLevelSchemaHelper().getRootElementName())) {
            var1.setSchemaHelper(this.getTopLevelSchemaHelper());
            this.currentEvent.addChild(var1);
         } else {
            ReaderEvent2 var3 = this.currentEvent;
            SchemaHelper var4 = this.currentEvent.helper;
            if (this.debug) {
               System.out.println("using parent [" + var3.getElementName() + "] to set helper for " + var1.getElementName() + " to " + var4);
            }

            try {
               var5 = var4.getPropertyIndex(var1.getElementName());
            } catch (MissingRootElementException var9) {
               Loggable var7 = MungerLogger.logMissingRootElementLoggable(this.getTopLevelSchemaHelper().getRootElementName(), this.getAbsolutePath());
               throw new MissingRootElementException(var7.getMessage());
            }

            if (this.debug) {
               System.out.println("helper.getPropertyIndex = " + var5 + " is bean? " + var4.isBean(var5));
            }

            if (var4.isBean(var5)) {
               var1.setSchemaHelper(var4.getSchemaHelper(var5));
            } else {
               var1.setSchemaHelper(var4);
            }

            if (var5 >= 0) {
               boolean var6 = true;
               if (!this.hasDTD() && this.disableReorder) {
                  var6 = false;
               }

               var3.adopt(var1, var4, var6);
            } else {
               var3.addChild(var1);
            }

            if (var4.isBean(var5)) {
               var1.setSchemaHelper(var4.getSchemaHelper(var5));
            }
         }
      } else {
         this.currentEvent.addChild(var1);
      }

      this.currentEvent = var1;
      ReaderEventInfo var10 = var1.getReaderEventInfo();
      if (this.getPrefix() != null) {
         var10.setPrefix(this.getPrefix());
      }

      int var11;
      if (this.getNamespaceCount() > 0) {
         var10.setNamespaceCount(this.getNamespaceCount());

         for(var11 = 0; var11 < this.getNamespaceCount(); ++var11) {
            var10.setNamespaceURI(this.getNamespacePrefix(var11), this.getNamespaceURI(var11));
         }
      }

      if (this.getAttributeCount() > 0) {
         var10.setAttributeCount(this.getAttributeCount());

         for(var11 = 0; var11 < this.getAttributeCount(); ++var11) {
            var10.setAttributeValue(this.getAttributeValue(var11), this.getAttributeNamespace(var11), this.getAttributeLocalName(var11));
         }
      }

      String var12 = var10.getElementName();
      if (this.getTopLevelSchemaHelper() != null && this.getTopLevelSchemaHelper().getRootElementName().equals(var12) && !this.hasDTD() && ("application".equals(var12) || "web-app".equals(var12) || "ejb-jar".equals(var12) || "application-client".equals(var12) || "webservices".equals(var12) || "persistence".equals(var12))) {
         var5 = var10.getAttributeCount();
         int var13 = 0;
         String var14 = null;

         for(boolean var8 = false; var13 < var5 && !var8; ++var13) {
            if ("version".equals(var10.getAttributeLocalName(var13))) {
               var8 = true;
               var14 = var10.getAttributeValue(var13);
               if (this.debug) {
                  System.out.println("version:  " + var14);
               }
            }
         }

         if (var14 == null) {
            MungerLogger.logMissingVersionAttribute(var12, this.getAbsolutePath());
         }
      }

      if (this.debug) {
         System.out.println("++ onStartElement: " + this.getLocalName());
      }

      if (!this.helpers.empty()) {
         this.currentEvent.setSchemaHelper((SchemaHelper)this.helpers.peek());
      }

   }

   private void _onCharacters() {
      String var1 = super.getText();
      char[] var2 = null;
      if (var1 != null) {
         if ("true".equalsIgnoreCase(var1.trim())) {
            var2 = var1.toLowerCase(Locale.US).toCharArray();
         } else if ("false".equalsIgnoreCase(var1.trim())) {
            var2 = var1.toLowerCase(Locale.US).toCharArray();
         } else {
            var2 = var1.toCharArray();
         }
      }

      this.currentEvent.getReaderEventInfo().appendCharacters(var2);
   }

   private void _onComment() {
      if (!this.isWhiteSpace()) {
         this.currentEvent.getReaderEventInfo().setComments(super.getText());
      }

   }

   private void _onEndElement() {
      if (this.debug) {
         System.out.println("-- onEndElement: local name = " + this.getLocalName());
         System.out.println("  currentEvent.getElementName : " + this.currentEvent.getReaderEventInfo().getElementName());
         System.out.println("  new scope: " + this.currentEvent.getParent());
      }

      if (!this.helperScopedNames.empty() && this.currentEvent.getReaderEventInfo().getElementName().equals(this.helperScopedNames.peek())) {
         this.helperScopedNames.pop();
         if (!this.helpers.empty()) {
            this.helpers.pop();
         }
      }

      this.currentEvent = this.currentEvent.getParent() == null ? this.currentEvent : this.currentEvent.getParent();
   }

   private void _onEndDocument() {
   }

   protected void orderChildren() {
      this.root.orderChildren();
   }

   private void initValueTable(final DeploymentPlanBean var1, String var2, String var3) {
      if (var1 != null && var2 != null && var3 != null) {
         boolean var4 = false;
         ModuleOverrideBean[] var5 = var1.getModuleOverrides();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (this.dbgValue) {
               System.out.println("initValueTable: mos[i].getModuleType() = " + var5[var6].getModuleType() + ",\n mos[i].getModuleName() = " + var5[var6].getModuleName());
            }

            if (var5[var6].getModuleName().equals(var2)) {
               ModuleDescriptorBean[] var7 = var5[var6].getModuleDescriptors();

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  if (var7[var8].getUri().equals(var3)) {
                     var4 = true;
                     VariableAssignmentBean[] var9 = var7[var8].getVariableAssignments();
                     if (this.dbgValue) {
                        System.out.println("initValueTable: vabs.length = " + var9.length);
                     }

                     for(int var10 = 0; var10 < var9.length; ++var10) {
                        if (this.dbgValue) {
                           System.out.println("initValueTable: " + var9[var10].getXpath() + ", " + var9[var10].getName());
                        }

                        VariableAssignment var11 = new VariableAssignment((String)this.getSymbolTable(var1).get(var9[var10].getName()), var9[var10].getOperation());
                        this.getValueTable().put(var9[var10].getXpath(), var11);
                        this.getLocationTable().put(var9[var10].getXpath(), new Location() {
                           public int getLineNumber() {
                              return 0;
                           }

                           public int getColumnNumber() {
                              return 0;
                           }

                           public int getCharacterOffset() {
                              return 0;
                           }

                           public String getPublicId() {
                              return var1.getConfigRoot() + "plan.xml";
                           }

                           public String getSystemId() {
                              return this.getPublicId();
                           }
                        });
                     }
                  }
               }

               if (this.dbgValue) {
                  System.out.println("\n");
               }
            }
         }

         if (var4) {
            J2EELogger.logValidPlanMerged(var2, var3);
            if (this.dbgValue) {
               try {
                  ((DescriptorBean)var1).getDescriptor().toXML(System.out);
               } catch (IOException var12) {
                  var12.printStackTrace();
               }
            }
         }

      }
   }

   private HashMap getValueTable() {
      if (this.valueTable == null) {
         this.valueTable = new LinkedHashMap();
      }

      return this.valueTable;
   }

   private HashMap getSymbolTable(DeploymentPlanBean var1) {
      if (this.symbolTable == null) {
         this.symbolTable = new HashMap();
         VariableDefinitionBean var2 = var1.getVariableDefinition();
         VariableBean[] var3 = var2.getVariables();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (this.dbgValue) {
               System.out.println("getSymbolTable: " + var3[var4].getName() + ", " + var3[var4].getValue());
            }

            this.symbolTable.put(var3[var4].getName(), var3[var4].getValue());
         }
      }

      return this.symbolTable;
   }

   private HashMap getLocationTable() {
      if (this.locationTable == null) {
         this.locationTable = new HashMap();
      }

      return this.locationTable;
   }

   String getVersionInfo() {
      return this.versionInfo;
   }

   public static void main(String[] var0) throws Exception {
      Location var1 = new Location() {
         public int getLineNumber() {
            return 0;
         }

         public int getColumnNumber() {
            return 0;
         }

         public int getCharacterOffset() {
            return 0;
         }

         public String getPublicId() {
            return "<no public id>";
         }

         public String getSystemId() {
            return "<no system id>";
         }
      };
      StringBuffer var2 = new StringBuffer("/weblogic-connector xmlns=\"http://www.bea.com/ns/weblogic/90\"/outbound-resource-adapter/connection-definition-group/[connection-factory-interface=\"javax.sql.DataSource\"]/connection-instance/jndi-name");
      System.out.print("\nxpath = " + var2 + "\nresults in:\n");
      UnsyncByteArrayOutputStream var3 = new UnsyncByteArrayOutputStream();
      (new ReaderEvent2(var2, new ReaderEvent2(7, var1), (SchemaHelper)null, "val1", var1)).toXML(new PrintStream(var3));
      byte[] var4 = var3.toRawBytes();
      Object var5 = null;
      String var6 = null;
      if (var0.length == 0) {
         var5 = new ByteArrayInputStream(var4);
         var6 = "local buff";
      } else {
         File var7 = new File(var0[0]);
         var5 = new FileInputStream(var7);
         var6 = var0[0];
      }

      System.out.println("stamp out munger...");
      System.out.flush();
      BasicMunger2 var8 = new BasicMunger2((InputStream)var5, var6);
      System.out.println("hand munger to descriptor manger...");
      System.out.flush();
      (new DescriptorManager()).createDescriptor(var8).toXML(System.out);
   }

   private static void usage() {
      System.err.println("usage: java weblogic.application.descriptor.BasicMunger2 <descriptor file name>");
      System.exit(0);
   }

   private class MyLocation implements Location {
      Location l;

      MyLocation(Location var2) {
         this.l = var2;
      }

      public int getLineNumber() {
         return this.l.getLineNumber();
      }

      public int getColumnNumber() {
         return this.l.getColumnNumber();
      }

      public int getCharacterOffset() {
         return this.l.getCharacterOffset();
      }

      public String getPublicId() {
         return BasicMunger2.this.getAbsolutePath() + ":" + this.l.getLineNumber() + ":" + this.l.getColumnNumber();
      }

      public String getSystemId() {
         return this.l.getSystemId();
      }
   }

   private static class ReaderEventOperation {
      ReaderEvent2 ev;
      int operation;

      ReaderEventOperation(ReaderEvent2 var1, int var2) {
         this.ev = var1;
         this.operation = var2;
      }

      ReaderEvent2 getReaderEvent() {
         return this.ev;
      }

      int getOperation() {
         return this.operation;
      }
   }
}
