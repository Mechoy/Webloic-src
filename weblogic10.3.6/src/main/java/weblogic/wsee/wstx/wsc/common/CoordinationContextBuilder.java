package weblogic.wsee.wstx.wsc.common;

import com.sun.xml.bind.api.JAXBRIContext;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.Transactional.Version;
import weblogic.wsee.wstx.wsc.common.types.CoordinationContextIF;
import weblogic.wsee.wstx.wsc.v10.CoordinationContextBuilderImpl;

public abstract class CoordinationContextBuilder {
   protected String coordinationType;
   protected String identifier;
   protected long expires;
   protected String address;
   protected String txId;
   protected boolean mustUnderstand;
   protected SOAPVersion soapVersion;
   protected Header coordinationHeader;
   Transactional.Version version;

   public static CoordinationContextBuilder newInstance(Transactional.Version var0) {
      if (Version.WSAT10 == var0) {
         return new CoordinationContextBuilderImpl();
      } else if (Version.WSAT11 != var0 && Version.WSAT12 != var0) {
         throw new IllegalArgumentException(var0 + "is not a supported ws-at version");
      } else {
         return new weblogic.wsee.wstx.wsc.v11.CoordinationContextBuilderImpl();
      }
   }

   public static CoordinationContextBuilder headers(HeaderList var0, Transactional.Version var1) {
      Object var2 = null;

      for(int var3 = 0; var3 < var0.size(); ++var3) {
         Header var4 = var0.get(var3);
         if (var4.getLocalPart().equals("CoordinationContext")) {
            if ("http://schemas.xmlsoap.org/ws/2004/10/wscoor".equals(var4.getNamespaceURI())) {
               if (var1 == Version.WSAT10 || var1 == Version.DEFAULT) {
                  var2 = new CoordinationContextBuilderImpl();
                  ((CoordinationContextBuilder)var2).version = Version.WSAT10;
               }
            } else if ("http://docs.oasis-open.org/ws-tx/wscoor/2006/06".equals(var4.getNamespaceURI()) && var1 != Version.WSAT10) {
               var2 = new weblogic.wsee.wstx.wsc.v11.CoordinationContextBuilderImpl();
               ((CoordinationContextBuilder)var2).version = Version.WSAT11;
            }

            if (var2 != null) {
               var0.understood(var3);
               return ((CoordinationContextBuilder)var2).header(var4);
            }
         }
      }

      return null;
   }

   public Transactional.Version getVersion() {
      return this.version;
   }

   public CoordinationContextBuilder address(String var1) {
      this.address = var1;
      return this;
   }

   public CoordinationContextBuilder txId(String var1) {
      this.txId = var1;
      return this;
   }

   public CoordinationContextBuilder identifier(String var1) {
      this.identifier = var1;
      return this;
   }

   public CoordinationContextBuilder expires(long var1) {
      this.expires = var1;
      return this;
   }

   public CoordinationContextBuilder mustUnderstand(boolean var1) {
      this.mustUnderstand = var1;
      return this;
   }

   public CoordinationContextBuilder soapVersion(SOAPVersion var1) {
      this.soapVersion = var1;
      return this;
   }

   public CoordinationContextBuilder coordinationType(String var1) {
      this.coordinationType = var1;
      return this;
   }

   CoordinationContextBuilder header(Header var1) {
      this.coordinationHeader = var1;
      return this;
   }

   public CoordinationContextIF buildFromHeader() {
      return this._fromHeader(this.coordinationHeader);
   }

   protected abstract CoordinationContextIF _fromHeader(Header var1);

   public abstract CoordinationContextIF build();

   public abstract JAXBRIContext getJAXBRIContext();
}
