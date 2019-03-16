package ru.test.utils;

/**
 * Типы свойств настроек
 */
public enum PropertiesType {

    USE_DB("ru.test.use.db", Boolean.class),

    DB_TYPE("ru.test.db.type", DBTypes.class),

    DB_INTERACTION_TYPE("ru.test.db.interaction", DBInteratcionType.class),

    USE_CHAT("ru.test.use.chat", Boolean.class);

    private String propertyName;

    private Class<?> typeValue;

    PropertiesType(String propertyName, Class<?> typeValue) {
        this.propertyName = propertyName;
        this.typeValue = typeValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Class<?> getTypeValue() {
        return typeValue;
    }
}
