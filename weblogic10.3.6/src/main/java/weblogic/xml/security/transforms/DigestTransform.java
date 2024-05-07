package weblogic.xml.security.transforms;

import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;

public class DigestTransform extends OctetTransform {
   private OutputStream os;
   private final MessageDigest md;

   public DigestTransform(OutputStream var1, MessageDigest var2) {
      this.os = var1;
      this.md = var2;
   }

   public OutputStream getOutputStream() {
      return new DigestOutputStream(this.os, this.md);
   }

   public String getURI() {
      return "Digest";
   }

   public void setDest(OctetTransform var1) {
      throw new AssertionError("Sink transform");
   }
}
