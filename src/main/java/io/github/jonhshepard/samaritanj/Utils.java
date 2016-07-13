package io.github.jonhshepard.samaritanj;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * @author JonhSHEPARD
 */
class Utils {

    static String removeAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}
