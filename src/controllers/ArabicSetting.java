package controllers;

public class ArabicSetting {

    public static String arabicToEnglish(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669) {
                ch -= 0x0660 - '0';
            } else if (ch >= 0x06f0 && ch <= 0x06F9) {
                ch -= 0x06f0 - '0';
            }
            chars[i] = ch;
        }
        return new String(chars);
    }

    public static boolean isProbablyArabic(String s) {
        boolean state = false;
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0) {
                state = true;
            } else {
                state = false;
            }

            i += Character.charCount(c);
        }
        return state;
    }

    public static String EnglishToarabic(String str) {
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                builder.append(arabicChars[(int) (str.charAt(i)) - 48]);
            } else {
                builder.append(str.charAt(i));
            }
        }
        return builder.toString();
    }

}
