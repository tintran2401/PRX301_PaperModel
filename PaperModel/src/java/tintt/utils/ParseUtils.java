/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author NhanTT
 */
public class ParseUtils {
    /**
     * extract number from text contains number
     * @param text
     * @return found number, or 0 if no number found
     */
    public static Integer extractNumber(String text) {
        String regex = "[0-9]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String token = matcher.group();
            try {
                int number = Integer.parseInt(token);
                return number;
            } catch (NumberFormatException e) {
                Logger.getLogger(ParseUtils.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return 0;
    }
}
