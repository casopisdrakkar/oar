package sk.drakkar.oar;

import sk.drakkar.oar.pipeline.GlobalContextVariables;

import java.beans.Introspector;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ContextVariableUtils {
    public static Map<String, Object> constructModel(GlobalContextVariables.Variable<?> variable, Object value) {
        Map<String, Object> root = new HashMap<>();
        String contextVariableExpression = getContextVariableExpression(variable);
        try(Scanner scanner = new Scanner(contextVariableExpression).useDelimiter("\\.")) {
            Map<String, Object> map = root;
            while (scanner.hasNext()) {
                Map<String, Object> nestedMap = new HashMap<>();
                String next = scanner.next();
                if (!scanner.hasNext()) {
                    map.put(next, value);
                } else {
                    map.put(next, nestedMap);
                    map = nestedMap;

                }
            }
        }
        return root;
    }



    public static String getContextVariableExpression(GlobalContextVariables.Variable<?> variable) {
        String className = variable.getClass().getName();
        String[] nestedClasses = className.split("\\$");
        if(nestedClasses.length != 3) {
            throw new IllegalArgumentException("Bad naming convention for " + className);
        }
        String pluginName = nestedClasses[0];
        String capitalizedVariableName = nestedClasses[2].replace("Variable", "");
        String pluginNameWithoutPackage = getPluginNameWithoutPackage(pluginName);

        return Introspector.decapitalize(pluginNameWithoutPackage) + "." + Introspector.decapitalize(capitalizedVariableName);
    }

    private static String getPluginNameWithoutPackage(String pluginName) {
        String[] packageNames = pluginName.split("\\.");
        return packageNames[packageNames.length - 1];
    }



}
