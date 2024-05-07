package weblogic.xml.jaxr.registry.infomodel;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.URIValidator;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;

public class URIValidatorImpl extends BaseInfoModelObject implements URIValidator {
   private static final long serialVersionUID = -1L;
   private boolean m_validateURI;

   public URIValidatorImpl(RegistryServiceImpl var1) {
      super(var1);
      this.m_validateURI = true;
   }

   public URIValidatorImpl(URIValidator var1, RegistryServiceImpl var2) throws JAXRException {
      super(var2);
      if (var1 != null) {
         this.m_validateURI = var1.getValidateURI();
      }

   }

   public void setValidateURI(boolean var1) throws JAXRException {
      this.m_validateURI = var1;
   }

   public boolean getValidateURI() throws JAXRException {
      return this.m_validateURI;
   }

   protected void validate(String var1) throws JAXRException {
      if (this.getValidateURI()) {
         try {
            URL var2 = new URL(var1);
            if (var2.getProtocol().equalsIgnoreCase("http")) {
               String var6;
               String var14;
               String[] var16;
               try {
                  HttpURLConnection var13 = (HttpURLConnection)var2.openConnection();
                  int var15 = var13.getResponseCode();
                  int var17 = var15 / 100;
                  if (var17 != 1 && var17 != 2 && var17 != 3) {
                     String[] var7;
                     String var8;
                     if (var17 == 4) {
                        var6 = "jaxr.validation.uri.clientError";
                        var7 = new String[]{var1};
                        var8 = JAXRMessages.getMessage(var6, var7);
                        throw new InvalidRequestException(var8);
                     }

                     if (var17 == 5) {
                        var6 = "jaxr.validation.uri.serverError";
                        var7 = new String[]{var1};
                        var8 = JAXRMessages.getMessage(var6, var7);
                        throw new InvalidRequestException(var8);
                     }

                     var6 = "jaxr.validation.uri.unknownResponse";
                     var7 = new String[]{var1};
                     var8 = JAXRMessages.getMessage(var6, var7);
                     throw new InvalidRequestException(var8);
                  }
               } catch (UnknownHostException var9) {
                  var14 = "jaxr.validation.uri.unknownHostException";
                  var16 = new String[]{var1};
                  var6 = JAXRMessages.getMessage(var14, var16);
                  throw new InvalidRequestException(var6, var9);
               } catch (ConnectException var10) {
                  var14 = "jaxr.validation.uri.connectException";
                  var16 = new String[]{var1};
                  var6 = JAXRMessages.getMessage(var14, var16);
                  throw new InvalidRequestException(var6, var10);
               } catch (IOException var11) {
                  var14 = "jaxr.validation.uri.ioException";
                  var16 = new String[]{var1};
                  var6 = JAXRMessages.getMessage(var14, var16);
                  throw new InvalidRequestException(var6, var11);
               }
            }
         } catch (MalformedURLException var12) {
            String var3 = "jaxr.validation.uri.malformed";
            String[] var4 = new String[]{var1};
            String var5 = JAXRMessages.getMessage(var3, var4);
            throw new InvalidRequestException(var5, var12);
         }
      }

   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{new Boolean(this.m_validateURI)};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_validateURI"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
