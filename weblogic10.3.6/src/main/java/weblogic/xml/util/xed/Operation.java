package weblogic.xml.util.xed;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLStreamException;
import weblogic.xml.xpath.StreamXPath;
import weblogic.xml.xpath.XPathException;
import weblogic.xml.xpath.XPathStreamFactory;

public class Operation {
   private ArrayList commands = new ArrayList();
   private XPathStreamFactory factory = new XPathStreamFactory();

   public void add(Command var1) {
      this.commands.add(var1);
   }

   public ArrayList getPre() throws XMLStreamException {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.commands.iterator();

      while(var2.hasNext()) {
         Command var3 = (Command)var2.next();
         if (var3.isInsertBefore()) {
            XMLInputStream var4 = var3.getResult();
            if (var4 != null) {
               var1.add(var4);
            }
         }
      }

      return var1;
   }

   public ArrayList getChild() throws XMLStreamException {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.commands.iterator();

      while(var2.hasNext()) {
         Command var3 = (Command)var2.next();
         if (var3.isInsertChild()) {
            XMLInputStream var4 = var3.getResult();
            if (var4 != null) {
               var1.add(var4);
            }
         }
      }

      return var1;
   }

   public ArrayList getPost() throws XMLStreamException {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.commands.iterator();

      while(var2.hasNext()) {
         Command var3 = (Command)var2.next();
         if (var3.isInsertAfter()) {
            XMLInputStream var4 = var3.getResult();
            if (var4 != null) {
               var1.add(var4);
            }
         }
      }

      return var1;
   }

   public boolean needToDelete() {
      Iterator var1 = this.commands.iterator();

      Command var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (Command)var1.next();
      } while(!var2.isDelete() || !((Delete)var2).wasHit());

      return true;
   }

   public ArrayList getCommands() {
      return this.commands;
   }

   public void prepare() throws XPathException, XMLStreamException {
      Iterator var1 = this.commands.iterator();

      while(var1.hasNext()) {
         Command var2 = (Command)var1.next();
         Controller var3 = new Controller(new Context(), var2);
         StreamXPath var4 = new StreamXPath(var2.getXPath());
         this.factory.install(var4, var3);
      }

   }

   public XMLInputStream getStream(String var1) throws XPathException, XMLStreamException {
      XMLInputStreamFactory var2 = XMLInputStreamFactory.newInstance();
      XMLInputStream var3 = var2.newInputStream(new File(var1));
      return this.factory.createStream(var3);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.commands.iterator();
      var1.append("-------[\n");

      while(var2.hasNext()) {
         var1.append("[" + var2.next() + "]\n");
      }

      var1.append("\n]-------\n");
      return var1.toString();
   }
}
