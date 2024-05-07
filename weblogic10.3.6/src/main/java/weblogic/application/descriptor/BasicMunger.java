package weblogic.application.descriptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import weblogic.descriptor.BeanCreationInterceptor;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.DescriptorImpl;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.j2ee.descriptor.wl.VariableAssignmentBean;
import weblogic.j2ee.descriptor.wl.VariableBean;
import weblogic.j2ee.descriptor.wl.VariableDefinitionBean;
import weblogic.utils.Debug;

public class BasicMunger extends StreamReaderDelegate implements XMLStreamReader, Munger, BeanCreationInterceptor {
   protected boolean debug;
   protected boolean merge;
   private static final XMLInputFactory xiFactory = XMLInputFactory.newInstance();
   private AbstractDescriptorLoader loader;
   private DescriptorImpl descriptor;
   private boolean usingDTD;
   private boolean hasDTD;
   private String dtdNamespaceURI;
   protected boolean playbackToggle;
   protected boolean playback;
   protected boolean forceNoBaseStreamHasNext;
   ReaderEvent queuedEvent;
   protected ArrayList queuedEvents;
   protected ReaderEvent root;
   protected ReaderEvent top;
   protected Stack stack;
   protected Stack beans;
   HashMap valueTable;
   HashMap locationTable;
   HashMap symbolTable;
   Map parentToPath;
   Map pathToParent;
   public static final String NON_VALIDATING_PARSER = "weblogic.NonValidatingParser";
   private static final String TRUE_STR = "true";
   private static final String FALSE_STR = "false";
   public static final int OP_DEFAULT = -1;
   public static final int OP_ADD = 1;
   public static final int OP_REMOVE = 2;
   public static final int OP_REPLACE = 3;

   public BasicMunger(XMLStreamReader var1, AbstractDescriptorLoader var2) {
      super(var1);
      this.debug = Debug.getCategory("weblogic.descriptor").isEnabled();
      this.merge = Debug.getCategory("weblogic.merge").isEnabled();
      this.usingDTD = false;
      this.hasDTD = false;
      this.playbackToggle = false;
      this.playback = false;
      this.forceNoBaseStreamHasNext = false;
      this.queuedEvent = null;
      this.top = new ReaderEvent(7, (Object)null, (Location)null, this);
      this.stack = new Stack();
      this.beans = new Stack();
      this.parentToPath = new HashMap();
      this.pathToParent = new LinkedHashMap();
      this.loader = var2;
   }

   public BasicMunger(InputStream var1, AbstractDescriptorLoader var2) throws XMLStreamException {
      super(xiFactory.createXMLStreamReader(var1));
      this.debug = Debug.getCategory("weblogic.descriptor").isEnabled();
      this.merge = Debug.getCategory("weblogic.merge").isEnabled();
      this.usingDTD = false;
      this.hasDTD = false;
      this.playbackToggle = false;
      this.playback = false;
      this.forceNoBaseStreamHasNext = false;
      this.queuedEvent = null;
      this.top = new ReaderEvent(7, (Object)null, (Location)null, this);
      this.stack = new Stack();
      this.beans = new Stack();
      this.parentToPath = new HashMap();
      this.pathToParent = new LinkedHashMap();
      this.loader = var2;
   }

   public BasicMunger(XMLStreamReader var1, AbstractDescriptorLoader var2, DeploymentPlanBean var3, String var4, String var5, String var6) {
      this(var1, var2);
      this.initValueTable(var3, var4, var6);
   }

   public BasicMunger(InputStream var1, AbstractDescriptorLoader var2, DeploymentPlanBean var3, String var4, String var5, String var6) throws XMLStreamException {
      this(xiFactory.createXMLStreamReader(var1), var2);
      this.initValueTable(var3, var4, var6);
   }

   public Map getLocalNameMap() {
      return Collections.EMPTY_MAP;
   }

   public void setDtdNamespaceURI(String var1) {
      this.dtdNamespaceURI = var1;
   }

   public String getDtdNamespaceURI() {
      return this.dtdNamespaceURI;
   }

   public void initDtdText(String var1) {
   }

   public void toXML(PrintStream var1) {
      this.root.toXML(var1);
   }

   public void logError(List var1) {
      if (!var1.isEmpty()) {
         J2EELogger.logDescriptorParseError((new DescriptorException("VALIDATION PROBLEMS " + this.loader.getAbsolutePath(), var1)).getMessage());
      }
   }

   public boolean isValidationEnabled() {
      return !"true".equals(System.getProperty("weblogic.NonValidatingParser"));
   }

   public void merge(BasicMunger var1) {
      try {
         this.resetDescriptor(this.loader, this.loader.getDescriptor());
      } catch (IOException var3) {
         throw new AssertionError(var3);
      } catch (XMLStreamException var4) {
         throw new AssertionError(var4);
      }

      this.merge(var1.root, false);
   }

   public void merge(ReaderEvent var1, boolean var2) {
      this.parentToPath.clear();
      this.pathToParent.clear();
      this.merge(this.root, var1, var2);
   }

   public void merge(ReaderEvent var1, ReaderEvent var2, boolean var3) {
      this.initKeyedParentToPath(var1);
      this.initPathToParent(var2);
      if (this.debug || this.merge) {
         this.dumpStartMerge(var2);
      }

      this.replaceMatchingXpathsByKey(var1, var3);
      if (this.debug || this.merge) {
         System.out.println("--- after replaceMatchingXpathsByKey ----");
         this.dumpStartMerge(var2);
      }

      Iterator var4 = this.pathToParent.values().iterator();

      while(true) {
         while(var4.hasNext()) {
            ReaderEvent var5 = (ReaderEvent)var4.next();
            if (this.debug || this.merge) {
               System.out.println("--- call adoptUnmatchedKeyedReaderEvents with: " + var5.getPath());
            }

            if (var1.searchSubTree(var5)) {
               if (this.merge) {
                  System.out.println("--- adoptUnmatchedKeyedReaderEvents: source already exists in target.. skipping");
                  var1.toXML(System.out);
               }
            } else {
               ReaderEvent var6 = this.scanForSiblingReaderEvent(var1, var5);
               if (this.debug || this.merge) {
                  System.out.println("--- call adoptUnmatchedKeyedReaderEvents with: " + var5.getPath());
                  System.out.println("--- and: " + var6.getPath());
               }

               ReaderEvent var7 = var6.getParent();
               if (var3 && (var7 == null || var7.getEventType() == 7)) {
                  var7 = var6;
               }

               ReaderEvent var8;
               if (!var5.getLocalName().equals(var6.getLocalName())) {
                  while(var5.getParent().getLocalName() != null && !var5.getParent().getLocalName().equals(var6.getParent().getLocalName())) {
                     var8 = var5.getParent();
                     if (var8 == null || var8.getEventType() == 7 || var8.getLocalName() == null) {
                        break;
                     }

                     var5 = var8;
                  }
               }

               var8 = var5.getParent();
               var8.getChildren().remove(var5);
               if (this.debug || this.merge && var8 != null && var8.getChildren() != null) {
                  System.out.println(" zap old parent iff size == 0, " + var8.getChildren().size());
               }

               while(var8 != null && var8.getChildren() != null && var8.getChildren().size() == 0) {
                  var8.setDiscard();
                  Stack var9 = var8.getChildren();
                  if (var9 != null) {
                     var9.remove(var8);
                  }

                  var8 = var8.getParent();
                  if ((this.debug || this.merge) && var8 != null && var8.getChildren() != null) {
                     System.out.println(" zap old parent iff size == 0, " + var8.getChildren().size());
                  }
               }

               var7.getChildren().add(var5);
               var5.fixParents(var7);
            }
         }

         if (this.debug || this.merge) {
            System.out.println("--- after adoptUnmatchedKeyedReaderEvents ----");
            this.dumpStartMerge(var2);
         }

         if (var1.searchSubTree(var2)) {
            if (this.merge) {
               System.out.println("Skipping replaceMatchingXpaths, source already exists in target");
            }
         } else {
            this.parentToPath.clear();
            this.initParentToPath(var2);
            if (this.debug || this.merge) {
               System.out.println("before replaceMatchingXpaths:");
               System.out.println("target = ");
               var1.toXML(System.out);
               System.out.println("source = ");
               var2.toXML(System.out);
               this.dumpParent2Path();
               System.out.println("-------------");
            }

            this.replaceMatchingXpaths(var1, var3);
            if (this.debug || this.merge) {
               System.out.print("\n\nafter replaceMatchingXpaths: ");
               System.out.println("target = ");
               var1.toXML(System.out);
               System.out.println("source = ");
               var2.toXML(System.out);
            }
         }

         if (this.debug || this.merge) {
            System.out.print("\n\n\n... before adoptUnmatchedReaderEvents ");
            this.root.toXML(System.out);
         }

         this.adoptUnmatchedReaderEvents(var2);
         if (this.debug || this.merge) {
            System.out.print("\n\n\n... after adoptUnmatchedReaderEvents ");
            this.root.toXML(System.out);
         }

         return;
      }
   }

   DescriptorBean mergeDescriptorBeanWithPlan(AbstractDescriptorLoader var1) throws IOException {
      ArrayList var2 = new ArrayList();
      this.mergePlan(var2);
      this.setQueuedEvents(var2);
      Descriptor var3 = var1.getDescriptorManager().createDescriptor(this);

      assert var3 != null;

      return var3.getRootBean();
   }

   public void mergePlan(ArrayList var1) throws IOException {
      if (this.valueTable != null) {
         ArrayList var2 = new ArrayList();
         Iterator var3 = this.valueTable.entrySet().iterator();

         while(true) {
            String var5;
            String var7;
            int var8;
            do {
               if (!var3.hasNext()) {
                  ReaderEvent var9;
                  for(var3 = var2.iterator(); var3.hasNext(); this.merge(var9, true)) {
                     var9 = (ReaderEvent)var3.next();
                     AbstractDescriptorBean var10 = (AbstractDescriptorBean)this.descriptor.getRootBean();
                     SchemaHelper var11 = var10._getSchemaHelper2();
                     var9.validate(var11);
                     if (this.debug || this.merge) {
                        System.out.println("\n\nBasicMunger: ReaderEvent to merge into plan: ");
                        var9.toXML(System.out);
                        System.out.println("----------- end ReaderEvent --------");
                     }
                  }

                  if (this.debug || this.merge) {
                     System.out.println("\n\nBasicMunger: Current descriptor after subtree has been merged: ");
                     this.root.toXML(System.out);
                  }

                  this.root.toQueuedEvents(var1);
                  return;
               }

               Map.Entry var4 = (Map.Entry)var3.next();
               var5 = (String)var4.getKey();
               VariableAssignment var6 = (VariableAssignment)var4.getValue();
               var7 = var6.getName();
               var8 = var6.getOperation();
            } while(var7 == null);

            if (this.debug || this.merge) {
               System.out.println("BasicMunger: xpath = " + var5 + ", val = " + var7);
            }

            var2.add(new ReaderEvent(new StringBuffer(var5), new ReaderEvent(7, (Object)null, (Location)null, this), var7, var8, this, (Location)this.getLocationTable().get(var5)));
         }
      }
   }

   public AbstractDescriptorBean getCurrentOrEventBean(ReaderEvent var1) {
      ReaderEvent var2 = this.getCurrentBeanEvent();
      if (var2 == null) {
         var2 = var1.getBeanEvent();
      }

      return var2 == null ? null : var2.getBean();
   }

   public ReaderEvent getParentReaderEvent(ReaderEvent var1) {
      AbstractDescriptorBean var2 = this.getCurrentOrEventBean(var1);
      return var2 == null ? var1.getParentReaderEvent() : (ReaderEvent)var2._getParentReaderEvent(var1);
   }

   public DescriptorBean beanCreated(DescriptorBean var1, DescriptorBean var2) {
      if (var2 == null) {
         this.descriptor = (DescriptorImpl)var1.getDescriptor();
      }

      if (this.top != null) {
         this.top.setBean((AbstractDescriptorBean)var1);
         ((AbstractDescriptorBean)var1)._setElementName(this.top.getElementName());
         this.beans.push(this.top);
      }

      return var1;
   }

   public String getLocalName() {
      if (this.playback) {
         return this.queuedEvent.getEventType() == 4 ? new String((char[])this.queuedEvent.getCharacters()) : this.queuedEvent.getLocalName();
      } else {
         String var1 = super.getLocalName();
         if (var1 == null) {
            return null;
         } else if (this.usingDTD) {
            String var2 = (String)this.getLocalNameMap().get(var1);
            return var2 != null ? var2 : var1;
         } else {
            return var1;
         }
      }
   }

   public char[] getTextCharacters() {
      if (this.playback && this.queuedEvent != null) {
         return (char[])this.queuedEvent.getCharacters();
      } else {
         char[] var1 = super.getTextCharacters();
         if (this.top != null) {
            this.top.setCharacters(var1);
         }

         if (!this.usingDTD) {
            return var1;
         } else {
            String var2 = new String(var1);
            if ("true".equalsIgnoreCase(var2.trim())) {
               return var2.toLowerCase(Locale.US).toCharArray();
            } else {
               return "false".equalsIgnoreCase(var2.trim()) ? var2.toLowerCase(Locale.US).toCharArray() : var1;
            }
         }
      }
   }

   protected void setPlayback(boolean var1) {
      if (var1 != this.playback) {
         if (var1) {
            if (this.queuedEvents.size() <= 0) {
               return;
            }

            if (this.forceNoBaseStreamHasNext) {
               this.queuedEvent = (ReaderEvent)this.queuedEvents.remove(0);
               this.playback = true;
               return;
            }

            this.playbackToggle = true;
         } else {
            this.playbackToggle = true;
         }

      }
   }

   public int next() throws XMLStreamException {
      if (this.playbackToggle) {
         this.playback = !this.playback;
         this.playbackToggle = false;
      }

      int var1;
      if (this.playback) {
         if (this.queuedEvents.size() > 0) {
            this.queuedEvent = (ReaderEvent)this.queuedEvents.remove(0);
            if (this.debug) {
               System.out.println("-> next: play = " + this.type2Str(this.queuedEvent.getEventType()) + " event queue size = " + this.queuedEvents.size());
            }

            switch (this.queuedEvent.getEventType()) {
               case 1:
                  this.push();
                  break;
               case 2:
                  this.pop();
               case 3:
               case 5:
               case 6:
               default:
                  break;
               case 4:
                  new String(this.getTextCharacters());
                  if (this.top != null) {
                     this.top.setCharacters((char[])this.queuedEvent.getCharacters());
                  }
                  break;
               case 7:
                  this.push();
                  break;
               case 8:
                  this.push();
            }

            var1 = this.queuedEvent.getEventType();
            return var1;
         }

         if (this.debug) {
            System.out.println("playback played out, delegate to reader...");
         }
      }

      this.playback = false;
      this.queuedEvent = null;
      var1 = super.next();
      if (this.debug) {
         System.out.println("->next = " + this.type2Str(var1));
      }

      ReaderEvent var2;
      switch (var1) {
         case 1:
            this.push();
            break;
         case 2:
            var2 = this.getCurrentBeanEvent();
            if (var2 != null && var2.getBean() != null && this.getLocalName().equals(var2.getBean()._getElementName())) {
               this.beans.pop();
            }

            this.pop();
         case 3:
         case 5:
         case 6:
         case 9:
         case 10:
         default:
            break;
         case 4:
            this.getTextCharacters();
            break;
         case 7:
            this.push();
            break;
         case 8:
            this.push();
            var2 = this.getCurrentBeanEvent();
            if (var2 != null && var2.getBean() != null) {
               this.beans.pop();
            }

            this.pop();
            break;
         case 11:
            this.hasDTD = true;
            this.usingDTD = true;
            this.initDtdText(this.getText());
            return this.next();
      }

      return var1;
   }

   private ReaderEvent getCurrentBeanEvent() {
      return this.beans.empty() ? null : (ReaderEvent)this.beans.peek();
   }

   public void push() {
      this.stack.push(this.top);
      Location var1 = this.getLocation();
      ReaderEvent var2 = new ReaderEvent(this.getEventType() == 8 ? null : this.getLocalName(), this.top, this, var1, this.getCurrentBeanEvent());
      if (this.top.getEventType() == 7) {
         this.root = var2;
      }

      this.top = var2;
      ReaderEvent var3 = this.getCurrentBeanEvent();
      if (var3 != null) {
         this.top.setBean(var3.getBean());
      }

      if (this.getEventType() == 1 && this.getAttributeCount() > 0) {
         var2.setAttributeCount(this.getAttributeCount());

         for(int var4 = 0; var4 < this.getAttributeCount(); ++var4) {
            var2.setAttributeValue(this.getAttributeValue(var4), var4);
         }
      }

   }

   public void pop() {
      if (this.stack.empty()) {
         if (this.debug) {
            System.out.println("\n\nStack is empty!!!!!");
         }

      } else {
         ReaderEvent var1 = (ReaderEvent)this.stack.pop();
         if (var1 != null) {
            var1.getChildren().push(this.top);
            this.top = var1;
         }

      }
   }

   public void setBean(AbstractDescriptorBean var1) {
      if (this.top != null) {
         ReaderEvent var2 = (ReaderEvent)this.top.getChildren().lastElement();
         var2.setBean(var1);
      }

   }

   public int skip(int var1) throws XMLStreamException {
      switch (var1) {
         case 1:
            this.top.setDiscard();
         case 2:
         default:
            if (this.debug) {
               System.out.println("skipped...");
            }

            return this.next();
      }
   }

   public String getNamespaceURI() {
      if (this.debug) {
         System.out.println("->getNamespaceURI: usingDTD() =" + this.usingDTD());
      }

      return this.usingDTD() ? this.getDtdNamespaceURI() : super.getNamespaceURI();
   }

   public boolean usingDTD() {
      return this.usingDTD;
   }

   public boolean hasDTD() {
      return this.hasDTD || super.getNamespaceURI() == null;
   }

   public void setParent(XMLStreamReader var1) {
      if (this.debug) {
         System.out.println("->setParent");
      }

      super.setParent(var1);
   }

   public XMLStreamReader getParent() {
      if (this.debug) {
         System.out.println("->getParent");
      }

      return super.getParent();
   }

   public int nextTag() throws XMLStreamException {
      if (this.debug) {
         System.out.println("->nextTag");
      }

      return this.playback ? this.next() : super.nextTag();
   }

   public String getElementText() throws XMLStreamException {
      if (this.debug) {
         System.out.println("->getElementText");
      }

      return super.getElementText();
   }

   public void require(int var1, String var2, String var3) throws XMLStreamException {
      if (this.debug) {
         System.out.println("->require");
      }

      super.require(var1, var2, var3);
   }

   public void setForceNoBaseStreamHasNext(boolean var1) {
      this.forceNoBaseStreamHasNext = var1;
   }

   public boolean hasNext() throws XMLStreamException {
      boolean var2 = this.playbackToggle ? !this.playback : this.playback;
      boolean var1;
      if (var2 && this.queuedEvents.size() > 0) {
         var1 = true;
      } else if (this.forceNoBaseStreamHasNext) {
         var1 = false;
      } else {
         var1 = super.hasNext();
      }

      if (this.debug) {
         System.out.println("->hasNext: = " + var1 + ", playback = " + this.playback);
      }

      return var1;
   }

   public void close() throws XMLStreamException {
      if (this.debug) {
         System.out.println("->close");
      }

      super.close();
   }

   public String getNamespaceURI(String var1) {
      if (this.debug) {
         System.out.println("->getNamespaceURI(String)");
      }

      return super.getNamespaceURI(var1);
   }

   public NamespaceContext getNamespaceContext() {
      if (this.debug) {
         System.out.println("->getNamespaceContext");
      }

      return super.getNamespaceContext();
   }

   public boolean isStartElement() {
      boolean var1 = this.playback ? this.queuedEvent.getEventType() == 1 : super.isStartElement();
      if (this.debug) {
         System.out.println("->isStartElement: " + var1);
      }

      return var1;
   }

   public boolean isEndElement() {
      if (this.debug) {
         System.out.println("->isEndElement playback=" + this.playback);
      }

      if (this.playback) {
         return this.queuedEvent.getEventType() == 2;
      } else {
         return super.isEndElement();
      }
   }

   public boolean isCharacters() {
      if (this.debug) {
         System.out.println("->isCharacters");
      }

      if (this.playback) {
         return this.queuedEvent.getEventType() == 4;
      } else {
         return super.isCharacters();
      }
   }

   private static boolean isSpace(char var0) {
      return var0 == ' ' || var0 == '\t' || var0 == '\n' || var0 == '\r';
   }

   public boolean isWhiteSpace() {
      if (this.debug) {
         System.out.println("->isWhiteSpace");
      }

      if (this.playback) {
         if (this.queuedEvent.getEventType() == 4) {
            char[] var1 = (char[])this.queuedEvent.getCharacters();

            for(int var2 = 0; var2 < var1.length; ++var2) {
               if (!isSpace(var1[var2])) {
                  return false;
               }
            }

            return true;
         } else {
            throw new IllegalStateException("isWhiteSpace on type " + this.type2Str(this.queuedEvent.getEventType()));
         }
      } else {
         return super.isWhiteSpace();
      }
   }

   public String getAttributeValue(String var1, String var2) {
      String var3 = this.playback ? this.queuedEvent.getAttributeValue(var1, var2) : super.getAttributeValue(var1, var2);
      if (!this.playback && this.top != null) {
         this.top.setAttributeValue(var3, var1, var2);
      }

      if (this.debug) {
         System.out.println("->getAttributeValue(" + var1 + ", " + var2 + ") returns: " + var3);
      }

      return var3;
   }

   public int getAttributeCount() {
      int var1 = this.playback ? this.queuedEvent.getAttributeCount() : super.getAttributeCount();
      if (this.debug) {
         System.out.println("->getAttributeCount() returns " + var1);
      }

      if (!this.playback && this.top != null) {
         this.top.setAttributeCount(var1);
      }

      return var1;
   }

   public QName getAttributeName(int var1) {
      QName var2 = this.playback ? this.queuedEvent.getAttributeName(var1) : super.getAttributeName(var1);
      if (this.debug) {
         System.out.println("->getAttributeName(" + var1 + ") returns: " + var2);
      }

      return var2;
   }

   public String getAttributePrefix(int var1) {
      String var2 = this.playback ? this.queuedEvent.getAttributePrefix(var1) : super.getAttributePrefix(var1);
      if (!this.playback && this.top != null) {
         this.top.setAttributePrefix(var2, var1);
      }

      if (this.debug) {
         System.out.println("->getAttributePrefix(" + var1 + ") return " + var2);
      }

      return var2;
   }

   public String getAttributeNamespace(int var1) {
      String var2 = this.playback ? this.queuedEvent.getAttributeNamespace(var1) : super.getAttributeNamespace(var1);
      if (!this.playback && this.top != null) {
         this.top.setAttributeNamespace(var2, var1);
      }

      if (this.debug) {
         System.out.println("->getAttributeNamespace(" + var1 + ") returns " + var2);
      }

      return var2;
   }

   public String getAttributeLocalName(int var1) {
      String var2 = this.playback ? this.queuedEvent.getAttributeLocalName(var1) : super.getAttributeLocalName(var1);
      if (!this.playback && this.top != null) {
         this.top.setAttributeLocalName(var2, var1);
      }

      if (this.debug) {
         System.out.println("->getAttributeLocalName(" + var1 + ") returns " + var2);
      }

      return var2;
   }

   public String getAttributeType(int var1) {
      if (this.debug) {
         System.out.println("->getAttributeType returns CDATA");
      }

      return "CDATA";
   }

   public String getAttributeValue(int var1) {
      String var2 = this.playback ? this.queuedEvent.getAttributeValue(var1) : super.getAttributeValue(var1);
      if (!this.playback && this.top != null) {
         this.top.setAttributeValue(var2, var1);
      }

      if (this.debug) {
         System.out.println("->getAttributeValue(" + var1 + ") returns: " + var2);
      }

      return var2;
   }

   public boolean isAttributeSpecified(int var1) {
      boolean var2 = this.playback ? this.queuedEvent.isAttributeSpecified(var1) : super.isAttributeSpecified(var1);
      if (this.debug) {
         System.out.println("->isAttributeSpecified(" + var1 + ") returns " + var2);
      }

      return var2;
   }

   public int getNamespaceCount() {
      int var1 = this.playback ? this.queuedEvent.getNamespaceCount() : super.getNamespaceCount();
      if (!this.playback && this.top != null) {
         this.top.setNamespaceCount(var1);
      }

      if (this.debug) {
         System.out.println("->getNamespaceCount return " + var1);
      }

      return var1;
   }

   public String getNamespacePrefix(int var1) {
      String var2 = this.playback ? null : super.getNamespacePrefix(var1);
      if (this.debug) {
         System.out.println("->getNamespacePrefix(" + var1 + ") return " + var2);
      }

      return var2;
   }

   public String getNamespaceURI(int var1) {
      if (this.debug) {
         System.out.println("->getNamespaceURI(int)");
      }

      return super.getNamespaceURI(var1);
   }

   public int getEventType() {
      int var1 = this.playback ? this.queuedEvent.getEventType() : super.getEventType();
      if (this.debug) {
         System.out.println("->getEventType: " + this.type2Str(var1));
      }

      return var1;
   }

   public String getText() {
      if (this.debug) {
         System.out.println("->getText");
      }

      return this.playback ? new String(this.getTextCharacters()) : super.getText();
   }

   public int getTextCharacters(int var1, char[] var2, int var3, int var4) throws XMLStreamException {
      throw new UnsupportedOperationException();
   }

   public int getTextStart() {
      if (this.debug) {
         System.out.println("->getTextStart");
      }

      return super.getTextStart();
   }

   public int getTextLength() {
      if (this.debug) {
         System.out.println("->getTextLength playback=" + this.playback + " queuedEvent=" + this.queuedEvent);
      }

      return this.getTextCharacters().length;
   }

   public String getEncoding() {
      if (this.debug) {
         System.out.println("->getEncoding");
      }

      return super.getEncoding();
   }

   public boolean hasText() {
      if (this.debug) {
         System.out.println("->hasText");
      }

      if (this.playback) {
         return this.queuedEvent.getEventType() == 4 || this.queuedEvent.getEventType() == 6 || this.queuedEvent.getEventType() == 11 || this.queuedEvent.getEventType() == 9 || this.queuedEvent.getEventType() == 5;
      } else {
         return super.hasText();
      }
   }

   public Location getLocation() {
      Object var1 = this.getEventType() == 8 ? this.top.getLocation() : (this.playback ? this.top.getLocation() : new MyLocation(super.getLocation()));
      if (!this.playback && this.top != null) {
         this.top.setLocation((Location)var1);
      }

      if (this.debug) {
         System.out.println("->getLocation: " + var1);
      }

      return (Location)var1;
   }

   public QName getName() {
      if (this.debug) {
         System.out.println("->getName");
      }

      return this.playback ? new QName(this.getNamespaceURI(), this.getLocalName()) : super.getName();
   }

   public boolean hasName() {
      if (this.debug) {
         System.out.println("->hasName");
      }

      if (!this.playback) {
         return super.hasName();
      } else {
         return this.queuedEvent.getEventType() == 1 || this.queuedEvent.getEventType() == 2;
      }
   }

   public String getPrefix() {
      if (this.debug) {
         System.out.println("->getPrefix");
      }

      return super.getPrefix();
   }

   public String getVersion() {
      if (this.debug) {
         System.out.println("->getVersion");
      }

      return super.getVersion();
   }

   public boolean isStandalone() {
      if (this.debug) {
         System.out.println("->isStandalone");
      }

      return super.isStandalone();
   }

   public boolean standaloneSet() {
      if (this.debug) {
         System.out.println("->standaloneSet");
      }

      return super.standaloneSet();
   }

   public String getCharacterEncodingScheme() {
      if (this.debug) {
         System.out.println("->getCharacterEncodingScheme");
      }

      return super.getCharacterEncodingScheme();
   }

   public String getPITarget() {
      if (this.debug) {
         System.out.println("->getPITarget");
      }

      return super.getPITarget();
   }

   public String getPIData() {
      if (this.debug) {
         System.out.println("->getPIData");
      }

      return super.getPIData();
   }

   public Object getProperty(String var1) {
      if (this.debug) {
         System.out.println("->getProperty");
      }

      return super.getProperty(var1);
   }

   private void initValueTable(final DeploymentPlanBean var1, String var2, String var3) {
      if (var1 != null && var2 != null && var3 != null) {
         boolean var4 = false;
         ModuleOverrideBean[] var5 = var1.getModuleOverrides();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (this.debug) {
               System.out.println("initValueTable: mos[i].getModuleType() = " + var5[var6].getModuleType() + ",\n mos[i].getModuleName() = " + var5[var6].getModuleName());
            }

            if (var5[var6].getModuleName().equals(var2)) {
               ModuleDescriptorBean[] var7 = var5[var6].getModuleDescriptors();

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  if (var7[var8].getUri().equals(var3)) {
                     var4 = true;
                     VariableAssignmentBean[] var9 = var7[var8].getVariableAssignments();
                     if (this.debug) {
                        System.out.println("initValueTable: vabs.length = " + var9.length);
                     }

                     for(int var10 = 0; var10 < var9.length; ++var10) {
                        if (this.debug) {
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

               if (this.debug) {
                  System.out.println("\n");
               }
            }
         }

         if (var4) {
            J2EELogger.logValidPlanMerged(var2, var3);
            if (this.debug) {
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
            if (this.debug) {
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

   private void initKeyedParentToPath(ReaderEvent var1) {
      Iterator var2 = var1.getChildren().iterator();

      while(true) {
         ReaderEvent var3;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (ReaderEvent)var2.next();
         } while(var3.isDiscarded());

         if (var3.isKey() || var3.isKeyComponent()) {
            if (var3.getParent().getParentReaderEvent() != null && var3.getAttributeCount() != 1) {
               this.parentToPath.put(var3.getParent(), var3.getPath());
            } else {
               this.parentToPath.put(var3, var3.getPath());
            }
         }

         if (var3.getChildren() != null) {
            this.initKeyedParentToPath(var3);
         }
      }
   }

   private void initParentToPath(ReaderEvent var1) {
      Iterator var2 = var1.getChildren().iterator();

      while(var2.hasNext()) {
         ReaderEvent var3 = (ReaderEvent)var2.next();
         if (!var3.isDiscarded()) {
            if (var3.getParent().getParentReaderEvent() == null) {
               this.parentToPath.put(var3, var3.getPath());
            } else {
               this.parentToPath.put(var3.getParent(), var3.getPath());
            }

            if (var3.getChildren() != null) {
               this.initParentToPath(var3);
            }
         }
      }

   }

   private void initPathToParent(ReaderEvent var1) {
      Iterator var2 = var1.getChildren().iterator();

      while(true) {
         ReaderEvent var3;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (ReaderEvent)var2.next();
         } while(var3.isDiscarded());

         if (var3.isKey() || var3.isKeyComponent()) {
            if (var3.isKeyAnAttribute()) {
               this.pathToParent.put(var3.getPath(), var3);
            } else if (var3.getParent().getParentReaderEvent() == null) {
               this.pathToParent.put(var3.getPath(), var3);
            } else {
               this.pathToParent.put(var3.getPath(), var3.getParent());
            }
         }

         if (var3.getChildren() != null) {
            this.initPathToParent(var3);
         }
      }
   }

   private void replaceMatchingXpathsByKey(ReaderEvent var1, boolean var2) {
      Iterator var3 = ((Collection)var1.getChildren().clone()).iterator();

      while(true) {
         while(true) {
            ReaderEvent var4;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               var4 = (ReaderEvent)var3.next();
            } while(var4.isDiscarded());

            String var5 = (String)this.parentToPath.get(var4);
            if (this.debug || this.merge) {
               System.out.println("targetChild = " + var4 + ", path = " + var4.getPath() + ", paretnToPath() = " + var5);
            }

            if (var5 != null) {
               ReaderEvent var6 = (ReaderEvent)this.pathToParent.get(var5);
               if (var6 != null && var4.hasBeanCompositeKey() && !ReaderEvent.compareKeys(var4.getBeanCompositeKey(), var6.getBeanCompositeKey(var4.getBean()._getSchemaHelper2()))) {
                  continue;
               }

               var6 = (ReaderEvent)this.pathToParent.remove(var5);
               if (var6 == null || var6.isDiscarded()) {
                  continue;
               }

               if (this.debug || this.merge) {
                  System.out.println("replaceMatchingXpathsByKey.path = " + var5);
                  System.out.println("source = " + var6);
               }

               if (var6 != null) {
                  if (this.debug || this.merge) {
                     System.out.println("replaceMatchingXpathsByKey.source = " + var6);
                     System.out.println("replaceMatchingXpathsByKey.source.getParent() = " + var6.getParent().getPath());
                  }

                  var6.getParent().getChildren().remove(var6);
                  if (var6.getParent().getChildren().empty()) {
                     for(ReaderEvent var7 = var6; var7 != null && var7.getParent() != null && var7.getParent().getChildren() != null && var7.getParent().getChildren().empty(); var7 = var7.getParent()) {
                        var7.getParent().setDiscard();
                     }
                  }

                  if (this.debug || this.merge) {
                     System.out.println("replaceMatchingXpathsByKey.targetChild.getParent() = " + var4.getParent().getPath());
                     System.out.println("replaceMatchingXpathsByKey.targetChild() = " + var4.getPath());
                     System.out.println("before...");
                     Iterator var8 = var4.getChildren().iterator();

                     while(var8.hasNext()) {
                        System.out.println("targetChild.child = " + ((ReaderEvent)var8.next()).getPath());
                     }
                  }

                  if (var2) {
                     this.mergeSiblings(var6, var4, var2);
                  }

                  var6.fixParents(var4.getParent());
                  if (var6.getBean() == null) {
                     var6.setBean(var4.getBean());
                  }

                  var1.getChildren().set(var1.getChildren().indexOf(var4), var6);
                  if (!var2) {
                     var4.discard();
                  }
                  continue;
               }
            }

            if (var4.getChildren() != null && var4.getChildren().size() > 0) {
               this.replaceMatchingXpathsByKey(var4, var2);
            }
         }
      }
   }

   private boolean mergeSiblings(ReaderEvent var1, ReaderEvent var2, boolean var3) {
      if (this.merge) {
         System.out.println("mergeSiblings(begin)... source");
         var1.toXML(System.out);
         System.out.println("mergeSiblings(begin)... targetChild");
         var2.toXML(System.out);
      }

      if (var1.getBean() == null) {
         var1.setBean(var2.getBean());
      }

      SchemaHelper var4 = var2.getBean() == null ? var2.getSchemaHelper() : var2.getBean()._getSchemaHelper2();
      HashMap var5 = new HashMap();

      ReaderEvent var7;
      for(Iterator var6 = ((Collection)var1.getChildren().clone()).iterator(); var6.hasNext(); var5.put(var7.getLocalName(), var7)) {
         var7 = (ReaderEvent)var6.next();
         int var8 = var7.getOperation();
         if (var8 == 3 || var8 == 2) {
            var2.removeNamedChildren(var7.getLocalName());
         }

         if (var8 == 2) {
            var1.getChildren().remove(var7);
         }

         if (var8 == 3 && var7.isBeanKey(var4) && !var7.isKey() && var5.containsKey(var7.getLocalName())) {
            var1.getChildren().remove(var5.get(var7.getLocalName()));
            var7.setOperation(-1);
         }

         if (var7.isBeanKey(var4) && (!var3 || var8 != 3)) {
            Object var9 = var2.getBeanKey(var4);
            if (!ReaderEvent.compareKeys(var7.getCharactersAsString(), var9) && !ReaderEvent.compareKeys(this.trimWS(var7.getCharactersAsString()), this.trimWS(var9))) {
               if (this.merge) {
                  System.out.println("mergeSiblings... Refusal to merge, keys different: " + var7.getElementName() + " values " + var7.getCharactersAsString() + ", " + var9);
               }

               return false;
            }
         }

         if (var5.containsKey(var7.getLocalName())) {
            String var16 = var7.getLocalName();
            var5.remove(var7.getLocalName());

            while(var6.hasNext()) {
               var7 = (ReaderEvent)var6.next();
               if (!var7.getLocalName().equals(var16)) {
                  break;
               }
            }

            if (!var6.hasNext()) {
               break;
            }
         }
      }

      ArrayList var13 = new ArrayList();
      Iterator var14 = var2.getChildren().iterator();

      while(true) {
         while(true) {
            ReaderEvent var15;
            do {
               if (!var14.hasNext()) {
                  if (var13.size() > 0) {
                     var1.getChildren().addAll(var13);
                  }

                  if (this.merge) {
                     System.out.println("mergeSiblings(end)... source");
                     var1.toXML(System.out);
                     System.out.println("mergeSiblings(end)... targetChild");
                     var2.toXML(System.out);
                  }

                  return true;
               }

               var15 = (ReaderEvent)var14.next();
            } while(var15.isDiscarded());

            if (var5.remove(var15.getLocalName()) != null) {
               if (var4 == null) {
                  System.out.println("no helper... continue");
                  continue;
               }

               int var17 = var4.getPropertyIndex(var15.getElementName());
               if (!var4.isBean(var17) && !var4.isArray(var17)) {
                  continue;
               }
            }

            boolean var18 = false;
            Iterator var10 = var1.getChildren().iterator();

            while(var10.hasNext()) {
               ReaderEvent var11 = (ReaderEvent)var10.next();
               if (var11.getLocalName().equals(var15.getLocalName()) && var4 != null) {
                  int var12 = var4.getPropertyIndex(var11.getElementName());
                  if (var4.isBean(var12)) {
                     if (var11.getChildren().size() > 0) {
                        if (this.mergeSiblings(var11, var15, var3)) {
                           var18 = true;
                        }
                     } else if (this.merge) {
                        System.out.println("mergeSiblings(end)... source empty, nothing to merge");
                     }
                  } else if (var11.isSingleton()) {
                     var18 = true;
                  }
               }
            }

            if (!var18) {
               var13.add(var15);
            }
         }
      }
   }

   private void replaceMatchingXpaths(ReaderEvent var1, boolean var2) {
      if (!var1.isDiscarded() && this.parentToPath.size() > 0) {
         String var3 = var1.getPath();
         Iterator var4 = this.parentToPath.entrySet().iterator();

         ReaderEvent var6;
         do {
            String var7;
            do {
               if (!var4.hasNext()) {
                  for(int var8 = 0; var8 < var1.getChildren().size(); ++var8) {
                     ReaderEvent var9 = (ReaderEvent)var1.getChildren().elementAt(var8);
                     this.replaceMatchingXpaths(var9, var2);
                  }

                  return;
               }

               Map.Entry var5 = (Map.Entry)var4.next();
               var6 = (ReaderEvent)var5.getKey();
               var7 = var6.getPath();
            } while(var3.compareTo(var7) != 0);

            if (this.debug || this.merge) {
               System.out.println(".. matched " + var3);
            }
         } while(!this.mergeSiblings(var6, var1, var2));

         var1.replaceAndMoveChildren(var6);
         this.pathToParent.remove(var6);
      }
   }

   private ReaderEvent scanForSiblingReaderEvent(ReaderEvent var1, ReaderEvent var2) {
      ReaderEvent var3 = var1;
      Iterator var4 = var1.getChildren().iterator();

      while(var4.hasNext()) {
         var3 = (ReaderEvent)var4.next();
         if (this.debug || this.merge) {
            System.out.println("compare sibling [" + var3.getLocalName() + "] to [" + var2.getLocalName() + "]");
         }

         if (var3.getLocalName().equals(var2.getLocalName()) && this.pathsMatch(var3, var2)) {
            return var3;
         }

         if (var3.getChildren() != null) {
            var3 = this.scanForSiblingReaderEvent(var3, var2);
         }
      }

      return var3;
   }

   private void adoptUnmatchedReaderEvents(ReaderEvent var1) {
      Iterator var2 = var1.getChildren().iterator();

      while(true) {
         ReaderEvent var3;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (ReaderEvent)var2.next();
         } while(var3.isDiscarded());

         boolean var4 = true;
         Iterator var5;
         ReaderEvent var6;
         if (var3.isSingleton()) {
            var5 = this.root.getChildren().iterator();

            while(var5.hasNext()) {
               var6 = (ReaderEvent)var5.next();
               if (var6.getLocalName() != null && var3.getLocalName() != null && var6.getLocalName().equals(var3.getLocalName())) {
                  var3.fixParents(this.root);
                  var3.discard();
                  var6.getChildren().addAll(var3.getChildren());
                  var4 = false;
                  break;
               }
            }
         }

         if (var3.isAdditive()) {
            var5 = this.root.getChildren().iterator();

            while(var5.hasNext()) {
               var6 = (ReaderEvent)var5.next();
               if (var6.getLocalName().equals(var3.getLocalName())) {
                  var3.fixParents(var6.getParentReaderEvent());
                  this.root.getChildren().set(this.root.getChildren().indexOf(var6), var3);
                  var4 = false;
                  break;
               }
            }
         }

         if (var4) {
            var3.fixParents(this.root);
            this.root.getChildren().add(var3);
         }
      }
   }

   private void resetDescriptor(AbstractDescriptorLoader var1, Descriptor var2) throws IOException, XMLStreamException {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      var1.getDescriptorManager().writeDescriptorAsXML(var2, var3);
      byte[] var4 = var3.toByteArray();
      ByteArrayInputStream var5 = new ByteArrayInputStream(var4);
      BasicMunger var6 = var1.createXMLStreamReader(var5);
      var1.getDescriptorManager().createDescriptor(var6);
      this.root = var6.root;
      this.top = var6.top;
      this.stack = var6.stack;
      this.descriptor = var6.descriptor;
   }

   private boolean pathsMatch(ReaderEvent var1, ReaderEvent var2) {
      while(var1 != null && var2 != null && var1.getEventType() != 7 && var2.getEventType() != 7) {
         String var3 = var1 == null ? "" : var1.getLocalName();
         String var4 = var2 == null ? "" : var2.getLocalName();
         if (!var3.equals(var4)) {
            return false;
         }

         var1 = var1.getParent();
         var2 = var2.getParent();
      }

      if ((var1 != null || var2 == null) && (var1 == null || var2 != null) && var1.getEventType() == var2.getEventType()) {
         return true;
      } else {
         return false;
      }
   }

   protected String type2Str(int var1) {
      switch (var1) {
         case 1:
            return "START_ELEMENT[" + this.getLocalName() + "]";
         case 2:
            return "END_ELEMENT[" + this.getLocalName() + "]";
         case 3:
            return "PROCESSING_INSTRUCTION";
         case 4:
            return "CHARACTERS: [" + new String(this.getTextCharacters()) + "]";
         case 5:
            return "COMMENT";
         case 6:
            return "SPACE[6]";
         case 7:
            return "START_DOCUMENT[7]";
         case 8:
            return "END_DOCUMENT[8]";
         case 9:
            return "ENTITY_REFERENCE[9]";
         case 10:
            return "ATTRIBUTE";
         case 11:
            return "DTD";
         case 12:
            return "CDATA[12]";
         case 13:
            return "NAMESPACE[13]";
         case 14:
            return "NOTATION_DECLARATION";
         case 15:
            return "ENTITY_DECLARATION";
         default:
            throw new AssertionError("Unexpected type " + var1);
      }
   }

   private void dumpStartMerge(ReaderEvent var1) {
      System.out.println("BasicMunger: start merge...");
      System.out.println("BasicMunger: current ReaderEvent: = ");
      this.root.toXML(System.out);
      this.dumpParent2Path();
      System.out.println("-----------");
      System.out.println("BasicMunger: ReaderEvent to merge into current: = ");
      var1.toXML(System.out);
      this.dumpPath2Parents();
      System.out.println("-----------");
      System.out.println("BasicMunger: ... continue merge...");
   }

   private void dumpParent2Path() {
      System.out.println("\ndump parentToPath:");
      Iterator var1 = this.parentToPath.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         ReaderEvent var3 = (ReaderEvent)var2.getKey();
         String var4 = (String)var2.getValue();
         System.out.println("instance = (" + var3 + ")=" + var3.getPath() + " mapped to key =" + var4);
      }

   }

   private void dumpPath2Parents() {
      System.out.println("\ndump pathToParent:");
      Iterator var1 = this.pathToParent.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         String var3 = (String)var2.getKey();
         ReaderEvent var4 = (ReaderEvent)var2.getValue();
         System.out.println("key =" + var3 + " mapped to instance = (" + var4 + ")=" + var4.getPath());
      }

   }

   public void setQueuedEvents(ArrayList var1) {
      this.queuedEvents = var1;
      this.setPlayback(true);
      this.usingDTD = true;
   }

   public void toQueuedEvents(ArrayList var1) {
      if (this.descriptor == null) {
         this.root.toQueuedEvents(var1);
      } else {
         this.root.toQueuedEvents(var1);
      }
   }

   public ReaderEvent getQueuedEvent(int var1, Object var2) {
      return new ReaderEvent(var1, var2, this.getLocation(), this);
   }

   public boolean supportsValidation() {
      return false;
   }

   private Object trimWS(Object var1) {
      if (!(var1 instanceof String)) {
         return var1;
      } else {
         String var2 = (String)var1;
         if (var2 == null) {
            return var2;
         } else {
            int var3 = 0;
            int var4 = var2.length();

            int var5;
            for(var5 = 0; var5 < var2.length() && Character.isWhitespace(var2.charAt(var5)); ++var5) {
               ++var3;
            }

            for(var5 = var4; var5 > 0 && var5 > var3 && Character.isWhitespace(var2.charAt(var5 - 1)); --var5) {
               --var4;
            }

            return var3 == 0 && var4 == var2.length() ? var2 : var2.substring(var3, var4);
         }
      }
   }

   public static class VariableAssignment {
      String name;
      int op;

      public VariableAssignment(String var1, String var2) {
         this.name = var1;
         this.setOperation(var2);
      }

      public void setOperation(String var1) {
         if (var1.equals("add")) {
            this.op = 1;
         } else if (var1.equals("remove")) {
            this.op = 2;
         } else if (var1.equals("replace")) {
            this.op = 3;
         } else {
            this.op = -1;
         }

      }

      public String getName() {
         return this.name;
      }

      public int getOperation() {
         return this.op;
      }
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
         return BasicMunger.this.loader.getAbsolutePath() + ":" + this.l.getLineNumber() + ":" + this.l.getColumnNumber();
      }

      public String getSystemId() {
         return this.l.getSystemId();
      }
   }
}
