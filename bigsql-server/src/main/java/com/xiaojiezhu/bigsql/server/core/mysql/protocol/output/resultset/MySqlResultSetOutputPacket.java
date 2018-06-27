package com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.resultset;

import com.xiaojiezhu.bigsql.model.constant.ColumnFlag;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.model.constant.StatusFlag;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.MultipartOutputPacket;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.MySqlEofOutputPacket;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlOkOutputPacket;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlOutputPacket;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaojie.zhu<br>
 * 时间 2018/5/2 17:10
 * 说明 ...
 */
public class MySqlResultSetOutputPacket extends MultipartOutputPacket {
    private List<MySqlOutputPacket> packets = new ArrayList<>();

    public MySqlResultSetOutputPacket() {
        super(-1);
    }

    public void addPacket(MySqlOutputPacket mySqlOutputPacket){
        this.packets.add(mySqlOutputPacket);
    }

    @Override
    public List<MySqlOutputPacket> getAll() {
        return packets;
    }

    /**
     * create a MySqlResultSetOutputPacket by ResultSet
     * @param resultSet jdbc ResultSet
     * @return
     * @throws SQLException
     */
    public static MySqlResultSetOutputPacket create(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        MySqlResultSetOutputPacket result = new MySqlResultSetOutputPacket();
        int sequenceId = 0;
        int columnCount = resultSet.getMetaData().getColumnCount();
        if(columnCount == 0){
            result.addPacket(new MySqlOkOutputPacket(++sequenceId));
            return result;
        }else{
            //header packet
            result.addPacket(new MySqlResultSetHeaderOutputPacket(++sequenceId,columnCount));

            //fields
            for(int i = 1 ; i <= columnCount ; i++){
                MySqlColumnDefinitionOutputPacket defineColumn = new MySqlColumnDefinitionOutputPacket(++sequenceId);
                defineColumn.setColumnFlag(ColumnFlag.SET_FLAG);
                defineColumn.setColumnType(ColumnType.valueOf(metaData.getColumnTypeName(i)));
                defineColumn.setDatabaseName(metaData.getCatalogName(i));
                defineColumn.setFieldLength(metaData.getColumnDisplaySize(i));
                defineColumn.setFieldName(metaData.getColumnLabel(i));
                defineColumn.setPrecision(metaData.getPrecision(i));
                defineColumn.setRealFieldName(metaData.getColumnName(i));
                defineColumn.setRealTableName(metaData.getTableName(i));
                defineColumn.setTableName(metaData.getTableName(i));
                result.addPacket(defineColumn);
            }

            //eof
            result.addPacket(new MySqlEofOutputPacket(++sequenceId,0, StatusFlag.SERVER_STATUS_AUTOCOMMIT));

            //row data
            while (resultSet.next()){
                Object[] objects = new Object[columnCount];
                for(int i = 1 ; i <= columnCount ; i++){
                    objects[i-1] = resultSet.getObject(i);
                }
                MySqlTextRowDataOutputPacket rowDataPacket = new MySqlTextRowDataOutputPacket(++sequenceId,objects);
                result.addPacket(rowDataPacket);
            }

            //eof
            result.addPacket(new MySqlEofOutputPacket(++sequenceId,0,StatusFlag.SERVER_STATUS_AUTOCOMMIT));

            return result;
        }
    }
}
