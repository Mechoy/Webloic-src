package weblogic.xml.security.keyinfo;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.xml.security.encryption.EncryptedKey;
import weblogic.xml.security.encryption.XMLEncReader;
import weblogic.xml.security.signature.DSIGConstants;
import weblogic.xml.security.signature.DSIGReader;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.security.wsse.SecurityTokenReference;
import weblogic.xml.security.wsse.Token;
import weblogic.xml.security.wsse.WSSEReader;
import weblogic.xml.security.wsse.v200207.WSSEConstants;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class KeyInfo implements DSIGConstants {
   private List keyNames = new ArrayList();
   private List keyValues = new ArrayList();
   private List x509Data = new ArrayList();
   private List encryptedKeys = new ArrayList();
   private List tokenRefs = new ArrayList();
   private boolean validated = false;

   public KeyInfo(String var1) {
      this.addKeyName(var1);
   }

   public KeyInfo(X509Certificate var1) throws KeyInfoException {
      this.addKeyValue(KeyValue.getKeyValue(var1.getPublicKey()));
      X509Data var2 = new X509Data(var1);
      this.addX509Data(var2);
      Iterator var3 = var2.getSubjectNames();

      while(var3.hasNext()) {
         this.addKeyName((String)var3.next());
      }

      this.validated = true;
   }

   public KeyInfo(Token var1) {
      this.addSecurityTokenReference(var1.getSecurityTokenReference());
      this.validated = true;
   }

   public KeyInfo(EncryptedKey var1) {
      this.addEncryptedKey(var1);
   }

   public KeyInfo(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public void addKeyName(String var1) {
      this.keyNames.add(var1);
   }

   public void removeKeyName(String var1) {
      this.keyNames.remove(var1);
   }

   public String getKeyName() {
      return this.keyNames.size() == 0 ? null : (String)this.keyNames.get(0);
   }

   public Iterator getKeyNames() {
      return this.keyNames.iterator();
   }

   public void addKeyValue(KeyValue var1) {
      this.keyValues.add(var1);
   }

   public void removeKeyValue(KeyValue var1) {
      this.keyValues.remove(var1);
   }

   public Iterator getKeyValues() {
      return this.keyValues.iterator();
   }

   public void addX509Data(X509Data var1) {
      this.x509Data.add(var1);
   }

   public void removeX509Data(X509Data var1) {
      this.x509Data.remove(var1);
   }

   public Iterator getX509Data() {
      return this.x509Data.iterator();
   }

   public void addEncryptedKey(EncryptedKey var1) {
      this.encryptedKeys.add(var1);
   }

   public void removeEncryptedKey(EncryptedKey var1) {
      this.encryptedKeys.remove(var1);
   }

   public Iterator getEncryptedKeys() {
      return this.encryptedKeys.iterator();
   }

   public void addSecurityTokenReference(SecurityTokenReference var1) {
      this.tokenRefs.add(var1);
   }

   public void addSecurityTokenReference(Token var1) {
      this.addSecurityTokenReference(var1.getSecurityTokenReference());
   }

   public void removeSecurityTokenReference(SecurityTokenReference var1) {
      this.tokenRefs.remove(var1);
   }

   public Iterator getSecurityTokenReferences() {
      return this.tokenRefs.iterator();
   }

   public Iterator getSubjectNames() throws KeyInfoValidationException {
      if (!this.validated) {
         this.validate();
      }

      ArrayList var1 = new ArrayList();
      Iterator var2 = this.getX509Data();

      while(var2.hasNext()) {
         X509Data var3 = (X509Data)var2.next();
         Iterator var4 = var3.getSubjectNames();

         while(var4.hasNext()) {
            var1.add(var4.next());
         }
      }

      return var1.iterator();
   }

   public Iterator getCertificates() throws KeyInfoValidationException {
      if (!this.validated) {
         this.validate();
      }

      ArrayList var1 = new ArrayList();
      Iterator var2 = this.getX509Data();

      while(var2.hasNext()) {
         X509Data var3 = (X509Data)var2.next();
         Iterator var4 = var3.getCertificates();

         while(var4.hasNext()) {
            var1.add(var4.next());
         }
      }

      return var1.iterator();
   }

   public Iterator getPublicKeys() throws KeyInfoValidationException {
      if (!this.validated) {
         this.validate();
      }

      ArrayList var1 = new ArrayList();
      Iterator var2 = this.getKeyValues();

      while(var2.hasNext()) {
         KeyValue var3 = (KeyValue)var2.next();
         var1.add(var3.getPublicKey());
      }

      return var1.iterator();
   }

   public void validate() throws KeyInfoValidationException {
      Iterator var1 = this.keyValues.iterator();

      while(var1.hasNext()) {
         KeyValue var2 = (KeyValue)var1.next();
         var2.validate();
      }

      var1 = this.x509Data.iterator();

      while(var1.hasNext()) {
         X509Data var3 = (X509Data)var1.next();
         var3.validate();
      }

      this.validated = true;
   }

   public void toXML(XMLOutputStream var1, int var2) throws XMLStreamException {
      this.toXML(var1, "http://www.w3.org/2000/09/xmldsig#", var2);
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      StreamUtils.addStart(var1, var2, "KeyInfo", var3);
      int var4 = var3 + 2;
      Iterator var5;
      if (this.keyNames.size() > 0) {
         var5 = this.keyNames.iterator();

         while(var5.hasNext()) {
            StreamUtils.addElement(var1, var2, "KeyName", (String)var5.next(), var4);
         }
      }

      if (this.keyValues.size() > 0) {
         var5 = this.keyValues.iterator();

         while(var5.hasNext()) {
            KeyValue var6 = (KeyValue)var5.next();
            StreamUtils.addStart(var1, var2, "KeyValue", var4);
            var6.toXML(var1, var2, var4 + 2);
            StreamUtils.addEnd(var1, var2, "KeyValue", var4);
         }
      }

      if (this.x509Data.size() > 0) {
         var5 = this.x509Data.iterator();

         while(var5.hasNext()) {
            X509Data var7 = (X509Data)var5.next();
            var7.toXML(var1, var2, var4);
         }
      }

      if (this.encryptedKeys.size() > 0) {
         var5 = this.encryptedKeys.iterator();

         while(var5.hasNext()) {
            EncryptedKey var8 = (EncryptedKey)var5.next();
            var8.toXML(var1, var4);
         }
      }

      if (!this.tokenRefs.isEmpty()) {
         var5 = this.tokenRefs.iterator();

         while(var5.hasNext()) {
            SecurityTokenReference var9 = (SecurityTokenReference)var5.next();
            var9.toXML(var1);
         }
      }

      StreamUtils.addEnd(var1, var2, "KeyInfo", var3);
   }

   private void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      var1.next();

      while(!var1.peek().isEndElement()) {
         String var3 = StreamUtils.getValue(var1, var2, "KeyName");
         if (var3 != null) {
            this.addKeyName(var3);
         } else {
            KeyValue var4 = (KeyValue)DSIGReader.read(var1, 5);
            if (var4 != null) {
               this.addKeyValue(var4);
            } else {
               X509Data var5 = (X509Data)DSIGReader.read(var1, 11);
               if (var5 != null) {
                  this.addX509Data(var5);
               } else {
                  EncryptedKey var6 = (EncryptedKey)XMLEncReader.read(var1, 2);
                  if (var6 != null) {
                     this.addEncryptedKey(var6);
                  } else {
                     SecurityTokenReference var7 = (SecurityTokenReference)WSSEReader.read(var1, 5);
                     if (var7 != null) {
                        this.addSecurityTokenReference(var7);
                     } else {
                        if (var1.peek().isEndElement()) {
                           break;
                        }

                        if (var1.peek().isStartElement()) {
                           var1.skipElement();
                        } else {
                           var1.skip();
                        }
                     }
                  }
               }
            }
         }
      }

      StreamUtils.closeScope(var1, var2, "KeyInfo");
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<KeyInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n  <KeyName>Carol</KeyName>\n  <KeyValue>\n    <DSAKeyValue>\n      <P>\n        3eOeAvqnEyFpW+uTSgrdj7YLjaTkpyHecKFIoLu8QZNkGTQI1ciITBH0lqfIkdCH\n        Si8fiUC3DTq3J9FsJef4YVtDF7JpUvHTOQqtq7Zgx6KC8Wxkz6rQCxOr7F0ApOYi\n        89zLRoe4MkDGe6ux0+WtyOTQoVIGNTDDUFXrUQNbLrE=\n      </P>\n      <Q>\n        hDLcFK0GO/Hz1arxOOvsgM/VLyU=\n      </Q>\n      <G>\n        nnx7hbdWozGbtnFgnbFnopfRl7XRacpkPJRGf5P2IUgVspEUSUoN6i1fDBfBg43z\n        Kt7dlEaQL7b5+JTZt3MhZNPosxsgxVuT7Ts/g5k7EnpdYv0a5hw5Bw29fjbGHfgM\n        8d2rhd2Ui0xHbk0D451nhLxVWulviOSPhzKKvXrbySA=\n      </G>\n      <Y>\n        cfYpihpAQeepbNFS4MAbQRhdXpDi5wLrwxE5hIvoYqo1L8BQVu8fY1TFAPtoae1i\n        Bg/GIJyP3iLfyuBJaDvJJLP30wBH9i/s5J3656PevpOVdTfi777Fi9Gj6y/ib2Vv\n        +OZfJkkp4L50+p5TUhPmQLJtREsgtl+tnIOyJT++G9U=\n      </Y>\n    </DSAKeyValue>\n  </KeyValue>\n  <X509Data>\n    <X509Certificate>\nMIIDUDCCAxCgAwIBAgIGAOz5IVHTMAkGByqGSM44BAMwdjELMAkGA1UEBhMCSUUx\nDzANBgNVBAgTBkR1YmxpbjEkMCIGA1UEChMbQmFsdGltb3JlIFRlY2hub2xvZ2ll\ncyBMdGQuMREwDwYDVQQLEwhYL1NlY3VyZTEdMBsGA1UEAxMUQW5vdGhlciBUcmFu\nc2llbnQgQ0EwHhcNMDIwNDAyMjM1OTUyWhcNMTIwNDAyMjI1OTQ2WjBoMQswCQYD\nVQQGEwJJRTEPMA0GA1UECBMGRHVibGluMSQwIgYDVQQKExtCYWx0aW1vcmUgVGVj\naG5vbG9naWVzIEx0ZC4xETAPBgNVBAsTCFgvU2VjdXJlMQ8wDQYDVQQDEwZNb3Jp\nZ3UwggG2MIIBKwYHKoZIzjgEATCCAR4CgYEAhIqwSieM0aAez+6H71gqCfBnwG3d\nPu7JAElb13GkwXRw9RfPh0VtIVjhDJbyKAIuzCk5r54ccRixa8TQ2vWVwodQ9eru\n7jUknAc2rVEAV5mJTrBr7UUve/X9PWsCDN6lXvFLiJp/Pi/F0cyV/CAp+jIp+rol\nTXOhUz9qEjnFYMMCFQCYS/p4gmsHgo3R89EAE/Hc0dhyWQKBgCWG5hn8DM+1rv5s\nTkJBqyZJXNzy4z974s3sAGURfBBGTpB9kFxfxNt480TxkWeDhR+39DMA5TEAsRPu\nQoB6Tgl7K2nNzRdgJjK4YkBObgX1ljWkAnnJCZSbC8Nh2VpkniV0bM79HnsS+eCf\n8bi2qOOiLSzHeOrtzO8PB0YeeTLQA4GEAAKBgH1NBJ9Az5TwY4tDE0dPYVHHABt+\nyLspnT3k9G6YWUMFhZ/+3RuqEPjnKrPfUoXTTJGIACgPU3/PkqwrPVD0JMdpOcnZ\nLHiJ/P7QRQeMwDRoBrs7genB1bDd4pSJrEUcjrkA5uRrIj2Z5fL+UuLiLGPO2rM7\nBNQRIq3QFPdX++NuozowODAOBgNVHQ8BAf8EBAMCB4AwEQYDVR0OBAoECIK7Ljjh\n+EsfMBMGA1UdIwQMMAqACIocVjBaMhJ9MAkGByqGSM44BAMDLwAwLAIUEJJCOHw8\nppxoRyz3s+Vmb4NKIfMCFDgJoZn9zh/3WoYNBURODwLvyBOy\n    </X509Certificate>\n  </X509Data>\n  <wsse:SecurityTokenReference xmlns:wsse=\"" + WSSEConstants.WSSE_URI + "\"\n" + "      Id=\"STR\">\n" + "    <wsse:Reference URI=\"#bingo\" />\n" + "  </wsse:SecurityTokenReference>" + "</KeyInfo>\n");
      KeyInfo var2 = (KeyInfo)DSIGReader.read(var1, 4);
      var2.validate();
      System.out.println(var2);
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, 0);
      var3.flush();
   }

   public String toString() {
      return "weblogic.xml.security.keyinfo.KeyInfo{keyNames=" + (this.keyNames == null ? null : "size:" + this.keyNames.size() + this.keyNames) + ", keyValues=" + (this.keyValues == null ? null : "size:" + this.keyValues.size() + this.keyValues) + ", x509Data=" + (this.x509Data == null ? null : "size:" + this.x509Data.size() + this.x509Data) + ", encryptedKeys=" + (this.encryptedKeys == null ? null : "size:" + this.encryptedKeys.size() + this.encryptedKeys) + ", tokenRefs=" + (this.tokenRefs == null ? null : "size:" + this.tokenRefs.size() + this.tokenRefs) + "}";
   }
}
