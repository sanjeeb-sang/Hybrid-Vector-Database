package bds.calcite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.*;
import org.apache.calcite.schema.Function;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.apache.calcite.schema.impl.ScalarFunctionImpl;
import bds.vectors.VectorFunctions;
import java.util.*;

/**
 * Schema for Calcite that contains all the Custom Tables and Custom Functions.
 */
public class CustomSchema extends AbstractSchema {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Map<String, Map<Object, ObjectNode>> tableLoadedValuesMap = new HashMap<>();
    private static final List<BaseCustomTable> _allTables = getAllTables();

    static {
        loadAllTablesData(_allTables);
    }

    /**
     * Method to get a map of all available Custom Tables.
     */
    @Override
    protected Map<String, Table> getTableMap() {
        HashMap<String, Table> allTablesMap = new HashMap<>();
        for (BaseCustomTable table : _allTables) {
            table.setData(tableLoadedValuesMap.get(table.getTableName()));
            allTablesMap.put(table.getTableName(), table);
        }
        return allTablesMap;
    }

    /**
     * List of all Custom Tables. If you need to add a new CustomTable, then just add a new instance of that
     * CustomTable to this List. For instance, if you want to add a new StudentsTable CustomTable, then you
     * will need to add 'new StudentTable()' to the end of the List returned below.
     * @return list of all Custom Tables.
     */
    protected static List<BaseCustomTable> getAllTables() {
        return Arrays.asList(new EmployeesTable());
    }

    /**
     * Method to get a Multimap of all the available Custom Functions.
     * @return Multimap of all the available custom Functions.
     */
    @Override
    protected Multimap<String, Function> getFunctionMultimap() {
        Multimap<String, Function> functionMap = ArrayListMultimap.create();
        functionMap.put("SUM_ELEMENTS", ScalarFunctionImpl.create(VectorFunctions.class, "sumElements"));
        functionMap.put("SIMILARITY", ScalarFunctionImpl.create(VectorFunctions.class, "similarity"));
        functionMap.put("COSINE_SIMILARITY", ScalarFunctionImpl.create(VectorFunctions.class, "cosineSimilarity"));

        return functionMap;
    }

    /**
     * Method to load Tables Data.
     */
    private static void loadAllTablesData(List<BaseCustomTable> tables) {
        if (tables == null) return;
        for (BaseCustomTable table : tables) {
            tableLoadedValuesMap.put(table.getTableName(), loadTableData(table.getDataLoader()));
        }
    }

    /**
     * Method to load a single table's data using the passed IDataLoader implementation.
     * @param dataLoader the IDataLoader implementation to be used to load data.
     * @return HashMap containing the loaded data.
     */
    private static HashMap<Object, ObjectNode> loadTableData(IDataLoader dataLoader) {
        HashMap<Object, ObjectNode> dataMap = new HashMap<>();
        dataLoader.loadData(mapper, dataMap);
        return dataMap;
    }
}