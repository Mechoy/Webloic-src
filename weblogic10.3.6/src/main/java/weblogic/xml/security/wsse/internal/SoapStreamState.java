package weblogic.xml.security.wsse.internal;

import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;

public class SoapStreamState {
   public static final int PRE_HEADER = 0;
   public static final int IN_HEADER = 1;
   public static final int BETWEEN = 2;
   public static final int IN_BODY = 3;
   public static final int POST_BODY = 4;
   private static final int MESSAGE_LEVEL = 1;
   private static final int PART_LEVEL = 2;
   private static final int TYPE_LEVEL = 3;
   static final String SOAP_HEADER = "Header";
   static final String SOAP_BODY = "Body";
   private int state = 0;
   private int depth = 0;

   public boolean update(XMLEvent var1) {
      if (var1.isStartElement()) {
         return this.update((StartElement)var1);
      } else {
         return var1.isEndElement() ? this.update((EndElement)var1) : false;
      }
   }

   public boolean update(StartElement var1) {
      ++this.depth;
      int var2 = this.state;
      XMLName var3;
      switch (this.state) {
         case 0:
            if (this.depth == 2) {
               var3 = var1.getName();
               if ("Header".equals(var3.getLocalName()) && this.validSoapNamespace(var3.getNamespaceUri())) {
                  this.state = 1;
               } else if ("Body".equals(var3.getLocalName())) {
                  this.state = 3;
               }
            }
         case 1:
         case 3:
         case 4:
         default:
            break;
         case 2:
            if (this.depth == 2) {
               var3 = var1.getName();
               if ("Body".equals(var3.getLocalName())) {
                  this.state = 3;
               }
            }
      }

      return this.state != var2;
   }

   public boolean update(EndElement var1) {
      int var2 = this.state;
      switch (this.state) {
         case 0:
         case 2:
         case 4:
         default:
            break;
         case 1:
            if (this.depth == 2) {
               this.state = 2;
            }
            break;
         case 3:
            if (this.depth == 2) {
               this.state = 4;
            }
      }

      --this.depth;
      return this.state != var2;
   }

   boolean validSoapNamespace(String var1) {
      return true;
   }

   public String getState() {
      switch (this.state) {
         case 0:
            return "PreHeader";
         case 1:
            return "InHeader";
         case 2:
            return "Between";
         case 3:
            return "InBody";
         case 4:
            return "PostBody";
         default:
            return "unknown";
      }
   }

   public String toString() {
      return "StreamState(" + this.getState() + ", " + this.depth + ")";
   }

   public boolean inState(int var1) {
      return this.state == var1;
   }

   public boolean inHeader() {
      return this.inState(1);
   }

   public boolean inBody() {
      return this.inState(3);
   }

   public boolean atLevel(int var1) {
      return this.depth == var1;
   }

   public boolean atTypeLevel() {
      return this.atLevel(3);
   }

   public boolean atPartLevel() {
      return this.atLevel(2);
   }

   public boolean after(int var1) {
      return this.state > var1;
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">    <soap:Header>      <wsrp:path soap:actor=\"http://schemas.xmlsoap.org/soap/actor/next\" soap:mustUnderstand=\"1\" xmlns:wsrp=\"http://schemas.xmlsoap.org/rp\">        <wsrp:action xmlns:wsu=\"http://schemas.xmlsoap.org/ws/2002/07/utility\" wsu:Id=\"Id-6eaab782-357a-43e4-984e-f54c3f593370\">http://microsoft.com/wsdk/samples/SumService/AddInt</wsrp:action>        <wsrp:to xmlns:wsu=\"http://schemas.xmlsoap.org/ws/2002/07/utility\" wsu:Id=\"Id-f8fcb28f-067d-4b60-8a79-8ee92eb2dd9f\">http://localhost/wsdkquickstart/usernamesigning/service/usernamesigning.asmx</wsrp:to>        <wsrp:id xmlns:wsu=\"http://schemas.xmlsoap.org/ws/2002/07/utility\" wsu:Id=\"Id-ce040a96-bd50-41fa-8166-124679c979e8\">uuid:4e76bcac-5307-4519-9e51-3cbe9c58ceb4</wsrp:id>      </wsrp:path>      <wsu:Timestamp xmlns:wsu=\"http://schemas.xmlsoap.org/ws/2002/07/utility\">        <wsu:Created wsu:Id=\"Id-e9f86751-e899-41e3-a797-17a1d6150888\">2002-09-26T16:13:15Z</wsu:Created>        <wsu:Expires wsu:Id=\"Id-745e1094-9bce-4bf6-8bf1-33b23ace87c6\">2002-09-26T16:14:15Z</wsu:Expires>      </wsu:Timestamp>      <wsse:Security soap:mustUnderstand=\"1\" xmlns:wsse=\"http://schemas.xmlsoap.org/ws/2002/07/secext\">        <wsse:UsernameToken xmlns:wsu=\"http://schemas.xmlsoap.org/ws/2002/07/utility\" wsu:Id=\"SecurityToken-82aa7b1e-db82-40b5-9944-d66bb767d9e5\">          <wsse:Username>billjg</wsse:Username>          <wsse:Password Type=\"wsse:PasswordDigest\">w94sA/jK11Td4HisIEAFTqMd/+w=</wsse:Password>          <wsse:Nonce>fgx3k755iWAdW0GFzDd4dw==</wsse:Nonce>          <wsu:Created>2002-09-26T16:13:15Z</wsu:Created>        </wsse:UsernameToken>        <Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">          <SignedInfo>            <CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />            <SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#hmac-sha1\" />            <Reference URI=\"#Id-ff2b91ec-6d8e-42ec-8a3b-01885b4ebbd2\">              <Transforms>                <Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />              </Transforms>              <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" />              <DigestValue>4fazWvZQTspNRY2Z1GHrTFS7eYQ=</DigestValue>            </Reference>            <Reference URI=\"#Id-6eaab782-357a-43e4-984e-f54c3f593370\">              <Transforms>                <Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />              </Transforms>              <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" />              <DigestValue>zFHn8/tx1goUjatsBto0imZEiYI=</DigestValue>            </Reference>            <Reference URI=\"#Id-f8fcb28f-067d-4b60-8a79-8ee92eb2dd9f\">              <Transforms>                <Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />              </Transforms>              <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" />              <DigestValue>YomslOQ7yRNnpDcpOkKCcoNVSWc=</DigestValue>            </Reference>            <Reference URI=\"#Id-ce040a96-bd50-41fa-8166-124679c979e8\">              <Transforms>                <Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />              </Transforms>              <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" />              <DigestValue>odkVpMjtM5xtJOIeIIXEiJHFZ+Q=</DigestValue>            </Reference>            <Reference URI=\"#Id-e9f86751-e899-41e3-a797-17a1d6150888\">              <Transforms>                <Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />              </Transforms>              <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" />              <DigestValue>w6BODXClETlLqdQ/I/U/LQKVTs4=</DigestValue>            </Reference>            <Reference URI=\"#Id-745e1094-9bce-4bf6-8bf1-33b23ace87c6\">              <Transforms>                <Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />              </Transforms>              <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" />              <DigestValue>5Z/IOnvyc7VxdC77uU7HSPRo8w4=</DigestValue>            </Reference>          </SignedInfo>          <SignatureValue>GnnGXrvG02TinOECHg4wO2A+ha0=</SignatureValue>          <KeyInfo>            <wsse:SecurityTokenReference>              <wsse:Reference URI=\"#SecurityToken-82aa7b1e-db82-40b5-9944-d66bb767d9e5\" />            </wsse:SecurityTokenReference>          </KeyInfo>        </Signature>      </wsse:Security>    </soap:Header>    <soap:Body xmlns:wsu=\"http://schemas.xmlsoap.org/ws/2002/07/utility\" wsu:Id=\"Id-ff2b91ec-6d8e-42ec-8a3b-01885b4ebbd2\">      <AddInt xmlns=\"http://microsoft.com/wsdk/samples/SumService\">        <a>9</a>        <b>10</b>      </AddInt>    </soap:Body>  </soap:Envelope>");
      SoapStreamState var2 = new SoapStreamState();
      Object var3 = null;

      while(var1.hasNext()) {
         XMLEvent var4 = var1.peek();
         if (!var4.isStartElement()) {
            if (var4.isEndElement()) {
               var2.update(var1.next());
               if (var2.after(1)) {
                  System.out.println("done with header at " + var4);
                  break;
               }
            } else {
               var1.next();
            }
         } else {
            StartElement var5 = (StartElement)var4;
            var2.update(var5);
            if (var2.inHeader() && var2.atTypeLevel()) {
               label60: {
                  label47: {
                     XMLName var6 = var5.getName();
                     String var7 = var6.getNamespaceUri();
                     if ("Security".equals(var6.getLocalName())) {
                        String var8 = StreamUtils.getAttribute(var5, "Role");
                        if (var3 == null) {
                           if (var8 == null) {
                              break label47;
                           }
                        } else if (var8.equals(var3)) {
                           break label47;
                        }
                     }

                     System.out.println("got type " + var5);
                     break label60;
                  }

                  System.out.println("HIT!");
                  break;
               }
            }

            var1.next();
         }
      }

   }
}
