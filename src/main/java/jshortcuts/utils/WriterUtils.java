package jshortcuts.utils;

public final class WriterUtils {

    public static String formatEmbeddedStruct(Object obj) {
        if(obj == null) {
            return "{}";
        }

        var result = new StringBuilder("{\n");

        convertStructToEmbeddedStringForm(obj, "    ", result);

        return result.append("    }").toString();
    }

    public static String formatEmbeddedStructs(Object[] objs) {
        if(objs.length == 0) {
            return "[]";
        }

        var result = new StringBuilder("[\n");
        var objCount = objs.length;

        for(var i = 0; i < objCount; ++i) {
            result.append("        {\n");
            convertStructToEmbeddedStringForm(objs[i], "        ", result);
            result.append("        }");

            if(i != objCount - 1) {
                result.append(',');
            }

            result.append('\n');
        }

        return result.append("    ]").toString();
    }

    public static String toZeroPaddedHexString(long value, int charCount) {
        var hexForm = Long.toHexString(value);

        return "0".repeat(charCount - hexForm.length()) + hexForm;
    }

    private static void convertStructToEmbeddedStringForm(Object obj, String padding, StringBuilder builder) {
        var lines = obj.toString().split("\n");

        for(var i = 1; i < lines.length - 1; ++i) {
            builder.append(padding + lines[i] + '\n');
        }
    }
}