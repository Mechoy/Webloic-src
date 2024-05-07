package weblogic.xml.dtdc;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Hashtable;
import java.util.Locale;
import org.xml.sax.AttributeList;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public abstract class BaseParser implements Parser, AttributeList, Locator {
   protected DocumentHandler dh;
   protected int currentLine = 1;
   protected int lastLinePosition = 0;
   protected int current = 0;
   protected int end = 0;
   protected int startCharacterData = -1;
   protected Hashtable ids = new Hashtable();
   protected String[] names;
   protected String[] types;
   protected String[] values;
   protected int numAttributes = 0;
   protected Hashtable htypes = new Hashtable();
   protected Hashtable hvalues = new Hashtable();

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[ " + this.getClass().getName() + ", " + "currentLine=" + this.currentLine + ", currentColumn=" + (this.current - this.lastLinePosition) + ", attributes=[");

      for(int var2 = 0; var2 < this.numAttributes; ++var2) {
         var1.append(this.names[var2] + "=(" + this.types[var2] + ")" + this.values[var2]);
         if (var2 != this.numAttributes - 1) {
            var1.append(", ");
         }
      }

      var1.append("]]");
      return var1.toString();
   }

   public int getLineNumber() {
      return this.currentLine;
   }

   public int getColumnNumber() {
      return this.current - this.lastLinePosition;
   }

   public String getPublicId() {
      return null;
   }

   public String getSystemId() {
      return null;
   }

   public void setLocale(Locale var1) {
      if (!Locale.getDefault().equals(var1)) {
         throw new Error("Operation unsupported, locale cannot differ from the default");
      }
   }

   public void setEntityResolver(EntityResolver var1) {
   }

   public void setDTDHandler(DTDHandler var1) {
   }

   public void setErrorHandler(ErrorHandler var1) {
   }

   public void setDocumentHandler(DocumentHandler var1) {
      this.dh = var1;
   }

   public void parse(String var1) throws IOException, SAXException {
      this.parse(new File(var1));
   }

   public void parse(InputSource var1) throws IOException, SAXException {
      CharArrayWriter var2 = new CharArrayWriter();
      BufferedReader var3 = getBufferedReader(var1);

      try {
         char[] var4 = new char[2048];

         int var5;
         while((var5 = var3.read(var4)) != -1) {
            var2.write(var4, 0, var5);
         }

         this.parse(var2.toCharArray());
      } finally {
         var3.close();
      }
   }

   public abstract void parse(char[] var1) throws SAXException;

   public void parse(File var1) throws IOException, SAXException {
      FileReader var2 = new FileReader(var1);

      try {
         char[] var3 = new char[(int)var1.length()];
         var2.read(var3, 0, var3.length);
         this.parse(var3);
      } finally {
         var2.close();
      }

   }

   public void parse(InputStream var1) throws IOException, SAXException {
      CharArrayWriter var2 = new CharArrayWriter();
      BufferedReader var3 = new BufferedReader(new InputStreamReader(var1));

      try {
         char[] var4 = new char[2048];

         int var5;
         while((var5 = var3.read(var4)) != -1) {
            var2.write(var4, 0, var5);
         }

         this.parse(var2.toCharArray());
      } finally {
         var3.close();
      }
   }

   public void parse(InputStream var1, String var2) throws IOException, SAXException {
      CharArrayWriter var3 = new CharArrayWriter();
      BufferedReader var4 = new BufferedReader(new InputStreamReader(var1, var2));

      try {
         char[] var5 = new char[2048];

         int var6;
         while((var6 = var4.read(var5)) != -1) {
            var3.write(var5, 0, var6);
         }

         this.parse(var3.toCharArray());
      } finally {
         var4.close();
      }
   }

   protected final void nextLine() {
      ++this.currentLine;
      this.lastLinePosition = this.current;
   }

   protected final void eatComment(char[] var1) throws SAXException {
      this.sendCharacters(var1, 4);

      while(var1[this.current++] != '-' || var1[this.current] != '-' || var1[this.current + 1] != '>') {
         if (var1[this.current] == '\n') {
            this.nextLine();
         }
      }

      this.current += 2;
      this.startCharacterData = -1;
   }

   protected final void sendCharacters(char[] var1, int var2) throws SAXException {
      if (this.startCharacterData != -1) {
         this.dh.characters(var1, this.startCharacterData, this.current - var2 - this.startCharacterData);
         this.startCharacterData = -1;
      }

   }

   protected final void match(char[] var1, String var2, String var3, int var4) throws SAXParseException {
      for(int var5 = var2.length() - 1; var5 >= 0; --var5) {
         if (var1[this.current + var5] != var2.charAt(var5)) {
            throw new SAXParseException("Could not parse " + var3 + ", starting at line " + var4, this);
         }
      }

      this.current += var2.length();
   }

   protected void reset() {
      for(int var1 = this.numAttributes - 1; var1 >= 0; --var1) {
         this.names[var1] = null;
         this.types[var1] = null;
         this.values[var1] = null;
      }

      this.numAttributes = 0;
      this.htypes.clear();
      this.hvalues.clear();
   }

   public int getLength() {
      return this.numAttributes;
   }

   public String getName(int var1) {
      return this.names[var1];
   }

   public String getType(int var1) {
      return this.types[var1];
   }

   public String getValue(int var1) {
      return this.values[var1];
   }

   public String getType(String var1) {
      return (String)this.htypes.get(var1);
   }

   public String getValue(String var1) {
      return (String)this.hvalues.get(var1);
   }

   public String _readAttribute(char[] var1) throws SAXException {
      StringBuffer var2 = null;
      ++this.current;

      char var3;
      do {
         do {
            var3 = var1[this.current++];
         } while(var3 == ' ');
      } while(var3 == '\t' || var3 == '\n' || var3 == '\r');

      if (var3 != '"' && var3 != '\'') {
         throw new SAXParseException("Invalid attribute", this);
      } else {
         int var5 = this.current;

         while(true) {
            char var4;
            char[] var6;
            while((var4 = var1[this.current++]) != var3) {
               switch (var4) {
                  case '&':
                     var6 = new char[this.current - var5];
                     int var7 = this.current;
                     this.handleEscapes(var1);
                     if (var2 == null) {
                        var2 = new StringBuffer();
                     }

                     System.arraycopy(var1, var5, var6, 0, var7 - var5);
                     var2.append(new String(var6));
                     var5 = this.current;
                     break;
                  case '<':
                     if (var1[this.current] != '!' || var1[this.current + 1] != '[' || var1[this.current + 2] != 'C' || var1[this.current + 3] != 'D' || var1[this.current + 4] != 'A' || var1[this.current + 5] != 'T' || var1[this.current + 6] != 'A' || var1[this.current + 7] != '[') {
                        throw new SAXParseException("Illegal character in input: <", this);
                     }

                     var6 = new char[this.current - var5 - 1];
                     if (var2 == null) {
                        var2 = new StringBuffer();
                     }

                     System.arraycopy(var1, var5, var6, 0, this.current - var5 - 1);
                     var2.append(new String(var6));
                     this.current += 8;
                     var5 = this.current;
                     boolean var8 = false;

                     label88:
                     do {
                        while(true) {
                           switch (var1[this.current++]) {
                              case ']':
                                 continue label88;
                           }
                        }
                     } while(var1[this.current] != ']' || var1[this.current + 1] != '>');

                     this.current += 2;
                     int var10 = this.current - 3;
                     var6 = new char[var10 - var5];
                     System.arraycopy(var1, var5, var6, 0, var10 - var5);
                     var2.append(new String(var6));
                     var5 = this.current;
               }
            }

            var6 = new char[this.current - var5 - 1];
            System.arraycopy(var1, var5, var6, 0, this.current - var5 - 1);
            String var9 = new String(var6);
            if (var2 != null) {
               return var2.append(var9).toString();
            }

            return var9;
         }
      }
   }

   protected void putAttribute(String var1, String var2, String var3) {
      this.hvalues.put(var1, var3);
      this.names[this.numAttributes] = var1;
      this.types[this.numAttributes] = var2;
      this.values[this.numAttributes++] = var3;
   }

   protected void handleEscapes(char[] var1) throws SAXException {
      int var2;
      var2 = this.current;
      label56:
      switch (var1[this.current]) {
         case 'a':
            switch (var1[this.current + 1]) {
               case 'm':
                  if (var1[this.current + 2] == 'p' && var1[this.current + 3] == ';') {
                     this.compress(var1, '&', 4);
                     break label56;
                  }

                  throw new SAXParseException("Invalid character sequence", this);
               case 'p':
                  if (var1[this.current + 2] != 'o' || var1[this.current + 3] != 's' || var1[this.current + 4] != ';') {
                     throw new SAXParseException("Invalid character sequence", this);
                  }

                  this.compress(var1, '\'', 5);
               default:
                  break label56;
            }
         case 'g':
            if (var1[this.current + 1] == 't' && var1[this.current + 2] == ';') {
               this.compress(var1, '>', 3);
               break;
            }

            throw new SAXParseException("Invalid character sequence", this);
         case 'l':
            if (var1[this.current + 1] == 't' && var1[this.current + 2] == ';') {
               this.compress(var1, '<', 3);
               break;
            }

            throw new SAXParseException("Invalid character sequence", this);
         case 'q':
            if (var1[this.current + 1] != 'u' || var1[this.current + 2] != 'o' || var1[this.current + 3] != 't' || var1[this.current + 4] != ';') {
               throw new SAXParseException("Invalid character sequence", this);
            }

            this.compress(var1, '"', 5);
      }

      if (this.startCharacterData != -1) {
         this.dh.characters(var1, this.startCharacterData, var2 - this.startCharacterData);
         this.startCharacterData = this.current;
      }

   }

   protected void handleCDATA(char[] var1) throws SAXException {
      if (this.startCharacterData != -1) {
         this.dh.characters(var1, this.startCharacterData, this.current - 9 - this.startCharacterData);
      }

      int var2 = this.current;

      label23:
      do {
         while(true) {
            switch (var1[this.current++]) {
               case ']':
                  continue label23;
            }
         }
      } while(var1[this.current] != ']' || var1[this.current + 1] != '>');

      char[] var3 = new char[this.current - var2 - 1];
      System.arraycopy(var1, var2, var3, 0, this.current - var2 - 1);
      new String(var3);
      this.current += 2;
      this.dh.characters(var1, var2, this.current - 3 - var2);
      this.startCharacterData = this.current;
   }

   private void compress(char[] var1, char var2, int var3) {
      var1[this.current - 1] = var2;
      this.current += var3;
   }

   private static BufferedReader getBufferedReader(InputSource var0) {
      Object var1 = var0.getCharacterStream();
      if (var1 == null) {
         String var2 = var0.getSystemId();

         try {
            var1 = new FileReader(var2);
         } catch (FileNotFoundException var6) {
            try {
               URL var4 = new URL(var0.getSystemId());
               var1 = new InputStreamReader(var4.openStream());
            } catch (Exception var5) {
            }
         }
      }

      return var1 == null ? null : new BufferedReader((Reader)var1, 1000);
   }
}
