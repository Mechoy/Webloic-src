package weblogic.xml.jaxr.registry;

import java.util.Collection;
import java.util.Collections;
import javax.xml.registry.BulkResponse;

public class BulkResponseImpl extends JAXRResponseImpl implements BulkResponse {
   private static final long serialVersionUID = -1L;
   private Collection m_data;
   private Collection m_exceptions;
   private boolean m_isPartial;

   public BulkResponseImpl(RegistryServiceImpl var1) {
      super(var1);
      this.setResponse(Collections.EMPTY_LIST, (Collection)null, false);
   }

   public Collection getCollection() {
      this.handleAsynchronous();
      return this.m_data;
   }

   public Collection getExceptions() {
      this.handleAsynchronous();
      return this.m_exceptions;
   }

   public boolean isPartialResponse() {
      return this.m_isPartial;
   }

   public void setResponse(Collection var1, Collection var2, boolean var3) {
      this.m_data = var1;
      this.m_exceptions = var2;
      this.m_isPartial = var3;
      byte var4;
      if (var2 != null && var2.size() > 0) {
         var4 = 2;
      } else if (var1 != null) {
         var4 = 0;
      } else {
         var4 = 3;
      }

      this.setStatus(var4);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_data, this.m_exceptions, new Boolean(this.m_isPartial)};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_data", "m_exceptions", "m_isPartial"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }

   private void handleAsynchronous() {
      if (!this.isAvailable()) {
         synchronized(this) {
            try {
               this.wait();
            } catch (InterruptedException var4) {
               this.getLogger().debug("Unexpected interruption of thread wait");
               this.getLogger().debug((Throwable)var4);
            }
         }
      }

   }
}
