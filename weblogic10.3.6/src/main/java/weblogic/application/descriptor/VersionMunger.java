package weblogic.application.descriptor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.utils.Debug;

public class VersionMunger extends BasicMunger2 {
   protected boolean debug;
   protected boolean dump;
   private Map elementNameChanges;
   protected Stack stack;
   protected String absolutePath;
   protected String schemaHelperClassName;
   protected boolean isOldSchema;
   protected boolean forceSkipParent;
   private String rootElementName;
   private String newNamespaceURI;
   ReaderEvent2 playback;
   protected ReaderEvent2 lastEvent;
   ReaderEvent2 anchorEvent;
   protected static final Continuation CONTINUE = new Continuation() {
      public int nextToken(int var1) {
         return var1;
      }
   };
   protected final Continuation USE_BUFFER;
   protected final Continuation SKIP;
   SchemaHelper schemaHelper;
   private static final String TRUE_STR = "true";
   private static final String FALSE_STR = "false";
   protected String tranformedNamespace;
   protected ReaderEvent2 skippedEvent;
   private static final int MAX_ELEMENTS = 10;

   public VersionMunger(InputStream var1, AbstractDescriptorLoader2 var2, String var3) throws XMLStreamException {
      this(var1, var2, var3, Collections.EMPTY_MAP, false);
   }

   public VersionMunger(InputStream var1, AbstractDescriptorLoader2 var2, String var3, boolean var4) throws XMLStreamException {
      this(var1, var2, var3, Collections.EMPTY_MAP, var4);
   }

   public VersionMunger(InputStream var1, AbstractDescriptorLoader2 var2, String var3, Map var4) throws XMLStreamException {
      this(var1, var2, var3, var4, false);
   }

   public VersionMunger(InputStream var1, AbstractDescriptorLoader2 var2, String var3, Map var4, boolean var5) throws XMLStreamException {
      this(var1, var2, var3, var4, var5, (String)null);
   }

   public VersionMunger(InputStream var1, AbstractDescriptorLoader2 var2, String var3, Map var4, String var5) throws XMLStreamException {
      this(var1, var2, var3, var4, false, var5);
   }

   public VersionMunger(InputStream var1, AbstractDescriptorLoader2 var2, String var3, String var4) throws XMLStreamException {
      this(var1, var2, var3, Collections.EMPTY_MAP, false, var4);
   }

   public VersionMunger(InputStream var1, AbstractDescriptorLoader2 var2, String var3, Map var4, boolean var5, String var6) throws XMLStreamException {
      super(var1, var2);
      this.debug = Debug.getCategory("weblogic.descriptor.versionmunger").isEnabled();
      this.dump = Debug.getCategory("weblogic.descriptor.versionmunger.dump").isEnabled();
      this.elementNameChanges = Collections.EMPTY_MAP;
      this.stack = new Stack();
      this.forceSkipParent = false;
      this.rootElementName = null;
      this.newNamespaceURI = null;
      this.playback = null;
      this.lastEvent = null;
      this.anchorEvent = null;
      this.USE_BUFFER = new Continuation() {
         public int nextToken(int var1) throws XMLStreamException {
            assert VersionMunger.this.anchorEvent != null;

            assert VersionMunger.this.playback != null;

            VersionMunger.this.playback.setParent(VersionMunger.this.anchorEvent);
            VersionMunger.this.playback.setSchemaHelper(VersionMunger.this.anchorEvent.helper);
            VersionMunger.this.anchorEvent = null;
            VersionMunger.this.playback = null;
            return var1;
         }
      };
      this.SKIP = new Continuation() {
         public int nextToken(int var1) throws XMLStreamException {
            if (var1 == 1) {
               VersionMunger.this.currentEvent.setDiscard();
            }

            return VersionMunger.this.next();
         }
      };
      this.schemaHelper = null;
      this.tranformedNamespace = null;
      this.skippedEvent = null;
      this.setElementNameChanges(var4);
      this.absolutePath = var2.getAbsolutePath();
      this.schemaHelperClassName = var3;
      this.disableReorder = var5;
      this.rootElementName = this.getTopLevelSchemaHelper().getRootElementName();
      this.newNamespaceURI = var6;
      this.initialize();
      this.init(var2);
   }

   protected void pushStartElement(String var1) {
      this.anchorEvent = this.lastEvent;
      this.queueEvent(1, var1);
   }

   protected void pushStartElementLastEvent(String var1) {
      this.anchorEvent = this.lastEvent;
      this.queueEvent(1, var1, this.lastEvent);
   }

   protected void pushCharacters(String var1) {
      this.pushCharacters(var1.toCharArray());
   }

   protected void pushStartElementWithStackAsParent(String var1) {
      this.anchorEvent = (ReaderEvent2)this.stack.peek();
      this.queueEvent(1, var1, this.anchorEvent);
   }

   protected void pushCharacters(char[] var1) {
      ReaderEvent2 var2 = (ReaderEvent2)this.stack.peek();
      var2.getReaderEventInfo().setCharacters(var1);
   }

   protected void pushEndElement(String var1) {
      if (this.anchorEvent == null) {
         this.anchorEvent = this.currentEvent;
      }

      if (!this.stack.empty()) {
         this.playback = (ReaderEvent2)this.stack.pop();
      }

      if (!this.helperScopedNames.empty() && this.playback.getReaderEventInfo().getElementName().equals(this.helperScopedNames.peek())) {
         this.helperScopedNames.pop();
         this.helpers.pop();
      }

   }

   protected boolean enableCallbacksOnSchema() {
      return false;
   }

   protected Continuation onStartElement(String var1) {
      return CONTINUE;
   }

   protected Continuation onCharacters(String var1) {
      return CONTINUE;
   }

   protected Continuation onEndElement(String var1) {
      return CONTINUE;
   }

   protected Continuation onEndDocument() {
      return CONTINUE;
   }

   public final int next() throws XMLStreamException {
      int var1 = this._next();
      if (this.debug) {
         Debug.say("** Returning..." + Utils.type2Str(var1, this));
      }

      return var1;
   }

   public Map getElementNameChanges() {
      return this.elementNameChanges;
   }

   public void setElementNameChanges(Map var1) {
      this.elementNameChanges = var1;
   }

   protected SchemaHelper getTopLevelSchemaHelper() {
      if (this.schemaHelper == null) {
         try {
            ClassLoader var1 = this.getClass().getClassLoader();
            this.schemaHelper = (SchemaHelper)Class.forName(this.schemaHelperClassName, false, var1).newInstance();
         } catch (Exception var2) {
            throw new AssertionError(var2);
         }
      }

      return this.schemaHelper;
   }

   public String getLocalName() {
      String var1 = super.getLocalName();
      if (this.hasDTD()) {
         String var2 = (String)this.getElementNameChanges().get(var1);
         return var2 != null ? var2 : var1;
      } else {
         return var1;
      }
   }

   public char[] getTextCharacters() {
      char[] var1 = super.getTextCharacters();
      if (this.hasDTD()) {
         String var2 = new String(var1);
         if ("true".equalsIgnoreCase(var2.trim())) {
            return var2.toLowerCase(Locale.US).toCharArray();
         }

         if ("false".equalsIgnoreCase(var2.trim())) {
            return var2.toLowerCase(Locale.US).toCharArray();
         }
      }

      return var1;
   }

   protected String getLatestSchemaVersion() {
      return null;
   }

   public String getDtdNamespaceURI() {
      return this.newNamespaceURI;
   }

   protected void transformOldSchema() {
      if (this.currentEvent.getElementName().equals(this.rootElementName)) {
         this.transformNamespace(this.newNamespaceURI, this.currentEvent);
      }

      this.tranformedNamespace = this.newNamespaceURI;
   }

   protected boolean isOldSchema() {
      return this.rootElementName != null && this.newNamespaceURI != null ? this.isOldNamespaceURI(this.getNamespaceURI()) : false;
   }

   protected boolean isOldNamespaceURI(String var1) {
      return var1 != null && (var1.equals("http://www.bea.com/ns/weblogic/90") || var1.equals("http://www.bea.com/ns/weblogic/10.0") || var1.equals("http://www.bea.com/ns/weblogic/10.0/persistence") || this.newNamespaceURI != null && !var1.equals(this.newNamespaceURI) && !var1.startsWith("http://www.w3.org/"));
   }

   private final int _next() throws XMLStreamException {
      this.lastEvent = this.currentEvent;
      if (this.lastEvent == this.skippedEvent) {
         this.lastEvent = this.lastEvent.getParent();
      }

      int var1 = super.next();
      if (!this.hasDTD()) {
         if (var1 == 1) {
            if (this.isOldSchema()) {
               this.transformOldSchema();
            } else {
               String var2 = this.getLatestSchemaVersion();
               if (var2 != null && this.versionInfo == null) {
                  this.versionInfo = var2;
               }
            }
         }

         if (!this.enableCallbacksOnSchema()) {
            return var1;
         }
      }

      Continuation var3;
      switch (var1) {
         case 1:
            var3 = this.onStartElement(this.getLocalName());
            if (var3 == this.SKIP) {
               this.skippedEvent = this.currentEvent;
            }
            break;
         case 2:
            var3 = this.onEndElement(this.getLocalName());
            break;
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            var3 = CONTINUE;
            break;
         case 4:
            var3 = this.onCharacters(this.getText());
            break;
         case 8:
            var3 = this.onEndDocument();
      }

      return var3.nextToken(var1);
   }

   public String getNamespaceURI() {
      if (this.debug) {
         System.out.println("->getNamespaceURI: usingDTD() =" + this.hasDTD());
      }

      if (this.tranformedNamespace != null) {
         return this.tranformedNamespace;
      } else {
         return this.hasDTD() ? this.getDtdNamespaceURI() : super.getNamespaceURI();
      }
   }

   public String getNamespaceURI(int var1) {
      String var2 = super.getNamespaceURI(var1);
      return this.newNamespaceURI != null && this.isOldNamespaceURI(var2) && !this.currentEvent.getReaderEventInfo().hasNamespaceURI(this.newNamespaceURI) ? this.newNamespaceURI : var2;
   }

   public String getNamespaceURI(String var1) {
      String var2 = super.getNamespaceURI(var1);
      return this.newNamespaceURI != null && this.isOldNamespaceURI(var2) && !this.currentEvent.getReaderEventInfo().hasNamespaceURI(this.newNamespaceURI) ? this.newNamespaceURI : var2;
   }

   protected void initialize() {
   }

   private void init(AbstractDescriptorLoader2 var1) throws XMLStreamException {
      this.consumeInputStream();
      if (!this.hasDTD() && !this.isOldSchema) {
         this.transformNamespace(this.getDtdNamespaceURI(), this.root);
      }

      if (this.dump) {
         this.root.toXML(System.out);
      }

   }

   protected void transformNamespace(String var1, ReaderEvent2 var2) {
      this.transformNamespace(var1, var2, (String)null);
   }

   protected void transformNamespace(String var1, ReaderEvent2 var2, String var3) {
      int var4 = 0;
      boolean var5 = false;
      ReaderEventInfo.Namespaces var6 = var2.getReaderEventInfo().getNamespaces();

      for(int var7 = 0; !var5 && var7 < var6.getNamespaceCount(); ++var7) {
         String var8 = var6.getNamespaceURI(var7);
         if (this.isOldNamespaceURI(var8) || var8.equals(var1) || var8.equals(var3)) {
            var4 = var7;
            var5 = true;
         }
      }

      String var9 = var2.getReaderEventInfo().getNamespaces().getNamespacePrefix(var4);
      var2.getReaderEventInfo().clearNamespaces();
      this.setNamespace(var9, var1, var2);
   }

   protected boolean lookForDTD(AbstractDescriptorLoader2 var1) throws XMLStreamException {
      InputStream var2 = null;

      try {
         var2 = var1.getInputStream();
      } catch (IOException var16) {
         return false;
      }

      if (var2 == null) {
         return false;
      } else {
         XMLStreamReader var3 = xiFactory.createXMLStreamReader(var2);

         try {
            for(int var4 = 0; var4 < 10 && var3.hasNext(); ++var4) {
               boolean var5;
               if (var3.isStartElement()) {
                  var5 = false;
                  return var5;
               }

               if (var3.next() == 11) {
                  var5 = true;
                  return var5;
               }
            }

            boolean var18 = false;
            return var18;
         } finally {
            try {
               var3.close();
               var2.close();
            } catch (IOException var15) {
            }

         }
      }
   }

   private void consumeInputStream() throws XMLStreamException {
      while(this.next() != 8) {
      }

   }

   void queueEvent(int var1, String var2) {
      this.queueEvent(var1, var2, this.currentEvent.getParent());
   }

   void queueEvent(int var1, String var2, ReaderEvent2 var3) {
      SchemaHelper var4 = null;
      if (var3 == this.skippedEvent) {
         var4 = var3.getSchemaHelper();
         var3 = var3.getParent();
      }

      ReaderEvent2 var5 = new ReaderEvent2(var1, var2, var3, this.currentEvent.getReaderEventInfo().getLocation());
      var3.getChildren().add(var5);
      ReaderEventInfo var6 = var5.getReaderEventInfo();
      ReaderEventInfo var7 = var3.getReaderEventInfo();
      if (var7.getPrefix() != null) {
         var6.setPrefix(var7.getPrefix());
      }

      int var8;
      if (var7.getNamespaceCount() > 0) {
         var6.setNamespaceCount(var7.getNamespaceCount());

         for(var8 = 0; var8 < var7.getNamespaceCount(); ++var8) {
            var6.setNamespaceURI(var7.getNamespacePrefix(var8), var7.getNamespaceURI(var8));
         }
      }

      if (var7.getAttributeCount() > 0 && !this.forceSkipParent) {
         var6.setAttributeCount(var7.getAttributeCount());

         for(var8 = 0; var8 < var7.getAttributeCount(); ++var8) {
            var6.setAttributeValue(var7.getAttributeValue(var8), var7.getAttributeNamespace(var8), var7.getAttributeLocalName(var8));
         }
      }

      if (!this.helpers.empty()) {
         var5.setSchemaHelper((SchemaHelper)this.helpers.peek());
      } else if (var4 != null) {
         var5.setSchemaHelper(var4);
      } else {
         var5.setSchemaHelper(var3.getSchemaHelper());
      }

      this.stack.push(var5);
   }

   public ReaderEvent2 getQueuedEvent(int var1, Object var2) {
      return null;
   }

   protected void pushReaderEvent(ReaderEvent2 var1) {
   }

   protected void pushReaderEvents(List var1) {
   }

   protected void replaceSlashWithPeriod(boolean var1) {
      boolean var2 = var1;
      boolean var3 = false;
      char[] var4 = this.currentEvent.getReaderEventInfo().getCharacters();
      if (var4 != null && var4.length > 0) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5] == '#') {
               var2 = false;
            }

            if (!var2 && var4[var5] == '/') {
               var4[var5] = '.';
               var3 = true;
            }
         }

         if (var3) {
            this.currentEvent.getReaderEventInfo().setCharacters(var4);
         }
      }

   }

   public static void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         usage();
         System.exit(-1);
      }

      String var1 = var0[0];
      String var2 = var0.length > 1 && var0[1].endsWith("plan.xml") ? var0[1] : null;
      File var3 = new File(var1);
      Object var4 = null;
      File var5 = new File(".");
      DeploymentPlanBean var6 = null;
      String var7 = var0.length > 2 ? var0[2] : null;
      final String var8 = var2 == null && var0.length > 1 ? var0[1] : null;
      final String var9 = var2 == null && var0.length > 2 ? var0[2] : null;
      AbstractDescriptorLoader2 var10;
      if (var2 != null) {
         if (var7 == null) {
            usage();
            System.exit(-1);
         }

         var10 = new AbstractDescriptorLoader2(new File(var2), var2) {
         };
         var6 = (DeploymentPlanBean)var10.loadDescriptorBean();
      }

      var10 = new AbstractDescriptorLoader2(var3, var5, var6, var7, var1) {
         protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
            return new VersionMunger(var1, this, var8) {
               private boolean inWeb;
               private boolean hasSetContextRoot;

               public String getDtdNamespaceURI() {
                  return var9 != null ? var9 : "http://java.sun.com/xml/ns/j2ee";
               }

               protected Continuation onStartElement(String var1) {
                  if (var1.equals("skip")) {
                     return this.SKIP;
                  } else {
                     if ("web".equals(var1)) {
                        this.inWeb = true;
                        this.hasSetContextRoot = false;
                     } else if ("context-root".equals(var1)) {
                        this.hasSetContextRoot = true;
                     }

                     return CONTINUE;
                  }
               }

               protected Continuation onEndElement(String var1) {
                  if (var1.equals("skip")) {
                     return this.SKIP;
                  } else {
                     if ("web".equals(var1)) {
                        assert this.inWeb;

                        this.inWeb = false;
                        if (!this.hasSetContextRoot) {
                           this.pushStartElement("context-root");
                           this.pushCharacters("CONTEXT_ROOT");
                           this.pushEndElement("context-root");
                           this.pushEndElement("web");
                           return this.USE_BUFFER;
                        }
                     }

                     return CONTINUE;
                  }
               }
            };
         }
      };
      System.out.println("stamp out version munger...");
      System.out.flush();
      DescriptorBean var11 = var10.loadDescriptorBean();
      Descriptor var12 = var11.getDescriptor();
      var12.toXML(System.out);
   }

   private static void usage() {
      System.out.print("java weblogic.application.descriptor.VersionMunger <dd-filename> <schema-helper-name> <name-space>|| <dd-filename> <plan-filename> <module-name> <schema-helper-name>");
   }

   protected interface Continuation {
      int nextToken(int var1) throws XMLStreamException;
   }
}
