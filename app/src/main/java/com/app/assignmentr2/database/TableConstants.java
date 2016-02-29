package com.app.assignmentr2.database;


/**
 * Created by Govind on 28-02-2016.
 */
public class TableConstants {
	public interface WordsColumn {
		String ID = "_id";
		String WID = "wid"; // word id
		String WORD = "word";
		String VARIANT = "variant";
		String MEANING = "meaning";
		String ASPECT_RATIO = "aspect_ratio";
		String IMAGE_URL = "image_url";
		String IMAGE_FEED = "image_feed";
	}
	
	public interface Tables {
		String WORDS = "words";
	}
	
	public static final int TABLE_WORD = 1;
}
