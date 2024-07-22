package bds.calcite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Map;

/**
 * Interface for a DataLoader that loads data for a specific Custom Table like EmployeesTable.
 */
public interface IDataLoader {
    /**
     * Method to load table data into dataMap using the passed Mapper.
     * @param mapper instance of ObjectMapper to store data into dataMap.
     * @param dataMap the dataMap where data needs to be stored.
     */
    void loadData(ObjectMapper mapper, Map<Object, ObjectNode> dataMap);
}
