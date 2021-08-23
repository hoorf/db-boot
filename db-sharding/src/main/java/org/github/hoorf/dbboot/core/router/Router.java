package org.github.hoorf.dbboot.core.router;

import java.util.Map;

public interface Router {


    String route(Map<String, Object> row);
}
