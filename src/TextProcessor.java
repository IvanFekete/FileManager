import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TextProcessor {


    private static Boolean isWordSymbol(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' || c == '\'';
    }

    public static Boolean isCorrectTagNameLetter(char c) {
        return c == '/' || Character.isLetter(c);
    }


    public static Boolean isImageLink(String link) {
        String[] formats = {"jpg", "jpeg", ".gif", "png", "bmp", "ico"};
        for(String format : formats) {
            if(link.length() > format.length() &&
                    link.substring(link.length() - format.length()).equals(format)) {
                return true;
            }
        }
        return false;
    }


    public static ArrayList<String> getWords(String text) {
        ArrayList<String> words = new ArrayList<>();
        String curWord = "";
        for(int i = 0; i < text.length(); i++) {
            if(isWordSymbol(text.charAt(i))) {
                curWord += text.charAt(i);
            }
            else {
                if(!curWord.equals("")) {
                    words.add(curWord.toLowerCase());
                    curWord = "";
                }
            }
        }
        if(!curWord.equals("")) {
            words.add(curWord.toLowerCase());
        }
        return words;
    }



    public static List<Tag> searchHtmlTags(String text) {
        TreeSet<String> openTags = new TreeSet<>(), closeTags = new TreeSet<>();
        for(int i = 0; i < text.length();) {
            if(text.charAt(i) == '<') {
                String word = "";
                i++;
                while(i < text.length() && isCorrectTagNameLetter(text.charAt(i))) {
                    word += text.charAt(i);
                    i++;
                }
                while(i < text.length() && text.charAt(i) != '>') {
                    i++;
                }
                if(i < text.length()) {
                    if(word.length() > 0) {
                        if(word.charAt(0) != '/') {
                            openTags.add(word);
                        }
                        else {
                            closeTags.add(word);
                        }
                    }
                    i++;
                }
            }
            else {
                i++;
            }
        }
        List<Tag> result = new ArrayList<>();
        for(String tag : openTags) {
            System.out.println(tag);
            result.add(new Tag(tag, closeTags.contains("/" + tag)));
        }
        return result;
    }


    public static String[] searchImagesLink(String text) {
        TreeSet<String> links = new TreeSet<>();
        for(int i = 0; i < text.length() - 5;) {
            if(text.substring(i, i + 6).equals("href=\"")) {
                i += 6;
                String link = "";
                while(i < text.length() && text.charAt(i) != '"') {
                    link += text.charAt(i);
                    i++;
                }
                i++;
                if(link.length() != 0 && isImageLink(link)) {
                    links.add(link);
                }
            }
            else {
                i++;
            }
        }
        Object[] array = links.toArray();
        String[] result = new String[array.length];
        for(int i = 0; i < result.length; i++) {
            result[i] = array[i].toString();
        }
        return result;

    }



    public static TreeSet<String> searchSimilar(String text, String prefix) {
        System.out.println("Searching for:" + prefix);
        ArrayList<String> words = getWords(text);
        int[] similarity = new int[words.size()];
        for(int j = 0; j < words.size(); j++) {
            for(int i = 0; i < Math.min(prefix.length(), words.get(j).length()); i++) {
                if(prefix.charAt(i) != words.get(j).charAt(i)) break;
                else similarity[j]++;
            }
            System.out.println(words.get(j) + ": " + similarity[j]);
        }
        int simMax = 0;
        for(int i = 1; i < words.size(); i++) {
            simMax = Math.max(simMax, similarity[i]);
        }
        TreeSet<String> result = new TreeSet<>();
        for(int i = 0; i < words.size(); i++) {
            if(similarity[i] == simMax) {
                result.add(words.get(i));
            }
        }

        return result;
    }

    public static String getReplaced(String text, String initTag, String replaceTag) {
        String newText = "";
        for(int i = 0; i < text.length();) {
            if(text.charAt(i) == '<') {
                newText += "<";
                i++;
                if(i < text.length()) {
                    if(text.charAt(i) == '/') {
                        newText += "/";
                        i++;
                    }

                    if(i + initTag.length() - 1 < text.length()) {
                        System.out.println(text.substring(i, i + initTag.length() - 1));
                        if (text.substring(i, i + initTag.length()).equals(initTag)) {

                            newText += replaceTag;
                            i += initTag.length();
                        } else {
                            newText += text.charAt(i);
                            i++;
                        }
                    }
                }
            }
            else {
                newText += text.charAt(i);
                i++;
            }
        }
        return newText;
    }
}
