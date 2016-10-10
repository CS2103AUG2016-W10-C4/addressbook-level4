package seedu.todo.logic.arguments;

import seedu.todo.commons.exceptions.IllegalValueException;

abstract public class Argument<T> implements Parameter {
    private String name;
    private String description;
    private String flag;
    private boolean optional = true;
    private boolean boundValue = false;
    
    protected T value;
    protected T defaultValue;
    
    private static final String REQUIRED_ERROR_FORMAT = "The %s parameter is required";
    private String requiredErrorMessage;
    
    public Argument(String name) {
        this.name = name;
    }
    
    public Argument(String name, T defaultValue) {
        this.name = name;
        this.value = defaultValue;
    }
    
    /**
     * Binds a value to this parameter. Implementing classes MUST override AND 
     * call the parent class function so that the dirty bit is set for required
     * parameter validation to work
     */
    @Override
    public void setValue(String input) throws IllegalValueException {
        boundValue = true;
    }
    
    public T getValue() {
        return value;
    }
    
    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Argument<T> description(String description) {
        this.description = description;
        return this;
    }

    public String getFlag() {
        return flag;
    }

    public Argument<T> flag(String flag) {
        this.flag = flag;
        return this;
    }

    public boolean isOptional() {
        return optional;
    }
    
    public boolean hasBoundValue() {
        return boundValue;
    }
    
    /**
     * Sets the field as required
     */
    public Argument<T> required() {
        this.optional = false;
        return this;
    }
    
    /**
     * Sets the field as required and specify an error message to show if it is not provided
     * @param errorMessage shown to the user when the parameter is not provided 
     */
    public Argument<T> required(String errorMessage) {
        this.optional = false;
        return this;
    }
    
    @Override
    public boolean isPositional() {
        return flag == null;
    }
    
    @Override
    public void checkRequired() throws IllegalValueException {
        if (!isOptional() && !hasBoundValue()) {
            String error = requiredErrorMessage == null ? 
                    String.format(Argument.REQUIRED_ERROR_FORMAT, name) : requiredErrorMessage;
            throw new IllegalValueException(error);
        }
    }
}
