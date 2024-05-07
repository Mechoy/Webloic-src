package weblogic.xml.jaxr.registry.bridge.uddi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.client.UDDIProxy;
import weblogic.auddi.uddi.request.UDDIRequest;
import weblogic.auddi.uddi.response.UDDIResponse;
import weblogic.xml.jaxr.registry.BaseJAXRObject;
import weblogic.xml.jaxr.registry.BulkResponseImpl;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class PartialCommitHelper extends BaseJAXRObject {
   private PartialCommitHelper() {
   }

   public static BulkResponse handlePartialCommit(JaxrAPIMapper var0, Collection var1, UDDIProxy var2, RegistryServiceImpl var3) throws JAXRException {
      Object var4;
      try {
         UDDIRequest var5 = var0.getUDDIRequest(var1);

         UDDIResponse var17;
         try {
            var17 = var2.execute(var5);
         } catch (UDDIException var15) {
            throw UDDIBridgeMapperUtil.mapException(var15);
         }

         var4 = var0.getBulkResponse(var3, var17);
      } catch (RegistryException var16) {
         ArrayList var6 = new ArrayList();
         Iterator var7 = var1.iterator();

         while(var7.hasNext()) {
            try {
               Object var8 = var7.next();
               ArrayList var20 = new ArrayList();
               var20.add(var8);
               UDDIRequest var10 = var0.getUDDIRequest(var20);

               try {
                  UDDIResponse var11 = var2.execute(var10);
                  BulkResponse var12 = var0.getBulkResponse(var3, var11);
                  var6.add(var12);
               } catch (UDDIException var13) {
                  throw UDDIBridgeMapperUtil.mapException(var13);
               }
            } catch (RegistryException var14) {
               BulkResponseImpl var9 = UDDIBridgeResponseMapper.getBulkResponseFromExceptions(var14, var3);
               var6.add(var9);
            }
         }

         BulkResponseImpl var18 = UDDIBridgeResponseMapper.getBulkResponseFromCollection(Collections.EMPTY_LIST, var3);
         BulkResponse[] var19 = (BulkResponse[])((BulkResponse[])var6.toArray(new BulkResponse[0]));
         UDDIBridgeMapperUtil.accumulateBulkResponses(var18, var19);
         var4 = var18;
      }

      return (BulkResponse)var4;
   }

   abstract static class JaxrAPIMapper {
      public abstract UDDIRequest getUDDIRequest(Collection var1) throws JAXRException;

      public abstract BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException;
   }
}
