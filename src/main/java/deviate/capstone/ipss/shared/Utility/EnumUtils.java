package deviate.capstone.ipss.shared.Utility;

import jakarta.validation.ValidationException;

public class EnumUtils {
    public static <T extends Enum<T>> T parseEnum(Class<T> enumClass, String value, String field) {
        try {
            return Enum.valueOf(enumClass, value);
        } catch(IllegalArgumentException e) {
            throw new ValidationException("INVALID " + field + " VALUE " + value);
        }
    }
}
