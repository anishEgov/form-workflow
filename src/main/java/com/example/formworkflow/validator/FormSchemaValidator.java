package com.example.formworkflow.validator;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FormSchemaValidator {

    @SuppressWarnings("unchecked")
    public void validate(Map<String, Object> schema, Map<String, Object> data) {
        if (schema == null || data == null) {
            throw new IllegalArgumentException("Schema or data cannot be null");
        }

        List<Map<String, Object>> fields = (List<Map<String, Object>>) schema.get("fields");

        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Schema must contain a non-empty 'fields' array");
        }

        for (Map<String, Object> field : fields) {
            String name = (String) field.get("name");
            String type = (String) field.get("type");
            boolean required = Boolean.TRUE.equals(field.get("required"));

            if (name == null || type == null) {
                throw new IllegalArgumentException("Each field must have 'name' and 'type'");
            }

            Object value = data.get(name);

            if (required && (value == null || value.toString().trim().isEmpty())) {
                throw new IllegalArgumentException("Required field '" + name + "' is missing or empty");
            }

            if (value != null) {
                validateType(name, type, value);
            }
        }
    }

    private void validateType(String name, String type, Object value) {
        switch (type) {
            case "text":
                if (!(value instanceof String)) {
                    throw new IllegalArgumentException("Field '" + name + "' must be a string");
                }
                break;
            case "date":
                if (!(value instanceof String) || !value.toString().matches("\\d{4}-\\d{2}-\\d{2}")) {
                    throw new IllegalArgumentException("Field '" + name + "' must be a date (YYYY-MM-DD)");
                }
                break;
            case "number":
                if (!(value instanceof Number)) {
                    throw new IllegalArgumentException("Field '" + name + "' must be a number");
                }
                break;
            case "boolean":
                if (!(value instanceof Boolean)) {
                    throw new IllegalArgumentException("Field '" + name + "' must be a boolean");
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported field type '" + type + "' for field '" + name + "'");
        }
    }
}
