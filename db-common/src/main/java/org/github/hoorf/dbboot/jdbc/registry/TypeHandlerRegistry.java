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
package org.github.hoorf.dbboot.jdbc.registry;

import org.github.hoorf.dbboot.jdbc.TypeHandler;
import org.github.hoorf.dbboot.jdbc.handler.*;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.JDBCType;
import java.time.*;
import java.util.*;


public final class TypeHandlerRegistry {

    private final static Map<JDBCType, TypeHandler<?>> JDBC_TYPE_HANDLER_MAP = new EnumMap<JDBCType, TypeHandler<?>>(JDBCType.class);
    private final static Map<Type, Map<JDBCType, TypeHandler<?>>> TYPE_HANDLER_MAP = new HashMap<Type, Map<JDBCType, TypeHandler<?>>>();
    private final static Map<Class<?>, TypeHandler<?>> ALL_TYPE_HANDLERS_MAP = new HashMap<Class<?>, TypeHandler<?>>();
    private final static UnknownTypeHandler UNKNOWN_TYPE_HANDLER = new UnknownTypeHandler();

    static {
        register(Boolean.class, new BooleanTypeHandler());
        register(boolean.class, new BooleanTypeHandler());
        register(JDBCType.BOOLEAN, new BooleanTypeHandler());
        register(JDBCType.BIT, new BooleanTypeHandler());

        register(Byte.class, new ByteTypeHandler());
        register(byte.class, new ByteTypeHandler());
        register(JDBCType.TINYINT, new ByteTypeHandler());

        register(Short.class, new ShortTypeHandler());
        register(short.class, new ShortTypeHandler());
        register(JDBCType.SMALLINT, new ShortTypeHandler());

        register(Integer.class, new IntegerTypeHandler());
        register(int.class, new IntegerTypeHandler());
        register(JDBCType.INTEGER, new IntegerTypeHandler());

        register(Long.class, new LongTypeHandler());
        register(long.class, new LongTypeHandler());

        register(Float.class, new FloatTypeHandler());
        register(float.class, new FloatTypeHandler());
        register(JDBCType.FLOAT, new FloatTypeHandler());

        register(Double.class, new DoubleTypeHandler());
        register(double.class, new DoubleTypeHandler());
        register(JDBCType.DOUBLE, new DoubleTypeHandler());

        register(Reader.class, new ClobReaderTypeHandler());
        register(String.class, new StringTypeHandler());
        register(String.class, JDBCType.CHAR, new StringTypeHandler());
        register(String.class, JDBCType.CLOB, new ClobTypeHandler());
        register(String.class, JDBCType.VARCHAR, new StringTypeHandler());
        register(String.class, JDBCType.LONGVARCHAR, new ClobTypeHandler());
        register(String.class, JDBCType.NVARCHAR, new NStringTypeHandler());
        register(String.class, JDBCType.NCHAR, new NStringTypeHandler());
        register(String.class, JDBCType.NCLOB, new NClobTypeHandler());
        register(JDBCType.CHAR, new StringTypeHandler());
        register(JDBCType.VARCHAR, new StringTypeHandler());
        register(JDBCType.CLOB, new ClobTypeHandler());
        register(JDBCType.LONGVARCHAR, new ClobTypeHandler());
        register(JDBCType.NVARCHAR, new NStringTypeHandler());
        register(JDBCType.NCHAR, new NStringTypeHandler());
        register(JDBCType.NCLOB, new NClobTypeHandler());

        register(Object.class, JDBCType.ARRAY, new ArrayTypeHandler());
        register(JDBCType.ARRAY, new ArrayTypeHandler());

        register(BigInteger.class, new BigIntegerTypeHandler());
        register(JDBCType.BIGINT, new LongTypeHandler());

        register(BigDecimal.class, new BigDecimalTypeHandler());
        register(JDBCType.REAL, new BigDecimalTypeHandler());
        register(JDBCType.DECIMAL, new BigDecimalTypeHandler());
        register(JDBCType.NUMERIC, new BigDecimalTypeHandler());

        register(InputStream.class, new BlobInputStreamTypeHandler());
        register(Byte[].class, new ByteObjectArrayTypeHandler());
        register(Byte[].class, JDBCType.BLOB, new BlobByteObjectArrayTypeHandler());
        register(Byte[].class, JDBCType.LONGVARBINARY, new BlobByteObjectArrayTypeHandler());
        register(byte[].class, new ByteArrayTypeHandler());
        register(byte[].class, JDBCType.BLOB, new BlobTypeHandler());
        register(byte[].class, JDBCType.LONGVARBINARY, new BlobTypeHandler());
        register(JDBCType.LONGVARBINARY, new BlobTypeHandler());
        register(JDBCType.BLOB, new BlobTypeHandler());

        register(Object.class, UNKNOWN_TYPE_HANDLER);
        register(Object.class, JDBCType.OTHER, UNKNOWN_TYPE_HANDLER);
        register(JDBCType.OTHER, UNKNOWN_TYPE_HANDLER);

        register(Date.class, new DateTypeHandler());
        register(Date.class, JDBCType.DATE, new DateOnlyTypeHandler());
        register(Date.class, JDBCType.TIME, new TimeOnlyTypeHandler());
        register(JDBCType.TIMESTAMP, new DateTypeHandler());
        register(JDBCType.DATE, new DateOnlyTypeHandler());
        register(JDBCType.TIME, new TimeOnlyTypeHandler());

        register(java.sql.Date.class, new SqlDateTypeHandler());
        register(java.sql.Time.class, new SqlTimeTypeHandler());
        register(java.sql.Timestamp.class, new SqlTimestampTypeHandler());

        register(String.class, JDBCType.SQLXML, new SqlxmlTypeHandler());

        register(Instant.class, new InstantTypeHandler());
        register(LocalDateTime.class, new LocalDateTimeTypeHandler());
        register(LocalDate.class, new LocalDateTypeHandler());
        register(LocalTime.class, new LocalTimeTypeHandler());
        register(OffsetDateTime.class, new OffsetDateTimeTypeHandler());
        register(OffsetTime.class, new OffsetTimeTypeHandler());
        register(ZonedDateTime.class, new ZonedDateTimeTypeHandler());
        register(Month.class, new MonthTypeHandler());
        register(Year.class, new YearTypeHandler());
        register(YearMonth.class, new YearMonthTypeHandler());

        register(Character.class, new CharacterTypeHandler());
        register(char.class, new CharacterTypeHandler());


    }

    public static void register(JDBCType jdbcType, TypeHandler<?> handler) {
        JDBC_TYPE_HANDLER_MAP.put(jdbcType, handler);
    }

    public static <T> void register(Class<T> javaType, TypeHandler<? extends T> typeHandler) {
        register((Type) javaType, typeHandler);
    }

    private static <T> void register(Type javaType, TypeHandler<? extends T> typeHandler) {
        register(javaType, null, typeHandler);
    }

    public static <T> void register(Class<T> type, JDBCType jdbcType, TypeHandler<? extends T> handler) {
        register((Type) type, jdbcType, handler);
    }

    private static void register(Type javaType, JDBCType jdbcType, TypeHandler<?> handler) {
        if (javaType != null) {
            Map<JDBCType, TypeHandler<?>> map = TYPE_HANDLER_MAP.get(javaType);
            if (map == null) {
                map = new HashMap<JDBCType, TypeHandler<?>>();
                TYPE_HANDLER_MAP.put(javaType, map);
            }
            map.put(jdbcType, handler);
        }
        ALL_TYPE_HANDLERS_MAP.put(handler.getClass(), handler);
    }

    public static void register(String javaTypeClassName, String typeHandlerClassName) throws ClassNotFoundException {
        register(Class.forName(javaTypeClassName), Class.forName(typeHandlerClassName));
    }

    public static void register(Class<?> javaTypeClass, Class<?> typeHandlerClass) {
        register(javaTypeClass, getInstance(javaTypeClass, typeHandlerClass));
    }

    @SuppressWarnings("unchecked")
    public static <T> TypeHandler<T> getInstance(Class<?> javaTypeClass, Class<?> typeHandlerClass) {
        if (javaTypeClass != null) {
            try {
                Constructor<?> c = typeHandlerClass.getConstructor(Class.class);
                return (TypeHandler<T>) c.newInstance(javaTypeClass);
            } catch (NoSuchMethodException ignored) {
                // ignored
            } catch (Exception e) {
                throw new RuntimeException("Failed invoking constructor for handler " + typeHandlerClass, e);
            }
        }
        try {
            Constructor<?> c = typeHandlerClass.getConstructor();
            return (TypeHandler<T>) c.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to find a usable constructor for " + typeHandlerClass, e);
        }
    }

    public static boolean hasTypeHandler(Class<?> javaType) {
        return hasTypeHandler(javaType, null);
    }


    public static boolean hasTypeHandler(Class<?> javaType, JDBCType jdbcType) {
        return javaType != null && getTypeHandler((Type) javaType, jdbcType) != null;
    }


    public static TypeHandler<?> getMappingTypeHandler(Class<? extends TypeHandler<?>> handlerType) {
        return ALL_TYPE_HANDLERS_MAP.get(handlerType);
    }

    public static  <T> TypeHandler<T> getTypeHandler(Class<T> type) {
        return getTypeHandler((Type) type, null);
    }


    // java type + handler

    public static TypeHandler<?> getTypeHandler(JDBCType jdbcType) {
        return JDBC_TYPE_HANDLER_MAP.get(jdbcType);
    }

    public static <T> TypeHandler<T> getTypeHandler(Class<T> type, JDBCType jdbcType) {
        return getTypeHandler((Type) type, jdbcType);
    }


    // java type + jdbc type + handler

    @SuppressWarnings("unchecked")
    private static  <T> TypeHandler<T> getTypeHandler(Type type, JDBCType jdbcType) {
        Map<JDBCType, TypeHandler<?>> jdbcHandlerMap = TYPE_HANDLER_MAP.get(type);
        TypeHandler<?> handler = null;
        if (jdbcHandlerMap != null) {
            handler = jdbcHandlerMap.get(jdbcType);
            if (handler == null) {
                handler = jdbcHandlerMap.get(null);
            }
            if (handler == null) {
                // #591
                handler = pickSoleHandler(jdbcHandlerMap);
            }
        }
        if (handler == null && type != null && type instanceof Class && Enum.class.isAssignableFrom((Class<?>) type)) {
            handler = new EnumTypeHandler((Class<?>) type);
        }
        // type drives generics here
        return (TypeHandler<T>) handler;
    }

    private static TypeHandler<?> pickSoleHandler(Map<JDBCType, TypeHandler<?>> jdbcHandlerMap) {
        TypeHandler<?> soleHandler = null;
        for (TypeHandler<?> handler : jdbcHandlerMap.values()) {
            if (soleHandler == null) {
                soleHandler = handler;
            } else if (!handler.getClass().equals(soleHandler.getClass())) {
                // More than one type handlers registered.
                return null;
            }
        }
        return soleHandler;
    }


    public static TypeHandler<Object> getUnknownTypeHandler() {
        return UNKNOWN_TYPE_HANDLER;
    }


    public static void register(Class<?> javaTypeClass, JDBCType jdbcType, Class<?> typeHandlerClass) {
        register(javaTypeClass, jdbcType, getInstance(javaTypeClass, typeHandlerClass));
    }

    public static Collection<TypeHandler<?>> getTypeHandlers() {
        return Collections.unmodifiableCollection(ALL_TYPE_HANDLERS_MAP.values());
    }

}
