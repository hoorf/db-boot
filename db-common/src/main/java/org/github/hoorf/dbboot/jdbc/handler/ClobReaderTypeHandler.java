/**
 * Copyright 2009-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.github.hoorf.dbboot.jdbc.handler;


import java.io.Reader;
import java.sql.*;


public class ClobReaderTypeHandler extends BaseTypeHandler<Reader> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Reader parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setClob(i, parameter);
    }


    @Override
    public Reader getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return toReader(rs.getClob(columnName));
    }


    @Override
    public Reader getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return toReader(rs.getClob(columnIndex));
    }


    @Override
    public Reader getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return toReader(cs.getClob(columnIndex));
    }

    private Reader toReader(Clob clob) throws SQLException {
        if (clob == null) {
            return null;
        } else {
            return clob.getCharacterStream();
        }
    }

}
