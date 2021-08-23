package org.github.hoorf.dbboot.core.router;

import org.github.hoorf.dbboot.spi.typed.TypedSPI;

public interface StandardRouter extends Router, TypedSPI {

    String INLINE = "INLINE";
    String ROUND = "ROUND";

}
