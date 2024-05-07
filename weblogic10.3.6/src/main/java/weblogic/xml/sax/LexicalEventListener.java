package weblogic.xml.sax;

import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class LexicalEventListener implements LexicalHandler {
   private String DTDName;
   private String systemId;
   private String publicId;

   public void comment(char[] var1, int var2, int var3) throws SAXException {
   }

   public void startCDATA() throws SAXException {
   }

   public void endCDATA() throws SAXException {
   }

   public void startEntity(String var1) throws SAXException {
   }

   public void endEntity(String var1) throws SAXException {
   }

   public void startDTD(String var1, String var2, String var3) throws SAXException {
      this.setDTDName(var1);
      this.setPublicId(var2);
      this.setSystemId(var3);
   }

   public void endDTD() throws SAXException {
   }

   public String getDTDName() {
      return this.DTDName;
   }

   private void setDTDName(String var1) {
      this.DTDName = var1;
   }

   public String getSystemId() {
      return this.systemId;
   }

   private void setSystemId(String var1) {
      this.systemId = var1;
   }

   public String getPublicId() {
      return this.publicId;
   }

   private void setPublicId(String var1) {
      this.publicId = var1;
   }
}
