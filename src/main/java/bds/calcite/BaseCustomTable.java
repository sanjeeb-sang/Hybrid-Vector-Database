package bds.calcite;

import bds.common.Logger;
import bds.common.StreamIterable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;
import bds.vectors.DecimalVector;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * BaseCustomTable class that can be subclassed to create a Custom Table like the Employees Table.
 */
public abstract class BaseCustomTable extends AbstractTable implements ScannableTable {

    private Map<Object, ObjectNode> data;
    private List<String> fieldNames;
    private static final Logger logger = Logger.getInstance();
    private List<SqlTypeName> fieldTypes;

    /**
     * Method to set the initial data in this custom table.
     */
    public void setData(Map<Object, ObjectNode> data) {
        this.data = data;

        List<String> names = new ArrayList<>();
        List<SqlTypeName> types = new ArrayList<>();

        this.addTableFieldNamesAndTypes(names, types);
        this.fieldNames = names;
        this.fieldTypes = types;
    }

    /**
     * Method to get the Row Type.
     */
    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {

        List<RelDataType> types = fieldTypes.stream().map(typeFactory::createSqlType).collect(Collectors.toList());
        return typeFactory.createStructType(types, fieldNames);
    }

    public abstract IDataLoader getDataLoader();

    public abstract String getTableName();

    /**
     * Method to scan the table.
     * @param root DataContext instance.
     * @return Scanned Enumerable instance.
     */
    @Override
    public Enumerable<Object[]> scan(DataContext root) {
        Stream<Object[]> dataStream = data.entrySet().stream().map(this::toObjectArray);
        return Linq4j.asEnumerable(new StreamIterable<>(dataStream));
    }

    /**
     * Abstract method that needs to be implemented by the subclass.
     * @param names List where names of the Table Fields need to be added.
     * @param types List where Types of the Table Fields need to be added.
     */
    public abstract void addTableFieldNamesAndTypes(List<String> names, List<SqlTypeName> types);

    /**
     * Method to convert Calcite Table Entry into an Object like String, Integer, Double, BigDecimal or
     * DecimalVector.
     * @param item the item which needs to be converted to a specific object.
     * @return Object parsed from the passed item.
     */
    private Object[] toObjectArray(Map.Entry<Object, ObjectNode> item) {

        Object[] res = new Object[fieldNames.size()];
        res[0] = item.getKey();

        for (int i = 1; i < fieldNames.size(); i++) {
            JsonNode v = item.getValue().get(fieldNames.get(i));
            SqlTypeName type = fieldTypes.get(i);
            logger.debug("toObjectArray: " + type.toString(), "CustomTable");
            switch (type) {
                case VARCHAR:
                    res[i] = v.textValue();
                    break;
                case INTEGER:
                    res[i] = v.intValue();
                    break;
                case ANY:
                    res[i] = DecimalVector.fromString(v.asText());
                    break;
                default:
                    logger.debug("unsupported sql type: " + type, "CustomTable");
            }
        }
        return res;
    }
}