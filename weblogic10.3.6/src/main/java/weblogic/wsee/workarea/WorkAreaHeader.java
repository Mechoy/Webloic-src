package weblogic.wsee.workarea;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.util.ToStringWriter;
import weblogic.xml.dom.DOMUtils;
import weblogic.xml.dom.Util;
import weblogic.xml.domimpl.Loader;
import weblogic.xml.domimpl.Saver;

public class WorkAreaHeader extends MsgHeader {
   private Element content;
   public static final QName NAME;
   public static final MsgHeaderType TYPE;

   public QName getName() {
      return NAME;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public void parse(InputStream var1) {
      try {
         Document var2 = Loader.load(var1);
         this.content = var2.getDocumentElement();
         if (this.content != null) {
            this.content.removeAttribute("version");
         }

      } catch (IOException var3) {
         throw new RuntimeException(var3.getMessage(), var3);
      }
   }

   public InputStream getInputStream() {
      try {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         Saver.save(var1, this.content);
         return new ByteArrayInputStream(var1.toByteArray());
      } catch (IOException var2) {
         throw new RuntimeException(var2.getMessage(), var2);
      }
   }

   public void read(Element var1) throws MsgHeaderException {
      if (var1 != null) {
         this.content = this.firstElementChild(var1);
      }

   }

   private Element firstElementChild(Element var1) {
      for(Node var2 = var1.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2.getNodeType() == 1) {
            return (Element)var2;
         }
      }

      return null;
   }

   public void write(Element var1) throws MsgHeaderException {
      DOMUtils.addNamespaceDeclaration(var1, "work", "http://bea.com/2004/06/soap/workarea/");
      var1.appendChild(var1.getOwnerDocument().importNode(this.content, true));
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Name:[" + this.getName() + "]\n");
      var1.append("Content:[");
      if (this.content != null) {
         var1.append(Util.printNode(this.content));
      } else {
         var1.append("<null>");
      }

      var1.append("]");
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      super.toString(var1);
   }

   static {
      NAME = WorkAreaConstants.WORK_AREA_HEADER;
      TYPE = new WorkAreaHeaderType();
   }
}
