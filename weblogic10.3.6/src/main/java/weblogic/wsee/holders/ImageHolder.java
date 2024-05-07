package weblogic.wsee.holders;

import java.awt.Image;
import javax.xml.rpc.holders.Holder;

public class ImageHolder implements Holder {
   public Image value;

   public ImageHolder() {
   }

   public ImageHolder(Image var1) {
      this.value = var1;
   }
}
