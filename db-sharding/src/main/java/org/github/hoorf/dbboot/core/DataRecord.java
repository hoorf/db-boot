package org.github.hoorf.dbboot.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class DataRecord {

    public final static Integer TYPE_RECORD = 0;

    private int type;

    private Map<String, Object> data;

    public DataRecord(Map<String, Object> data) {
        this.data = data;
    }
}
