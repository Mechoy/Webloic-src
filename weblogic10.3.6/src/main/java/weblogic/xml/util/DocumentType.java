package weblogic.xml.util;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.net.URL;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.xml.process.XMLParsingException;

public final class DocumentType implements XMLConstants {
   private static final boolean debug = System.getProperty("weblogic.xml.debug") != null;
   private static final String DOCTYPE_DECL_START = "<!DOCTYPE";
   private static final String[] ROOT_NAME_STOP = new String[]{"[", "\t", "\r", " ", "\n", ">"};
   private static final String[] ELEMENT_NAME_STOP = new String[]{"/", "\t", "\r", " ", "\n", ">"};
   private static final String[] EXTERNAL_ID_START = new String[]{"PUBLIC", "SYSTEM", ">"};
   private static final String[] QUOTES = new String[]{"\"", "'"};
   private static final String[] WHITE_SPACE = new String[]{"\t", "\r", " ", "\n"};
   private String rootTag;
   private String publicId;
   private String systemId;

   public DocumentType(String var1, String var2, String var3) {
      this.publicId = var1;
      this.systemId = var2;
      this.rootTag = var3;
   }

   public DocumentType(InputSource var1, boolean var2) throws IOException, SAXException, XMLParsingException {
      this(getReader(var1), var2);
   }

   public DocumentType(Reader var1, boolean var2) throws IOException, SAXException, XMLParsingException {
      if (var1 == null) {
         throw new SAXException("Document input stream was null");
      } else if (var2 && !var1.markSupported()) {
         throw new XMLParsingException("Cannot read stream with reset=true because it does not support mark");
      } else {
         if (debug) {
            System.out.println("DocumentType: reset = " + var2);
         }

         if (var2) {
            var1.mark(1000);
         }

         PushbackReader var4 = new PushbackReader(var1, 1000);

         try {
            while(true) {
               if (debug) {
                  System.out.println("advancing to '<'");
               }

               String var5 = ParsingUtils.read(var4, "<", false);
               if (debug) {
                  System.out.println("\"" + var5 + "\"");
               }

               if (startDocTypeDecl(var4)) {
                  DocTypeDecl var9 = docTypeDecl(var4);
                  this.rootTag = var9.rootTag;
                  this.publicId = var9.publicId;
                  this.systemId = var9.systemId;
                  break;
               }

               if (startElementTag(var4)) {
                  this.rootTag = elementTag(var4);
                  this.systemId = retrieveSchemaSystemId(var4);
                  break;
               }

               var4.read();
               String var6 = ParsingUtils.peek(var4, 1);
               if ("?!/".indexOf(var6) < 0) {
                  throw new XMLParsingException("Cannot locate DOCTYPE header or root element");
               }
            }
         } catch (EOFException var8) {
            throw new XMLParsingException("Cannot locate DOCTYPE header or root element");
         }

         if (var2) {
            try {
               var1.reset();
            } catch (IOException var7) {
               throw new IOException("Could not reset mark because read too far");
            }
         }

      }
   }

   public String getPublicId() {
      return this.publicId;
   }

   public String getSystemId() {
      return this.systemId;
   }

   public String getRootElementTag() {
      return this.rootTag;
   }

   public boolean equals(Object var1) {
      DocumentType var2 = null;

      try {
         var2 = (DocumentType)var1;
      } catch (ClassCastException var4) {
         return false;
      }

      return nullableStringEquals(this.publicId, var2.publicId) && nullableStringEquals(this.systemId, var2.systemId) && nullableStringEquals(this.rootTag, var2.rootTag);
   }

   public int hashCode() {
      int var1 = 0;
      if (this.publicId != null) {
         var1 ^= this.publicId.hashCode();
      }

      if (this.systemId != null) {
         var1 ^= this.systemId.hashCode();
      }

      if (this.rootTag != null) {
         var1 ^= this.rootTag.hashCode();
      }

      return var1;
   }

   private static boolean nullableStringEquals(String var0, String var1) {
      if (var0 == null) {
         return var1 == null;
      } else {
         return var0.equals(var1);
      }
   }

   private static String readQuotedLiteral(PushbackReader var0) throws IOException {
      if (debug) {
         System.out.println("reading quoted literal:");
      }

      if (debug) {
         System.out.println("reading through first quote");
      }

      String var1 = ParsingUtils.read(var0, QUOTES, true);
      char var2 = var1.charAt(var1.length() - 1);
      if (debug) {
         System.out.println("quote type is " + var2);
      }

      String var3 = new String(new char[]{var2});
      if (debug) {
         System.out.println("reading until close quote");
      }

      String var4 = ParsingUtils.read(var0, var3, false);
      ParsingUtils.read(var0, var3, true);
      return var4;
   }

   private static Reader getReader(InputSource var0) throws XMLParsingException, SAXException {
      Object var1 = var0.getCharacterStream();
      if (var1 == null) {
         InputStream var2 = var0.getByteStream();
         if (var2 != null) {
            var1 = new InputStreamReader(var2);
         } else {
            String var3 = var0.getSystemId();

            try {
               if (debug) {
                  System.out.println("Trying to open file " + var3);
               }

               if (var3 != null) {
                  var1 = new FileReader(var3);
               }
            } catch (FileNotFoundException var7) {
               try {
                  if (debug) {
                     System.out.println("Not found. Trying to open URL " + var3);
                  }

                  var1 = new InputStreamReader((new URL(var3)).openStream());
               } catch (Exception var6) {
               }
            }
         }
      }

      if (var1 == null) {
         if (debug) {
            System.out.println("SystemID = " + var0.getSystemId());
         }

         throw new SAXException("Could not open or read input source");
      } else {
         return (Reader)var1;
      }
   }

   public static void dumpReader(Reader var0) throws IOException {
      char[] var1 = new char[1000];

      while(var0.read(var1) > 0) {
         System.out.print(var1);
      }

      System.out.println("");
   }

   private static boolean startElementTag(PushbackReader var0) throws IOException {
      String var1 = ParsingUtils.peek(var0, 2);
      return var1.charAt(0) == '<' && Character.isLetter(var1.charAt(1));
   }

   private static boolean startDocTypeDecl(PushbackReader var0) throws IOException {
      return "<!DOCTYPE".equals(ParsingUtils.peek(var0, "<!DOCTYPE".length()));
   }

   private static boolean startExternalId(PushbackReader var0) throws IOException {
      return !"[".equals(ParsingUtils.peek(var0, 1));
   }

   private static ExternalId externalId(PushbackReader var0) throws IOException {
      ExternalId var1 = new ExternalId();
      if (debug) {
         System.out.println("reading external id start");
      }

      String var2 = ParsingUtils.read(var0, EXTERNAL_ID_START, true);
      if (debug) {
         System.out.println("external id start = " + var2);
      }

      if (debug) {
         System.out.println("reading through WS");
      }

      ParsingUtils.readWS(var0);
      if ("SYSTEM".equals(var2)) {
         var1.systemId = readQuotedLiteral(var0);
         if (debug) {
            System.out.println("reading system ID = \"" + var1.systemId + "\"");
         }
      } else if ("PUBLIC".equals(var2)) {
         var1.publicId = readQuotedLiteral(var0);
         if (debug) {
            System.out.println("reading public ID = \"" + var1.publicId + "\"");
         }

         if (debug) {
            System.out.println("reading through WS");
         }

         ParsingUtils.readWS(var0);
         var1.systemId = readQuotedLiteral(var0);
         if (debug) {
            System.out.println("reading system ID = \"" + var1.systemId + "\"");
         }
      }

      return var1;
   }

   private static DocTypeDecl docTypeDecl(PushbackReader var0) throws IOException {
      DocTypeDecl var1 = new DocTypeDecl();
      if (debug) {
         System.out.println("reading through <!DOCTYPE");
      }

      ParsingUtils.read(var0, "<!DOCTYPE", true);
      if (debug) {
         System.out.println("reading through WS");
      }

      ParsingUtils.readWS(var0);
      if (debug) {
         System.out.println("reading through root tag name");
      }

      var1.rootTag = ParsingUtils.read(var0, ROOT_NAME_STOP, false);
      ParsingUtils.readWS(var0);
      if (debug) {
         System.out.println("read root tag name = " + var1.rootTag);
      }

      if (startExternalId(var0)) {
         ExternalId var2 = externalId(var0);
         var1.publicId = var2.publicId;
         var1.systemId = var2.systemId;
      }

      return var1;
   }

   private static String elementTag(PushbackReader var0) throws IOException {
      if (debug) {
         System.out.println("reading element tag start");
      }

      ParsingUtils.read(var0, "<", true);
      String var1 = ParsingUtils.read(var0, ELEMENT_NAME_STOP, false);
      return var1;
   }

   private static String retrieveSchemaSystemId(PushbackReader var0) throws IOException {
      String var1 = ParsingUtils.read(var0, new String[]{"schemaLocation", ">"}, true);
      if (var1 != null && !"".equals(var1) && var1.charAt(var1.length() - 1) == '>') {
         return null;
      } else {
         if (debug) {
            System.out.println("reading schemaLocation:");
         }

         if (debug) {
            System.out.println("reading through first quote");
         }

         String var2 = ParsingUtils.read(var0, QUOTES, true);
         char var3 = var2.charAt(var2.length() - 1);
         if (debug) {
            System.out.println("quote type is " + var3);
         }

         String var4 = new String(new char[]{var3});
         if (debug) {
            System.out.println("reading until close quote or white space");
         }

         String[] var5 = new String[WHITE_SPACE.length + 1];
         System.arraycopy(WHITE_SPACE, 0, var5, 0, WHITE_SPACE.length);
         var5[var5.length - 1] = var4;
         String var6 = ParsingUtils.read(var0, var5, false);
         String var7 = ParsingUtils.peek(var0, 1);
         if (var7.equals(var4)) {
            return var6;
         } else {
            ParsingUtils.readWS(var0);
            var6 = ParsingUtils.read(var0, var5, false);
            return var6;
         }
      }
   }

   private static class DocTypeDecl {
      public String publicId;
      public String systemId;
      public String rootTag;

      private DocTypeDecl() {
      }

      // $FF: synthetic method
      DocTypeDecl(Object var1) {
         this();
      }
   }

   private static class ExternalId {
      public String publicId;
      public String systemId;

      private ExternalId() {
      }

      // $FF: synthetic method
      ExternalId(Object var1) {
         this();
      }
   }
}
