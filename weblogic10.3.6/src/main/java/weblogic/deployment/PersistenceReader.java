package weblogic.deployment;

import java.io.InputStream;
import java.util.Stack;
import javax.xml.stream.XMLStreamException;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.ReaderEvent2;
import weblogic.application.descriptor.ReaderEventInfo;
import weblogic.application.descriptor.VersionMunger;

public final class PersistenceReader extends VersionMunger {
   private static final String SCHEMA_HELPER = "weblogic.j2ee.descriptor.PersistenceBeanImpl$SchemaHelper2";
   public static final String DUMMY_PERSISTENCE_UNIT = "__ORACLE_WLS_INTERNAL_DUMMY_PERSISTENCE_UNIT";
   private boolean addDummyPU;
   private boolean addExcludeUnlisted;

   public PersistenceReader(InputStream var1, AbstractDescriptorLoader2 var2) throws XMLStreamException {
      super(var1, var2, "weblogic.j2ee.descriptor.PersistenceBeanImpl$SchemaHelper2");
   }

   protected String getLatestSchemaVersion() {
      return "2.0";
   }

   protected boolean isOldSchema() {
      if ("persistence".equals(this.currentEvent.getElementName())) {
         ReaderEventInfo var1 = this.currentEvent.getReaderEventInfo();
         if ("1.0".equals(var1.getAttributeValue((String)null, "version"))) {
            this.isOldSchema = true;
         } else if (var1.getAttributeValue((String)null, "version") == null) {
            this.isOldSchema = true;
         }
      }

      return this.isOldSchema;
   }

   protected boolean enableCallbacksOnSchema() {
      return this.isOldSchema;
   }

   protected void transformOldSchema() {
      if ("persistence".equals(this.currentEvent.getElementName())) {
         ReaderEventInfo var1 = this.currentEvent.getReaderEventInfo();
         int var2 = var1.getAttributeCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            if ("version".equals(var1.getAttributeLocalName(var3))) {
               this.versionInfo = var1.getAttributeValue(var3);
               this.currentEvent.getReaderEventInfo().setAttributeValue("2.0", var3);
            }
         }

         if (this.versionInfo == null) {
            this.versionInfo = "1.0";
         }
      }

   }

   protected VersionMunger.Continuation onStartElement(String var1) {
      if ("persistence".equals(var1)) {
         this.addDummyPU = "1.0".equals(this.versionInfo);
      } else if ("persistence-unit".equals(var1)) {
         this.addExcludeUnlisted = "1.0".equals(this.versionInfo);
         this.addDummyPU = false;
      } else if ("exclude-unlisted-classes".equals(var1)) {
         this.addExcludeUnlisted = false;
      } else if ("properties".equals(var1) && this.addExcludeUnlisted) {
         this.addExcludeUnlistedClasses();
         Stack var2 = this.currentEvent.getParent().getChildren();
         Object var3 = var2.remove(var2.size() - 2);
         var2.add(var3);
         return this.USE_BUFFER;
      }

      return CONTINUE;
   }

   protected VersionMunger.Continuation onEndElement(String var1) {
      if ("persistence".equals(var1) && this.addDummyPU) {
         this.addDummyPU();
         this.pushEndElement(var1);
         return this.USE_BUFFER;
      } else if ("persistence-unit".equals(var1) && this.addExcludeUnlisted) {
         this.addExcludeUnlistedClasses();
         this.pushEndElement(var1);
         return this.USE_BUFFER;
      } else {
         if ("exclude-unlisted-classes".equals(var1) && "1.0".equals(this.versionInfo)) {
            ReaderEventInfo var2 = this.lastEvent.getReaderEventInfo();
            char[] var3 = var2.getCharacters();
            if (var3 == null || this.isAllWhiteSpace(var3)) {
               var2.setCharacters("false".toCharArray());
            }
         }

         return CONTINUE;
      }
   }

   protected String getOriginalVersion() {
      return this.versionInfo;
   }

   private void addDummyPU() {
      this.forceSkipParent = true;
      this.pushStartElementLastEvent("persistence-unit");
      ReaderEventInfo var1 = ((ReaderEvent2)this.stack.peek()).getReaderEventInfo();
      var1.setAttributeCount(1);
      var1.setAttributeValue("__ORACLE_WLS_INTERNAL_DUMMY_PERSISTENCE_UNIT", (String)null, "name");
      this.pushEndElement("persistence-unit");
      this.forceSkipParent = false;
      this.addDummyPU = false;
   }

   private void addExcludeUnlistedClasses() {
      this.forceSkipParent = true;
      this.pushStartElementLastEvent("exclude-unlisted-classes");
      ReaderEventInfo var1 = ((ReaderEvent2)this.stack.peek()).getReaderEventInfo();
      var1.setCharacters("false".toCharArray());
      this.pushEndElement("exclude-unlisted-classes");
      this.forceSkipParent = false;
      this.addExcludeUnlisted = false;
   }

   private boolean isAllWhiteSpace(char[] var1) {
      char[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char var5 = var2[var4];
         if (!this.isWhiteSpace(var5)) {
            return false;
         }
      }

      return true;
   }

   private boolean isWhiteSpace(char var1) {
      return var1 == ' ' || var1 == '\t' || var1 == '\r' || var1 == '\n';
   }
}
