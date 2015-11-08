package sk.drakkar.oar.pipeline;

import sk.drakkar.oar.ContextVariableUtils;
import sk.drakkar.oar.pipeline.GlobalContextVariables.Variable;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Context {
    private Map<Variable<? extends Object>, Object> internalContext = new HashMap<>();

    public Context() {
        // empty constructor
    }

    public static Context of(Variable<? extends Object> variable, Object value) {
        return new Context(variable, value);
    }

    public Context andOf(Variable<? extends Object> variable, Object value) {
        this.internalContext.put(variable, value);
        return this;
    }

    public Context(Variable<? extends Object> variable, Object value) {
        this.internalContext.put(variable, value);
    }

    public Set<Entry<Variable<? extends Object>, Object>> entrySet() {
        return this.internalContext.entrySet();
    }

    public Object put(Variable<? extends Object> variable, Object value) {
        return internalContext.put(variable, value);
    }

    public <T> T get(Variable<T> variable) {
        return (T) internalContext.get(variable);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry<Variable<? extends Object>, Object> entry : internalContext.entrySet()) {
            Variable<?> variable = entry.getKey();
            Object value = entry.getValue();

            sb.append(ContextVariableUtils.getVariableName(variable))
                    .append("=")
                    .append(value);
        }
        return sb.toString();
    }
}
