package com.nativeappstudio.milewski_10529136.evilhangman;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victo on 3-12-2015.
 */
public class WordlistXmlParser {

    public static List<String> parse(XmlResourceParser parser) throws XmlPullParserException, IOException {
        List<String> items = new ArrayList<>();

        parser.next();
        int type = parser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {
            if (type == XmlPullParser.TEXT) {
                String word = parser.getText();
                items.add(word);
            }
            type = parser.next();
        }
        return items;
    }
}
