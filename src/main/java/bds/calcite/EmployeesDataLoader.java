package bds.calcite;

import bds.common.Logger;
import bds.vectors.DecimalVector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.math.BigDecimal;
import java.util.*;

/**
 * DataLoader that loads Data for the EmployeesTable custom table.
 */
public class EmployeesDataLoader implements IDataLoader {
    private static final Random random = new Random();

    /**
     * Method to load table data into dataMap using the passed Mapper.
     * @param mapper instance of ObjectMapper to store data into dataMap.
     * @param dataMap the dataMap where data needs to be stored.
     */
    public void loadData(ObjectMapper mapper, Map<Object, ObjectNode> dataMap) {
        createNewEmployees(dataMap, mapper);
    }

    private static void createNewEmployees(Map<Object, ObjectNode> employees, ObjectMapper mapper) {

        String firstNameLastNamesListString = "Karter Dennis | Maisie Franco | Gage Wilkins | Amalia Reilly | Alvaro Barton | " +
                "Danna McGee | Conner Fuller | Oakley Parks | Gianni Andrade | Emmy Schmitt | Murphy Knox | Kallie Walls " +
                "| Larry Dejesus | Julissa Quinn | Rhys Richards | Trinity Mack | Esteban Holt | Adelynn McBride | Denver Beasley " +
                "| Jaylah Hartman | Baker Mayo | Aarya Martinez | Alexander Gilbert | Jocelyn Benton | Jamal Robles | Felicity Rowland |" +
                " Eliezer Fowler | Lennon Erickson | Johnny Finley | Jovie Mathis | Gustavo Waters | Bristol Leal | Cedric Huang | Francesca " +
                "Cunningham | Alejandro Zimmerman  | Ariyah Morrison | Maximus Ayala | Blair Hernandez | Mason McCullough | Hana Zhang | Isaias Stokes" +
                " | Miranda Hutchinson | Korbin Caldwell | Evelynn Kramer | Kylan Reilly | Tori Neal | Eddie Butler | Athena Liu | Pedro Rich | Sunny Goodman";

        String[] stringListItem = firstNameLastNamesListString.split("\\|");

        int index = 1;
        for(String it : stringListItem) {
            String name = it.trim();
            String[] nameParts = name.split(" ");
            String firstName = nameParts[0].trim();
            String lastName = nameParts[1].trim();
            String email = firstName + "." + lastName + "@gmail.com";
            int age = 20 + random.nextInt(50);
            double vectorBound = 20.20165424;
            employees.put((long) index, mapper.createObjectNode()
                    .put("firstname", firstName)
                    .put("lastname", lastName)
                    .put("email", email.toLowerCase())
                    .put("age", age)
                    .put("vec",
                            decimalVector(
                                    String.valueOf(random.nextDouble(vectorBound)),
                                    String.valueOf(random.nextDouble(vectorBound)),
                                    String.valueOf(random.nextDouble(vectorBound)),
                                    String.valueOf(random.nextDouble(vectorBound))
                    )));
            index++;
        }
    }

    private static String decimalVector(String... list) {
        List<BigDecimal> decimalsList = new ArrayList<>();
        for (String strVal : list) {
            decimalsList.add(new BigDecimal(strVal));
        }
        String v = new DecimalVector(decimalsList).toString();
        Logger.getInstance().debug("vector = " + v, "DataLoader");
        return v;
    }
}
