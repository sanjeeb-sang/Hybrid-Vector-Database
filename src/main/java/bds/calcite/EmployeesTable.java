package bds.calcite;

import org.apache.calcite.sql.type.SqlTypeName;
import java.util.List;

/**
 * This EmployeesTable extends abstract BaseCustomTable class helps create a new Employees Table in Calcite.
 */
public class EmployeesTable extends BaseCustomTable {

    /**
     * Adding the Names of all the Table fields to names map, and the Type of all Fields of the Table
     * to the types map.
     * @param names List where names of the Table Fields need to be added.
     * @param types List where Types of the Table Fields need to be added.
     */
    @Override
    public void addTableFieldNamesAndTypes(List<String> names, List<SqlTypeName> types) {
        names.add("id");
        names.add("firstname");
        names.add("lastname");
        names.add("email");
        names.add("age");
        names.add("vec");

        types.add(SqlTypeName.BIGINT);
        types.add(SqlTypeName.VARCHAR);
        types.add(SqlTypeName.VARCHAR);
        types.add(SqlTypeName.VARCHAR);
        types.add(SqlTypeName.INTEGER);
        types.add(SqlTypeName.ANY);
    }

    @Override
    public IDataLoader getDataLoader() {
        return new EmployeesDataLoader();
    }

    @Override
    public String getTableName() {
        return "employees";
    }
}
