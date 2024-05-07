package weblogic.wsee.wsdl.validation;

import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.SchemaGlobalElement;
import com.bea.xml.SchemaType;
import com.bea.xml.SchemaTypeSystem;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.WsdlTypes;
import weblogic.wsee.wsdl.mime.MimeContent;
import weblogic.wsee.wsdl.mime.MimeMultipartRelated;
import weblogic.wsee.wsdl.mime.MimePart;
import weblogic.wsee.wsdl.mime.MimeXml;
import weblogic.wsee.wsdl.schema.SchemaTypeSystemGenerator;
import weblogic.wsee.wsdl.soap11.SoapBody;
import weblogic.wsee.wsdl.soap11.SoapHeader;

public class PartsAndSchemaValidator implements WsdlValidator {
   public void validateDefinitions(WsdlDefinitions var1) throws WsdlValidationException {
      WsdlTypes var2 = var1.getTypes();
      SchemaDocument[] var3 = var2.getSchemaArray();
      SchemaTypeSystemGenerator var4 = new SchemaTypeSystemGenerator();
      var4.addSchemaDocuments(var3);
      SchemaTypeSystem var5 = null;

      try {
         var5 = var4.generate();
      } catch (XmlException var15) {
         throw new WsdlValidationException("Impossible to compile the schemas", var15);
      }

      Iterator var6 = var1.getMessages().values().iterator();

      Iterator var8;
      while(var6.hasNext()) {
         WsdlMessage var7 = (WsdlMessage)var6.next();
         var8 = var7.getParts().values().iterator();

         while(var8.hasNext()) {
            WsdlPart var9 = (WsdlPart)var8.next();
            boolean var10 = false;
            QName var11 = var9.getType();
            QName var12 = var9.getElement();
            if (var11 != null) {
               SchemaType var20 = var5.findType(var11);
               SchemaType var21 = XmlBeans.getBuiltinTypeSystem().findType(var11);
               if (var20 != null || var21 != null) {
                  var10 = true;
               }
            } else {
               if (var12 == null) {
                  throw new WsdlValidationException("No element or type specified for part'" + var9.getName() + "' in message '" + var7.getName() + "'");
               }

               SchemaGlobalElement var13 = var5.findElement(var12);
               SchemaGlobalElement var14 = XmlBeans.getBuiltinTypeSystem().findElement(var12);
               if (var13 != null || var14 != null) {
                  var10 = true;
               }
            }

            if (!var10) {
               throw new WsdlValidationException("The element or type specified for part'" + var9.getName() + "' in message '" + var7.getName() + "' cannot be found in any schemas referenced by this wsdl");
            }
         }
      }

      var6 = var1.getBindings().values().iterator();

      while(var6.hasNext()) {
         WsdlBinding var16 = (WsdlBinding)var6.next();
         var8 = var16.getOperations().values().iterator();

         while(var8.hasNext()) {
            WsdlBindingOperation var17 = (WsdlBindingOperation)var8.next();
            WsdlBindingMessage var18 = var17.getInput();
            WsdlBindingMessage var19 = var17.getOutput();
            if (var18 != null) {
               this.validateSoapHeaderMessage(var18, var1);
            }

            if (var19 != null) {
               this.validateSoapHeaderMessage(var19, var1);
            }
         }
      }

   }

   private void validateSoapHeaderMessage(WsdlBindingMessage var1, WsdlDefinitions var2) throws WsdlValidationException {
      SoapHeader var3 = SoapHeader.narrow(var1);
      if (var3 != null) {
         this.validateSoapHeader(var3, var2);
      }

      MimeMultipartRelated var4 = MimeMultipartRelated.narrow(var1);
      if (var4 != null) {
         List var5 = var4.getParts();
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            MimePart var7 = (MimePart)var6.next();
            SoapHeader var8 = SoapHeader.narrow(var7);
            if (var8 != null) {
               this.validateSoapHeader(var8, var2);
            }
         }
      }

   }

   private void validateSoapHeader(SoapHeader var1, WsdlDefinitions var2) throws WsdlValidationException {
      QName var3 = var1.getMessage();
      if (var3 == null) {
         throw new WsdlValidationException("The message attribute in the soap:header element must be specified");
      } else {
         WsdlMessage var4 = (WsdlMessage)var2.getMessages().get(var3);
         if (var4 == null) {
            throw new WsdlValidationException("The message '" + var3 + "' specified in the soap:header cannot be found in the wsdl");
         } else {
            String var5 = var1.getPart();
            WsdlPart var6 = (WsdlPart)var4.getParts().get(var5);
            if (var6 == null) {
               throw new WsdlValidationException("The part '" + var5 + "' specified in the soap-header for the message '" + var3 + "' cannot be found");
            }
         }
      }
   }

   public void validateService(WsdlService var1) throws WsdlValidationException {
   }

   public void validatePort(WsdlPort var1) throws WsdlValidationException {
   }

   public void validateBinding(WsdlBinding var1) throws WsdlValidationException {
   }

   public void validateBindingOperation(WsdlBindingOperation var1) throws WsdlValidationException {
   }

   public void validateBindingMessage(WsdlBindingMessage var1) throws WsdlValidationException {
      MimeMultipartRelated var2 = MimeMultipartRelated.narrow(var1);
      SoapBody var3 = SoapBody.narrow(var1);
      MimeXml var4 = MimeXml.narrow(var1);
      List var5 = MimeContent.narrow(var1);
      if (var5 != null) {
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            MimeContent var7 = (MimeContent)var6.next();
            this.validateMimeContent(var1, var7);
         }
      }

      if (var4 != null) {
         this.validateMimeXml(var1, var4);
      }

      if (var3 != null) {
         this.validateSoapBody(var1, var3);
      }

      if (var2 != null) {
         this.validateMultipartRelated(var1, var2);
      }

   }

   private void validateMultipartRelated(WsdlBindingMessage var1, MimeMultipartRelated var2) throws WsdlValidationException {
      List var3 = var2.getParts();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         MimePart var5 = (MimePart)var4.next();
         this.validateMimePart(var1, var5);
      }

   }

   private void validateMimePart(WsdlBindingMessage var1, MimePart var2) throws WsdlValidationException {
      List var3 = MimeContent.narrow(var2);
      MimeXml var4 = MimeXml.narrow(var2);
      SoapBody var5 = SoapBody.narrow(var2);
      if (var3 != null) {
         Iterator var6 = var3.iterator();

         while(var6.hasNext()) {
            MimeContent var7 = (MimeContent)var6.next();
            this.validateMimeContent(var1, var7);
         }
      }

      if (var4 != null) {
         this.validateMimeXml(var1, var4);
      }

      if (var5 != null) {
         this.validateSoapBody(var1, var5);
      }

   }

   private void validateSoapBody(WsdlBindingMessage var1, SoapBody var2) throws WsdlValidationException {
      String var3 = var2.getParts();
      if (var3 != null && !var3.equals("")) {
         String[] var4 = var3.split(" ");
         String[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = var5[var7];
            this.validateMessagePartName(var1, var8);
         }

      }
   }

   private void validateMimeXml(WsdlBindingMessage var1, MimeXml var2) throws WsdlValidationException {
      String var3 = var2.getPart();
      if (var3 != null && !var3.equals("")) {
         this.validateMessagePartName(var1, var3);
      }

   }

   private void validateMimeContent(WsdlBindingMessage var1, MimeContent var2) throws WsdlValidationException {
      String var3 = var2.getPart();
      if (var3 != null && !var3.equals("")) {
         this.validateMessagePartName(var1, var3);
      }

   }

   private void validateMessagePartName(WsdlBindingMessage var1, String var2) throws WsdlValidationException {
      WsdlMessage var3 = null;

      try {
         var3 = var1.getMessage();
      } catch (Exception var5) {
         throw new WsdlValidationException("The message in the operation '" + var1.getBindingOperation().getName() + "' cannot be found");
      }

      WsdlPart var4 = (WsdlPart)var3.getParts().get(var2);
      if (var4 == null) {
         throw new WsdlValidationException("The part '" + var2 + "' specified in the binding '" + var1.getBindingOperation().getName() + "' for the message '" + var3.getName() + "' cannot be found");
      }
   }

   public void validatePortType(WsdlPortType var1) throws WsdlValidationException {
   }

   public void validateOperation(WsdlOperation var1) throws WsdlValidationException {
   }

   public void validateMessage(WsdlMessage var1) throws WsdlValidationException {
   }

   public void validatePart(WsdlPart var1) throws WsdlValidationException {
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
