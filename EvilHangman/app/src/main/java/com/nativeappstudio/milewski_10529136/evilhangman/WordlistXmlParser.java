package com.nativeappstudio.milewski_10529136.evilhangman;

import android.content.res.XmlResourceParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 3-12-2015.
 * Takes all the words from an xml file and stores them in an
 * arraylist which is returned.
 */
public class WordlistXmlParser {

    /**
     * Reads all the words from an xml file
     * @param parser    The parser which will read the file
     * @return          An arraylist with all the words from the file
     */
    public static List<String> parse(XmlResourceParser parser) throws XmlPullParserException, IOException {
        List<String> items = new ArrayList<>();

        parser.next();
        int type = parser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {
            //only handle elements from type text. The tags are not needed
            if (type == XmlPullParser.TEXT) {
                String word = parser.getText();
                items.add(word);
            }
            type = parser.next();
        }
        return items;
    }
}
