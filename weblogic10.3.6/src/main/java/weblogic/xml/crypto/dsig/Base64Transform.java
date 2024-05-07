package weblogic.xml.crypto.dsig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.util.Iterator;
import org.w3c.dom.Node;
import weblogic.utils.encoders.BASE64Decoder;
import weblogic.xml.crypto.OctetData;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.NodeSetData;
import weblogic.xml.crypto.api.OctetStreamData;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.spec.TransformParameterSpec;

public class Base64Transform extends TransformImpl implements OctetTransform, TransformFactory {
   private static final TransformFactory factory = new Base64Transform();

   protected Base64Transform() {
   }

   public String getAlgorithm() {
      return "http://www.w3.org/2000/09/xmldsig#base64";
   }

   public Data transform(Data var1, XMLCryptoContext var2) throws XMLSignatureException {
      try {
         if (var1 instanceof OctetStreamData) {
            OctetStreamData var10 = (OctetStreamData)var1;
            return this.getDecodedData(var10.getOctetStream());
         } else if (!(var1 instanceof NodeSetData)) {
            if (var1 instanceof OctetData) {
               OctetData var9 = (OctetData)var1;
               return this.getDecodedData(new ByteArrayInputStream(var9.getBytes()));
            } else {
               throw new XMLSignatureException("Data type not supported in Base64Tranform.");
            }
         } else {
            NodeSetData var3 = (NodeSetData)var1;
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            Iterator var5 = var3.iterator();

            while(true) {
               Node var6;
               do {
                  if (!var5.hasNext()) {
                     return this.getDecodedData(new ByteArrayInputStream(var4.toByteArray()));
                  }

                  var6 = (Node)var5.next();
               } while(var6.getNodeType() != 3 && var6.getNodeType() != 2);

               byte[] var7 = var6.getNodeValue().getBytes("ASCII");
               var4.write(var7);
            }
         }
      } catch (IOException var8) {
         throw new XMLSignatureException(var8);
      }
   }

   private Data getDecodedData(InputStream var1) throws IOException {
      BASE64Decoder var2 = new BASE64Decoder();
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      var2.decodeBuffer(var1, var3);
      var3.flush();
      var3.close();
      return new OctetStreamData(new ByteArrayInputStream(var3.toByteArray()));
   }

   public Transform newTransform(TransformParameterSpec var1) throws InvalidAlgorithmParameterException {
      return this;
   }

   public String getURI() {
      return "http://www.w3.org/2000/09/xmldsig#base64";
   }

   public static void init() {
      register(factory);
   }
}
