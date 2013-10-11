package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.dao.factory.HrcmsDAOFactory;
import com.hrcms.server.model.DictItem;
import com.hrcms.server.model.TableColumn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

public class DictItemDAO extends baseDAO {
    public List<DictItem> getDictItemList(String dictName) throws Exception {
        TableColumnDAO dDAO = HrcmsDAOFactory.getTableColumnDAO();
        StringBuilder b = new StringBuilder();
        final List<TableColumn> l = dDAO.getColumnList(dictName);
        for (TableColumn c : l) {
            if (b.length() > 0) {
                b.append(',');
            }
            b.append("t.").append(c.columnName);
        }
        final List<DictItem> pList = new ArrayList<DictItem>();
        jdbcTemplate.query(String.format(sqlManager.getSQL("DictItemDAO/sqlDictItems"), b.toString(), dictName), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createDynamicDictEntity(l, rs));
            }
        });
        return pList;
    }
}
