package org.app.repo.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;


public abstract class JdbcUtils {
    public static Class<? extends Object> toClass(int type) {
        Class<? extends Object> result = Object.class;
        switch( type ) {
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
          result = String.class;
          break;

        case Types.NUMERIC:
        case Types.DECIMAL:
          result = BigDecimal.class;
          break;

        case Types.BIT:
          result = Boolean.class;
          break;

        case Types.TINYINT:
          result = Byte.class;
          break;

        case Types.SMALLINT:
          result = Short.class;
          break;

        case Types.INTEGER:
          result = Integer.class;
          break;

        case Types.BIGINT:
          result = Long.class;
          break;

        case Types.FLOAT:
        case Types.DOUBLE:
          result = Double.class;
          break;

        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
          result = Byte[].class;
          break;

        case Types.DATE:
          result = Date.class;
          break;

        case Types.TIME:
          result = Time.class;
          break;

        case Types.TIMESTAMP:
          result = Timestamp.class;
          break;
      }

      return result;

    }
}
